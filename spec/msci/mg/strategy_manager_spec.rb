require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

describe MSci::MG::StrategyManager do
  let(:package) { MSci::MG }
  let(:klass) { package::StrategyManager }
  
  let(:integer) { Java::JavaLang::Integer }
  let(:array_list) { Java::JavaUtil::ArrayList }
  
  let(:strategies) { 
    mock = Mockito.mock(Java::JavaUtil::ArrayList.java_class) 
    strategy = Mockito.mock(package::Strategy.java_class)
    Mockito.when(mock.get(Mockito.any)).
      then_return(strategy)
    mock
  }
  
  let(:strategy_manager) { klass.new(strategies) }
  
  describe "public interface" do
    it "has a random_strategy instance method" do
      strategy_manager.should respond_to(:random_strategy)
    end
    
    it "has a highest_scoring_strategy instance method" do
      strategy_manager.should respond_to(:highest_scoring_strategy)
    end
    
    it "has an increment_scores instance method" do
      strategy_manager.should respond_to(:increment_scores)
    end
    
    it "has a number_of_strategies instance method" do
      strategy_manager.should respond_to(:number_of_strategies)
    end
    
    it "has a strategy_key_length instance method" do
      strategy_manager.should respond_to(:strategy_key_length)
    end
    
    it "has a strategies instance method" do
      strategy_manager.should respond_to(:strategies)
    end
  end
  
  describe "constructor" do
    describe "with a List of Strategy objects" do
      it "sets the strategies attribute to the supplied strategies" do
        strategy_manager.strategies.should == strategies
      end
    end
  end

  describe "#number_of_strategies" do
    it "returns the number of strategies managed by the strategy manager" do
      Mockito.when(strategies.size).then_return(integer.new(3))
      strategy_manager.number_of_strategies.should == 3
    end
  end
  
  describe "#strategy_key_length" do
    it "returns the length of the keys of the strategies passed in to the " +
      "constructor" do
        strategy = Mockito.mock(package::Strategy.java_class)
        
        Mockito.when(strategy.get_key_length).then_return(integer.new(3))
        Mockito.when(strategies.get(Mockito.any())).then_return(strategy)
        
        strategy_manager.strategy_key_length.should == 3
    end
  end

  describe "#random_strategy" do
    let(:strategy_1) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_2) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_3) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_manager) {
      strategies = Java::JavaUtil::ArrayList.new
      
      [strategy_1, strategy_2, strategy_3].each do |mock_strategy|
        strategies.add(mock_strategy)
      end
      
      klass.new(strategies)
    }
    
    it "returns strategies randomly with uniform distribution" do
      count_1 = count_2 = count_3 = 0;
      
      10000.times do
        case strategy_manager.random_strategy
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
    let(:strategy_4) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_manager) {
      strategies = Java::JavaUtil::ArrayList.new
      
      [strategy_1, strategy_2, strategy_3, strategy_4].each do |mock_strategy|
        strategies.add(mock_strategy)
      end
      
      klass.new(strategies)
    }
    
    it "returns the highest scoring strategy if there is only one with " + 
      "that score" do
      Mockito.when(strategy_1.score).
        then_return(Java::JavaLang::Integer.new(5))
      Mockito.when(strategy_2.score).
        then_return(Java::JavaLang::Integer.new(2))
      Mockito.when(strategy_3.score).
        then_return(Java::JavaLang::Integer.new(7))
      Mockito.when(strategy_4.score).
        then_return(Java::JavaLang::Integer.new(2))
        
      strategy_manager.
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
      Mockito.when(strategy_4.score).
        then_return(Java::JavaLang::Integer.new(7))
        
      count_1 = count_2 = count_3 = count_4 = 0;

      1000.times do
        case strategy_manager.highest_scoring_strategy
          when strategy_1 then count_1 += 1
          when strategy_2 then count_2 += 1
          when strategy_3 then count_3 += 1
          when strategy_4 then count_4 += 1
        end
      end
      
      count_2.should == 0
      
      non_zero_counts = [count_1, count_3, count_4]
      
      non_zero_counts.each do |count|
        count.should be_between(300, 360)
      end
      
      non_zero_counts.inject(0) { |memo, obj| memo + obj }.should == 1000
    end
    
    it "runs in less than five thousandths of a second" do
      start_time = Time.now
      1000.times { strategy_manager.highest_scoring_strategy }
      end_time = Time.now
      
      (end_time - start_time).should be < 5
    end
  end

  describe "#increment_scores" do
    let(:strategy_1) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_2) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_3) { Mockito.mock(package::Strategy.java_class) }
    let(:strategy_manager) {
      strategies = Java::JavaUtil::ArrayList.new
      
      [strategy_1, strategy_2, strategy_3].each do |mock_strategy|
        strategies.add(mock_strategy)
      end
      
      klass.new(strategies)
    }
    let(:past_choices) { Mockito.mock(Java::JavaUtil::List.java_class) }
        
    before(:each) do
      Mockito.when(strategy_1.predict_minority_choice(past_choices)).
        then_return(package::Choice::A)
      Mockito.when(strategy_2.predict_minority_choice(past_choices)).
        then_return(package::Choice::A)
      Mockito.when(strategy_3.predict_minority_choice(past_choices)).
        then_return(package::Choice::B)
    end
    
    it "calls increment_score on strategies that give the supplied " +
      "choice for the supplied choice history" do
      strategy_manager.increment_scores(
        past_choices, package::Choice::A
      )
      
      Mockito.verify(strategy_1).increment_score
      Mockito.verify(strategy_2).increment_score
    end
    
    it "doesn't call increment_score on strategies that don't give the " +
      "supplied choice for the supplied choice history" do
      strategy_manager.increment_scores(
        past_choices, package::Choice::A
      )

      Mockito.verify(strategy_3, Mockito.never).increment_score
    end
  end
end