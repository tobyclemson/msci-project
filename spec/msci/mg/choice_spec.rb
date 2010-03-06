require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

import msci.mg.Choice

describe Choice do
  it "has only the enumerated constants A and B" do
    Choice.constants.should eql(["A", "B"])
  end
end