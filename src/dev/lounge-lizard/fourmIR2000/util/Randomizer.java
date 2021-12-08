package dev.lounge-lizard.fourmIR2000.util;

import java.util.Random;

public final class Randomizer {

	/** 'Random' object to use */
	private static Random random;
	
	static {
		random = new Random();
	}
	
	private Randomizer() {
		/* nothing */
	}

	/**
	 * Give a randomized number between 0 and 1
	 * @return the double number generated
	 */
	public static double getDouble() {
		return random.nextDouble();
	}
	
	/**
	 * Give a randomized number between min and max
	 * @return the double number generated, min if min>max or min<0 or max<0
	 */
	public static double getRangeDouble(double min, double max) {
		return (min>=0 && max>=0 && min < max) 
			? (max - min)*random.nextDouble() + min
			: min;
	}
	
	/**
	 * Give a randomized number between 0 (inclusive) and the number in parameter (exclusive)
	 * @return the integer generated
	 */
	public static int getInt(int n) {
		return random.nextInt(n);
	}
}
