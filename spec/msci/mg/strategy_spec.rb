require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

import java.lang.IllegalArgumentException
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import msci.mg.Choice
import msci.mg.Strategy

describe Strategy do
  let(:a) { 
    choice_list = ArrayList.new
    choice_list.add(Choice::A)
    choice_list
  }
  let(:b) { 
    choice_list = ArrayList.new
    choice_list.add(Choice::B)
    choice_list
  }
  
  let(:strategy_map) {
    hash_map = HashMap.new
    hash_map.put(a, Choice::A)
    hash_map.put(b, Choice::B)
    hash_map
  }
  
  let(:strategy) { Strategy.new(strategy_map) }
  
  describe "constructor" do
    describe "with Map argument" do
      it "throws an IllegalArgumentException unless all of the keys in the " +
        "supplied Map are of the same length" do
        choice_list_with_2_elements = ArrayList.new
        choice_list_with_2_elements.add(Choice::A)
        choice_list_with_2_elements.add(Choice::B)
        
        choice_list_with_3_elements = ArrayList.new
        choice_list_with_3_elements.add(Choice::B)
        choice_list_with_3_elements.add(Choice::A)
        choice_list_with_3_elements.add(Choice::A)
          
        mappings = HashMap.new
        mappings.put(choice_list_with_2_elements, Choice::A)
        mappings.put(choice_list_with_3_elements, Choice::B)
        
        expect {
          Strategy.new(mappings)
        }.to raise_error(IllegalArgumentException)
      end
      
      it "throws an IllegalArgumentException unless all string " + 
        "permutations of the supplied key length are present in the " + 
        "supplied Map" do
        choice_a = Choice::A
        choice_b = Choice::B
          
        a_b = ArrayList.new
        b_a = ArrayList.new
        b_b = ArrayList.new
        
        a_b.add(choice_a)
        a_b.add(choice_b)
        
        b_a.add(choice_b)
        b_a.add(choice_a)
        
        b_b.add(choice_b)
        b_b.add(choice_b)
          
        mappings = HashMap.new()
        mappings.put(a_b, choice_a)
        mappings.put(b_a, choice_b)
        mappings.put(b_b, choice_b)
        
        expect {
          Strategy.new(mappings)
        }.to raise_error(IllegalArgumentException)
      end
      
      it "throws an IllegalArgumentException if an empty Map is " + 
        "supplied" do
        empty_map = HashMap.new()
        
        expect {
          Strategy.new(empty_map)
        }.to raise_error(IllegalArgumentException)
      end
      
      it "sets the mappings to the supplied map" do
        choice_a = Choice::A
        choice_b = Choice::B
          
        a_a = ArrayList.new
        a_b = ArrayList.new
        b_a = ArrayList.new
        b_b = ArrayList.new
        
        a_a.add(choice_a)
        a_a.add(choice_a)
        
        a_b.add(choice_a)
        a_b.add(choice_b)
        
        b_a.add(choice_b)
        b_a.add(choice_a)
        
        b_b.add(choice_b)
        b_b.add(choice_b)
        
        mappings = HashMap.new()
        mappings.put(a_a, choice_b)
        mappings.put(a_b, choice_a)
        mappings.put(b_a, choice_b)
        mappings.put(b_b, choice_b)
        
        strategy = Strategy.new(mappings)
        
        strategy.map.should == mappings
      end
    end
  end
  
  describe "#predict_minority_choice" do
    it "throws an IllegalArgumentException if the supplied key is of the " + 
      "wrong length" do
      a_a_a = ArrayList.new
      a_a_a.add(Choice::A)
      a_a_a.add(Choice::A)
      a_a_a.add(Choice::A)
      
      expect {
        strategy.predict_minority_choice(a_a_a)
      }.to raise_error(IllegalArgumentException)
    end
    
    it "returns the choice corresponding to the supplied key as passed in " +
      "the map to the constructor at initialisation" do
      choice_a = Choice::A
      choice_b = Choice::B
        
      a_a = ArrayList.new
      a_b = ArrayList.new
      b_a = ArrayList.new
      b_b = ArrayList.new
      
      a_a.add(choice_a)
      a_a.add(choice_a)
      
      a_b.add(choice_a)
      a_b.add(choice_b)
      
      b_a.add(choice_b)
      b_a.add(choice_a)
      
      b_b.add(choice_b)
      b_b.add(choice_b)
      
      mappings = HashMap.new()
      mappings.put(a_a, choice_b)
      mappings.put(a_b, choice_a)
      mappings.put(b_a, choice_b)
      mappings.put(b_b, choice_b)
      
      strategy = Strategy.new(mappings)
      
      mappings.each do |key, value|
        strategy.predict_minority_choice(key).should == value
      end
    end
  end

  describe "#key_length" do
    it "returns the length of the keys passed in to the constructor at " + 
      "initialisation" do
      choice_a = Choice::A
      choice_b = Choice::B
      
      a_a = ArrayList.new  
      a_b = ArrayList.new
      b_a = ArrayList.new
      b_b = ArrayList.new
      
      a_a.add(choice_a)
      a_a.add(choice_a)
      
      a_b.add(choice_a)
      a_b.add(choice_b)
      
      b_a.add(choice_b)
      b_a.add(choice_a)
      
      b_b.add(choice_b)
      b_b.add(choice_b)
        
      mappings = HashMap.new()
      mappings.put(a_a, choice_a)
      mappings.put(a_b, choice_a)
      mappings.put(b_a, choice_b)
      mappings.put(b_b, choice_b)
      
      strategy = Strategy.new(mappings)
      
      strategy.key_length.should == 2
    end
  end

  describe "#score" do
    it "is initially zero" do
      strategy.score.should == 0
    end
    
    it "returns a score equal to the number of times #increment_score has " + 
      "been called" do
      5.times { strategy.increment_score }
      strategy.score.should == 5
    end
  end

  describe "#map" do
    it "returns the map supplied at initialisation" do
      strategy.map.should equal(strategy_map)
    end
  end

  describe "valid_choice_histories" do
    it "returns a set of all permutations of choice A and B of the same " + 
      "length as those passed in at initialisation" do
      permutation_set = HashSet.new
      permutation_set.add(a)
      permutation_set.add(b)
      
      strategy.valid_choice_histories.should == permutation_set
    end
  end

  describe "#increment_score" do
    it "adds 1 to the current score" do
      expect {
        strategy.increment_score
      }.to change(strategy, :score).from(0).to(1)
    end
  end
end