// Code for Explorer.java - Laveen Chandnani (2004842)

/** Preamble for Ex2:
 * 
 *  
 */


import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.util.*;

public class Ex2 {
    private int pollRun = 0; // Incremented after each move
    private RobotDataNoCoord robotData; // Data store for junctions
    private int explorerMode = 1; // 1 = explore, 0 = backtrack

    public void controlRobot(IRobot robot) {
        if ((robot.getRuns() == 0) && (pollRun == 0)) { // Checking if we are currently on the first run of a new maze
            robotData = new RobotDataNoCoord(); // Resets the data store
            explorerMode = 1;
        }

        // We then execute the appropriate block of code depending on if we are exploring or backtracking
        if (explorerMode == 1) {
            exploreControl(robot);
        }
        else {
            backtrackControl(robot);
        }

    }

    public void exploreControl(IRobot robot) {
        int exits;
        int direction = 0;

        exits = nonwallExits(robot);

        if (exits == 1) { // Dead end case
            direction = deadEnd(robot);
            explorerMode = 0; // We want to backtrack when we are at a dead end
        }
        else if (exits == 2) { // Corridoor case
            direction = corridoor(robot);
        }
        else { // Junction and crossroads case
            direction = junction(robot);
        }

        robot.face(direction);
        pollRun++; // Increment pollRun so that the data is not reset each time the robot moves
    }

    public void backtrackControl(IRobot robot) {
        int direction;
        int heading;
        int nonwallExits;
        
        nonwallExits = nonwallExits(robot); 

        if (nonwallExits == 1) { // Dead end case
            direction = deadEnd(robot); 
        }
        else if (nonwallExits == 2) { // Corridoor case
            direction = corridoor(robot);
        }
        else { // Junction and crossroads case
            if ((nonwallExits - beenbeforeExits(robot)) > 0) { // Meaning we have passage exits
                explorerMode = 1;
            }
            direction = junction(robot);
        }
        robot.face(direction);
        pollRun++; // Increment pollRun so that the data is not reset each time the robot moves
    }

    public void reset() {
        explorerMode = 1;
    }

    private int getBacktrackDirection(IRobot robot, int heading) { 
        // Useful to have a method which can convert the absolute direction we want the robot to head in to a relative direction the opposite way
        return IRobot.AHEAD + (heading - robot.getHeading() + 6) % 4;
    }

    // Method to choose a random direction, given an ArrayList of absolute directions
    private int randomDirection(IRobot robot, ArrayList<Integer> directions) { 
		int direction;
        int randno;
        
        // Selecting a random number
        randno = (int) Math.floor(Math.random()*directions.size());
        direction = directions.get(randno);

        return direction;
	}

    private int nonwallExits(IRobot robot) { 
        /* Note: Return values could be
            - 1 => Dead end
            - 2 => Corridoor
            - 3/4 => Junction
        */
        int nonWallCount = 0;
        for (int i = 0; i < 4; i++) {
            if (robot.look(IRobot.AHEAD + i) != IRobot.WALL) {
                nonWallCount++;
            }
        }
        return nonWallCount;
    }

    private int beenbeforeExits(IRobot robot) {
        // This will return us the number of BEENBEFORE exits adjacent to the current square the robot is occupying
        int exits = 0;

        for (int i  = 0; i < 4; i++) {
            if (robot.look(IRobot.AHEAD + i) == IRobot.BEENBEFORE) {
                exits++;
            }
        }
        return exits;
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

        // If this is a new junction that has not been previously visited, we need to store data for this junction
        if (beenbeforeExits(robot) == 1) {
            robotData.headings.push(robot.getHeading()); // We store the current junction's data in the junctions array
            // robotData.printJunction(); // test line to ensure data is being stored as expected
        }

        for (int i = 0; i < 4; i++) {
            if (robot.look(IRobot.AHEAD + i) == IRobot.PASSAGE) {
                passageDirections.add(IRobot.AHEAD + i);
            }
            else if ((IRobot.AHEAD + i != IRobot.BEHIND) && (robot.look(IRobot.AHEAD + i) == IRobot.BEENBEFORE))  { // Note that we want to exclude going back as an option
                previousDirections.add(IRobot.AHEAD + i);
            }
        }
    
        // Multiple PASSAGE exits:
        if (passageDirections.size() > 1) {
            return randomDirection(robot, passageDirections);
        }
        // Single PASSAGE exit
        else if (passageDirections.size() == 1){
            return passageDirections.get(0);
        }
        // No PASSAGE exit
        else {
            explorerMode = 0; // If we reach here it means we should be backtracking
            return getBacktrackDirection(robot, robotData.headings.pop()); // When backtracking, pop off the last junction's heading            // Note that we add 6 instead of 4 to reverse the direction, as we want to go in the opposite direction to which we approached the junction from
        }
    }
}

class RobotDataNoCoord {
    Stack<Integer> headings = new Stack<>(); // To hold the initially arrived-from headings for each junction
}
