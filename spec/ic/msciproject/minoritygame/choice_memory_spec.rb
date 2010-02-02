require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::ChoiceMemory do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::ChoiceMemory }
  
  let(:memory_capacity) { 3 }
  let(:initial_choices) {
    list = Java::JavaUtil::ArrayList.new
    list.add(package::Choice::A)
    list.add(package::Choice::B)
    list.add(package::Choice::B)
    list
  }
  
  let(:choice_memory) { 
    klass.new(memory_capacity, initial_choices) 
  }
  
  describe "public interface" do
    it "has a capacity instance method" do
      choice_memory.should respond_to(:capacity)
    end
    
    it "has an add instance method" do
      choice_memory.should respond_to(:add)
    end
    
    it "has a fetch instance method" do
      choice_memory.should respond_to(:fetch)
    end
  end
  
  describe "constructor" do
    describe "with an integer representing the memory capacity and a list " + 
      "of initial choices" do
      it "sets the capacity attribute to the supplied integer" do
        choice_memory.capacity.should == memory_capacity
      end
      
      it "sets the initial memory contents to the supplied list of choices" + 
        "if the size of the supplied list and the required capacity are " + 
        "equal" do
        choice_memory.fetch.to_a.should == initial_choices.to_a
      end
      
      it "sets the initial memory contents to the elements with highest " + 
        "index if the size of the supplied list of choices is greater than " +
        "the required capacity" do
        list = Java::JavaUtil::ArrayList.new
        list.add(package::Choice::A)
        list.add(package::Choice::B)
        list.add(package::Choice::B)
        list.add(package::Choice::B)
        
        choice_memory = klass.new(3, list)
        
        choice_memory.fetch.to_a.should == 
          [package::Choice::B, package::Choice::B, package::Choice::B]
      end
      
      it "throws an IllegalArgumentException if the supplied list of " + 
        "initial choices contains fewer choices than the specified " + 
        "capacity" do
        initial_choices = Java::JavaUtil::ArrayList.new
        initial_choices.add(package::Choice::A)
        
        memory_capacity = 4
        
        expect {
          klass.new(memory_capacity, initial_choices)
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
    end
  end
  
  describe "#add and #fetch" do
    let(:a) { package::Choice::A }
    let(:b) { package::Choice::B }
    
    it "adds the supplied choice to the memory" do
      [a, a, b, a, b, b, b, b, a, b].each do |choice|
        choice_memory.add(choice)
        contents = choice_memory.fetch
        index_of_most_recent_entry = choice_memory.capacity - 1
        contents.get(index_of_most_recent_entry).should == choice
      end
    end
    
    it "retrieves the most recent choices added to the memory with number " + 
      "equal to the capacity" do
      [[a,b,a], [a,a,a], [b,a,a], [b,b,a]].each do |memory|
        choice_list = Java::JavaUtil::ArrayList.new
        
        memory.each do |choice|
          choice_memory.add(choice)
          choice_list.add(choice)
        end
        
        choice_memory.fetch.should == choice_list
      end
    end
  end
end