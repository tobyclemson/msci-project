Feature: agents have friends whose opinion influences their decision making

  As a user of the minority game library
  I want agents to be on a network
  So that I can investigate the effect of local information on the game dynamics

  Scenario: construct a minority game with a community of disconnected agents
    Given I have a properties hash
    When I set the 'network-type' property to 'empty'
    And I set the 'agent-type' property to 'networked'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And the associated community should contain no friendships
    And each agent in the community should be friends with no other
    And each agent's social network should consist of just that agent

  Scenario: construct a minority game with a completely connected community
    Given I have a properties hash
    When I set the 'network-type' property to 'complete'
    And I set the 'agent-type' property to 'networked'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And each agent in the community should be friends with all others
    And each agent's social network should consist of that agent connected to all other agents

  Scenario: construct a minority game with networked agents
    Given I have a properties hash
    When I set the 'agent-type' property to 'networked'
    And I construct a minority game with the properties hash
    Then I should have a minority game
    And it should have agents that are instances of msci.mg.agents.BasicNetworkedAgent

  Scenario: the strategy scores are correct at each time step
    Given I have a minority game with networked agents with a memory size of 2
    And I have an experimentalist
    And I set the experimentalist to record the choice history of the last 2 steps at the start of the step
    And I set the experimentalist to record the minority choice
    And I set the experimentalist to record the strategy scores
    When I take 20 time steps
    Then the strategy scores at each time step should be correct with respect to the minority choice
    
  Scenario: the correct prediction counts are correct at each time step
    Given I have a minority game with networked agents connected by a random network
    And I have an experimentalist
    And I set the experimentalist to record the minority choice
    And I set the experimentalist to record the predictions of all agents
    And I set the experimentalist to record the correct prediction counts of all agents
    When I take 20 time steps
    Then the correct prediction counts at each time step should be correct with respect to the predictions and the minority choice
    
  Scenario: the agents use the highest scoring strategy for their predictions at each time step
    Given I have a minority game with networked agents with a memory size of 2
    And I have an experimentalist
    And I set the experimentalist to record the choice history of the last 2 steps at the start of the step
    And I set the experimentalist to record the predictions of all agents
    And I set the experimentalist to record the strategy scores
    When I take 20 time steps
    Then every agent prediction except the first should use the highest scoring strategy
    
  Scenario: the agents follow the prediction of the agent in their social network that has predicted correctly most often
    Given I have a minority game with networked agents connected by a random network
    And I have an experimentalist
    And I set the experimentalist to record the predictions of all agents
    And I set the experimentalist to record the choices of all agents
    And I set the experimentalist to record the minority choice
    And I set the experimentalist to record the agent identification numbers
    When I take 20 time steps
    Then every agent choice except the first should follow the agent in their social network that has predicted correctly most often
  
  Scenario: the prediction of the agents best friend and the agents choice are the same
    Given I have a minority game with networked agents connected by a random network
    And I have an experimentalist
    And I set the experimentalist to record the predictions of the best friend of all agents
    And I set the experimentalist to record the choices of all agents
    When I take 20 time steps
    Then every agent choice should follow the prediction of their best friend
    