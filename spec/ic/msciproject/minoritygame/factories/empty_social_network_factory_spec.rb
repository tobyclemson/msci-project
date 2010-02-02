require File.join(
  File.dirname(__FILE__), '..', '..', '..', '..', 'spec_helper.rb'
)

describe MSciProject::MinorityGame::Factories::EmptySocialNetworkFactory do
  let(:package) { MSciProject::MinorityGame::Factories }
  let(:klass) { package::EmptySocialNetworkFactory}
  
  let(:factory_interface) { Java::OrgApacheCommonsCollections15::Factory }
  
  let(:agent_factory) { Mockito.mock(factory_interface.java_class) }
  let(:friendship_factory) { Mockito.mock(factory_interface.java_class) }
  let(:number_of_agents) { 101 }
  
  let(:factory) { 
    klass.new(agent_factory, friendship_factory, number_of_agents) 
  }
  
  it "extends the SocialNetworkFactory class" do
    factory.should be_a_kind_of(package::SocialNetworkFactory)
  end
  
  describe "#create" do
    let(:agent_factory) {
      agent_factory_klass = Class.new(package::AgentFactory) do
        def create
          return Mockito.mock(
            MSciProject::MinorityGame::AbstractAgent.java_class
          )
        end
      end
      agent_factory_klass.new
    }
    let(:friendship_factory) {
      friendship_factory_klass = Class.new(package::FriendshipFactory) do
        def create
          return Mockito.mock(
            MSciProject::MinorityGame::Friendship.java_class
          )
        end
      end
      friendship_factory_klass.new
    }
    
    before(:each) do
      factory = klass.new(agent_factory, friendship_factory, number_of_agents)
    end
    
    it "generates a social network containing the specified number of " + 
      "agents" do
      social_network = factory.create
      social_network.vertex_count.should == number_of_agents
    end
    
    it "generates a social network containing no friendships" do
      social_network = factory.create
      social_network.edge_count.should == 0
    end
  end
  
end