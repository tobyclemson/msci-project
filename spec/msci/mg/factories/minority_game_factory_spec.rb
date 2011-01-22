require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

# This is a functional test, not a unit test!!! How do I unit test this
# factory without returning a mock when new is called? This implies the class
# is badly written but at the moment don't really want to have to rewrite it.

import java.lang.IllegalArgumentException
import msci.mg.agents.BasicAgent
import msci.mg.agents.LearningAgent
import msci.mg.agents.BasicNetworkedAgent
import msci.mg.agents.RandomAgent
import msci.mg.factories.MinorityGameFactory

describe MinorityGameFactory do
  include FactoryHelpers
  
  describe ".construct" do
    let(:properties) { properties_hash }
    
    describe "properties hash validation" do
      describe "for a valid set of properties" do
        it "successfully creates a minority game" do
          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows a range to be specified for the 'agent-memory-size' " + 
          "property" do
          properties.set_property("agent-memory-size", "2..3")
            
          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows 'empty' to be specified for the 'network-type' property" do
          properties.set_property("network-type", "empty")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows 'complete' to be specified for the 'network-type' " + 
          "property" do
          properties.set_property("network-type", "complete")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows 'random' to be specified for the 'network-type' " + 
          "property" do
          properties.set_property("network-type", "random")
          
          # if the network type is random, a link probability must be supplied
          properties.set_property("link-probability", "0.1")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows 'scale-free' to be specified for the 'network-type' " + 
          "property" do
          properties.set_property("network-type", "scale-free")

          # if the network type is random, a number of friends to attach must 
          # be supplied
          properties.set_property("average-number-of-friends", "5")

          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows 'regular-ring' to be specified for the 'network-type' " +
          "property" do
            properties.set_property("network-type", "regular-ring")

            # if the network type is regular-ring, a number of friends to 
            # attach in each direction must be supplied
            properties.set_property("number-of-friends-in-each-direction", "5")

            expect {
              MinorityGameFactory.construct(properties)
            }.to_not raise_error
        end
        
        it "allows 'basic' to be specified for the 'agent-type' property" do
          properties.set_property("agent-type", "basic")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows 'learning' to be specified for the 'agent-type' " + 
          "property" do
          properties.set_property("agent-type", "learning")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows 'random' to be specified for the 'agent-type' property" do
          properties.set_property("agent-type", "random")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows 'networked' to be specified for the 'agent-type' " + 
          "property" do
          properties.set_property("agent-type", "networked")

          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows an integer to be specified for the 'link-probability' " + 
          "property" do
          properties.set_property("link-probability", "1");
          
          # the link-probability property is only required for a network type
          # of random
          properties.set_property("network-type", "random")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows an integer to be specified for the " + 
          "'average-number-of-friends' property" do
          properties.set_property("average-number-of-friends", "10");
          
          # the average number of friends property is only required for a 
          # network type of scale-free
          properties.set_property("network-type", "scale-free")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows an integer to be specified for the " +
          "'number-of-friends-in-each-direction' property" do
          properties.set_property("number-of-friends-in-each-direction", "15");

          # the number of friends in each direction property is only required 
          # for a network type of regular-ring
          properties.set_property("network-type", "regular-ring")

          expect {
            MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
      end
      
      describe "for an invalid set of properties" do
        it "throws an IllegalArgumentException if the options object " + 
          "doesn't contain a value for the 'agent-memory-size' property" do
          properties.remove("agent-memory-size")
          expect { 
            MinorityGameFactory.construct(properties) 
          }.to raise_error(IllegalArgumentException)
        end
  
        it "throws an IllegalArgumentException if the options object " + 
          "doesn't contain a value for the 'number-of-agents' property" do
          properties.remove("number-of-agents")
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
  
        it "throws an IllegalArgumentException if the options object " + 
          "doesn't contain a value for the 'agent-type' property" do
          properties.remove("agent-type")
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
  
        it "throws an IllegalArgumentException if the options object " + 
          "doesn't contain a value for the " + 
          "'number-of-strategies-per-agent' property" do
          properties.remove("number-of-strategies-per-agent")
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
  
        it "throws an IllegalArgumentException if the options object " + 
          "contains anything other than digits or a range for the " + 
          "'agent-memory-size' property" do
          properties.set_property("agent-memory-size", "non-numeric")
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the range supplied " +
          "for the 'agent-memory-size' property has a negative lower " + 
          "bound" do
          properties.set_property("agent-memory-size", "-1..2")
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
  
        it "throws an IllegalArgumentException if the range supplied " +
          "for the 'agent-memory-size' property has a negative lower " + 
          "bound" do
          properties.set_property("agent-memory-size", "1..-5")
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
  
        it "throws an IllegalArgumentException if the range supplied " +
          "for the 'agent-memory-size' property has an upper bound smaller " + 
          "than the lower bound" do
          properties.set_property("agent-memory-size", "5..2")
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " + 
          "contains anything other than digits for the " + 
          "'number-of-agents' property" do
          properties.set_property("number-of-agents", "non-numeric")
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
  
        it "throws an IllegalArgumentException if the options object " + 
          "contains an unrecognised value for the 'agent-type' property" do
          properties.set_property("agent-type", "unsupported!")
          expect { 
            MinorityGameFactory.construct(properties) 
          }.to raise_error(IllegalArgumentException)
        end
  
        it "throws an IllegalArgumentException if the options object " + 
          "contains anything other than digits for the " + 
          "'number-of-strategies-per-agent' property" do
          properties.set_property(
            "number-of-strategies-per-agent", "non-numeric"
          )
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " +
          "contains an unrecognised value for the 'network-type' property" do
          properties.set_property("network-type", "unsupported!")
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " +
          "contains 'random' for the 'network-type' property but doesn't " +
          "contain a value for the 'link-probability' property" do
          properties.set_property("network-type", "random")
          if properties.contains_key("link-probability")
            properties.remove("link-probability")
          end
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " +
          "contains 'scale-free' for the 'network-type' property but " + 
          "doesn't contain a value for the 'average-number-of-friends' " + 
          "property" do
          properties.set_property("network-type", "scale-free")
          if properties.contains_key("average-number-of-friends")
            properties.remove("average-number-of-friends")
          end
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " +
          "contains a non integer for the 'average-number-of-friends' " +
          "property" do
          properties.set_property("average-number-of-friends", "non-integer")
          
          # only the scale free network type requires the average number of 
          # friends property
          properties.set_property("network-type", "scale-free")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " +
          "contains 'regular-ring' for the 'network-type' property but " + 
          "doesn't contain a value for the 
          'number-of-friends-in-each-direction' property" do
          properties.set_property("network-type", "regular-ring")
          if properties.contains_key("number-of-friends-in-each-direction")
            properties.remove("number-of-friends-in-each-direction")
          end
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " +
          "contains a non-integer for the " + 
          "'number-of-friends-in-each-direction' property" do
          properties.set_property(
            "number-of-friends-in-each-direction", "10.63"
          )
          
          # only the regular-ring network type requires the number of friends 
          # in each drection property
          properties.set_property("network-type", "regular-ring")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " +
          "contains a non-decimal value for the 'link-probability' " + 
          "property" do
          properties.set_property("link-probability", "non-decimal")
          
          # the link-probability property is only required for a network type
          # of random
          properties.set_property("network-type", "random")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " +
          "contains a decimal value less than 0.0 for the " + 
          "'link-probability' property" do
          properties.set_property("link-probability", "-0.3")
          
          # the link-probability property is only required for a network type
          # of random
          properties.set_property("network-type", "random")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " +
          "contains a decimal value greater than 1.0 for the " + 
          "'link-probability' property" do
          properties.set_property("link-probability", "1.3")
          
          # the link-probability property is only required for a network type
          # of random
          properties.set_property("network-type", "random")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the options object " +
          "contains a non integer value for the " + 
          "'average-number-of-friends' property" do
          properties.set_property("average-number-of-friends", "blahblah")
          
          # the link-probability property is only required for a network type
          # of random
          properties.set_property("network-type", "scale-free")
          
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
  
        it "throws an IllegalArgumentException if the options object " +
          "contains an even number for the 'number-of-agents' property" do
          properties.set_property(
            "number-of-agents", "1000"
          )
          expect {
            MinorityGameFactory.construct(properties)
          }.to raise_error(IllegalArgumentException)
        end
      end
    end    
    
    describe "minority game dependency generation" do
      it "initialises the choice_history attribute with a ChoiceHistory " + 
        "instance of the required initial size" do
        properties.set_property("agent-memory-size", "2")
  
        instance = MinorityGameFactory.construct(properties)
        
        instance.choice_history.size.should == 2
      end
      
      it "creates agents with memories of the specified capacity" do
        properties.set_property("agent-memory-size", "2")
        
        instance = MinorityGameFactory.construct(properties)
        
        instance.agents.each do |agent|
          agent.memory.capacity.should == 2
        end
      end
      
      it "creates agents with an initial choice memory corresponding to " + 
        "the initial choice history" do
        properties.set_property("agent-memory-size", "3")
        instance = MinorityGameFactory.construct(properties)
        
        instance.agents.each do |agent|
          agent.memory.fetch.to_a.should == 
            instance.choice_history.as_list(3).to_a
        end
      end
      
      it "creates an equal number of agents with each memory capacity when " +
        "a range is specified for the 'agent-memory-size' parameter" do
        properties.set_property("agent-memory-size", "2..5")
        properties.set_property("number-of-agents", "10001")
        
        instance = MinorityGameFactory.construct(properties)
        
        scores = {2 => 0, 3 => 0, 4 => 0, 5 => 0}
        
        instance.agents.each do |agent|
          scores[agent.memory.capacity] += 1
        end
        
        scores.each_value do |score|
          score.should be_between(2300, 2700)
        end
      end
  
      it "initialises the agents attribute with the required " +
        "number of agents" do
        properties.set_property("number-of-agents", "101")
        instance = MinorityGameFactory.construct(properties)
        instance.agents.should have(101).agents
      end
      
      it "initialises the agents attribute with agents with the required " + 
        "number of strategies" do
        properties.set_property("number-of-strategies-per-agent", "2")
        instance = MinorityGameFactory.construct(properties)
        instance.agents.first.should have(2).strategies
      end
      
      it "initialises the agents attribute with BasicAgent instances when " +
        "the 'agent-type' property is set to 'basic'" do
        properties.set_property("agent-type", "basic")
        instance = MinorityGameFactory.construct(properties)
        instance.agents.each do |agent|
          agent.should be_a_kind_of(BasicAgent)
        end
      end
      
      it "initialises the agents attribute with LearningAgent instances " + 
        "when the 'agent-type' property is set to 'learning'" do
        properties.set_property("agent-type", "learning")
        instance = MinorityGameFactory.construct(properties)
        instance.agents.each do |agent|
          agent.should be_a_kind_of(LearningAgent)
        end
      end
      
      it "initialises the agents attribute with RandomAgent instances when " +
        "the 'agent-type' property is set to 'random'" do
        properties.set_property("agent-type", "random")
        instance = MinorityGameFactory.construct(properties)
        instance.agents.each do |agent|
          agent.should be_a_kind_of(RandomAgent)
        end
      end
      
      it "initialises the agents attribute with BasicNetworkedAgent " + 
        "instances when the 'agent-type' property is set to 'networked'" do
        properties.set_property("agent-type", "networked")
        instance = MinorityGameFactory.construct(properties)
        instance.agents.each do |agent|
          agent.should be_a_kind_of(BasicNetworkedAgent)
        end
      end
      
      it "initialises the social network to a network with the specified " + 
        "number of agents when the 'network-type' property is set to " + 
        "a valid network type" do
        properties.set_property("network-type", "empty")
        properties.set_property("number-of-agents", "101")
        instance = MinorityGameFactory.construct(properties)
        instance.community.should have(101).agents
      end
      
      it "initialises the social network to a network with no friendships " +
        "when the 'network-type' property is set to 'empty'" do
        properties.set_property("network-type", "empty")
        instance = MinorityGameFactory.construct(properties)
        instance.community.should have(0).friendships
      end
      
      it "initialises the social network to a network with average degree " +
        "of approximately the same value as the " + 
        "'average-number-of-friends' property when the 'network-type' " + 
        "property is set to 'scale-free'" do
        properties.set_property("network-type", "scale-free")
        properties.set_property("average-number-of-friends", "10")
        properties.set_property("number-of-agents", "301")
        
        instance = MinorityGameFactory.construct(properties)
        social_network = instance.community.social_network
        
        average_degree = 
          (2 * social_network.edge_count.to_f) / social_network.vertex_count

        average_degree.should be_within(0.2).of(10)
      end
      
      it "initialises the social network to a network with a total number " + 
        "of friendships equal to the required number of friends in each " + 
        "direction multiplied by the number of agents when the " + 
        "'network-type' property is set to 'regular-ring'" do
        number_of_friends_in_each_direction = 15
        number_of_agents = 101  
        
        properties.set_property("network-type", "regular-ring")
        properties.set_property(
          "number-of-friends-in-each-direction", 
          "#{number_of_friends_in_each_direction}"
        )
        properties.set_property("number-of-agents", "#{number_of_agents}")
        
        instance = MinorityGameFactory.construct(properties)
        community = instance.community
        
        required_number_of_friendships = 
          number_of_friends_in_each_direction * number_of_agents
        
        community.should have(required_number_of_friendships).friendships
      end
      
      it "initialises the social network to a network where each agent " +
        "has a number of friends equal to exactly twice the required " + 
        "number of friends in each direction" do
        number_of_friends_in_each_direction = 15

        properties.set_property("agent-type", "networked")
        properties.set_property("network-type", "regular-ring")
        properties.set_property(
          "number-of-friends-in-each-direction", 
          "#{number_of_friends_in_each_direction}"
        )

        instance = MinorityGameFactory.construct(properties)
        agents = instance.agents

        required_number_of_friends = 
          2 * number_of_friends_in_each_direction

        agents.each do |agent|
          agent.should have(required_number_of_friends).friends
        end
      end
      
      it "initialises the social network to one where each agent has " + 
        "friends with the correct number of friends in common for a " + 
        "ring graph" do
        number_of_friends_in_each_direction = 15

        properties.set_property("agent-type", "networked")
        properties.set_property("network-type", "regular-ring")
        properties.set_property(
          "number-of-friends-in-each-direction", 
          "#{number_of_friends_in_each_direction}"
        )

        instance = MinorityGameFactory.construct(properties)
        agents = instance.agents
        social_network = instance.community.social_network
        
        upper_number_in_common = 2 * (number_of_friends_in_each_direction - 1)
        lower_number_in_common = number_of_friends_in_each_direction - 1

        agents.each do |agent|
          agents_with_number_in_common = 
            (lower_number_in_common..upper_number_in_common).
              inject({}) do |m, n|
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
      
      it "initialises the social network to a network with N(N-1)/2 " + 
        "friendships if there are N agents when the 'network-type' " + 
        "property is set to 'complete'" do
        properties.set_property("network-type", "complete")
        properties.set_property("number-of-agents", "11")
        instance = MinorityGameFactory.construct(properties)
        instance.community.should have((10*11)/2).friendships
      end
      
      it "sets each agent's social network to the corresponding subgraph " +
        "of the complete social network when the 'agent-type' property is " +
        "set to 'networked'" do
        properties.set_property("agent-type", "networked")
        properties.set_property("network-type", "complete")
        properties.set_property("number-of-agents", "11")
        
        instance = MinorityGameFactory.construct(properties)
        
        global_social_network = instance.community.social_network
        
        instance.agents.each do |agent|
          friends = global_social_network.get_neighbors(agent)
          agent_network = agent.social_network
          agent_network.contains_vertex(agent).should be_true
          agent_network.edge_count.should == friends.size
          friends.each do |friend|
            agent_network.contains_vertex(friend).should be_true
            agent_network.find_edge(agent, friend).should_not be_nil
          end
        end
      end
    end
  end
end