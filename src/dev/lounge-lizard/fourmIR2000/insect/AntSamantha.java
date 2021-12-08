package dev.lounge-lizard.fourmIR2000.insect;

import dev.lounge-lizard.fourmIR2000.insect.behaviors.BehaviorFactory;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;

/**
 * Ant controled by the player. In fact, this is like a soldier, but under our orders.
 */
public final class AntSamantha extends Insect {

	/** For serialization : version of the class */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new ant controled by the player
	 * @param curPos	position of the ant in the map
	 * @param team		team of the ant (basicaly : the first team)
	 * @param skills	skills of the insect
	 */
	public AntSamantha(WorldPoint curPos, Team team, Skills skills) {
		super(curPos, team);
		assignSkills(130, 6, skills);
	}


    /**
     * Give the picture of the insect
     * @return the current picture
     */
    @Override public Values getPicture() {
    	switch (dir) {
    	case LEFT:			return Values.antYellow_SL;
    	case LEFT_UP:		return Values.antYellow_SUL;
    	case UP:			return Values.antYellow_SU;
    	case UP_RIGHT:		return Values.antYellow_SUR;
    	case RIGHT:			return Values.antYellow_SR;
    	case RIGHT_DOWN:	return Values.antYellow_SDR;
    	case DOWN:			return Values.antYellow_SD;
    	default:			return Values.antYellow_SDL;
    	}
    }
	
	
	/**
	 * Normaly, when an ant reaches is destination, we choose another one (eg. when we have found
	 * food, we go back to the antHill). In this case, we don't want our samantha to go anywhere.
	 * So we override the method with almost empty code.
	 */
	@Override public void findNewDestination() {
         path = null;
		// DO NOTHING ! We do not want that Regina go anywhere when she meet the point
	}
	
	
	/**
     * Equivalent to a method live(). Give the instruction of what the
     * ant must do in the next turn
     */
	@Override public void getAction() {
        if (energy > 0) {
            if (needFood())
                askForFood(radiusFood);
            curBehavior = BehaviorFactory.getBehaviorWarrior();
            super.getAction();
		}
	}
	
    
    /**
     * Move in a random direction. If the destination is blocked, we try in few
     * others juste to be sure we can't do something.
     */
    @Override public void moveRandom() {
        move();
    }
    
	
	/**
	 * Called when an insect die.
     */
    @Override public void die() {
		super.die();
		team.getGame().createSamantha(true);
    }
}
