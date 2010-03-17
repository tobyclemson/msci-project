require File.join(
  File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb'
)

import msci.mg.factories.RegularRingSocialNetworkFactory
import msci.mg.factories.SocialNetworkFactory
import msci.mg.factories.AgentFactory
import msci.mg.factories.FriendshipFactory
import msci.mg.agents.Agent
import msci.mg.Friendship

describe RegularRingSocialNetworkFactory do
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
  let(:number_of_agents) { 51 }
  let(:number_of_friends_in_each_direction) { 5 }
  
  let(:factory) {
    RegularRingSocialNetworkFactory.new(
      agent_factory,
      friendship_factory,
      number_of_agents,
      number_of_friends_in_each_direction
    )
  }
  
  it "extends SocialNetworkFactory" do
    factory.should be_a_kind_of(SocialNetworkFactory)
  end
  
  describe "constructor" do
    it "sets the number_of_friends_in_each_direction attribute to the " +
      "supplied value" do
      factory.number_of_friends_in_each_direction.should ==
        number_of_friends_in_each_direction
    end
  end
  
  describe "setters" do
    describe "#number_of_friends_in_each_direction" do
      it "sets the number_of_friends_in_each_direction attribute to the " +
        "supplied value" do
        new_number_of_friends_in_each_direction = 15
        factory.number_of_friends_in_each_direction = 
          new_number_of_friends_in_each_direction
        factory.number_of_friends_in_each_direction.should ==
          new_number_of_friends_in_each_direction
      end
    end
  end
  
  describe "#create" do
    let(:social_network) { factory.create }
    
    it "generates a social network containing the specified number of " + 
      "agents" do
      social_network.vertex_count.should == number_of_agents
    end
    
    it "generates a social network with a number of friendships equal to " +
      "the required number of friends in each direction multiplied " + 
      "by the required number of agents" do
      # note, factors of 2 cancel - each agent actually has 
      # 2 * number_of_friends_in_each_direction friends but since friendships
      # connect to two agents we divide by 2 as well
      required_number_of_friendships = 
        number_of_friends_in_each_direction * number_of_agents
      
      social_network.edge_count.should == required_number_of_friendships
    end
    
    it "generates a social network where each agent has a number of " + 
      "friends equal to exactly twice the supplied number of friends in " + 
      "each direction parameter" do
      agents = social_network.vertices
      required_number_of_friends = 2 * number_of_friends_in_each_direction
      
      agents.each do |agent|
        social_network.degree(agent).should == required_number_of_friends
      end
    end
    
    it "generates a social network where each agent has friends with the " +
      "correct number of friends in common for a ring graph" do
      agents = social_network.vertices
      upper_number_in_common = 2 * (number_of_friends_in_each_direction - 1)
      lower_number_in_common = number_of_friends_in_each_direction - 1
      
      agents.each do |agent|
        agents_with_number_in_common = 
          (lower_number_in_common..upper_number_in_common).inject({}) do |m, n|
            m[n] = 0
            m
          end
          
        friends = social_network.get_neighbors(agent)
        friends.each do |friend|
          friends_friends = social_network.get_neighbors(friend)
          friends_in_common = friends.to_a & friends_friends.to_a
          
          if agents_with_number_in_common.has_key?(friends_in_common.size)
            agents_with_number_in_common[friends_in_common.size] += 1
          end
        end
        
        agents_with_number_in_common.each do |common_friends, number_of_agents|
          number_of_agents.should == 2
        end
      end
    end
  end
end