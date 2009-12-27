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
    
    it "has a choose instance method" do
      abstract_agent.should respond_to(:choose)
    end
    
    it "has an increment_score instance method" do
      abstract_agent.should respond_to(:increment_score)
    end
    
    it "has an update instance method" do
      abstract_agent.should respond_to(:update)
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
end