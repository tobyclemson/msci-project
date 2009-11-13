Given /^I have a properties hash$/ do
  @properties = properties_hash
end

When /^I set the '(.*)' property to '(.*)'$/ do |property, value|
  @properties.set_property(property, value)
end

When /^I construct a minority game with the properties hash$/ do
  @minority_game_instance =
    MSciProject::MinorityGame::MinorityGame.construct(@properties)
end

Then /^I should end up with an instance of (.*)$/ do |class_name|
  @minority_game_instance.should be_an_instance_of(class_name.constantize)
end

Then /^I should end up with a minority game containing (.*) agents$/ do |num|
  @minority_game_instance.should have(10000).agents
end
