require File.join(
  File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb'
)

import msci.mg.factories.RandomAgentFactory

describe RandomAgentFactory do
  let(:factory) { RandomAgentFactory.new }
  
  describe "#create" do
    it "returns different RandomAgent instances each time it is called" do
      agents = (1..100).collect { factory.create }
      agents.uniq.size.should == 100
    end
  end
end