require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AbstractAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::AbstractAgent }
  let(:empty_strategy_collection) { package::StrategyCollection.new }
  let(:abstract_agent_instance) { klass.new(empty_strategy_collection) }
  let(:history_string) { package::HistoryString.new(2) }
  let(:map_to_build_strategy) {
      map = Java::JavaUtil::HashMap.new
      map.put("00", "0"); map.put("01", "1") 
      map.put("10", "0"); map.put("11", "1")
      map
  }
  
  describe "public interface" do
    it "has a strategies instance method" do
      abstract_agent_instance.should respond_to(:strategies)
    end
    
    it "has a score instance method" do
      abstract_agent_instance.should respond_to(:score)
    end
    
    it "has a last_choice instance method" do
      abstract_agent_instance.should respond_to(:last_choice)
    end
    
    it "has a make_choice instance method" do
      abstract_agent_instance.should respond_to(:make_choice)
    end
    
    it "has an increment_score instance method" do
      abstract_agent_instance.should respond_to(:increment_score)
    end
    
    it "has an update_for_choice instance method" do
      abstract_agent_instance.should respond_to(:update_for_choice)
    end
  end
  
  describe "constructor" do
    describe "with strategy collection argument" do
      it "sets the strategies attribute to the supplied strategy collection" do
        abstract_agent = klass.new(empty_strategy_collection)
        abstract_agent.strategies.should == empty_strategy_collection
      end
    end
  end
  
  describe "#last_choice" do
    it "throws an IllegalStateException if choice has been made yet" do
      expect {
        abstract_agent_instance.last_choice
      }.to raise_error(Java::JavaLang::IllegalStateException)
    end
    
    it "returns '0' or '1' if a choice has been made" do
      strategy_collection = package::StrategyCollection.new
      strategy = package::Strategy.new(map_to_build_strategy)
      strategy_collection.add(strategy)
      
      abstract_agent = klass.new(strategy_collection)
      
      abstract_agent.make_choice(history_string.to_string)
      ['0', '1'].should include(abstract_agent.last_choice)
    end
  end

  describe "#make_choice" do
    let(:strategy_collection) { 
      Mockito.mock(package::StrategyCollection.java_class)
    }
    let(:random_strategy) {
      map_to_build_strategy.put("01", "1")
      package::Strategy.new(map_to_build_strategy)
    }
    let(:highest_scoring_strategy) {
      map_to_build_strategy.put("01", "0")
      package::Strategy.new(map_to_build_strategy)
    }
    let(:abstract_agent) { klass.new(strategy_collection) }
    
    before(:each) do
      Mockito.when(strategy_collection.random_strategy).
        then_return(random_strategy)
      Mockito.when(strategy_collection.highest_scoring_strategy).
        then_return(highest_scoring_strategy)
    end
    
    describe "when making the first choice of the game" do
      it "retrieves a random strategy" do
        abstract_agent.make_choice(history_string.to_string)
        Mockito.verify(strategy_collection).random_strategy
      end
      
      it "makes a choice based on the random strategy" do
        abstract_agent.make_choice("01")
        abstract_agent.last_choice.should == random_strategy.get("01")
      end
    end
    
    describe "when making a subsequent choice in the game" do
      before(:each) do
        abstract_agent.make_choice(history_string.to_string)
      end
      
      it "retrieves the highest scoring strategy" do
        abstract_agent.make_choice(history_string.to_string)
        Mockito.verify(strategy_collection).highest_scoring_strategy
      end
      
      it "makes a choice based on the highest scoring strategy" do
        abstract_agent.make_choice("01")
        abstract_agent.last_choice.should == 
          highest_scoring_strategy.get("01")
      end
    end
  end

  describe "#increment_score" do
    it "increases the agents score by 1" do
      expect {
        abstract_agent_instance.increment_score
      }.to change(abstract_agent_instance, :score).from(0).to(1)
    end
  end

  describe "#update_for_choice" do
    let(:strategy_collection) { 
      Mockito.mock(package::StrategyCollection.java_class)
    }
    
    it "calls increment_scores_for_choice on the strategy collection" do
      abstract_agent = klass.new(strategy_collection)
      abstract_agent.update_for_choice("1", "01")
      Mockito.verify(strategy_collection).
        increment_scores_for_choice("1", "01")
    end
  end
end