package dev.lounge-lizard.fourmIR2000.world;

import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.util.Randomizer;


/**
 * Class that generate a world to use with World
 * You can make different kind of worlds, like plains, mountains, etc.
 */
final class RandomWorld {

    /** Matrix of Floor elements */
    private final Floor[][] tab;
    
    /** Size X of the game */
    private final int sizeX;
    
    /** Size Y of the game */
    private final int sizeY;
    
    /** AntHill of the player */
    private WorldPoint antHill;
    
    /** AntHill of the oponent */
    private WorldPoint antHillOpp;

    /** By default, we avoid antHills to be put near a border. This is the minimal distance from a border */
    private static final int DIST_BORDER = 5;
    
    
    
    /**
     * Create a new structure for the world.
     * to have a working world.
     * @param sizeX         size X of the world to build
     * @param sizeY         size Y of the world to build
     * @param percentDesert percent of desert tiles in the map
     * @param percentWater  percent of water tiles in the map
     * @param percentRocks  % of rocks to have in the map
     * @param percentFood   % of food to have in the map
     */
    public RandomWorld(int sizeX, int sizeY, int percentDesert, int percentWater, int percentRocks, int percentFood) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        tab = new Floor[sizeY][sizeX];
        antHill = null;
        antHillOpp = null;
        generateWorld(percentDesert, percentWater, percentRocks, percentFood);
    }
    
    
    /**
     * Generate a new world with some specificities
     * @param percentDesert percent of desert tiles in the map
     * @param percentWater  percent of water tiles in the map
     * @param percentRocks  percent of rocks in the map
     * @param percentFood   percent of food in the map
     */
    private void generateWorld(final int percentDesert, final int percentWater, int percentRocks, int percentFood) {
        int nb;
        WorldPoint p;
        final int nbTiles = sizeX * sizeY;

        // Error detections. If percentages are too bigs, we limit them !!
        if (percentRocks > 33) percentRocks = 33;
        if (percentFood > 33) percentFood = 33;
        
        final int nbDesert = (percentDesert * nbTiles) / 100;
        final int nbWater =  (percentWater * nbTiles) / 100;
        final int nbRocks =  (percentRocks * nbTiles) / 100;
        final int nbFood =   (percentFood * nbTiles) / 100;

        // What is the most important element ? Grass or desert ?
        Values defValue;
        Values whatNext;
        int qtyWhatNext;
        if (nbDesert > 50) {
        	defValue = Values.desert;
        	whatNext = Values.grass;
        	qtyWhatNext = nbTiles - nbDesert;
        } else {
        	defValue = Values.grass;
        	whatNext = Values.desert;
        	qtyWhatNext = nbDesert;
        }
        
        // We fill with this element
        for (int y=0; y<sizeY; y++)
            for (int x=0; x<sizeX; x++)
                tab[y][x] = new Floor(defValue, x, y);

        // And we builds some shapes of all the others elements.
        //The main element after the most important (grass or desert)
        createShapes(whatNext, qtyWhatNext);
        
        // We add water elements
        createShapes(Values.water, nbWater);
        
        // We add rocks
        for (nb=0; nb<nbRocks; nb++) {
            p = findEmptyTile();
            int x = p.getX();
            int y = p.getY();
            tab[y][x].setType(Values.rock);
        }

        // We add food
        for (nb = 0; nb < nbFood; nb++) {
            p = findEmptyTile();
            int x = p.getX();
            int y = p.getY();
            tab[y][x].setType(Values.food);
        }
        
        // We add the player's antHill
        antHill = findEmptyTileNotInBorder();
        tab[antHill.getY()][antHill.getX()].setType(Values.antHill);
        antHillOpp = findEmptyTileNotInBorder();
        tab[antHillOpp.getY()][antHillOpp.getX()].setType(Values.antHill_enemy);
   
        // And finally we blur all the map
        WorldBlur.blurMap(tab);
    }
    
    
    /**
     * Find a random empty tile to put an object on it (antHill, food, rock...)
     * @return the Point of the tile
     */
    private WorldPoint findEmptyTile() {
    	int x;
    	int y;
    	WorldPoint p;
        do {
        	p = findRandomTile();
            x = p.getX();
            y = p.getY();
    	} while (tab[y][x].getType() != null || tab[y][x].getBackgroundType() == Values.water);        
        return tab[y][x].getPoint();
    }

    
    /**
     * Find a random tile on the world
     * @return the chosen point
     */
    private WorldPoint findRandomTile() {
    	final int x = Randomizer.getInt(sizeX);
    	final int y = Randomizer.getInt(sizeY);
        return tab[y][x].getPoint();
    }
    
    
    /**
     * Find a random empty tile, but not near a border of the game
     * @return the point of the tile
     */
    private WorldPoint findEmptyTileNotInBorder() {
    	// Variables
    	WorldPoint p = null;
    	boolean found = false;
    	
    	// If the map is too smal, we admit points that don't fit the default space
    	final int limitX = sizeX < 2 * DIST_BORDER
    		? sizeX
    		: DIST_BORDER;
    	final int limitY = sizeY < 2 * DIST_BORDER
			? sizeY
	    	: DIST_BORDER;
    	while (! found) {
    		p = findEmptyTile();
    		if ( p.getX() >= limitX && p.getX() <= sizeX - limitX
    		  && p.getY() >= limitY && p.getY() <= sizeY - limitY)
    			found = true;
    	}
    	return p;
    }
    
    
    
    /**
     * Create random shapes of 'value' elements on the map, to a maximum of 'nbTiles tiles
     * @param value		the element to fill with
     * @param nbTiles	number of tiles to fill
     */
    private void createShapes(final Values value, int nbTiles) {
	    int curNb = 0;
	    while (nbTiles > 0) {
	    	
	    	if (nbTiles - 1 <= 0)
	    		curNb = 1;
	    	else
	    		curNb = 1 + Randomizer.getInt(nbTiles - 1);
	    	nbTiles -= curNb - createRandomShape(value, curNb);
	    }
    }
    
    
    /**
     * Create a random shape of 'backElem' elements, with a maximum of 'maxTiles' tiles.
     * If not all tiles are used, the quantity left is returned.
     * @param backElem	the element to fill with
     * @param maxTiles  the maximal number of tiles that we can add	
     * @return the number of left tiles
     */
    private int createRandomShape(final Values backElem, final int maxTiles) {
    	// We don't create shape if there is no need
    	if (maxTiles <= 0)
    		return 0;
    	
    	// Initialization
    	int nbTiles = 0;
    	final int ratio = new Double(Math.sqrt(maxTiles)).intValue();
    	int width = Randomizer.getInt(ratio * 2);
    	if (width <= 1) width=2;
    	int height = maxTiles / width;
    	if (height <= 1) height = 2;
   
    	final int minX = Randomizer.getInt(sizeX);
    	final int minY = Randomizer.getInt(sizeY);
    	final int maxX = minX + width;
    	final int maxY = minY + height;
    	final int decalX = Randomizer.getInt(2) - 1;
    	int nbDecal = 0;
   
    	// We create the shape
    	boolean end = false;
    	int y = minY;
    	while (y <= maxY && !end) {
    		int curDecal = decalX * nbDecal++;
    		int x = minX + curDecal;
    		while (x <= maxX + curDecal && !end) {
    			if (nbTiles > maxTiles)
    				end = true;
    			else {
    				// We add the new element only if there isn't already the same
    				if (x >= 0 && x < sizeX && y >= 0 && y < sizeY)
    					if (tab[y][x].getBackgroundType() != backElem) {
    						tab[y][x] = new Floor(backElem, x, y);
    						nbTiles++;
    					}
    				x++;
    			}
    		}
    		y++;
    	}
    	return maxTiles - nbTiles;
    }
    
    
    /**
     * Give the matrix of Floor
     * @return the matrix
     */
    Floor[][] getWorld() {
        return tab;
    }
}
