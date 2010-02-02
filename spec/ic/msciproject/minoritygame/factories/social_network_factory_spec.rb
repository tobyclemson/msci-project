require File.join(
  File.dirname(__FILE__), '..', '..', '..', '..', 'spec_helper.rb'
)

describe MSciProject::MinorityGame::Factories::SocialNetworkFactory do
  let(:package) { MSciProject::MinorityGame::Factories }
  let(:klass) { 
    Class.new(package::SocialNetworkFactory) do
      def create; end
    end
  }
  
  let(:factory_interface) { Java::OrgApacheCommonsCollections15::Factory }
  
  let(:agent_factory) { Mockito.mock(factory_interface.java_class) }
  let(:friendship_factory) { Mockito.mock(factory_interface.java_class) }
  let(:number_of_agents) { 101 }
  
  let(:factory) { 
    klass.new(agent_factory, friendship_factory, number_of_agents) 
  }
  
  describe "constructor" do
    it "sets the agent_factory attribute to the supplied factory" do
      factory.agent_factory.should == agent_factory
    end
    
    it "sets the friendship_factory attribute to the supplied factory" do
      factory.friendship_factory.should == friendship_factory
    end
    
    it "sets the number_of_agents attribute to the supplied value" do
      factory.number_of_agents.should == number_of_agents
    end
  end
  
  describe "setters" do
    describe "#agent_factory=" do
      it "sets the agent_factory attribute to the supplied factory" do
        new_agent_factory = Mockito.mock(factory_interface.java_class)
        factory.agent_factory = new_agent_factory
        factory.agent_factory.should == new_agent_factory
      end
    end
    
    describe "#friendship_factory=" do
      it "sets the friendship_factory attribute to the supplied factory" do
        new_friendship_factory = Mockito.mock(factory_interface.java_class)
        factory.friendship_factory = new_friendship_factory
        factory.friendship_factory.should == new_friendship_factory
      end
    end
    
    describe "#number_of_agents=" do
      it "sets the number_of_agents attribute to the supplied value" do
        new_number_of_agents = 99
        factory.number_of_agents = new_number_of_agents
        factory.number_of_agents.should == new_number_of_agents
      end
    end
  end
end