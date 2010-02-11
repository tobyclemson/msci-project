require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

describe MSci::MG::LearningAgent do
  let(:package) { MSci::MG }
  let(:klass) { package::LearningAgent }
  
  let(:strategy_manager) { Mockito.mock(package::StrategyManager.java_class) }
  let(:choice_memory) { Mockito.mock(package::ChoiceMemory.java_class) }
  
  let(:learning_agent) { klass.new(strategy_manager, choice_memory) }
  
  it "extends the AbstractAgent class" do
    learning_agent.should be_a_kind_of(package::AbstractAgent)
  end
end