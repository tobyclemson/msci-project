require File.join(
  File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb'
)

import msci.mg.factories.ScaleFreeSocialNetworkFactory
import msci.mg.factories.SocialNetworkFactory
import msci.mg.factories.AgentFactory
import msci.mg.factories.FriendshipFactory
import msci.mg.agents.Agent
import msci.mg.Friendship

describe ScaleFreeSocialNetworkFactory do
  ConcreteAgentFactory = Class.new(AgentFactory) do
    def create
      return Mockito.mock(Agent.java_class)
    end
  end
  ConcreteFriendshipFactory = Class.new(FriendshipFactory) do
    def create
      return Mockito.mock(Friendship.java_class)
    end
  end
  
  let(:agent_factory) { ConcreteAgentFactory.new }
  let(:friendship_factory) { ConcreteFriendshipFactory.new }
  let(:number_of_agents) { 501 }
  let(:average_number_of_friends) { 10 }
  
  let(:factory) {
    ScaleFreeSocialNetworkFactory.new(
      agent_factory, 
      friendship_factory,
      number_of_agents, 
      average_number_of_friends
    )
  }
  
  it "extends the SocialNetworkFactory" do
    factory.should be_a_kind_of(SocialNetworkFactory)
  end
  
  describe "constructor" do
    it "sets the average_number_of_friends attribute to the supplied " +
      "value" do
      factory.average_number_of_friends.should == average_number_of_friends
    end
  end
  
  describe "setters" do
    describe "#average_number_of_friends=" do
      it "sets the average_number_of_friends attribute to the supplied " +
        "value" do
        new_average_number_of_friends = 10
        factory.average_number_of_friends = new_average_number_of_friends
        factory.average_number_of_friends.should == 
          average_number_of_friends
      end
    end
  end
  
  describe "#create" do
    let(:social_network) { factory.create }
    
    it "generates a social network containing the specified number of " +
      "agents" do
      social_network.vertex_count.should == number_of_agents
    end
    
    it "generates a connected social network" do
      degrees = social_network.vertices.map { |a| social_network.degree(a) }
      
      degrees.should_not include(0)
    end
    
    it "generates a social network with an average degree approximately " +
      "equal to the supplied number of edges to attach attribute" do
      average_degree = 
        (2.0 * social_network.edge_count) / social_network.vertex_count
         
      average_degree.should be_close(average_number_of_friends, 0.2)
    end
    
    it "generates a social network with a maximum degree about 3 times the " +
      "average degree" do
      degrees = social_network.vertices.map { |a| social_network.degree(a) }
         
      degrees.max.should be > average_number_of_friends * 5
    end
  end
end