require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

import edu.uci.ics.jung.graph.Graph
import java.lang.Integer
import java.util.ArrayList
import java.util.Collection
import java.util.HashSet
import msci.mg.agents.AbstractAgent
import msci.mg.agents.RandomAgent
import msci.mg.Choice
import msci.mg.Community

describe Community do
  let(:agents) { Mockito.mock(ArrayList.java_class) }
  let(:social_network) { Mockito.mock(Graph.java_class) }
  
  let(:community) { Community.new(social_network) }
  
  describe "constructor" do
    describe "with a Graph of Agents and Friendships as an argument" do
      it "sets the social network attribute to the supplied network of the " + 
        "community" do
        new_community = Community.new(social_network)
        new_community.social_network.should == social_network
      end
    end
  end

  describe "#number_of_agents" do
    it "returns the number of agents in the social network" do
      Mockito.when(social_network.vertex_count).then_return(Integer.new(3))
      community.number_of_agents.should == 3
    end
  end
  
  describe "#agents" do
    it "returns the agents in the social network" do
      agents = HashSet.new
      11.times { agents.add(RandomAgent.new()) }

      Mockito.when(social_network.vertices).then_return(agents)

      returned_agents = community.agents
      
      agents.each do |agent|
        returned_agents.should include(agent)
      end
      
      returned_agents.size.should == agents.size
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
      choice_a_agent = Mockito.mock(RandomAgent.java_class)
      choice_b_agent = Mockito.mock(RandomAgent.java_class)
      
      Mockito.when(choice_a_agent.getChoice()).thenReturn(Choice::A)
      Mockito.when(choice_b_agent.getChoice()).thenReturn(Choice::B)
      
      agent_list = ArrayList.new
      
      3.times { agent_list.add(choice_a_agent) }
      5.times { agent_list.add(choice_b_agent) }
      
      Mockito.when(social_network.vertices).then_return(agent_list)
      
      community = Community.new(social_network)
      
      totals = community.choice_totals
      
      {Choice::A => 3, Choice::B => 5}.each do |choice, total|
        totals.get(choice).should == total
      end
    end
  end
  
  describe "#prepare_agents" do
    it "calls prepare on each agent" do
      mock_agent_1 = Mockito.mock(AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(AbstractAgent.java_class)
      
      agent_list = ArrayList.new
      
      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        agent_list.add(agent)
      end
      
      Mockito.when(social_network.vertices).then_return(agent_list)
      
      community = Community.new(social_network)
      
      community.prepare_agents
      
      Mockito.verify(mock_agent_1).prepare()
      Mockito.verify(mock_agent_2).prepare()
      Mockito.verify(mock_agent_3).prepare()
    end
  end

  describe "#make_choices" do
    it "calls choose on each agent" do
      mock_agent_1 = Mockito.mock(AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(AbstractAgent.java_class)
      
      agent_list = ArrayList.new
      
      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        agent_list.add(agent)
      end
      
      Mockito.when(social_network.vertices).then_return(agent_list)
      
      community = Community.new(social_network)
      
      community.make_choices
      
      Mockito.verify(mock_agent_1).choose()
      Mockito.verify(mock_agent_2).choose()
      Mockito.verify(mock_agent_3).choose()
    end
  end
  
  describe "#update_agents" do
    it "calls update on each agent" do
      mock_agent_1 = Mockito.mock(AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(AbstractAgent.java_class)
      
      agent_list = ArrayList.new

      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        agent_list.add(agent)
      end
      
      Mockito.when(social_network.vertices).then_return(agent_list)
      
      community = Community.new(social_network)
      
      community.update_agents(Choice::A)
      
      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        Mockito.verify(agent).update(Choice::A)
      end
    end
  end
end