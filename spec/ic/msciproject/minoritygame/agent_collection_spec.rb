require File.join(File.dirname(__FILE__), '..', '..', '..', 'spec_helper.rb')

describe MSciProject::MinorityGame::AgentCollection do
  let(:package) { MSciProject::MinorityGame }
  let(:klass) { package::AgentCollection }
  let(:agent) { package::AbstractAgent.new(package::StrategyCollection.new) }
  let(:agent_collection_instance) { klass.new }
  
  it "extends the AbstractCollection class" do
    agent_collection_instance.should be_a_kind_of(
      Java::JavaUtil::AbstractCollection
    )
  end
  
  describe "public interface" do
    it "has a last_choice_totals instance method" do
      agent_collection_instance.should respond_to(:last_choice_totals)
    end
    
    it "has a make_choices instance method" do
      agent_collection_instance.should respond_to(:make_choices)
    end
    
    it "has an increment_scores_for_choice instance method" do
      agent_collection_instance.should respond_to(
        :increment_scores_for_choice
      )
    end
    
    it "has a update_for_choice instance method" do
      agent_collection_instance.should respond_to(
        :update_for_choice
      )
    end
  end
  
  describe "constructor" do
    describe "with no arguments" do
      it "initialises an empty collection" do
        strategy_collection = klass.new
        strategy_collection.size.should == 0
      end
    end
    
    describe "with another AgentCollection instance as an argument" do
      let(:agent_collection_to_copy) { klass.new }
      
      before(:each) do
        agent_collection_to_copy.add(agent)
      end
      
      it "adds all AbstractAgent objects in the supplied AgentCollection " + 
        "to the collection" do
        agent_collection = klass.new(agent_collection_to_copy)
        agent_collection.contains(agent).should be_true
      end
      
      it "has independent references to the Agent objects" do
        agent_collection = klass.new(agent_collection_to_copy)
        agent_collection_to_copy.remove(agent)
        agent_collection.contains(agent).should be_true
      end
    end
  end

  describe "#add" do
    it "adds the supplied Agent to the collection" do
      agent_collection_instance.add(agent) 
      agent_collection_instance.contains(agent).should be true
    end
  end
  
  describe "#size" do
    it "returns the number of agents in the collection" do
      3.times do 
        agent_collection_instance.add(agent)
      end
      agent_collection_instance.size.should == 3
    end
  end
  
  describe "#iterator" do
    it "returns an Iterator" do
      agent_collection_instance.iterator.should be_a_kind_of(
        Java::JavaUtil::Iterator
      )
    end
  end

  describe "#last_choice_totals" do
    it "counts the number of agents making each choice during the last " + 
      "timestep" do
      choice_a_agent = Mockito.mock(package::AbstractAgent.java_class)
      choice_b_agent = Mockito.mock(package::AbstractAgent.java_class)
      
      Mockito.when(choice_a_agent.last_choice).thenReturn(package::Choice::A)
      Mockito.when(choice_b_agent.last_choice).thenReturn(package::Choice::B)
      
      3.times { agent_collection_instance.add(choice_a_agent) }
      5.times { agent_collection_instance.add(choice_b_agent) }
      
      totals = agent_collection_instance.last_choice_totals
      
      {
        package::Choice::A => 3, 
        package::Choice::B => 5
      }.each do |choice, total|
        totals.get(choice).should == total
      end
    end
  end

  describe "#make_choices" do
    it "calls make choice on each agent in the collection with the " +
      "supplied choice history" do
      mock_agent_1 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(package::AbstractAgent.java_class)
      
      agent_collection_instance.add(mock_agent_1)
      agent_collection_instance.add(mock_agent_2)
      agent_collection_instance.add(mock_agent_3)
      
      choice_history = package::ChoiceHistory.new(5)
      
      agent_collection_instance.make_choices(choice_history.as_list(5))
      
      Mockito.verify(mock_agent_1).choose(choice_history.as_list(5))
      Mockito.verify(mock_agent_2).choose(choice_history.as_list(5))
      Mockito.verify(mock_agent_3).choose(choice_history.as_list(5))
    end
  end

  describe "#increment_scores_for_choice" do
    it "calls increment_score on all agents that made the supplied choice " +
      "as their last choice" do
      mock_agent_1 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(package::AbstractAgent.java_class)
      
      Mockito.when(mock_agent_1.last_choice).then_return(package::Choice::A)
      Mockito.when(mock_agent_2.last_choice).then_return(package::Choice::A)
      Mockito.when(mock_agent_3.last_choice).then_return(package::Choice::B)

      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        agent_collection_instance.add(agent)
      end
      
      agent_collection_instance.increment_scores_for_choice(
        package::Choice::A
      )
      
      Mockito.verify(mock_agent_1).increment_score
      Mockito.verify(mock_agent_2).increment_score
      Mockito.verify(mock_agent_3, Mockito.never()).increment_score
    end
  end
  
  describe "#update_for_choice" do
    it "calls update_for_choice on each agent" do
      mock_agent_1 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_2 = Mockito.mock(package::AbstractAgent.java_class)
      mock_agent_3 = Mockito.mock(package::AbstractAgent.java_class)

      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        agent_collection_instance.add(agent)
      end
      
      choice_history = Java::JavaUtil::ArrayList.new
      choice_history.add(package::Choice::A)
      choice_history.add(package::Choice::B)
      
      agent_collection_instance.update_for_choice(
        package::Choice::A, choice_history)
      
      [mock_agent_1, mock_agent_2, mock_agent_3].each do |agent|
        Mockito.verify(agent).update(package::Choice::A, choice_history)
      end
    end
  end
end