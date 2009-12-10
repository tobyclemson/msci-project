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
      | 1                |
      | 11               |
      | 10001            |
  
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
  
  Scenario Outline: construct a minority game with an even number of agents
    Given I have a properties hash
    When I set the 'number-of-agents' property to '<even_number_of_agents>'
    And I construct a minority game with the properties hash
    Then a Java::Lang::IllegalArgumentException should be thrown
    
    Examples:
      | even_number_of_agents |
      | 0                     |
      | 2                     |
      | 10                    |
      | 10000                 |
  
  Scenario Outline: construct a minority game with agents with a particular number of strategies
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
  
  Scenario Outline: construct a minority game with agents with a particular memory size
    Given I have a properties hash
    When I set the 'agent-memory-size' property to '<agent_memory_size>'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And it should have a choice history with initial length <agent_memory_size>
    
    Examples:
      | agent_memory_size |
      | 0                 |
      | 1                 |
      | 10                |
  
  Scenario Outline: construct a minority game with an agent memory size of an invalid length
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
      | random       | MSciProject::MinorityGame::RandomAgent                |
  
  Scenario Outline: agent strategies have 2^m mappings for an agent memory size of m
    Given I have a properties hash
    When I set the 'agent-memory-size' property to '<agent_memory_size>'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And it should have agents with strategies with <number_of_mappings> mappings
    
    Examples:
      | agent_memory_size | number_of_mappings |
      | 2                 | 4                  |
      | 3                 | 8                  |
      | 6                 | 64                 |
  