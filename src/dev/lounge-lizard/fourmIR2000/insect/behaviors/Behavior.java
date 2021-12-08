package dev.lounge-lizard.fourmIR2000.insect.behaviors;

import dev.lounge-lizard.fourmIR2000.insect.Insect;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;


/**
 * Comportments of the insects
 */
public abstract class Behavior {
    
    /**
     * Do the appropriated action for the insect
     * @param insect    the insect we work with
     * @return true if an action has been executed, else false
     */
    public boolean doAction(final Insect insect) {
        // If we are fighting, we continue
        if (insect.isFighting()) {
            insect.fight(insect.getOpponent());
            insect.askForHelpToFight();
            return true;
        }
        
        // If we are on a food point, we pickup some
        final WorldPoint curPos = insect.getPos();
        if (insect.getTeam().getWorld().getElement(curPos.getX(), curPos.getY()) == Values.food)
            insect.eat();
        
        // Then, we do future actions only if it is time to walk
        int delay = insect.getTimeBeforeActing();
        insect.setTimeBeforeActing(--delay);
        if (delay > 0)
            return true;
        insect.setTimeBeforeActing(insect.getSpeed());

        return false;
    }
}
