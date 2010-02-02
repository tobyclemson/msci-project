require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::Community do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::Community }
  
  let(:integer) { Java::JavaLang::Integer }
  let(:array_list) { Java::JavaUtil::ArrayList }
  
  let(:agents) { Mockito.mock(array_list.java_class) }
  let(:social_network) { Mockito.mock(Jung::Graph::Graph.java_class) }
  
  let(:community) { klass.new(social_network) }
  
  describe "public interface" do
    it "has an agents instance method" do
      community.should respond_to(:agents)
    end
    
    it "has a friendships instance method" do
      community.should respond_to(:friendships)
    end
    
    it "has a social_network instance method" do
      community.should respond_to(:social_network)
    end
    
    it "has a choice_totals instance method" do
      community.should respond_to(:choice_totals)
    end
    
    it "has a prepare_agents instance method" do
      community.should respond_to(:prepare_agents)
    end
    
    it "has a make_choices instance method" do
      community.should respond_to(:make_choices)
    end
    
    it "has a update_agents instance method" do
      community.should respond_to(:update_agents)
    end
    
    it "has a number_of_agents instance method" do
      community.should respond_to(:number_of_agents)
    end
  end
  
  describe "constructor" do
    describe "with a Graph of Agents and Friendships as an argument" do
      it "sets the social network attribute to the supplied network of the " + 
        "community" do
        new_community = klass.new(social_network)
        new_community.social_network.should == social_network
      end
    end
  end

  describe "#number_of_agents" do
    it "returns the number of agents in the social network" do
      Mockito.when(social_network.vertex_count).then_return(integer.new(3))
      community.number_of_agents.should == 3
    end
  end
  
  describe "#agents" do
    it "returns the agents in the social network sorted by " + 
      "identification_number" do
      agents = Java::JavaUtil::HashSet.new
      11.times { agents.add(package::RandomAgent.new()) }

      Mockito.when(social_network.vertices).then_return(agents)
      
      community.agents.to_a.should == agents.sort_by do |agent|
        agent.identification_number
      end
    end
  end
  
  describe "#friendships" do
    it "returns the friendships in the social network" do
      friendships = Mockito.mock(Java::JavaUtil::Collection.java_class)
      Mockito.when(social_network.edges).then_return(friendships)
      
      community.friendships.should == friendships
      
      Mockito.verify(social_network).edges
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
      
      Mockito.when(social_network.vertices).then_return(agent_list)
      
      community = klass.new(social_network)
      
      totals = community.choice_totals
      
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
      
      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        agent_list.add(agent)
      end
      
      Mockito.when(social_network.vertices).then_return(agent_list)
      
      community = klass.new(social_network)
      
      community.prepare_agents
      
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
      
      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        agent_list.add(agent)
      end
      
      Mockito.when(social_network.vertices).then_return(agent_list)
      
      community = klass.new(social_network)
      
      community.make_choices
      
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
      
      Mockito.when(social_network.vertices).then_return(agent_list)
      
      community = klass.new(social_network)
      
      community.update_agents(package::Choice::A)
      
      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        Mockito.verify(agent).update(package::Choice::A)
      end
    end
  end
end