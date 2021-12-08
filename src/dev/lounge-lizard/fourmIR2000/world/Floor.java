package dev.lounge-lizard.fourmIR2000.world;

import java.io.Serializable;
import java.util.EnumSet;

import dev.lounge-lizard.fourmIR2000.insect.Insect;
import dev.lounge-lizard.fourmIR2000.pictures.Values;

/**
 * Floor element. Can contain an insect.
 */
public final class Floor implements Serializable {
    
    /** For serialization : version of the class */
    private final static long serialVersionUID = 1L;
    
    /** Array of insects in this element */
    private Insect insect;
    
    /** Type of the element (rock, food, antHill...) */
    private Values type;
    
    /** Type of the background (grass, desert, water...) */
    private Values backgroundType;
    
    /** Picture of the element (could be many transparent pictures) */
    private EnumSet<Values> pictures;
    
    /** Is this element crossable ? */
    private boolean isObstacle;
    
    /** Position of the floor on the map */
    private final WorldPoint p;
    
    /** Difficulty to pass on this tile */
    private int difficulty;
    
    
    /**
     *  Constructor. 
     * @param backgroundType type of the background (could not be null)
     * @param x				the X coord of the floor
     * @param y				the Y coord of the floor
     */
    public Floor(Values backgroundType, int x, int y) {
    	this(null, backgroundType, x, y);
    }
    
    
    /**
     *  Constructor specifying a background and an element  
     * @param type       	type of the element (could be null)
     * @param backgroundType type of the background (could not be null)
     * @param x				the X coord of the floor
     * @param y				the Y coord of the floor
     */
    public Floor(Values type, Values backgroundType, int x, int y) {
    	if (backgroundType == null)
    		throw new IllegalStateException();
        insect = null;
        this.type = type;
        this.backgroundType = backgroundType;
        isObstacle = Values.isObstacle(type);
        pictures = EnumSet.noneOf(Values.class);
        pictures.add(backgroundType);
        difficulty = Values.getDifficultyCrossing(backgroundType);
        p = new WorldPoint(x, y);
    }
    
    
    /**
	 * Get the type of the element
	 * @return  the type
	 */
    Values getType() {
        return type;
    }

    
    /**
	 * Assign a new type for the element (could be null)
	 * @param type  new type
	 */
    void setType(final Values type) {
        this.type = type;
        if (type != null)
        	isObstacle = Values.isObstacle(type) || Values.isObstacle(backgroundType);
        else
        	isObstacle = Values.isObstacle(backgroundType);
        pictures = EnumSet.noneOf(Values.class);
        pictures.add(backgroundType);
        if (type != null)
        	pictures.add(type);
    }


    /**
	 * Get the background type of the element
	 * @return  the background type
	 */
    Values getBackgroundType() {
        return backgroundType;
    }

    
    /**
     * Give the weight of the tile. This represents the difficulty we will have
     * to cross this tile
     * @return the weight
     */
    public int getWeight() {
    	return difficulty; 
    }
    
    
    /**
     * Give all the pictures of the floor (could be many transparent pictures)
     * @return the pictures
     */
    EnumSet<Values> getPictures() {
    	return pictures;
    }

    
    /**
     * Assign a new set of pictures to the current floor.
     * THIS MUST ONLY BE DONE IF YOU KNOW WHAT YOU ARE DOING.
     * To be secure, this method MUST NOT BE PUBLIC.
     * @param pictures the new pictures to assign
     */
    void setPictures(final EnumSet<Values> pictures) {
    	this.pictures = pictures;
    }
    
    
    /**
     * Indicate if the element is an obstacle or not
     * @return true if it is an obstacle
     */
    boolean isObstacle() {
        return isObstacle;
    }
    
    
    /**
     * Indicate if the element is an obstacle or not
     * @param isObstacle true if it is an obstacle
     */
    void setObstacle(final boolean isObstacle) {
        this.isObstacle = isObstacle;
    }
    
    
    /**
	 * Assign an insect to this element
	 * @param insect  insect to assign
	 */
    void setInsect(final Insect insect) {
        this.insect = insect;
    }
    
    
    /**
	 * Get the insect of this element
	 * @return  the insect, or null
	 */
    Insect getInsect() {
        return insect;
    }


    /**
     * Give the point of the floor
     * @return the WorldPoint
     */
    WorldPoint getPoint() {
        return p;
    }
}
