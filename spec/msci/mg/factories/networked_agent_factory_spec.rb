require File.join(
  File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb'
)

import msci.mg.factories.NetworkedAgentFactory
import msci.mg.factories.IntelligentAgentFactory
import java.lang.Integer
import java.util.ArrayList
import msci.mg.Choice
import org.apache.commons.collections15.Factory

describe NetworkedAgentFactory do
  let(:package) { MSci::MG::Factories }
  
  let(:memory_capacity_factory) { Mockito.mock(Factory.java_class) }
  let(:number_of_strategies_factory) { Mockito.mock(Factory.java_class) }
  let(:initial_choice_memory) {
    list = ArrayList.new
    list.add(Choice::A)
    list.add(Choice::B)
    list.add(Choice::B)
    list
  }
  
  let(:factory) { 
    NetworkedAgentFactory.new(
      memory_capacity_factory, 
      number_of_strategies_factory,
      initial_choice_memory
    ) 
  }
  
  it "extends the IntelligentAgentFactory class" do
    factory.should be_a_kind_of(IntelligentAgentFactory)
  end
  
  describe "#create" do
    let(:agent) { factory.create }
    
    before(:each) do
      Mockito.when(memory_capacity_factory.create).then_return(
        Integer.new(3)
      )
      Mockito.when(number_of_strategies_factory.create).then_return(
        Integer.new(2)
      )
    end
    
    it "returns an agent with a memory capacity as specified by the " + 
      "supplied memory capacity factory" do
      agent.memory.capacity.should == 3
    end
    
    it "returns an agent with an initial choice memory populated with the " + 
      "supplied choice list" do
      agent.memory.fetch.should == initial_choice_memory
    end
    
    it "returns an agent with as many strategies as specified by the " + 
      "supplied number of strategies factory" do
      agent.should have(2).strategies
    end
  end
end