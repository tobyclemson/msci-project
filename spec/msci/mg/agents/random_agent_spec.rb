require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

import msci.mg.agents.AbstractAgent
import msci.mg.agents.RandomAgent
import msci.mg.Choice
import msci.mg.ChoiceMemory
import msci.mg.StrategyManager

describe RandomAgent do
  let(:strategy_manager) { Mockito.mock(StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(ChoiceMemory.java_class) }
  
  let(:agent) { RandomAgent.new(strategy_manager, choice_memory) }
  
  it "extends AbstractAgent" do
    agent.should be_a_kind_of(AbstractAgent)
  end
  
  describe "#choose" do
    it "chooses randomly between choice A and choice B" do
      choices = []

      100.times do
        agent.choose()
        choices << agent.choice
      end

      choice_a_count = choice_b_count = 0

      choices.each do |choice|
        case choice
        when Choice::A
          choice_a_count += 1
        when Choice::B
          choice_b_count += 1
        end
      end

      choice_a_count.should be_between(40, 60)
      choice_b_count.should be_between(40, 60)
      choice_a_count.should == 100 - choice_b_count
    end
  end
  
  describe "#increment_score" do
    it "increases the agents score by 1" do
      expect {
        agent.increment_score
      }.to change(agent, :score).from(0).to(1)
    end
  end
  
  describe "#update" do
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
  end
end