package dev.lounge-lizard.fourmIR2000.insect.behaviors;

public final class BehaviorFactory {
    
	/** Only instance of Worker behavior */
    private final static BehaviorWorker worker;
    
    /** Only instance of Warrior behavior */
    private final static BehaviorWarrior warrior;
    
    /** Only instance of Sentinel behavior */
    private final static BehaviorSentinel sentinel;
    
    /** Only instance of Group behavior */
    private final static BehaviorGroup group;
    
    
    static {
        worker= new BehaviorWorker();
        warrior = new BehaviorWarrior();
        sentinel = new BehaviorSentinel();
        group = new BehaviorGroup();
    }
    
    
    /**
     * Default constructor. This does nothing. It just avoids that non-package
     * classes build a new object.
     */
    private BehaviorFactory() {
    	/* nothing */
    }
    
    
    /**
     * Give the BehaviorWarrior object
     * @return the behavior
     */
    public static Behavior getBehaviorWarrior() {
        return warrior;
    }
    
    
    /**
     * Give the BehaviorWorker object
     * @return the behavior
     */
    public static Behavior getBehaviorWorker() {
        return worker;
    }
    
    
    /**
     * Give the BehaviorSentinel object
     * @return the behavior
     */
    public static Behavior getBehaviorSentinel() {
        return sentinel;
    }
    
    
    /**
     * Give the BehaviorGroup object
     * @return the behavior
     */
    public static Behavior getBehaviorGroup() {
        return group;
    }
}
