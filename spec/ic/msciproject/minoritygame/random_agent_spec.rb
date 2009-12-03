require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::RandomAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::RandomAgent }
  let(:empty_strategy_collection) { package::StrategyCollection.new }
  let(:random_agent_instance) { klass.new(empty_strategy_collection) }
  
  it "extends AbstractAgent" do
    random_agent_instance.should be_a_kind_of(package::AbstractAgent)
  end
  
  it "chooses randomly between choice A and choice B" do
    choices = []
    
    100.times do
      random_agent_instance.choose(Java::JavaUtil::ArrayList.new)
      choices << random_agent_instance.last_choice
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
    
    choice_a_count.should be_between(42, 58)
    choice_b_count.should be_between(42, 58)
    choice_a_count.should == 100 - choice_b_count
  end
end