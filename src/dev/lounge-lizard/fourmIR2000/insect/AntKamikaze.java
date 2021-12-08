package dev.lounge-lizard.fourmIR2000.insect;

import dev.lounge-lizard.fourmIR2000.insect.behaviors.BehaviorFactory;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;


/**
 * Ant soldier. This is an ant with raised capabilities, like stronger or more resistant.
 */
public final class AntKamikaze extends Insect {

    /** For serialization : version of the class */
    private static final long serialVersionUID = 1L;
    
    /**
     * Create a new ant soldier.
     * @param pos   position of the ant in the map
     * @param team  team of the ant
     * @param skills    skills of the insect
     */
    public AntKamikaze(WorldPoint pos, Team team, Skills skills) {
        super(pos, team);
        assignSkills(110, 2, skills);
    }

    
    /**
     * Give the picture of the insect
     * @return the current picture
     */
    @Override public Values getPicture() {
        switch (team.getColor() % 4) {
        case 0:
            switch (dir) {
            case LEFT:          return Values.antBlack_KL;
            case LEFT_UP:       return Values.antBlack_KUL;
            case UP:            return Values.antBlack_KU;
            case UP_RIGHT:      return Values.antBlack_KUR;
            case RIGHT:         return Values.antBlack_KR;
            case RIGHT_DOWN:    return Values.antBlack_KDR;
            case DOWN:          return Values.antBlack_KD;
            default:            return Values.antBlack_KDL;
            }
        case 1:
            switch (dir) {
            case LEFT:          return Values.antRed_KL;
            case LEFT_UP:       return Values.antRed_KUL;
            case UP:            return Values.antRed_KU;
            case UP_RIGHT:      return Values.antRed_KUR;
            case RIGHT:         return Values.antRed_KR;
            case RIGHT_DOWN:    return Values.antRed_KDR;
            case DOWN:          return Values.antRed_KD;
            default:            return Values.antRed_KDL;
            }       
        case 2:
            switch (dir) {
            case LEFT:          return Values.antBrown_KL;
            case LEFT_UP:       return Values.antBrown_KUL;
            case UP:            return Values.antBrown_KU;
            case UP_RIGHT:      return Values.antBrown_KUR;
            case RIGHT:         return Values.antBrown_KR;
            case RIGHT_DOWN:    return Values.antBrown_KDR;
            case DOWN:          return Values.antBrown_KD;
            default:            return Values.antBrown_KDL;
            }
        case 3:
            switch (dir) {
            case LEFT:          return Values.antWhite_KL;
            case LEFT_UP:       return Values.antWhite_KUL;
            case UP:            return Values.antWhite_KU;
            case UP_RIGHT:      return Values.antWhite_KUR;
            case RIGHT:         return Values.antWhite_KR;
            case RIGHT_DOWN:    return Values.antWhite_KDR;
            case DOWN:          return Values.antWhite_KD;
            default:            return Values.antWhite_KDL;
            } 
        }
        return null;
    }

    
    /**
     * Kill ennemies with a suicide : this projects a lots of poison on everybody
     * @param   radius the radius to inspect for explosion
     */
    private void explose(final int radius) {
        // If the kamikaze exploses, it poisons all the ennemies around
        for(Insect insect : team.getWorld().giveInsectAround(this, radius, false))
            insect.setPoisoned(true);
        // Then we die
        die();
    }

    
    /**
     * Equivalent to a method live(). Give the instruction of what the
     * insect must do in the next turn
     */
     @Override public void getAction() {
         loseEnergy();
         if (energy > 0) {
             if (team.getWorld().giveInsectAround(this, 1, false).size() > 0)
                 explose(4);
             else {
                 if (needFood()) {
                     askForFood(radiusFood);
                     curBehavior = null;
                 } else 
                     curBehavior = BehaviorFactory.getBehaviorSentinel();
                 super.getAction();
             }
         }
    }
}
