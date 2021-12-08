package dev.lounge-lizard.fourmIR2000.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import dev.lounge-lizard.fourmIR2000.Game;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.MnuControls;

final class FactoryMenus {

	/** Group for 'speed' elements */
	private static final ButtonGroup groupSpeed;
		
	/** Group for 'difficulty' elements */
	private static final ButtonGroup groupDifficulty;
	
	/** Group for 'zoom' elements */
	private static final ButtonGroup groupZoom;
	
	static {
		groupSpeed = new ButtonGroup();
		groupDifficulty = new ButtonGroup();
		groupZoom = new ButtonGroup();
	}
	
	
	private FactoryMenus() {
		/* Nothing */
	}
	

	/**
	 * 
	 * @param gameFrame		the gameFrame we work with
	 * @param tabMnu		container of JMenu components accessible on others classes
	 * @return the menuBar
	 */
	public static JMenuBar createMenus(final GameFrame gameFrame, final HashMap<MnuControls, JMenuItem> tabMnu) {
		
		// File menu
		final JMenu mnuFile = FactoryGameFrame.createMenu("File", KeyEvent.VK_F);
		mnuFile.add(createMenuItem("New Level", "file_level_new.png", KeyEvent.VK_N, ActionEvent.CTRL_MASK, tabMnu, MnuControls.MNU_FileLevelNew));
		mnuFile.add(createMenuItem("Open Level", "file_level_load.png", KeyEvent.VK_O, ActionEvent.CTRL_MASK, tabMnu, MnuControls.MNU_FileLevelOpen));
		mnuFile.add(createMenuItem("Save Level", "file_level_save.png", KeyEvent.VK_S, ActionEvent.CTRL_MASK, tabMnu, MnuControls.MNU_FileLevelSave));
		mnuFile.add(new JSeparator());
		mnuFile.add(createMenuItem("Open Game", "file_game_load.png", KeyEvent.VK_F2, 0, tabMnu, MnuControls.MNU_FileGameOpen));
		mnuFile.add(createMenuItem("Save Game", "file_game_save.png", KeyEvent.VK_F3, 0, tabMnu, MnuControls.MNU_FileGameSave));
		mnuFile.add(createMenuItem("Close Game", "file_game_close.png", KeyEvent.VK_F4, 0, tabMnu, MnuControls.MNU_FileGameClose));
		mnuFile.add(new JSeparator());
		mnuFile.add(createMenuItem("Quit", "file_exit.png", KeyEvent.VK_F4, ActionEvent.CTRL_MASK, tabMnu, MnuControls.MNU_FileQuit));
		
		// Options menu...
		final JMenu mnuOpts = FactoryGameFrame.createMenu("Options", KeyEvent.VK_O);
		
		// Zoom
		final JMenu mnuZoom= FactoryGameFrame.createMenu("Zoom", KeyEvent.VK_Z);
		mnuZoom.setIcon(getMenuIcon("zoom.png"));
		for (double i = GameFrame.MIN_ZOOM; i < GameFrame.MAX_ZOOM; i += .25)
			mnuZoom.add(createMnuRadioZoom(gameFrame, "Ratio " + i + " : 1", i == 1 ? true : false, i));
		mnuOpts.add(mnuZoom);
		mnuOpts.add(new JSeparator());

		// Speed
		final JMenu mnuSpeed = FactoryGameFrame.createMenu("Speed", KeyEvent.VK_S);
		mnuSpeed.setIcon(getMenuIcon("speed.png"));
		mnuSpeed.add(createMnuRadioSpeed(gameFrame, "Slow", KeyEvent.VK_F10, false, 300));
		mnuSpeed.add(createMnuRadioSpeed(gameFrame, "Normal", KeyEvent.VK_F11, false, 200));
		mnuSpeed.add(createMnuRadioSpeed(gameFrame, "Fast", KeyEvent.VK_F12, true, 100));
		mnuOpts.add(mnuSpeed);
		mnuOpts.add(new JSeparator());
		
		// Difficulty
		final JMenu mnuDiff = FactoryGameFrame.createMenu("Difficulty", KeyEvent.VK_D);
		mnuDiff.setIcon(getMenuIcon("difficulty.png"));
		mnuDiff.add(createMnuRadioDiff(gameFrame, "Easy", KeyEvent.VK_E, false, Game.Difficulty.EASY));
		mnuDiff.add(createMnuRadioDiff(gameFrame, "Medium", KeyEvent.VK_M, true, Game.Difficulty.MEDIUM));
		mnuDiff.add(createMnuRadioDiff(gameFrame, "Hard", KeyEvent.VK_H, false, Game.Difficulty.HARD));
		tabMnu.put(MnuControls.MNU_Difficulty, mnuDiff);
		mnuOpts.add(mnuDiff);
		
		// About menu
		final JMenu mnuAbout = FactoryGameFrame.createMenu("?", 0);
		mnuAbout.add(createMenuItem("About", "about.png", KeyEvent.VK_F1, 0, tabMnu, MnuControls.MNU_About));
		
		// MenuBar
		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(mnuFile);
		menuBar.add(mnuOpts);
		menuBar.add(mnuAbout);
		return menuBar;
	}

	
	/**
	 * Create a MenuItem for 'difficulty' selection 
	 * @param caption the caption of the item
	 * @param key the shortcut key
	 * @param isSelected true if the item must me selected by default (one per group)
	 * @param difficulty the willing difficulty of the game
	 * @return the JRadioButtonMenuItem created
	 */
	private static JRadioButtonMenuItem createMnuRadioDiff(final GameFrame gameFrame, final String caption, final int key, final boolean isSelected, final Game.Difficulty difficulty) {
		final JRadioButtonMenuItem item = createMenuRadio(caption, key, groupDifficulty, isSelected);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameFrame.setGameDifficulty(difficulty);
			}
		});
		return item;
	}
	
	
	/**
	 * Create a MenuItem for 'zoom' selection 
	 * @param caption the caption of the item
	 * @param isSelected true if the item must me selected by default (one per group)
	 * @param zoom the willing zoom
	 * @return the JRadioButtonMenuItem created
	 */
	private static JRadioButtonMenuItem createMnuRadioZoom(final GameFrame gameFrame, final String caption, final boolean isSelected, final double zoom) {
		final JRadioButtonMenuItem item = createMenuRadio(caption, 0, groupZoom, isSelected);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameFrame.setZoom(zoom);
			}
		});
		return item;
	}
	
	
	/**
	 * Create a MenuItem for 'speed' selection 
	 * @param caption the caption of the item
	 * @param key the shortcut key
	 * @param isSelected true if the item must me selected by default (one per group)
	 * @param speed the willing speed (in milliseconds) of the game
	 * @return the JRadioButtonMenuItem created
	 */
	private static JRadioButtonMenuItem createMnuRadioSpeed(final GameFrame gameFrame, final String caption, final int key, final boolean isSelected, final int speed) {
		final JRadioButtonMenuItem item = createMenuRadio(caption, key, groupSpeed, isSelected);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameFrame.setGameSpeed(speed);
			}
		});
		return item;
	}

	
	/**
	 * Create a new item for a menu. You can add an icon and a keyboard
	 * shortcut. If you don't want them, just putt null or '0' (for int values)
	 * @param caption  		the caption to print on the item
	 * @param namePicture 	the icon to show before the text
	 * @param evtKey   		what shortcut key must we assign ?
	 * @param evtMod   		the modifier (Alt, Ctrl, ...) to use with the key
	 * @param map	   	   if not null, we assign the component in the map with the key 'control' 
	 * @param control  	   if  not null, used as a key to assign the component in the map
	 * @return the new menu item
	 */
	private static JMenuItem createMenuItem(final String caption, final String namePicture, final int evtKey, final int evtMod, final HashMap<MnuControls, JMenuItem> map, final GameFrame.MnuControls control) {
		JMenuItem mnuItem;
		final ImageIcon icon = getMenuIcon(namePicture);
		mnuItem = (icon == null) ? new JMenuItem(caption) : new JMenuItem(caption, icon);

		if (evtKey != 0)
			mnuItem.setAccelerator(KeyStroke.getKeyStroke(evtKey, evtMod));
		if (map != null && control != null) 
			map.put(control, mnuItem);
		return mnuItem;
	}

	/**
	 * Create a new menu item with a radio button. You can add a keyboard
	 * shortcut. If you don't want to, just put '0' instead.
	 * @param caption      the caption of the menu
	 * @param evtKey       the eventual keyboard shortcut
	 * @param group        the group to link the radioButton with
	 * @param isSelected   true if this element is shecked by default
	 * @return the new radioButtonMenuItem
	 */
	private static JRadioButtonMenuItem createMenuRadio(final String caption, final int evtKey, final ButtonGroup group, final boolean isSelected) {
		final JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(caption);
		rbMenuItem.setActionCommand(caption);
		rbMenuItem.setSelected(isSelected);
		if (evtKey != 0)
			rbMenuItem.setMnemonic(evtKey);
		group.add(rbMenuItem);
		return rbMenuItem;
	}

	
	/**
	 * Return the icon associated with the name.
	 * Images are checked only in the 'menu pictures' folder
	 * @param name	the name of the icon to load
	 * @return the icon, or null if nothing was found
	 */
	private static ImageIcon getMenuIcon(String name) {
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(ImageIO.read(AboutFrame.class.getResourceAsStream("/resources/pictures/menu/" + name)));
		} catch (Exception e) {/* nothing */}
		return icon;
	}
}
