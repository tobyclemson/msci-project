require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::MinorityGame do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { Class.new(package::MinorityGame) }
  
  let(:agent_manager) { 
    package::AgentManager.new(Java::JavaUtil::ArrayList.new) 
  }
  
  let(:agent_memory_size) { 2 }
  
  let(:choice_history) { package::ChoiceHistory.new(agent_memory_size) }
  let(:choice_memory) { 
    package::ChoiceMemory.new(choice_history, agent_memory_size)
  }
  
  let(:minority_game_instance) { 
    klass.new(agent_manager, choice_history) 
  }
  
  describe "public interface" do
    it "has an agents instance method" do
      minority_game_instance.should respond_to(:agents)
    end
    
    it "has an agent_manager instance method" do
      minority_game_instance.should respond_to(:agent_manager)
    end
    
    it "has an choice_history instance method" do
      minority_game_instance.should respond_to(:choice_history)
    end
    
    it "has a minority_size instance method" do
      minority_game_instance.should respond_to(:minority_size)
    end
    
    it "has a minority_choice instance method" do
      minority_game_instance.should respond_to(:minority_choice)
    end
    
    it "has a step_forward instance method" do
      minority_game_instance.should respond_to(:step_forward)
    end
  end
  
  describe "constructor" do
    let(:strategy_manager) { 
      Mockito.mock(package::StrategyManager.java_class)
    }
    let(:agent) { 
      package::BasicAgent.new(strategy_manager, choice_memory) 
    }
    
    before(:each) do
      Mockito.when(strategy_manager.strategy_key_length).
        then_return(Java::JavaLang::Integer.new(agent_memory_size))
    end
    
    describe "with agent_manager and choice history arguments" do
      before(:each) do        
        agent_list = Java::JavaUtil::ArrayList.new
        
        3.times { agent_list.add(agent) }
        
        agent_manager = package::AgentManager.new(agent_list)
      end

      it "sets the agent_manager attribute to the supplied AgentManager " + 
        "instance" do
        minority_game = klass.new(agent_manager, choice_history)
        minority_game.agent_manager.should == agent_manager
      end

      it "sets the choice_history attribute to the supplied ChoiceHistory " + 
        "instance" do
        minority_game = klass.new(agent_manager, choice_history)
        minority_game.choice_history.should == choice_history
      end
    end
  end

  describe "agents" do
    it "returns the agents stored in the agent manager" do
      minority_game_instance.agents.should equal(
        minority_game_instance.agent_manager.agents
      )
    end
  end

  describe "#minority_size" do
    it "returns the number of agents in the minority group at the end of " + 
      "the step" do
      mock_agent_manager = Mockito.mock(
        package::AgentManager.java_class
      )
      choice_totals = Java::JavaUtil::HashMap.new
      choice_totals.put(
        package::Choice::A, Java::JavaLang::Integer.new(15)
      )
      choice_totals.put(
        package::Choice::B, Java::JavaLang::Integer.new(12)
      )
      
      Mockito.when(mock_agent_manager.choice_totals).
        then_return(choice_totals)
      
      minority_game = klass.new(
        mock_agent_manager, choice_history
      )
      
      minority_game.minority_size.should == 12
    end
  end

  describe "#minority_choice" do
    it "returns one of Choice.A or Choice.B representing the choice that the " +
      "minority of agents have made in this step" do
      mock_agent_manager = Mockito.mock(
        package::AgentManager.java_class
      )
      choice_totals = Java::JavaUtil::HashMap.new
      choice_totals.put(
        package::Choice::A, Java::JavaLang::Integer.new(15)
      )
      choice_totals.put(
        package::Choice::B, Java::JavaLang::Integer.new(12)
      )

      Mockito.when(mock_agent_manager.choice_totals).
        then_return(choice_totals)

      minority_game = klass.new(
        mock_agent_manager, choice_history
      )

      minority_game.minority_choice.should == package::Choice::B
    end
  end
end