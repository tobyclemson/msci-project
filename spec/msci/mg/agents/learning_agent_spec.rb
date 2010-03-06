require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

import msci.mg.agents.AbstractAgent
import msci.mg.agents.LearningAgent
import msci.mg.ChoiceMemory
import msci.mg.StrategyManager

describe LearningAgent do
  let(:strategy_manager) { Mockito.mock(StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(ChoiceMemory.java_class) }
  
  let(:agent) { LearningAgent.new(strategy_manager, choice_memory) }
  
  it "extends the AbstractAgent class" do
    agent.should be_a_kind_of(AbstractAgent)
  end
end