package dev.lounge-lizard.fourmIR2000.insect.behaviors;

import java.io.Serializable;
import java.util.LinkedList;

import dev.lounge-lizard.fourmIR2000.insect.Insect;
import dev.lounge-lizard.fourmIR2000.world.World;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;

public final class BehaviorSentinel extends Behavior implements Serializable {

    /** For serialization : version of the class */
    private static final long serialVersionUID = 1L;

    BehaviorSentinel() {
        /* nothing : it just avoids that non-package classes build a new object */
    }
    
    @Override public boolean doAction(final Insect insect) {

        // If no vital action has been done
        if (! super.doAction(insect)) {
            
            // If we found an ennemy just near us, we fight him
        	final World world = insect.getTeam().getWorld();
            LinkedList<Insect> ennemy = world.giveInsectAround(insect, 1, false);
            if (ennemy.size() != 0)
                insect.fight(ennemy.get(0));
            
            // Else, we search for an ennemy around
            else {
                ennemy = world.giveInsectAround(insect, 5, false);
                final WorldPoint refPoint = insect.getRefPoint();
                if (refPoint != null) {
	                // If we found one, we go in its direction, but only if we are not too far
	                // of our reference point
	                if (ennemy.size() != 0) {
	                    boolean found = false;
	                    int i = 0;
	                    while (i < ennemy.size() && !found) {
	                        Insect opponent = ennemy.get(i);
	                        if (opponent.getPos().distance(refPoint) <5) {
	                            insect.setDirection(opponent.getPos());
	                            insect.move();
	                            found = true;
	                        } else
	                            i++;
	                    }
	                // If there is nobody, we return to our reference point
	                } else
	                    insect.setDirection(refPoint);
	                insect.move();
                }
            }
        }
        return true;
    }
}
