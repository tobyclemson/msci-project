require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::RandomAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::RandomAgent }
  
  let(:agent_memory_size) { 2 }
  
  let(:strategy_manager) { 
    Mockito.mock(package::StrategyManager.java_class) 
  }
  
  let(:choice_memory) { 
    package::ChoiceMemory.new(
      package::ChoiceHistory.new(agent_memory_size), agent_memory_size
    )
  }
  
  let(:random_agent_instance) { 
    klass.new(strategy_manager, choice_memory) 
  }
  
  before(:each) do
    Mockito.when(strategy_manager.strategy_key_length).
      then_return(Java::JavaLang::Integer.new(agent_memory_size))
  end
  
  it "extends AbstractAgent" do
    random_agent_instance.should be_a_kind_of(package::AbstractAgent)
  end
  
  it "chooses randomly between choice A and choice B" do
    choices = []
    
    100.times do
      random_agent_instance.choose()
      choices << random_agent_instance.choice
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