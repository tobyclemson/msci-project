require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::StrategyCollection do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::StrategyCollection }
  let(:strategy_collection_instance) { klass.new }
  let(:strategy) { 
    hash_map = Java::JavaUtil::HashMap.new
    hash_map.put("0", "0")
    hash_map.put("1", "1")
    package::Strategy.new(hash_map) 
  }
  
  it "extends the AbstractCollection class" do
    strategy_collection_instance.should be_a_kind_of(
      Java::JavaUtil::AbstractCollection
    )
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
        "the returned collection" do
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
end