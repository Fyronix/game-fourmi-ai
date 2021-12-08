package dev.lounge-lizard.fourmIR2000.world;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import dev.lounge-lizard.fourmIR2000.pictures.Values;


/**
 * Search in the map for the fastest path between 2 points. For an optimized use
 * of this class, you must create ONLY ONE INSTANCE PER PROGRAM. 
 * WHY ? to gain speed, we declare 'model' variables, initialized in the constructor.
 * Then, for each search, we use a clone() of these variables. This way, the
 * creation of wide objects is way faster !!!
 *  
 * The algorithm used here is the A* (pronounce 'A star')
 *     http://fr.wikipedia.org/wiki/Algorithme_A%2A#Pseudocode
 */
public final class PathFinding implements Serializable {

    /** For serialization : version of the class */
    private static final long serialVersionUID = 1L;

    /** Map to use */
    private final World world;
    
    /** Maximum value of X */
    private final int maxX;
    
    /** Size Y of the map */
    private final int maxY;
    
    /** A Star : heuristic of known tile (the cost to reach a known point) F(n) = G(n) + estimated cost "n --> goal" This is a model. This variable will be cloned at each path finding */
    private final double[] F_model; 

    /** A Star : G(n') = cost of G(n) + cost "n --> n'" This is a model. This variable will be cloned at each path finding */
	private final double[] G_model;
    
    /** A Star : predecessors of each tiles This is a model. This variable will be cloned at each path finding */
    private final int[] parent_model;
    
    
    /**
     * Constructor of the class.
     * @param world the world to use to search the path
     */
    public PathFinding(final World world) {
        if (world == null)
            throw new IllegalArgumentException();
        this.world = world;
        maxX = world.getWidth();
        maxY = world.getHeight();
        F_model = new double[maxX * maxY];  
        G_model = new double[maxX * maxY];
        parent_model = new int[maxX * maxY];
    }


    
    /**
     * Give the index of a tile when we have the point
     * @param p the point to get the coords 
     * @return the index for arrays
     */
    private int getIndex(final WorldPoint p) {
    	return p.getY() * maxX + p.getX();
    }
    
    
    /**
     * Give the coords of a tile when we have the index
     * @param index index of the tile
     * @return the coords
     */
    private WorldPoint getCoords(final int index) {
        final int y = index / maxX;
        final int x = index % maxX;
        return world.getPoint(x, y);
    }
    

    /**
     * Give a fastest path between src and dest.
     * @param src       The source point
     * @param dest      The destination point
     * @return  the path between the 2 points, or the current position if the destination is not reachable
     */
    public LinkedList<WorldPoint> getPath(final WorldPoint src, final WorldPoint dest) {
        if (src == null || dest == null || !world.isPositionAvailable(src) || !world.isPositionAvailable(dest))
            throw new IllegalArgumentException();   
        return getPathAStar(src, dest);
    }
    
    
    /**
     * Perform a A* search to find the best path between src and dest.
     * The algorithm used here (and just the algorithm, not the code !) comes from Wikipedia :
     *     http://fr.wikipedia.org/wiki/Algorithme_A%2A#Pseudocode
     * @param src   the source point
     * @param dest  the destination point
     * @return the list of points to follow to reach the destination, or the src
     *         point if there is no valid path
     */
    private LinkedList<WorldPoint> getPathAStar(final WorldPoint src, final WorldPoint dest) {        

        // Initialization : clone() function is faster than the 'new' operations... so we abuse of it :)
        final double[] F = F_model.clone();       // heuristic : G(n) + estimated cost "n --> goal"
        final double[] G = G_model.clone();       // G(n') = cost of G(n) + cost "n --> n'"
        final int[] parent = parent_model.clone();// predecessors of all tiles
        
        final int indexSrc = getIndex(src);              // index of the source point in arrays
        final int indexDest = getIndex(dest);           // index of the destination point in arrays
        final HashSet<WorldPoint> close = new HashSet<WorldPoint>(dest.getX() * dest.getY() * 2);     // tiles visited
        
        /* To find the most probable next point, we use a priority queue (a heap). This way, we always get
        * the better point in a marvelous time : O(log(n)) for 'add', and O(1) for 'remove' */
        final PriorityQueue<Integer> open = new PriorityQueue<Integer>(1,new Comparator<Integer>() {
            public final int compare(final Integer p1, final Integer p2) {
            	double f1 = F[p1];
            	double f2 = F[p2];
            	return f1 < f2 ? -1 : f1 == f2 ? 0 : 1; 
            }
        });
        open.add(getIndex(src));
        
        // We try to build the path
        boolean found = false;
        while (! open.isEmpty() && !found) {
            // We get the point with the lower heuristic
            WorldPoint curPoint = getCoords(open.remove());
            int indexCur = getIndex(curPoint);
            
            // DEBUG ONLY : PRINT FOOD ELEMENTS TO SHOW THE TILES USED (really funny !)
            //world.setElement(curPoint.getX(), curPoint.getY(), fr.umlv.fourmIR2000.pictures.Values.food, true);
            
            // If we reach the destination, then enjoy, we got our path :)
            if (curPoint.equals(dest)) {
                found = true;
                break;
            }
            
            // In other case, we look for the better child (if any)
            for (WorldPoint child : adjacent(curPoint)) {

                // Are the values better than the previous ones ?
                int indexChild = getIndex(child);
                int difficulty = Values.getDifficultyCrossing(world.getBackgroundElement(child.getX(), child.getY()));
                double Gtmp = G[indexCur] + difficulty;
                double Ftmp = Gtmp + child.distance(dest);
                
                // If not, we try with the next child
                if (open.contains(child)  && F[indexChild] <= Ftmp) continue;
                if (close.contains(child)  && F[indexChild] <= Ftmp) continue;

                // If yes, we remove the point from lists, we update everything and we re-add it to 'open'
                open.remove(getIndex(child));
                close.remove(child);
                parent[indexChild] = indexCur;
                G[indexChild] = Gtmp;
                F[indexChild] = Ftmp;
                open.add(getIndex(child));
            }
            close.add(curPoint);
        }

        // If no path was found, we return the starting position
        final LinkedList<WorldPoint> resu = new LinkedList<WorldPoint>();
        if (!found)
            resu.add(src);
        // In other cases, we build the final path by following the predecessors
        else {
            int curIndex = indexDest;
            while (curIndex != indexSrc) {
                resu.addFirst(getCoords(curIndex));
                curIndex = parent[curIndex];
            }
        }
        return resu;
    }
    
    
    /**
     * Give all possible adjacents tiles (ie. tiles that are crossables) 
     * @param p     The point to check
     * @return the list of reachable adjacent points
     */
    private LinkedList<WorldPoint> adjacent(final WorldPoint p) {
        final LinkedList<WorldPoint> resu = new LinkedList<WorldPoint>();
        final int pX = p.getX();
        final int pY = p.getY();
        for (int y = pY - 1; y <= pY + 1; y++)
            for (int x = pX - 1; x <= pX + 1; x++)
                if (x >= 0 && x < maxX && y >= 0 && y < maxY) {
                    if (world.isPositionAvailable(x, y)) {
                        WorldPoint testPos = world.getPoint(x, y);
                        if (! testPos.equals(p) && world.canMoveHere(p, testPos))
                            resu.add(testPos);
                    }
                }
        return resu;
    }
}
