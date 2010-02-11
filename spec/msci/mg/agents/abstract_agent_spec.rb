require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSci::MG::Agents::AbstractAgent do
  let(:package) { MSci::MG }
  let(:klass) { 
    Class.new(package::Agents::AbstractAgent) do
      field_accessor :choice, :prediction
      
      def choose(*args)
        self.choice = MSci::MG::Choice::A
      end
      
      def prepare
        self.prediction = MSci::MG::Choice::A
      end
    end
  }
  
  let(:integer) { Java::JavaLang::Integer }
  let(:array_list) { Java::JavaUtil::ArrayList }
  let(:graph) { Java::EduUciIcsJungGraph::Graph }

  let(:strategy_manager) { Mockito.mock(package::StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(package::ChoiceMemory.java_class) }
  
  let(:neighbourhood) { Mockito.mock(package::Neighbourhood.java_class)}
  
  let(:abstract_agent) { klass.new(strategy_manager, choice_memory) }
  
  describe "public interface" do
    it "has a strategies instance method" do
      abstract_agent.should respond_to(:strategies)
    end
    
    it "has a strategy_manager instance method" do
      abstract_agent.should respond_to(:strategy_manager)
    end
    
    it "has a score instance method" do
      abstract_agent.should respond_to(:score)
    end
    
    it "has a memory instance method" do
      abstract_agent.should respond_to(:memory)
    end
    
    it "has a prediction instance method" do
      abstract_agent.should respond_to(:prediction)
    end
    
    it "has a correct_prediction_count instance method" do
      abstract_agent.should respond_to(:correct_prediction_count)
    end
    
    it "has a choice instance method" do
      abstract_agent.should respond_to(:choice)
    end
    
    it "has a social_network instance method" do
      abstract_agent.should respond_to(:social_network)
    end
    
    it "has a neighbourhood method" do
      abstract_agent.should respond_to(:neighbourhood)
    end
    
    it "has a neighbourhood= method" do
      abstract_agent.should respond_to(:neighbourhood=)
    end
    
    it "has a friends instance method" do
      abstract_agent.should respond_to(:friends)
    end
    
    it "has a best_friend instance method" do
      abstract_agent.should respond_to(:best_friend)
    end
    
    it "has a prepare instance method" do
      abstract_agent.should respond_to(:prepare)
    end
    
    it "has a choose instance method" do
      abstract_agent.should respond_to(:choose)
    end
    
    it "has an increment_score instance method" do
      abstract_agent.should respond_to(:increment_score)
    end
    
    it "has an increment_correct_prediction_count method" do
      abstract_agent.should respond_to(:increment_correct_prediction_count)
    end
    
    it "has an update instance method" do
      abstract_agent.should respond_to(:update)
    end
    
    it "has an identification_number instance method" do
      abstract_agent.should respond_to(:identification_number)
    end
    
    it "has a compare_to method" do
      abstract_agent.should respond_to(:compare_to)
    end
  end
  
  describe "constructor" do
    describe "with strategy manager and memory arguments" do
      it "sets the strategy_manager attribute to the supplied strategy " + 
        "manager" do
        abstract_agent.strategy_manager.should == strategy_manager
      end
      
      it "set the memory attribute to the supplied choice memory" do
        abstract_agent.memory.should == choice_memory
      end
      
      it "throws an IllegalArgumentError if the strategy key lengths are " + 
        "not equal to the memory capacity" do
        Mockito.when(strategy_manager.get_strategy_key_length).
          then_return(integer.new(3))
        Mockito.when(choice_memory.get_capacity).
          then_return(integer.new(2))
        
        expect {
          klass.new(strategy_manager, choice_memory)
        }.to raise_error
      end
      
      it "sets the id attribute to a unique integer" do
        agent_ids = (1..10).collect do
          abstract_agent = klass.new(strategy_manager, choice_memory)
          abstract_agent.identification_number
        end
        
        agent_ids.uniq.size.should == 10
      end
    end
  end
  
  describe "setters" do
    describe "#neighbourhood" do
      it "sets the neighbourhood attribute to the supplied object" do
        new_neighbourhood = Mockito.mock(package::Neighbourhood.java_class)
        abstract_agent.neighbourhood = new_neighbourhood
        abstract_agent.neighbourhood.should == new_neighbourhood
      end
    end
  end
  
  describe "#friends" do
    it "returns the result of calling friends on the supplied " + 
      "neighbourhood" do
      friends = Mockito.mock(array_list.java_class)
      Mockito.when(neighbourhood.friends).then_return(friends)
      
      abstract_agent.neighbourhood = neighbourhood
      
      abstract_agent.friends.should == friends
    end
    
    it "throws an IllegalStateException if no neighbourhood has been " +
      "set" do
      abstract_agent.neighbourhood = nil
      expect {
        abstract_agent.friends
      }.to raise_error(Java::JavaLang::IllegalStateException)
    end
  end
  
  describe "#best_friend" do
    it "returns this agent itself" do
      abstract_agent.best_friend.should == abstract_agent
    end
  end
  
  describe "#social_network" do
    it "returns the result of calling social_network on the supplied " +
      "neighbourhood" do
      social_network = Mockito.mock(graph.java_class)
      Mockito.when(neighbourhood.social_network).then_return(social_network)
      
      abstract_agent.neighbourhood = neighbourhood
      
      abstract_agent.social_network.should == social_network
    end
    
    it "throws an IllegalStateException if no neighbourhood has been " +
      "set" do
      abstract_agent.neighbourhood = nil
      expect {
        abstract_agent.social_network
      }.to raise_error(Java::JavaLang::IllegalStateException)
    end
  end
  
  describe "#compare_to" do
    # agents are assigned id numbers sequentially so the later an agent is
    # created, the higher its id
    
    it "returns a negative integer if the identification_number attribute " + 
      "of the supplied agent is greater than this agent's " + 
      "identification_number attribute" do
      agent_1 = klass.new(strategy_manager, choice_memory)
      agent_2 = klass.new(strategy_manager, choice_memory)
      
      agent_1.compare_to(agent_2).should be < 0
    end
    
    it "returns zero if the identification_number attribute of the " + 
      "supplied agent is equal to this agent's identification_number " +
      "attribute" do
      agent = klass.new(strategy_manager, choice_memory)
      agent.compare_to(agent).should == 0
    end
    
    it "returns a positive integer if the identification_number attribute " + 
      "of the supplied agent is smaller than this agent's " + 
      "identification_number attribute" do
      agent_1 = klass.new(strategy_manager, choice_memory)
      agent_2 = klass.new(strategy_manager, choice_memory)
      
      agent_2.compare_to(agent_1).should be > 0
    end
  end
  
  describe "#strategies" do
    it "returns the list of strategies stored in the strategy manager" do
      mock_strategies = Mockito.mock(array_list.java_class)
      
      Mockito.when(strategy_manager.get_strategies).
        then_return(mock_strategies)
      
      abstract_agent.strategies.should == strategy_manager.strategies
    end
  end
  
  describe "#choice" do
    it "throws an IllegalStateException if no choice has been made yet" do
      expect {
        abstract_agent.get_choice
      }.to raise_error(Java::JavaLang::IllegalStateException)
    end
    
    it "doesn't throw an IllegalStateException if a choice has been made" do
      abstract_agent.choose
      expect {
        abstract_agent.get_choice
      }.to_not raise_error(Java::JavaLang::IllegalStateException)
    end
  end
  
  describe "#prediction" do
    it "throws an IllegalStateException if no prediction has been made yet" do
      expect {
        abstract_agent.get_prediction
      }.to raise_error(Java::JavaLang::IllegalStateException)
    end
    
    it "doesn't throw an IllegalStateException if a prediction has been " + 
      "made" do
      abstract_agent.prepare
      expect {
        abstract_agent.get_prediction
      }.to_not raise_error(Java::JavaLang::IllegalStateException)
    end
  end
  
  describe "#increment_score" do
    it "increases the agent's score by 1" do
      expect {
        abstract_agent.increment_score
      }.to change(abstract_agent, :score).by(+1)
    end
  end
  
  describe "#increment_correct_prediction_count" do
    it "increases the agent's correct prediction count by 1" do
      expect {
        abstract_agent.increment_correct_prediction_count
      }.to change(abstract_agent, :correct_prediction_count).by(+1)
    end
  end
  
  describe "#prepare" do
    it "does nothing" do
      # at least not that I can think of yet...
    end
  end
  
  describe "#update" do
    it "increments the score if the minority choice is equal to the " +
      "current choice" do
      expect {
        abstract_agent.choose
        abstract_agent.update(abstract_agent.choice)
      }.to change(abstract_agent, :score).by(+1)
    end
    
    it "doesn't increment the score if the minority choice is not equal " + 
      "to the current choice" do
      expect {
        abstract_agent.choose
        minority_choice = if(abstract_agent.choice == package::Choice::A)
          package::Choice::B
        else
          package::Choice::A
        end
        abstract_agent.update(minority_choice)
      }.to_not change(abstract_agent, :score)
    end
  end
end