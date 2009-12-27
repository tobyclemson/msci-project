require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::ChoiceMemory do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::ChoiceMemory }
  
  let(:choice_history) { Mockito.mock(package::ChoiceHistory.java_class) }
  let(:memory_capacity) { 3 }
  
  let(:choice_memory) { 
    package::ChoiceMemory.new(choice_history, memory_capacity) 
  }
  
  describe "public interface" do
    it "has a capacity instance method" do
      choice_memory.should respond_to(:capacity)
    end
    
    it "has a choice_history_provider instance method" do
      choice_memory.should respond_to(:choice_history_provider)
    end
    
    it "has a fetch instance method" do
      choice_memory.should respond_to(:fetch)
    end
  end
  
  describe "constructor" do
    describe "with a ChoiceHistory object representing the full choice " + 
      "history and an integer representing the memory capacity" do
      it "sets the capacity attribute to the supplied integer" do
        choice_memory.capacity.should == memory_capacity
      end
      
      it "sets the choice_history_provider attrubute to the supplied " + 
        "ChoiceHistory instance" do
        choice_memory.choice_history_provider.should == choice_history
      end
    end
  end
  
  describe "#fetch" do
    it "retrieves as many past choices from the supplied choice memory as " +
      "it has capacity to remember" do
      memory = choice_memory.fetch
      Mockito.verify(choice_history).as_list(memory_capacity)
    end
  end
end