package dev.lounge-lizard.fourmIR2000.world;

import java.util.HashMap;


/** Enum containing all the elements that could be add on a level file */
public enum StorableWorldElem {
	@CharAscii(' ') GRASS,
	@CharAscii('o') GRASS_FOOD,
	@CharAscii('O') GRASS_ROCK, 
	@CharAscii('1') GRASS_ANTHILL, 
	@CharAscii('2') GRASS_ANTHILL_EN,
	@CharAscii('.') DESERT,
	@CharAscii('*') DESERT_FOOD,
	@CharAscii('#') DESERT_ROCK,
	@CharAscii('3') DESERT_ANTHILL,
	@CharAscii('4') DESERT_ANTHILL_EN,
	@CharAscii('~') WATER;

	
	/** Map allowing to retrieve a StorableWorldElem when we have the associated char */
	private static HashMap<Character, StorableWorldElem> mapAscii;

	/* Initialization of the map */
	static {
		mapAscii = new HashMap<Character, StorableWorldElem>();
		for(StorableWorldElem elem : StorableWorldElem.class.getEnumConstants()) {
			Character curChar = null;
			try {
				curChar = elem.getClass().getField(elem.name()).getAnnotation(CharAscii.class).value();
			} catch (Exception e) {
				curChar = ' ';
			}
			mapAscii.put(curChar, elem);
		}
	}
	
	
	 /**
     *  Give the ascii char associated with the StorableWorldElem
     * @param elem the element to check
     * @return the corresponding char, or ' ' if it was not found
     */
	public static char getCharValue(StorableWorldElem elem) {
		Character curChar = null;
		try {
			curChar = elem.getClass().getField(elem.name()).getAnnotation(CharAscii.class).value();
		} catch (Exception e) {
			curChar = ' ';
		}
		return curChar;
	}
	
	
	/**
	 * Give the StorableWorldElem associated to an ascii char
	 * @param elem the elem to check
	 * @return the associated StorableWorldElem
	 */
	public static StorableWorldElem getValueChar(char elem) {
		return mapAscii.get(Character.valueOf(elem));
	}
}
