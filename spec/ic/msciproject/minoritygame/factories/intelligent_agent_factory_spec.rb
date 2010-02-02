require File.join(
  File.dirname(__FILE__), '..', '..', '..', '..', 'spec_helper.rb'
)

describe MSciProject::MinorityGame::Factories::IntelligentAgentFactory do
  let(:package) { MSciProject::MinorityGame::Factories }
  let(:klass) {
    Class.new(package::IntelligentAgentFactory) do
      def create; end
    end
  }
  
  let(:factory_interface) {
    Java::OrgApacheCommonsCollections15::Factory
  }
  
  let(:memory_capacity_factory) { 
    Mockito.mock(factory_interface.java_class) 
  }
  let(:number_of_strategies_factory) {
    Mockito.mock(factory_interface.java_class)
  }
  let(:initial_choice_memory) {
    Mockito.mock(Java::JavaUtil::List.java_class)
  }
  
  let(:factory) { 
    klass.new(
      memory_capacity_factory, 
      number_of_strategies_factory,
      initial_choice_memory
    ) 
  }
  
  it "extends the AgentFactory class" do
    factory.should be_a_kind_of(package::AgentFactory)
  end
  
  describe "constructor" do
    it "sets the memory_capacity_factory attribute to the supplied factory" do
      factory.memory_capacity_factory.should == memory_capacity_factory
    end
    
    it "sets the number_of_strategies attribute to the supplied value" do
      factory.number_of_strategies_factory.should == 
        number_of_strategies_factory
    end
    
    it "sets the initial_choice_memory attribute to the supplied list of " + 
      "choices" do
      factory.initial_choice_memory.should == initial_choice_memory
    end
  end
  
  describe "setters" do
    describe "#memory_capacity_factory=" do
      it "sets the memory_capacity_factory attribute to the supplied " + 
        "factory" do
        new_memory_capacity_factory = Mockito.mock(
          factory_interface.java_class
        )
        factory.memory_capacity_factory = new_memory_capacity_factory
        factory.memory_capacity_factory.should == new_memory_capacity_factory
      end
    end
    
    describe "#number_of_strategies_factory=" do
      it "sets the number_of_strategies attribute to the supplied value" do
        new_number_of_strategies_factory = Mockito.mock(
          factory_interface.java_class
        )
        factory.number_of_strategies_factory = 
          new_number_of_strategies_factory
        factory.number_of_strategies_factory.should == 
          new_number_of_strategies_factory
      end
    end
    
    describe "#initial_choice_memory=" do
      it "sets the initial_choice_memory attribute to the supplied value" do
        
      end
    end
  end
end