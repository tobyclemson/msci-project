require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

import java.lang.Integer
import java.util.ArrayList
import msci.mg.agents.AbstractIntelligentAgent
import msci.mg.StrategyManager
import msci.mg.ChoiceMemory
import msci.mg.Choice

describe AbstractIntelligentAgent do
  ConcreteIntelligentAgent = Class.new(AbstractIntelligentAgent) do
    field_accessor :choice
    def choose
      choice = Choice::A
    end
  end
  
  let(:strategy_manager) { Mockito.mock(StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(ChoiceMemory.java_class) }
  
  let(:agent) { 
    ConcreteIntelligentAgent.new(strategy_manager, choice_memory) 
  }
  
  describe "constructor" do
    describe "with strategy manager and memory arguments" do
      it "sets the strategy_manager attribute to the supplied strategy " + 
        "manager" do
        agent.strategy_manager.should == strategy_manager
      end

      it "set the memory attribute to the supplied choice memory" do
        agent.memory.should == choice_memory
      end

      it "throws an IllegalArgumentException if the strategy key lengths " + 
        "are not equal to the memory capacity" do
        Mockito.when(strategy_manager.strategy_key_length).
          then_return(Integer.new(3))
        Mockito.when(choice_memory.capacity).
          then_return(Integer.new(2))

        expect {
          ConcreteIntelligentAgent.new(strategy_manager, choice_memory)
        }.to raise_error
      end
    end
  end
  
  describe "#strategies" do
    it "returns the list of strategies stored in the strategy manager" do
      mock_strategies = Mockito.mock(ArrayList.java_class)

      Mockito.when(strategy_manager.get_strategies).
        then_return(mock_strategies)

      agent.strategies.should == strategy_manager.strategies
    end
  end
end