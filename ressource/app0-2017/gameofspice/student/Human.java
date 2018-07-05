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
            
            //setLocation (nextX (getX(), direction), nextY (getY(), direction));
            this.xSpies = nextX (getX(), direction);
            this.ySpies = nextY (getY(), direction);
            steps++;
           
	    if (containsWolf(getX(), getY())) 
	    {
                System.out.println ("[" + this.getClass().getName() + "] AAAAAaaaaaaahhhh un loup! *miam*. Vos agents viennent de se faire manger.");
		throw new SpiesAreDeadException();
	    }
        }
    }

    /*private void setLocation(int x, int y) {
    	this.xSpies = x;
	this.ySpies = y;
    }*/

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
    public final int getTargetX()
    {
        return this.xNed;
    }
    
    /**
     * Get the y-coordinate of Ned Stark
     */
    public final int getTargetY()
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
    
    public void contest01()
    {
        placeObjects (Status.FIR, new int[] {1, 1, 14, 10});
        this.placeSpies(14, 1);
	this.placeNed(1,10);
    }
    
    public void contest02()
    {
        placeObjects (Status.FIR, new int[] {13, 9, 11, 10, 9, 8, 7, 9, 7, 7, 4, 8, 3, 10, 1, 8, 4, 5, 1, 5, 3, 3, 4, 1, 7, 3, 9, 2, 11, 3, 13, 1, 14, 4, 12, 6, 9, 5});
        this.placeSpies(0, 5);
	this.placeNed(15,6);
    }
    
    public void contest03()
    {
        placeObjects (Status.FIR, new int[] {14, 8, 11, 10, 10, 8, 8, 10, 7, 7, 4, 8, 3, 10, 1, 8, 4, 5, 1, 5, 3, 3, 4, 1, 7, 3, 10, 1, 12, 3, 14, 1, 14, 5, 12, 6, 9, 5});
        this.placeSpies(15, 1);
	this.placeNed(0,8);
    }
    
    public void contest04()
    {
        placeObjects (Status.FIR, new int[] {2, 4, 2, 5, 2, 6, 2, 7, 2, 8, 5, 6, 6, 6, 5, 3, 6, 3, 7, 3, 8, 3, 7, 6, 9, 5, 9, 6, 9, 7, 11, 2, 11, 3, 11, 4, 14, 5, 14, 6, 14, 7, 14, 4, 9, 9, 13, 10, 5, 9, 3, 1});
        this.placeSpies(0, 6);
	this.placeNed(15,6);
    }
    
    public void contest05()
    {
        placeObjects (Status.FIR, new int[] {1, 3, 2, 3, 3, 3, 3, 3, 4, 3, 6, 1, 6, 2, 6, 3, 6, 4, 6, 5, 6, 6, 4, 8, 5, 8, 6, 8, 7, 8, 8, 8, 2, 10, 2, 9, 2, 8, 2, 8, 2, 7, 2, 6, 8, 6, 9, 6, 10, 6, 12, 6, 9, 4, 9, 3, 12, 4, 8, 1, 9, 1, 10, 1, 12, 1, 12, 2, 12, 3, 12, 5, 12, 7, 12, 8, 10, 10, 10, 10, 11, 10, 12, 10, 13, 10, 14, 10, 4, 10, 6, 10, 8, 10, 4, 1, 2, 1, 14, 6, 14, 7, 14, 2});
        this.placeSpies(14, 4);
	this.placeNed(9,5);
    }
    
    public void contest06()
    {
        placeObjects (Status.FIR, new int[] {4, 6, 5, 6, 6, 6, 7, 6, 9, 6, 10, 6, 11, 6, 12, 6, 13, 6, 8, 4, 8, 3, 8, 2, 8, 1, 8, 8, 8, 9, 8, 10, 3, 6});
        this.placeSpies(1, 6);
	this.placeNed(8,6);
    }
    
    public void contest07()
    {
        placeObjects (Status.FIR, new int[] {2, 6, 2, 5, 3, 5, 6, 4, 7, 4, 7, 3, 9, 6, 9, 7, 10, 7, 6, 8, 5, 7, 6, 7, 11, 2, 11, 3, 12, 3, 13, 7, 13, 8, 12, 8});
        this.placeSpies(0, 6);
	this.placeNed(12,2);
    }
    
    public void contest08()
    {
        placeObjects (Status.FIR, new int[] {13, 1, 13, 2, 14, 2, 13, 4, 12, 4, 12, 5, 10, 6, 9, 6, 9, 7, 6, 8, 7, 8, 7, 9, 5, 10, 4, 9, 4, 10, 3, 4, 2, 4, 3, 3, 5, 1, 5, 2, 6, 2, 2, 10, 1, 9, 2, 9, 3, 7, 2, 7, 2, 6, 8, 3, 8, 4, 8, 2, 8, 1, 10, 2, 13, 7, 12, 7, 12, 9, 14, 7, 10, 10, 6, 6, 6, 5, 6, 4, 1, 1, 1, 2, 14, 10});
        this.placeSpies(14, 1);
	this.placeNed(1,10);
    }
    
    public void contest09()
    {
        placeObjects (Status.FIR, new int[] {4, 0, 4, 1, 4, 2, 4, 3, 4, 4, 4, 5, 4, 6, 8, 3, 8, 4, 8, 5, 8, 6, 8, 7, 8, 8, 8, 9, 8, 10, 8, 11, 0, 9, 1, 9, 2, 9, 3, 9, 4, 9, 10, 4, 5, 9, 11, 4, 12, 4, 13, 4, 14, 4, 15, 4, 12, 8, 12, 9, 12, 10, 12, 11, 14, 8, 15, 8});
        this.placeSpies(1, 1);
	this.placeNed(15,6);
    }
    
    public void contest10()
    {
        placeObjects (Status.FIR, new int[] {2, 7, 6, 4, 2, 6, 3, 6, 6, 5, 6, 6, 6, 7, 6, 8, 7, 10, 6, 10, 4, 10, 5, 10, 3, 10, 2, 0, 2, 1, 2, 2, 4, 4, 4, 3, 4, 2, 8, 2, 8, 3, 9, 2, 11, 6, 12, 6, 12, 5, 10, 8, 11, 8, 12, 8, 13, 10, 14, 10, 14, 9, 14, 1, 13, 2, 14, 2});
        placeObjects (Status.WOLF, new int[] {2, 4, 1, 9, 4, 8, 6, 2, 8, 5, 8, 7, 10, 4, 11, 2, 14, 4, 14, 6, 11, 10, 9, 10});
        this.placeSpies(0, 6);
	this.placeNed(14,5);
    }
    
    public void contest11()
    {
        placeObjects (Status.FIR, new int[] {1, 5, 2, 5, 4, 5, 4, 5, 3, 5, 5, 5, 6, 5, 6, 6, 6, 7, 5, 7, 4, 7, 3, 7, 2, 7, 1, 7, 8, 5, 9, 5, 9, 4, 9, 3, 8, 3, 7, 3, 6, 3, 5, 3, 4, 3, 3, 3, 2, 3, 1, 3, 1, 9, 2, 9, 3, 9, 4, 9, 5, 9, 6, 9, 7, 9, 8, 9, 9, 9, 9, 8, 9, 7, 8, 7, 11, 5, 12, 5, 11, 6, 12, 7, 11, 7, 13, 7, 13, 5, 14, 5, 14, 7, 11, 3, 12, 3, 13, 3, 14, 3, 14, 2, 14, 1, 13, 1, 12, 1, 11, 1, 11, 9, 12, 9, 13, 9, 14, 9, 14, 10, 14, 11, 13, 11, 12, 11, 11, 11});
        this.placeSpies(0, 6);
	this.placeNed(12,6);
    }
    
    public void contest12()
    {
        placeObjects (Status.FIR, new int[] {1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1, 8, 1, 9, 4, 11, 4, 10, 4, 8, 4, 9, 4, 7, 4, 6, 4, 5, 4, 4, 4, 3, 3, 2, 4, 2, 5, 2, 6, 2, 8, 1, 8, 2, 8, 0, 8, 3, 8, 4, 8, 5, 7, 5, 5, 7, 6, 7, 7, 7, 8, 7, 10, 5, 10, 6, 10, 7, 9, 7, 11, 9, 9, 3, 10, 3, 11, 3, 12, 3, 12, 4, 12, 5, 12, 7, 12, 6, 12, 8, 12, 9, 10, 9, 9, 9, 6, 9, 7, 9, 8, 9, 10, 2, 10, 1, 12, 2, 12, 1, 14, 0, 14, 1, 13, 3, 14, 3, 14, 5, 15, 5, 13, 7, 14, 7, 15, 9, 14, 9, 11, 10, 9, 11, 7, 10, 14, 11});
        placeObjects (Status.WOLF, new int[] {4, 1, 1, 10});
        this.placeSpies(0, 6);
	this.placeNed(9,2);
    }
    
    public void contest13()
    {
        placeObjects (Status.FIR, new int[] {7, 7, 8, 7, 8, 6, 8, 6, 7, 5, 8, 5, 6, 5, 5, 5, 5, 6, 5, 7, 5, 8, 5, 9, 6, 9, 7, 9, 8, 9, 9, 9, 10, 9, 10, 8, 10, 7, 10, 6, 10, 5, 10, 4, 10, 3, 9, 3, 8, 3, 7, 3, 6, 3, 5, 3, 4, 3, 3, 3, 3, 4, 3, 5, 3, 6, 3, 7, 3, 8, 3, 9, 3, 10, 3, 11, 4, 11, 5, 11, 6, 11, 7, 11, 8, 11, 9, 11, 10, 11, 11, 11, 12, 11, 12, 10, 12, 9, 12, 8, 12, 7, 12, 6, 12, 5, 12, 4, 12, 3, 12, 2, 12, 1, 11, 1, 10, 1, 9, 1, 8, 1, 7, 1, 6, 1, 5, 1, 4, 1, 3, 1, 2, 1, 1, 1});
        placeObjects (Status.WOLF, new int[] {1, 3, 1, 5, 1, 7, 1, 9, 1, 11, 14, 1, 14, 3, 14, 5, 14, 7, 14, 9, 14, 11});
        this.placeSpies(7, 6);
	this.placeNed(1,6);
    }
    
    public void contest14()
    {
        placeObjects (Status.FIR, new int[] {1, 0, 1, 1, 0, 3, 1, 3, 2, 3, 3, 3, 3, 2, 3, 1, 5, 0, 5, 1, 5, 2, 5, 3, 5, 4, 5, 5, 4, 5, 3, 5, 2, 5, 1, 5, 1, 7, 3, 8, 2, 10, 1, 9, 0, 9, 6, 4, 7, 4, 8, 4, 8, 5, 8, 6, 7, 6, 7, 7, 6, 7, 5, 7, 5, 8, 5, 9, 4, 9, 1, 6, 3, 6, 1, 10, 0, 10, 0, 11, 1, 11, 2, 11, 3, 11, 4, 11, 5, 11, 6, 11, 7, 11, 7, 10, 7, 9, 8, 9, 9, 9, 9, 8, 10, 8, 9, 5, 10, 5, 10, 6, 11, 6, 12, 5, 12, 6, 12, 7, 12, 8, 11, 10, 12, 10, 12, 11, 9, 11, 14, 8, 14, 9, 14, 10, 14, 11, 14, 5, 15, 5, 12, 3, 13, 3, 14, 3, 11, 3, 10, 3, 7, 2, 8, 2, 8, 1, 10, 2, 10, 1, 12, 1, 12, 2, 14, 1, 14, 2});
        this.placeSpies(7, 1);
	this.placeNed(0,0);
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