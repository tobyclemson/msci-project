require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

# This is a functional test, not a unit test!!! How do I unit test this
# factory without returning a mock when new is called? This implies the class
# is badly written but at the moment don't really want to have to rewrite it.

describe MSciProject::MinorityGame::MinorityGameFactory do
  include FactoryHelpers
  
  let(:package) { MSciProject::MinorityGame }
  
  describe "API" do
    it "has a static construct method" do
      package::MinorityGameFactory.should respond_to(:construct)
    end
  end
  
  describe ".construct" do
    let(:properties) { properties_hash }
    
    describe "error handling" do
      it "throws an IllegalArgumentException if the options object doesn't " +
        "contain a value for the 'type' property" do
        properties.remove("type")  
        expect { package::MinorityGameFactory.construct(properties) }.to raise_error(
          Java::JavaLang::IllegalArgumentException
        )
      end

      it "throws an IllegalArgumentException if the options object doesn't " +
        "contain a value for the 'history-string-length' property" do
        properties.remove("history-string-length")
        expect { 
          package::MinorityGameFactory.construct(properties) 
        }.to raise_error(
          Java::JavaLang::IllegalArgumentException
        )
      end
      
      it "throws an IllegalArgumentException if the options object doesn't " +
        "contain a value for the 'number-of-agents' property" do
        properties.remove("number-of-agents")
        expect {
          package::MinorityGameFactory.construct(properties)
        }.to raise_error(
          Java::JavaLang::IllegalArgumentException
        )
      end
      
      it "throws an IllegalArgumentException if the options object doesn't " +
        "contain a value for the 'agent-type' property" do
        properties.remove("agent-type")
        expect {
          package::MinorityGameFactory.construct(properties)
        }.to raise_error(
          Java::JavaLang::IllegalArgumentException
        )
      end
      
      it "throws an IllegalArgumentException if the options object doesn't " +
        "contain a value for the 'number-of-strategies-per-agent' property" do
        properties.remove("number-of-strategies-per-agent")
        expect {
          package::MinorityGameFactory.construct(properties)
        }.to raise_error(
          Java::JavaLang::IllegalArgumentException
        )
      end

      it "throws an IllegalArgumentException if the options object " + 
        "contains an unrecognised value for the 'type' property" do
        properties.set_property("type", "unsupported!")
        expect { 
          package::MinorityGameFactory.construct(properties) 
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
      
      it "throws an IllegalArgumentException if the options object " + 
        "contains anything other than digits for the " + 
        "'history-string-length' property" do
        properties.set_property("history-string-length", "non-numeric")
        expect {
          package::MinorityGameFactory.construct(properties)
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
      
      it "throws an IllegalArgumentException if the options object " + 
        "contains anything other than digits for the " + 
        "'number-of-agents' property" do
        properties.set_property("number-of-agents", "non-numeric")
        expect {
          package::MinorityGameFactory.construct(properties)
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
      
      it "throws an IllegalArgumentException if the options object " + 
        "contains an unrecognised value for the 'agent-type' property" do
        properties.set_property("agent-type", "unsupported!")
        expect { 
          package::MinorityGameFactory.construct(properties) 
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
      
      it "throws an IllegalArgumentException if the options object " + 
        "contains anything other than digits for the " + 
        "'number-of-strategies-per-agent' property" do
        properties.set_property(
          "number-of-strategies-per-agent", "non-numeric"
        )
        expect {
          package::MinorityGameFactory.construct(properties)
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
    end    
    
    it "returns an instance of StandardMinorityGame when the type " + 
      "'standard' is supplied" do
      properties.set_property("type", "standard")
      instance = package::MinorityGameFactory.construct(properties)
      instance.should be_a_kind_of(
        package::StandardMinorityGame
      )
    end
    
    it "returns an instance of EvolutionaryMinorityGame when the type " +
      "'evolutionary' is supplied" do
      properties.set_property("type", "evolutionary")
      instance = package::MinorityGameFactory.construct(properties)
      instance.should be_a_kind_of(
        package::EvolutionaryMinorityGame
      )
    end
    
    describe "minority game dependency generation" do
      it "initialises the history_string attribute with a HistoryString " + 
        "instance of the required length" do
        properties.set_property("history-string-length", "2")

        instance = package::MinorityGameFactory.construct(properties)
        instance.history_string.should be_a_kind_of(package::HistoryString)
        instance.history_string.length.should == 2
      end

      it "initialises the agents attribute with the required " +
        "number of agents" do
        properties.set_property("number-of-agents", "10")
        instance = package::MinorityGameFactory.construct(properties)      
        instance.should have(10).agents
      end
      
      it "initialises the agents attribute with agents with the required " + 
        "number of strategies" do
        properties.set_property("number-of-strategies-per-agent", "2")
        instance = package::MinorityGameFactory.construct(properties)
        instance.agents.first.should have(2).strategies
      end
      
      it "initialises the agents attribute with agents of the specified " + 
        "type" do
        properties.set_property("agent-type", "learning")
        instance = package::MinorityGameFactory.construct(properties)
        instance.agents.each do |agent|
          agent.should be_a_kind_of(package::LearningAgent)
        end
      end
    end
  end
end