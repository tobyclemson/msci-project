require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::HistoryString do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::HistoryString }
  
  describe "constructor" do
    describe "with no arguments" do
      it "sets the length attribute to 1" do
        history_string = klass.new
        history_string.length.should == 1
      end
    end
    
    describe "with a length argument" do
      it "sets the length attribute to the supplied length" do
        history_string = klass.new(2)
        history_string.length.should == 2
      end
    end
  end
end