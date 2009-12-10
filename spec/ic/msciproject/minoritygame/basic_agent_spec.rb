require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::BasicAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::BasicAgent }
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
  
  let(:basic_agent_instance) { klass.new(empty_strategy_manager) }
  
  it "extends the AbstractAgent class" do
    basic_agent_instance.should be_a_kind_of(package::AbstractAgent)
  end
  
  describe "#choose" do
    let(:strategy_manager) { 
      Mockito.mock(package::StrategyManager.java_class)
    }
    let(:random_strategy) {
      map_to_build_strategy.put(a_b, package::Choice::B)
      package::Strategy.new(map_to_build_strategy)
    }
    let(:highest_scoring_strategy) {
      map_to_build_strategy.put(a_b, package::Choice::A)
      package::Strategy.new(map_to_build_strategy)
    }
    let(:basic_agent) { klass.new(strategy_manager) }
    
    before(:each) do
      Mockito.when(strategy_manager.random_strategy).
        then_return(random_strategy)
      Mockito.when(strategy_manager.highest_scoring_strategy).
        then_return(highest_scoring_strategy)
    end
    
    describe "when making the first choice of the game" do
      it "retrieves a random strategy" do
        basic_agent.choose(choice_history.as_list(2))
        Mockito.verify(strategy_manager).random_strategy
      end
      
      it "makes a choice based on the random strategy" do
        basic_agent.choose(a_b)
        basic_agent.last_choice.should == 
          random_strategy.predict_minority_choice(a_b)
      end
    end
    
    describe "when making a subsequent choice in the game" do
      before(:each) do
        basic_agent.choose(choice_history.as_list(2))
      end
      
      it "retrieves the highest scoring strategy" do
        basic_agent.choose(choice_history.as_list(2))
        Mockito.verify(strategy_manager).highest_scoring_strategy
      end
      
      it "makes a choice based on the highest scoring strategy" do
        basic_agent.choose(a_b)
        basic_agent.last_choice.should == 
          highest_scoring_strategy.predict_minority_choice(a_b)
      end
    end
  end

  describe "#increment_score" do
    it "increases the agents score by 1" do
      expect {
        basic_agent_instance.increment_score
      }.to change(basic_agent_instance, :score).from(0).to(1)
    end
  end

  describe "#update" do
    let(:strategy_manager) { 
      Mockito.mock(package::StrategyManager.java_class)
    }
    
    it "calls increment_scores on the strategy manager" do
      basic_agent = klass.new(strategy_manager)
      basic_agent.update(package::Choice::B, a_b)
      Mockito.verify(strategy_manager).
        increment_scores(a_b, package::Choice::B)
    end
  end
end