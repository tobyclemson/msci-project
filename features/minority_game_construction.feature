Feature: Versatile minority game construction
  
  As a user of the minority game library
  I want to be able to construct minority games with different properties
  So that I can conduct different experiments
  
  Scenario Outline: construct a minority game instance of a particular type
    Given I don't have a minority game instance
    When I construct a minority game with the value <type> for the 'type' property
    Then I should end up with an instance of <class_name>
    
    Examples:
      | type         | class_name                                            |
      | standard     | MSciProject::MinorityGame::StandardMinorityGame       |
      | evolutionary | MSciProject::MinorityGame::EvolutionaryMinorityGame   |
