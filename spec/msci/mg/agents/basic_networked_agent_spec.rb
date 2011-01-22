require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

import java.util.ArrayList
import msci.mg.agents.AbstractNetworkedAgent
import msci.mg.agents.BasicNetworkedAgent
import msci.mg.Choice
import msci.mg.ChoiceMemory
import msci.mg.Neighbourhood
import msci.mg.Strategy
import msci.mg.StrategyManager

describe BasicNetworkedAgent do
  let(:strategy_manager) { Mockito.mock(StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(ChoiceMemory.java_class) }
  
  let(:neighbourhood) { Mockito.mock(Neighbourhood.java_class) }
  
  let(:agent) { 
    object = BasicNetworkedAgent.new(strategy_manager, choice_memory) 
    object.neighbourhood = neighbourhood
    object
  }
  
  it "extends the AbstractNetworkedAgent class" do
    agent.should be_a_kind_of(AbstractNetworkedAgent)
  end
  
  describe "#prepare" do
    let(:random_strategy) { Mockito.mock(Strategy.java_class) }
    let(:highest_scoring_strategy) { Mockito.mock(Strategy.java_class) }

    before(:each) do
      Mockito.when(random_strategy.predict_minority_choice(Mockito.any())).
        then_return(Choice::A)
      Mockito.when(
        highest_scoring_strategy.predict_minority_choice(Mockito.any())
      ).then_return(Choice::B)

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
      Mockito.mock(BasicNetworkedAgent.java_class) 
    }
    
    before(:each) do
      Mockito.when(most_successful_predictor.getPrediction()).
        then_return(Choice::B)
        
      Mockito.when(neighbourhood.getMostSuccessfulPredictor()).
        then_return(most_successful_predictor)
    end
    
    it "retrieves the most successful predictor from the neighbourhood" do
      agent.choose
      Mockito.verify(neighbourhood).getMostSuccessfulPredictor()
    end
    
    it "makes a choice based on that agent's prediction" do
      agent.choose
      agent.getChoice().should == most_successful_predictor.getPrediction()
    end
    
    it "sets the best_friend attribute to the agent that this agent " + 
      "followed" do
      agent.choose
      agent.getBestFriend().should == most_successful_predictor
    end
  end
  
  describe "#update" do
    let(:random_strategy) { Mockito.mock(Strategy.java_class) }
    let(:highest_scoring_strategy) { Mockito.mock(Strategy.java_class) }

    before(:each) do
      Mockito.when(random_strategy.predict_minority_choice(Mockito.any())).
        then_return(Choice::A)
      Mockito.when(
        highest_scoring_strategy.predict_minority_choice(Mockito.any())
      ).then_return(Choice::B)

      Mockito.when(strategy_manager.get_random_strategy).
        then_return(random_strategy)
      Mockito.when(strategy_manager.get_highest_scoring_strategy).
        then_return(highest_scoring_strategy)
    end
    
    it "calls increment_scores on the strategy manager" do
      past_choices = Mockito.mock(ArrayList.java_class)
      
      Mockito.when(choice_memory.fetch).
        then_return(past_choices)
      
      agent = BasicNetworkedAgent.new(strategy_manager, choice_memory)
      agent.update(Choice::B)
      
      Mockito.verify(strategy_manager).
        increment_scores(past_choices, Choice::B)
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
        minority_choice = if(agent.prediction == Choice::A)
          Choice::B
        else
          Choice::A
        end
        agent.update(minority_choice)
      }.to_not change(agent, :correct_prediction_count)
    end
    
    it "adds the minority choice to the memory" do
      agent.update(Choice::A)
      Mockito.verify(choice_memory).add(Choice::A)
    end
  end
end