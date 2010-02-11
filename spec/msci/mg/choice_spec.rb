require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

describe MSci::MG::Choice do
  let(:package) { MSci::MG }
  
  it "has only the enumerated constants A and B" do
    package::Choice.constants.should eql(["A", "B"])
  end
end