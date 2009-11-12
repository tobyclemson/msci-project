require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::MinorityGame do
  describe "API" do
    it "should have a construct class method" do
      MSciProject::MinorityGame::MinorityGame.should respond_to(:construct)
    end
  end
  
  describe ".construct" do
    before(:each) do
      @options = Java::JavaUtil::Properties.new
    end
    
    it "throws an IllegalArgumentException if the options object doesn't " +
      "contain a value for the 'type' property" do
      @options.remove("type")  
        
      expect {
        MSciProject::MinorityGame::MinorityGame.construct(@options) 
      }.to raise_error(
        Java::JavaLang::IllegalArgumentException
      )
    end
    
    it "throws an IllegalArgumentException if the options object contains " +
      "an unrecognised value for the 'type' property" do
      @options.set_property("type", "unsupported!")
       
      expect { 
        MSciProject::MinorityGame::MinorityGame.construct(@options) 
      }.to raise_error(Java::JavaLang::IllegalArgumentException)
    end
    
    it "returns an instance of StandardMinorityGame when the type " + 
      "'standard' is supplied" do
      @options.set_property("type", "standard")
      instance = MSciProject::MinorityGame::MinorityGame.construct(@options)
      
      instance.should be_a_kind_of(
        MSciProject::MinorityGame::StandardMinorityGame
      )
    end
    
    it "returns an instance of EvolutionaryMinorityGame when the type " +
      "'evolutionary' is supplied" do
      @options.set_property("type", "evolutionary")
      instance = MSciProject::MinorityGame::MinorityGame.construct(@options)
      
      instance.should be_a_kind_of(
        MSciProject::MinorityGame::EvolutionaryMinorityGame
      )
    end
  end
end