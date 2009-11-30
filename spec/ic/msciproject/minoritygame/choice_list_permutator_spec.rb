require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::StringPermutator do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::StringPermutator }
  
  describe ".generate_all" do
    it "returns a set of all possible permutations of lists of the choices " +
      "Choice.A and Choice.B of the specified length" do
      returned_permutations = klass.generate_all(3)
      
      a = package::Choice::A
      b = package::Choice::B
      
      arraylist_klass = Java::JavaUtil::ArrayList
      a_a_a = a_a_b = a_b_a = a_b_b = b_a_a = b_a_b = b_b_a = b_b_b = nil
      
      [
        [:a, :a, :a], [:a, :a, :b], [:a, :b, :a], 
        [:a, :b, :b], [:b, :a, :a], [:b, :a, :b],
        [:b, :b, :a], [:b, :b, :b]
      ].each do |permutation|
        name = permutation.join("_")
        eval("#{name} = arraylist_klass.new")
        permutation.each do |choice|
          eval("#{name}.add(#{choice})")
        end
      end
      
      expected_permutations = [
        a_a_a, a_a_b, a_b_a, a_b_b, b_a_a, b_a_b, b_b_a, b_b_b
      ]
      
      expected_permutations.each do |permutation|
        returned_permutations.contains(permutation).should be_true
      end
    end
    
    it "returns a reference to the same lists if permutations of the same " +
      "length are requested" do
      first_call_results = klass.generate_all(2)
      second_call_results = klass.generate_all(2)
      
      first_call_results.each do |first_call_permutation|
        second_call_results.each do |second_call_permutation|
          if first_call_permutation == second_call_permutation
            first_call_permutation.should equal(second_call_permutation)
          end
        end
      end
    end
  end
end