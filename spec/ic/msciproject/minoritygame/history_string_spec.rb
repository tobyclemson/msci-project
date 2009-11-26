require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::HistoryString do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::HistoryString }
  let(:history_string_instance) { package::HistoryString.new(2) }
  
  describe "public interface" do
    it "has a to_string method" do
      history_string_instance.should respond_to(:to_string)
    end
    
    it "has a push method" do
      history_string_instance.should respond_to(:push)
    end
  end
  
  describe "constructor" do
    describe "with a length argument" do
      it "sets the length attribute to the supplied length" do
        history_string = klass.new(2)
        history_string.length.should == 2
      end
      
      it "sets itself to a string of the specified length" do
        history_string = klass.new(2)
        string = history_string.to_string
        
        string.length.should == 2
      end
      
      it "sets itself to a string containing only '0's and '1's" do
        history_string = klass.new(2)
        string = history_string.to_string
        
        string.each_char do |character|
          ["0", "1"].should include(character)
        end
      end
      
      it "sets itself to a random string" do
        strings = []
        1000.times do
          history_string = klass.new(12)
          strings << history_string.to_string
        end
        
        strings.uniq.size.should be > 700
      end
    end
  end

  describe "#push" do
    it "removes the left most character, pushes all other characters one " +
      "place left and adds the supplied character to the end of the string" do
      history_strings = (1..5).collect do
        package::HistoryString.new(5)
      end
      
      strings_before = []
      history_strings.each_with_index do |string, index|
        strings_before[index] = string.to_string
      end
      
      expected_strings = []
      strings_before.each_with_index do |string, index|
        expected_strings[index] = string[1..-1].concat("1")
      end
      
      history_strings.each do |string|
        string.push("1")
      end
      
      history_strings.each_with_index do |history_string, index|
        expected_string = expected_strings[index]
        history_string.to_string.should == expected_string
      end
    end
  end
end