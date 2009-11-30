require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::Choice do
  let(:package) { MSciProject::MinorityGame }
  
  it "has only the enumerated constants A and B" do
    package::Choice.constants.should eql(["A", "B"])
  end
end