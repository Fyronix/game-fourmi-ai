package dev.lounge-lizard.fourmIR2000.insect;

import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.util.Randomizer;
import dev.lounge-lizard.fourmIR2000.world.World;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;



/**
 * Queen of the ants. She can produce other ants, but she can't move.
 * She has an uncredible energy.
 */
public final class AntQueen extends Insect {

    /** For serialization : version of the class */
    private final static long serialVersionUID = 1L;

    /** number of turns before we could lay a new ant */
    private final static int DELAY_TO_LAY = 20; 
    
    /** When decrease to '0', we lay a new ant */
    private int nextLay;
    
    /** What type of Ant was created the last time? */
    private int whichAnt;

    
    /**
     * Create a new queen of ants. This is an ant that can't move.
     * @param pos   position of the queen in the map
     * @param team  team of the queen
     * @param skills    skills of the insect
     */
    public AntQueen(WorldPoint pos, Team team, Skills skills) {
        super(pos, team);
        assignSkills(500, 0, skills);
        this.picture = Values.antHill;
        this.nextLay = DELAY_TO_LAY;
        whichAnt = 0;
    }


    /**
     * We don't want that the queen moves, so we override the function without doing anything.
     */
    @Override public void move() {
        // We do nothing. The queen doesn't move !
    }

    
    /**
     * Equivalent to a method live(). Give the instruction of what the ant must do in the next turn
     */
    @Override public void getAction() {
        loseEnergy();
        if(energy >0) {
	        // Is it time to lay an new ant ?
	        nextLay--;
	        if (nextLay <= 0) {
	            layInsect();
	            nextLay = DELAY_TO_LAY;
	        }
	        
	        // If we need food, we inform ALL our ants, EVERYWHERE in the map
	        if(needFood())
	           askForFood(Math.max(team.getWorld().getWidth(), team.getWorld().getHeight()));
	
	        super.getAction();
        }
    }
    

    /**
     * Add a new insect to our team. His skills will change with the game difficulty
     */
    private void layInsect() {

        // Preparation of the skills
        Insect insect;
        Skills nvSkills = null;
        final int color = team.getColor();
        switch (team.getDifficulty()) {
            case EASY :   nvSkills = color == 0 ? Skills.STRONG : Skills.MEDIUM; break;
            case MEDIUM : nvSkills = color == 0 ? Skills.STRONG : Skills.STRONG; break;
            case HARD :   nvSkills = color == 0 ? Skills.MEDIUM : Skills.STRONG; break;
        }
        
        // Is it a worker or a soldier ?
        int percentWorker = team.getPercentWorker();
        if (percentWorker == 0) percentWorker = 1;
        final int limit = 100 / percentWorker;
        
        // We create the corresponding insect
        if(++whichAnt < limit) {
            insect = new AntSoldier(team.getAntHill(), team, nvSkills);
        } else {
            insect = new AntWorker(team.getAntHill(), team, nvSkills);
            whichAnt = 0;
        }
        insect.setRank(team.getRank());
        team.addInsect(insect);
    }
    
    
    /**
     * Force the queen to loose some energy. If it reach '0', then the queen die.
     * Nota : she try to regain some energy by picking in the food stock. It is only when
     *        the stock is empty that she will lose is own energy.
     * @param quantity  quantity of energy we have to remove.
     */
    @Override protected void decreaseEnergy(final int quantity) {
    	int foodStock = team.getFoodStock();
        if (quantity < foodStock)
            team.setFoodStock(foodStock -= quantity);
        else {
        	final int realCost = quantity - foodStock;
            team.setFoodStock(0);
            super.decreaseEnergy(realCost);
        }
    }
    
    
    /**
     * Called when somebody gives some food at the queen. She uses it to restore her energy,
     * then stock the complement in the antHill.
     * Note : a part of the food may be converted in knowledges by the queen. This way, she could
     *        discover new technologies
     * @param quantity  quantity of energy given
     * @return the food left at the end of the operation. For the queen, IT IS ALWAYS '0'
     */
    @Override public int eat(final int quantity) {
    	final int prctKnowledge = team.getPercentKnowledge();
    	int realQuantity = (quantity * (100 - prctKnowledge)) / 100;
    	team.setKnowledge(team.getKnowledges() + quantity - realQuantity);

    	final int decalEnergy = maxEnergy - energy;
        // if there is just enough food for the queen, wi give her everything
        if (realQuantity <= decalEnergy)
            energy += realQuantity;
        // If there is too much energy, we stock the rest
        else {
            energy += decalEnergy;
            team.setFoodStock(team.getFoodStock() + realQuantity - decalEnergy);
        }
        return 0;
    }
    
    
    /** Called when the queen die.
     */
    @Override public void die() {
        super.die();
        team.getGame().informDeadMasterOfTeam(this);
    }
    
    
    /**
     * Add a new bug in our team. This is a big insect with an incredible energy and strength.
     */
    public void addBug() {
        // If we havn't enough food, we ignore the call
        if (team.getFoodStock() < Team.FOOD_NEEDED_FOR_A_BUG)
            return;
        decreaseEnergy(Team.FOOD_NEEDED_FOR_A_BUG);
        Skills nvSkills = null;
        final int color = team.getColor();
        switch (team.getDifficulty()) {
            case EASY :   nvSkills = color == 0 ? Skills.GOD :     Skills.AMAZING;  break;
            case MEDIUM : nvSkills = color == 0 ? Skills.AMAZING : Skills.AMAZING;  break;
            case HARD :   nvSkills = color == 0 ? Skills.AMAZING : Skills.GOD;    break;
        }
        final Bug bug = new Bug(curPos, team, nvSkills);
        bug.setRefPoint(findEmptyTileNearAntHill());
        bug.setRank(team.getRank());
        team.addInsect(bug);
    }
    
    
    /**
     * Add a new kamikaze to the team.
     */
    public void addKamikaze() {
    	// If we havn't enough food, we ignore the call
        if (team.getFoodStock() < Team.FOOD_NEEDED_FOR_A_KAMIKAZE)
            return;
        decreaseEnergy(Team.FOOD_NEEDED_FOR_A_KAMIKAZE);
        Skills nvSkills = null;
        final int color = team.getColor();
        switch (team.getDifficulty()) {
            case EASY :   nvSkills = color == 0 ? Skills.GOD :     Skills.AMAZING;  break;
            case MEDIUM : nvSkills = color == 0 ? Skills.AMAZING : Skills.AMAZING;  break;
            case HARD :   nvSkills = color == 0 ? Skills.AMAZING : Skills.GOD;    break;
        }
        final AntKamikaze kamikaze = new AntKamikaze(curPos, team, nvSkills);
        kamikaze.setRefPoint(findEmptyTileNearAntHill());
        kamikaze.setRank(team.getRank());
        team.addInsect(kamikaze);
    }
    
    
    /**
     * Create a Samantha Religiosa ant
     * @return Samantha
     */
    public Insect addSamantha() {
    	Insect.Skills skills = null;
		switch (team.getDifficulty()) {
		    case EASY:	 skills = Insect.Skills.MEDIUM;  break;
            case MEDIUM: skills = Insect.Skills.GOD;     break;
            case HARD:   skills = Insect.Skills.AMAZING; break;
		}
		AntSamantha samantha = new AntSamantha(curPos, team, skills);
		samantha.setRank(team.getRank());
		team.addInsect(samantha);
		return samantha;
    }
    
    
    /**
     * Find a random empty tile
     * @return the Point of the tile
     */
    private WorldPoint findEmptyTileNearAntHill() {
    	int x, radiusX;
        int y, radiusY;
        final int radius = 5;
        final World world = team.getWorld(); 
        int nbTries = 0;
        while (nbTries++ < 8)
        {
        	radiusX = Randomizer.getInt(2 * radius) - radius;
            radiusY = Randomizer.getInt(2 * radius) - radius;
            x = curPos.getX() + radiusX;
            y = curPos.getY() + radiusY;
            if (world.isPositionAvailable(x, y) && ! Values.isObstacle(world.getElement(x, y)))
                return world.getPoint(x, y);
        }
        return curPos;
    }   
	
    
    /**
     * Update the rank of the queen.
     */
	@Override public void setRank(final Rank rank) {
		this.rank = rank;
	}
}
