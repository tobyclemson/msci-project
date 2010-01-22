require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AgentManager do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::AgentManager }
  
  let(:integer) { Java::JavaLang::Integer }
  let(:array_list) { Java::JavaUtil::ArrayList }
  
  let(:agents) { Mockito.mock(array_list.java_class) }
  
  let(:agent_manager) { klass.new(agents) }
  
  describe "public interface" do
    it "has an agents instance method" do
      agent_manager.should respond_to(:agents)
    end
    
    it "has a choice_totals instance method" do
      agent_manager.should respond_to(:choice_totals)
    end
    
    it "has a prepare_agents instance method" do
      agent_manager.should respond_to(:prepare_agents)
    end
    
    it "has a make_choices instance method" do
      agent_manager.should respond_to(:make_choices)
    end
    
    it "has a update_agents instance method" do
      agent_manager.should respond_to(:update_agents)
    end
    
    it "has a number_of_agents instance method" do
      agent_manager.should respond_to(:number_of_agents)
    end
  end
  
  describe "constructor" do
    describe "with a List of AbstractAgent instance as an argument" do      
      it "sets the agents attribute to the supplied collection of agents" do
        agent_manager.agents.should == agents
      end
    end
  end

  describe "#number_of_agents" do
    it "returns the number of agents stored by the agent manager" do
      Mockito.when(agents.size).then_return(integer.new(3))
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
      
      agent_manager = klass.new(agent_list)
      
      totals = agent_manager.choice_totals
      
      {
        package::Choice::A => 3, 
        package::Choice::B => 5
      }.each do |choice, total|
        totals.get(choice).should == total
      end
    end
  end
  
  describe "#prepare_agents" do
    it "calls prepare on each agent" do
      mock_agent_1 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(package::AbstractAgent.java_class)
      
      agent_list = array_list.new
      
      agent_list.add(mock_agent_1)
      agent_list.add(mock_agent_2)
      agent_list.add(mock_agent_3)
      
      agent_manager = klass.new(agent_list)
      
      agent_manager.prepare_agents
      
      Mockito.verify(mock_agent_1).prepare()
      Mockito.verify(mock_agent_2).prepare()
      Mockito.verify(mock_agent_3).prepare()
    end
  end

  describe "#make_choices" do
    it "calls choose on each agent" do
      mock_agent_1 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(package::AbstractAgent.java_class)
      
      agent_list = array_list.new
      
      agent_list.add(mock_agent_1)
      agent_list.add(mock_agent_2)
      agent_list.add(mock_agent_3)
      
      agent_manager = klass.new(agent_list)
      
      agent_manager.make_choices
      
      Mockito.verify(mock_agent_1).choose()
      Mockito.verify(mock_agent_2).choose()
      Mockito.verify(mock_agent_3).choose()
    end
  end
  
  describe "#update_agents" do
    it "calls update on each agent" do
      mock_agent_1 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(package::AbstractAgent.java_class)
      
      agent_list = array_list.new

      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        agent_list.add(agent)
      end
      
      agent_manager = klass.new(agent_list)
      
      agent_manager.update_agents(package::Choice::A)
      
      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        Mockito.verify(agent).update(package::Choice::A)
      end
    end
  end
end