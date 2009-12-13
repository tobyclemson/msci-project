require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::LearningAgent do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::LearningAgent }
  
  let(:agent_memory_size) { 2 }
  
  let(:strategy_manager) { 
    Mockito.mock(package::StrategyManager.java_class)
  }
  
  let(:choice_memory) { 
    package::ChoiceMemory.new(
      package::ChoiceHistory.new(agent_memory_size), agent_memory_size
    )
  }
  
  let(:learning_agent_instance) { 
    klass.new(strategy_manager, choice_memory) 
  }
  
  before(:each) do
    Mockito.when(strategy_manager.strategy_key_length).
      then_return(Java::JavaLang::Integer.new(agent_memory_size))
  end
  
  it "extends the AbstractAgent class" do
    learning_agent_instance.should be_a_kind_of(package::AbstractAgent)
  end
end