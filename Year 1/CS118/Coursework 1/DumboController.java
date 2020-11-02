/*
* File: DumboController.java
* Created: 17 September 2002, 00:34
* Author: Stephen Jarvis
*/

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class DumboController {

	public void controlRobot(IRobot robot) {

		int randno;
		int direction;
		int wallCount;
		String logEntry = "I'm going forward";
		String directionWord = "";

		// Removing collisions
		while (true) {	// Repeatedly generate directions until we choose one that is not into a wall

			// Select a random number
			randno = (int) Math.round(Math.random()*3);

			// Convert this to a direction and set this direction to a string to be added to logEntry
			switch (randno) {
				case 0:
					direction = IRobot.LEFT;
					directionWord = "left";
				case 1:
					direction = IRobot.RIGHT;
					directionWord = "right";
				case 2:
					direction = IRobot.BEHIND;
					directionWord = "backwards";
				default:
					direction = IRobot.AHEAD;
					directionWord = "forwards";
			}

			if (robot.look(direction) != IRobot.WALL)	// Check that the chosen direction is not into a wall
				break;

		}

		// Print a log of movements taken by the robot

		logEntry += directionWord;

		System.out.println(logEntry);

		robot.face(direction);	// Face the robot in this direction

	}

}
