require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AgentManager do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::AgentManager }
  
  let(:agent_memory_size) { 2 }
  
  let(:strategy_manager) { Mockito.mock(package::StrategyManager.java_class) }
  
  let(:agent) {
    package::BasicAgent.new(
      strategy_manager,
      package::ChoiceMemory.new(
        package::ChoiceHistory.new(agent_memory_size), agent_memory_size
      )
    ) 
  }
  
  let(:agent_manager_instance) { klass.new(Java::JavaUtil::ArrayList.new) }
  
  before(:each) do
    Mockito.when(strategy_manager.strategy_key_length).
      then_return(Java::JavaLang::Integer.new(agent_memory_size))
  end
  
  describe "public interface" do
    it "has an agents instance method" do
      agent_manager_instance.should respond_to(:agents)
    end
    
    it "has a choice_totals instance method" do
      agent_manager_instance.should respond_to(:choice_totals)
    end
    
    it "has a make_choices instance method" do
      agent_manager_instance.should respond_to(:make_choices)
    end
    
    it "has an increment_scores_for_choice instance method" do
      agent_manager_instance.should respond_to(
        :increment_scores_for_choice
      )
    end
    
    it "has a update_for_choice instance method" do
      agent_manager_instance.should respond_to(
        :update_for_choice
      )
    end
    
    it "has a number_of_agents instance method" do
      agent_manager_instance.should respond_to(
        :number_of_agents
      )
    end
  end
  
  describe "constructor" do
    describe "with a List of AbstractAgent instance as an argument" do      
      it "sets the agents attribute to the supplied collection of agents" do
        list = Java::JavaUtil::ArrayList.new
        list.add(agent)
        
        agent_manager = klass.new(list)
        
        agent_manager.agents.should == list
      end
    end
  end

  describe "#number_of_agents" do
    it "returns the number of agents stored by the agent manager" do
      agent_list = Java::JavaUtil::ArrayList.new
      3.times { agent_list.add(agent) }
      
      agent_manager = klass.new(agent_list)
      
      agent_manager.number_of_agents.should == 3
    end
  end

  describe "#choice_totals" do
    it "counts the number of agents that have made each choice" do
      choice_a_agent = Mockito.mock(package::AbstractAgent.java_class)
      choice_b_agent = Mockito.mock(package::AbstractAgent.java_class)
      
      Mockito.when(choice_a_agent.choice).thenReturn(package::Choice::A)
      Mockito.when(choice_b_agent.choice).thenReturn(package::Choice::B)
      
      agent_list = Java::JavaUtil::ArrayList.new
      
      3.times { agent_list.add(choice_a_agent) }
      5.times { agent_list.add(choice_b_agent) }
      
      agent_manager_instance = klass.new(agent_list)
      
      totals = agent_manager_instance.choice_totals
      
      {
        package::Choice::A => 3, 
        package::Choice::B => 5
      }.each do |choice, total|
        totals.get(choice).should == total
      end
    end
  end

  describe "#make_choices" do
    it "calls make choice on each agent in the collection with the " +
      "supplied choice history" do
      mock_agent_1 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(package::AbstractAgent.java_class)
      
      agent_list = Java::JavaUtil::ArrayList.new
      
      agent_list.add(mock_agent_1)
      agent_list.add(mock_agent_2)
      agent_list.add(mock_agent_3)
      
      agent_manager_instance = klass.new(agent_list)
      
      agent_manager_instance.make_choices()
      
      Mockito.verify(mock_agent_1).choose()
      Mockito.verify(mock_agent_2).choose()
      Mockito.verify(mock_agent_3).choose()
    end
  end

  describe "#increment_scores_for_choice" do
    it "calls increment_score on all agents that made the supplied choice." do
      mock_agent_1 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(package::AbstractAgent.java_class)
      
      Mockito.when(mock_agent_1.choice).then_return(package::Choice::A)
      Mockito.when(mock_agent_2.choice).then_return(package::Choice::A)
      Mockito.when(mock_agent_3.choice).then_return(package::Choice::B)

      agent_list = Java::JavaUtil::ArrayList.new

      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        agent_list.add(agent)
      end
      
      agent_manager_instance = klass.new(agent_list)
      
      agent_manager_instance.increment_scores_for_choice(
        package::Choice::A
      )
      
      Mockito.verify(mock_agent_1).increment_score
      Mockito.verify(mock_agent_2).increment_score
      Mockito.verify(mock_agent_3, Mockito.never()).increment_score
    end
  end
  
  describe "#update_for_choice" do
    it "calls update_for_choice on each agent" do
      mock_agent_1 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(package::AbstractAgent.java_class)
      
      agent_list = Java::JavaUtil::ArrayList.new

      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        agent_list.add(agent)
      end
      
      agent_manager_instance = klass.new(agent_list)
      
      agent_manager_instance.update_for_choice(package::Choice::A)
      
      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        Mockito.verify(agent).update(package::Choice::A)
      end
    end
  end
end