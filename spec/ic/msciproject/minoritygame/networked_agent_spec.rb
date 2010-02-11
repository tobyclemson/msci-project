require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::NetworkedAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::NetworkedAgent }
  
  let(:array_list) { Java::JavaUtil::ArrayList }
  
  let(:strategy_manager) { Mockito.mock(package::StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(package::ChoiceMemory.java_class) }
  
  let(:neighbourhood) { Mockito.mock(package::Neighbourhood.java_class) }
  
  let(:agent) { 
    object = klass.new(strategy_manager, choice_memory) 
    object.neighbourhood = neighbourhood
    object
  }
  
  it "extends the AbstractAgent class" do
    agent.should be_a_kind_of(package::AbstractAgent)
  end
  
  describe "#best_friend" do
    it "throws an IllegalStateException if no choice has been made yet" do
      expect {
        agent.best_friend
      }.to raise_error(Java::JavaLang::IllegalStateException)
    end
    
    it "returns the most recently followed agent if a choice has been made" do
      best_friend = Mockito.mock(package::RandomAgent.java_class)
      Mockito.when(neighbourhood.most_successful_predictor).
        then_return(best_friend)
        
      agent.choose
      
      agent.best_friend.should equal(best_friend)
    end
  end
  
  describe "#prepare" do
    let(:random_strategy) { Mockito.mock(package::Strategy.java_class) }
    let(:highest_scoring_strategy) { 
      Mockito.mock(package::Strategy.java_class) 
    }

    before(:each) do
      Mockito.when(random_strategy.predict_minority_choice(Mockito.any())).
        then_return(package::Choice::A)
      Mockito.when(
        highest_scoring_strategy.predict_minority_choice(Mockito.any())
      ).then_return(package::Choice::B)

      Mockito.when(strategy_manager.get_random_strategy).
        then_return(random_strategy)
      Mockito.when(strategy_manager.get_highest_scoring_strategy).
        then_return(highest_scoring_strategy)
    end
    
    describe "when making the first prediction of the game" do
      it "retrieves a random strategy" do
        agent.prepare
        Mockito.verify(strategy_manager).get_random_strategy
      end
      
      it "makes a prediction based on the random strategy" do
        agent.prepare
        agent.prediction.should == 
          random_strategy.predict_minority_choice(choice_memory.fetch)
      end
    end
    
    describe "when making a subsequent choice in the game" do
      before(:each) do
        agent.prepare
      end
      
      it "retrieves the highest scoring strategy" do
        agent.prepare
        Mockito.verify(strategy_manager).get_highest_scoring_strategy
      end
      
      it "makes a choice based on the highest scoring strategy" do
        agent.prepare
        agent.prediction.should == 
          highest_scoring_strategy.predict_minority_choice(
            choice_memory.fetch
          )
      end
    end
  end
  
  describe "#choose" do
    let(:most_successful_predictor) { 
      Mockito.mock(package::AbstractAgent.java_class) 
    }
    
    before(:each) do
      Mockito.when(most_successful_predictor.prediction).
        then_return(package::Choice::B)
        
      Mockito.when(neighbourhood.most_successful_predictor).
        then_return(most_successful_predictor)
    end
    
    it "retrieves the most successful predictor from the neighbourhood" do
      agent.choose
      Mockito.verify(neighbourhood).most_successful_predictor
    end
    
    it "makes a choice based on that agent's prediction" do
      agent.choose
      agent.choice.should == most_successful_predictor.prediction
    end
    
    it "sets the best_friend attribute to the agent that this agent " + 
      "followed" do
      agent.choose
      agent.best_friend.should == most_successful_predictor
    end
  end
  
  describe "#update" do
    let(:random_strategy) { Mockito.mock(package::Strategy.java_class) }
    let(:highest_scoring_strategy) { 
      Mockito.mock(package::Strategy.java_class) 
    }

    before(:each) do
      Mockito.when(random_strategy.predict_minority_choice(Mockito.any())).
        then_return(package::Choice::A)
      Mockito.when(
        highest_scoring_strategy.predict_minority_choice(Mockito.any())
      ).then_return(package::Choice::B)

      Mockito.when(strategy_manager.get_random_strategy).
        then_return(random_strategy)
      Mockito.when(strategy_manager.get_highest_scoring_strategy).
        then_return(highest_scoring_strategy)
    end
    
    it "calls increment_scores on the strategy manager" do
      past_choices = Mockito.mock(array_list.java_class)
      
      Mockito.when(choice_memory.fetch).
        then_return(past_choices)
      
      agent = klass.new(strategy_manager, choice_memory)
      agent.update(package::Choice::B)
      
      Mockito.verify(strategy_manager).
        increment_scores(past_choices, package::Choice::B)
    end
    
    it "increments the correct prediction count if the minority choice is " + 
      "equal to this agent's prediction" do
      expect {
        agent.prepare
        agent.update(agent.prediction)
      }.to change(agent, :correct_prediction_count).by(+1)
    end
    
    it "doesn't increment the correct predicton count if the minority " + 
      "choice is not equal to this agent's prediction" do
      expect {
        agent.prepare
        minority_choice = if(agent.prediction == package::Choice::A)
          package::Choice::B
        else
          package::Choice::A
        end
        agent.update(minority_choice)
      }.to_not change(agent, :correct_prediction_count)
    end
    
    it "adds the minority choice to the memory" do
      agent.update(package::Choice::A)
      Mockito.verify(choice_memory).add(package::Choice::A)
    end
  end
end