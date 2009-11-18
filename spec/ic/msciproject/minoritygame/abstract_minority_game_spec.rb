require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AbstractMinorityGame do
  include FactoryHelpers
  
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::AbstractMinorityGame }
  let(:minority_game_instance) { klass.new }
  
  describe "API" do
    it "responds to :agents" do
      minority_game_instance.should respond_to(:agents)
    end
    
    it "responds to :history_string" do
      minority_game_instance.should respond_to(:history_string)
    end
  end
  
  describe "constructor" do
    describe "with agent and history string arguments" do
      let(:agents) { Java::JavaUtil::ArrayList.new }
      let(:agent) { package::AbstractAgent.new }
      let(:history_string) { package::HistoryString.new }

      before(:each) do
        3.times { agents.add(agent) }
        history_string.set_length(2)
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

    describe "with no arguments" do
      it "sets the agents attribute to an empty ArrayList" do
        minority_game = klass.new
        minority_game.agents.should be_a_kind_of(Java::JavaUtil::ArrayList)
        minority_game.should have(0).agents
      end
      
      it "sets the history_string attribute to an instance of " + 
        "HistoryString" do
        minority_game = klass.new
        minority_game.history_string.should be_a_kind_of(
          package::HistoryString
        )
      end
    end
  end
  
  describe "setters" do
    describe "#set_agents" do
      it "sets the agents attribute to the supplied ArrayList" do
        agents = Java::JavaUtil::ArrayList.new
        agent = package::AbstractAgent.new
        agents.add(agent)
        minority_game_instance.set_agents(agents)
        minority_game_instance.agents.should == agents
      end
    end

    describe "#set_history_string" do
      it "sets the history_string attribute to the supplied HistoryString" do
        history_string = package::HistoryString.new
        history_string.set_length(2)
        minority_game_instance.set_history_string(history_string)
        minority_game_instance.history_string.should == history_string
      end
    end
  end
end