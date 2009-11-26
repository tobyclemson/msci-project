require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::Strategy do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::Strategy }
  let(:hash_map_klass) { Java::JavaUtil::HashMap }
  let(:strategy_instance) { 
    hash_map = hash_map_klass.new()
    hash_map.put("1", "0")
    hash_map.put("0", "0")
    klass.new(hash_map) 
  }
  
  it "extends the AbstractMap class" do
    strategy_instance.should be_a_kind_of(Java::JavaUtil::AbstractMap)
  end
  
  describe "public interface" do
    it "has a score instance method" do
      strategy_instance.should respond_to(:score)
    end
    
    it "has an increment_score instance method" do
      strategy_instance.should respond_to(:increment_score)
    end
  end
  
  describe "constructor" do
    describe "with HashMap argument" do
      it "throws an IllegalArgumentException unless all of the keys in the " +
        "supplied Map are of the same length" do
        mappings = Java::JavaUtil::HashMap.new()
        mappings.put("01", "0")
        mappings.put("100", "1")
        
        expect {
          klass.new(mappings)
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
      
      it "throws an IllegalArgumentException unless all string " + 
        "permutations of the supplied key length are present in the " + 
        "supplied Map" do
        mappings = Java::JavaUtil::HashMap.new()
        mappings.put("01", "0")
        mappings.put("10", "1")
        mappings.put("11", "1")
        
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
      
      it "throws an IllegalArgumentException if any of the values in the " +
        "supplied Map are not '0' or '1'" do
          mappings = Java::JavaUtil::HashMap.new()
          mappings.put("01", "A")
          mappings.put("10", "1")
          mappings.put("11", "1")
          mappings.put("00", "0")
          
          expect {
            klass.new(mappings)
          }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
      
      it "sets the mappings to the supplied map" do
        mappings = Java::JavaUtil::HashMap.new()
        mappings.put("00", "1")
        mappings.put("01", "0")
        mappings.put("10", "1")
        mappings.put("11", "1")
        
        strategy = klass.new(mappings)
        
        mappings.each do |key, value|
          strategy.get(key).should == value
        end
      end
    end
  end
  
  describe "#get" do
    it "throws an IllegalArgumentException if the supplied key is not " + 
      "in the set of possible keys" do
      expect {
        strategy_instance.get("3")
      }.to raise_error(Java::JavaLang::IllegalArgumentException)
    end
  end

  describe "#increment_score" do
    it "adds 1 to the current score" do
      expect {
        strategy_instance.increment_score
      }.to change(strategy_instance, :score).from(0).to(1)
    end
  end
end