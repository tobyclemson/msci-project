require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AbstractMinorityGame do
  include FactoryHelpers
  
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::AbstractMinorityGame }
  let(:agents) { package::AgentCollection.new }
  let(:history_string) { package::HistoryString.new(2) }
  let(:minority_game_instance) { klass.new(agents, history_string) }
  
  describe "public interface" do
    it "has an agents instance method" do
      minority_game_instance.should respond_to(:agents)
    end
    
    it "has an history_string instance method" do
      minority_game_instance.should respond_to(:history_string)
    end
    
    it "has a last_minority_size instance method" do
      minority_game_instance.should respond_to(:last_minority_size)
    end
    
    it "has a last_minority_choice instance method" do
      minority_game_instance.should respond_to(:last_minority_choice)
    end
    
    it "has a step_forward instance method" do
      minority_game_instance.should respond_to(:step_forward)
    end
  end
  
  describe "constructor" do
    let(:strategy_collection) { package::StrategyCollection.new }
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

  describe "#last_minority_size" do
    it "returns the number of agents in the minority group at the end of " + 
      "the last step" do
      mock_agent_collection = Mockito.mock(
        package::AgentCollection.java_class
      )
      last_choice_totals = Java::JavaUtil::HashMap.new
      last_choice_totals.put("0", Java::JavaLang::Integer.new(15))
      last_choice_totals.put("1", Java::JavaLang::Integer.new(12))
      
      Mockito.when(mock_agent_collection.last_choice_totals).
        then_return(last_choice_totals)
      
      minority_game = klass.new(mock_agent_collection, history_string)
      
      minority_game.last_minority_size.should == 12
    end
  end

  describe "#last_minority_choice" do
    it "returns a string representing the choice that the minority of " + 
      "agents made in the last step" do
      mock_agent_collection = Mockito.mock(
        package::AgentCollection.java_class
      )
      last_choice_totals = Java::JavaUtil::HashMap.new
      last_choice_totals.put("0", Java::JavaLang::Integer.new(15))
      last_choice_totals.put("1", Java::JavaLang::Integer.new(12))

      Mockito.when(mock_agent_collection.last_choice_totals).
        then_return(last_choice_totals)

      minority_game = klass.new(mock_agent_collection, history_string)

      minority_game.last_minority_choice.should == "1"
    end
  end

  describe "#step_forward" do
    let(:mock_agent_collection) { 
      Mockito.mock(package::AgentCollection.java_class) 
    }
    let(:last_choice_totals) {
      hash = Java::JavaUtil::HashMap.new
      hash.put("0", Java::JavaLang::Integer.new(15))
      hash.put("1", Java::JavaLang::Integer.new(12))
      hash
    }
    let(:history_string) {
      Mockito.mock(package::HistoryString.java_class)
    }
    
    before(:each) do
      Mockito.when(mock_agent_collection.last_choice_totals).
        then_return(last_choice_totals)
    end
    
    it "tells all agents to make their choice for this step" do
      Mockito.when(history_string.to_string).then_return("01")
      minority_game = klass.new(mock_agent_collection, history_string)
      
      minority_game.step_forward
      
      Mockito.verify(mock_agent_collection).
        make_choices("01")
    end
    
    it "tells the agent collection to increment scores for the minority " +
      "choice" do
        minority_game = klass.new(mock_agent_collection, history_string)

        minority_game.step_forward

        Mockito.verify(mock_agent_collection).
          increment_scores_for_choice("1")
    end
    
    it "tells all agents to update given the minority choice and history " + 
      "string" do
      Mockito.when(history_string.to_string).then_return("01")
      minority_game = klass.new(mock_agent_collection, history_string)  
        
      minority_game.step_forward
      
      Mockito.verify(mock_agent_collection).update_for_choice(
        "1", 
        "01"
      )
    end
    
    it "pushes the minority choice onto the history string" do
      minority_game = klass.new(mock_agent_collection, history_string)  
      
      minority_game.step_forward
      
      Mockito.verify(history_string).push("1")
    end
  end
end