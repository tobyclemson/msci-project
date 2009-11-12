Given /^I don't have a minority game instance$/ do
end

When /^I try to construct a (.*) type minority game$/ do |type|
  @options = Java::JavaUtil::Properties.new
  @options.set_property("type", type)
  @minority_game_instance = MSciProject::MinorityGame::MinorityGame.construct(@options)
end

Then /^I should end up with an instance of (.*)$/ do |class_name|
  @minority_game_instance.should be_an_instance_of(class_name.constantize)
end