require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

import java.lang.IllegalArgumentException
import java.util.ArrayList
import msci.mg.Choice
import msci.mg.ChoiceMemory

describe ChoiceMemory do
  let(:memory_capacity) { 3 }
  let(:initial_choices) {
    list = ArrayList.new
    list.add(Choice::A)
    list.add(Choice::B)
    list.add(Choice::B)
    list
  }
  
  let(:choice_memory) { ChoiceMemory.new(memory_capacity, initial_choices) }
  
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
        list = ArrayList.new
        list.add(Choice::A)
        list.add(Choice::B)
        list.add(Choice::B)
        list.add(Choice::B)
        
        choice_memory = ChoiceMemory.new(3, list)
        
        choice_memory.fetch.to_a.should == [Choice::B, Choice::B, Choice::B]
      end
      
      it "throws an IllegalArgumentException if the supplied list of " + 
        "initial choices contains fewer choices than the specified " + 
        "capacity" do
        initial_choices = ArrayList.new
        initial_choices.add(Choice::A)
        
        memory_capacity = 4
        
        expect {
          ChoiceMemory.new(memory_capacity, initial_choices)
        }.to raise_error(IllegalArgumentException)
      end
    end
  end
  
  describe "#add and #fetch" do
    let(:a) { Choice::A }
    let(:b) { Choice::B }
    
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
        choice_list = ArrayList.new
        
        memory.each do |choice|
          choice_memory.add(choice)
          choice_list.add(choice)
        end
        
        choice_memory.fetch.should == choice_list
      end
    end
  end
end