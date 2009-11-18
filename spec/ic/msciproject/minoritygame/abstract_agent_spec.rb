require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AbstractAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::AbstractAgent }
  let(:abstract_agent_instance) { klass.new }
  
  describe "constructor" do
    describe "with no arguments" do
      it "initialises the strategies attribute with an empty " + 
        "StrategyCollection" do
        abstract_agent = klass.new
        abstract_agent.strategies.should be_a_kind_of(
          package::StrategyCollection
        )
        abstract_agent.strategies.should be_empty
      end
    end
  end
  
  describe "setters" do
    describe "#set_strategies" do
      it "sets the strategies attribute to the supplied StrategyCollection" do
        strategy_collection = package::StrategyCollection.new
        abstract_agent_instance.set_strategies(strategy_collection)
        abstract_agent_instance.strategies.should == strategy_collection
      end
    end
  end
end