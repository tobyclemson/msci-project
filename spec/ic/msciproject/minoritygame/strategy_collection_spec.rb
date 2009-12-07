require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::StrategyCollection do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::StrategyCollection }
  let(:strategy_collection_instance) { klass.new }
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
  let(:strategy) { 
    hash_map = Java::JavaUtil::HashMap.new
    hash_map.put(a, package::Choice::A)
    hash_map.put(b, package::Choice::B)
    package::Strategy.new(hash_map) 
  }
  
  it "extends the AbstractCollection class" do
    strategy_collection_instance.should be_a_kind_of(
      Java::JavaUtil::AbstractCollection
    )
  end
  
  describe "public interface" do
    it "has a random_strategy instance method" do
      strategy_collection_instance.should respond_to(:random_strategy)
    end
    
    it "has a highest_scoring_strategy instance method" do
      strategy_collection_instance.should respond_to(
        :highest_scoring_strategy
      )
    end
    
    it "has a increment_scores_for_choice instance method" do
      strategy_collection_instance.should respond_to(
        :increment_scores_for_choice
      )
    end
  end
  
  describe "constructor" do
    describe "with no arguments" do
      it "initialises an empty collection" do
        strategy_collection = klass.new
        strategy_collection.size.should == 0
      end
    end
    
    describe "with another StrategyCollection instance as an argument" do
      let(:strategy_collection_to_copy) { klass.new }
      
      before(:each) do
        strategy_collection_to_copy.add(strategy)
      end
      
      it "adds all Strategy objects in the supplied StrategyCollection to " +
        "the collection" do
        strategy_collection = klass.new(strategy_collection_to_copy)
        strategy_collection.contains(strategy).should be_true
      end
      
      it "has independent references to the Strategy objects" do
        strategy_collection = klass.new(strategy_collection_to_copy)
        strategy_collection_to_copy.remove(strategy)
        strategy_collection.contains(strategy).should be_true
      end
    end
  end
  
  describe "#add" do
    it "adds the supplied Strategy to the collection" do
      strategy_collection_instance.add(strategy) 
      strategy_collection_instance.contains(strategy).should be true
    end
  end
  
  describe "#size" do
    it "returns the number of strategies in the collection" do
      3.times do 
        strategy_collection_instance.add(strategy)
      end
      strategy_collection_instance.size.should == 3
    end
  end
  
  describe "#iterator" do
    it "returns an Iterator" do
      strategy_collection_instance.iterator.should be_a_kind_of(
        Java::JavaUtil::Iterator
      )
    end
  end

  describe "#random_strategy" do
    let(:strategy_1) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_2) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_3) { Mockito.mock(package::Strategy.java_class) }
    
    before(:each) do
      [strategy_1, strategy_2, strategy_3].each do |strategy|
        strategy_collection_instance.add(strategy)
      end
    end
    
    it "returns strategies randomly with uniform distribution" do
      count_1 = count_2 = count_3 = 0;
      
      10000.times do
        case strategy_collection_instance.random_strategy
          when strategy_1 then count_1 += 1
          when strategy_2 then count_2 += 1
          when strategy_3 then count_3 += 1
        end
      end
      
      [count_1, count_2, count_3].each do |count|
        count.should be_between(3100, 3500)
      end
    end
  end

  describe "#highest_scoring_strategy" do
    let(:strategy_1) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_2) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_3) { Mockito.mock(package::Strategy.java_class) }
    
    before(:each) do
      [strategy_1, strategy_2, strategy_3].each do |strategy|
        strategy_collection_instance.add(strategy)
      end
    end
    
    it "returns the highest scoring strategy if there is only one with " + 
      "that score" do
      Mockito.when(strategy_1.score).
        then_return(Java::JavaLang::Integer.new(5))
      Mockito.when(strategy_2.score).
        then_return(Java::JavaLang::Integer.new(2))
      Mockito.when(strategy_3.score).
        then_return(Java::JavaLang::Integer.new(7))  
        
      strategy_collection_instance.
        highest_scoring_strategy.should == strategy_3
    end
    
    it "returns one of the highest scoring strategies at random if there " +
      "is more than one with that score" do
      Mockito.when(strategy_1.score).
        then_return(Java::JavaLang::Integer.new(7))
      Mockito.when(strategy_2.score).
        then_return(Java::JavaLang::Integer.new(2))
      Mockito.when(strategy_3.score).
        then_return(Java::JavaLang::Integer.new(7))
        
        count_1 = count_2 = count_3 = 0;

      1000.times do
        case strategy_collection_instance.highest_scoring_strategy
          when strategy_1 then count_1 += 1
          when strategy_2 then count_2 += 1
          when strategy_3 then count_3 += 1
        end
      end
      
      count_2.should == 0
      
      [count_1, count_3].each do |count|
        count.should be_between(460, 540)
      end
    end
    
    it "runs in less than five hundredths of a second" do
      start_time = Time.now
      1000.times { strategy_collection_instance.highest_scoring_strategy }
      end_time = Time.now
      
      (end_time - start_time).should be < 2
    end
  end

  describe "#increment_scores_for_choice" do
    let(:strategy_1) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_2) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_3) { Mockito.mock(package::Strategy.java_class) }
    let(:a_b) { Mockito.mock(Java::JavaUtil::List.java_class) }
    
    before(:each) do
      [strategy_1, strategy_2, strategy_3].each do |strategy|
        strategy_collection_instance.add(strategy)
      end
      
      Mockito.when(strategy_1.predict_minority_choice(a_b)).
        then_return(package::Choice::A)
      Mockito.when(strategy_2.predict_minority_choice(a_b)).
        then_return(package::Choice::A)
      Mockito.when(strategy_3.predict_minority_choice(a_b)).
        then_return(package::Choice::B)
    end
    
    it "calls increment_score on strategies that give the supplied " +
      "choice for the supplied choice history" do
      strategy_collection_instance.increment_scores_for_choice(
        a_b, package::Choice::A
      )
      
      Mockito.verify(strategy_1).increment_score
      Mockito.verify(strategy_2).increment_score
    end
    
    it "doesn't call increment_score on strategies that don't give the " +
      "supplied choice for the supplied choice history" do
      strategy_collection_instance.increment_scores_for_choice(
        a_b, package::Choice::A
      )

      Mockito.verify(strategy_3, Mockito.never).increment_score
    end
  end
end