package dev.lounge-lizard.fourmIR2000;

import java.util.ArrayList;
import java.util.LinkedList;

import dev.lounge-lizard.fourmIR2000.ai.Intelligence;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame;
import dev.lounge-lizard.fourmIR2000.frame.SelectBuilder;
import dev.lounge-lizard.fourmIR2000.insect.Insect;
import dev.lounge-lizard.fourmIR2000.insect.Team;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.world.BadWorldFormatException;
import dev.lounge-lizard.fourmIR2000.world.World;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;

/**
 * Manage the game. This class give elements to build the map, then control the play.
 */
public final class Game {

	/** Differents types of world we could ask for in the level editor */
	static enum typeWorld {
		plain, marsh, mountain, desert, user
	}

	/** Difficulty level */
	public static enum Difficulty {
		EASY, MEDIUM, HARD
	}

	/** Type of file to load / save	*/
	static enum TypeFile {
		LEVEL, GAME
	}

	/** For serialization : version of the class */
	private final static long serialVersionUID = 1L;

	/** Map to use */
	private final World world;

	/** For the creation of the map : position of our antHill */
	private WorldPoint antHill;

	/** For the creation of the map : list of positions of other antHills */
	private final ArrayList<WorldPoint> antHillOpp;

	/** For the creation of the map : indicate if some change were made on the map */
	private boolean levelModified;

	/** For the creation of the map : indicate if we had finish to prepare the map */
	private boolean initBreak;

	/** For the game : boolean that indicate if we must stop the game or not */
	private boolean gameBreak;
	
	/** For the game : indicate that we are saving or loading a game, and that we must pause the current game */
	private boolean gamePaused;

	/** For the game initialization : indicate if the world is a new one or a loaded one */
	private boolean newGame;

	/** The game JFrame. We use it to assign listeners when we need them */
	private final GameFrame gameFrame;

	/** The GameListeners to work with. This object manage the listeners of the game */
	private final GameListeners gameListeners;
	
	/** Used for the wait of the game */
	private long startTime;

	/** The insect contr�led by the player */
	private Insect samantha;

	/** Number of teams still alive in the current game */
	private int nbOpponents;

	/** Indicate if the button 'develop veterans' is activated or not */ 
	private boolean isVeteransActivated;
	
	/** Indicate if the button 'develop a bug' is activated or not */
    private boolean isBugsActivated;
    
    /** Indicate if the button 'develop a kamikaze' is activated or not */
    private boolean isKamikazesActivated;
	
    /** Counter of the player's ant death */
    private int countDeath;
	
    
	/** 
	 * Constructor of a new game
	 * @param gameFrame    the graphical frame to use for the game
	 */
	public Game(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
		this.world = gameFrame.getWorld();
		antHill = null;
		antHillOpp = new ArrayList<WorldPoint>();
		gameBreak = initBreak = levelModified = gamePaused = false;
		newGame = true;
		samantha = null;
		nbOpponents = 0;
		countDeath = 0;
		isVeteransActivated = false;
		isBugsActivated = false;
		isKamikazesActivated = false;
		gameListeners = new GameListeners(this, gameFrame);
		gameListeners.initializeListeners();
	}

	/**
	 * Define a new random map for the current game
	 * @see #defineNewWorld(int, int, int, int)
	 * @param type Type of the world to generate
	 */
	@SuppressWarnings("incomplete-switch")
	void defineNewWorld(final typeWorld type) {
		// We get the world (WE DON'T DO the 'user' case)
		switch (type) {
			case plain:		defineNewWorld(0, 3, 5, 2);		break;
			case marsh:		defineNewWorld(0, 40, 5, 2);	break;
			case mountain: 	defineNewWorld(40, 1, 20, 2);	break;
			case desert:	defineNewWorld(90, 1, 15, 1);	break;
		}
	}

    /**
     * Generate a new world with user-select percentages
     * @param percentDesert percent of desert tiles in the map
     * @param percentWater  percent of water tiles in the map
     * @param percentRocks  % of rocks to have in the map
     * @param percentFood   % of food to have in the map
     */
	void defineNewWorld(final int percentDesert, final int percentWater, final int percentRocks, final int percentFood) {
		final WorldPoint maxPoint = gameFrame.getAndValidSizeMap();
		if (maxPoint == null)
			return;
		world.generateNewWorld(maxPoint.getX(), maxPoint.getY(), percentDesert, percentWater, percentRocks, percentFood);
		getAntHillsPositions();
		gameFrame.assignNewWorld(world);
		gameListeners.replaceMainListener(GameListeners.MainLst.LST_Level);
		levelModified = false;
		// We center the view around the player's antHill
		gameFrame.centerGamePaneAroundCoords(antHill);
	}

	/**
	 * Get the positions of the AntHills from the current 'world'
	 */
	void getAntHillsPositions() {
		antHill = null;
		antHillOpp.clear();
		for (int y = 0; y < world.getHeight(); ++y)
			for (int x = 0; x < world.getWidth(); ++x) {
				WorldPoint p = world.getPoint(x, y);
				if (world.getElement(x, y) == Values.antHill)
					antHill = p;
				else if (world.getElement(x, y) == Values.antHill_enemy)
					antHillOpp.add(p);
			}
	}

	
	/**
	 * Save the current game state (level or game) in a file
	 * @param typFile  king of file to save (level, game ?)
	 * @return true if the operation was a success
	 */
	boolean saveStateToFile(final TypeFile typFile) {
		final String file = SelectBuilder.showSelectFile( typFile == TypeFile.GAME ? "gam" : "lvl", false);
		if (file == null)
			return false;
		try {
			if (typFile == TypeFile.GAME)
				world.saveGame(file);
			else
				world.saveLevel(file);
		} catch (BadWorldFormatException bfe) {
			SelectBuilder.showMessageAlert(gameFrame.getTitle(),
				"Impossible to write on the "
				+ (typFile == TypeFile.GAME ? "game" : "level")
				+ " '" + file + "'." + "\nError : "
				+ bfe.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Load the current state (level or game) from a file
	 * @param typFile  kind of file to load (level, game ?)
	 * @return true if the operation was a success
	 */
	boolean loadStateFromFile(final TypeFile typFile) {
		final String file = SelectBuilder.showSelectFile( typFile == TypeFile.GAME ? "gam" : "lvl", true);
		if (file == null)
			return false;
		try {
			if (typFile == TypeFile.GAME) {
				world.loadGame(file);
			} else
				world.loadLevel(file);
		} catch (BadWorldFormatException bfe) {
			SelectBuilder.showMessageAlert(gameFrame.getTitle(),
				"The file '" + file + "' is not a valid "
				+ (typFile == TypeFile.GAME ? "game" : "level")
				+ ".\nError : " + bfe.getMessage());
			return false;
		}
		gameFrame.assignNewWorld(world);
		gameListeners.replaceMainListener(typFile == TypeFile.GAME ? GameListeners.MainLst.LST_Game : GameListeners.MainLst.LST_Level);
		return true;
	}

	
	/**
	 * Part 1 of the game : creation of the map. It ends when the boolean isInitBreak become true
	 */
	void launchEditor() {
		gameFrame.prepareGraphicForLevelEditor();
		initBreak = false;
		gameBreak = false;
		gameListeners.replaceMainListener(GameListeners.MainLst.LST_Level);
		defineNewWorld(typeWorld.plain);
	}

	/**
	 * Part 2 of the game : game itself. It ends when the boolean isGameBreak become true
	 */
	void launchGame() {
        // Variables
        int ancEnergySamantha = Integer.MIN_VALUE;
        int ancFoodStock = Integer.MIN_VALUE;
        int ancKnowledge = Integer.MIN_VALUE;
        boolean isShieldActivated = false;
        int nextKnowledgeVeteran = Team.KNOWLEDGE_FOR_SERGEANTS;
        
        // Initialization
		gameFrame.prepareGraphicForGame();
		isVeteransActivated = false;
		countDeath = 0;
		gameFrame.updateGameButton(GameFrame.GameControls.GAM_Bug, false);
		gameFrame.updateGameButton(GameFrame.GameControls.GAM_Kamikaze, false);
		gameFrame.updateGameButton(GameFrame.GameControls.GAM_Shield, false);
		gameFrame.updateGameButton(GameFrame.GameControls.GAM_Veteran, false);
		
		// If it is as new game, we add the teams and stock food positions
		nbOpponents = 0;
		if (newGame) {
			int nbColor = 0;
			world.clearTeams();
			world.addTeam(antHill, nbColor++, this);
			for (WorldPoint p : antHillOpp) {
				world.addTeam(p, nbColor++, this);
				nbOpponents++;
			}
			createSamantha(false);
			world.storeFood();
			// If it is a saved one, we reassign all the transient variables
		} else {
			for (Team t : world.getTeams()) {
				if (t.getMaster() != null)
					nbOpponents++;
				t.setGame(this);
				t.setWorld(world);
			}
			samantha = world.getTeams().get(0).getMaster().addSamantha();
		}
		
		LinkedList<Intelligence> IA = new LinkedList<Intelligence>();
		for(int i=1; i<world.getTeams().size(); i++) {
			IA.add(new Intelligence(world.getTeams().get(i), gameFrame));
		}

		// We update all the indicators and the eventsListener
		initBreak = false;
		gameBreak = false;
		gameListeners.replaceMainListener(GameListeners.MainLst.LST_Game);
		
		// And we launch the game
		while (!gameBreak) {

			// if we are not paused, we move all the insects of the game
			if (!gamePaused) {
				for(Intelligence intelligence : IA)
					intelligence.think();
				for (Team team : world.getTeams()) {
					/* BE CAREFUL : we CAN'T use an Iterator to get all the insects !!!
                     * >> If one of them die, his reference is 'squized' and the iterator generates a
					 * ConcurrentCallException because it looks at the wrong place for the next element.
                     * To avoid this, we take an array, so the reference of the insect is kept until the
					 * end of the turn.
					 */
					
					LinkedList<Insect> lst = team.getTabInsect();
					if (lst != null) {
						Insect[] ins = new Insect[lst.size()];
						ins = lst.toArray(ins);
						for (int i = 0; i < ins.length; ++i)
							if (ins[i] != null)
								ins[i].getAction();
					}
				}
				world.refresh();
            	
	            // We do some more actions, like "refresh the food stock information"
	            Team playerTeam = world.getTeams().get(0);
				int energySamantha = samantha.getEnergy();
	            int foodStock = playerTeam.getFoodStock();
	            int knowledge = playerTeam.getKnowledges();
	            if (ancFoodStock != foodStock) {
	                gameFrame.updateFoodStock(foodStock);
	                ancFoodStock = foodStock;
	            }
	            if (ancKnowledge != knowledge) {
	                gameFrame.updateKnowledges(knowledge);
	                ancKnowledge = knowledge;
	            }
	            if (ancEnergySamantha != energySamantha) {
	                gameFrame.updateEnergySamantha(energySamantha);
	                ancEnergySamantha = energySamantha;
	            }

	            // We activate gameButtons when we have enough knowledges in the player's team
	            if (knowledge >= Team.KNOWLEDGE_FOR_BUGS &&  playerTeam.getFoodStock() >= Team.FOOD_NEEDED_FOR_A_BUG && !isBugsActivated) {
	            	gameFrame.updateGameButton(GameFrame.GameControls.GAM_Bug, true);
	            	isBugsActivated = true;
	            }
	            if (knowledge >= Team.KNOWLEDGE_FOR_KAMIKAZES  && playerTeam.getFoodStock() >= Team.FOOD_NEEDED_FOR_A_KAMIKAZE && !isKamikazesActivated) {
	            	gameFrame.updateGameButton(GameFrame.GameControls.GAM_Kamikaze, true);
	            	isKamikazesActivated = true;
	            }
	            if (knowledge >= Team.KNOWLEDGE_FOR_SHIELDS && !isShieldActivated) {
	            	gameFrame.updateGameButton(GameFrame.GameControls.GAM_Shield, true);
	            	gameFrame.updateToolTip(GameFrame.GameControls.GAM_Shield, "You have already developped shields", false);
	            	isShieldActivated = true;
	            }
	            if (knowledge >= nextKnowledgeVeteran && playerTeam.getFoodStock() >= Team.FOOD_NEEDED_FOR_VETERANS && !isVeteransActivated) {
	            	isVeteransActivated = true;
	            	switch(playerTeam.getRank()) {
	            		case PRIVATE:	nextKnowledgeVeteran = Team.KNOWLEDGE_FOR_CAPTAINS;	break;
		            	case SERGEANT:	nextKnowledgeVeteran = Team.KNOWLEDGE_FOR_MAJORS;	break;
		            	case CAPTAIN:	nextKnowledgeVeteran = Team.KNOWLEDGE_FOR_COLONELS; break;
		            	case MAJOR:		nextKnowledgeVeteran = Team.KNOWLEDGE_FOR_GENERALS;	break;
		            	case COLONEL:	nextKnowledgeVeteran = Integer.MAX_VALUE;			break;
		            	case GENERAL:	nextKnowledgeVeteran = Integer.MAX_VALUE;			break;
	            	}
	           		gameFrame.updateGameButton(GameFrame.GameControls.GAM_Veteran, true);
	            }
	            
	            // And we desactivate them if the food sudently disapeared :)
	            if (isBugsActivated && playerTeam.getFoodStock() < Team.FOOD_NEEDED_FOR_A_BUG) {
	            	gameFrame.updateGameButton(GameFrame.GameControls.GAM_Bug, false);
	            	isBugsActivated = false;
	            }
	            if (!isShieldActivated && playerTeam.getFoodStock() < Team.FOOD_NEEDED_FOR_SHIELD) {
	            	gameFrame.updateGameButton(GameFrame.GameControls.GAM_Shield, false);
	            }
	            if (isKamikazesActivated && playerTeam.getFoodStock() < Team.FOOD_NEEDED_FOR_A_KAMIKAZE) {
	            	gameFrame.updateGameButton(GameFrame.GameControls.GAM_Kamikaze, false);
	            	isKamikazesActivated = false;
	            }
	            if (isVeteransActivated && playerTeam.getFoodStock() < Team.FOOD_NEEDED_FOR_VETERANS) {
	            	gameFrame.updateGameButton(GameFrame.GameControls.GAM_Veteran, false);
	            	isVeteransActivated = false;
	            }
	            	            
				wait(gameFrame.getGameSpeed());
			}
        }
	}

	/**
	 * Wait for tps miliseconds
	 * @param tps  delay to wait
	 */
	private void wait(final int tps) {
		if (startTime == 0) {
			this.startTime = System.currentTimeMillis();
		} else {
			try {
				final long timeToWait = tps - (System.currentTimeMillis() - startTime);
				if (timeToWait > 0) {
					Thread.sleep(timeToWait);
				}
			} catch (InterruptedException ex) {/* NOTHING */}
			this.startTime = System.currentTimeMillis();
		}
	}

	/**
	 * Give the current difficulty of the game
	 * @return the difficulty
	 */
	public Difficulty getDifficulty() {
		return gameFrame.getDifficulty();
	}


	/**
	 * Create a new Samantha Religiosa ant. This capacities are determined by
	 * the difficulty of the game
	 * @param isDeadBefore if true, we print a dialog box indicating that we are dead.
	 */
	public void createSamantha(final boolean isDeadBefore) {
		if (isDeadBefore) {
			gameFrame.setLeftPanelSamanthaDead();
			countDeath++;
		}
			final Team curTeam = world.getTeams().get(0);
		samantha = curTeam.getMaster().addSamantha();
	}

	/**
	 * Notify the Game when a master of a team is dead. If it is the player's
	 * team, he lose. Otherwise, if he killed all others masters, he win
	 * @param insect   the master that have just died.
	 */
	public void informDeadMasterOfTeam(final Insect insect) {
		if (!insect.equals(insect.getTeam().getMaster()) || gameBreak)
			return;
		if (insect.getTeam().getColor() == 0) {
			SelectBuilder.showMessageAlert(gameFrame.getTitle(), getEndGameMessage("Your queen is dead. You lose, sorry !"));
			gameBreak = true;
		} else {
			nbOpponents--;
			if (nbOpponents == 0) {
				SelectBuilder.showMessageInfo(gameFrame.getTitle(), getEndGameMessage("All the other queens are dead. You won !"));
				gameBreak = true;
			}
		}
	}

	
	/**
	 * Give some information on the game
	 * @param caption	the main message to return
	 * @return the end message
	 */
	private String getEndGameMessage(String caption) {
		return new StringBuilder(caption)
			.append(" For information, you have been killed ")
			.append(countDeath).append(countDeath <= 1 ? " time" : " times")
			.toString();
	}
	
	
	/**
	 * Show a confirm dialog box, asking the user if he really wants to
	 * continue, knowing that if he does, he lose the modifications he made on a level
	 * @return true if he agreed, else false
	 */
	boolean confirmLostModifications() {
		if (levelModified && !SelectBuilder.showMessageConfirm(gameFrame.getTitle(), "All change made will be lost. Continue anyway ?"))
			return false;
		return true;
	}
	
	
	/**
	 * Give the current World element
	 * @return  the World element
	 */
	World getWorld() {
		return world;
	}
	
	/**
	 * Give the player's antHill position
	 * @return  the position
	 */
	WorldPoint getAntHill() {
		return antHill;
	}
	
	/**
	 * Give the opponents' antHill positions
	 * @return the list of positions
	 */
	ArrayList<WorldPoint> getAntHillOpp() {
		return antHillOpp;
	}
	
	/**
	 * Give a reference to Samantha
	 * @return  Samantha
	 */
	Insect getSamantha() {
		return samantha;
	}

	/**
	 * Indicate if the game is paused or not
	 * @return  true if the game is paused
	 */
	boolean getGamePaused() {
		return gamePaused;
	}
	
	/**
	 * Pause / 'unpause' the game
	 */
	void setGamePaused(final boolean state) {
		gamePaused = state;
	}
	
	/**
	 * Indicate if a level has been modified or not
	 * @param state   if 'true' the the level has been modified
	 */
	void setLevelModified(final boolean state) {
		levelModified = state;
	}

	/**
	 * Indicate when the initialisation part of the game is over
	 * @return  true when it is over
	 */
	boolean getInitBreak() {
		return initBreak;
	}
	
	/**
	 * Indicate the end of the level editor 
	 * @param state  if 'true', the the level editor just finished
	 */
	void setInitBreak(final boolean state) {
		initBreak = state;
	}

	/**
	 * Indicate when the game is over
	 * @return  true when the game is over
	 */
	boolean getGameBreak() {
		return gameBreak;
	}

	/**
	 * Indicate the end of the current game
	 * @param state  if 'true' the the game just finished
	 */
	void setGameBreak(final boolean state) {
		gameBreak = state;
	}
	
	/** 
	 * Determine if the game to launch is a new or a saved one
	 * @param state  if 'true' then it is a new game. Else it is a saved one
	 */
	void setNewGame(final boolean state) {
		newGame = state;
	}

    public void setAntHill(final WorldPoint antHill) {
        this.antHill = antHill;
    }

    
    /**
     * Desactivate the 'developp veterans' button
     */
	void desactivateVeteranButton() {
		this.isVeteransActivated = false;
	}
 
	
	/**
     * Desactivate the 'developp a bug' button
     */
	void desactivateBugButton() {
		this.isBugsActivated = false;
	}
	
	
	/**
     * Desactivate the 'developp a kamikaze' button
     */
	void desactivateKamikazeButton() {
		this.isKamikazesActivated = false;
	}
	
}