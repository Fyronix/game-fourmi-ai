package dev.lounge-lizard.fourmIR2000.insect;

import java.io.Serializable;
import java.util.LinkedList;

import dev.lounge-lizard.fourmIR2000.Game;
import dev.lounge-lizard.fourmIR2000.insect.Insect.Skills;
import dev.lounge-lizard.fourmIR2000.world.World;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;

/** 
 * Team of insects. Basicly, a team is composed of :
 *  - an house (antHill),
 *  - a color (do make a graphical difference between the teams)
 *  - a master (the most important insect, eg. the queen for ants)
 */
public final class Team implements Serializable {
    
    /** For serialization : version of the class */
    private final static long serialVersionUID = 1L;
    
    /** Quantity of food needed to ask some help to a Bug */
    public final static int FOOD_NEEDED_FOR_A_BUG = 3000;
    
    /** Quantity of food needed to employ a kamikaze */
    public final static int FOOD_NEEDED_FOR_A_KAMIKAZE = 1000;
    
    /** Quantity of food needed to gain the capacity of producing armors */
    public final static int FOOD_NEEDED_FOR_SHIELD = 2000;
    
    /** Quantity of food needed to gain the capacity of training the ants */
    public final static int FOOD_NEEDED_FOR_VETERANS = 2000;
    
    /** 'Quantity' of knowledges to be able to employ bugs */
    public final static int KNOWLEDGE_FOR_BUGS = 5000;
    
    /** 'Quantity' of knowledges to be able to build kamikazes */
    public final static int KNOWLEDGE_FOR_KAMIKAZES = 3000;
    
    /** 'Quantity' of knowledges to be able to produce ants protected by a shield */
    public final static int KNOWLEDGE_FOR_SHIELDS = 2000;

    /** 'Quantity' of knowledges to be able to produce sergeant ants */
    public final static int KNOWLEDGE_FOR_SERGEANTS = 2000;
    
    /** 'Quantity' of knowledges to be able to produce captain ants */
    public final static int KNOWLEDGE_FOR_CAPTAINS = 5000;
    
    /** 'Quantity' of knowledges to be able to produce major ants */
    public final static int KNOWLEDGE_FOR_MAJORS = 10000;
    
    /** 'Quantity' of knowledges to be able to produce colonel ants */
    public final static int KNOWLEDGE_FOR_COLONELS = 15000;
    
    /** 'Quantity' of knowledges to be able to produce general ants */
    public final static int KNOWLEDGE_FOR_GENERALS = 20000;
    
    
    /** NON SERIALIZABLE : The map */
    private transient World world;
    
    /** NON SERIALIZABLE : The game element. Used when the player insect die, or when somebody win for example */
    private transient Game game;
    
    /** List of all insects in the team */
    private final LinkedList<Insect> tabInsect;
    
    /** Posiiton of the house */
    private final WorldPoint antHill;
    
    /** Most important insect of the team */
    private AntQueen master;

    /** Insect controled by a player */
    private Insect playerInsect;
    
    /** Color of the team */
    private final int color;

    /** Probability of having a worker when an insect appears */
    private int percentWorker;
    
    /** Percentage of food to be converted in knowledges */
    private int percentKnowledge;
    
    /** The food we stock during the game. */
    private int foodStock;
    
    /** The knowledge accumulated by the team */
    private int knowledge;
    
    /** The rank to create the insect */
    private Rank rank;
    
    /** Declares if the shield is activated for the insect created */
    private boolean isShieldActivated;
    
    
    /**
     * Create a new team
     * @param world     the map in witch we put the team
     * @param antHill   the house of the insects
     * @param color     the color of the insects
     * @param game      the Game element
     */
    public Team(World world, WorldPoint antHill, int color, Game game) {
        this.antHill = antHill;
        this.world = world;
        this.color = color;
        this.game= game;
        rank = Rank.PRIVATE;
        master = null;
        playerInsect = null;
        tabInsect = new LinkedList<Insect>();
        percentWorker = 25;
        percentKnowledge = 75;
        foodStock = 0;
        knowledge = 0;
        isShieldActivated = false;
    }
    
    /**
     * Add the master of the team on the map
     * >> In function of the team we are and the difficulty level, masters
     * don't have exactely the same skills
     */
    public void addMaster() {
        Skills skills = null;
        switch (game.getDifficulty()) {
            case EASY :   skills = color == 0 ? Skills.AMAZING : Skills.MEDIUM;  break;
            case MEDIUM : skills = color == 0 ? Skills.STRONG :  Skills.STRONG;  break;
            case HARD :   skills = color == 0 ? Skills.MEDIUM :  Skills.AMAZING; break;
        }
        master = new AntQueen(antHill, this, skills);
        tabInsect.add(master);
    }
    
    
    /**
	 * Give the master insect of the team
	 * @return  the insect
	 */
    public AntQueen getMaster() {
        return master;
    }
    
    /**
	 * Give the position of the antHill
	 * @return  the position of the antHill
	 */
    public WorldPoint getAntHill() {
        return antHill;
    }
    
    /**
     * Give the food stock of the team
     * @return the food stock
     */
    public int getFoodStock() {
    	return foodStock;
    }
    
    /**
     * Change the food stock of the team
     * @param newStock the new stock
     */
    public void setFoodStock(final int newStock) {
    	foodStock = newStock;
    }
    
    /**
     * Give the quantity of knowledges of the team
     * @return the knowledge
     */
    public int getKnowledges() {
    	return knowledge;
    }

    /**
     * Change the quantity of knowledges 
     * @param newKnowledge the new knowledge
     */
    void setKnowledge(final int newKnowledge) {
    	knowledge = newKnowledge;
    }
    
    
    /**
     * Add an insect manipulated by a player
     */
    public void addPlayerInsect(final Insect insect) {
        playerInsect = insect;
        addInsect(playerInsect);
    }
    
    
    /**
     * Put a new insect at the right place on the map
     * @param insect the insect to put on the map
     */
    void addInsect(final Insect insect) {
        if (insect == null || !this.equals(insect.getTeam()))
                throw new IllegalArgumentException();
        insect.setDirection(antHill);
        world.addInsect(insect);    // We don't check the result. We put a new insect on his house, so there is no way to have an error 
        tabInsect.add(insect);
    }
    

    /**
     * Remove an insect of the current team from the map
     * @param insect    the insect to remove
     */
    public void removeInsect(Insect insect) {
        if (insect != null) {
            tabInsect.remove(insect);
            world.removeInsect(insect);
            insect = null;
        }
    }

    
    /**
     * Give the list of all insects in the team
     * @return the list of insects
     */
    public LinkedList<Insect> getTabInsect() {
        return tabInsect;
    }
    
    
    /**
	 * Give the color of the team
	 * @return  the color
	 */
    public int getColor() {
        return color;
    }
    
        
    /**
	 * Give the current World object
	 * @return  the World
	 */
    public World getWorld() {
        return world;
    }
    
    
    /**
	 * Assign a new World to the team. Nota : this should only be used after an unserialization !
	 * @param newWorld  the new world
	 */
    public void setWorld(final World newWorld) {
        world = newWorld;
    }
    
    
    /**
	 * Give the current Game object
	 * @return  the game object
	 */
    Game getGame() {
        return game;
    }
    
    
    /**
	 * Assign a new Game to the team. Nota : this should only be used after an unserialization !
	 * @param newGame  the new game.
	 */
    public void setGame(final Game newGame) {
        game = newGame;
    }
    
    
    /**
     * Give the difficulty of the game
     * @return the current difficuty
     */
    Game.Difficulty getDifficulty() {
        return game.getDifficulty();
    }

    /**
	 * Give the percentage of food that must me converted in knowledges
	 * @return  the percentage
	 */
    int getPercentKnowledge() {
        return percentKnowledge;
    }
    
    /**
	 * Give the probability of having a worker when a new insect appears
	 * @return  the percentage
	 */
    int getPercentWorker() {
        return percentWorker;
    }
    
    /**
	 * Assign a new percentge of worker to be created when a new insect appears
	 * @param  percent the new percentage
	 */
    public void setPercentWorker(final int percent) {
        if (percent < 0 || percent > 100)
            throw new IllegalArgumentException();
        percentWorker = percent;
    }
    
    
    /**
	 * Assign a new percentge of knowledges to be added when the master eat something
	 * @param  percent the new percentage
	 */
    public void setPercentKnowledge(final int percent) {
        if (percent < 0 || percent > 100)
            throw new IllegalArgumentException();
        percentKnowledge = percent;

    }
    
    
    /**
     * Give the number of instance of AntWorker in the team
     * @return the number of AntWorker in the team
     */
    public int nbOfAntWorkerInTeam() {
        int result = 0;
        for (Insect insect : tabInsect)
            if (insect instanceof AntWorker)
                result++;

        return result;
    }
    
    /**
     * Give the number of instance of Warriors in the team
     * @return the number of Kamikaze in the team
     */
    public int nbOfWarriorsInTeam() {
        int result = 0;
        for (Insect insect : tabInsect)
            if (!(insect instanceof AntWorker) && !(insect instanceof AntQueen))
                result++;

        return result;
    }
    
    /**
     * Give a list of Bug of the team
     * @param nb the number of bugs and kamikaze wanted
     */
	public LinkedList<Insect> getListOfWarrior(int nb) {
		final LinkedList<Insect> lst = new LinkedList<Insect>();
		int cpt = 0;
		
        // We search bugs in all our team
		for(int i=0; i<tabInsect.size() && cpt<nb; i++) {
			Insect insect = tabInsect.get(i);
            if (!(insect instanceof AntWorker) && !(insect instanceof AntQueen)) {
                lst.add(insect);
                cpt++;
            }
		}

        return lst;
	}
    
    
    /**
     * Give a group containing all the insects between the 2 coords
     * @param p1    the first coord
     * @param p2    the second coord
     */
	public LinkedList<Insect> createGroup(final WorldPoint p1, final WorldPoint p2) {
		final LinkedList<Insect> insGroup = new LinkedList<Insect>();
		
		try {
			final int p1X = p1.getX();    			final int p1Y = p1.getY();
			final int p2X = p2.getX();    			final int p2Y = p2.getY();
			final int minX = Math.min(p1X, p2X);	final int maxX = Math.max(p1X, p2X);
			final int minY = Math.min(p1Y, p2Y);	final int maxY = Math.max(p1Y, p2Y);
			int cpt = 0;
			
			// We erase the previous group
			undoGroup();
			
	        // We search in all our team
			for(Insect insect : tabInsect) {
				WorldPoint insectPoint = insect.getPos();
				int insectPointX = insectPoint.getX();
				int insectPointY = insectPoint.getY();
	            // If the current insect is on the seletion...
				if((insectPointX >= minX) && (insectPointX <= maxX)
					&& (insectPointY >= minY) && (insectPointY <= maxY)) {
	                    // ... and isn't the queen, we add it to the group
	                    if (! insect.equals(master)) {
	                        insect.setIsInGroup(true);
	                        insGroup.add(insect);
	                        cpt++;
	                    }
				}
			}
			
	        return insGroup;
		} catch (Exception e) {
			return insGroup;
		}
	}

	
	/**
	 * Remove all the insects of the current group
	 */
	public void undoGroup() {
		for(Insect insect : tabInsect)
			insect.setIsInGroup(false);
	}
	
	
	/**
	 * Give the rank given to the insects of the team when one of them is layed
	 * @return the rank
	 */
	public Rank getRank() {
		return rank;
	}

	
	/**
	 * Assign a new rank to use with new insects
	 * @param rank the rank
	 */
	public void setRank(final Rank rank) {
		this.rank = rank;
	}

	
	/**
	 * Indicate if shields are activated in this team
	 * @return true if the shield is activated, else false
	 */
	boolean isShieldActivated() {
		return isShieldActivated;
	}

	
	/**
	 * Activate shields for the current team
	 */
	public void activateShield() {
		isShieldActivated = true;
	}
}
