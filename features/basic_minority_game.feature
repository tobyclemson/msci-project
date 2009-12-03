Feature: Basic minority game execution

  As a user of the minority game library
  I want to be able to run a simulation of the standard minority game
  So that I can investigate its dynamics
  
  Scenario: agents in the minority choice have 1 point each after one time step
    Given I have a standard minority game
    And no time steps have occurred yet
    When I take a time step
    Then each agent that made the minority choice should have 1 point
    
  Scenario: agent scores are in a valid range given the number of time steps
    Given I have a standard minority game
    And no time steps have occurred yet
    When I take 10 time steps
    Then the scores should range between 0 and 10
    
  Scenario: strategies that would have predicted the correct minority choice each receive a point
    Given I have a standard minority game
    And no time steps have occurred yet
    And I store the initial choice history
    When I take a time step
    Then each strategy that would have predicted the correct minority choice given the choice history has 1 point
  
  Scenario: strategy scores are in a valid range given the number of time steps
    Given I have a standard minority game
    And no time steps have occurred yet
    When I take 10 time steps
    Then the strategy scores should range between 0 and 10
  
  Scenario: choice attendance varies over time steps
    Given I have a standard minority game
    And I have an experimentalist
    And I set the experimentalist to record the attendance of choice 'A'
    When I take 100 time steps
    Then the measured attendances should be varied
  
  Scenario: choice attendance of a particular choice is in a valid range at each time step
    Given I have a standard minority game with 10001 agents
    And I have an experimentalist
    And I set the experimentalist to record the attendance of choice 'A'
    When I take 100 time steps
    Then the measured attendances should range between 0 and 10000
    
  Scenario: the minority choice size is less than half the number of agents minus 1 at each time step
    Given I have a standard minority game with 10001 agents
    And I have an experimentalist
    And I set the experimentalist to record the attendance of the minority choice
    When I take 100 time steps
    Then the measured attendances should range between 0 and 5000
    
  Scenario: the choice history should increase in size with time
    Given I have a standard minority game with an agent memory size of 5
    When I take 100 time steps
    Then the choice history should increase in size by 100