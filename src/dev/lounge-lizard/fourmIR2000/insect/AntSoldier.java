package dev.lounge-lizard.fourmIR2000.insect;

import dev.lounge-lizard.fourmIR2000.insect.behaviors.BehaviorFactory;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;


/**
 * Ant soldier. This is an ant with raised capabilities, like stronger or more resistant.
 */
public final class AntSoldier extends Insect {

    /** For serialization : version of the class */
    private static final long serialVersionUID = 1L;
    
    /**
     * Create a new ant soldier.
     * @param pos   position of the ant in the map
     * @param team  team of the ant
     * @param skills    skills of the insect
     */
    public AntSoldier(WorldPoint pos, Team team, Skills skills) {
        super(pos, team);
        assignSkills(110, 5, skills);
    }

    
    /**
     * Give the picture of the insect
     * @return the current picture
     */
    @Override public Values getPicture() {
        switch (team.getColor() % 4) {
        case 0:
            switch (dir) {
            case LEFT:          return Values.antBlack_SL;
            case LEFT_UP:       return Values.antBlack_SUL;
            case UP:            return Values.antBlack_SU;
            case UP_RIGHT:      return Values.antBlack_SUR;
            case RIGHT:         return Values.antBlack_SR;
            case RIGHT_DOWN:    return Values.antBlack_SDR;
            case DOWN:          return Values.antBlack_SD;
            default:            return Values.antBlack_SDL;
            }
        case 1:
            switch (dir) {
            case LEFT:          return Values.antRed_SL;
            case LEFT_UP:       return Values.antRed_SUL;
            case UP:            return Values.antRed_SU;
            case UP_RIGHT:      return Values.antRed_SUR;
            case RIGHT:         return Values.antRed_SR;
            case RIGHT_DOWN:    return Values.antRed_SDR;
            case DOWN:          return Values.antRed_SD;
            default:            return Values.antRed_SDL;
            }       
        case 2:
            switch (dir) {
            case LEFT:          return Values.antBrown_SL;
            case LEFT_UP:       return Values.antBrown_SUL;
            case UP:            return Values.antBrown_SU;
            case UP_RIGHT:      return Values.antBrown_SUR;
            case RIGHT:         return Values.antBrown_SR;
            case RIGHT_DOWN:    return Values.antBrown_SDR;
            case DOWN:          return Values.antBrown_SD;
            default:            return Values.antBrown_SDL;
            }
        case 3:
            switch (dir) {
            case LEFT:          return Values.antWhite_SL;
            case LEFT_UP:       return Values.antWhite_SUL;
            case UP:            return Values.antWhite_SU;
            case UP_RIGHT:      return Values.antWhite_SUR;
            case RIGHT:         return Values.antWhite_SR;
            case RIGHT_DOWN:    return Values.antWhite_SDR;
            case DOWN:          return Values.antWhite_SD;
            default:            return Values.antWhite_SDL;
            } 
        }
        return null;
    }
    

    /**
     * Equivalent to a method live(). Give the instruction of what the
     * insect must do in the next turn
     */
     @Override public void getAction() {
         loseEnergy();
         if (energy > 0) {
             if (needFood()) {
                 askForFood(radiusFood);
                 curBehavior = null;
             } else
                 curBehavior = BehaviorFactory.getBehaviorWarrior();
             super.getAction();
         }
    }
}
