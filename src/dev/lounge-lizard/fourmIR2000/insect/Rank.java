
package dev.lounge-lizard.fourmIR2000.insect;

/**
 * Ranks of the insects
 */
public enum Rank {
	PRIVATE {
		@Override public void increaseExperience(Insect insect) {
			pvIncreaseExperience(insect, SERGEANT);
		}},
	SERGEANT {
		@Override public void increaseExperience(Insect insect) {
			pvIncreaseExperience(insect, CAPTAIN);
		}},
	CAPTAIN {
		@Override public void increaseExperience(Insect insect) {
			pvIncreaseExperience(insect, MAJOR);
		}},
	MAJOR {
		@Override public void increaseExperience(Insect insect) {
			pvIncreaseExperience(insect, COLONEL);
		}},
	COLONEL {
		@Override public void increaseExperience(Insect insect) {
			pvIncreaseExperience(insect, GENERAL);
		}},
	GENERAL {
		@Override public void increaseExperience(Insect insect) {
			// We do nothing because we can't increase experience
		}};
	
	
	/** Number of values before increasing the rank */
	private final static int NEXT_RANK = 5;
	

	/**
	 * Increase the experience of the insect
	 * @param insect	the insect to use
	 * @param nextRank	the next possible rank
	 */
	private static void pvIncreaseExperience(Insect insect, Rank nextRank) {
		insect.addStrength(Insect.STRENGTH_PER_FIGHT);
		insect.maxEnergy += Insect.STRENGTH_PER_FIGHT;
		int rank = insect.getNextRank();
		rank++;
		if(rank >= NEXT_RANK) {
			rank = 0;
			insect.rank = nextRank;
		}
		insect.setNextRank(rank);
	}
	
	
	/**
	 * Increase the experience of an insect.
	 * @param insect the insect to use
	 */
	public abstract void increaseExperience(Insect insect);
}