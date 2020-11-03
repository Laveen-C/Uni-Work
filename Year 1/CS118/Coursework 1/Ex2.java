/*
 * File: DumboController.java
 * Created: 17 September 2002, 00:34
 * Author: Stephen Jarvis
 */

 /* Preamble for exercise 2:
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

		if (robot.look(IRobot.AHEAD) != IRobot.WALL) { // If the square in the direction the robot is facing is not a wall, then we go in that direction
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