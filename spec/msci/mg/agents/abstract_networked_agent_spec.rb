require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

import edu.uci.ics.jung.graph.Graph
import java.lang.IllegalStateException
import java.util.ArrayList
import msci.mg.agents.AbstractIntelligentAgent
import msci.mg.agents.AbstractNetworkedAgent
import msci.mg.agents.NetworkedAgent
import msci.mg.StrategyManager
import msci.mg.Strategy
import msci.mg.ChoiceMemory
import msci.mg.Choice
import msci.mg.Neighbourhood

describe AbstractNetworkedAgent do
  ConcreteNetworkedAgent = Class.new(AbstractNetworkedAgent) do
    field_accessor :choice, :bestFriend, :neighbourhood, :prediction
    
    def prepare
      self.prediction = Choice::B
    end
    
    def choose
      self.choice = Choice::A
      self.bestFriend = neighbourhood.most_successful_predictor
    end
  end
  
  let(:strategy_manager) { Mockito.mock(StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(ChoiceMemory.java_class) }
  
  let(:neighbourhood) { Mockito.mock(Neighbourhood.java_class) }
  
  let(:agent) { 
    object = ConcreteNetworkedAgent.new(strategy_manager, choice_memory) 
    object.neighbourhood = neighbourhood
    object
  }
  
  it "extends AbstractIntelligentAgent" do
    agent.should be_a_kind_of(AbstractIntelligentAgent)
  end
  
  it "implements the NetworkedAgent interface" do
    agent.should be_a_kind_of(NetworkedAgent)
  end
  
  describe "setters" do
    describe "#neighbourhood" do
      it "sets the neighbourhood attribute to the supplied object" do
        new_neighbourhood = Mockito.mock(Neighbourhood.java_class)
        agent.neighbourhood = new_neighbourhood
        agent.neighbourhood.should == new_neighbourhood
      end
    end
  end

  describe "#friends" do
    it "returns the result of calling friends on the supplied " + 
      "neighbourhood" do
      friends = Mockito.mock(ArrayList.java_class)
      Mockito.when(neighbourhood.friends).then_return(friends)
      
      agent.neighbourhood = neighbourhood
      
      agent.friends.should == friends
    end

    it "throws an IllegalStateException if no neighbourhood has been " +
      "set" do
      agent.neighbourhood = nil
      expect {
        agent.friends
      }.to raise_error(IllegalStateException)
    end
  end

  describe "#social_network" do
    it "returns the result of calling social_network on the supplied " +
      "neighbourhood" do
      social_network = Mockito.mock(Graph.java_class)
      Mockito.when(neighbourhood.social_network).then_return(social_network)

      agent.neighbourhood = neighbourhood

      agent.social_network.should == social_network
    end

    it "throws an IllegalStateException if no neighbourhood has been " +
      "set" do
      agent.neighbourhood = nil
      expect {
        agent.social_network
      }.to raise_error(IllegalStateException)
    end
  end
  
  describe "#best_friend" do
    it "throws an IllegalStateException if no choice has been made yet" do
      expect {
        agent.best_friend
      }.to raise_error(IllegalStateException)
    end
    
    it "returns the most recently followed agent if a choice has been made" do
      best_friend = Mockito.mock(ConcreteNetworkedAgent.java_class)
      Mockito.when(neighbourhood.most_successful_predictor).
        then_return(best_friend)
        
      agent.choose
      
      agent.best_friend.should equal(best_friend)
    end
  end
  
  describe "#prediction" do
    let(:strategy) { Mockito.mock(Strategy.java_class) }
    
    before(:each) do
      Mockito.when(strategy.predict_minority_choice(Mockito.any())).
        then_return(Choice::A)
      Mockito.when(strategy_manager.random_strategy).then_return(strategy)
    end
    
    it "throws an IllegalStateException if no prediction has been made yet" do
      expect {
        agent.get_prediction
      }.to raise_error(IllegalStateException)
    end

    it "doesn't throw an IllegalStateException if a prediction has been " + 
      "made" do
      agent.prepare
      expect {
        agent.get_prediction
      }.to_not raise_error(IllegalStateException)
    end
  end
  
  describe "#increment_correct_prediction_count" do
    it "increases the agent's correct prediction count by 1" do
      expect {
        agent.increment_correct_prediction_count
      }.to change(agent, :correct_prediction_count).by(+1)
    end
  end
end