require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AbstractMinorityGame do
  include FactoryHelpers
  
  before(:each) do
    @klass = MSciProject::MinorityGame::AbstractMinorityGame
    @package = MSciProject::MinorityGame
  end
  
  describe "API" do
    
    before(:each) do
      @minority_game_instance = 
        MSciProject::MinorityGame::AbstractMinorityGame.new
    end
    
    it "responds to :agents" do
      @minority_game_instance.should respond_to(:agents)
    end
    
    it "responds to :number_of_agents" do
      @minority_game_instance.should respond_to(:number_of_agents)
    end
  end
  
  describe "constructor with properties hash" do
    let(:properties) { properties_hash }
    
    it "sets the number_of_agents attribute to the supplied integer" do
      properties.set_property("number-of-agents", "10")
      minority_game = @klass.new(properties)
      minority_game.number_of_agents.should == 10
    end
    
    it "sets the agents attribute to an array of number-of-agents agents" do
      properties.set_property("number-of-agents", "10")
      minority_game = @klass.new(properties)
      minority_game.agents.size.should == 10
    end
    
    it "sets the agents attribute to an array of agents of the specified " + 
      "type" do
      properties.set_property("agent-type", "probabilistic")
      minority_game = @klass.new(properties)
      minority_game.agents.each do |agent|
        agent.should be_a_kind_of(@package::ProbabilisticAgent)
      end
    end
  end
end