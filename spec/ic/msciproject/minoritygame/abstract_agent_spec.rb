require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AbstractAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { 
    Class.new(package::AbstractAgent) do
      field_accessor :lastChoice
      
      def choose(*args)
        self.lastChoice = MSciProject::MinorityGame::Choice::A
      end
    end
  }
  let(:choice_history) { package::ChoiceHistory.new(2) }
  let(:empty_strategy_manager) { 
    package::StrategyManager.new(Java::JavaUtil::ArrayList.new)
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
    package::ChoiceMemory.new(choice_history, 2)
  }
  
  let(:abstract_agent_instance) { 
    klass.new(empty_strategy_manager, choice_memory) 
  }
  
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
    
    it "has a last_choice instance method" do
      abstract_agent_instance.should respond_to(:last_choice)
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
    end
  end
  
  describe "#strategies" do
    it "returns the list of strategies stored in the strategy manager" do
      abstract_agent = klass.new(strategy_manager, choice_memory)
      abstract_agent.strategies.should == strategy_manager.strategies
    end
  end
  
  describe "#last_choice" do
    it "throws an IllegalStateException if no choice has been made yet" do
      abstract_agent = klass.new(strategy_manager, choice_memory)
      expect {
        abstract_agent.last_choice
      }.to raise_error(Java::JavaLang::IllegalStateException)
    end
    
    it "doesn't throw an IllegalStateException if a choice has been made" do
      abstract_agent = klass.new(strategy_manager, choice_memory)
      abstract_agent.choose(choice_history.as_list(2))
      expect {
        abstract_agent.last_choice
      }.to_not raise_error(Java::JavaLang::IllegalStateException)
    end
  end
end