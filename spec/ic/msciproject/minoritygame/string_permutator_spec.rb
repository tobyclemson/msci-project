require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::StringPermutator do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::StringPermutator }
  
  describe ".generate_all" do
    it "returns a set of all possible permutations of strings of the " +
      "specified length" do
      returned_permutations = klass.generate_all(3)
      expected_permutations = [
        "000", "001", "010", 
        "011", "100", "101",
        "110", "111"
      ]
      
      expected_permutations.each do |permutation|
        returned_permutations.contains(permutation).should be_true
      end
    end
  end
end