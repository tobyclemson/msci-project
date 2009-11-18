require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::StrategySpace do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::StrategySpace }
  let(:strategy_space_instance) { klass.new(2) }
  
  describe "constructor" do
    describe "with a key length argument" do
      it "sets the key_length attribute to the supplied length" do
        strategy_space = klass.new(4)
        strategy_space.key_length.should == 4
      end
    end
  end
  
  describe "setters" do
    describe "#set_key_length" do
      it "sets the key_length to the supplied integer" do
        strategy_space_instance.set_key_length(10)
        strategy_space_instance.key_length.should == 10
      end
    end
  end
  
  describe "#generate_strategy" do
    it "returns an instance of Strategy" do
      returned_object = strategy_space_instance.generate_strategy
      returned_object.should be_a_kind_of(package::Strategy)
    end
    
    it "generates a strategy with keys of the length specified in the " + 
      "strategy space" do
      strategy = strategy_space_instance.generate_strategy  
      strategy.key_length.should == strategy_space_instance.key_length
    end
      
    it "generates a strategy with a key set corresponding to all possible " +
      "keys of the specified key length" do
      strategy_space = klass.new(2)
      strategy = strategy_space.generate_strategy

      key_set = strategy.key_set
      
      ["00", "01", "10", "11"].each do |key|
        key_set.contains(key).should be_true
      end
    end
    
    it "generates a random strategy each time" do
      strategy_space = klass.new(3)
      strategies = (1..256).collect { strategy_space.generate_strategy }
      strategies.uniq.length.should be >= 0.5 * 256
    end
  end
end