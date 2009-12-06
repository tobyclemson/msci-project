Feature: Basic version of the minority game with strategic agents

  As a user of the minority game library
  I want to be able to run a simulation of the basic minority game
  So that I can investigate its dynamics
  
  Scenario: the strategy scores are correct at each time step
    Given I have a standard minority game with basic agents with a memory size of 2
    And I have an experimentalist
    And I set the experimentalist to record the choice history of the last 2 steps at the start of the step
    And I set the experimentalist to record the minority choice
    And I set the experimentalist to record the strategy scores
    When I take 20 time steps
    Then the strategy scores at each time step should be correct with respect to the minority choice
  
  Scenario: the agents use the highest scoring strategy at each time step
    Given I have a standard minority game with basic agents with a memory size of 2
    And I have an experimentalist
    And I set the experimentalist to record the choice history of the last 2 steps at the start of the step
    And I set the experimentalist to record the choices of all agents
    And I set the experimentalist to record the strategy scores
    When I take 20 time steps
    Then every step except the first should use the highest scoring strategy
    
  # check random strategies are used during the first term and if more than
  # one has the highest score
  