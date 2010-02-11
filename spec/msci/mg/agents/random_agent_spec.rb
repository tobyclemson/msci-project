require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSci::MG::Agents::RandomAgent do
  let(:package) { MSci::MG }
  let(:klass) { package::Agents::RandomAgent }
  
  let(:strategy_manager) { Mockito.mock(package::StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(package::ChoiceMemory.java_class) }
  
  let(:random_agent) { klass.new(strategy_manager, choice_memory) }
  
  it "extends AbstractAgent" do
    random_agent.should be_a_kind_of(package::Agents::AbstractAgent)
  end
  
  describe "#choose" do
    it "chooses randomly between choice A and choice B" do
      choices = []

      100.times do
        random_agent.choose()
        choices << random_agent.choice
      end

      choice_a_count = choice_b_count = 0

      choices.each do |choice|
        case choice
        when package::Choice::A
          choice_a_count += 1
        when package::Choice::B
          choice_b_count += 1
        end
      end

      choice_a_count.should be_between(40, 60)
      choice_b_count.should be_between(40, 60)
      choice_a_count.should == 100 - choice_b_count
    end
  end
  
  describe "#increment_score" do
    it "increases the agents score by 1" do
      expect {
        random_agent.increment_score
      }.to change(random_agent, :score).from(0).to(1)
    end
  end
  
  describe "#update" do
    it "increments the score if the minority choice is equal to the " +
      "current choice" do
      expect {
        random_agent.choose
        random_agent.update(random_agent.choice)
      }.to change(random_agent, :score)
    end

    it "doesn't increment the score if the minority choice is not equal " + 
      "to the current choice" do
      expect {
        random_agent.choose
        minority_choice = if(random_agent.choice == package::Choice::A)
          package::Choice::B
        else
          package::Choice::A
        end
        random_agent.update(minority_choice)
      }.to_not change(random_agent, :score)
    end
  end
end