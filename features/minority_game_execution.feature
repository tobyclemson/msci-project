Feature: Standard minority game execution

  As a user of the minority game library
  I want to be able to run a simulation of the standard minority game
  So that I can investigate its dynamics
  
  Scenario: agent scores are correct after one time step
    Given I have a standard minority game
    And no time steps have occurred yet
    When I take a time step
    Then the sum of the agents scores should equal the number of agents making the minority choice
  
  Scenario: agent scores increase with time
    Given I have a standard minority game
    And no time steps have occurred yet
    When I take 10 time steps
    Then some agents should have a non zero score
    
  Scenario: strategy scores increase with time
    Given I have a standard minority game
    And no time steps have occurred yet
    When I take 10 time steps
    Then some strategies should have a non zero score
  
  Scenario: strategy scores are in a valid range given the number of time steps
    Given I have a standard minority game
    And no time steps have occurred yet
    When I take 10 time steps
    Then the strategy scores should range between 0 and 10
  
  Scenario: agent scores are in a valid range given the number of time steps
    Given I have a standard minority game
    And no time steps have occurred yet
    When I take 10 time steps
    Then the scores should range between 0 and 10
  
  Scenario: choice attendance varies over time steps
    Given I have a standard minority game
    And I have an experimentalist
    And I set the experimentalist to record the attendance of choice '0'
    When I take 100 time steps
    Then the measured attendances should be varied
  
  Scenario: choice attendance is in a valid range given the number of time steps
    Given I have a standard minority game with 10000 agents
    And I have an experimentalist
    And I set the experimentalist to record the attendance of choice '0'
    When I take 100 time steps
    Then the measured attendances should range between 0 and 10000
    
  Scenario: the history string should vary with time
    Given I have a standard minority game with a history string length of 5
    And I have an experimentalist
    And I set the experimentalist to record the history string
    When I take 100 time steps
    Then the measured history strings should be varied