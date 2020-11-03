/*
 * File: DumboController.java
 * Created: 17 September 2002, 00:34
 * Author: Stephen Jarvis
 */

/*
 * Preamble for exercise 1:
 *
 * To ensure that a robot does not hit a wall, I used the part of the code from DumboController.java 
 * which converts a random number into a direction and placed this into a loop. 
 * This was done so that I was able to reuse that block of code repeatedly until the randomly chosen direction 
 * happened to not be facing at a wall. I checked that the direction was not facing a wall 
 * using "robot.look(direction)" and comparing this to "IRobot.WALL" .
 * I also created a string variable called logEntry, as this would allow me to add the appropriate words and phrases as the program 
 * determined what type of path and direction the robot was going in. This could then be printed out at the end just before the robot moves.
 * This approach also makes the code easier to maintain and , since if the customer requires any further additions to the code then 
 * only minor changes are needed.
 */

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Ex1 {

	public void controlRobot(IRobot robot) {

		int randno;
		int direction;
		int wallCount = 0;
		String logEntry = "I'm going";
		String phraseToAdd = "";

		// Removing collisions
		while (true) {	// Repeatedly generate directions until we choose one that is not into a wall
		
			// Select a random number
			randno = (int) Math.round(Math.random()*3);

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

		// Print a log of movements taken by the robot

		logEntry += phraseToAdd; // Adding the direction on to logEntry

		for (int i = 0; i < 4; i++) { // Iterating to get the number of walls neighbouring the robot's current square
			
			int square = IRobot.AHEAD + i;

			if (robot.look(square) == IRobot.WALL)
				wallCount += 1;	

		}

		switch (wallCount) { // Determining the type of path the robot is on, based on the number of adjacent walls
		
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

		logEntry += phraseToAdd; // Adding the type of path the robot is on

		System.out.println(logEntry);

		robot.face(direction);	// Face the robot in this direction

	}

}

/* Discussion about probability of choosing each direction:
 * 
 * For the sake of this discussion, I will use round() and floor() to refer to the Math.round() and Math.floor() methods respectively.
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
 * We can also observe this by using count.pl in the terminal when running our code.
 */
