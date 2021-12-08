package dev.lounge-lizard.fourmIR2000.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import dev.lounge-lizard.fourmIR2000.Game;
import dev.lounge-lizard.fourmIR2000.insect.Insect;
import dev.lounge-lizard.fourmIR2000.insect.Team;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.lawrence.impl.DefaultGridModel;


/**
 * World to use with the game.
 * This is a sort of container of all objects that must be drawn on the JPanel.
 */
public final class World implements Serializable {
	
    /** For serialization : version of the class */
    private final static long serialVersionUID = 1L;
    
    /** Model used to draw pictures  BE CAREFUL : We don't want it to be serializable !!! */
    private transient DefaultGridModel<Values> model;

    /** Shortest path finding */
    private transient PathFinding path;
    
    /** Size X of the game */
    private int sizeX;
    
    /** Size Y of the game */
    private int sizeY;
    
    /** Matrix of Floor elements */
    private Floor tab[][];  
    
    /** Array of teams playing */
    private ArrayList<Team> tabTeam;
    
    /** Array of food present in the game */
    private ArrayList<WorldPoint> tabFood;

    /** The point of the map who has the focus */
    private WorldPoint selectedPoint;
    
    
    /**
     * Create a new World for playing
     * @param sizeX     Size X of the game (the real size is this size + size of the left menu)
     * @param sizeY     Size Y of the game
     */
    public World(int sizeX, int sizeY) {
        // Errors detection
        if (sizeX < 1) sizeX = 1;
        if (sizeY < 1) sizeY = 1;
        
        // Initialization of the variables
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        tabTeam = new ArrayList<Team>();
        tabFood = new ArrayList<WorldPoint>();
        path = null;
        selectedPoint = null;
        initializeGridModel();
    }
    

    /**
     * Give the GridModel used by the world
     * @return the DefaultGridModel to use in a GridPane
     * @see fr.umlv.lawrence.GridPane
     */
    public DefaultGridModel<Values> getModel() {
        return model;
    }

    
    /**
     * Generate an new world, and store the position of the two antHills
     * >> The first antHill is the one we play with
     * @param maxX          new X maximal coord
     * @param maxY          new Y maximal coord
     * @param percentDesert percent of desert tiles in the map
     * @param percentWater  percent of water tiles in the map
     * @param percentRocks  % of rocks to have in the map
     * @param percentFood   % of food to have in the map
     */
    public void generateNewWorld(int maxX, int maxY, int percentDesert, int percentWater, int percentRocks, int percentFood) {
        sizeX = maxX;
        sizeY = maxY;
        RandomWorld rndWorld = new RandomWorld(sizeX, sizeY, percentDesert, percentWater, percentRocks, percentFood);
        tab = rndWorld.getWorld();
        tabTeam.clear();
        path = new PathFinding(this);
        initializeGridModel();
        refresh();
    }
    
    
    /**
     * Initialize a new GridModel with the current 'Floor' elements 
     */
    private void initializeGridModel() {
        @SuppressWarnings("unchecked")
        Set<Values>[] array = (Set<Values>[]) new Set<?>[sizeX * sizeY];
        int i = 0;
        for (int y=0; y < sizeY; ++y)
            for (int x=0; x < sizeX; ++x) {
            	array[i]=EnumSet.noneOf(Values.class);
            	if (tab == null)
            		array[i].add(Values.grass);
            	else
            		array[i].addAll(tab[y][x].getPictures());
            	i++;
            }
        model = new DefaultGridModel<Values>(sizeY, sizeX, Arrays.asList(array));
    }
    
    
    /**
     * Store the food positions in an array. This way, all insects can find where is the nearest food.
     */
    public void storeFood() {
        tabFood.clear();
        for (int y = 0; y < sizeY; ++y)
            for (int x = 0; x < sizeX; ++x)
                if (tab[y][x].getType() == Values.food)
                    tabFood.add(tab[y][x].getPoint());
    }

    
    /**
     * Give the width of the map
     * @return the width of the map (+ the size of the menu)
     */
    public int getWidth() {
        return sizeX;
    }
    
    
    /**
     * Give the height of the map
     * @return the height of the map
     */
    public int getHeight() {
        return sizeY;
    }

    
    /**
     * Return the value of the element at this tile
     * @param x X position
     * @param y Y position
     * @return the value
     */
    public Values getElement(int x, int y) {
        if (isPositionAvailable(x, y))
            return tab[y][x].getType();
        return null;
    }

    
    /**
     * Return the value of the background element at this tile
     * @param x X position
     * @param y Y position
     * @return the value
     */
    public Values getBackgroundElement(int x, int y) {
        if (isPositionAvailable(x, y))
            return tab[y][x].getBackgroundType();
        return null;
    }
    
    
    /**
     * Assign an element for the backgroun of a specific tile
     * @param x         X posiiton
     * @param y         Y position
     * @param type value to assign
     * @param isDirect true to redraw immediately, false to wait for a refresh() call
     * @see #refresh()
     */
    public void setElement(int x, int y, Values type, boolean isDirect) {
        if (isPositionAvailable(x, y)) {
            // We update the ground element
            tab[y][x].setType(type);
            tab[y][x].setObstacle(Values.isObstacle(type));
            // And we inform the model that it has changed
            updateModelAndAdjacentTiles(x, y, isDirect);
        }
    }
    
    
    /**
     * Assign an element for the backgroun of a specific tile
     * @param floor		the floor element to update
     * @param isDirect true to redraw immediately, false to wait for a refresh() call
     * @see #refresh()
     */
    public void setElement(Floor floor, boolean isDirect) {
        int x = floor.getPoint().getX();
        int y = floor.getPoint().getY();
    	if (isPositionAvailable(x, y)) {
            // We update the ground element and the graphics of tiles around
            tab[y][x] = floor;
            WorldBlur.blurFloor(tab, floor);
            WorldBlur.blurAdjacentFloors(tab, floor);
            // And we inform the model that it has changed
            updateModelAndAdjacentTiles(x, y, isDirect);
     
        }
    }
    

    /**
     * Try to update the pictures of all adjacent tiles
     * @param x	the X coord of the main tile
     * @param y	the Y coord of the main tile
     * @param isDirect true to redraw immediately, false to wait for a refresh() call
     * @see #refresh()
     */
    private void updateModelAndAdjacentTiles(int x, int y, boolean isDirect) {
        updateModel(x, y, isDirect);
        if (isPositionAvailable(x-1, y)) updateModel(x-1, y, isDirect);
        if (isPositionAvailable(x+1, y)) updateModel(x+1, y, isDirect);
        if (isPositionAvailable(x, y-1)) updateModel(x, y-1, isDirect);
        if (isPositionAvailable(x, y+1)) updateModel(x, y+1, isDirect);
    }
    

    /**
     * Indicate if the specified position is crossable or not, without
     * regarding if there is an insect or not in the tile. 
     * @param p The position to check
     * @return true if it is crossable
     */
    private boolean isBackgroundCrossable(WorldPoint p) {
        return isPositionAvailable(p) && !tab[p.getY()][p.getX()].isObstacle();
    }

    
    /**
     * Indicate if the specified position is crossable or not, regarding
     * if there is an insect on it
     * @param p The position to check
     * @return true if it is crossable
     */
    public boolean isCrossable(WorldPoint p) {
        return isBackgroundCrossable(p) && tab[p.getY()][p.getX()].getInsect() == null;
    }


    /**
     * Indicate if the specified position is available for an insect or not
     * @param x     X position
     * @param y     Y position
     * @return true if the tile is free
     * @see #isPositionAvailable(WorldPoint)
     */
    public boolean isPositionAvailable(int x, int y) {
        return (x < 0 || y < 0 || x >= sizeX || y >= sizeY) ? false : true;
    }
    

    /**
     * Indicate if the specified position is available for an insect or not
     * @param p the point to check
     * @return true if the tile is free
     * @see #isPositionAvailable(int, int) 
     */
    boolean isPositionAvailable(final WorldPoint p) {
        return isPositionAvailable(p.getX(), p.getY());
    }
        
    
    /**
     * Tell us if we can move to the chosen destination.
     * Be careful: we can only move of one tile ! 
     * @param src   the source Point
     * @param dest  the destination Point
     * @return true if we can go there
     */
    public boolean canMoveHere(WorldPoint src, WorldPoint dest) {
        int destX = dest.getX();
        int destY = dest.getY();
        
        // Bad position ? We quit immediatly
        if (src.distance(dest) >= 2 || ! isPositionAvailable(dest) || tab[destY][destX].isObstacle())
            return false;
        /* We check for diagonals : we can't move in a diagonal if there are block all around.
         * ie. if we want to go Upper/Left, and if there are rocks on left and upper, we can't go. */
        int x = src.getX();
        int y = src.getY();
        final WorldPoint UpLeft =    getPoint(x - 1, y - 1);
        final WorldPoint Up =        getPoint(x,     y - 1);
        final WorldPoint UpRight =   getPoint(x + 1, y - 1);
        final WorldPoint Left =      getPoint(x - 1, y);
        final WorldPoint Right =     getPoint(x + 1, y);
        final WorldPoint DownLeft =  getPoint(x - 1, y + 1);
        final WorldPoint Down =      getPoint(x,     y + 1);
        final WorldPoint DownRight = getPoint(x + 1, y + 1);
        if (   (dest.equals(UpLeft)    && !isCrossable(Up)   && !isCrossable(Left))
            || (dest.equals(UpRight)   && !isCrossable(Up)   && !isCrossable(Right))
            || (dest.equals(DownLeft)  && !isCrossable(Down) && !isCrossable(Left))
            || (dest.equals(DownRight) && !isCrossable(Down) && !isCrossable(Right))
        )
            return false;
        return true;
    }
    
    
     /**
     * Give the list of all insects that are around a point
     * @param me        insect who calls the verification
     * @param radius    the radius to check around the insect
     * @param wantAFriend if true return our friends. If false, return the ennemies
     * @return true if exists or false;
     */
    public LinkedList<Insect> giveInsectAround(Insect me, int radius, boolean wantAFriend) {
        WorldPoint p = me.getPos();
        LinkedList<Insect> result= new LinkedList<Insect>();

        for(int y = p.getY() - radius; y <= p.getY() + radius; y++) {
            for (int x = p.getX() - radius; x <= p.getX() + radius; x++) {
                if (isPositionAvailable(x, y)) {
                    Floor floor = tab[y][x];
                    if (! floor.getPoint().equals(p)) {
                        Insect insect = floor.getInsect();
                        if (insect != null) {
                            boolean isOurTeam = insect.getTeam().equals(me.getTeam());
                            if ((isOurTeam && wantAFriend) || (!isOurTeam && !wantAFriend))
                                result.add(insect);
                        }
                    }
                }
            }
        }
        return result;
    }
    
    
    /**
     * Add a team of insects to the world. The first team would be the team we play with !
     * >> it works because we use an ArrayList
     * @param antHill    point of the antHill
     * @param color      color of the team
     * @param game       the gameassociated (to perform some special actions when an antQueen die for example)
     */
    public void addTeam(WorldPoint antHill, int color, Game game) {
        Team team = new Team(this, antHill, color, game);
        team.addMaster();
        tabTeam.add(team);
    }

    
    /**
     * Add an insect in the game
     * @param insect    new insect to add
     * @return          true if we can add the insect in the specified place
     */
    public boolean addInsect(Insect insect) {
        if (insect == null)
            return false;
        WorldPoint p = insect.getPos();
        int x = p.getX();
        int y = p.getY();
        if (tab[y][x].isObstacle())
            return false;
        /* If the current tile is an antHill, we could have many insects waiting for the tile
         * become free before appearing. Si if there is already an insect, we don't put the new
         * at this place. We do nothing. This new insect will apear on the map at his first move. 
         */
        if (tab[y][x].getInsect() == null) {
            tab[y][x].setInsect(insect);
            
            // We inform the model that there is an insect here
            updateModel(x, y, true);
        }
        return true;
    }
    
    
    /**
     * Move an insect to a tile to another
     * @param insect    insect to move
     * @param dest      point to fix the insect
     * @return true if the insect has moved (eg. if the destination is not an obstacle)
     */
    public boolean moveInsect(Insect insect, WorldPoint dest) {
        if (!isCrossable(dest) )
            return false;
        int x = insect.getPos().getX();
        int y = insect.getPos().getY();

        /* There is a possibility that more than one insect is on the current tile : 
         * if it is an antHill, and if new ants are waiting to appear. So before removing
         * the insect of the old tile, we check if it is the good one !
         */
        Insect test = tab[y][x].getInsect();
        if (test != null && test.equals(insect))
            tab[y][x].setInsect(null);
        tab[dest.getY()][dest.getX()].setInsect(insect);
        // We inform the grid that the 2 tiles have changed
        updateModel(x, y, false);
        updateModel(dest.getX(), dest.getY(), false);
        return true;
    }
    
    
    /**
     * Remove an insect from the floor
     * @param insect    insect to dereference
     */
    public void removeInsect(Insect insect) {
        WorldPoint curPos = insect.getPos();
        int x = curPos.getX();
        int y = curPos.getY();
        /* There is a possibility that more than one insect is on the current tile : 
         * if it is an antHill, and if new ants are waiting to appear. So before removing
         * the insect, we check if it is the good one !
         */
        Insect test = tab[y][x].getInsect();
        if (test != null && test.equals(insect)) {
            tab[curPos.getY()][curPos.getX()].setInsect(null);
            updateModel(x, y, false);
        }
    }
    
    
    /**
     * Return the list of the teams (the first is the one we play with)
     * @return the list of the teams
     */
    public List<Team> getTeams() {
        return tabTeam;
    }
    
    
    /**
     * Erase all the teams presents.
     */
    public void clearTeams() {
        tabTeam.clear();
    }
    
    
    /**
     * Update the elements presents in a specific tile
     * BE CAREFUL : this will store the result in a DoubleBuffered image. So changes 
     *              won't be visible until we call the refresh() method.
     * >> If you want an immediate change, use updateModelDirect()
     * @param x         the X coord of the tile to refresh
     * @param y         the Y coord of the tile to refresh
     * @param isDirect  if false, the change is store in a double buffered picture, 
     *                  and will be displayed only by a refresh() call
     * @see #refresh()
     */
    public void updateModel(int x, int y, boolean isDirect) {
        // Background
        EnumSet<Values> elems = EnumSet.noneOf(Values.class);
        elems.addAll(tab[y][x].getPictures());

        // Got an insect here ?
        Insect insect = tab[y][x].getInsect();  // Got insect here ?
        if (insect != null)
            elems.addAll(insect.getPictures());
        // Got a selection point here ?
        if (selectedPoint != null && selectedPoint.equals(getPoint(x, y)))
            elems.add(Values.select);
        
        // We update the graphic
        if (! isDirect)
            model.setDeffered(x, y, elems);
        else
            model.setDirect(x, y, elems);
    }
    

    /**
     * Update all the tiles'pictures of the game
     * @see #refresh()
     */
    public void updateAllModel() {
        for (int y = 0; y < sizeY; ++y)
            for (int x = 0; x < sizeX; ++x)
                updateModel(x, y, true);
        refresh();
    }
    
    
    /**
     * Force the GridPane to redraw everything
     */
    public void refresh() {
        model.swap();
    }
    
    
    /**
     * Return the PathFinding object, that permits to search for fastests paths
     * @return the PathFinding
     */
    public PathFinding getPathFinding() {
        return path;
    }
    

    /**
     * Save the current game in a file.
     * The method compresses the datas
     * @param file  file where we save
     */
    public void saveGame(String file) throws BadWorldFormatException  {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
            // Compress Serialize of the current object
            out.writeObject(new Integer(sizeX));
            out.writeObject(new Integer(sizeY));
            out.writeObject(tab);
            out.writeObject(tabTeam);
            out.writeObject(tabFood);
            out.flush(); // flush the memory
            out.close(); // close the stream
        } catch (Exception e) {
            throw new BadWorldFormatException(e.getMessage()); 
        }
    }


    /**
     * Load the current game elements from a file.
     * The method uncompresses the datas
     * @param file  file loaded
    */
    @SuppressWarnings("unchecked")
    /* We KNOW what we are doing. And we KNOW that the
        result will be good !!! The only error could be when a third-person create a level with
        wrong objects in it. But then it generates an exception !!
    */
    public void loadGame(String file) throws BadWorldFormatException {
        /* We store the file content in temporary objects. This way, if a file is corrupted,
        * we don't corrupt our real variables !!! */
        Integer tmpSizeX;
        Integer tmpSizeY;
        Floor[][] tmpTab;
        ArrayList<Team> tmpTabTeam;
        ArrayList<WorldPoint> tmpTabFood;
        // We unserialize, uncompress the file and store its content
        try {
            ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
            tmpSizeX = (Integer)in.readObject();
            tmpSizeY = (Integer)in.readObject();
            tmpTab = (Floor[][])in.readObject();
            tmpTabTeam = (ArrayList<Team>)in.readObject();
            tmpTabFood = (ArrayList<WorldPoint>) in.readObject();
            in.close(); // close the stream
        } catch (Exception e) {
            throw new BadWorldFormatException(e.getMessage()); 
        }
        // And if everything goes right, we store the result in the good variables
        sizeX = tmpSizeX;
        sizeY = tmpSizeY;
        tab = tmpTab;
        tabTeam = tmpTabTeam;
        tabFood = tmpTabFood;
        path = new PathFinding(this);
        @SuppressWarnings("unchecked") 
        Set<Values>[] array = (Set<Values>[]) new Set<?>[sizeX * sizeY]; 
        model = new DefaultGridModel<Values>(sizeY, sizeX, Arrays.asList(array));
        //model = new DefaultGridModel<Values>(sizeY, sizeX, Arrays.asList(new Set[sizeX * sizeY]));
    }

    
    /**
     * Save the current level in ascii mode
     * Here are the possible elements :
     *   - 'space' for empty blocks (grass)
     *   - '#' for walls
     *   - 'o' for food
     *   - '1' for the player's antHill
     *   - '2' for other antHills
     * @param file  file where we save
     */
    public void saveLevel(String file) throws BadWorldFormatException  {
        try {
            // We open the file
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            
            // We write the current dimensions
            String sX = Integer.toString(sizeX);
            String sY = Integer.toString(sizeY);
            out.write(sX); out.newLine();
            out.write(sY); out.newLine();
            
            // We write a representation of all the tiles
            for (int y=0; y < sizeY; ++y) {
                for (int x=0; x < sizeX; ++x) {
                    Floor f = tab[y][x];
                    Values foreType = f.getType();
                    StorableWorldElem c = null;
                    switch (f.getBackgroundType()) {
                    case grass :
                    	c = StorableWorldElem.GRASS;
                    	if (foreType != null)
	                    	switch (foreType) {
	                    	case food: 			c = StorableWorldElem.GRASS_FOOD; break;
	                    	case rock: 			c = StorableWorldElem.GRASS_ROCK; break;
	                    	case antHill: 		c = StorableWorldElem.GRASS_ANTHILL; break;
	                    	case antHill_enemy: c = StorableWorldElem.GRASS_ANTHILL_EN; break;
	                    	}
                    	break;
                    case desert:
                    	c = StorableWorldElem.DESERT;
                    	if (foreType != null)
	                    	switch (foreType) {
	                    	case food: 			c = StorableWorldElem.DESERT_FOOD; break;
	                    	case rock: 			c = StorableWorldElem.DESERT_ROCK; break;
	                    	case antHill: 		c = StorableWorldElem.DESERT_ANTHILL; break;
	                    	case antHill_enemy: c = StorableWorldElem.DESERT_ANTHILL_EN; break;
	                    	}
                    	break;
                    case water:
                    	c = StorableWorldElem.WATER;
                    }                    
                    out.write(StorableWorldElem.getCharValue(c));
                }
                out.newLine();
            }
            // And we close
            out.flush();
            out.close();
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new BadWorldFormatException(e.getMessage()); 
        }
    }
    

    /**
     * Load a level from a file. This file is an ascii one, with a special character for element
     * For a list of acceptable characters to write, see the SaveLevel function 
     * Verifications of what number of what element is allowed are not made here !!
     * This function takes the elements ant put them in the ground. That's all.
     * @param file the file we want to load
     * @see #saveLevel(String)
     */
    public void loadLevel(String file) throws BadWorldFormatException {
        /* We store the file content in temporary objects. This way, if a file is corrupted,
        * we don't corrupt our real variables !!! */
    	Integer tmpSizeX;
        Integer tmpSizeY;
        Floor[][] tmpTab;
        ArrayList<Team> tmpTabTeam = new ArrayList<Team>();
        ArrayList<WorldPoint> tmpTabFood = new ArrayList<WorldPoint>();
        int nbAntHill = 0;
        int nbAntHillOpp = 0;
        
        // We try to get the file
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            tmpSizeX = Integer.parseInt(in.readLine());
            tmpSizeY = Integer.parseInt(in.readLine());
            tmpTab = new Floor[tmpSizeY][tmpSizeX];
            for (int y = 0; y < tmpSizeY; ++y) {
                // Before using a ling, we check if she is OK
                String line = in.readLine();
                if (line == null)
                    throw new BadWorldFormatException("Not enough colums found");
                if (line.length() != tmpSizeX)
                    throw new BadWorldFormatException("Bad number of tiles for line " + y);
                // If the size is good, we convert all the characters in Floor elements
                for (int x = 0; x < tmpSizeX; ++x) {
                    Values valBack = Values.grass;
                    Values valFore = null;
                    StorableWorldElem c = StorableWorldElem.getValueChar(line.charAt(x));
                    switch (c) {
	                    case GRASS_FOOD			: valFore = Values.food; break;
	                    case GRASS_ROCK			: valFore = Values.rock; break;
	                    case GRASS_ANTHILL		: valFore = Values.antHill; nbAntHill++; break;
	                    case GRASS_ANTHILL_EN	: valFore = Values.antHill_enemy; nbAntHillOpp++; break;
	                    case DESERT				: valBack = Values.desert; break;
	                    case DESERT_FOOD		: valBack = Values.desert; valFore = Values.food; break;
	                    case DESERT_ROCK		: valBack = Values.desert; valFore = Values.rock; break;
	                    case DESERT_ANTHILL		: valBack = Values.desert; valFore = Values.antHill; nbAntHill++; break;
	                    case DESERT_ANTHILL_EN	: valBack = Values.desert; valFore = Values.antHill_enemy; break;
	                    case WATER				: valBack = Values.water;
                    }
                    tmpTab[y][x] = new Floor(valFore, valBack,x, y);
                }
            }
            in.close(); // close the stream
        } catch (Exception e) {
            throw new BadWorldFormatException(e.getMessage());
        }
        // And if everything goes right, we store the result in the good variables
        sizeX = tmpSizeX;
        sizeY = tmpSizeY;
        tab = tmpTab;
        tabTeam = tmpTabTeam;
        tabFood = tmpTabFood;
        path = new PathFinding(this);
        WorldBlur.blurMap(tab);
        //model = new DefaultGridModel<Values>(sizeY, sizeX, Arrays.asList(new Set[sizeX * sizeY]));
        @SuppressWarnings("unchecked") 
        Set<Values>[] array = (Set<Values>[]) new Set<?>[sizeX * sizeY]; 
        model = new DefaultGridModel<Values>(sizeY, sizeX, Arrays.asList(array));

    }
    
    
    /**
     * Give all the positions of the food in the map
     * @return the List of food
     */
    public List<WorldPoint> getFood() {
        return tabFood;
    }
    
    
    /**
     * Give the point of the current tile
     * @param x the x coord
     * @param y the y coord
     * @return the point of this position, else null of the position is not available
     */
    public WorldPoint getPoint(int x, int y) {
        return isPositionAvailable(x,y)
            ? tab[y][x].getPoint()
            : null;
    }
    
      
    /**
     * Give the weight of the tile. This represents the difficulty we will have
     * to cross this tile
     * @return the weight
     */
    public int getWeight(final int x, final int y) {
    	return tab[y][x].getWeight();
    }
    
}
