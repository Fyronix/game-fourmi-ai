package dev.lounge-lizard.fourmIR2000.frame;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;

import dev.lounge-lizard.fourmIR2000.Game;
import dev.lounge-lizard.fourmIR2000.Game.Difficulty;
import dev.lounge-lizard.fourmIR2000.insect.Insect;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.fourmIR2000.world.World;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;
import dev.lounge-lizard.lawrence.GridPane;
import dev.lounge-lizard.lawrence.InputListener;
import dev.lounge-lizard.lawrence.impl.EnumImageProvider;

public final class GameFrame {

	/** Enumeration of all menuItems elements for what we could add a listener. */
	public static enum MnuControls {
		MNU_FileLevelNew, MNU_FileLevelOpen, MNU_FileLevelSave, MNU_FileGameOpen, MNU_FileGameSave, MNU_FileGameClose, MNU_FileQuit, MNU_Difficulty, MNU_About
	}

	/** Enumeration of all buttons / pictures for what we could add a listener. */
	public static enum BtnControls {
		BTN_MapPlain, BTN_MapMarsh, BTN_MapMountain, BTN_MapDesert, BTN_MapUserSelect, BTN_Grass, BTN_Desert, BTN_Water, BTN_Rock, BTN_Food, BTN_AntHill, BTN_AntHillEn, BTN_RemoveTop, BTN_Play
	}

	/** Enumeration of buttons that are disabled by default, and enabled with a new picture */
	public static enum GameControls {
		GAM_Bug, GAM_Kamikaze, GAM_Shield, GAM_Veteran
	}
	
	/** Enumeration of all scrollBars */
	static enum ScrollControls {
		SCR_Water, SCR_Food, SCR_Rock
	}
	
	/** Enumeration of all TextFields */
	static enum TextControls {
		TXT_SIZE_X_GAME, TXT_SIZE_Y_GAME
	}
	
	/** Enumeration of all Sliders */
	public static enum SlideControls {
		SLI_DESERT, SLI_WORKER, SLI_KNOWLEDGE
	}
	
	/** Enumeration of all JLabels */
	static enum LblControls {
		LBL_Food, LBL_Knowledge, LBL_SamanthaEnergy
	}
	
	
	/* CONSTANTS */
	/** Width of the frame */
	private final static int WIDTH_FRAME = 935;

	/** Height of the frame */
	private final static int HEIGHT_FRAME = 660;

	/** Size of the left menu (in pixels) */
	final static int LEFT_MENU_WIDTH = 140;

	/** maximal width of the playground */
	private final static int MAX_PLAY_X = 150;

	/** maximal height of the playground */
	private final static int MAX_PLAY_Y = 150;

	/** minimal width of the playground */
	private final static int MIN_PLAY_X = 20;

	/** minimal height of the playground */
	private final static int MIN_PLAY_Y = 20;

	/** default width of the playground */
	final static int DEFAULT_PLAY_X = 40;

	/** default height of the playground */
	final static int DEFAULT_PLAY_Y = 30;

	/** Minimal zoom of the tiles pictures */
	final static double MIN_ZOOM = 0.5;
	
	/** Maximal zoom of the tiles pictures */
	final static double MAX_ZOOM = 3;

	
	/* GRAPHICAL COMPONENTS */
	/** Graphic frame to use */
	private final JFrame frame;

	/** Graphic grid of the playground */
	private GridPane<Values> gridPane;

	/** Container for menu and playground */
	private final JSplitPane splitPane;

	/** Container of the GridPane */
	private JScrollPane gamePane;

	/** Menu used for the level editor */
	private final JPanel leftComponentEditor;

	/** Menu used for the game */
	private final JPanel leftComponentGame;

    /** Menu of the game : informations of the player's antHill */
    private final JPanel panelGameAntHill;
    
    /** Menu of the game : informations of Samantha */
    private final JPanel panelGameSamantha;
    
    /** Menu of the game : informations on the death of Samantha */
    private final JPanel panelGameSamanthaDead;
    
    /** Menu of the game : informations of the current group */
    private final JPanel panelGameGroup;
    
    /** List of all JMenuItem that can have an ActionListener on them */
    private final HashMap<MnuControls, JMenuItem> tabMnu;
    
    /** List of all buttons that can have an InputListener on them */
    private final HashMap<BtnControls, JLabel> tabBtn;
    
    /** List of all JScrollBar */
    private final HashMap<ScrollControls, JScrollBar> tabScroll;
    
    /** List of all JSlideBar */
    private final HashMap<SlideControls, JSlider> tabSlide;
    
    /** List of all JTextFields */
    private final HashMap<TextControls, JTextField> tabTxtField;
    
    /** List of all JLabels that must change during the game */
    private final HashMap<LblControls, JLabel> tabLbl;
    
    /** List of all special actions we have to unblock during the game.
     * The arrayList is in the form {enabledButton, disabledButton} */
    private final HashMap<GameControls, GameButton> tabAction;
    
    
	/* GAME ELEMENTS */
	/** Model of the world */
	private World world;

	/** Width of the playground */
	private final int widthPlayGround;

	/** height of the playground */
	private final int heightPlayGround;

	/** Width of the playground's tiles */
	private final int widthTiles;

	/** Height of the playground's tiles */
	private final int heightTiles;
	
	/** Speed of the game (in milliseconds) */
	private int gameSpeed;
	
	/** Zoom of the game */
	private double gameZoom;
	
	/** Difficulty of the game */
	private Difficulty gameDifficulty;
	
	
	/* OTHER VARIABLES */
	/** The object that could give us the pictures we need */
	private final EnumImageProvider<Values> imageProvider;

    
	/**
	 * Create a new game frame
	 * @param title    the caption of the frame
	 */
	public GameFrame(String title) {
		// Initialization of the object
		widthPlayGround = DEFAULT_PLAY_X;
		heightPlayGround = DEFAULT_PLAY_Y;
		imageProvider = new EnumImageProvider<Values>(Values.class);
		//gra = new FactoryGameFrame();
		tabMnu = new HashMap<MnuControls, JMenuItem>();
	    tabBtn = new HashMap<BtnControls, JLabel>();
	    tabScroll = new HashMap<ScrollControls, JScrollBar>(); 
	    tabTxtField = new HashMap<TextControls, JTextField>();
	    tabSlide = new HashMap<SlideControls, JSlider>();
	    tabLbl = new HashMap<LblControls, JLabel>();
	    tabAction = new HashMap<GameControls, GameButton>();
		gameSpeed = 100;
		gameZoom = 1;
		gameDifficulty = Difficulty.MEDIUM;


		// We get the playground tiles' size (we get the size of the grass element, assuming
		// that other tiles would have the same size !!)
		BufferedImage imgTest = FactoryGameFrame.giveImage(Values.grass);
		widthTiles = imgTest.getWidth();
		heightTiles = imgTest.getHeight();
		
		// Try to draw components in a native style
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { /* If we can't change the look, it isn't that important. We just continue */ }		

		// Initialization of the game playground
		frame = new JFrame(title);
		world = new World(widthPlayGround, heightPlayGround);
		gridPane = createGridPane(world);
		gamePane = new JScrollPane(gridPane);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		setZoom(gameZoom);
		
		// Initialization of the graphical elements that can have a listener on them
		leftComponentEditor = new JPanel(null);
		leftComponentGame = new JPanel(null);
        panelGameAntHill = new JPanel(null);
        panelGameSamantha = new JPanel(null);
        panelGameSamanthaDead = new JPanel(null);
        panelGameGroup = new JPanel(null);
        
		// We create all the graphical elements, and we prepare the frame for the level editor
		createGameFrame();
	}

	
	/**
	 * Give the current dimensions of a tile
	 * @return the point representign the size of the tiles
	 */
	public final WorldPoint getTilesSize() {
		return new WorldPoint(gridPane.getTileWidth(), gridPane.getTileHeight());
	}
	
	
	/**
	 * Change the zoom of the tiles. Admitted values are between MIN_ZOOM and MAX_ZOOM 
	 * @param zoom the zoom to assign
	 */
	void setZoom(final double zoom) {
		if (zoom < MIN_ZOOM || zoom > MAX_ZOOM)
			throw new IllegalArgumentException();
		gridPane.setTileDimension(new Double(widthTiles * zoom).intValue(), new Double(heightTiles * zoom).intValue());
		gridPane.revalidate();	// inform the JScrollPane that is size has changed
		gameZoom = zoom;
	}

	
	/**
	 * Build the graphical frame and return the reference.
	 */
	private void createGameFrame() {

		/* We begin by the main window */
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH_FRAME, HEIGHT_FRAME);
		frame.setLocationRelativeTo(null); // Center on screen

		/* Then we add the menus */
		frame.setJMenuBar(FactoryMenus.createMenus(this, tabMnu));

		/* And we prepare the game zone. It is separated in 2 parts : a menu on the left, and
		 * the main widow on the right. These 2 parts are divided by a JSplitPane */
		splitPane.setLeftComponent(null);
		splitPane.setRightComponent(gamePane);
		splitPane.setOneTouchExpandable(false);
		splitPane.setDividerSize(1);
		frame.add(splitPane);
		leftComponentGame.setLayout(new BoxLayout(leftComponentGame, BoxLayout.Y_AXIS));

		// We prepare the differents menus that could be put on the left
		FactoryPanelEditor.createMenu(leftComponentEditor, tabBtn, tabScroll, tabSlide, tabTxtField);	
        FactoryPanelAntHill.createMenu(panelGameAntHill, tabAction, tabLbl, tabSlide);
        FactoryPanelSamantha.createMenu(panelGameSamantha, tabLbl);
        FactoryPanelSamantha.createMenuDeath(panelGameSamanthaDead);

		// At the end, we display the window with the editor menu by default
        prepareGraphicForLevelEditor();
        frame.setVisible(true);
		frame.repaint();
	}

	
	/**
	 * Prepare the JFrame to use the level editor : we update menus and panels
	 * so that we can create / edit a level
	 */
	public void prepareGraphicForLevelEditor() {
		// We change the menuItems to disable unaccessible options
		tabMnu.get(MnuControls.MNU_FileGameSave).setEnabled(false);
		tabMnu.get(MnuControls.MNU_FileGameClose).setEnabled(false);
		tabMnu.get(MnuControls.MNU_FileLevelNew).setEnabled(true);
		tabMnu.get(MnuControls.MNU_FileLevelOpen).setEnabled(true);
		tabMnu.get(MnuControls.MNU_FileLevelSave).setEnabled(true);
		tabMnu.get(MnuControls.MNU_Difficulty).setEnabled(true);

		// We add it to the SplitPane, and we put it at the right size
		splitPane.setLeftComponent(leftComponentEditor);
		resizeMenu();
	}

	/**
	 * Prepare the frame to the game : we disable some menus, and we change the
	 * left panel
	 */
	public void prepareGraphicForGame() {
		// We change the menuItems to disable unaccessible options
		tabMnu.get(MnuControls.MNU_FileLevelNew).setEnabled(false);
		tabMnu.get(MnuControls.MNU_FileLevelOpen).setEnabled(false);
		tabMnu.get(MnuControls.MNU_FileLevelSave).setEnabled(false);
		tabMnu.get(MnuControls.MNU_FileGameSave).setEnabled(true);
		tabMnu.get(MnuControls.MNU_FileGameClose).setEnabled(true);
		tabMnu.get(MnuControls.MNU_Difficulty).setEnabled(false);

		// We add it to the SplitPane, and we put it at the right size
		splitPane.setLeftComponent(leftComponentGame);
		resizeMenu();
	}

	/**
	 * Resize the menu on the left of the game. We have to do this every time we
	 * switch between the game and the level editor
	 */
	public void resizeMenu() {
		splitPane.setDividerLocation(LEFT_MENU_WIDTH);
		final Dimension dim = new Dimension(LEFT_MENU_WIDTH, HEIGHT_FRAME);
		splitPane.setPreferredSize(dim);
		splitPane.setMinimumSize(dim);
		splitPane.setMaximumSize(dim);
	}

	/**
	 * Add an ActionListener to a specific menuItem of the frame
	 * @param component    the componant where we add the listener
	 * @param listener     the listener to add
	 */
	public void addActionListener(final MnuControls component, final ActionListener listener) {
		if (component == null || listener == null)
			throw new IllegalArgumentException();
		tabMnu.get(component).addActionListener(listener);
	}

	
	/**
	 * Add a MouseListener to a specific button of the frame 
	 * @param component    the componant where we add the listener
	 * @param listener     the listener to add
	 */
	public void addButtonListener(final BtnControls component, final MouseListener listener) {
		if (component == null || listener == null)
			throw new IllegalArgumentException();
		tabBtn.get(component).addMouseListener(listener);
	}

	
	/**
	 * Add a MouseListener to a specific GameButton of the frame 
	 * @param component    the button in witch we put the listener
	 * @param listener     the listener to add
	 */
	public void addGameButtonListener(final GameControls component, final MouseListener listener) {
		if (component == null || listener == null)
			throw new IllegalArgumentException();
		tabAction.get(component).updateMouseListener(listener);
	}
	
	
	/**
	 * Update the state of a game button
	 * @param control	the current button to modify
	 * @param state		true=enabled, else disabled
	 */
	public void updateGameButton(final GameControls control, final boolean state) {
		final GameButton btn = tabAction.get(control);
		btn.setEnabled(state);
	}
	
	/**
	 * Update the tooptip text of a game button when disabled
	 * @param control	the current button to modify
	 * @param tooltip	the new tooltip to show
	 * @param isForEnabled	to know if the text is applied for enbled or disabled button
	 */
	public void updateToolTip(final GameControls control, final String tooltip, final boolean isForEnabled) {
		final GameButton btn = tabAction.get(control);
		if(isForEnabled)
			btn.setEnabledTooltip(tooltip);
		else
		btn.setDisabledTooltip(tooltip);
	}
	
    /**
     * Add a ChangeListener to a slider of the frame.
     * @param component the JSlider component to use
     * @param listener the new Listener
     */
    public void addSliderListener(final SlideControls component, final ChangeListener listener) {
    	final JSlider slider = tabSlide.get(component);
        slider.addChangeListener(listener);
    }
    
    
	/**
	 * Add an InputListener to the game Grid panel
	 * @param listener     the listener to add
	 */
	public void addGridPaneListener(final InputListener listener) {
		if (listener == null)
			throw new IllegalArgumentException();
		gridPane.addInputListener(listener);
	}

	/**
	 * Remove an InputListener from the game Grid panel
	 * @param listener     the listener to remove
	 */
	public void removeInputListener(final InputListener listener) {
		if (listener == null)
			throw new IllegalArgumentException();
		gridPane.removeInputListener(listener);
	}


	/**
	 * Give the quantity of rocks in the game (in percents)
	 * @return the quantity
	 */
	public int getQtyOfRocks() {
		return tabScroll.get(ScrollControls.SCR_Rock).getValue();
	}

	
	/**
	 * Give the quantity of food in the game (in percents)
	 * @return the quantity
	 */
	public int getQtyOfFood() {
		return tabScroll.get(ScrollControls.SCR_Food).getValue();
	}

	
	/**
	 * Give the quantity of sand in the game (in percents)
	 * @return the quantity
	 */
	public int getQtyOfSand() {
		return tabSlide.get(SlideControls.SLI_DESERT).getValue();
	}
	
	
	/**
	 * Give the quantity of water in the game (in percents)
	 * @return the quantity
	 */
	public int getQtyOfWater() {
		return tabScroll.get(ScrollControls.SCR_Water).getValue();
	}
	
	
	/**
	 * Give the difficulty of the game (selected in the 'Difficulty' menu)
	 * @return the difficulty
	 */
	public Game.Difficulty getDifficulty() {
		return gameDifficulty;
	}

	
	/**
	 * Give the world associated at this frame.
	 * @return  the World element
	 */
	public World getWorld() {
		return world;
	}

	
	/**
	 * Give the game speed
	 * @return  the game speed
	 */
	public int getGameSpeed() {
		return gameSpeed;
	}
	
	/**
	 * Assign the new game speed
	 * @param newSpeed the new speed
	 */
	void setGameSpeed(int newSpeed) {
		gameSpeed = newSpeed;
	}
	
	
	/**
	 * Assign the new game difficulty
	 * @param newDifficulty the new difficulty
	 */
	void setGameDifficulty(Difficulty newDifficulty) {
		gameDifficulty = newDifficulty;
	}
	
	
	/**
	 * Assign a new world to the frame
	 * @param newWorld     the new world to assign
	 */
	public void assignNewWorld(final World newWorld) {
		world = newWorld;
		gridPane = createGridPane(newWorld);
		gamePane = new JScrollPane(gridPane);
		splitPane.setRightComponent(gamePane);
		setZoom(gameZoom);
		resizeMenu();
	}

	
	/**
	 * Create a new GridPane of the given world
	 * @param newWorld  the word to use as model
	 * @return the new GridPane
	 */
	private GridPane<Values> createGridPane(final World newWorld) {
		gridPane = new GridPane<Values>(newWorld.getModel(), imageProvider, widthTiles, heightTiles);
		return gridPane;
	}
	
	
	/**
	 * Give the title of the window
	 * @return the title
	 */
	public String getTitle() {
		return frame.getTitle();
	}

	/**
	 * Get the willing size of the map. If the coords are invalid, we inform the
	 * user and we return null.
	 * @return the maximal's coord point if it is valid, else null
	 */
	public WorldPoint getAndValidSizeMap() {
		int x = 0;
		int y = 0;
		String errorMessage = null;
		try {
			x = Integer.parseInt(tabTxtField.get(TextControls.TXT_SIZE_X_GAME).getText());
			y = Integer.parseInt(tabTxtField.get(TextControls.TXT_SIZE_Y_GAME).getText());
			if (x < MIN_PLAY_X || x > MAX_PLAY_X || y < MIN_PLAY_Y || y > MAX_PLAY_Y)
				errorMessage = "Incorrect dimensions. Accepted sizes are ("
					+ MIN_PLAY_X + "," + MIN_PLAY_Y + ") to (" + MAX_PLAY_X
					+ "," + MAX_PLAY_Y + ").";
		} catch (NumberFormatException nfe) {
			errorMessage = "The 'size' fields are incorrects.";
		}

		if (errorMessage != null) {
			SelectBuilder.showMessageAlert(getTitle(), errorMessage);
			return null;
		}
		return new WorldPoint(x, y);
	}
    
    
    /**
     * Change the left panel during the game, to show informations about the player's antHill
     */
    public void setLeftPanelAntHill() {
        setLeftPanelGame(panelGameAntHill);
    }

    
    /**
     * Change the left panel during the game, to show informations about the player's Samantha
     */
    public void setLeftPanelSamantha() {
        setLeftPanelGame(panelGameSamantha);
    }
    
    
    /**
     * Change the left panel during the game, to show that the player's samantha is dead
     */
    public void setLeftPanelSamanthaDead() {
        setLeftPanelGame(panelGameSamanthaDead);
    }
    
    
    /**
     * Change the left panel during the game, to show informations about selected insects
     */
    public void setLeftPanelGroup(final List<Insect> insGroup) {
    	FactoryPanelGroup.createMenu(panelGameGroup, insGroup);
        setLeftPanelGame(panelGameGroup);
    }

    
    /**
     * Update the left menu for the game
     * @param panel the panel to show
     */
    private void setLeftPanelGame(final JPanel panel) {
        leftComponentGame.removeAll();
        leftComponentGame.add(panel);
        resizeMenu();
        leftComponentGame.validate();
        leftComponentGame.repaint();
    }

    
    /**
     * Update the label giving the stock of food for the player's team
     * @param quantity the new quantity to show
     */
    public void updateFoodStock(final int quantity) {
    	tabLbl.get(LblControls.LBL_Food).setText(Integer.toString(quantity));
    }
    
    
    /**
     * Update the label giving the knowledges of the player's team
     * @param quantity the new quantity to show
     */
    public void updateKnowledges(final int quantity) {
    	tabLbl.get(LblControls.LBL_Knowledge).setText(Integer.toString(quantity));
    }
    
    
    /**
     * Update the lable giving the actual energy of Samantha, the ant controled by the player
     * @param energy the energy to show
     */
    public void updateEnergySamantha(final int energy) {
    	tabLbl.get(LblControls.LBL_SamanthaEnergy).setText(Integer.toString(energy));
    }
    

    /**
     * Add a listener to the GridPane (game or level editor)
     * @param listener the listener to add
     */
    public void addGridPaneListener(final MouseListener listener) {
    	gridPane.addMouseListener(listener);
    }
    
    
    /**
     * Remove a listener from the GridPane (game or level editor)
     * @param listener the listener to remove
     */
    public void removeGridPaneListener(final MouseListener listener) {
    	gridPane.removeMouseListener(listener);
    }
    
    
    /**
     * Add a listener indicating when the size of the frame change.
     * @param listener the listener to add
     */
    public void addResizeFrameListener(final ComponentListener listener) {
    	frame.addComponentListener(listener);
    }
    
  
    /**
     * Scroll the gamePane so that the specified point become visible
     * @param p the point to show
     */
    public void centerGamePaneAroundCoords(final WorldPoint p) {
    	final int x = p.getX() * widthTiles;
    	final int y = p.getY() * heightTiles;
    	final int decalX = 8 * widthTiles;
    	final int decalY = 8 * heightTiles;
    	final JViewport viewport = gamePane.getViewport();
        viewport.scrollRectToVisible(new Rectangle(x - decalX, y - decalY, widthTiles, heightTiles));
    }
    

    /**
     * Show the 'about' window
     */
    public void showAbout() {
    	AboutFrame.showAbout(frame, frame.getTitle());
    }
}
