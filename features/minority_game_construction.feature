Feature: Versatile minority game construction
  
  As a user of the minority game library
  I want to be able to construct minority games with different properties
  So that I can conduct different experiments
  
  Scenario Outline: construct a minority game instance of a particular type
    Given I have a properties hash
    When I set the 'type' property to '<type>'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And its class should be <class_name>
    
    Examples:
      | type         | class_name                                            |
      | standard     | MSciProject::MinorityGame::StandardMinorityGame       |
      | evolutionary | MSciProject::MinorityGame::EvolutionaryMinorityGame   |
      
  Scenario Outline: construct a minority game of an invalid type
    Given I have a properties hash
    When I set the 'type' property to '<invalid_type>'
    And I construct a minority game with the properties hash
    Then a Java::Lang::IllegalArgumentException should be thrown
    
    Examples:
      | invalid_type |
      | not-a-type   |
      | 1            |
      | 0.6          |

  Scenario Outline: construct a minority game with a particular number of agents
    Given I have a properties hash
    When I set the 'number-of-agents' property to '<number_of_agents>'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And it should have <number_of_agents> agents
    
    Examples:
      | number_of_agents |
      | 0                |
      | 1                |
      | 10               |
      | 10000            |
      
  Scenario Outline: construct a minority game with an invalid number of agents 
    Given I have a properties hash
    When I set the 'number-of-agents' property to '<invalid_number_of_agents>'
    And I construct a minority game with the properties hash
    Then a Java::Lang::IllegalArgumentException should be thrown
    
    Examples:
      | invalid_number_of_agents |
      | -1                       |
      | jeremy                   |
      | 0.486                    |
    
  Scenario Outline: constructs a minority game with agents with a particular number of strategies
    Given I have a properties hash
    When I set the 'number-of-strategies-per-agent' property to '<number_of_strategies_per_agent>'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And it should have agents with <number_of_strategies_per_agent> strategies
    
    Examples:
      | number_of_strategies_per_agent |
      | 0                              |
      | 1                              |
      | 10                             |
      | 1000                           |
      
  Scenario Outline: construct a minority game with an invalid number of strategies per agent
    Given I have a properties hash
    When I set the 'number-of-strategies-per-agent' property to '<invalid_number_of_strategies>'
    And I construct a minority game with the properties hash
    Then a Java::Lang::IllegalArgumentException should be thrown

    Examples:
      | invalid_number_of_strategies |
      | -1                           |
      | jeremy                       |
      | 0.486                        |
    
  Scenario Outline: construct a minority game with a history string of a particular length
    Given I have a properties hash
    When I set the 'agent-memory-size' property to '<agent-memory-size>'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And it should have a choice history with initial length <agent-memory-size>
    
    Examples:
      | agent-memory-size |
      | 0                 |
      | 1                 |
      | 10                |
    
  Scenario Outline: construct a minority game with a history string of an invalid length
    Given I have a properties hash
    When I set the 'agent-memory-size' property to '<invalid_length>'
    And I construct a minority game with the properties hash
    Then a Java::Lang::IllegalArgumentException should be thrown

    Examples:
      | invalid_length |
      | -1             |
      | jeremy         |
      | 0.486          |
      
  Scenario Outline: construct a minority game with agents of a particular type
    Given I have a properties hash
    When I set the 'agent-type' property to '<agent_type>'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And it should have agents that are instances of <agent_class_name>
    
    Examples:
      | agent_type   | agent_class_name                                      |
      | basic        | MSciProject::MinorityGame::BasicAgent                 |
      | learning     | MSciProject::MinorityGame::LearningAgent              |
