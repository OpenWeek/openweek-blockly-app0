package student;
// Human.java

import static java.lang.System.out;

import java.util.List;
import java.awt.Color;

/**
 * A Human which can move in a squared grid, oriented along the cardinal
 * points (NORTH, SOUTH, EAST, WEST). At each step, the Spies can turn on
 * itself, move of one cell in the current direction, dig in the ground or
 * pick up the gem. He knows his coordinates and those of Ned Stark.
 * 
 * @author Fabien Duchene (UCLouvain)
 * @version September 20, 2017
 */
public class Human
{
    // Enumeration to represents the direction
    public enum Direction { NORTH, EAST, SOUTH, WEST };
    // Status 
    private enum Status { EMPTY, FIR, WOLF };
    
    // Constants
    private static final int LIVE = 150;
    
    // Instance variables
    private Direction direction;
    private boolean foundNed;
    private int counter;
    private int steps;
    private Status[][] world;
    private int xNed, yNed, xSpies, ySpies;

    /**
     * Constructor
     */
    public Human()
    {
        setDirection (Direction.EAST);
        foundNed = false;
        counter = 0;
	this.world = new Status[16][12];
	for (int i= 0; i < 16; i++) {
		for (int j=0; j < 12; j++) {
			this.world[i][j] = Status.EMPTY;
		}
	}
    }
    
    /**
     * Human behaviour. Should be filled in extending classes
     */
    protected void behave(){}
    
    /**
     * Move of one cell in the current direction.
     * - If there is a Fir, don't move
     * - If there is a Wolf, die
     * Duration: one cycle
     */
    public final void move()
    {
        if (canMove() && isAlive())
        {
            if (steps == LIVE)
            {
                System.out.println ("[" + this.getClass().getName() + "] Trop tard, l'hiver est arrivé...");
		throw new SpiesAreDeadException();
            }
            
            setLocation (nextX (getX(), direction), nextY (getY(), direction));
            steps++;
           
	    if (containsWolf(getX(), getY())) 
	    {
                System.out.println ("[" + this.getClass().getName() + "] AAAAAaaaaaaahhhh un loup! *miam*. Vos agents viennent de se faire manger.");
		throw new SpiesAreDeadException();
	    }
        }
    }

    private void setLocation(int x, int y) {
    	this.xSpies = x;
	this.ySpies = y;
    }

    /**
     * Test whether there is a Wolf in front of the Human, in the current direction
     */
    public final boolean isInFrontOfWolf()
    {
        return containsWolf(nextX (getX(), direction),nextY (getY(), direction));
    }

    public final boolean containsWolf(int x, int y) {
    	return this.world[x][y] == Status.WOLF;
    }

    public final boolean containsFir(int x, int y) {
    	return this.world[x][y] == Status.FIR;
    }

    /**
     * Test is the Human can move, that is if there is no Fir in front
     * of him in the current direction, or if the Human is not against
     * the border of the world
     */
    public final boolean canMove()
    {
        if (! isAlive())
        {
            return false;
        }
        
        int x = nextX (getX(), direction);
        int y = nextY (getY(), direction);

        if (x >= world.length || y >= world[0].length || x < 0 || y < 0)
        {
            return false;
        }
	
        return !containsFir(x,y);
    }
    
    /**
     * Turn towards the left
     * Duration: one cycle
     */
    public final void turnLeft()
    {
        if (! isAlive())
        {
            return;
        }
        
        setDirection (left (direction));
    }
    
    /**
     * Turn towards the right
     * Duration: one cycle
     */
    public final void turnRight()
    {
        if (! isAlive())
        {
            return;
        }
        
        setDirection (right (direction));
    }
    
    /**
     * Set the direction of the Human
     */
    private void setDirection (Direction direction)
    {
        this.direction = direction;
    }
    
    /**
     * Get the current direction
     */
    public final Direction getDirection()
    {
        return direction;
    }
    
    /**
     * Get the x-coordinate of Ned Stark
     */
    private final int getTargetX()
    {
        return this.xNed;
    }
    
    /**
     * Get the y-coordinate of Ned Stark
     */
    private final int getTargetY()
    {
        return this.yNed;
    }
    
    /**
     * Test if the Human is on Ned Stark
     */
    public final boolean isOnTarget()
    {
        return (getX() == getTargetX() && getY() == getTargetY());
    }
     public int getSteps() {
         return this.steps;
     }
    
    /**
     * Spy on target. Prints information about the situation
     * Duration: one cycle
     */
    public final void spyOnTarget()
    {
        boolean found = false;
        if (! foundNed && isOnTarget())
        {
            printStats();
            found = true;
            foundNed = true;
        }

        System.out.println ("[" + this.getClass().getName() + "] " + (found ? "Cible localisée, espionnage en cours..." : "Le centre souhaiterait savoir pourquoi vous espionnez de la neige..."));
    }
    
    /**
     * Get the number of cycles since the beginning
     */
    private int getCounter()
    {
        return counter;
    }
    
    public boolean getFoundNed() {
        return this.foundNed;
    }
    /**
     * Print stats about the Human
     */
    public void printStats()
    {
        out.println ("[" + this.getClass().getName() + "] Nombre de pas : " + steps + ", Temps ecoule : " + getCounter());
    }
    
    /*
     * Auxiliary methods to do computation on direction
     */

    // get the direction on the right of the specified one
    private Direction right (Direction dir)
    {
        switch (dir)
        {
            case EAST: return Direction.SOUTH;
            case WEST: return Direction.NORTH;
            case NORTH: return Direction.EAST;
            case SOUTH: return Direction.WEST;
        }
        
        assert false;
        return null;
    }

    // get the direction on the left of the specified one
    private Direction left (Direction dir)
    {
        switch (dir)
        {
            case EAST: return Direction.NORTH;
            case WEST: return Direction.SOUTH;
            case NORTH: return Direction.WEST;
            case SOUTH: return Direction.EAST;
        }
        
        assert false;
        return null;
    }

    // get x shifted of one cell long specified direction
    private int nextX (int x, Direction dir)
    {
        switch (dir)
        {
            case EAST: return x + 1;
            case WEST: return x - 1;
            case NORTH:
            case SOUTH: return x;
        }
        
        assert false;
        return -1;
    }

    // get y shifted of one cell along specified direction
    private int nextY (int y, Direction dir)
    {
        switch (dir)
        {
            case SOUTH: return y + 1;
            case NORTH: return y - 1;
            case EAST:
            case WEST: return y;
        }
        
        assert false;
        return -1;
    }

    private boolean isAlive() {
    	return true;
    }

    public int getX() {
    	return this.xSpies;
    }

    public int getY() {
    	return this.ySpies;
    }
    
    public void buildScenario()
    {
        placeObjects (Status.FIR, new int[] {4, 5, 3, 8, 7, 9, 8, 6, 10, 4, 12, 8, 14, 10, 7, 3, 3, 2, 1, 10, 11, 2, 14, 1, 13, 5, 2, 6});
        this.placeSpies(0, 8);
        this.placeNed(9,2);
    }
    private void placeSpies(int x, int y) {
    	this.xSpies = x;
	this.ySpies = y;
    }

    private void placeNed(int x, int y) {
    	this.xNed = x;
	this.yNed = y;
    }

    // Methods to create scenarii
    private void placeObjects (Status objectType, int[] coords)
    {
        for (int i = 0; i < coords.length - 1; i += 2)
        {
            try
            {
	    	this.world[coords[i]][coords[i + 1]] = objectType;
            }
            catch (Exception exception){
	    	System.out.println("OULA");
	    }

        }
    }
}