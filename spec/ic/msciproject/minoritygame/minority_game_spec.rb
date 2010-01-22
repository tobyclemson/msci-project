require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::MinorityGame do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { Class.new(package::MinorityGame) }
  
  let(:integer) { Java::JavaLang::Integer }
  let(:array_list) { Java::JavaUtil::ArrayList }
  
  let(:agent_manager) { Mockito.mock(package::AgentManager.java_class) }
  let(:choice_history) { Mockito.mock(package::ChoiceHistory.java_class) }
  
  let(:minority_game) { klass.new(agent_manager, choice_history) }
  
  describe "public interface" do
    it "has an agents instance method" do
      minority_game.should respond_to(:agents)
    end
    
    it "has an agent_manager instance method" do
      minority_game.should respond_to(:agent_manager)
    end
    
    it "has an choice_history instance method" do
      minority_game.should respond_to(:choice_history)
    end
    
    it "has a minority_size instance method" do
      minority_game.should respond_to(:minority_size)
    end
    
    it "has a minority_choice instance method" do
      minority_game.should respond_to(:minority_choice)
    end
    
    it "has a step_forward instance method" do
      minority_game.should respond_to(:step_forward)
    end
  end
  
  describe "constructor" do
    describe "with agent_manager and choice history arguments" do
      it "sets the agent_manager attribute to the supplied AgentManager " + 
        "instance" do
        minority_game.agent_manager.should == agent_manager
      end

      it "sets the choice_history attribute to the supplied ChoiceHistory " + 
        "instance" do
        minority_game.choice_history.should == choice_history
      end
    end
  end

  describe "agents" do
    it "returns the agents stored in the agent manager" do
      agents = Mockito.mock(array_list.java_class)
      Mockito.when(agent_manager.get_agents).then_return(agents)
      minority_game.agents.should equal(agents)
    end
  end

  describe "#minority_size" do
    it "returns the number of agents in the minority group at the end of " + 
      "the step" do
      choice_totals = Java::JavaUtil::HashMap.new
      choice_totals.put(package::Choice::A, integer.new(15))
      choice_totals.put(package::Choice::B, integer.new(12))
      
      Mockito.when(agent_manager.get_choice_totals).
        then_return(choice_totals)
      
      minority_game.minority_size.should == 12
    end
  end

  describe "#minority_choice" do
    it "returns one of Choice.A or Choice.B representing the choice that " + 
      "the minority of agents have made in this step" do
      choice_totals = Java::JavaUtil::HashMap.new
      choice_totals.put(package::Choice::A, integer.new(15))
      choice_totals.put(package::Choice::B, integer.new(12))

      Mockito.when(agent_manager.choice_totals).
        then_return(choice_totals)

      minority_game.minority_choice.should == package::Choice::B
    end
  end
  
  describe "#step_forward" do
    let(:choice_totals) {
      hash = Java::JavaUtil::HashMap.new
      hash.put(
        package::Choice::A, Java::JavaLang::Integer.new(15)
      )
      hash.put(
        package::Choice::B, Java::JavaLang::Integer.new(12)
      )
      hash
    }

    before(:each) do
      Mockito.when(agent_manager.choice_totals).
        then_return(choice_totals)
    end
    
    it "tells all agents to prepare to make a choice for this step" do
      minority_game.step_forward
      Mockito.verify(agent_manager).prepare_agents
    end

    it "tells all agents to make their choice for this step" do
      minority_game.step_forward
      Mockito.verify(agent_manager).make_choices
    end

    it "tells all agents to update given the minority choice and choice " + 
      "history" do
      minority_game.step_forward
      Mockito.verify(agent_manager).update_agents(
        package::Choice::B
      )
    end

    it "adds the minority choice to the choice history" do
      minority_game.step_forward
      Mockito.verify(choice_history).add(package::Choice::B)
    end
  end
end