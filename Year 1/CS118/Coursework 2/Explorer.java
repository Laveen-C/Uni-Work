// Code for Exercise 1 of Coursework 2, Part 1 - Laveen Chandnani (2004842)


import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.util.*;

public class Explorer {
    private int pollRun = 0; // Incremented after each move
    private RobotData robotData; // Data store for junctions
    private int explorerMode = 1; // 1 = explore, 0 = backtrack

    public void controlRobot(IRobot robot) {
        if ((robot.getRuns() == 0) && (pollRun == 0)) { // Checking if we are currently on the first run of a new maze
            robotData = new RobotData(); // Resets the data store
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
        // Note that we only go into backtracking mode when we reach a deadend or a junction with beenBefore exits >= 2
        int exits;
        int direction = 0;

        exits = nonwallExits(robot);

        switch (exits) {
            case 1:
                direction = deadEnd(robot);
                explorerMode = 0; // We want to backtrack when we are at a dead end
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
            if ((nonwallExits - beenbeforeExits(robot) > 0) { // Meaning we have passage exits
                explorerMode = 1;
                direction = junction(robot); // Get the direction to proceed in
            }
            else { // We need to backtrack through this junction
                heading = robotData.searchJunction(robot.getLocation().x, robot.getLocation().y); // Heading we initially approached this junction from
                direction = IRobot.AHEAD + (heading - robot.getHeading() + 4) % 4; // Formula to calculate the relative direction the robot must go in
            }
        }
        robot.face(direction);
        pollRun++; // Increment pollRun so that the data is not reset each time the robot moves
    }

    public void reset() {
        robotData.resetJunctionCounter();
        explorerMode = 1;
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
            robotData.junctions[robotData.junctionCounter] = new JunctionRecorder(robot); // We store the current junction's data in the junctions array
            // robotData.printJunction(); // test line to ensure data is being stored as expected
            robotData.junctionCounter++; // Increment the junction counter since we have found a new junction
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
        else if (passageDirections.size() == 1) {
            return passageDirections.get(0);
        }
        // No PASSAGE exits
        else { // We need to backtrack through this junction 
            System.out.println("OOPSIE"); 
        }

    }

    private int crossroads(IRobot robot) {
        /* Here we can reuse our approach from the junction method above, as the above method will work for a junction with 4 exits i.e. a crossroads */
        return junction(robot);
    }
}

class RobotData {
    int junctionCounter = 0; // No. of junctions currently being stored in the array
    int maxJunctions = 10000; // Max number of junctions likely to occur
    JunctionRecorder[] junctions = new JunctionRecorder[maxJunctions];

    public void resetJunctionCounter() {
        junctionCounter = 0;
    }

    public int searchJunction(int x, int y) { // Returns the robot's heading when first approaching the junction at (x, y)
        for (int i = 0; i <= junctionCounter; i++) {
            if ((junctions[i].x == x) && (junctions[i].y == y)) {
                return junctions[i].arrived;
            }
        }
        // If we reach this block of code, it means we have reached a junction which we have not previously encountered 
        return IRobot.CENTRE; // We take this to be our 'null' value to indicate that we must return to explorer mode 
    }

    public void printJunction() {
        String temp;
        temp = "Junction " + (junctionCounter + 1) + "(x=" + junctions[junctionCounter].x + ", y=" + junctions[junctionCounter].y + ") heading " + junctions[junctionCounter].arrived;
        System.out.println(temp);
    }
}

class JunctionRecorder { // This class will be used to create an object for each junction to store data about it.
    // Attributes
    int x;
    int y;
    int arrived;

    public JunctionRecorder(IRobot robot) {
        x = robot.getLocation().x;
        y = robot.getLocation().y;
        arrived = robot.getHeading();
    }
}