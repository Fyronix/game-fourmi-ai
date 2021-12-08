package dev.lounge-lizard.fourmIR2000.insect.behaviors;

import java.io.Serializable;

import dev.lounge-lizardv.fourmIR2000.insect.Insect;
import dev.lounge-lizardv.fourmIR2000.insect.Insect.Order;
import dev.lounge-lizardv.fourmIR2000.pictures.Values;
import dev.lounge-lizardv.fourmIR2000.world.WorldPoint;

public final class BehaviorWorker extends Behavior implements Serializable {

    /** For serialization : version of the class */
    private static final long serialVersionUID = 1L;

    BehaviorWorker() {
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
        	final Order curOrder = insect.getOrder();
            Insect authorOrder = insect.getOrderAuthor();
            
            // If we had an order, but the other insect died before we can help, we cancel it
            if (curOrder != Order.NONE && authorOrder == null) {
                insect.setOrder(Order.NONE);
                insect.findNewDestination();
                insect.move();

            // If a friend asked for some help in a battle, we go help him
            } else if (curOrder == Order.FIGHT) {
                // If we are near the conflict, and if the ennemy is still alive, we join the fight
                    if (insect.isNearDestination())
                        insect.fight(insect.getOpponent());
                    else
                        insect.move();
            
            // If a friend asked for some food, we go help him.
            // If we have some food and are near the antHill, we give all to the Queen
            } else {
                int food = insect.getFood();
                final double distAntHill = insect.getPos().distance(insect.getTeam().getAntHill());
                if (curOrder == Order.GIVE_FOOD || (distAntHill < 2 && food > 0)) {
                    if ((distAntHill < 2 && food > 0) || insect.isNearDestination()) {
                        if (authorOrder == null && distAntHill < 2)
                            authorOrder = insect.getTeam().getMaster();
                        if (authorOrder != null) {
                        	final int foodLeft = authorOrder.eat(food);
                            insect.setFood(foodLeft);
                        }
                        insect.setOrder(Order.NONE);
                        insect.findNewDestination();
                    } else
                        insect.move();
                    
                // Otherwise, we just walk around, performing some actions if needed
                } else {
                    // If we are on a 'food' tile, we get some to stock
                	final WorldPoint curPos = insect.getPos();
                    if (insect.getTeam().getWorld().getElement(curPos.getX(), curPos.getY()) == Values.food) {
                        food = insect.getMaxFood();
                        if (curOrder == Order.EAT)
                            insect.setOrder(Order.NONE);
                        insect.findNewDestination();
                    }
                    // If we need food, we have to find food ourself whenever we can
                    if (insect.needFood(2) && curOrder == Order.NONE) {
                        insect.setOrder(Order.EAT);
                        final WorldPoint ptFood = insect.findTheBestPointOfFood(1);
                        if (ptFood != null) {
                            insect.setDirection(ptFood);
                        }
                    }
                    insect.move();
                }
            }
        }
        return true;
    }
}
