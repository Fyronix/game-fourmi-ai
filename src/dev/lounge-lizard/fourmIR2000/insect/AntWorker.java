package dev.lounge-lizard.fourmIR2000.insect;

import java.util.EnumSet;

import dev.lounge-lizard.fourmIR2000.insect.behaviors.BehaviorFactory;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;

/**
 * Ant by default. This is the equivalent of the Worker. It searchs for food and give it to
 * ants that have no energy
 */
public final class AntWorker extends Insect {

    /** For serialization : version of the class */
    private static final long serialVersionUID = 1L;

    /** true when there is no food on the map */
    private boolean noFoodOnMap;
    
    /**
     * Create a new worker ant. This is a weak ant, that only try to give
     * as food as possible at the queen
     * @param curPos    the position of the ant
     * @param team      the team of the ant
     * @param skills    skills of the insect
     */
    public AntWorker(WorldPoint curPos, Team team, Skills skills) {
        super(curPos, team);
        assignSkills(100, 1, skills);
        maxFood = 100;
        noFoodOnMap = false;   
    }

    
    /**
     * Give the picture of the insect
     * @return the current picture
     */
    @Override public Values getPicture() {
        switch (team.getColor() % 4) {
        case 0:
            switch (dir) {
            case LEFT:          return Values.antBlack_WL;
            case LEFT_UP:       return Values.antBlack_WUL;
            case UP:            return Values.antBlack_WU;
            case UP_RIGHT:      return Values.antBlack_WUR;
            case RIGHT:         return Values.antBlack_WR;
            case RIGHT_DOWN:    return Values.antBlack_WDR;
            case DOWN:          return Values.antBlack_WD;
            default:            return Values.antBlack_WDL;
            }
        case 1:
            switch (dir) {
            case LEFT:          return Values.antRed_WL;
            case LEFT_UP:       return Values.antRed_WUL;
            case UP:            return Values.antRed_WU;
            case UP_RIGHT:      return Values.antRed_WUR;
            case RIGHT:         return Values.antRed_WR;
            case RIGHT_DOWN:    return Values.antRed_WDR;
            case DOWN:          return Values.antRed_WD;
            default:            return Values.antRed_WDL;
            }       
        case 2:
            switch (dir) {
            case LEFT:          return Values.antBrown_WL;
            case LEFT_UP:       return Values.antBrown_WUL;
            case UP:            return Values.antBrown_WU;
            case UP_RIGHT:      return Values.antBrown_WUR;
            case RIGHT:         return Values.antBrown_WR;
            case RIGHT_DOWN:    return Values.antBrown_WDR;
            case DOWN:          return Values.antBrown_WD;
            default:            return Values.antBrown_WDL;
            }
        case 3:
            switch (dir) {
            case LEFT:          return Values.antWhite_WL;
            case LEFT_UP:       return Values.antWhite_WUL;
            case UP:            return Values.antWhite_WU;
            case UP_RIGHT:      return Values.antWhite_WUR;
            case RIGHT:         return Values.antWhite_WR;
            case RIGHT_DOWN:    return Values.antWhite_WDR;
            case DOWN:          return Values.antWhite_WD;
            default:            return Values.antWhite_WDL;
            } 
        }
        return null;
    }
    
    
    /**
     * Give the picture of the insect, followed by his actions pictures. For exemple,
     * the picture showing that he is fighting or that he need food
     * @return the list of pictures
     */
    @Override public EnumSet<Values> getPictures() {
    	final EnumSet<Values> elems = super.getPictures();
        if (food > 0)
            elems.add(Values.ins_food);
        return elems;
    }
    
    
    /**
     * Indicate if a worker got food for others ants or not
     * @return true or false
     */
    protected boolean hasFood() {
        return food != 0;
    }
    

    /**
     * Find a new random destination for the insect. This method can be
     * overriden and set to do nothing for insects we want to control by
     * ourselves (for example, player's insect)
     */
    @Override public void findNewDestination() {
        // If we have a stock of food, we want to put it in the antHill
        if (food != 0)
            setDirection(team.getAntHill());
        
        // Otherwise, we try to get some food (if any)
        else {
        	final WorldPoint ptFood = findTheBestPointOfFood(team.nbOfAntWorkerInTeam() / 2);
            if (ptFood != null)
                setDirection(ptFood);
            else {
                noFoodOnMap = true;
                super.findNewDestination();
            }
        }
    }

    
    /**
     * Equivalent to a method live(). Give the instruction of what the insect
     * must do in the next turn.
     */
    @Override public void getAction() {
        loseEnergy();
        if (energy > 0) {
            if (needFood()) {
                askForFood(radiusFood);
                curBehavior = null;
            } else
                curBehavior = BehaviorFactory.getBehaviorWorker();
            super.getAction();
        }
    }
    
 
    /**
     * Called when an insect move to go somewhere
     */
    @Override public void move() {
        // If there is no food on the map, and if we were asked to find some, we just walk around
        if (noFoodOnMap && order == Order.GIVE_FOOD) {
            order = Order.NONE;
            moveRandom();
        } else {
            super.move();
            if (foundNothingToDo == true)
            	findNewDestination();
        }
    }
}
