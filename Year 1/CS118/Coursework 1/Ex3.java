/*
 * File:    Broken	.java
 * Created: 7 September 2001
 * Author:  Stephen Jarvis
 */

/** Preamble for exercise 3:
 * 
 * When it came to designing my headingController method, I used the following approach:
 * 		- Create a hash map to hold the return values of isTargetNorth and isTargetEast as keys, and the preferred directions as values.
 * 		- Use this to obtain the preferred directions for the robot's current position.
 * 		- Check the alignment of the robot with the target square, and create an arraylist of directions we want to check accordingly.
 * 		- Set the robot to make it's next move along the chosen direction.
 * 
 * I decided to use a hashmap as I thought this would be the best way to relate the position of the robot (relative to the target) to the preferred direction we want the robot to go in.
 * I accounted for the case when the robot was vertically or horizontally in line with the target by assigning the key value of 0 (which refers to this exact situation) to the value 0 as well.
 * This is so I can essentially check for the case when the robot is aligned when using if-else statements to decide the direction the robot should take. 
 * I also decided to use an arraylist because the different positions that our robot could be in allow for a different number of possible directions the robot could go in. I also decided
 * to keep the process of choosing a random direction in a separate function; I passed my arraylist into this function, which generates a random number from 0 to the size of the list - 1 to obtain 
 * an index to decide the direction.
 * 
 * The design for the heading controller does not always ensure that the robot reaches the target, and this is due to the requirements of the specification for this exercise. 
 * The heading controller is designed to always select a heading that will move the robot closer to the target unless there are no such headings available, in which case it will randomly 
 * choose between other headings which may not move the robot closer to the target, but instead farther. 
 * Suppose that the robot, whilst following the directions generated in order to get itself closer to the target with each move, reaches a dead end on it's way to the target. Then, by the specification,
 * the robot will attempt to go backwards, since that is the only direction it can go without colliding with a wall. However, by going backwards, it has a new move which will allow it to get closer to the target,
 * which is the same move that brought it into the dead end. This will repeat until the user stops the run. Therefore, it is not ensured that the robot always moves closer towards the target, neither is it ensured that the robot always finds the target.
 * 
 * To improve the robot, I think writing the robot in such a way that it can remember it's past few moves, so that when it gets stuck in a deadend or some sort of loop of movements, it is able to get out of them by essentially
 * backtracking it's previous movements.
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

	// Method for working out the state of a square at some given absolute direction 
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
