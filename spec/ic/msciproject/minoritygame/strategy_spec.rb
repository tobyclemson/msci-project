require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::Strategy do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::Strategy }
  
  let(:a) { 
    choice_list = Java::JavaUtil::ArrayList.new
    choice_list.add(package::Choice::A)
    choice_list
  }
  let(:b) { 
    choice_list = Java::JavaUtil::ArrayList.new
    choice_list.add(package::Choice::B)
    choice_list
  }
  
  let(:strategy_map) {
    hash_map = Java::JavaUtil::HashMap.new
    hash_map.put(a, package::Choice::A)
    hash_map.put(b, package::Choice::B)
    hash_map
  }
  
  let(:strategy) { klass.new(strategy_map) }
  
  describe "public interface" do
    it "has a score instance method" do
      strategy.should respond_to(:score)
    end
    
    it "has an increment_score instance method" do
      strategy.should respond_to(:increment_score)
    end
    
    it "has a key_length instance method" do
      strategy.should respond_to(:key_length)
    end
    
    it "has a prediction instance method" do
      strategy.should respond_to(:predict_minority_choice)
    end
    
    it "has a map instance method" do
      strategy.should respond_to(:map)
    end
    
    it "has a valid_choice_histories instance method" do
      strategy.should respond_to(:valid_choice_histories)
    end
  end
  
  describe "constructor" do
    describe "with Map argument" do
      it "throws an IllegalArgumentException unless all of the keys in the " +
        "supplied Map are of the same length" do
        choice_list_with_2_elements = Java::JavaUtil::ArrayList.new
        choice_list_with_2_elements.add(package::Choice::A)
        choice_list_with_2_elements.add(package::Choice::B)
        
        choice_list_with_3_elements = Java::JavaUtil::ArrayList.new
        choice_list_with_3_elements.add(package::Choice::B)
        choice_list_with_3_elements.add(package::Choice::A)
        choice_list_with_3_elements.add(package::Choice::A)
          
        mappings = Java::JavaUtil::HashMap.new
        mappings.put(choice_list_with_2_elements, package::Choice::A)
        mappings.put(choice_list_with_3_elements, package::Choice::B)
        
        expect {
          klass.new(mappings)
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
      
      it "throws an IllegalArgumentException unless all string " + 
        "permutations of the supplied key length are present in the " + 
        "supplied Map" do
        choice_a = package::Choice::A
        choice_b = package::Choice::B
          
        a_b = Java::JavaUtil::ArrayList.new
        b_a = Java::JavaUtil::ArrayList.new
        b_b = Java::JavaUtil::ArrayList.new
        
        a_b.add(choice_a)
        a_b.add(choice_b)
        
        b_a.add(choice_b)
        b_a.add(choice_a)
        
        b_b.add(choice_b)
        b_b.add(choice_b)
          
        mappings = Java::JavaUtil::HashMap.new()
        mappings.put(a_b, choice_a)
        mappings.put(b_a, choice_b)
        mappings.put(b_b, choice_b)
        
        expect {
          klass.new(mappings)
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
      
      it "throws an IllegalArgumentException if an empty Map is " + 
        "supplied" do
        empty_map = Java::JavaUtil::HashMap.new()
        
        expect {
          klass.new(empty_map)
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
      
      it "sets the mappings to the supplied map" do
        choice_a = package::Choice::A
        choice_b = package::Choice::B
          
        a_a = Java::JavaUtil::ArrayList.new
        a_b = Java::JavaUtil::ArrayList.new
        b_a = Java::JavaUtil::ArrayList.new
        b_b = Java::JavaUtil::ArrayList.new
        
        a_a.add(choice_a)
        a_a.add(choice_a)
        
        a_b.add(choice_a)
        a_b.add(choice_b)
        
        b_a.add(choice_b)
        b_a.add(choice_a)
        
        b_b.add(choice_b)
        b_b.add(choice_b)
        
        mappings = Java::JavaUtil::HashMap.new()
        mappings.put(a_a, choice_b)
        mappings.put(a_b, choice_a)
        mappings.put(b_a, choice_b)
        mappings.put(b_b, choice_b)
        
        strategy = klass.new(mappings)
        
        strategy.map.should == mappings
      end
    end
  end
  
  describe "#predict_minority_choice" do
    it "throws an IllegalArgumentException if the supplied key is of the " + 
      "wrong length" do
      a_a_a = Java::JavaUtil::ArrayList.new
      a_a_a.add(package::Choice::A)
      a_a_a.add(package::Choice::A)
      a_a_a.add(package::Choice::A)
      
      expect {
        strategy.predict_minority_choice(a_a_a)
      }.to raise_error(Java::JavaLang::IllegalArgumentException)
    end
    
    it "returns the choice corresponding to the supplied key as passed in " +
      "the map to the constructor at initialisation" do
      choice_a = package::Choice::A
      choice_b = package::Choice::B
        
      a_a = Java::JavaUtil::ArrayList.new
      a_b = Java::JavaUtil::ArrayList.new
      b_a = Java::JavaUtil::ArrayList.new
      b_b = Java::JavaUtil::ArrayList.new
      
      a_a.add(choice_a)
      a_a.add(choice_a)
      
      a_b.add(choice_a)
      a_b.add(choice_b)
      
      b_a.add(choice_b)
      b_a.add(choice_a)
      
      b_b.add(choice_b)
      b_b.add(choice_b)
      
      mappings = Java::JavaUtil::HashMap.new()
      mappings.put(a_a, choice_b)
      mappings.put(a_b, choice_a)
      mappings.put(b_a, choice_b)
      mappings.put(b_b, choice_b)
      
      strategy = klass.new(mappings)
      
      mappings.each do |key, value|
        strategy.predict_minority_choice(key).should == value
      end
    end
  end

  describe "#key_length" do
    it "returns the length of the keys passed in to the constructor at " + 
      "initialisation" do
      choice_a = package::Choice::A
      choice_b = package::Choice::B
      
      a_a = Java::JavaUtil::ArrayList.new  
      a_b = Java::JavaUtil::ArrayList.new
      b_a = Java::JavaUtil::ArrayList.new
      b_b = Java::JavaUtil::ArrayList.new
      
      a_a.add(choice_a)
      a_a.add(choice_a)
      
      a_b.add(choice_a)
      a_b.add(choice_b)
      
      b_a.add(choice_b)
      b_a.add(choice_a)
      
      b_b.add(choice_b)
      b_b.add(choice_b)
        
      mappings = Java::JavaUtil::HashMap.new()
      mappings.put(a_a, choice_a)
      mappings.put(a_b, choice_a)
      mappings.put(b_a, choice_b)
      mappings.put(b_b, choice_b)
      
      strategy = klass.new(mappings)
      
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
      permutation_set = Java::JavaUtil::HashSet.new
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