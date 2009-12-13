require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::StandardMinorityGame do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::StandardMinorityGame }
  
  let(:agent_manager) { 
    package::AgentManager.new(Java::JavaUtil::ArrayList.new) 
  }
  
  let(:agent_memory_size) { 2 }
  
  let(:choice_history) { package::ChoiceHistory.new(agent_memory_size) }
  let(:choice_memory) { 
    package::ChoiceMemory.new(choice_history, agent_memory_size)
  }
  
  let(:standard_minority_game) { 
    klass.new(agent_manager, choice_history)
  }
  
  it "extends the AbstractMinorityGame class" do
    standard_minority_game.should be_a_kind_of(package::AbstractMinorityGame)
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
  
  describe "#step_forward" do
    let(:mock_agent_manager) { 
      Mockito.mock(package::AgentManager.java_class) 
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
      Mockito.when(mock_agent_manager.last_choice_totals).
        then_return(last_choice_totals)
      
      choice_history_list.add(package::Choice::A)
      choice_history_list.add(package::Choice::B)
    end
    
    it "tells all agents to make their choice for this step" do      
      Mockito.when(choice_history.as_list(agent_memory_size)).
        then_return(choice_history_list)
      minority_game = klass.new(
        mock_agent_manager, choice_history
      )
      
      minority_game.step_forward
      
      Mockito.verify(mock_agent_manager).
        make_choices
    end
    
    it "tells the agent manager to increment scores for the minority " +
      "choice" do
        minority_game = klass.new(
          mock_agent_manager, choice_history
        )

        minority_game.step_forward

        Mockito.verify(mock_agent_manager).
          increment_scores_for_choice(package::Choice::B)
    end
    
    it "tells all agents to update given the minority choice and choice " + 
      "history" do
      Mockito.when(choice_history.as_list(2)).
        then_return(choice_history_list)
      minority_game = klass.new(
        mock_agent_manager, choice_history
      )  
        
      minority_game.step_forward
      
      Mockito.verify(mock_agent_manager).update_for_choice(
        package::Choice::B
      )
    end
    
    it "adds the minority choice to the choice history" do
      minority_game = klass.new(
        mock_agent_manager, choice_history
      )
      
      minority_game.step_forward
      
      Mockito.verify(choice_history).add(package::Choice::B)
    end
  end
  
end