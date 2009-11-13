require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::MinorityGame do
  include FactoryHelpers
  
  describe "API" do
    it "has a static construct method" do
      MSciProject::MinorityGame::MinorityGame.should respond_to(:construct)
    end
  end
  
  describe ".construct" do
    before(:each) do
      @properties = properties_hash
    end
    
    it "throws an IllegalArgumentException if the options object doesn't " +
      "contain a value for the 'type' property" do
      @properties.remove("type")  
        
      expect {
        MSciProject::MinorityGame::MinorityGame.construct(@properties) 
      }.to raise_error(
        Java::JavaLang::IllegalArgumentException
      )
    end
    
    it "throws an IllegalArgumentException if the options object contains " +
      "an unrecognised value for the 'type' property" do
      @properties.set_property("type", "unsupported!")
       
      expect { 
        MSciProject::MinorityGame::MinorityGame.construct(@properties) 
      }.to raise_error(Java::JavaLang::IllegalArgumentException)
    end
    
    it "returns an instance of StandardMinorityGame when the type " + 
      "'standard' is supplied" do
      @properties.set_property("type", "standard")
      instance = MSciProject::MinorityGame::MinorityGame.construct(@properties)
      
      instance.should be_a_kind_of(
        MSciProject::MinorityGame::StandardMinorityGame
      )
    end
    
    it "returns an instance of EvolutionaryMinorityGame when the type " +
      "'evolutionary' is supplied" do
      @properties.set_property("type", "evolutionary")
      instance = MSciProject::MinorityGame::MinorityGame.construct(@properties)
      
      instance.should be_a_kind_of(
        MSciProject::MinorityGame::EvolutionaryMinorityGame
      )
    end
  end
end