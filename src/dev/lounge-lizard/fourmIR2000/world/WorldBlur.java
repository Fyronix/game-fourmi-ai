package dev.lounge-lizard.fourmIR2000.world;

import java.util.EnumSet;

import dev.lounge-lizard.fourmIR2000.pictures.Values;

public final class WorldBlur {

	private WorldBlur() {
		/* nothing  */
	}
	
	/**
     * Blur the given map : this change the backgrounds pictures to have a nicer graphical view
     * @param tab    the array of all floor elements
     */
	static void blurMap(final Floor[][] tab) {
		if (tab == null)
			return;
		final int sizeX = tab[0].length;
		final int sizeY = tab.length;
		for (int y=0; y<sizeY; y++)
            for (int x=0; x<sizeX; x++)
            	blurFloor(tab, tab[y][x]);
	}

	
	/**
	 * Blur the adjacents floors. This is typically used when a Floor element has changed, and
	 * we have to update the map so that elements around have their new pictures updated
	 * @param tab    the array of all floor elements
	 * @param middle the center floor element to use
	 */
	static void blurAdjacentFloors(final Floor[][] tab, final Floor middle) {
		if (middle == null)
			return;
		// Variables
		final int sizeX = tab[0].length;
		final int sizeY = tab.length;
		final int x = middle.getPoint().getX();
		final int y = middle.getPoint().getY();
		final Floor top =    isValidCoords(sizeX, sizeY, x,  y-1) ? tab[y-1][x] : null;
		final Floor bottom = isValidCoords(sizeX, sizeY, x,  y+1) ? tab[y+1][x] : null;
		final Floor left =   isValidCoords(sizeX, sizeY, x-1,  y) ? tab[y][x-1] : null;
		final Floor right =  isValidCoords(sizeX, sizeY, x+1,  y) ? tab[y][x+1] : null;
		blurFloor(tab, top);
		blurFloor(tab, bottom);
		blurFloor(tab, left);
		blurFloor(tab, right);
	}

	
	/**
	 * Give the 'background value' elements of Floor elements around a specific Floor
	 * @param tab	 the array of all floor elements
	 * @param floor  the central floor element ot use
	 * @return an array of Values : 0=top, 1=bottom, 2=left, 3=right
	 */
	private static Values[] giveAdjacentValues(final Floor[][] tab, final Floor floor) {
		if (tab == null)
			return null;
		final int sizeX = tab[0].length;
		final int sizeY = tab.length;
		final int x = floor.getPoint().getX();
		final int y = floor.getPoint().getY();
		return new Values[] {
			isValidCoords(sizeX, sizeY, x,  y-1) ? tab[y-1][x].getBackgroundType() : null,
			isValidCoords(sizeX, sizeY, x,  y+1) ? tab[y+1][x].getBackgroundType() : null,
			isValidCoords(sizeX, sizeY, x-1,  y) ? tab[y][x-1].getBackgroundType() : null,
			isValidCoords(sizeX, sizeY, x+1,  y) ? tab[y][x+1].getBackgroundType() : null
		};
	}
	
	
    /**
     * Adapt the tile to elements around (ie put a border if the tile near us is desert or grass 
     * and if we are an herb tile)
     * @param tab		array of floors elements to check
     * @param middle	the Floor element to blur
     */
    static void blurFloor(final Floor[][] tab, final Floor middle) {
    	if (middle == null)
    		return;
    	
    	// Initialization
    	final int x = middle.getPoint().getX();
    	final int y = middle.getPoint().getY();
		final Values[] adjacent = giveAdjacentValues(tab, tab[y][x]);
    	Values cur = Values.grass;
    	Values t = adjacent[0];
    	Values b = adjacent[1];
    	Values l = adjacent[2];
    	Values r = adjacent[3];
    	final Values desert = Values.desert;
    	final Values water = Values.water;
    	final Values backgroundType = middle.getBackgroundType();
    	final Values type = middle.getType();
    	final EnumSet<Values> resu = EnumSet.noneOf(Values.class);
    	
    	// If elements are nulls, we put the current element instead
    	if (t == null) t = backgroundType;
    	if (b == null) b = backgroundType;
    	if (l == null) l = backgroundType;
    	if (r == null) r = backgroundType;
    	
    	// Image detection (TODO : LOTS OF IFs... any better idea ?)
    	if (backgroundType == Values.grass) {
    		cur = Values.grass;
    		resu.add(Values.grass);
    	} else if (backgroundType == desert) {
    		cur = desert;
    		if      (t != desert && b == desert && l == desert && r == desert) cur = Values.desert_T;
    		else if (t != desert && b != desert && l == desert && r == desert) cur = Values.desert_TB;
    		else if (t != desert && b != desert && l != desert && r == desert) cur = Values.desert_TBL;
    		else if (t != desert && b != desert && l != desert && r != desert) cur = Values.desert_TBLR;
    		else if (t != desert && b != desert && l == desert && r != desert) cur = Values.desert_TBR;
    		else if (t != desert && b == desert && l != desert && r == desert) cur = Values.desert_TL;
    		else if (t != desert && b == desert && l != desert && r != desert) cur = Values.desert_TLR;
    		else if (t != desert && b == desert && l == desert && r != desert) cur = Values.desert_TR;
    		else if (t == desert && b != desert && l == desert && r == desert) cur = Values.desert_B;
    		else if (t == desert && b != desert && l != desert && r == desert) cur = Values.desert_BL;
    		else if (t == desert && b != desert && l != desert && r != desert) cur = Values.desert_BLR;
    		else if (t == desert && b != desert && l == desert && r != desert) cur = Values.desert_BR;
    		else if (t == desert && b == desert && l != desert && r == desert) cur = Values.desert_L;
    		else if (t == desert && b == desert && l != desert && r != desert) cur = Values.desert_LR;
    		else if (t == desert && b == desert && l == desert && r != desert) cur = Values.desert_R;
    	} else if (backgroundType == Values.water) {
    		cur = water;
    		if      (t != water && b == water && l == water && r == water) cur = Values.water_T;
    		else if (t != water && b != water && l == water && r == water) cur = Values.water_TB;
    		else if (t != water && b != water && l != water && r == water) cur = Values.water_TBL;
    		else if (t != water && b != water && l != water && r != water) cur = Values.water_TBLR;
    		else if (t != water && b != water && l == water && r != water) cur = Values.water_TBR;
    		else if (t != water && b == water && l != water && r == water) cur = Values.water_TL;
    		else if (t != water && b == water && l != water && r != water) cur = Values.water_TLR;
    		else if (t != water && b == water && l == water && r != water) cur = Values.water_TR;
    		else if (t == water && b != water && l == water && r == water) cur = Values.water_B;
    		else if (t == water && b != water && l != water && r == water) cur = Values.water_BL;
    		else if (t == water && b != water && l != water && r != water) cur = Values.water_BLR;
    		else if (t == water && b != water && l == water && r != water) cur = Values.water_BR;
    		else if (t == water && b == water && l != water && r == water) cur = Values.water_L;
    		else if (t == water && b == water && l != water && r != water) cur = Values.water_LR;
    		else if (t == water && b == water && l == water && r != water) cur = Values.water_R;
    	}
    	// We add a grass element if there is any transparent part
    	if (cur != Values.grass && cur != desert && cur != water)
    		resu.add(Values.grass);
   		resu.add(cur);
    	
    	// We add the object on the floor (if any)
    	if (type != null)
    		resu.add(type);

    	middle.setPictures(resu);
    }
    
    
    /**
     * Indicate if the current coords are in the map or not
     * @param sizeX : the maximal acceptable X value
     * @param sizeY : the maximal acceptable Y value
     * @param x the X coord
     * @param y the Y coord
     * @return true if the coords are on the map, else false
     */
    private static boolean isValidCoords(final int sizeX, final int sizeY, final int x, final int y) {
    	return x >= 0 && x < sizeX && y >= 0 && y < sizeY;
    }

}
