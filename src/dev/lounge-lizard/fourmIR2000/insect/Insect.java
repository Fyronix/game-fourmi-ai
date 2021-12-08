package dev.lounge-lizard.fourmIR2000.insect;

import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import dev.lounge-lizard.fourmIR2000.insect.behaviors.Behavior;
import dev.lounge-lizard.fourmIR2000.insect.behaviors.BehaviorFactory;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.util.Randomizer;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;


/**
 * Abstract class of all the insects
 */
public abstract class Insect implements Serializable {
	
	
	/** Enum witch gives the current direction of the insect */
    public enum Direction {
        LEFT, LEFT_UP, UP, UP_RIGHT, RIGHT, RIGHT_DOWN, DOWN, DOWN_LEFT
    }
    
    /** Skills of the insect. Use to determine if he's strong, got a lot of energy, ... */
    public enum Skills {
        WEAK, MEDIUM, STRONG, AMAZING, GOD
    }
    
    /** Enum which gives the orders which can be given by an insect */
    public enum Order {
        NONE, FIGHT, GIVE_FOOD, FIND_FOOD, FOLLOW_GROUP, STAY, KEEP_WALKING, EAT
    }
    
    /** For serialization : version of the class */
    private static final long serialVersionUID = 1L;
    
    /** Max tries for an insect to move (for a round) */
    private static final int NB_TRY_TO_MOVE = 8;
    
    /** Strength earn each time the insect win a fight */
    final static int STRENGTH_PER_FIGHT = 1;
    
    /** Importance of damages when an insect is poisoned */
    private final static int POISON_DAMAGE = 20;


    /** Current position of the insect */
    protected WorldPoint curPos;
    
    /** Destination of the insect */
    private WorldPoint destPos;
    
    /** Reference point */
    private WorldPoint refPoint;
    
    /** Picture of the insect */
    protected Values picture;
    
    /** Team of the insect */
    protected Team team;
    
    /** Is the insect fighting ? */
    private boolean isFighting;
    
    /** The insect we eventualy fight with */
    private Insect opponent;
    
    /** Maximal energy of an insect. This may be modified in subclasses */
    protected int maxEnergy = 100;
    
    /** Energy of the insect */
    protected int energy = 100;
    
    /** Strength of the insect */
    private int strength = 2;
    
    /** Path to follow to reach the current destination */
    protected LinkedList<WorldPoint> path;
    
    /** Time since wen the insect tries to go in a tile, but without succeed (ie. if there is already another insect there) */
    private int timeIsWaiting;
    
    /** Current order of the insect */
    protected Order order;
    
    /** The insect who has given the order */
    private Insect orderAuthor;
    
    /** Direction of the insect */
    protected Direction dir;
    
    /** maximal quantity of food that an insect can handle */
    protected int maxFood;
    
    /** The quantity of food the insect is able to handle */
    protected int food;
    
    /** To know if the insect is marked by the selection */
    private boolean isInGroup;
    
    /** Radius around the insect to ask for some help in a fight */
    protected int radiusFight;
    
    /** Radius around the insect to ask for some food */
    protected int radiusFood;
    
    /** Curent behavior of the insect */
    protected Behavior curBehavior;
    
    /** Speed of the insect : the biggest is the value, the slower is the insect */
    protected int speed;
    
    /** Linked to the 'speed' data : time to wait before doing anything. */
    private int timeBeforeActing;
    
    /** True when an insect is poisoned */
    private boolean isPoisoned;
    
    /** True when a shield is activated, reducing the strength of attacks */
    private boolean isShieldActivated;
    
    /** Difficulty of walking in this kind of tile */
    private int tileDifficulty;

    /** true when an insect didn't find something to do */
	protected boolean foundNothingToDo;

	/** Determines the rank of the Insect */
    protected Rank rank;
    
    /** Determines the time left before upgrading the rank */
    protected int nextRank; 
    
    
    /**
     * Constructor of all insects
     * To finish the initialization of a real insect (inherited ones), you HAVE TO call the 
     * assignSkills() function
     * @see #assignSkills(int, int, Skills)
     * @param curPos    current position of the insect
     * @param team      team of the insect
     */
    Insect(WorldPoint curPos, Team team) {
        this.curPos = curPos;
        this.destPos = curPos;
        this.team = team;
        this.picture = null;
        rank = Rank.PRIVATE;
        nextRank = 0;
        timeIsWaiting = 0;
        order = Order.NONE;
        orderAuthor = null;
        isInGroup = false;
        path = null;
        maxFood = 0;
        food = 0;
        dir = Direction.UP;
        radiusFight = 5;
        radiusFood = 6;
        curBehavior = null;
        speed = 1;
        timeBeforeActing = 0;
        isPoisoned = false;
        tileDifficulty = 0;
        isShieldActivated = team.isShieldActivated();
        foundNothingToDo = false;
    }
    
    
    /**
     * Set the skills of the insect. This function should only be called once, just
     * after the constructor of inherited objects
     * @param newEnergy the new maximal energy of the insect
     * @param newStrength  the new strength of the insect
     * @param skill the new skills
     */
    protected void assignSkills(int newEnergy, int newStrength, Skills skill) {
        maxEnergy = newEnergy;
        strength = newStrength;
        switch (skill) {
            case WEAK:      maxEnergy /= 2;
                            strength -= 2;
                            break;
            case MEDIUM:    maxEnergy = new Double(maxEnergy * .75).intValue();
                            strength -= 1;
                            break;
            case STRONG:    break;  /* starting values */
            case AMAZING:   maxEnergy = new Double(maxEnergy * 1.25).intValue();
                            strength += 1;
                            break;
            case GOD:       maxEnergy *= 2;
                            strength += 2;
                            break;
        }
        if (strength < 1)   strength = 1;
        if (maxEnergy <= 0) maxEnergy = 1;
        energy = maxEnergy;
    }
    
    
    /**
     * Give a number between -1 and 1 to move an insect of 1 tile
     * @return the random number
     */
    protected final static int giveRandomMove() {
        int val = new Double(Math.floor(Randomizer.getRangeDouble(0, 3))).intValue();
        return val >= 2 ? -1 : val;
    }
    
    
    /**
     * Give a random position around the starting point
     * @param p The point to use to find a new position
     * @return the new Point
     */
    protected WorldPoint giveRandomPosition(WorldPoint p) {
        WorldPoint testPos = null;
        int nbTry = 0;
        boolean posFound = false;
        
        while (!posFound && nbTry++ < 8) {
            int x = p.getX() + giveRandomMove();
            int y = p.getY() + giveRandomMove();
            if (team.getWorld().isPositionAvailable(x, y)) {
                testPos = team.getWorld().getPoint(x, y);
                if (team.getWorld().canMoveHere(curPos, testPos))
                    posFound = true;
            }
        }
        return !posFound ? curPos : testPos;
    }
    
    
    /**
     * Research the food is the nearest of the ant hill
     * @param nbPoints The number of nearest point of food for the choice
     * @return Point the position of the food
     */
    public WorldPoint findTheBestPointOfFood(int nbPoints) {
        // Variables
        List<WorldPoint> tabFood = team.getWorld().getFood();
        
        // If there is no food on the map, we exit
        if (tabFood.size() == 0)
            return null;
        
        // We put all the food elements in a priority queue
        final PriorityQueue<WorldPoint> queue = new PriorityQueue<WorldPoint>(1, new Comparator<WorldPoint>() {
            public int compare(WorldPoint p1, WorldPoint p2) {
                double dist1 = p1.distance(curPos);
                double dist2 = p2.distance(curPos);
                return dist1 < dist2 ? -1 : dist1 == dist2 ? 0 : 1;
            }
        });
        for (WorldPoint p : tabFood)
            queue.add(p);
        
        // If we want only the nearest element, we return it
        if (nbPoints == 1)
            return queue.remove();
        
        // Else we get a random number between 1 and the number of points to check (limit to the number of food elements)        
        int nbTries = Randomizer.getInt(Math.min(nbPoints, tabFood.size()) + 1);
        nbTries--;
        // And we return the associated point of food
        WorldPoint resu = null;
        for (int i=0; i<=nbTries; ++i)
            resu = queue.remove();
        return resu;
    }
    
    
    /**
     * Called when an insect move to go somewhere
     */
    public void move() {
    	foundNothingToDo = false;
    	
    	// We move only if we managed to exit the last tile (it could be a difficult tile, like
    	// water, and it takes time to travel
    	if (!tryToWalk())
    		return;
    	
        WorldPoint lastPos = curPos;
        
        // The insect can't move if he is fighting
        if (!isFighting) {
            
            // If we were following a path, we continue
            if (path != null && !path.isEmpty()) {
                WorldPoint newPos;
                
                /* If we weren't waiting, we get the new position to continue.
                 * But if we were blocked, we try in a random position just for one time
                 * --> It can help to resolve a conflict when 2 insects go in opposites directions */
                if (timeIsWaiting == 0) {
                    newPos = path.removeFirst();
                } else if (timeIsWaiting > 1) {
                    newPos = giveRandomPosition(curPos);
                } else {
                    newPos = curPos;
                }
                
                // If we can't move for now, we wait
                timeIsWaiting = team.getWorld().moveInsect(this, newPos) ? 0 : timeIsWaiting + 1;
                if (timeIsWaiting == 0) {
                    curPos = newPos;
                    tileDifficulty = team.getWorld().getWeight(curPos.getX(), curPos.getY());
                } else
                	foundNothingToDo = true;
            }
            
            // If we are at the end of the path, we find another destination
            else {
                if (curPos.equals(destPos)) 
                    findNewDestination();
            }
            
            // We get the new direction of the insect
            changeDirection(lastPos);
        }
    }
    
    
    /**
     * Find a new random destination for the insect.
     * This method can be overriden and set to do nothing for insects
     * we want to control by ourselves (for example, player's insect)
     */
    public void findNewDestination() {
        WorldPoint p;
        do {
            int x = Randomizer.getInt(team.getWorld().getWidth());
            int y = Randomizer.getInt(team.getWorld().getHeight());
            p = team.getWorld().getPoint(x, y);
        } while (!team.getWorld().isCrossable(p));
        setDirection(p);
    }
    
    
    /**
     * Move in a random direction. If the destination is blocked, we try in few
     * others juste to be sure we can't do something.
     */
    public void moveRandom() {
    	// We move only if we managed to exit the last tile (it could be a difficult tile, like
    	// water, and it takes time to travel
    	if (!tryToWalk())
    		return;
    	
        /* We limit the number of tries to avoid
         * infinite loops, for example when all blocks around are obstacles  */
        WorldPoint lastPos = curPos;
        boolean hasMoved = false;
        int nbTry = 0;
        
        while (!hasMoved && nbTry++ < NB_TRY_TO_MOVE) {
            /* We search a new crossable position. If it is free, we move there */
            WorldPoint newPos = giveRandomPosition(curPos);
            if (team.getWorld().moveInsect(this, newPos)) {
            	tileDifficulty = team.getWorld().getWeight(curPos.getX(), curPos.getY());
                curPos = newPos;
                hasMoved = true;
            }
        }
        changeDirection(lastPos);
    }
    
    
    /**
     * Looks if it is possible to walk, or if we need some more time to cross the
     * current tile
     * @return true if we can move, else false
     */
    private boolean tryToWalk() {
    	tileDifficulty--;
    	if (tileDifficulty <= 0)
    		return true;
    	return false;
    }
    
    
    /**
     * Assign a new direction for the insect
     * @param destPos the destination point
     */
    public void setDirection(WorldPoint destPos) {
        if (destPos != null)
            setDirection(destPos, Order.NONE, null);
    }
    
    /**
     * Assign a new direction for the insect
     * @param destPos the destination point
     * @param order  true if somebody asks the insect to go there specifically
     * @param insectWhoHasGivenOrder the insect who has given the order
     */
    public void setDirection(WorldPoint destPos, Order order, Insect insectWhoHasGivenOrder) {
        if (curPos.equals(destPos))
            return;
        this.order = order;
        this.destPos = destPos;
        this.orderAuthor = insectWhoHasGivenOrder;
        if (!curPos.equals(destPos))
            path = team.getWorld().getPathFinding().getPath(curPos, destPos);
    }
    
    
    /**
     * Set the new direction of the insect regarding on the last position he was
     * @param lastPos his last position
     */
    private void changeDirection(WorldPoint lastPos) {
        if (! lastPos.equals(curPos)) {
            int curX = curPos.getX(); int lastX = lastPos.getX();
            int curY = curPos.getY(); int lastY = lastPos.getY();
            if      (curX < lastX  && curY < lastY) dir = Direction.LEFT_UP;
            else if (curX == lastX && curY < lastY) dir = Direction.UP;
            else if (curX > lastX  && curY < lastY) dir = Direction.UP_RIGHT;
            else if (curX < lastX  && curY == lastY)dir = Direction.LEFT;
            else if (curX > lastX  && curY == lastY)dir = Direction.RIGHT;
            else if (curX < lastX  && curY > lastY) dir = Direction.DOWN_LEFT;
            else if (curX == lastX && curY > lastY) dir = Direction.DOWN;
            else dir = Direction.RIGHT_DOWN;
        }
    }
    
    
    /**
     * Called when an insect fight with another insect
     * @param insect the other insect we fight with
     */
    public void fight(Insect insect) {
        // If our opponent is dead, we stop the fight
        if (insect == null || insect.energy <= 0) {
            isFighting = false;
            opponent = null;
            order = Order.NONE;
            orderAuthor = null;
        } else {
            // If it is our first kick, we ask for some help
            if(!isFighting)
                askForHelpToFight();
            isFighting = true;
            opponent = insect;
            insect.isFighting = true;
            insect.opponent = this;
            opponent.isFighting = true;
            // We kick him with all our power and hope we kill him
            //insect.decreaseEnergy(Double.valueOf(strength*Math.random()).intValue());
            insect.decreaseEnergy(strength);
            if (insect.energy <= 0) {
                isFighting = false;
                opponent = null;
                order = Order.NONE;
                orderAuthor = null;
                // If we won the fight, our skills are increased
                rank.increaseExperience(this);
            }
        }
    }
    
    
    /** 
     * Called when an insect die.
     */
    protected void die() {
        if (opponent != null && opponent.isFighting) {
            opponent.isFighting = false;
        }
        opponent = null;
        orderAuthor = null;
        team.removeInsect(this);
    }
    
    
    /**
     * Called when an insect eat something
     * @param foodOffered   quantity of food we could eat
     * @return the quantity of food left at the end of the operation
     */
    public int eat(int foodOffered) {
        int foodTaken = (energy + foodOffered) < maxEnergy
            ? foodOffered
            : maxEnergy - energy;
        energy += foodTaken;
        return foodOffered - foodTaken;
    }
    
    
    /**
     * Called when an insect eat as much as he can
     */
    public void eat() {
        energy = maxEnergy;
        food = maxFood;
    }
    
    
    /**
	 * Give the picture of the insect
	 * @return  the picture
	 */
    public Values getPicture() {
        return picture;
    }
    
    
    /**
     * Give the picture of the insect, followed by his actions pictures. For exemple,
     * the picture showing that he is fighting or that he need food
     * @return the list of pictures
     */
    public EnumSet<Values> getPictures() {
        EnumSet<Values> elems = EnumSet.noneOf(Values.class);
        elems.add(getPicture());
        if (isFighting)
            elems.add(Values.ins_fight);
        if (needFood())
            elems.add(Values.ins_help);
        if (isPoisoned)
            elems.add(Values.ins_poison);
        if (isInGroup)
            elems.add(Values.select);
        /* pictures if the insect is ranked */
        if (rank == Rank.SERGEANT)
        	elems.add(Values.rnk_sergeant);
        else if (rank == Rank.CAPTAIN)
        	elems.add(Values.rnk_captain);
        else if (rank == Rank.MAJOR)
        	elems.add(Values.rnk_major);
        else if (rank == Rank.COLONEL)
        	elems.add(Values.rnk_colonel);
        else if (rank == Rank.GENERAL)
        	elems.add(Values.rnk_general);

        return elems;
    }
    
    
    /**
	 * Give the energy of the insect
	 * @return  the energy
	 */
    public int getEnergy() {
        return energy;
    }
    
    
    /**
     * Give the current position of the insect
     * @return the Point of the insect
     */
    public WorldPoint getPos() {
        return curPos;
    }
    
    
    /**
	 * Cheks if the insect is fighting
	 * @return  true if insect is fighting, else false;
	 */
    public boolean isFighting() {
        return isFighting;
    }

    
    /**
     * Checks if the insect needs to demand food
     * @return boolean
     */
    protected boolean needFood() {
        return energy < maxEnergy / 4
            ? true
            : false;
    }

    
    /**
     * Ask some food
     * @param nbCaseAround    The number of case to see around
     */
    protected void askForFood(int nbCaseAround) {
        LinkedList<Insect> friend;

        friend = team.getWorld().giveInsectAround(this, nbCaseAround, true);
        for (Insect insect : friend) {
            if (!insect.isFighting && (insect instanceof AntWorker)) {
                AntWorker antWorker = (AntWorker)insect;
                if(antWorker.order == Order.NONE && antWorker.hasFood()) {
                    antWorker.setDirection(curPos, Order.GIVE_FOOD, this);
                    return;
                }
            }
        }
    }
    
    
    /**
     * Checks if the insect needs to demand food
     * @return boolean
     */
    public boolean needFood(int quarter) {
        return energy < maxEnergy / quarter ? true : false;
    }
    
    
    /**
     * Give order to an Worker to give food
     */
    public void askForHelpToFight() {
        if (opponent == null)
            return;
        LinkedList<Insect> friend = team.getWorld().giveInsectAround(this, radiusFight, true);
        for (Insect insect : friend)
            if (!insect.isFighting && insect.order != Order.FIGHT) {
            	insect.opponent = opponent;
                insect.setDirection(opponent.curPos, Order.FIGHT, this);
            }
    }
    
    
    /**
     * Checks if the insect is near the point of destination
     * @return boolean    true if we are near the destination, false else.
     */
    public boolean isNearDestination() {
        return curPos.distance(destPos) < 2;
    }
    
    
    /**
     * Give the insect's team
	 * @return  the team
	 */
    public Team getTeam() {
        return team;
    }
    
    
    /**
     * Decrease the energy of the insect, so that he needs food to survive
     */
    protected void loseEnergy() {
    		decreaseEnergy(1);
    }
    
    
    /**
     * Force the insect to loose some energy. If it reach '0', then the insect die.
     * (This is the hard low of the Nature)
     * @param quantity  quantity of energy lost.
     */
    protected void decreaseEnergy(int quantity) {
    	if(isFighting && isShieldActivated) {
    		// If an insect got a shield, damages are reduced
    		double tmp = Randomizer.getRangeDouble(2./3., 1.);
    		double qtyLost = tmp * quantity; 
    		energy -= qtyLost <= 1 ? 1 : qtyLost; 
    	}
    	else
    		energy -= quantity;
        if (energy <= 0)
            die();
    }
    
    
    /**
     * Equivalent to a method live(). Give the instruction of what the
     * insect must do in the next turn. The action depends on the current
     * behavior of the insect
     */
    public void getAction() {
        // If we are poisoned, we loose a lot of energy
        if (isPoisoned) {
            decreaseEnergy(POISON_DAMAGE);
            if (energy <= 0)
            	return;
        }
        // If we are on a group, we just follow him
        if (isInGroup)
            curBehavior = BehaviorFactory.getBehaviorGroup();
        // Else we do the default action of the insect
        if (curBehavior != null)
            curBehavior.doAction(this);
        // And we refresh the pictures
        team.getWorld().updateModel(curPos.getX(), curPos.getY(), false);
    }
    
    
    /**
	 * @return true if the insect is currently in a group
	 */
    public boolean getIsInGroup() {
        return isInGroup;
    }

    /**
	 * @param state assign / unassign the insect in a group
	 */
    void setIsInGroup(boolean state) {
        this.isInGroup = state;
    }


    /**
     * Give the insect who ordered us to do something
     * @return the insect, else null
     */
    public Insect getOrderAuthor() {
        return orderAuthor;
    }

	/**
	 * Give the quantity of food of the insect if it is able to handle it
	 * @return the quantity of food handled
	 */
    public int getFood() {
        return food;
    }

    /**
     * Sets the quantity of food handled by the insect
     * @param food	the quantity of food handled
     */
    public void setFood(int food) {
        this.food = food;
    }

    /**
     * Give the opponent of the insect
     * @return the opponent
     */
    public Insect getOpponent() {
        return opponent;
    }

    /**
     * Give the maximum of food the insect the insect can handle
     * @return the maximum of food handled
     */
    public int getMaxFood() {
        return maxFood;
    }

    /**
     * Give the speed of the insect
     * @return the speed of the insect
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Give the time before a new action
     * @return the time before the next action
     */
    public int getTimeBeforeActing() {
        return timeBeforeActing;
    }
    

    /**
     * Set the time before a new action
     * @param timeBeforeActing the time before acting
     */
    public void setTimeBeforeActing(int timeBeforeActing) {
        this.timeBeforeActing = timeBeforeActing;
    }

    /**
     * Give the order of the insect has to do
     * @return the order to execute
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Set the order to be executed by the insect
     * @param order the order to transmit to the insect
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Give the point of reference of the insect 
     * @return the point of reference
     */
    public WorldPoint getRefPoint() {
        return refPoint;
    }

    /**
     * Set the point of reference of the insect
     * @param refPoint the point of reference to transmit
     */
    public void setRefPoint(WorldPoint refPoint) {
        this.refPoint = refPoint;
    }
    
    /**
     * Set if the insect was poisoned or not
     * @param state true if poisoned, false else
     */
    protected void setPoisoned(boolean state) {
        isPoisoned = state;
    }

    /**
     * Set the rank of the insect
     * It permits to increase easily the level of the insect
     * @param willingRank the rank wished for the insect
     */
	protected void setRank(Rank willingRank) {
		while(rank.ordinal() < willingRank.ordinal()) 
			rank.increaseExperience(this);
	}

	
	/**
	 * Add some strength to the insect
	 * @param number the strength to add
	 */
	void addStrength(int number) {
		strength += number;
	}


	/**
	 * Give how many fights the insect must still win to increase his rank
	 * @return the number of fights to win
	 */
	int getNextRank() {
		return nextRank;
	}


	/**
	 * Assign the number of fights to win before changing of ranck
	 * @param nextRank the number of fights to win
	 */
	void setNextRank(int nextRank) {
		this.nextRank = nextRank;
	}
}
