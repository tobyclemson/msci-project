require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSci::MG::Agents::BasicAgent do
  let(:package) { MSci::MG }
  let(:klass) { package::Agents::BasicAgent }
  
  let(:array_list) { Java::JavaUtil::ArrayList }
  
  let(:strategy_manager) { 
    Mockito.mock(package::StrategyManager.java_class) 
  }
  let(:choice_memory) { 
    Mockito.mock(package::ChoiceMemory.java_class) 
  }
  
  let(:basic_agent) { klass.new(strategy_manager, choice_memory) }
  
  it "extends the AbstractAgent class" do
    basic_agent.should be_a_kind_of(package::Agents::AbstractAgent)
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
    let(:strategy) { Mockito.mock(package::Strategy.java_class) }
    
    before(:each) do
      Mockito.when(strategy.predict_minority_choice(Mockito.any())).
        then_return(package::Choice::A)
        
      Mockito.when(strategy_manager.get_random_strategy).
        then_return(strategy)
      Mockito.when(strategy_manager.get_highest_scoring_strategy).
        then_return(strategy)
    end
    
    it "increments the score if the minority choice is equal to the " +
      "current choice" do
      expect {
        basic_agent.choose
        basic_agent.update(basic_agent.choice)
      }.to change(basic_agent, :score)
    end

    it "doesn't increment the score if the minority choice is not equal " + 
      "to the current choice" do
      expect {
        basic_agent.choose
        minority_choice = if(basic_agent.choice == package::Choice::A)
          package::Choice::B
        else
          package::Choice::A
        end
        basic_agent.update(minority_choice)
      }.to_not change(basic_agent, :score)
    end
    
    it "calls increment_scores on the strategy manager" do
      past_choices = Mockito.mock(array_list.java_class)
      
      Mockito.when(choice_memory.fetch).
        then_return(past_choices)
      
      basic_agent = klass.new(strategy_manager, choice_memory)
      basic_agent.update(package::Choice::B)
      
      Mockito.verify(strategy_manager).
        increment_scores(past_choices, package::Choice::B)
    end
    
    it "adds the minority choice to the memory" do
      basic_agent.update(package::Choice::A)
      Mockito.verify(choice_memory).add(package::Choice::A)
    end
  end
end