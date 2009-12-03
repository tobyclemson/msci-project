Given /^I have a properties hash$/ do
  @properties = properties_hash
end

Given /^I have a (.*) minority game$/ do |type|
  Given "I have a properties hash"
  Given "I set the 'type' property to '#{type}'"
  Given "I construct a minority game with the properties hash"
end

Given /^I have a (.*) minority game with (\d*) agents?$/ do |type, agents|
  Given "I have a properties hash"
  Given "I set the 'type' property to '#{type}'"
  Given "I set the 'number-of-agents' property to '#{agents}'"
  Given "I construct a minority game with the properties hash"
end

Given /^I have a (.*) minority game with an agent memory size of (\d*)?$/ do |type, length|
  Given "I have a properties hash"
  Given "I set the 'type' property to '#{type}'"
  Given "I set the 'agent-memory-size' property to '#{length}'"
  Given "I construct a minority game with the properties hash"
end

Given /^no time steps have occurred yet$/ do end
  
Given /^I have an experimentalist$/ do
  @experimentalist = Experimentalist.new
end

Given /^I set the experimentalist to record the attendance of choice '(.)'$/ do |choice|
  @attendances = []
  
  @choice = if(choice == "A")
    MSciProject::MinorityGame::Choice::A
  else
    MSciProject::MinorityGame::Choice::B
  end
  
  @experimentalist.add_measurement { |minority_game|
    last_attendances = minority_game.agents.last_choice_totals
    @attendances << last_attendances.get(@choice)
  }
end

Given /^I set the experimentalist to record the attendance of the minority choice$/ do
  @attendances = []
  
  @experimentalist.add_measurement { |minority_game| 
    @attendances << minority_game.last_minority_size
  }
end


Given /^I store the (initial)? choice history$/ do |unnused|
  @choice_history = @minority_game.choice_history.as_list.clone
end

When /^I set the '(.*)' property to '(.*)'$/ do |property, value|
  @properties.set_property(property, value)
end

When /^I construct a minority game with the properties hash$/ do
  begin
    @minority_game =
      MSciProject::MinorityGame::MinorityGameFactory.construct(@properties)
  rescue Exception => exception
    @exception = exception
  end
end

When /^I take (a|\d*) time steps?$/ do |steps|
  steps = (steps == "a") ? 1 : steps.to_i
  steps.times do 
    @minority_game.step_forward
    if instance_variable_defined?(:@experimentalist)
      @experimentalist.make_measurements(@minority_game)
    end
  end
end

Then /^I should have a minority game$/ do
  @minority_game.should be_a_kind_of(
    MSciProject::MinorityGame::AbstractMinorityGame
  )
end

Then /^its class should be (.*)$/ do |class_name|
  @minority_game.should be_an_instance_of(class_name.constantize)
end

Then /^it should have (.*) agents$/ do |num|
  @minority_game.should have(num.to_i).agents
end

Then /^it should have agents with (.*) strategies$/ do |num|
  @minority_game.agents.first.strategies.size.should == num.to_i
end

Then /^it should have agents that are instances of (.*)$/ do |agent_class_name|
  klass = agent_class_name.constantize
  @minority_game.agents.each do |agent|
    agent.should be_a_kind_of(klass)
  end
end

Then /^it should have a choice history with initial length (.*)$/ do |length|
  @minority_game.choice_history.size.should == length.to_i
end

Then /^it should have agents with strategies with (\d*) mappings$/ do |mappings|
  @minority_game.agents.each do |agent|
    agent.strategies.each do |strategy|
      strategy.key_set.size.should == mappings.to_i
    end
  end
end


Then /^a (.*) should be thrown$/ do |exception|
  @exception.cause.should be_a_kind_of(
    exception.constantize
  )
end

Then /^each agent that made the minority choice should have (\d*) point$/ do |points|
  minority_choice = @minority_game.last_minority_choice
  agents = @minority_game.agents
  
  agents.each do |agent|
    if agent.last_choice == minority_choice
      agent.score.should == points.to_i
    end
  end
end

Then /^each strategy that would have predicted the correct minority choice given the choice history has (\d*) points?$/ do |score|
  minority_choice = @minority_game.last_minority_choice
  
  @minority_game.agents.each do |agent|
    agent.strategies.each do |strategy|
      if strategy.get(@choice_history) == minority_choice
        strategy.score.should == score.to_i
      end
    end
  end
end

Then /^some agents should have a non zero score$/ do
  non_zero_scores = false
  @minority_game.agents.each do |agent|
    non_zero_scores = true if agent.score > 0
  end
  non_zero_scores.should be_true
end

Then /^some strategies should have a non zero score$/ do
  non_zero_strategies = false
  @minority_game.agents.each do |agent|
    agent.strategies.each do |strategy|
      non_zero_strategies = true if strategy.score > 0
    end
  end
  non_zero_strategies.should be_true
end

Then /^the scores should range between (\d*) and (\d*)$/ do |lower, upper|
  @minority_game.agents.each do |agent|
    agent.score.should be_between(lower.to_i, upper.to_i)
  end
end

Then /^the strategy scores should range between (\d*) and (\d*)$/ do |lower, upper|
  @minority_game.agents.each do |agent|
    agent.strategies.each do |strategy|
      strategy.score.should be_between(lower.to_i, upper.to_i)
    end
  end
end

Then /^the measured attendances should be varied$/ do
  total_attendances = @attendances.size
  unique_threshold = (0.2 * total_attendances).to_i
  unique_attendances = @attendances.uniq.size
  unique_attendances.should be > unique_threshold
end

Then /^the measured attendances should range between (\d*) and (\d*)$/ do |lower, upper|
  @attendances.each do |attendance|
    attendance.should be_between(lower.to_i, upper.to_i)
  end
end

Then /^the choice history should increase in size by (\d*)$/ do |increase|
  @minority_game.choice_history.size.should == 
    @minority_game.agent_memory_size + increase.to_i
end