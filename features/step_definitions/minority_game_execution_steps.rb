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

When /^I take (a|\d*) time steps?$/ do |steps|
  steps = (steps == "a") ? 1 : steps.to_i
  steps.times do 
    @minority_game.step_forward
    if instance_variable_defined?(:@experimentalist)
      @experimentalist.make_measurements(@minority_game)
    end
  end
end

Then /^the sum of the agents scores should equal the number of agents making the minority choice$/ do
  total_score = @minority_game.agents.inject(0) do |sum, agent|
    sum + agent.score
  end
  
  @minority_game.last_minority_size.should == total_score
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

