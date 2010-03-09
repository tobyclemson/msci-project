require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

import java.lang.IllegalStateException
import msci.mg.agents.AbstractAgent
import msci.mg.agents.Agent
import msci.mg.Choice

describe AbstractAgent do
  ConcreteAgent = Class.new(AbstractAgent) do
    field_accessor :choice
      
    def choose(*args)
      self.choice = Choice::A
    end
  end
  
  let(:agent) { ConcreteAgent.new }
  
  it "implements the Agent interface" do
    agent.should be_a_kind_of(Agent)
  end
  
  describe "constructor" do
    it "sets the id attribute to a unique Integer" do
      agent_ids = (1..10).collect do
        ConcreteAgent.new.identification_number
      end

      agent_ids.map(&:to_string).uniq.size.should == 10
    end
  end

  describe "#compare_to" do
    # agents are assigned random uuids as their identification numbers
    # so we have to manually check which is smaller

    it "returns a positive integer if the identification number of the " +
      "calling agent is greater than the identification number of the " + 
      "supplied agent" do
      agent_1 = ConcreteAgent.new
      agent_2 = ConcreteAgent.new

      agent_1_id = agent_1.identification_number
      agent_2_id = agent_2.identification_number

      if agent_1_id.compare_to(agent_2_id) < 0
        larger_agent = agent_2
        smaller_agent = agent_1
      else
        larger_agent = agent_1
        smaller_agent = agent_2
      end
      
      larger_agent.compare_to(smaller_agent).should be > 0
    end

    it "returns zero if the identification_number attribute of the " + 
      "supplied agent is equal to this agent's identification_number " +
      "attribute" do
      agent = ConcreteAgent.new
      agent.compare_to(agent).should == 0
    end

    it "returns a negative integer if the identification number of the " +
      "calling agent is less than the identification number of the " + 
      "supplied agent" do
      agent_1 = ConcreteAgent.new
      agent_2 = ConcreteAgent.new

      agent_1_id = agent_1.identification_number
      agent_2_id = agent_2.identification_number

      if agent_1_id.compare_to(agent_2_id) < 0
        larger_agent = agent_2
        smaller_agent = agent_1
      else
        larger_agent = agent_1
        smaller_agent = agent_2
      end
      
      smaller_agent.compare_to(larger_agent).should be < 0
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

  describe "#increment_score" do
    it "increases the agent's score by 1" do
      expect {
        agent.increment_score
      }.to change(agent, :score).by(+1)
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
        minority_choice = agent.choice == Choice::A ? Choice::B : Choice::A
        agent.update(minority_choice)
      }.to_not change(agent, :score)
    end
  end
end