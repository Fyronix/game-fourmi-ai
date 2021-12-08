package dev.lounge-lizard.fourmIR2000.insect.behaviors;

import java.io.Serializable;

import dev.lounge-lizard.fourmIR2000.insect.Insect;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;

public final class BehaviorGroup extends Behavior implements Serializable {

    /** For serialization : version of the class */
    private static final long serialVersionUID = 1L;

    BehaviorGroup() {
        /* nothing */
    }
    
    
    /**
     * Do the appropriated action for the insect
     * @param insect    the insect we work with
     * @return true if an action has been executed, else false
     */
    @Override public boolean doAction(final Insect insect) {
      
        // If no vital action has been done
        if (! super.doAction(insect)) {
        	final WorldPoint refPoint = insect.getRefPoint();
            if (refPoint != null)
                insect.move();
        }
        return true;
    }
}
