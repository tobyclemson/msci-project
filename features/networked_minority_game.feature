Feature: agents have friends whose opinion influences their decision making

  As a user of the minority game library
  I want agents to be on a network
  So that I can investigate the effect of local information on the game dynamics
  
  Scenario: construct a minority game with a community of disconnected agents
    Given I have a properties hash
    When I set the 'network-type' property to 'empty'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And the associated community should contain no friendships
    And each agent in the community should be friends with no other
    And each agent's social network should consist of just that agent
    
  Scenario: construct a minority game with a completely connected community
    Given I have a properties hash
    When I set the 'network-type' property to 'complete'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And each agent in the community should be friends with all others
    And each agent's social network should consist of that agent connected to all other agents
  
  Scenario: construct a minority game with an Erdos-Renyi type random graph as the social network
    Given I have a properties hash
    When I set the 'network-type' property to 'random'
    And I set the 'link-probability' property to '0.1'
    And I set the 'number-of-agents' property to '501'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And each agent in the community should be friends with approximately 50 others
  