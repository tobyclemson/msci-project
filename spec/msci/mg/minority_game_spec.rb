require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

import java.lang.Integer
import java.util.ArrayList
import java.util.HashMap
import msci.mg.Choice
import msci.mg.ChoiceHistory
import msci.mg.Community
import msci.mg.MinorityGame

describe MinorityGame do
  let(:community) { Mockito.mock(Community.java_class) }
  let(:choice_history) { Mockito.mock(ChoiceHistory.java_class) }
  
  let(:minority_game) { MinorityGame.new(community, choice_history) }
  
  describe "constructor" do
    describe "with community and choice history arguments" do
      it "sets the community attribute to the supplied Community " + 
        "instance" do
        minority_game.community.should == community
      end

      it "sets the choice_history attribute to the supplied ChoiceHistory " + 
        "instance" do
        minority_game.choice_history.should == choice_history
      end
    end
  end

  describe "agents" do
    it "returns the agents stored in the community manager" do
      agents = Mockito.mock(ArrayList.java_class)
      Mockito.when(community.get_agents).then_return(agents)
      minority_game.agents.should equal(agents)
    end
  end

  describe "#minority_size" do
    it "returns the number of agents in the minority group at the end of " + 
      "the step" do
      choice_totals = HashMap.new
      choice_totals.put(Choice::A, Integer.new(15))
      choice_totals.put(Choice::B, Integer.new(12))
      
      Mockito.when(community.get_choice_totals).then_return(choice_totals)
      
      minority_game.minority_size.should == 12
    end
  end

  describe "#minority_choice" do
    it "returns one of Choice.A or Choice.B representing the choice that " + 
      "the minority of agents have made in this step" do
      choice_totals = HashMap.new
      choice_totals.put(Choice::A, Integer.new(15))
      choice_totals.put(Choice::B, Integer.new(12))

      Mockito.when(community.choice_totals).then_return(choice_totals)

      minority_game.minority_choice.should == Choice::B
    end
  end
  
  describe "#step_forward" do
    let(:choice_totals) {
      hash = HashMap.new
      hash.put(Choice::A, Integer.new(15))
      hash.put(Choice::B, Integer.new(12))
      hash
    }

    before(:each) do
      Mockito.when(community.choice_totals).then_return(choice_totals)
    end
    
    it "tells all agents to prepare to make a choice for this step" do
      minority_game.step_forward
      Mockito.verify(community).prepare_agents
    end

    it "tells all agents to make their choice for this step" do
      minority_game.step_forward
      Mockito.verify(community).make_choices
    end

    it "tells all agents to update given the minority choice and choice " + 
      "history" do
      minority_game.step_forward
      Mockito.verify(community).update_agents(Choice::B)
    end

    it "adds the minority choice to the choice history" do
      minority_game.step_forward
      Mockito.verify(choice_history).add(Choice::B)
    end
  end
end