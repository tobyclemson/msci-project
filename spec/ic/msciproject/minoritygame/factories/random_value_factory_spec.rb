require File.join(
  File.dirname(__FILE__), '..', '..', '..', '..', 'spec_helper.rb'
)

describe MSciProject::MinorityGame::Factories::RandomValueFactory do
  let(:package) { MSciProject::MinorityGame::Factories }
  let(:klass) { package::RandomValueFactory }
  
  describe "constructor" do
    it "sets the lower_bound attribute to the supplied value" do
      random_value_factory = klass.new(2, 3)
      random_value_factory.lower_bound.should == 2
    end
    
    it "sets the upper_bound attribute to the supplied value" do
      random_value_factory = klass.new(2, 3)
      random_value_factory.upper_bound.should == 3
    end
  end
  
  describe "#create" do
    it "returns values in the specified range at random" do
      counts = {3 => 0, 4 => 0, 5 => 0, 6 => 0}
      
      random_value_factory = klass.new(3, 6)
      
      10000.times do
        value = random_value_factory.create
        if counts.has_key?(value)
          counts[value] += 1
        end
      end
      
      counts.each do |number, count|
        count.should be_between(2400, 2600)
      end
    end
  end
end