Feature: the social network connecting the agents can be varied
	
	As a user of the minority game library
  I want to be able to choose different social networks connecting the agents
  So that I can investigate how the network structure affects the dynamics of the game

	Scenario Outline: construct a minority game with an Erdos-Renyi type random graph as the social network
    Given I have a properties hash
    When I set the 'network-type' property to 'random'
    And I set the 'agent-type' property to 'networked'
    And I set the 'link-probability' property to '<link_probability>'
    And I set the 'number-of-agents' property to '<number_of_agents>'
    And I construct a minority game with the properties hash
    Then I should have a minority game
		And it should have <number_of_agents> agents
    And each agent in the community should be friends with approximately <friend_count> others

    Examples: 
      | link_probability | number_of_agents | friend_count |
      | 0.1              | 201              | 20           |
      | 0.5              | 501              | 250          |
      | 0.9              | 101              | 90           |
  
  Scenario Outline: construct a minority game with a Barabasi-Albert type scale-free graph as the social network
    Given I have a properties hash
    When I set the 'network-type' property to 'scale-free'
    And I set the 'agent-type' property to 'networked'
    And I set the 'average-number-of-friends' property to '<average_number_of_friends>'
    And I set the 'number-of-agents' property to '<number_of_agents>'
    And I construct a minority game with the properties hash
    Then I should have a minority game
		And it should have <number_of_agents> agents
    And the maximum degree of the community should be greater than <maximum_degree>
    And the average degree of the community should be approximately <average_degree>
    
    Examples:
      | average_number_of_friends | number_of_agents | maximum_degree | average_degree |
      | 20                        | 201              | 50             | 20             |
      | 40                        | 201              | 80             | 40             |
      | 12                        | 501              | 60             | 12             |
	
	Scenario Outline: construct a minority game with a regular ring graph as the social network
		Given I have a properties hash
		When I set the 'network-type' property to 'regular-ring'
		And I set the 'number-of-friends-in-each-direction' property to '<number_of_friends_in_each_direction>'
		And I set the 'number-of-agents' property to '<number_of_agents>'
		And I set the 'agent-type' property to 'networked'
		And I construct a minority game with the properties hash
		Then I should have a minority game
		And it should have <number_of_agents> agents
		And it should have <number_of_friendships> friendships
		And each agent in the community should be friends with <friend_count> others
		
		Examples:
      | number_of_friends_in_each_direction | number_of_agents | number_of_friendships | friend_count |
      | 10                        					| 301              | 3010             		 | 20           |
      | 40                        					| 201              | 8040             		 | 80           |
      | 22                        					| 501              | 11022             		 | 44           |
	
	
	# Need to check that the number of friends each agent and its friends has in
	# common is correct to ensure we have a ring graph
	# Need to check what happens if the number of friends in each direction is
	# greater than half the total number of agents