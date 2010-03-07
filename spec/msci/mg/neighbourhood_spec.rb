require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

import edu.uci.ics.jung.graph.Graph
import java.lang.IllegalArgumentException
import java.lang.Integer
import java.util.HashSet
import msci.mg.agents.NetworkedAgent
import msci.mg.agents.RandomAgent
import msci.mg.Neighbourhood

describe Neighbourhood do
  let(:social_network) { Mockito.mock(Graph.java_class) }
  let(:root_agent) { Mockito.mock(NetworkedAgent.java_class) }
  
  let(:neighbourhood) { Neighbourhood.new(social_network, root_agent) }
  
  before(:each) do
    Mockito.when(social_network.contains_vertex(root_agent)).then_return(true)
  end
  
  describe "constructor" do
    it "throws an IllegalArgumentException if the supplied root AbstractAgent " + 
      "doesn't exist in teh supplied Graph"do
      Mockito.when(social_network.contains_vertex(root_agent)).
        then_return(false)
        
      expect {
        neighbourhood = Neighbourhood.new(social_network, root_agent)
      }.to raise_error(IllegalArgumentException)
    end
    it "sets the social_network attribute to the supplied Graph of AbstractAgents " + 
      "and friendships" do
      neighbourhood.social_network.should equal(social_network)
    end
    
    it "sets the root_agent attribute to the supplied AbstractAgent" do
      neighbourhood.root_agent.should equal(root_agent)
    end
  end
  
  describe "#friends" do
    it "returns the AbstractAgents in the supplied social network excluding the " + 
      "root AbstractAgent" do
      neighbours = HashSet.new
      11.times { neighbours.add(RandomAgent.new()) }

      Mockito.when(social_network.get_neighbors(root_agent)).
        then_return(neighbours)
      
      friends = neighbourhood.friends
      
      neighbours.each do |neighbour|
        friends.should include(neighbour)
      end
      
      friends.size.should == neighbours.size
    end
  end
  
  describe "#most_successful_predictor" do
    it "returns the agent in the social network that has the highest " + 
      "correct prediction count" do
      friend_1 = Mockito.mock(NetworkedAgent.java_class)
      friend_2 = Mockito.mock(NetworkedAgent.java_class)
      
      neighbours = HashSet.new
      neighbours.add(friend_1)
      neighbours.add(friend_2)
      
      Mockito.when(social_network.get_neighbors(root_agent)).
        then_return(neighbours)
        
      Mockito.when(root_agent.correct_prediction_count).
        then_return(Integer.new(3))
      Mockito.when(friend_1.correct_prediction_count).
        then_return(Integer.new(4))
      Mockito.when(friend_2.correct_prediction_count).
        then_return(Integer.new(5))
        
      neighbourhood.most_successful_predictor.should equal(friend_2)
    end
    
    it "returns the root agent if it has a higher score than all of its " +
      "friends" do
      friend_1 = Mockito.mock(NetworkedAgent.java_class)
      friend_2 = Mockito.mock(NetworkedAgent.java_class)

      neighbours = HashSet.new
      neighbours.add(friend_1)
      neighbours.add(friend_2)

      Mockito.when(social_network.get_neighbors(root_agent)).
        then_return(neighbours)

      Mockito.when(root_agent.correct_prediction_count).
        then_return(Integer.new(10))
      Mockito.when(friend_1.correct_prediction_count).
        then_return(Integer.new(4))
      Mockito.when(friend_2.correct_prediction_count).
        then_return(Integer.new(5))

      neighbourhood.most_successful_predictor.should equal(root_agent)
    end
    
    it "returns one of the most successful predictors at random when more " + 
      "than one has the same correct prediction count" do
        friend_1 = Mockito.mock(NetworkedAgent.java_class)
        friend_2 = Mockito.mock(NetworkedAgent.java_class)
        friend_3 = Mockito.mock(NetworkedAgent.java_class)

        neighbours = HashSet.new
        neighbours.add(friend_1)
        neighbours.add(friend_2)
        neighbours.add(friend_3)

        Mockito.when(social_network.get_neighbors(root_agent)).
          then_return(neighbours)

        Mockito.when(root_agent.correct_prediction_count).
          then_return(Integer.new(10))
        Mockito.when(friend_1.correct_prediction_count).
          then_return(Integer.new(10))
        Mockito.when(friend_2.correct_prediction_count).
          then_return(Integer.new(5))
        Mockito.when(friend_3.correct_prediction_count).
          then_return(Integer.new(2))

        root_agent_count = 0
        friend_1_count = 0

        100.times do
          predictor = neighbourhood.most_successful_predictor

          if predictor.equals(root_agent)
            root_agent_count += 1
          elsif predictor.equals(friend_1)
            friend_1_count += 1
          end
        end
        
        root_agent_count.should be_between(40, 60)
        friend_1_count.should be_between(40, 60)
        (root_agent_count + friend_1_count).should == 100
    end
    
    it "returns each agent in the network with equal probability when they " + 
      "all have the same correct prediction count" do
        friend_1 = Mockito.mock(NetworkedAgent.java_class)
        friend_2 = Mockito.mock(NetworkedAgent.java_class)
        friend_3 = Mockito.mock(NetworkedAgent.java_class)

        neighbours = HashSet.new
        neighbours.add(friend_1)
        neighbours.add(friend_2)
        neighbours.add(friend_3)

        Mockito.when(social_network.get_neighbors(root_agent)).
          then_return(neighbours)

        Mockito.when(root_agent.correct_prediction_count).
          then_return(Integer.new(10))
        Mockito.when(friend_1.correct_prediction_count).
          then_return(Integer.new(10))
        Mockito.when(friend_2.correct_prediction_count).
          then_return(Integer.new(10))
        Mockito.when(friend_3.correct_prediction_count).
          then_return(Integer.new(10))

        root_agent_count = 0
        friend_1_count = 0
        friend_2_count = 0
        friend_3_count = 0

        100.times do
          predictor = neighbourhood.most_successful_predictor

          if predictor.equals(root_agent)
            root_agent_count += 1
          elsif predictor.equals(friend_1)
            friend_1_count += 1
          elsif predictor.equals(friend_2)
            friend_2_count += 1
          elsif predictor.equals(friend_3)
            friend_3_count += 1
          end
        end
        
        counts = [
          root_agent_count, friend_1_count, friend_2_count, friend_3_count
        ]
        
        counts.each do |frequency|
          frequency.should be_between(15, 35)
        end
                 
        counts.sum.should == 100
    end
  end
end