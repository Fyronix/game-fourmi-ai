package dev.lounge-lizard.fourmIR2000.world;

public final class BadWorldFormatException extends Exception {

	/** For serialization : version of the class */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the exception
	 * @param msg the message to print on the exception
	 */
	BadWorldFormatException(String msg) {
		super(msg);
	}
}
