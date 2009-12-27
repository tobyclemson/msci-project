require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::BasicAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::BasicAgent }
  
  let(:array_list) { Java::JavaUtil::ArrayList }
  
  let(:strategy_manager) { Mockito.mock(package::StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(package::ChoiceMemory.java_class) }
  
  let(:basic_agent) { klass.new(strategy_manager, choice_memory) }
  
  it "extends the AbstractAgent class" do
    basic_agent.should be_a_kind_of(package::AbstractAgent)
  end
  
  describe "#choose" do
    let(:random_strategy) { Mockito.mock(package::Strategy.java_class) }
    let(:highest_scoring_strategy) { 
      Mockito.mock(package::Strategy.java_class) 
    }
    
    before(:each) do
      Mockito.when(random_strategy.predict_minority_choice(Mockito.any())).
        then_return(package::Choice::A)
      Mockito.when(
        highest_scoring_strategy.predict_minority_choice(Mockito.any())
      ).then_return(
        package::Choice::B
      )
      
      Mockito.when(strategy_manager.get_random_strategy).
        then_return(random_strategy)
      Mockito.when(strategy_manager.get_highest_scoring_strategy).
        then_return(highest_scoring_strategy)
    end
    
    describe "when making the first choice of the game" do
      it "retrieves a random strategy" do
        basic_agent.choose
        Mockito.verify(strategy_manager).get_random_strategy
      end
      
      it "makes a choice based on the random strategy" do
        basic_agent.choose
        basic_agent.choice.should == 
          random_strategy.predict_minority_choice(choice_memory.fetch)
      end
    end
    
    describe "when making a subsequent choice in the game" do
      before(:each) do
        basic_agent.choose
      end
      
      it "retrieves the highest scoring strategy" do
        basic_agent.choose
        Mockito.verify(strategy_manager).get_highest_scoring_strategy
      end
      
      it "makes a choice based on the highest scoring strategy" do
        basic_agent.choose
        basic_agent.choice.should == 
          highest_scoring_strategy.predict_minority_choice(
            choice_memory.fetch
          )
      end
    end
  end

  describe "#increment_score" do
    it "increases the agents score by 1" do
      expect {
        basic_agent.increment_score
      }.to change(basic_agent, :score).from(0).to(1)
    end
  end

  describe "#update" do
    it "calls increment_scores on the strategy manager" do
      past_choices = Mockito.mock(array_list.java_class)
      
      Mockito.when(choice_memory.fetch).
        then_return(past_choices)
      
      basic_agent = klass.new(strategy_manager, choice_memory)
      basic_agent.update(package::Choice::B)
      
      Mockito.verify(strategy_manager).
        increment_scores(past_choices, package::Choice::B)
    end
  end
end