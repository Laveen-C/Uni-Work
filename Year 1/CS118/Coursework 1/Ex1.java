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
 * determined what type of road and direction the robot was going in. This could then be printed out at the end just before the robot moves.
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
					phraseToAdd = " forwards";
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
