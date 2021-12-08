package dev.lounge-lizard.fourmIR2000.insect;

import dev.lounge-lizard.fourmIR2000.insect.behaviors.BehaviorFactory;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;

public final class Bug extends Insect {
	
	/** For serialization : version of the class */
	private final static long serialVersionUID = 1L;
    
	
    /**
     * Create a new Bug. This is a very strong insect that just walk around,
     * bitting as many ennemies as possible. As it is very big, it walks slowly
     * @param curPos	the position of the ant
     * @param team		the team of the ant
     * @param skills	skills of the insect
     */
	public Bug(WorldPoint curPos, Team team, Skills skills) {
		super(curPos, team);
		assignSkills(400, 20, skills);
        speed = 4;
        radiusFight = 2;
	}

	
	/**
     * Give the picture of the insect
     * @return the current picture
     */
    @Override public Values getPicture() {
        switch (team.getColor() % 4) {
        case 0:
            switch (dir) {
            case LEFT:          return Values.antBlack_BL;
            case LEFT_UP:       return Values.antBlack_BUL;
            case UP:            return Values.antBlack_BU;
            case UP_RIGHT:      return Values.antBlack_BUR;
            case RIGHT:         return Values.antBlack_BR;
            case RIGHT_DOWN:    return Values.antBlack_BDR;
            case DOWN:          return Values.antBlack_BD;
            default:            return Values.antBlack_BDL;
            }
        case 1:
            switch (dir) {
            case LEFT:          return Values.antRed_BL;
            case LEFT_UP:       return Values.antRed_BUL;
            case UP:            return Values.antRed_BU;
            case UP_RIGHT:      return Values.antRed_BUR;
            case RIGHT:         return Values.antRed_BR;
            case RIGHT_DOWN:    return Values.antRed_BDR;
            case DOWN:          return Values.antRed_BD;
            default:            return Values.antRed_BDL;
            }       
        case 2:
            switch (dir) {
            case LEFT:          return Values.antBrown_BL;
            case LEFT_UP:       return Values.antBrown_BUL;
            case UP:            return Values.antBrown_BU;
            case UP_RIGHT:      return Values.antBrown_BUR;
            case RIGHT:         return Values.antBrown_BR;
            case RIGHT_DOWN:    return Values.antBrown_BDR;
            case DOWN:          return Values.antBrown_BD;
            default:            return Values.antBrown_BDL;
            }
        case 3:
            switch (dir) {
            case LEFT:          return Values.antWhite_BL;
            case LEFT_UP:       return Values.antWhite_BUL;
            case UP:            return Values.antWhite_BU;
            case UP_RIGHT:      return Values.antWhite_BUR;
            case RIGHT:         return Values.antWhite_BR;
            case RIGHT_DOWN:    return Values.antWhite_BDR;
            case DOWN:          return Values.antWhite_BD;
            default:            return Values.antWhite_BDL;
            } 
        }
        return null;
    }
	
    
	/**
	 * Equivalent to a method live(). Give the instruction of what the
	 * ant must do in the next turn
	 */
	@Override public void getAction() {
		if(energy >0) {	
	        if (needFood()) {
	            askForFood(radiusFood);
	            curBehavior = null;
	        } else 
	        curBehavior = BehaviorFactory.getBehaviorSentinel();
	        super.getAction();
		}
	}
}
