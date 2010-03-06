require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

import edu.uci.ics.jung.graph.Graph
import java.lang.IllegalStateException
import java.lang.Integer
import java.util.ArrayList
import msci.mg.agents.AbstractAgent
import msci.mg.Choice
import msci.mg.ChoiceMemory
import msci.mg.Neighbourhood
import msci.mg.StrategyManager

describe AbstractAgent do
  AgentImplementation = Class.new(AbstractAgent) do
    field_accessor :choice, :prediction
      
    def choose(*args)
      self.choice = Choice::A
    end
      
    def prepare
      self.prediction = Choice::A
    end
  end

  let(:strategy_manager) { Mockito.mock(StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(ChoiceMemory.java_class) }
  
  let(:neighbourhood) { Mockito.mock(Neighbourhood.java_class)}
  
  let(:agent) { AgentImplementation.new(strategy_manager, choice_memory) }
  
  describe "constructor" do
    describe "with strategy manager and memory arguments" do
      it "sets the strategy_manager attribute to the supplied strategy " + 
        "manager" do
        agent.strategy_manager.should == strategy_manager
      end
      
      it "set the memory attribute to the supplied choice memory" do
        agent.memory.should == choice_memory
      end
      
      it "throws an IllegalArgumentException if the strategy key lengths " + 
        "are not equal to the memory capacity" do
        Mockito.when(strategy_manager.strategy_key_length).
          then_return(Integer.new(3))
        Mockito.when(choice_memory.capacity).
          then_return(Integer.new(2))
        
        expect {
          AgentImplementation.new(strategy_manager, choice_memory)
        }.to raise_error
      end
      
      it "sets the id attribute to a unique Integer" do
        agent_ids = (1..10).collect do
          agent = AgentImplementation.new(strategy_manager, choice_memory)
          agent.identification_number
        end
        
        agent_ids.uniq.size.should == 10
      end
    end
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
  
  describe "#best_friend" do
    it "returns this agent itself" do
      agent.best_friend.should == agent
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
  
  describe "#compare_to" do
    # agents are assigned id numbers sequentially so the later an agent is
    # created, the higher its id
    
    it "returns a negative Integer if the identification_number attribute " + 
      "of the supplied agent is greater than this agent's " + 
      "identification_number attribute" do
      agent_1 = AgentImplementation.new(strategy_manager, choice_memory)
      agent_2 = AgentImplementation.new(strategy_manager, choice_memory)
      
      agent_1.compare_to(agent_2).should be < 0
    end
    
    it "returns zero if the identification_number attribute of the " + 
      "supplied agent is equal to this agent's identification_number " +
      "attribute" do
      agent = AgentImplementation.new(strategy_manager, choice_memory)
      agent.compare_to(agent).should == 0
    end
    
    it "returns a positive Integer if the identification_number attribute " + 
      "of the supplied agent is smaller than this agent's " + 
      "identification_number attribute" do
      agent_1 = AgentImplementation.new(strategy_manager, choice_memory)
      agent_2 = AgentImplementation.new(strategy_manager, choice_memory)
      
      agent_2.compare_to(agent_1).should be > 0
    end
  end
  
  describe "#strategies" do
    it "returns the list of strategies stored in the strategy manager" do
      mock_strategies = Mockito.mock(ArrayList.java_class)
      
      Mockito.when(strategy_manager.get_strategies).
        then_return(mock_strategies)
      
      agent.strategies.should == strategy_manager.strategies
    end
  end
  
  describe "#choice" do
    it "throws an IllegalStateException if no choice has been made yet" do
      expect {
        agent.get_choice
      }.to raise_error(IllegalStateException)
    end
    
    it "doesn't throw an IllegalStateException if a choice has been made" do
      agent.choose
      expect {
        agent.get_choice
      }.to_not raise_error(IllegalStateException)
    end
  end
  
  describe "#prediction" do
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
  
  describe "#increment_score" do
    it "increases the agent's score by 1" do
      expect {
        agent.increment_score
      }.to change(agent, :score).by(+1)
    end
  end
  
  describe "#increment_correct_prediction_count" do
    it "increases the agent's correct prediction count by 1" do
      expect {
        agent.increment_correct_prediction_count
      }.to change(agent, :correct_prediction_count).by(+1)
    end
  end
  
  describe "#prepare" do
    it "does nothing" do
      # at least not that I can think of yet...
    end
  end
  
  describe "#update" do
    it "increments the score if the minority choice is equal to the " +
      "current choice" do
      expect {
        agent.choose
        agent.update(agent.choice)
      }.to change(agent, :score).by(+1)
    end
    
    it "doesn't increment the score if the minority choice is not equal " + 
      "to the current choice" do
      expect {
        agent.choose
        minority_choice = if(agent.choice == Choice::A)
          Choice::B
        else
          Choice::A
        end
        agent.update(minority_choice)
      }.to_not change(agent, :score)
    end
  end
end