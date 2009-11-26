Given /^I have a properties hash$/ do
  @properties = properties_hash
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

Then /^it should have a history string of length (.*)$/ do |length|
  @minority_game.history_string.length.should == length.to_i
end

Then /^a (.*) should be thrown$/ do |exception|
  @exception.cause.should be_a_kind_of(
    exception.constantize
  )
end