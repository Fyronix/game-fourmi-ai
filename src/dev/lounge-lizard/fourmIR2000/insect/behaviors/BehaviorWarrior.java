package dev.lounge-lizard.fourmIR2000.insect.behaviors;

import java.io.Serializable;
import java.util.LinkedList;

import dev.lounge-lizard.fourmIR2000.insect.Insect;
import dev.lounge-lizard.fourmIR2000.insect.Insect.Order;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;

public final class BehaviorWarrior extends Behavior implements Serializable {

    /** For serialization : version of the class */
    private static final long serialVersionUID = 1L;

    BehaviorWarrior() {
        /* nothing : it just avoids that non-package classes build a new object */
    }
    
    
    /**
     * Do the appropriated action for the insect
     * @param insect    the insect we work with
     * @return true if an action has been executed, else false
     */
    @Override public boolean doAction(final Insect insect) {      
        // If no vital action has been done
        if (! super.doAction(insect)) {
            // If we need food, we have to find food ourself whenever we can
            if (insect.needFood(2)) {
                insect.setOrder(Order.EAT);
                final WorldPoint ptFood = insect.findTheBestPointOfFood(1);
                if (ptFood != null) {
                    insect.setDirection(ptFood);
                    insect.move();
                } else
                    insect.moveRandom();
            } else {
            	final LinkedList<Insect> ennemy = insect.getTeam().getWorld().giveInsectAround(insect, 1, false);
                if (ennemy.size() != 0)
                    insect.fight(ennemy.get(0));
                else {
                	final WorldPoint refPoint = insect.getRefPoint();
                    if (refPoint != null)
                		insect.move();
                	else {
                		insect.setRefPoint(null);
                		insect.moveRandom();
                	}
                }
            }
        }
        return true;
    }
}
