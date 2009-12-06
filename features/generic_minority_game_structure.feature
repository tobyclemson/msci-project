Feature: general structure of the minority game

  As a user of the minority game library
  I want the library to provide a framework to facilitate minority games
  So that I can create minority games of different types
  
  Scenario Outline: the minority choice is correct at each time step
    Given I have a standard minority game with <agent_type> agents
    And I have an experimentalist
    And I set the experimentalist to record the choices of all agents
    And I set the experimentalist to record the minority choice
    When I take 20 time steps
    Then the minority choice at each time step should be correct with respect to the agent's choices
    
    Examples:
      | agent_type |
      | basic      |
      | random     |
  
  Scenario Outline: the minority size is correct at each time step
    Given I have a standard minority game with <agent_type> agents
    And I have an experimentalist
    And I set the experimentalist to record the choices of all agents
    And I set the experimentalist to record the minority size
    When I take 20 time steps
    Then the minority size at each time step should be correct with respect to the agent's choices
    
    Examples:
      | agent_type |
      | basic      |
      | random     |
  
  Scenario Outline: the agent scores are correct at each time step
    Given I have a standard minority game with <agent_type> agents
    And I have an experimentalist
    And I set the experimentalist to record the scores of all agents
    And I set the experimentalist to record the choices of all agents
    And I set the experimentalist to record the minority choice
    When I take 20 time steps
    Then the agent scores at each time step should be correct with respect to the minority choice
    
    Examples:
      | agent_type |
      | basic      |
      | random     |
  
  Scenario Outline: the choice history is updated correctly at each time step
    Given I have a standard minority game with <agent_type> agents
    And I have an experimentalist
    And I set the experimentalist to record the minority choice
    And I set the experimentalist to record the initial choice history
    And I set the experimentalist to record the choice history of all steps at the end of the step
    When I take 20 time steps
    Then the choice history at each time step should be correct with respect to the minority choice
    
    Examples:
      | agent_type |
      | basic      |
      | random     |
  