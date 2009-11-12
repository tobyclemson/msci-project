Feature: Versatile minority game construction
  
  As a user of the minority game library
  I want to be able to construct minority games with different properties
  So that I can conduct different experiments
  
  Scenario Outline: construct a standard minority game
    Given I don't have a minority game instance
    When I try to construct a <type> type minority game
    Then I should end up with an instance of <class_name>
    
    Examples:
      | type         | class_name                                            |
      | standard     | MSciProject::MinorityGame::StandardMinorityGame       |
      | evolutionary | MSciProject::MinorityGame::EvolutionaryMinorityGame |