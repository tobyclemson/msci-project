require File.join(File.dirname(__FILE__), '..', '..', 'spec_helper.rb')

describe MSci::MG::Neighbourhood do
  let(:package) { MSci::MG }
  let(:klass) { package::Neighbourhood }
  
  let(:graph) { Java::EduUciIcsJungGraph::Graph }
  let(:agent) { package::Agents::AbstractAgent }
  
  let(:social_network) { Mockito.mock(graph.java_class) }
  let(:root_agent) { Mockito.mock(agent.java_class) }
  
  let(:neighbourhood) { klass.new(social_network, root_agent) }
  
  before(:each) do
    Mockito.when(social_network.contains_vertex(root_agent)).then_return(true)
  end
  
  describe "constructor" do
    it "throws an IllegalArgumentException if the supplied root agent " + 
      "doesn't exist in teh supplied graph"do
      Mockito.when(social_network.contains_vertex(root_agent)).
        then_return(false)
        
      expect {
        neighbourhood = klass.new(social_network, root_agent)
      }.to raise_error(Java::JavaLang::IllegalArgumentException)
    end
    it "sets the social_network attribute to the supplied graph of agents " + 
      "and friendships" do
      neighbourhood.social_network.should equal(social_network)
    end
    
    it "sets the root_agent attribute to the supplied agent" do
      neighbourhood.root_agent.should equal(root_agent)
    end
  end
  
  describe "#friends" do
    it "returns the agents in the supplied social network sorted by " +
      "identification number connected to the root agent but excluding the " + 
      "root agent" do
      neighbours = Java::JavaUtil::HashSet.new
      11.times { neighbours.add(package::Agents::RandomAgent.new()) }

      Mockito.when(social_network.get_neighbors(root_agent)).
        then_return(neighbours)
      
      neighbourhood.friends.to_a.should == neighbours.to_a.sort_by do |friend|
        friend.identification_number
      end
    end
  end
  
  describe "#most_successful_predictor" do
    it "returns the agent in the social network that has the highest " + 
      "correct prediction count" do
      friend_1 = Mockito.mock(agent.java_class)
      friend_2 = Mockito.mock(agent.java_class)
      
      neighbours = Java::JavaUtil::HashSet.new
      neighbours.add(friend_1)
      neighbours.add(friend_2)
      
      Mockito.when(social_network.get_neighbors(root_agent)).
        then_return(neighbours)
        
      Mockito.when(root_agent.correct_prediction_count).
        then_return(Java::JavaLang::Integer.new(3))
      Mockito.when(friend_1.correct_prediction_count).
        then_return(Java::JavaLang::Integer.new(4))
      Mockito.when(friend_2.correct_prediction_count).
        then_return(Java::JavaLang::Integer.new(5))
        
      neighbourhood.most_successful_predictor.should equal(friend_2)
    end
    
    it "returns the root agent if it has a higher score than all of its " +
      "friends" do
      friend_1 = Mockito.mock(agent.java_class)
      friend_2 = Mockito.mock(agent.java_class)

      neighbours = Java::JavaUtil::HashSet.new
      neighbours.add(friend_1)
      neighbours.add(friend_2)

      Mockito.when(social_network.get_neighbors(root_agent)).
        then_return(neighbours)

      Mockito.when(root_agent.correct_prediction_count).
        then_return(Java::JavaLang::Integer.new(10))
      Mockito.when(friend_1.correct_prediction_count).
        then_return(Java::JavaLang::Integer.new(4))
      Mockito.when(friend_2.correct_prediction_count).
        then_return(Java::JavaLang::Integer.new(5))

      neighbourhood.most_successful_predictor.should equal(root_agent)
    end
    
    it "returns one of the most successful predictors at random when two " + 
      "have the same correct prediction count" do
        friend_1 = Mockito.mock(agent.java_class)
        friend_2 = Mockito.mock(agent.java_class)
        friend_3 = Mockito.mock(agent.java_class)

        neighbours = Java::JavaUtil::HashSet.new
        neighbours.add(friend_1)
        neighbours.add(friend_2)
        neighbours.add(friend_3)

        Mockito.when(social_network.get_neighbors(root_agent)).
          then_return(neighbours)

        Mockito.when(root_agent.correct_prediction_count).
          then_return(Java::JavaLang::Integer.new(10))
        Mockito.when(friend_1.correct_prediction_count).
          then_return(Java::JavaLang::Integer.new(10))
        Mockito.when(friend_2.correct_prediction_count).
          then_return(Java::JavaLang::Integer.new(5))
        Mockito.when(friend_3.correct_prediction_count).
          then_return(Java::JavaLang::Integer.new(2))

        root_agent_count = 0
        friend_1_count = 0

        100.times do
          predictor = neighbourhood.most_successful_predictor

          if predictor.equals(root_agent)
            root_agent_count += 1
          elsif predictor.equals(friend_1)
            friend_1_count += 1
          end
        end
        
        root_agent_count.should be_between(40, 60)
        friend_1_count.should be_between(40, 60)
        (root_agent_count + friend_1_count).should == 100
    end
    
    it "returns each agent in the network with equal probability when they " + 
      "all have the same correct prediction count" do
        friend_1 = Mockito.mock(agent.java_class)
        friend_2 = Mockito.mock(agent.java_class)
        friend_3 = Mockito.mock(agent.java_class)

        neighbours = Java::JavaUtil::HashSet.new
        neighbours.add(friend_1)
        neighbours.add(friend_2)
        neighbours.add(friend_3)

        Mockito.when(social_network.get_neighbors(root_agent)).
          then_return(neighbours)

        Mockito.when(root_agent.correct_prediction_count).
          then_return(Java::JavaLang::Integer.new(10))
        Mockito.when(friend_1.correct_prediction_count).
          then_return(Java::JavaLang::Integer.new(10))
        Mockito.when(friend_2.correct_prediction_count).
          then_return(Java::JavaLang::Integer.new(10))
        Mockito.when(friend_3.correct_prediction_count).
          then_return(Java::JavaLang::Integer.new(10))

        root_agent_count = 0
        friend_1_count = 0
        friend_2_count = 0
        friend_3_count = 0

        100.times do
          predictor = neighbourhood.most_successful_predictor

          if predictor.equals(root_agent)
            root_agent_count += 1
          elsif predictor.equals(friend_1)
            friend_1_count += 1
          elsif predictor.equals(friend_2)
            friend_2_count += 1
          elsif predictor.equals(friend_3)
            friend_3_count += 1
          end
        end
        
        counts = [
          root_agent_count, friend_1_count, friend_2_count, friend_3_count
        ]
        
        counts.each do |frequency|
          frequency.should be_between(15, 35)
        end
                 
        counts.inject(0) { |memo, obj| memo + obj }.should == 100
    end
  end
end