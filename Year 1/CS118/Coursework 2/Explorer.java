// Code for Exercise 1 of Coursework 2, Part 1 - Laveen Chandnani (2004842)


import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.util.*;

public class Explorer {
    public void controlRobot(IRobot robot) {
        int exits;
        int direction = 0;
        
        exits = nonwallExits(robot);

        switch (exits) {
            case 1:
                direction = deadEnd(robot);
                break;
            case 2:
                direction = corridoor(robot);
                break;
            case 3:
                direction = junction(robot);
                break;
            case 4:
                direction = crossroads(robot);
                break;
        }

        robot.face(direction);
    }

    // Method to choose a random direction, given an ArrayList of absolute directions
    private int randomDirection(IRobot robot, ArrayList<Integer> directions) { 
		int direction;
        int randno;
        
        // Select a random number
        randno = (int) Math.floor(Math.random()*directions.size());
        direction = directions.get(randno);

        return direction;
	}

    private int nonwallExits(IRobot robot) { 
        /* Note: Return values could be
            - 1 => Dead end
            - 2 => Corridoor
            - 3 => Junction
            - 4 => Crossroads
        */
        int nonWallCount = 0;
        for (int i = 0; i < 4; i++) {
            if (robot.look(IRobot.AHEAD + i) != IRobot.WALL) {
                nonWallCount++;
            }
        }
        return nonWallCount;
    }

    private int deadEnd(IRobot robot) {
        // We search for the only available non-wall route and return it's relative direction
        for (int i = 0; i < 4; i++) {
            if (robot.look(IRobot.AHEAD + i) != IRobot.WALL) {
                return IRobot.AHEAD + i;
            }
        } 
        return 0;
    }

    private int corridoor(IRobot robot) {
        /* Our corridoor can either be straight or a corner:
            - straight => Continue ahead
            - corner => Check left and right, and take the appropriate path
        */
        if (robot.look(IRobot.AHEAD) != IRobot.WALL) {
            return IRobot.AHEAD;
        }
        else {
            if (robot.look(IRobot.RIGHT) != IRobot.WALL) {
                return IRobot.RIGHT;
            }
        }
        return IRobot.LEFT; // If we have reached this line it means that we did not return anything above, so the only available route is left
    }

    private int junction(IRobot robot) {
        /* Our approach here will be to create 2 arrays:
            - passageDirections: This will hold the directions which have not been visited before
            - previousDirections: This will hold the directions which have been visited before
           We then decide what to do based on the number of elements in passageDirections
        */
        
        ArrayList<Integer> passageDirections = new ArrayList<Integer>();
        ArrayList<Integer> previousDirections = new ArrayList<Integer>();

        for (int i = 0; i < 4; i++) {
            if (robot.look(IRobot.AHEAD + i) == IRobot.PASSAGE) {
                passageDirections.add(IRobot.AHEAD + i);
            }
            else if (robot.look(IRobot.AHEAD + i) == IRobot.BEENBEFORE) {
                previousDirections.add(IRobot.AHEAD + i);
            }
        }
    
        // Multiple PASSAGE exits:
        if (passageDirections.size() > 1) {
            return randomDirection(robot, passageDirections);
        }
        // Single PASSAGE exit
        else if (passageDirections.size() == 1) {
            return passageDirections.get(0);
        }
        // No PASSAGE exits
        else {
            return randomDirection(robot, previousDirections);
        }

    }

    private int crossroads(IRobot robot) {
        /* Here we can reuse our approach from the junction method above, as the above method will work for a junction with 4 exits i.e. a crossroads */
        return junction(robot);
    }
}
