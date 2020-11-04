/*
 * File:    Broken	.java
 * Created: 7 September 2001
 * Author:  Stephen Jarvis
 */

/** Preamble for exercise 3:
 * 
 * 
 * 
 */

import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.util.*;

public class Ex3 {

	public void reset() {
		ControlTest.printResults();
	}

	// Method for working out whether the target is above, below or at the same latitude as the robot
    private byte isTargetNorth(IRobot robot) { 

		// Using built-in method to determine the sign of the difference in y-coordinates
		byte result = (byte) Math.signum(-(robot.getTargetLocation().y - robot.getLocation().y)); 
		
		return result;
	
	}

	// Method for working out whether the target is to the right, left or at the same longitude as the robot
	private byte isTargetEast(IRobot robot) { 

		// Using built-in method to determine the sign of the difference in x-coordinates
		byte result = (byte) Math.signum((robot.getTargetLocation().x - robot.getLocation().x)); 
		
		return result;
	
	}	

	// Method for working out the state of a square at some given bsolute direction 
	private int lookHeading(IRobot robot, int heading) {
		/* We calculate the state of the square by doing the following steps:
		 * 		- Work out the difference between the integers representing the absolute direction of interest and the relative direction currently faced
		 * 		- Add 4 to this result in case the difference is negative 
		 * 		- Find the remainder when divided by 4 to get the difference between 0 and 3
		 * 		- Add this to IRobot.AHEAD to look at the absolute direction of interest 
		 */
		return robot.look(IRobot.AHEAD + (heading - robot.getHeading() + 4) % 4);

	}

	// To choose between two squares to go down
	private int randomDirection(IRobot robot, ArrayList<Integer> directions) { // Method takes absolute directions

		int direction;
		int randno;
		// Removing collisions
		while (true) {	// Repeatedly generate directions until we choose one that is not into a wall
		
			// Select a random number
			randno = (int) Math.floor(Math.random()*directions.size());

			direction = directions.get(randno);

			// Check that the chosen direction is not into a wall
			if (lookHeading(robot, direction) != IRobot.WALL)	
				return direction;

		}
	
	}
	
	private int headingController(IRobot robot) {

		int direction, horizontalDirection, verticalDirection, horizontalVal, verticalVal;
		ArrayList<Integer> allowedDirections = new ArrayList<Integer>(); // To hold all possible directions we want to consider
		
		// Dictionary to map integers representing if the target is North/South to absolute directions
		HashMap<Integer, Integer> ns = new HashMap<>();
		// Initialising dictonary with values
		ns.put(1, IRobot.NORTH);
		ns.put(0, 0); // Using 0 as dummy value when the robot is aligned vertically or horizontally
		ns.put(-1, IRobot.SOUTH);

		// Dictionary to map integers representing if the target is East/West to absolute directions
		HashMap<Integer, Integer> ew = new HashMap<>();
		// Initialising dictionary with values
		ew.put(1, IRobot.EAST);
		ew.put(0, 0); // Using 0 as dummy value when the robot is aligned vertically or horizontally
		ew.put(-1, IRobot.WEST);	

		// Setting the desired directions from the dictionary
		horizontalVal = (int) isTargetEast(robot);
		verticalVal = (int) isTargetNorth(robot);
		horizontalDirection = ew.get(horizontalVal); 
		verticalDirection = ns.get(verticalVal);

		if (horizontalDirection == 0) { // Meaning we were aligned horizontally

			if (lookHeading(robot, verticalDirection) != IRobot.WALL) { // No wall means the robot should go in that direction

				direction = verticalDirection;

			}

			else { // We decide randomly from paths around it, since our choice in horizontal direction does not matter

				allowedDirections.add(IRobot.EAST);
				allowedDirections.add(IRobot.WEST);
				allowedDirections.add(ns.get(-verticalVal));
				direction = randomDirection(robot, allowedDirections);

			}

		}
		else if (verticalDirection == 0) { // Meaning we were aligned vertically

			if (lookHeading(robot, horizontalDirection) != IRobot.WALL) {

				direction = horizontalDirection;

			}
			else {

				allowedDirections.add(IRobot.NORTH);
				allowedDirections.add(IRobot.SOUTH);
				allowedDirections.add(ew.get(-horizontalVal));
				direction = randomDirection(robot, allowedDirections);

			}

		}
		else { // Not aligned horizontally nor vertically

			int verticalSquare = lookHeading(robot, verticalDirection);
			int horizontalSquare = lookHeading(robot, horizontalDirection);

			if ((horizontalSquare != IRobot.WALL) && (verticalSquare != IRobot.WALL)) {

				allowedDirections.add(verticalDirection);
				allowedDirections.add(horizontalDirection);
				direction = randomDirection(robot, allowedDirections);

			}
			else if ((verticalSquare != IRobot.WALL) && (horizontalSquare == IRobot.WALL)) { 
				
				direction = verticalDirection;

			}
			else if ((horizontalSquare != IRobot.WALL) && (verticalSquare == IRobot.WALL)) {
				
				direction = horizontalDirection;

			}
			else {

				allowedDirections.add(ew.get(-horizontalVal));
				allowedDirections.add(ns.get(-verticalVal));
				direction = randomDirection(robot, allowedDirections);

			}
			
		}

		return direction;	

	}

	public void controlRobot(IRobot robot) {

		int direction;

		direction = headingController(robot);

		ControlTest.test(direction, robot);

		// Face the robot in this direction
		robot.setHeading(direction);	

	}

}
