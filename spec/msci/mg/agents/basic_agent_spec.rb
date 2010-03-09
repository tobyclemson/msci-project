require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

import java.util.ArrayList
import msci.mg.agents.AbstractIntelligentAgent
import msci.mg.agents.BasicAgent
import msci.mg.Choice
import msci.mg.ChoiceMemory
import msci.mg.Strategy
import msci.mg.StrategyManager

describe BasicAgent do
  let(:strategy_manager) { Mockito.mock(StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(ChoiceMemory.java_class) }
  
  let(:agent) { BasicAgent.new(strategy_manager, choice_memory) }
  
  it "extends the AbstractIntelligentAgent class" do
    agent.should be_a_kind_of(AbstractIntelligentAgent)
  end
  
  describe "#choose" do
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
    
    describe "when making the first choice of the game" do
      it "retrieves a random strategy" do
        agent.choose
        Mockito.verify(strategy_manager).get_random_strategy
      end
      
      it "makes a choice based on the random strategy" do
        agent.choose
        agent.choice.should == 
          random_strategy.predict_minority_choice(choice_memory.fetch)
      end
    end
    
    describe "when making a subsequent choice in the game" do
      before(:each) do
        agent.choose
      end
      
      it "retrieves the highest scoring strategy" do
        agent.choose
        Mockito.verify(strategy_manager).get_highest_scoring_strategy
      end
      
      it "makes a choice based on the highest scoring strategy" do
        agent.choose
        agent.choice.should == 
          highest_scoring_strategy.predict_minority_choice(
            choice_memory.fetch
          )
      end
    end
  end

  describe "#update" do
    let(:strategy) { Mockito.mock(Strategy.java_class) }
    
    before(:each) do
      Mockito.when(strategy.predict_minority_choice(Mockito.any())).
        then_return(Choice::A)
        
      Mockito.when(strategy_manager.get_random_strategy).
        then_return(strategy)
      Mockito.when(strategy_manager.get_highest_scoring_strategy).
        then_return(strategy)
    end
    
    it "increments the score if the minority choice is equal to the " +
      "current choice" do
      expect {
        agent.choose
        agent.update(agent.choice)
      }.to change(agent, :score)
    end

    it "doesn't increment the score if the minority choice is not equal " + 
      "to the current choice" do
      expect {
        agent.choose
        minority_choice = if(agent.choice == Choice::A)
          Choice::B
        else
          Choice::A
        end
        agent.update(minority_choice)
      }.to_not change(agent, :score)
    end
    
    it "calls increment_scores on the strategy manager" do
      past_choices = Mockito.mock(ArrayList.java_class)
      
      Mockito.when(choice_memory.fetch).
        then_return(past_choices)
      
      agent = BasicAgent.new(strategy_manager, choice_memory)
      agent.update(Choice::B)
      
      Mockito.verify(strategy_manager).
        increment_scores(past_choices, Choice::B)
    end
    
    it "adds the minority choice to the memory" do
      agent.update(Choice::A)
      Mockito.verify(choice_memory).add(Choice::A)
    end
  end
end