Feature: Unintelligent random choice agents
  
  As a user of the minority game library
  I want to be able to run simulations with random choice agents
  So that I have a baseline against which to compare other results
  
  Scenario: construct a minority game with a 'random' type agent
    Given I have a properties hash
    And I set the 'agent-type' property to 'random'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And it should have agents that are instances of MSciProject::MinorityGame::RandomAgent
    
  Scenario: random agents make each choice with equal probability at each time step
    Given I have a standard minority game with random agents
    And I have an experimentalist
    And I set the experimentalist to record the choices of one agent in the game
    When I take 100 time steps
    Then each choice should have been made approximately an equal number of times