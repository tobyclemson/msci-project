# Properties hash related steps
Given /^I have a properties hash$/ do
  @properties = properties_hash
end

When /^I set the '(.*)' property to '(.*)'$/ do |property, value|
  @properties.set_property(property, value)
end

# Minority game construction steps
When /^I construct a minority game with the properties hash$/ do
  begin
    @minority_game =
      MSciProject::MinorityGame::MinorityGameFactory.construct(@properties)
  rescue Exception => exception
    @exception = exception
  end
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

Given /^I have a (.*) minority game with ([a-zA-Z\-]*) agents?$/ do |type, agent_type|
  Given "I have a properties hash"
  Given "I set the 'type' property to '#{type}'"
  Given "I set the 'agent-type' property to '#{agent_type}'"
  Given "I construct a minority game with the properties hash"
end

Given /^I have a (.*) minority game with ([a-zA-Z\-]*) agents? with a memory size of (\d*)$/ do |type, agent_type, agent_memory_size|
  Given "I have a properties hash"
  Given "I set the 'type' property to '#{type}'"
  Given "I set the 'agent-type' property to '#{agent_type}'"
  Given "I set the 'agent-memory-size' property to '#{agent_memory_size}'"
  Given "I construct a minority game with the properties hash"
end

# Experimentalist setup steps
Given /^I have an experimentalist$/ do
  @experimentalist = Experimentalist.new
end

Given /^I set the experimentalist to record the attendance of choice '(.)'$/ do |choice|
  if choice == "A"
    @choice = MSciProject::MinorityGame::Choice::A
  elsif choice == "B"
    @choice = MSciProject::MinorityGame::Choice::B
  end
  
  @experimentalist.add_measurement(:choice_attendance) { |minority_game|
    minority_game.agents.choice_totals.get(@choice)
  }
end

Given /^I set the experimentalist to record the minority choice$/ do
  @experimentalist.add_measurement(:minority_choice) { |minority_game| 
    minority_game.minority_choice
  }
end

Given /^I set the experimentalist to record the minority size$/ do
  @experimentalist.add_measurement(:minority_size) { |minority_game| 
    minority_game.minority_size
  }
end

Given /^I set the experimentalist to record the scores? of (all|one) agents?$/ do |how_many|
  if how_many == "all"
    @experimentalist.add_measurement(:agent_score) { |minority_game|
      scores = []
      minority_game.agents.each do |agent|
        scores << agent.score
      end
      scores
    }
  elsif how_many == "one"
    @experimentalist.add_measurement(:agent_score) { |minority_game|
      minority_game.agents.first.score
    }
  end
end

Given /^I set the experimentalist to record the choices? of (all|one) agents?$/ do |how_many|
  if how_many == "all"
    @experimentalist.add_measurement(:agent_choice) { |minority_game| 
      choices = []
      minority_game.agents.each do |agent|
        choices << agent.choice
      end
      choices
    }
  elsif how_many == "one"
    @experimentalist.add_measurement(:agent_choice) { |minority_game| 
      minority_game.agents.first.choice
    }
  end
end

Given /^I set the experimentalist to record the initial choice history$/ do
  first_step = true
  @experimentalist.add_measurement(:initial_choice_history) do |minority_game|
    if first_step
      first_step = false
      choice_list = minority_game.choice_history.as_list
      choice_list.sub_list(0, choice_list.size - 1).to_a
    end
  end
end

Given /^I set the experimentalist to record the choice history of the last (\d*) steps at the (start|end) of the step$/ do |distance_into_past, measurement_time|
  number_of_past_steps = distance_into_past.to_i
  if measurement_time == "start"
    @experimentalist.add_measurement(:choice_history) { |minority_game| 
      minority_game.choice_history.
        as_list(number_of_past_steps + 1).
        sub_list(0, number_of_past_steps).
        to_a
    }
  elsif measurement_time == "end"
    @experimentalist.add_measurement(:choice_history) { |minority_game| 
      minority_game.choice_history.
        as_list(number_of_past_steps + 1).
        sub_list(0, number_of_past_steps).
        to_a
    }
  end
end

Given /^I set the experimentalist to record the choice history of all steps at the (start|end) of the step$/ do |measurement_time|
  if measurement_time == "start"
    @experimentalist.add_measurement(:choice_history) { |minority_game| 
      choice_list = minority_game.choice_history.as_list
      choice_list.sub_list(0, choice_list.size - 1).to_a
    }
  elsif measurement_time == "end"
    @experimentalist.add_measurement(:choice_history) { |minority_game| 
      minority_game.choice_history.as_list.to_a
    }
  end
end

Given /^I set the experimentalist to record the strategy scores$/ do
  @experimentalist.add_measurement(:strategy_score) { |minority_game| 
    strategy_array = []
    minority_game.agents.each do |agent|
      agent.strategies.each do |strategy|
        mapping = {}
        strategy.map.each do |key, value|
          mapping[key.to_a] = value
        end
        score = strategy.score
        strategy_array << [mapping, score]
      end
    end
    strategy_array
  }
end

# Time step related steps
Given /^no time steps have occurred yet$/ do
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

# Experimentalist verification steps
Then /^the minority choice at each time step should be correct with respect to the agent's choices$/ do
  all_choices = @experimentalist.measurement_results(:agent_choice)
  all_minority_choices = @experimentalist.measurement_results(:minority_choice)
  
  all_choices.each_with_index do |agent_choices, step|
    choice_a_count = 0
    choice_b_count = 0
    
    agent_choices.each do |choice|
      if(choice == MSciProject::MinorityGame::Choice::A)
        choice_a_count += 1
      elsif(choice == MSciProject::MinorityGame::Choice::B)
        choice_b_count += 1
      end
    end
    
    minority_choice = if(choice_a_count < choice_b_count)
      MSciProject::MinorityGame::Choice::A
    else
      MSciProject::MinorityGame::Choice::B
    end
    
    minority_choice.should == all_minority_choices[step]
  end
end

Then /^the minority size at each time step should be correct with respect to the agent's choices$/ do
  choices = @experimentalist.measurement_results(:agent_choice)
  minority_sizes = @experimentalist.measurement_results(:minority_size)
  
  choices.each_with_index do |agent_choices, step|
    choice_a_count = 0
    choice_b_count = 0
    
    agent_choices.each do |choice|
      if(choice == MSciProject::MinorityGame::Choice::A)
        choice_a_count += 1
      elsif(choice == MSciProject::MinorityGame::Choice::B)
        choice_b_count += 1
      end
    end
    
    minority_size = [choice_a_count, choice_b_count].min
    
    minority_size.should == minority_sizes[step]
  end
end

Then /^the agent scores at each time step should be correct with respect to the minority choice$/ do
  # fetch the data for each step
  all_scores = @experimentalist.measurement_results(:agent_score)
  all_choices = @experimentalist.measurement_results(:agent_choice)
  all_minority_choices = @experimentalist.measurement_results(:minority_choice)
  
  # create an empty data table for the expected scores
  expected_scores = []
  
  # iterate through the minority choices for each time step
  all_minority_choices.each_with_index do |minority_choice, step|
    
    # iterate through the choices made by each agent at this time step
    all_choices[step].each_with_index do |agent_choice, agent_index|
      
      # initialise an array element for this agent if it has not yet been 
      # initialised
      expected_scores[agent_index] ||= 0
      
      # if the choice made by the agent is equal to the minority choice for 
      # this step, increment the expected score
      if agent_choice == minority_choice
        expected_scores[agent_index] += 1
      end
      
    end
    
    # ensure the actual scores are equal to the expected scores
    all_scores[step].should == expected_scores
    
  end
end

Then /^each choice should have been made approximately an equal number of times$/ do
  choice_a_count = choice_b_count = 0
  
  @experimentalist.measurement_results(:agent_choice).each do |choice|
    case choice
    when MSciProject::MinorityGame::Choice::A
      choice_a_count += 1
    when MSciProject::MinorityGame::Choice::B
      choice_b_count += 1
    end
  end
  
  choice_a_count.should be_between(40, 60)
  choice_b_count.should be_between(40, 60)
end

Then /^the choice history at each time step should be correct with respect to the minority choice$/ do
  initial_choice_history = @experimentalist.measurement_results(
    :initial_choice_history
  )
  choice_histories = @experimentalist.measurement_results(:choice_history)
  minority_choices = @experimentalist.measurement_results(:minority_choice)
  
  choice_list = initial_choice_history
  
  minority_choices.each_with_index do |minority_choice, step|
    choice_list << minority_choice
    choice_histories[step].should == choice_list
  end
end

Then /^the strategy scores at each time step should be correct with respect to the minority choice$/ do
  # fetch the data for each step
  strategies_and_scores = @experimentalist.measurement_results(
    :strategy_score
  )
  choice_histories = @experimentalist.measurement_results(
    :choice_history
  )
  minority_choices = @experimentalist.measurement_results(
    :minority_choice
  )
  
  # create arrays to hold the expected and actual scores at each time step
  total_number_of_strategies = strategies_and_scores.first.size
  expected_scores = Array.new(total_number_of_strategies, 0)
  
  # iterate through the time steps
  minority_choices.each_with_index do |minority_choice, step|
    
    # reset the array of measured scores
    actual_scores = Array.new(total_number_of_strategies, 0)
    
    # iterate through the strategies
    strategies_and_scores[step].each_with_index do |strategy_to_score_map, strategy_index|
      
      # get the strategy and score
      strategy = strategy_to_score_map[0]
      score = strategy_to_score_map[1]

      # increment the expected score if the predicted choice matches the 
      # minority choice
      if(strategy[choice_histories[step]] == minority_choice)
        expected_scores[strategy_index] += 1
      end
      
      # add this score to the array of scores
      actual_scores[strategy_index] = score
    end
    
    # expect the actual scores array to equal the expected scores array
    actual_scores.should == expected_scores
  end
end

Then /^every step except the first should use the highest scoring strategy$/ do
  # fetch the data for each step
  strategies_and_scores = @experimentalist.measurement_results(
    :strategy_score
  )
  choice_histories = @experimentalist.measurement_results(
    :choice_history
  )
  agent_choices = @experimentalist.measurement_results(
    :agent_choice
  )
  
  # find out the number of strategies per agent
  strategies_per_agent = @minority_game.agents.first.strategies.size
  
  # iterate through the steps
  choice_histories.each_with_index do |key, step|
    # skip the first step as the strategy used is initially random
    next if step == 0
    
    # iterate through the agents
    agent_choices[step].each_with_index do |choice, agent_index|
      
      # create arrays to hold the strategies and their scores
      strategies = []
      scores = []
      
      # get the strategies and scores
      (0..(strategies_per_agent - 1)).collect do |strategy_index|
        # the index of the strategy is the number per agent multiplied by 
        # the index of the agent plus the index of the strategy we want
        total_index = agent_index * strategies_per_agent + strategy_index
        
        # we need the strategy scores at the start of the turn
        strategy_and_score = strategies_and_scores[step - 1][total_index]
        
        # add the strategy and score to the arrays
        strategies << strategy_and_score[0]
        scores << strategy_and_score[1]
      end
      
      # get the index of the highest scoring strategy
      max_score = scores.max
      
      # ignore agents with equal scoring strategies
      if scores.select { |score| score == max_score }.size > 1
        next
      else
        highest_scoring_strategy = strategies[scores.index(max_score)]
      end
     
      # ensure the agent's choice is the same as that predicted by the
      # highest scoring strategy
      highest_scoring_strategy[key].should == choice
    end
  end   
end

# Minority game verification steps
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

Then /^it should have equal proportions of agents with memories of length between (\d*) and (\d*)$/ do |lower_bound, upper_bound|
  lower_bound = lower_bound.to_i
  upper_bound = upper_bound.to_i
  
  number_of_different_memory_sizes = upper_bound - lower_bound + 1
  number_of_agents = @minority_game.agents.size
  average_number_of_agents = number_of_agents / number_of_different_memory_sizes
  
  scores = {}
  (lower_bound..upper_bound).each do |size|
    scores[size] = 0
  end
  
  @minority_game.agents.each do |agent|
    scores[agent.memory.capacity] += 1
  end
  
  scores.each_value do |score|
    score.should be_between(
      average_number_of_agents * 0.6, 
      average_number_of_agents * 1.4
    )
  end  
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
      strategy.map.key_set.size.should == mappings.to_i
    end
  end
end

# Miscellaneous steps
Then /^a (.*) should be thrown$/ do |exception|
  @exception.cause.should be_a_kind_of(
    exception.constantize
  )
end