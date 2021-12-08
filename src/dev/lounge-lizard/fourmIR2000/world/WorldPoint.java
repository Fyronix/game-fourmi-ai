package dev.lounge-lizard.fourmIR2000.world;

import java.io.Serializable;


/**
 * Create a non-mutable 2D Point. We didn't want to use the existing
 * 2D point for the following reasons :
 * - we don't want to implements all the methods,
 * - we want an object as little as possible
 * - we want methods as fast as possible 
 */
public final class WorldPoint implements Serializable {
    
    /** For serialization : version of the class */
    private static final long serialVersionUID = 1L;
    
    /** X coord */
    private final int x;
    
    /** Y coord */
    private final int y;


    /**
     * Constructor of the WorldPoint
     * @param x The X coord of the point
     * @param y The Y coord of the point
     */
    public WorldPoint (int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    
    /**
	 * Give the X coord of the point
	 * @return  the X coord.
	 */
    public int getX() {
        return x;
    }
    
    
    /**
	 * Give the Y coord of the point
	 * @return  the Y coord.
	 */
    public int getY() {
        return y;
    }
    
    
    /**
     * Indicate if 2 WorldPoint are the same
     * @param o the other object to compare with
     * @return true if the objects are equals
     */
    @Override public boolean equals(final Object o) {
        if (!(o instanceof WorldPoint))
            return false;
        final WorldPoint w = (WorldPoint)o;
        return x == w.x && y == w.y;
    }

    
    /**
     * This hashCode function comes from the 'java.awt.geom.Point2D.Double' class.
     * @return the hash code
     */
    @Override public int hashCode() {
        long bits = Double.doubleToLongBits(x);
        bits ^= java.lang.Double.doubleToLongBits(y) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }
    
    
    /**
     * For debug only : give a representation of the point in a String
     */
    @Override public String toString() {
        return "(" + x + "," + y + ")";
    }
    
    
    /**
     * Returns the distance between two points.
     * @param p the point to deal with
     * @return the distance between the two sets of specified coordinates.
     */
    public double distance(final WorldPoint p)  {
    	// METHOD 1 : real number of tiles (bad effect, but faster)
        /*double dX = Math.max(x, p.x) - Math.min(x, p.x);
        double dY = Math.max(y, p.y) - Math.min(y, p.y);
        double min = dX < dY ? dX : dY;
        double max = dX == min ? dY : dX;
        return max;
         */

    	// METHOD 2 : Hypothenuse (best method, a bit slow due to sqrt() calls )
        return Math.sqrt( (x - p.x) * (x - p.x)  +  (y - p.y) * (y - p.y));
    }
}
