require File.join(
  File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb'
)

import msci.mg.factories.RandomAgentFactory

describe RandomAgentFactory do
  let(:factory) { RandomAgentFactory.new }
  
  describe "#create" do
    it "returns an agent with no memory set " do
      agent = factory.create
      agent.memory.should be_nil
    end
  end
end