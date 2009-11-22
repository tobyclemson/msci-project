require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AbstractAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::AbstractAgent }
  let(:empty_strategy_collection) { package::StrategyCollection.new }
  let(:abstract_agent_instance) { klass.new(empty_strategy_collection) }
  
  describe "constructor" do
    describe "with strategy collection argument" do
      it "sets the strategies attribute to the supplied strategy collection" do
        abstract_agent = klass.new(empty_strategy_collection)
        abstract_agent.strategies.should == empty_strategy_collection
      end
    end
  end
end