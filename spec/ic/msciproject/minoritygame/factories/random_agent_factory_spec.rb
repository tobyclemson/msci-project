require File.join(
  File.dirname(__FILE__), '..', '..', '..', '..', 'spec_helper.rb'
)

describe MSciProject::MinorityGame::Factories::RandomAgentFactory do
  let(:package) { MSciProject::MinorityGame::Factories }
  let(:klass) { package::RandomAgentFactory }
  
  let(:factory) { klass.new }
  
  describe "#create" do
    it "returns an agent with no memory set " do
      agent = factory.create()
      agent.memory.should be_nil
    end
  end
end