/*
 * File: DumboController.java
 * Created: 17 September 2002, 00:34
 * Author: Stephen Jarvis
 */

/* Preamble for exercise 2:
 * 
 * Discussion about probability of choosing each direction:
 * For this discussion, I will use round() and floor() to refer to the Math.round() and Math.floor() methods respectively.
 * After reading the Java documentation, we can see that the random function is approximately uniform in terms of probability,
 * meaning we can generally assume that this will choose every number with equal probability.
 * Let x be the number returned after calling Math.random()
 * We can see that the round function is equivalent to adding 0.5 and flooring i.e. round(x) = floor(x + 1/2)
 * Therefore our randomly generated integer can be written as round(3x) = floor(3x + 1/2)
 * For the randomly generated integer to be equal to 0, we can see that 0 <= 3x + 1/2 < 1 , since the floor of anything in that interval will give 0.
 * This can be rearranged to get the inequality -1/6 <= x < 1/6 , but since the lowest x can be is 0 (from the random method) this inequality resolves to
 * 0 <= x < 1/6 , meaning the probability of randomly generating the integer 0 with our current approach is 1/6.
 * We can take a similar approach for the other values to get the probabilites for each direction below:
 * 		p(generating 0) = p(moving left) = 1/6
 * 		p(generating 1) = p(moving right) = 1/3
 * 		p(generating 2) = p(moving behind) = 1/3
 * 		p(generating 3) = p(moving ahead) = 1/6
 * This shows us that the probability of choosing each direction (since each direction is assigned to an integer from 0 to 3) is not equal.
 * Therefore the customer is correct in claiming that the robot chooses some directions more than others.
 * We can also observe this by using count.pl in the terminal when running our code on a blank maze.
 * 
 * To solve this problem and ensure fair probabilities, we use the following formula instead:
 * 		floor(4 * random())
 * This works because our random number will generate something between 0 and 1. Multiplying this with 4 gives us a number between 0 and 4.
 * Flooring numbers between each integer interval will ensure that every integer from 0 to 3 can be generated with equal probability:
 * 		floor(0 to 1) = 0
 * 		floor(1 to 2) = 1
 * 		floor(2 to 3) = 2
 * 		floor(3 to 4) = 4
 * Since each of these intervals is given by multiplying 4 by the value returned from the random method, we can be sure that each integer from 0 to 3 is generated with equal probability.
 * 
 * To incorporate the 1 in 8 chance of choosing a new direction randomly, I chose to generate a random integer between 0 and 7 using the same approach from above. 
 * When this generated integer was equal to 0, I made the robot choose a new direction randomly, although this would work for any integer from 0 to 7; the choice of using 0 was arbitrary.
 * I think my approach to incorporating this 1 in 8 chance for the robot was good enough, since my approach did not require a lot of modification to the code without the 1 in 8 chance.
 * 
 */

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Ex2 {

	public void controlRobot(IRobot robot) {

		int randno;
		int direction;
		int wallCount = 0;
		String logEntry = "I'm going";
		String phraseToAdd = "";

		if ((robot.look(IRobot.AHEAD) != IRobot.WALL) && ((int) Math.floor(Math.random()*8) != 0)) { // If the square ahead is a wall or if we choose the number 0 from integers 0-7 (1/8 chance)
			
			direction = IRobot.AHEAD;
			phraseToAdd = " forward";

		}

		else { // If the square in the direction the robot is facing is a wall, then we randomly choose another direction that is not a wall.
			
			// Removing collisions
			while (true) {	// Repeatedly generate directions until we choose one that is not into a wall
			
				// Select a random number
				randno = (int) Math.floor(Math.random()*4); // Modification to the original approach of generating random integers to ensure randomness

				// Convert this to a direction and set this direction to a string to be added to logEntry
				switch (randno) {
					
					case 0:
						direction = IRobot.LEFT;
						phraseToAdd = " left";
						break;
					case 1:
						direction = IRobot.RIGHT;
						phraseToAdd = " right";
						break;
					case 2:
						direction = IRobot.BEHIND;
						phraseToAdd = " backwards";
						break;
					default:
						direction = IRobot.AHEAD;
						phraseToAdd = " forward";
						break;
		
				}

				if (robot.look(direction) != IRobot.WALL)	// Check that the chosen direction is not into a wall
					break;

			}

		}

		// Print a log of movements taken by the robot (done regardless of whether we continue in our current direction or choose a new one)

		logEntry += phraseToAdd; // Adding the direction on to logEntry

		for (int i = 0; i < 4; i++) { // Iterating to get the number of walls neighbouring the robot's current square
			
			int square = IRobot.AHEAD + i;

			if (robot.look(square) == IRobot.WALL)
				wallCount += 1;

		}

		switch (wallCount) {

			case 0:
				phraseToAdd = " at a crossroads";
				break;
			case 1:
				phraseToAdd = " at a junction";
				break;
			case 2:
				phraseToAdd = " down a corridor";
				break;
			case 3:
				phraseToAdd = " at a deadend";
				break;
		
		}

		logEntry += phraseToAdd; // Adding the type of road the robot is on

		System.out.println(logEntry);

		robot.face(direction);	// Face the robot in this direction

	}

}