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
    
    it "returns an instance of StandardMinorityGame when the type " + 
      "'standard' is supplied" do
      properties.set_property("type", "standard")
      instance = package::MinorityGameFactory.construct(properties)
      instance.should be_a_kind_of(
        package::StandardMinorityGame
      )
    end
    
    describe "properties hash validation" do
      describe "for a valid set of properties" do
        it "successfully creates a minority game" do
          expect {
            package::MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
        
        it "allows a range to be specified for the 'agent-memory-size' " + 
          "property" do
          properties.set_property("agent-memory-size", "2..3")
            
          expect {
            package::MinorityGameFactory.construct(properties)
          }.to_not raise_error
        end
      end
      
      describe "for an invalid set of properties" do
        it "throws an IllegalArgumentException if the options object doesn't " +
          "contain a value for the 'type' property" do
          properties.remove("type")  
          expect { package::MinorityGameFactory.construct(properties) }.to raise_error(
            Java::JavaLang::IllegalArgumentException
          )
        end

        it "throws an IllegalArgumentException if the options object doesn't " +
          "contain a value for the 'agent-memory-size' property" do
          properties.remove("agent-memory-size")
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
          "contains anything other than digits or a range for the " + 
          "'agent-memory-size' property" do
          properties.set_property("agent-memory-size", "non-numeric")
          expect {
            package::MinorityGameFactory.construct(properties)
          }.to raise_error(Java::JavaLang::IllegalArgumentException)
        end
        
        it "throws an IllegalArgumentException if the range supplied " +
          "for the 'agent-memory-size' property has a negative lower bound" do
          properties.set_property("agent-memory-size", "-1..2")
          expect {
            package::MinorityGameFactory.construct(properties)
          }.to raise_error(Java::JavaLang::IllegalArgumentException)
        end

        it "throws an IllegalArgumentException if the range supplied " +
          "for the 'agent-memory-size' property has a negative lower bound" do
          properties.set_property("agent-memory-size", "1..-5")
          expect {
            package::MinorityGameFactory.construct(properties)
          }.to raise_error(Java::JavaLang::IllegalArgumentException)
        end

        it "throws an IllegalArgumentException if the range supplied " +
          "for the 'agent-memory-size' property has an upper bound smaller " + 
          "than the lower bound" do
          properties.set_property("agent-memory-size", "5..2")
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

        it "throws an IllegalArgumentException if the options object " +
          "contains an even number for the 'number-of-agents' property" do
          properties.set_property(
            "number-of-agents", "1000"
          )
          expect {
            package::MinorityGameFactory.construct(properties)
          }.to raise_error(Java::JavaLang::IllegalArgumentException)
        end
      end
    end    
    
    describe "minority game dependency generation" do
      it "initialises the choice_history attribute with a ChoiceHistory " + 
        "instance of the required initial length" do
        properties.set_property("agent-memory-size", "2")

        instance = package::MinorityGameFactory.construct(properties)
        instance.choice_history.should be_a_kind_of(package::ChoiceHistory)
        instance.choice_history.size.should == 2
      end
      
      it "creates agents with memories of the specified capacity" do
        properties.set_property("agent-memory-size", "2")
        
        instance = package::MinorityGameFactory.construct(properties)
        instance.agents.first.memory.capacity.should == 2
      end
      
      it "creates an equal number of agents with each memory capacity when " +
        "a range is specified for the 'agent-memory-size' parameter" do
        properties.set_property("agent-memory-size", "2..5")
        properties.set_property("number-of-agents", "10001")
        
        instance = package::MinorityGameFactory.construct(properties)
        
        scores = {2 => 0, 3 => 0, 4 => 0, 5 => 0}
        
        instance.agents.each do |agent|
          scores[agent.memory.capacity] += 1
        end
        
        scores.each_value do |score|
          score.should be_between(2400, 2600)
        end
      end

      it "initialises the agents attribute with the required " +
        "number of agents" do
        properties.set_property("number-of-agents", "101")
        instance = package::MinorityGameFactory.construct(properties)      
        instance.agents.should have(101).agents
      end
      
      it "initialises the agents attribute with agents with the required " + 
        "number of strategies" do
        properties.set_property("number-of-strategies-per-agent", "2")
        instance = package::MinorityGameFactory.construct(properties)
        instance.agents.
          first.
          should have(2).strategies
      end
      
      it "initialises the agents attribute with BasicAgent instances when " +
        "the 'agent-type' property is set to 'basic'" do
        properties.set_property("agent-type", "basic")
        instance = package::MinorityGameFactory.construct(properties)
        instance.agents.each do |agent|
          agent.should be_a_kind_of(package::BasicAgent)
        end
      end
      
      it "initialises the agents attribute with LearningAgent instances " + 
        "when the 'agent-type' property is set to 'learning'" do
        properties.set_property("agent-type", "learning")
        instance = package::MinorityGameFactory.construct(properties)
        instance.agents.each do |agent|
          agent.should be_a_kind_of(package::LearningAgent)
        end
      end
      
      it "initialises the agents attribute with RandomAgent instances when " +
        "the 'agent-type' property is set to 'random'" do
        properties.set_property("agent-type", "random")
        instance = package::MinorityGameFactory.construct(properties)
        instance.agents.each do |agent|
          agent.should be_a_kind_of(package::RandomAgent)
        end
      end
    end
  end
end