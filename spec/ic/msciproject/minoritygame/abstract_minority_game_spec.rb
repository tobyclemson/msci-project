require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AbstractMinorityGame do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::AbstractMinorityGame }
  let(:agents) { package::AgentCollection.new }
  let(:choice_history) { package::ChoiceHistory.new(2) }
  let(:agent_memory_size) { 2 }
  let(:minority_game_instance) { 
    klass.new(agents, choice_history, agent_memory_size) 
  }
  
  describe "public interface" do
    it "has an agents instance method" do
      minority_game_instance.should respond_to(:agents)
    end
    
    it "has an choice_history instance method" do
      minority_game_instance.should respond_to(:choice_history)
    end
    
    it "has an agent_memory_size instance method" do
      minority_game_instance.should respond_to(:agent_memory_size)
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
    let(:strategy_manager) { 
      package::StrategyManager.new(Java::JavaUtil::ArrayList.new)
    }
    let(:agent) { package::AbstractAgent.new(strategy_manager) }
    
    describe "with agents, choice history and agent memory size arguments" do
      before(:each) do
        3.times { agents.add(agent) }
      end

      it "sets the agents attribute to the supplied ArrayList of agents" do
        minority_game = klass.new(agents, choice_history, agent_memory_size)
        minority_game.agents.should == agents
      end

      it "sets the choice_history attribute to the supplied ChoiceHistory " + 
        "instance" do
        minority_game = klass.new(agents, choice_history, agent_memory_size)
        minority_game.choice_history.should == choice_history
      end
      
      it "sets the agent_memory_size attribute to the supplied integer" do
        minority_game = klass.new(agents, choice_history, agent_memory_size)
        minority_game.agent_memory_size.should == agent_memory_size
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
      last_choice_totals.put(
        package::Choice::A, Java::JavaLang::Integer.new(15)
      )
      last_choice_totals.put(
        package::Choice::B, Java::JavaLang::Integer.new(12)
      )
      
      Mockito.when(mock_agent_collection.last_choice_totals).
        then_return(last_choice_totals)
      
      minority_game = klass.new(
        mock_agent_collection, choice_history, agent_memory_size
      )
      
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
      last_choice_totals.put(
        package::Choice::A, Java::JavaLang::Integer.new(15)
      )
      last_choice_totals.put(
        package::Choice::B, Java::JavaLang::Integer.new(12)
      )

      Mockito.when(mock_agent_collection.last_choice_totals).
        then_return(last_choice_totals)

      minority_game = klass.new(
        mock_agent_collection, choice_history, agent_memory_size
      )

      minority_game.last_minority_choice.should == package::Choice::B
    end
  end

  describe "#step_forward" do
    let(:mock_agent_collection) { 
      Mockito.mock(package::AgentCollection.java_class) 
    }
    let(:last_choice_totals) {
      hash = Java::JavaUtil::HashMap.new
      hash.put(
        package::Choice::A, Java::JavaLang::Integer.new(15)
      )
      hash.put(
        package::Choice::B, Java::JavaLang::Integer.new(12)
      )
      hash
    }
    let(:choice_history) {
      Mockito.mock(package::ChoiceHistory.java_class)
    }
    let(:choice_history_list) {
      Java::JavaUtil::ArrayList.new
    }
    
    before(:each) do
      Mockito.when(mock_agent_collection.last_choice_totals).
        then_return(last_choice_totals)
      
      choice_history_list.add(package::Choice::A)
      choice_history_list.add(package::Choice::B)
    end
    
    it "tells all agents to make their choice for this step" do      
      Mockito.when(choice_history.as_list(agent_memory_size)).
        then_return(choice_history_list)
      minority_game = klass.new(
        mock_agent_collection, choice_history, agent_memory_size
      )
      
      minority_game.step_forward
      
      Mockito.verify(mock_agent_collection).
        make_choices(choice_history_list)
    end
    
    it "tells the agent collection to increment scores for the minority " +
      "choice" do
        minority_game = klass.new(
          mock_agent_collection, choice_history, agent_memory_size
        )

        minority_game.step_forward

        Mockito.verify(mock_agent_collection).
          increment_scores_for_choice(package::Choice::B)
    end
    
    it "tells all agents to update given the minority choice and choice " + 
      "history" do
      Mockito.when(choice_history.as_list(2)).
        then_return(choice_history_list)
      minority_game = klass.new(
        mock_agent_collection, choice_history, agent_memory_size
      )  
        
      minority_game.step_forward
      
      Mockito.verify(mock_agent_collection).update_for_choice(
        package::Choice::B, 
        choice_history_list
      )
    end
    
    it "adds the minority choice to the choice history" do
      minority_game = klass.new(
        mock_agent_collection, choice_history, agent_memory_size
      )
      
      minority_game.step_forward
      
      Mockito.verify(choice_history).add(package::Choice::B)
    end
  end
end