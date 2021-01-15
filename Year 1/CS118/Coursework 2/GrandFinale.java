// Code for Exercise 3 of Coursework 2, Part 1 - Laveen Chandnani (2004842)

/* Preamble for Grand Finale:
 * 
 * I decided to modify my previous approach from Exercise 3, as I felt that it would be easier to just remove the junctions which were backtracked on to obtain a path that 
 * is shorter and reached the target quicker, since it is not possible to make progress on a backtracked junction by this design. 
 * 
 * Since Exercise 3 worked on loopy mazes, and my Grand Finale is an extension of my Exercise 3, I know that it will work on loopy mazes. It is able to deal with repeat runs of 
 * the same maze as well as new mazes, since Tremaux's algorithm works on any maze. Although I was not able to fully extend the code to solve the problem of collisions, 
 * which I was unable to work out, the code is able to shorten it's steps and time taken to reach a target compared to it's initial run.
 */

import uk.ac.warwick.dcs.maze.logic.IRobot;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GrandFinale {
    private int pollRun = 0; // Incremented after each move
    private RobotData3 robotData; // Data store for junctions
    private int explorerMode = 1; // 1 = explore, 0 = backtrack
    private ArrayList<Integer> junctionHeadings = new ArrayList<Integer>(); // To hold all of the junction headings to send the robot towards when we approach a junction.
    private int junctionNo = 0; // Holds the index of the junction we are currently on

    public void controlRobot(IRobot robot) {
        int direction;
        int nonwallExits;

        if ((robot.getRuns() == 0) && (pollRun == 0)) { // Checking if we are currently on the first run of a new maze
            robotData = new RobotData3(); // Resets the data store
            explorerMode = 1;
        }

        nonwallExits = nonwallExits(robot); 

        if (nonwallExits == 1) { // Dead end case
            if (robotData.numOfJunctions != 0) {
                explorerMode = 0; // We backtrack since we are in a deadend that has passed a junction
            }
            direction = deadEnd(robot); 
        }
        else if (nonwallExits == 2) { // Corridoor case
            direction = corridoor(robot);
        }
        else { // Junction and crossroads case
            if (robot.getRuns() == 0) {
                direction = junction(robot);
                robotData.printRoute(robot);
            }
            else {
                for (int i = 0; i < robotData.numOfJunctions; i++) {
                    if (robotData.junctions[i].state == 1) { // If the junction was not a dead end, we add it to the route of junction headings to take
                        junctionHeadings.add(robotData.junctions[i].entered);
                    }
                }
                direction = junction2(robot); // An alternative method to go through the arrayList of junction headings to take
                junctionNo++;
            }
        }
        //robotData.printRoute(robot);
        robot.face(direction);
        pollRun++; // Increment pollRun so that the data is not reset each time the robot moves
    }

    public void reset() {
        robotData.resetJunction();
        explorerMode = 1;
    }

    // Method to choose a random direction, given an ArrayList of absolute directions
    private int randomDirection(ArrayList<Integer> directions) { 
		int direction;
        int randno;
        
        // Selecting a random number
        randno = (int) Math.floor(Math.random()*directions.size());
        direction = directions.get(randno);

        return direction;
	}

    private int passageExits(IRobot robot) {
        return nonwallExits(robot) - beenbeforeExits(robot);
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

        for (int i = 0; i < 4; i++) {
            if (robot.look(IRobot.AHEAD + i) == IRobot.BEENBEFORE) {
                exits++;
            }
        }
        return exits;
    }

    private int getHeadingState(IRobot robot, int heading) { // Returns the state of the square in the given heading
        return robot.look(IRobot.AHEAD + (heading - robot.getHeading() + 4) % 4); 
    }

    private int getMarks(IRobot robot, JunctionRecorder3 junction, int heading) {
        int mark = 0;
        switch (heading) {
            case IRobot.NORTH:
                mark = junction.N;
                break;
            case IRobot.EAST:
                mark =  junction.E;
                break;
            case IRobot.SOUTH:
                mark = junction.S;
                break;
            case IRobot.WEST:
                mark = junction.W;
                break;
        }
        return mark;
    }

    private JunctionRecorder3 wallMarker(IRobot robot, JunctionRecorder3 junction) { // Used to mark the heading with a wall with a 3
        int squareState;
        for (int i = 0; i < 4; i++) { // Iterating through all headings
            squareState = getHeadingState(robot, IRobot.NORTH + i);
            if (squareState == IRobot.WALL) { // We assign a 3 to a heading if we find a square that is a wall at this junction
                switch (IRobot.NORTH + i) {
                    case IRobot.NORTH: 
                        junction.N = 3;
                        break;
                    case IRobot.EAST:
                        junction.E = 3; 
                        break;
                    case IRobot.SOUTH:
                        junction.S = 3;
                        break;
                    case IRobot.WEST:
                        junction.W = 3;
                        break;
                }
            }
        }
        return junction;
    }

    private JunctionRecorder3 initialMarker (IRobot robot, JunctionRecorder3 junction, int heading) { // Used to mark the arrived-from heading with a 2
        switch (heading) {
            case IRobot.NORTH:
                junction.N = 2;
                break;
            case IRobot.EAST:
                junction.E = 2;
                break;
            case IRobot.SOUTH:
                junction.S = 2;
                break;
            case IRobot.WEST:
                junction.W = 2;
                break;
        }
        return junction;
    }

    private JunctionRecorder3 junctionMarker(IRobot robot, JunctionRecorder3 junction, int heading) { // Similar to wallMarker, except this method will +1 to the heading given
        switch (heading) {
            case IRobot.NORTH:
                junction.N = 1;
                break;
            case IRobot.EAST:
                junction.E = 1;
                break;
            case IRobot.SOUTH:
                junction.S = 1;
                break;
            case IRobot.WEST:
                junction.W = 1;
                break;
        }
        return junction;
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
        HashMap<Integer, Integer> oppositeHeading = new HashMap<Integer, Integer>(); // Using a hash map since this is a convenient way to reverse directions
        oppositeHeading.put(IRobot.NORTH, IRobot.SOUTH);
        oppositeHeading.put(IRobot.EAST, IRobot.WEST);
        oppositeHeading.put(IRobot.SOUTH, IRobot.NORTH);
        oppositeHeading.put(IRobot.WEST, IRobot.EAST);
        
        int direction = 0; // To hold the direction the robot will be faced in
        int heading = 0; // The absolute direction chosen

        ArrayList<Integer> passageDirections = new ArrayList<Integer>();
        
        robotData.junctionIndex = robotData.searchJunction(robot.getLocation().x, robot.getLocation().y); // Obtaining the junction index for the current junction
        
        // We can either be exploring or backtracking
        if (explorerMode == 1) { // Exploring
            // We can either be going into a new junction, or an old junction
            if (robotData.junctionIndex == -1) { // New junction case
                /* Here we have to:
                 *  - Add the new junction to the junction list
                 *  - Mark the heading where the robot entered
                 *  - Mark all of the walls
                 *  - Choose the heading the robot will exit from and mark it
                 *  - Change the state of the junction
                 *  - Increment the total number of junctions
                 */
                robotData.junctionIndex = robotData.numOfJunctions;
                robotData.junctions[robotData.numOfJunctions] = new JunctionRecorder3(robot); // Creating a new junction object for this junction
                robotData.junctions[robotData.numOfJunctions] = initialMarker(robot, robotData.junctions[robotData.numOfJunctions], oppositeHeading.get(robot.getHeading())); // Marking the entered-from heading
                robotData.junctions[robotData.numOfJunctions] = wallMarker(robot, robotData.junctions[robotData.numOfJunctions]);

                for (int i = 0; i < 4; i++) {
                    if (robot.look(IRobot.AHEAD + i) == IRobot.PASSAGE) {
                        passageDirections.add(IRobot.AHEAD + i);
                    }
                }

                // Since I have used nonwallExits to identify junctions, but am using passageExits to calculate the number of paths leading out from a junction, it is possible for the robot to end up in a junction
                // with no passages going out from the junction. We need to account for this case, which is what the selection statements below do.
                if (passageDirections.size() != 0) {
                    direction = randomDirection(passageDirections); // Choosing the heading the robot will exit
                    heading = (((direction - IRobot.AHEAD) + robot.getHeading()) % IRobot.NORTH) + IRobot.NORTH;
                }
                else {
                    heading = oppositeHeading.get(robotData.junctions[robotData.numOfJunctions].entered);
                    direction = (IRobot.AHEAD + ((heading - robot.getHeading() + 4) % 4));
                    explorerMode = 0;
                    robotData.junctions[robotData.junctionIndex].state = 2; // Marking the junction as dead
                }
                robotData.junctions[robotData.numOfJunctions] = junctionMarker(robot, robotData.junctions[robotData.numOfJunctions], heading); // Marking the exit heading
                robotData.junctions[robotData.numOfJunctions].state = 1; // Marking the junction as an old junction
                robotData.numOfJunctions++; // Increment the total number of junctions in the array
            }
            else { // Old junction case
                /* Here we have to:
                 *  - Find the junction in the junction list
                 *  - Mark the heading we've currently approached the passage from as visited
                 *  - Reverse direction and change to backtracking
                 */
                robotData.junctions[robotData.junctionIndex] = junctionMarker(robot, robotData.junctions[robotData.junctionIndex], oppositeHeading.get(robot.getHeading())); // Marking the heading as visited
                direction = IRobot.BEHIND; // We reverse direction
                explorerMode = 0; // Changing to backtracking
            }
        }
        else { // Backtracking 
            // We can either be entering a junction with all visited passages, or some visited passsages
            if (nonwallExits(robot) - beenbeforeExits(robot) == 0) { 
                /* Here we have to: 
                 *  - Continue backtracking by taking the initial heading the robot entered the junction from
                 *  - Mark the junction as a dead junction 
                 */
                robotData.junctions[robotData.junctionIndex] = junctionMarker(robot, robotData.junctions[robotData.junctionIndex], oppositeHeading.get(robot.getHeading()));
                heading = oppositeHeading.get(robotData.junctions[robotData.junctionIndex].entered); // We take the opposite direction to which we initially entered the junction from
                direction = (IRobot.AHEAD + ((heading - robot.getHeading() + 4) % 4)); // Converting this into a relative direction
                robotData.junctions[robotData.junctionIndex].state = 2; // Marking the junction as dead
            }
            else {
                /* Here we have to:
                 *  - Select an unvisited passage
                 *  - Mark the entrance of this passage as visited
                 *  - Change to exploring
                 */
                robotData.junctions[robotData.junctionIndex] = junctionMarker(robot, robotData.junctions[robotData.junctionIndex], oppositeHeading.get(robot.getHeading()));
                for (int i = 0; i < 4; i++) {
                    if (robot.look(IRobot.AHEAD + i) == IRobot.PASSAGE) {
                        passageDirections.add(IRobot.AHEAD + i);
                    }
                }
                
                if (passageDirections.size() != 0) {
                    direction = randomDirection(passageDirections); // Choosing the heading the robot will exit
                    heading = (((direction - IRobot.AHEAD) + robot.getHeading()) % IRobot.NORTH) + IRobot.NORTH;
                    explorerMode = 1; // Changing to exploring
                }
                else {
                    heading = oppositeHeading.get(robotData.junctions[robotData.numOfJunctions].entered);
                    direction = (IRobot.AHEAD + ((heading - robot.getHeading() + 4) % 4));
                    robotData.junctions[robotData.junctionIndex].state = 2; // Marking the junction as dead
                }
                
                robotData.junctions[robotData.junctionIndex] = junctionMarker(robot, robotData.junctions[robotData.junctionIndex], heading); // Marking this entrance as visited
            }
        }
        return direction;
    }

    // Note that we do not implement a crossroads method since it would effectively be doing the same thing as the junction method

    private int junction2(IRobot robot) { // Method to be used for the second run and beyond
        return IRobot.AHEAD + ((junctionHeadings.get(junctionNo + 1) - robot.getHeading() + 4) % 4); // Since we want to go in the direction of arrived-from for the next junction as we leave the first junction
    }

}

class RobotData3 { // Class to hold data for each junction the robot passes through
    int junctionIndex = 0; // Pointer to the junction currently being accessed
    int numOfJunctions = 0; // Number of junctions currently being stored in the array
    int maxJunctions = 10000000; // Max number of junctions likely to occur
    JunctionRecorder3[] junctions = new JunctionRecorder3[maxJunctions];

    public void resetJunction() {
        junctionIndex = 0;

    }

    public void printRoute(IRobot robot) {
        for (int i = 0; i < numOfJunctions; i++) {
            junctions[i].printJunction(robot);
        }
        System.out.println("");
    }

    public int searchJunction(int x, int y) { // Returns the index the junction is stored at in the list
        for (int i = 0; i < numOfJunctions; i++) {
            if ((junctions[i].x == x) && (junctions[i].y == y)) {
                return i;
            }
        }
        return -1;
    }
}

class JunctionRecorder3 { // Class to create an object for each junction to store data about it
    // We want to hold data for each junction about where we arrived from, whether it has been marked, and what the mark count is on its exits
    int state; // 0 = Unvisited (new junction), 1 = Visited (old junction), 2 = Dead junction (All headings are greater than 0)
    int x;
    int y;
    int entered;
    int exited;
    // For N, E, S, W: 0 = Unvisited, 1 = Visited, 2 = Initially visited
    int N;
    int E;
    int S;
    int W;

    public JunctionRecorder3(IRobot robot) {
        state = 1;
        x = robot.getLocation().x;
        y = robot.getLocation().y;
        entered = robot.getHeading();
        exited = 0;
        N = 0;
        E = 0;
        S = 0;
        W = 0;
    }

    public boolean checkVisited(IRobot robot) {
        if (N > 0) {
            if (E > 0) {
                if (S > 0) {
                    if (W > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void printJunction(IRobot robot) {
        String text;
        text = "x=" + x + ", y=" + y + ", N=" + N + ", E=" + E + ", S=" + S + ", W=" + W + ", state=" + state + ", entered=" + entered;
        System.out.println(text);
    }

}