Feature: Random choice version of the minority game
  
  As a user of the minority game library
  I want to be able to run simulations of the random choice minority game
  So that I have a baseline against which to compare other results
  
  Scenario: each choice is made with equal probability at each time step
    Given I have a minority game with random agents
    And I have an experimentalist
    And I set the experimentalist to record the choice of one agent
    When I take 100 time steps
    Then each choice should have been made approximately an equal number of times
  