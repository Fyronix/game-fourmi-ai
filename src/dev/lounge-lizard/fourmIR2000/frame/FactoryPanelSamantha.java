package dev.lounge-lizard.fourmIR2000.frame;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.lounge-lizard.fourmIR2000.frame.GameFrame.LblControls;
import dev.lounge-lizard.fourmIR2000.pictures.Values;

final class FactoryPanelSamantha {

	private FactoryPanelSamantha() {
		/* Nothing */
	}

	
	/**
	 * Create the game menu showing information on Samantha Religiosa
	 * @param panel		panel where we draw the current group
	 * @param tabLbl	container of labels components accessible on others classes
	 */
	public static void createMenu(final JPanel panel, final HashMap<LblControls, JLabel> tabLbl) {
		panel.setLayout(new FlowLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Samantha"));
		
		final JPanel rndMap = new JPanel(new GridBagLayout());
		FactoryFactory.addImage(rndMap, Values.antYellow_SL);
		FactoryFactory.addValue(rndMap, "Energy : ", tabLbl, LblControls.LBL_SamanthaEnergy);
		panel.add(rndMap);
	}
	
	
	/**
	 * Create the menu game indicating that Samantha Religiosa is dead
	 * @param panel		panel where we draw the menu
	 */
	public static void createMenuDeath(JPanel panel) {
		panel.setLayout(new GridLayout());	// FlowLayout to put the image on the top !
		panel.setBorder(BorderFactory.createTitledBorder("You're dead !"));
		try {
			final ImageIcon splash = new ImageIcon(ImageIO.read(FactoryPanelSamantha.class.getResourceAsStream("/resources/pictures/insects/dead.png")));
			final JLabel img = new JLabel(splash);
			panel.add(img);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
