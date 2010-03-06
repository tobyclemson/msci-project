require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

import java.util.ArrayList
import msci.mg.Choice
import msci.mg.Strategy
import msci.mg.StrategySpace

describe StrategySpace do
  let(:strategy_space) { StrategySpace.new(2) }
  
  describe "constructor" do
    describe "with a key length argument" do
      it "sets the key_length attribute to the supplied length" do
        strategy_space.key_length.should == 2
      end
    end
  end
  
  describe "setters" do
    describe "#set_key_length" do
      it "sets the key_length to the supplied integer" do
        strategy_space.set_key_length(10)
        strategy_space.key_length.should == 10
      end
    end
  end
  
  describe "#generate_strategy" do
    it "returns an instance of Strategy" do
      returned_object = strategy_space.generate_strategy
      returned_object.should be_a_kind_of(Strategy)
    end
    
    it "generates a strategy with keys of the length specified in the " + 
      "strategy space" do
      strategy = strategy_space.generate_strategy  
      strategy.key_length.should == strategy_space.key_length
    end
      
    it "generates a strategy with a key set corresponding to all possible " +
      "keys of the specified key length" do
      strategy_space = StrategySpace.new(2)
      strategy = strategy_space.generate_strategy

      key_set = strategy.map.key_set
      
      a = Choice::A
      b = Choice::B
      
      a_a = ArrayList.new
      a_b = ArrayList.new
      b_a = ArrayList.new
      b_b = ArrayList.new
      
      a_a.add(a)
      a_a.add(a)
      
      a_b.add(a)
      a_b.add(b)
      
      b_a.add(b)
      b_a.add(a)
      
      b_b.add(b)
      b_b.add(b)
      
      [a_a, a_b, b_a, b_b].each do |key|
        key_set.contains(key).should be_true
      end
    end
    
    it "generates a random strategy each time" do
      strategy_space = StrategySpace.new(3)
      strategies = (1..256).collect { strategy_space.generate_strategy }
      strategies.uniq.length.should be >= 0.6 * 256
    end
  end
end