// Code for Exercise 1 of Coursework 2, Part 1 - Laveen Chandnani (2004842)

/* Preamble for Exercise 1:
 * 
 * When storing the data for each junction, I decided to use an object for each junction as opposed to multiple arrays, since
 * this makes it easier to maintain the list of junctions the robot has visited (which is a list of junction objects in my implementation).
 * 
 * I chose not to have a method for passageExits, as I didn't think this was necessary beacuse the number of passage exits is given by the 
 * difference between the number of non-wall exits and the number of been before exits, which already have methods defined for them.
 * 
 * For the three controller methods, I chose to implement them as following;
 *  - deadEnd: I checked all four directions to find the direction which does not have a wall facing it, and returned this
 *  - corridoor: We know that for any corridoor, we have 3 options of directions to move in; ahead, left or right. Backwards is not an option
 *               since we have no reason to reverse in a corridoor.
 *  - junction: My approach here was to create 2 array lists:
 *              - passageDirections: To hold the directions which have not been visited before
 *              - previousDirections: To hold the directions which have been visited before
 *              From this I was then able to determine what type of junction the robot is in:
 *              - A junction with multiple passages => We choose randomly from these (using a randomDirection method I've implemented)
 *              - A junction with a single passage => We take this single passage
 *              - A junction with no passages => We backtrack to the previous junction 
 *              I used array lists as I wanted the sizes of the arrays to be dynamic, as this way I could then check the size of the passageDirections 
 *              array list to work out how many passages the current junction has 
 * 
 * The RobotData class I'd written holds the array of junction objects as an attribute, and when backtracking we use the searchJunction method, which will 
 * return to us the heading the robot needs to go in to return to the previous junction we approached the current junction from. 
 * 
 * There isn't a lot of repeated code since I have made sure to put code that I am using frequently into methods, but there are smaller blocks of code that
 * are repeated e.g. the exploreControl and backtrackControl methods share a similar purpose and so are coded similarly. However, this is justified since 
 * the lines that are repeated don't make sense to put into a method/function of their own.
 * 
 * In the worst case, we can be sure that the robot will always find it's target using the current specification, since the code will at most check every 
 * single path in the maze before arriving at the end goal. At most, the robot will have to go through every path in the maze before reaching the goal cell.
 * This means that all cells between a deadend and a junction/crossroads will be encountered twice, all junctions will be encountered twice, all crossroads 
 * will be encountered thrice, and all other remaining cells will be encountered once. The result of this calculation will give us the maximum number of steps 
 * that the robot may have to take before finding the target.
 */


import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.util.*;

public class Ex1 {
    private int pollRun = 0; // Incremented after each move
    private RobotData1 robotData; // Data store for junctions
    private int explorerMode = 1; // 1 = explore, 0 = backtrack

    public void controlRobot(IRobot robot) {
        if ((robot.getRuns() == 0) && (pollRun == 0)) { // Checking if we are currently on the first run of a new maze
            robotData = new RobotData1(); // Resets the data store
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
        robotData.resetJunctionCounter();
        explorerMode = 1;
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
            robotData.junctions[robotData.junctionCounter] = new JunctionRecorder1(robot); // We store the current junction's data in the junctions array
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
        else if (passageDirections.size() == 1){
            return passageDirections.get(0);
        }
        // No PASSAGE exit
        else {
            explorerMode = 0; // If we reach here it means we should be backtracking
            return IRobot.AHEAD + (robotData.searchJunction(robot.getLocation().x, robot.getLocation().y) - robot.getHeading() + 6) % 4;
            // Note that we add 6 instead of 4 to reverse the direction, as we want to go in the opposite direction to which we approached the junction from
        }
    }

    // Note that we do not implement a crossroads method since it would effectively be doing the same thing as the junction method

}

class RobotData1 {
    int junctionCounter = 0; // No. of junctions currently being stored in the array
    int maxJunctions = 10000; // Max number of junctions likely to occur
    JunctionRecorder1[] junctions = new JunctionRecorder1[maxJunctions];

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

    public void printJunction() { // Test method to verify that the code is working as intended
        String temp;
        temp = "Junction " + (junctionCounter + 1) + "(x=" + junctions[junctionCounter].x + ", y=" + junctions[junctionCounter].y + ") heading " + junctions[junctionCounter].arrived;
        System.out.println(temp);
    }
}

class JunctionRecorder1 { // This class will be used to create an object for each junction to store data about it.
    // Attributes
    int x;
    int y;
    int arrived;

    public JunctionRecorder1(IRobot robot) {
        x = robot.getLocation().x;
        y = robot.getLocation().y;
        arrived = robot.getHeading();
    }
}