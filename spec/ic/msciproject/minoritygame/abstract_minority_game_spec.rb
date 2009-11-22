require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AbstractMinorityGame do
  include FactoryHelpers
  
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::AbstractMinorityGame }
  let(:agents) { Java::JavaUtil::ArrayList.new }
  let(:history_string) { package::HistoryString.new(2) }
  let(:minority_game_instance) { klass.new(agents, history_string) }
  
  describe "API" do
    it "responds to :agents" do
      minority_game_instance.should respond_to(:agents)
    end
    
    it "responds to :history_string" do
      minority_game_instance.should respond_to(:history_string)
    end
  end
  
  describe "constructor" do
    let(:strategy_collection) { package::StrategyCollection.new }
    let(:history_string) { package::HistoryString.new(2) }
    let(:agents) { Java::JavaUtil::ArrayList.new }
    let(:agent) { package::AbstractAgent.new(strategy_collection) }
    
    describe "with agent and history string arguments" do
      before(:each) do
        3.times { agents.add(agent) }
      end

      it "sets the agents attribute to the supplied ArrayList of agents" do
        minority_game = klass.new(agents, history_string)
        minority_game.agents.should == agents
      end

      it "sets the history_string attribute to the supplied HistoryString " + 
        "instance" do
        minority_game = klass.new(agents, history_string)
        minority_game.history_string.should == history_string
      end
    end
  end
end