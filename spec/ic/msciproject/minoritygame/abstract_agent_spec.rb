require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AbstractAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { 
    Class.new(package::AbstractAgent) do
      field_accessor :choice
      
      def choose(*args)
        self.choice = MSciProject::MinorityGame::Choice::A
      end
    end
  }
  
  let(:integer) { Java::JavaLang::Integer }
  let(:array_list) { Java::JavaUtil::ArrayList }

  let(:strategy_manager) { Mockito.mock(package::StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(package::ChoiceMemory.java_class) }
  
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
    
    it "has a choice instance method" do
      abstract_agent.should respond_to(:choice)
    end
    
    it "has a social_network instance method" do
      abstract_agent.should respond_to(:social_network)
    end
    
    it "has a social_network= instance method" do
      abstract_agent.should respond_to(:social_network=)
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
    describe "#social_network=" do
      it "sets the social_network attribute to the supplied network" do
        new_social_network = Mockito.mock(
          Java::EduUciIcsJungGraph::SparseGraph.java_class
        )
        abstract_agent.social_network = new_social_network
        abstract_agent.social_network.should == new_social_network
      end
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
  
  describe "#increment_score" do
    it "increases the agent's score by 1" do
      expect {
        abstract_agent.increment_score
      }.to change(abstract_agent, :score).by(+1)
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
      }.to change(abstract_agent, :score)
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