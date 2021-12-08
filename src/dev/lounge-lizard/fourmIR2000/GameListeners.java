package dev.lounge-lizard.fourmIR2000;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import dev.lounge-lizard.fourmIR2000.frame.GameFrame;
import dev.lounge-lizard.fourmIR2000.frame.SelectBuilder;
import dev.lounge-lizard.fourmIR2000.insect.Insect;
import dev.lounge-lizard.fourmIR2000.insect.Rank;
import dev.lounge-lizard.fourmIR2000.insect.Team;
import dev.lounge-lizard.fourmIR2000.insect.Insect.Order;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.world.Floor;
import dev.lounge-lizard.fourmIR2000.world.World;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;
import dev.lounge-lizard.lawrence.InputListener;
import dev.lounge-lizard.lawrence.Key;

/**
 * Listeners of the game.
 * This class dialog make the interface between the game and the graphical components
 * 
 * >> The visibility MUST BE 'PACKAGE', not 'PUBLIC' !!!
 */
final class GameListeners {

	/** Main listeners : level editor or game ? */
	enum MainLst {
		LST_Level, LST_Game
	}

	/** Mouse button used for selections */
	private final static int BTN_MOUSE_SELECT = MouseEvent.BUTTON1;
	
	/** Mouse button used for movements */
	private final static int BTN_MOUSE_MOVE = MouseEvent.BUTTON3;
	
	/** Game to work with */
	private final Game game;
	
	/** Graphical frame we will dialog with */
	private final GameFrame gameFrame;
	
	/** InputListener for the level editor */
	private InputListener inputEditor;
	
	private MouseAdapter gridPaneListener;
	
	/** For the creation of the map : current background element to use */
	private Values curBackElem;
	
	/** For the creation of the map : current element to use */
	private Values curElem;
	
	
	
	/**
	 * Create a new GameListener : this object dialog with the graphical window and assign / unassign listeners
	 * @param game		 the game object who launched us 
	 * @param gameFrame	the frame to dialog with
	 */
	GameListeners(Game game, GameFrame gameFrame) {
		this.game = game;
		this.gameFrame = gameFrame;
		inputEditor = null;
		gridPaneListener = null;
		curBackElem = Values.grass;
		curElem = null;
	}

	
	/**
	 * Part 0 of the game : initialization of all the listeners of the frame
	 * >> We are ready to build levels
	 */
	void initializeListeners() {
		// Exit menu
		gameFrame.addActionListener(GameFrame.MnuControls.MNU_FileQuit, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// New level
		gameFrame.addActionListener(GameFrame.MnuControls.MNU_FileLevelNew, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.defineNewWorld(Game.typeWorld.plain);
			}
		});

		// About
		gameFrame.addActionListener(GameFrame.MnuControls.MNU_About, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.setGamePaused(true);
				gameFrame.showAbout();
				game.setGamePaused(false);
			}
		});
		
        /* worlds pre-builds */
        gameFrame.addButtonListener(GameFrame.BtnControls.BTN_MapPlain, new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (game.confirmLostModifications())
                	game.defineNewWorld(Game.typeWorld.plain);
            }
        });
        gameFrame.addButtonListener(GameFrame.BtnControls.BTN_MapMarsh, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
                if (game.confirmLostModifications())
                	game.defineNewWorld(Game.typeWorld.marsh);
            }
        });
        gameFrame.addButtonListener(GameFrame.BtnControls.BTN_MapMountain, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
                if (game.confirmLostModifications())
                	game.defineNewWorld(Game.typeWorld.mountain);
            }
        });
        gameFrame.addButtonListener(GameFrame.BtnControls.BTN_MapDesert, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
                if (game.confirmLostModifications())
                	game.defineNewWorld(Game.typeWorld.desert);
            }
        });

		/* Random world (values = user select) */
		gameFrame.addButtonListener(GameFrame.BtnControls.BTN_MapUserSelect, new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if (game.confirmLostModifications()) {
					int prctDesert = gameFrame.getQtyOfSand();
					int prctWater = gameFrame.getQtyOfWater();
					int prctRocks = gameFrame.getQtyOfRocks();
					int prctFood = gameFrame.getQtyOfFood();
					game.defineNewWorld(prctDesert, prctWater, prctRocks, prctFood);
				}
			}
		});

		/* Modification 'by hand' of the world */
		gameFrame.addButtonListener(GameFrame.BtnControls.BTN_Grass, new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				curBackElem = Values.grass;
				curElem = null;
			}
		});
		gameFrame.addButtonListener(GameFrame.BtnControls.BTN_Desert, new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				curBackElem = Values.desert;
				curElem = null;
			}
		});
		gameFrame.addButtonListener(GameFrame.BtnControls.BTN_Water, new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				curBackElem = Values.water;
				curElem = null;
			}
		});
		gameFrame.addButtonListener(GameFrame.BtnControls.BTN_RemoveTop, new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				curBackElem = null;
				curElem = null;
			}
		});
        gameFrame.addButtonListener(GameFrame.BtnControls.BTN_Rock, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
        		curBackElem = null;
        		curElem = Values.rock;
            }
        });
        gameFrame.addButtonListener(GameFrame.BtnControls.BTN_Food, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
        		curBackElem = null;
                curElem = Values.food;
            }
        });
        gameFrame.addButtonListener(GameFrame.BtnControls.BTN_AntHill, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
        		curBackElem = null;
                curElem = Values.antHill;
            }
        });
        gameFrame.addButtonListener(GameFrame.BtnControls.BTN_AntHillEn, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
        		curBackElem = null;
                curElem = Values.antHill_enemy;
            }
        });

		// Listener for the level editor
		inputEditor = new InputListener() {
			public void keyTyped(int x, int y, Key key) {/* NOTHING */}

			public void mouseClicked(int x, int y, int button) {
				if (game.getWorld().isPositionAvailable(x, y)) {
					game.setLevelModified(true);
                    World world = game.getWorld();
                    WorldPoint p = world.getPoint(x, y);
                    WorldPoint antHill = game.getAntHill();
                    ArrayList<WorldPoint> antHillOpp = game.getAntHillOpp();
                
                    // If we have already an antHill in our color, we can't add 
                    // another one. So we remove the last before !
                    if (curElem == Values.antHill) {
                        if (antHill != null) {
                            world.setElement(antHill.getX(), antHill.getY(), Values.grass, true);
                            game.setAntHill(null);
                        }
                    }
                    // If on this position there is an antHill, we remove it from the list
                    if (world.getElement(x, y) == Values.antHill) {
                        world.setElement(x, y, null, true);
                        game.setAntHill(null);
                    } else if (world.getElement(x, y) == Values.antHill_enemy) {
                        world.setElement(x, y, null, true);
                        antHillOpp.remove(p);
                    }
                    // If we add an antHill, we add it to the arrays too
                    if (curElem == Values.antHill)
                        game.setAntHill(p);
                    else if (curElem == Values.antHill_enemy)
                        antHillOpp.add(p);
					// Anf finally we update the world with the new element
                    Values foreVal;
                    Values backVal;
                    if (curBackElem != null) {
                    	backVal = curBackElem;
                    	foreVal = world.getElement(x, y);
                    } else {
                    	backVal = world.getBackgroundElement(x, y);
                    	foreVal = curElem;
                    }
                    world.setElement(new Floor(foreVal, backVal, x, y), true);
				}
			}
		};

		// Listener for the game
		gridPaneListener = new MouseAdapter() {
			private WorldPoint pos1;
			private WorldPoint pos2;
			private WorldPoint getCoords(MouseEvent e) {
				// Variables
				WorldPoint p = gameFrame.getTilesSize();
				int x = e.getX() / p.getX();
				int y = e.getY() / p.getY();
				return game.getWorld().getPoint(x, y);
			}
			// When the mouse button is pressed (but not already released)
			@Override public void mousePressed(MouseEvent e) {
				if (e.getButton() == BTN_MOUSE_SELECT)
					pos1 = getCoords(e);
			}
			// When the mouse button is released
			@Override public void mouseReleased(MouseEvent e) {
				pos2 = getCoords(e);
				if (pos1 == null) pos1 = pos2;	// Avoid error when the mouse was pressed 'out' of the game and released 'in'
				switch (e.getButton())
				{
				// Selection button
				case BTN_MOUSE_SELECT:
					// Same tile at the begining ant at the end ? We show informations
					if (pos1.equals(pos2)) {
                        // Player's antHill ?
                        Team team = game.getWorld().getTeams().get(0);
                        if (team.getAntHill().equals(pos2))
                            gameFrame.setLeftPanelAntHill();
                        // Samantha (by default)
                        else
                            gameFrame.setLeftPanelSamantha();
                        game.getSamantha().getTeam().undoGroup();

                    // Different tiles ? We build a new group
					} else {
						List<Insect> insGroup = game.getSamantha().getTeam().createGroup(pos1, pos2);
						gameFrame.setLeftPanelGroup(insGroup);
					}
					break;
					
				// Move button
				case BTN_MOUSE_MOVE:
			       	Insect samantha = game.getSamantha();
                	samantha.setDirection(pos2);
					for(Insect insect : samantha.getTeam().getTabInsect())
                         if(insect.getIsInGroup() == true) {
                             insect.setDirection(pos2, Order.FOLLOW_GROUP, samantha);
                             insect.setRefPoint(pos2);
                         }
                     break;
				}
			}
		};
		
        // Listener on the antWorker percentage's JSlider
        gameFrame.addSliderListener(GameFrame.SlideControls.SLI_WORKER, new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int percent = source.getValue();
                    game.getWorld().getTeams().get(0).setPercentWorker(percent);
                }
            }
        });
        
        // Listener on the knowledges / food JSlider
        gameFrame.addSliderListener(GameFrame.SlideControls.SLI_KNOWLEDGE, new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int percent = source.getValue();
                    game.getWorld().getTeams().get(0).setPercentKnowledge(100 - percent);
                }
            }
        });
        
        // Listener for the 'Employ a bug' button
        gameFrame.addGameButtonListener(GameFrame.GameControls.GAM_Bug, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
                Team team = game.getWorld().getTeams().get(0);
                int food = team.getFoodStock();
                if (food > Team.FOOD_NEEDED_FOR_A_BUG) {
                    team.getMaster().addBug();
                    if (team.getFoodStock() < Team.FOOD_NEEDED_FOR_A_BUG) {
                    	gameFrame.updateGameButton(GameFrame.GameControls.GAM_Bug, false);
                    	game.desactivateBugButton();
                    }
                }
            }
        });

        // Listener for the 'Add a kamikaze' button
        gameFrame.addGameButtonListener(GameFrame.GameControls.GAM_Kamikaze, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
                Team team = game.getWorld().getTeams().get(0);
                int food = team.getFoodStock();
                if (food > Team.FOOD_NEEDED_FOR_A_KAMIKAZE) {
                	team.getMaster().addKamikaze();
                	if (team.getFoodStock() < Team.FOOD_NEEDED_FOR_A_KAMIKAZE) {
                		gameFrame.updateGameButton(GameFrame.GameControls.GAM_Kamikaze, false);
                		game.desactivateKamikazeButton();
                	}
                }
            }
        });
        
        // Listener for the 'Develop Shields' button
        gameFrame.addGameButtonListener(GameFrame.GameControls.GAM_Shield, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
                Team team = game.getWorld().getTeams().get(0);
                int food = team.getKnowledges();
                if (food > Team.FOOD_NEEDED_FOR_SHIELD) {
                	team.activateShield();
                	gameFrame.updateGameButton(GameFrame.GameControls.GAM_Shield, false);
                	gameFrame.updateToolTip(GameFrame.GameControls.GAM_Shield, "You have already developped shields", false);
                } else
                	SelectBuilder.showMessageInfo(gameFrame.getTitle(), "You don't have enough food to develop shields. Sorry !");
            }
        });
        
        // Listener for the 'Develop veterans' button
        gameFrame.addGameButtonListener(GameFrame.GameControls.GAM_Veteran, new MouseAdapter() {
        	@Override public void mouseClicked(MouseEvent e) {
                Team team = game.getWorld().getTeams().get(0);
                updateVeteransButton(team);
        	}
        });

        
		// Play menu
		gameFrame.addButtonListener(GameFrame.BtnControls.BTN_Play, new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				// Before we launch the game, we verify that we have at least one antHill of each sort
				if (game.getAntHill() == null || game.getAntHillOpp().isEmpty())
					SelectBuilder.showMessageInfo(gameFrame.getTitle(), "You must have at least one antHill of each sort !");
				else {
					// If all is good, we remove buttons and events and we indicate that we have finished
					game.setNewGame(true);
					game.setInitBreak(true);
				}
			}
		});

		// New level menu
		gameFrame.addActionListener(GameFrame.MnuControls.MNU_FileLevelNew, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (game.confirmLostModifications())
					game.defineNewWorld(Game.typeWorld.plain);
			}
		});

		// Load level menu
		gameFrame.addActionListener(GameFrame.MnuControls.MNU_FileLevelOpen, new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (!game.confirmLostModifications())
    				return;
    			if (game.loadStateFromFile(Game.TypeFile.LEVEL)) {
    				game.getAntHillsPositions();
    				game.getWorld().updateAllModel();
    			}
    		}
    	});

		// Save level menu
		gameFrame.addActionListener(GameFrame.MnuControls.MNU_FileLevelSave, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.saveStateToFile(Game.TypeFile.LEVEL);
			}
		});

		// Load game menu
		gameFrame.addActionListener(GameFrame.MnuControls.MNU_FileGameOpen, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.setGamePaused(true);
				if (game.loadStateFromFile(Game.TypeFile.GAME)) {
					game.getWorld().updateAllModel();
					game.setInitBreak(true);
					game.setGameBreak(true);
					game.setNewGame(false);
					game.getWorld().updateAllModel();
				}
				game.setGamePaused(false);
			}
		});

		// Save game menu
		gameFrame.addActionListener(GameFrame.MnuControls.MNU_FileGameSave, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.setGamePaused(true);
				game.saveStateToFile(Game.TypeFile.GAME);
				game.setGamePaused(false);
			}
		});

		// Close game menu
		gameFrame.addActionListener(GameFrame.MnuControls.MNU_FileGameClose, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.setGamePaused(true);
				if (!SelectBuilder.showMessageConfirm(gameFrame.getTitle(), "Your game will be lost if you not save before. Continue anyway ?")) {
					game.setGamePaused(false);
					return;
				}
				game.setGameBreak(true);
				gameFrame.prepareGraphicForLevelEditor();
				game.setGamePaused(false);
			}
		});
		
		// Frame resizing ?
        gameFrame.addResizeFrameListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				gameFrame.resizeMenu();
			}
        });
		
	}

	
	/**
	 * Replace the listener associated to the GridPane.
	 * If an old one was associated, we remove it
	 * @param listener the new listener
	 */
	void replaceMainListener(MainLst listener) {
		gameFrame.removeInputListener(inputEditor);
		gameFrame.removeGridPaneListener(gridPaneListener);
		if (listener == MainLst.LST_Level)
			gameFrame.addGridPaneListener(inputEditor);
		else
			gameFrame.addGridPaneListener(gridPaneListener);
	}

	
	/**
	 * Update the veteran button so that the player could buy the next element
	 * @param team	the player's team
	 */
	private void updateVeteransButton(Team team) {
		// Variables
		int needKnown = 0;
		int nextKnown = 0;
		Rank nextRank = null;
		String devel = null;
		String toolTip = null;
		
		// We get the actual variables
		int knowledge = team.getKnowledges();
		int food = team.getFoodStock();
		Rank rank = team.getRank();
		
		// If we are already generals, we do nothing
		if (rank == Rank.GENERAL)
			return;
		
		// We get all the information wee need
		switch(rank) {
		case PRIVATE:
			needKnown = Team.KNOWLEDGE_FOR_SERGEANTS;
			nextKnown = Team.KNOWLEDGE_FOR_CAPTAINS;
			nextRank = Rank.SERGEANT;
			devel = "captains";
			break;
		case SERGEANT:
			needKnown = Team.KNOWLEDGE_FOR_CAPTAINS;
			nextKnown = Team.KNOWLEDGE_FOR_MAJORS;
			nextRank = Rank.CAPTAIN;
			devel = "majors";
			break;
		case CAPTAIN:
			needKnown = Team.KNOWLEDGE_FOR_MAJORS;
			nextKnown = Team.KNOWLEDGE_FOR_COLONELS;
			nextRank = Rank.MAJOR;
			devel = "colonels";
			break;

		case MAJOR:
			needKnown = Team.KNOWLEDGE_FOR_COLONELS;
			nextKnown = Team.KNOWLEDGE_FOR_GENERALS;
			nextRank = Rank.COLONEL;
			devel = "generals";
			break;

		case COLONEL:
			needKnown = Team.KNOWLEDGE_FOR_GENERALS;
			nextRank = Rank.GENERAL;
			break;
		}

		// Not enough food or knowledges ? We exit
		if (food < Team.FOOD_NEEDED_FOR_VETERANS || knowledge < needKnown) {
			return;
		}

		if (devel == null)
			toolTip = "You reached the maximum. You can't develop this any further!";
		else
			toolTip = "Develop " + devel + ": $" + Team.FOOD_NEEDED_FOR_VETERANS + ", (K) " + nextKnown;
		gameFrame.updateToolTip(GameFrame.GameControls.GAM_Veteran, toolTip, true);
		gameFrame.updateToolTip(GameFrame.GameControls.GAM_Veteran, toolTip + " (unvailable)", false);
		gameFrame.updateGameButton(GameFrame.GameControls.GAM_Veteran, false);
		team.setFoodStock(food - Team.FOOD_NEEDED_FOR_VETERANS);
		team.setRank(nextRank);
		game.desactivateVeteranButton();
	}
}