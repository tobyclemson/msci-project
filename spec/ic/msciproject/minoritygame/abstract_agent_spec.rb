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

  let(:agent_memory_size) { 2 }

  let(:choice_history) { package::ChoiceHistory.new(agent_memory_size) }
  let(:mock_strategy_manager) { 
    Mockito.mock(package::StrategyManager.java_class)
  }
  
  let(:a_a) {
    key = Java::JavaUtil::ArrayList.new
    key.add(package::Choice::A)
    key.add(package::Choice::A)
    key
  }
  let(:a_b) {
    key = Java::JavaUtil::ArrayList.new
    key.add(package::Choice::A)
    key.add(package::Choice::B)
    key
  }
  let(:b_a) {
    key = Java::JavaUtil::ArrayList.new
    key.add(package::Choice::B)
    key.add(package::Choice::A)
    key
  }
  let(:b_b) {
    key = Java::JavaUtil::ArrayList.new
    key.add(package::Choice::B)
    key.add(package::Choice::B)
    key
  }
 
  let(:map_to_build_strategy) {
    map = Java::JavaUtil::HashMap.new
    map.put(a_a, package::Choice::A); 
    map.put(a_b, package::Choice::B) 
    map.put(b_a, package::Choice::A); 
    map.put(b_b, package::Choice::B)
    map
  }

  let(:strategy) { package::Strategy.new(map_to_build_strategy) }
  let(:strategy_manager) {
    list = Java::JavaUtil::ArrayList.new
    list.add(strategy)
    package::StrategyManager.new(list)
  }
  
  let(:choice_memory) {
    package::ChoiceMemory.new(choice_history, agent_memory_size)
  }
  
  let(:abstract_agent_instance) { 
    klass.new(mock_strategy_manager, choice_memory) 
  }
  
  before(:each) do
    Mockito.when(mock_strategy_manager.strategy_key_length).
      then_return(Java::JavaLang::Integer.new(agent_memory_size))
  end
  
  describe "public interface" do
    it "has a strategies instance method" do
      abstract_agent_instance.should respond_to(:strategies)
    end
    
    it "has a strategy_manager instance method" do
      abstract_agent_instance.should respond_to(:strategy_manager)
    end
    
    it "has a score instance method" do
      abstract_agent_instance.should respond_to(:score)
    end
    
    it "has a memory instance method" do
      abstract_agent_instance.should respond_to(:memory)
    end
    
    it "has a choice instance method" do
      abstract_agent_instance.should respond_to(:choice)
    end
    
    it "has a choose instance method" do
      abstract_agent_instance.should respond_to(:choose)
    end
    
    it "has an increment_score instance method" do
      abstract_agent_instance.should respond_to(:increment_score)
    end
    
    it "has an update instance method" do
      abstract_agent_instance.should respond_to(:update)
    end
  end
  
  describe "constructor" do
    describe "with strategy manager and memory arguments" do
      it "sets the strategy_manager attribute to the supplied strategy manager" do
        abstract_agent = klass.new(strategy_manager, choice_memory)
        abstract_agent.strategy_manager.should == strategy_manager
      end
      
      it "set the memory attribute to the supplied choice memory" do
        abstract_agent = klass.new(strategy_manager, choice_memory)
        abstract_agent.memory.should == choice_memory
      end
      
      it "throws an IllegalArgumentError if the strategy key lengths are " + 
        "not equal to the memory length" do
        choice_history = package::ChoiceHistory.new(8)
        choice_memory = package::ChoiceMemory.new(choice_history, 8)
        
        strategies = Java::JavaUtil::ArrayList.new
        3.times { strategies.add(strategy) }
        
        strategy_manager = package::StrategyManager.new(strategies)
        
        expect {
          klass.new(strategy_manager, choice_memory)
        }.to raise_error
      end
    end
  end
  
  describe "#strategies" do
    it "returns the list of strategies stored in the strategy manager" do
      abstract_agent = klass.new(strategy_manager, choice_memory)
      abstract_agent.strategies.should == strategy_manager.strategies
    end
  end
  
  describe "#choice" do
    it "throws an IllegalStateException if no choice has been made yet" do
      abstract_agent = klass.new(strategy_manager, choice_memory)
      expect {
        abstract_agent.get_choice
      }.to raise_error(Java::JavaLang::IllegalStateException)
    end
    
    it "doesn't throw an IllegalStateException if a choice has been made" do
      abstract_agent = klass.new(strategy_manager, choice_memory)
      abstract_agent.choose(choice_history.as_list(2))
      expect {
        abstract_agent.get_choice
      }.to_not raise_error(Java::JavaLang::IllegalStateException)
    end
  end
end