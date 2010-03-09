require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

import msci.mg.agents.AbstractAgent
import msci.mg.agents.RandomAgent
import msci.mg.agents.UnintelligentAgent
import msci.mg.Choice

describe RandomAgent do
  let(:agent) { RandomAgent.new }
  
  it "extends AbstractAgent" do
    agent.should be_a_kind_of(AbstractAgent)
  end
  
  it "implements the UnintelligentAgent interface" do
    agent.should be_a_kind_of(UnintelligentAgent)
  end
  
  describe "#choose" do
    it "chooses randomly between choice A and choice B" do
      choices = []

      100.times do
        agent.choose()
        choices << agent.choice
      end

      choice_a_count = choice_b_count = 0

      choices.each do |choice|
        case choice
        when Choice::A
          choice_a_count += 1
        when Choice::B
          choice_b_count += 1
        end
      end

      choice_a_count.should be_between(40, 60)
      choice_b_count.should be_between(40, 60)
      choice_a_count.should == 100 - choice_b_count
    end
  end
end