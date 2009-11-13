Feature: Versatile minority game construction
  
  As a user of the minority game library
  I want to be able to construct minority games with different properties
  So that I can conduct different experiments
  
  Scenario Outline: construct a minority game instance of a particular type
    Given I have a properties hash
    When I set the 'type' property to '<type>'
    And I construct a minority game with the properties hash
    Then I should end up with an instance of <class_name>
    
    Examples:
      | type         | class_name                                            |
      | standard     | MSciProject::MinorityGame::StandardMinorityGame       |
      | evolutionary | MSciProject::MinorityGame::EvolutionaryMinorityGame   |

  Scenario: construct a minority game with 10000 agents
    Given I have a properties hash
    When I set the 'number-of-agents' property to '10000'
    And I construct a minority game with the properties hash
    Then I should end up with a minority game containing 10000 agents