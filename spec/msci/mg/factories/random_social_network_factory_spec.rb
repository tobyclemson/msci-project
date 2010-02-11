require File.join(
  File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb'
)

describe MSci::MG::Factories::RandomSocialNetworkFactory do
  let(:package) { MSci::MG::Factories }
  let(:klass) { package::RandomSocialNetworkFactory }
  
  let(:factory_interface) { Java::OrgApacheCommonsCollections15::Factory }
  
  let(:agent_factory) { Mockito.mock(factory_interface.java_class) }
  let(:friendship_factory) { Mockito.mock(factory_interface.java_class) }
  let(:number_of_agents) { 101 }
  let(:link_probability) { 0.1 }
  
  let(:factory) { 
    klass.new(
      agent_factory, friendship_factory, number_of_agents, link_probability
    ) 
  }
  
  it "extends the SocialNetworkFactory class" do
    factory.should be_a_kind_of(package::SocialNetworkFactory)
  end
  
  describe "constructor" do
    it "sets the link_probability attribute to the supplied value" do
      factory.link_probability.should == link_probability
    end
    
    it "throws an IllegalArgumentException if the supplied " + 
      "link_probability is less than zero" do
      link_probability = -0.5
      
      expect {
        factory = klass.new(
          agent_factory, 
          friendship_factory, 
          number_of_agents, 
          link_probability
        )
      }.to raise_error(Java::JavaLang::IllegalArgumentException)
    end
    
    it "throws an IllegalArgumentException if the supplied " +
      "link_probability is greater than one" do
        link_probability = 1.5

        expect {
          factory = klass.new(
            agent_factory, 
            friendship_factory, 
            number_of_agents, 
            link_probability
          )
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
    end
  end
  
  describe "setters" do
    describe "#link_probability=" do
      it "sets the link_probability attribute to the supplied value" do
        new_link_probability = 0.6
        factory.link_probability = new_link_probability
        factory.link_probability.should == new_link_probability 
      end
      
      it "throws an IllegalArgumentException if the supplied " + 
        "link_probability is less than zero" do
        new_link_probability = -0.5

        expect {
          factory.link_probability = new_link_probability
        }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end

      it "throws an IllegalArgumentException if the supplied " +
        "link_probability is greater than one" do
          new_link_probability = 1.5

          expect {
            factory.link_probability = new_link_probability
          }.to raise_error(Java::JavaLang::IllegalArgumentException)
      end
    end
  end

  describe "#create" do
    let(:agent_factory) {
      agent_factory_klass = Class.new(package::AgentFactory) do
        def create
          return Mockito.mock(
            MSci::MG::AbstractAgent.java_class
          )
        end
      end
      agent_factory_klass.new
    }
    let(:friendship_factory) {
      friendship_factory_klass = Class.new(package::FriendshipFactory) do
        def create
          return Mockito.mock(
            MSci::MG::Friendship.java_class
          )
        end
      end
      friendship_factory_klass.new
    }
    
    it "generates a social network containing the specified number of " + 
      "agents" do
      social_network = factory.create
      social_network.vertex_count.should == number_of_agents
    end
    
    it "generates a social network with approximately pN(N-1)/2 " + 
      "friendships" do
      vertex_counts = (1..10).collect do
        factory.create.edge_count
      end
      
      total = vertex_counts.inject(0) do |sum, count|
        sum + count
      end
      
      average = total / 10.0
      
      expected = (
        link_probability * number_of_agents * (number_of_agents - 1)
      ) / 2.0
      
      average.should be_between(expected * 0.9, expected * 1.1)  
    end
    
    it "generates a social network with approximately pN edges connected " +
      "to each agent" do
      social_network = factory.create
      
      degree_counts = social_network.vertices.collect do |agent|
        social_network.degree(agent)
      end
      
      total = degree_counts.inject(0) do |sum, count|
        sum + count
      end
      
      average = total.to_f / number_of_agents
      
      expected = link_probability * number_of_agents
      
      average.should be_between(expected * 0.9, expected * 1.1)
    end
  end
end