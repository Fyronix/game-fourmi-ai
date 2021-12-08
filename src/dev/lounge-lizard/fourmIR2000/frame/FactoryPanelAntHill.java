package dev.lounge-lizard.fourmIR2000.frame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import dev.lounge-lizard.fourmIR2000.frame.GameFrame.GameControls;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.LblControls;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.SlideControls;
import dev.lounge-lizard.fourmIR2000.insect.Team;
import dev.lounge-lizard.fourmIR2000.pictures.Values;

final class FactoryPanelAntHill {

	private FactoryPanelAntHill() {
		/* Nothing */
	}

	
	/**
	 * Create the game menu showing information on the player's antHill
	 * @param panel		panel where we add controls
	 * @param tabAction container of GameButtons components accessible on others classes
	 * @param tabLbl	container of JLabel components accessible on others classes
	 * @param tabSlide	container of JSlider components accessible on others classes
	 */
	public static void createMenu(final JPanel panel, 
			final HashMap<GameControls, GameButton> tabAction,
			final HashMap<LblControls, JLabel> tabLbl,
			final HashMap<SlideControls, JSlider> tabSlide ) {

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder("Your AntHill"));
		
		final JPanel content = new JPanel(new GridBagLayout());
		content.setMaximumSize(new Dimension(100, 500));
		FactoryFactory.addImage(content, Values.antHill);
		FactoryFactory.addValue(content, "Energy : ", tabLbl, LblControls.LBL_SamanthaEnergy);
		FactoryFactory.addSlideBar(content, "Soldiers", "Workers", 0, 100, 25, 25, tabSlide, SlideControls.SLI_WORKER);
		FactoryFactory.addSlideBar(content, "Science", "Food", 0, 100, 25, 25, tabSlide, SlideControls.SLI_KNOWLEDGE);
		FactoryFactory.addValue(content, "Food :", tabLbl, LblControls.LBL_Food);
		FactoryFactory.addValue(content, "Knowledge :", tabLbl, LblControls.LBL_Knowledge);
		panel.add(content);
		
		panel.add(Box.createVerticalGlue());
		
		final JPanel specActions = new JPanel(new FlowLayout());
		specActions.setMaximumSize(new Dimension(GameFrame.LEFT_MENU_WIDTH - 10, panel.getMaximumSize().height));
		specActions.setBorder(BorderFactory.createTitledBorder("Special actions"));
		String devShield = "Develop shields: $" + Team.FOOD_NEEDED_FOR_SHIELD + ", (K) " + Team.KNOWLEDGE_FOR_SHIELDS;
		String devKamikaze = "Build a kamikaze: $" + Team.FOOD_NEEDED_FOR_A_KAMIKAZE + ", (K) " + Team.KNOWLEDGE_FOR_KAMIKAZES;
		String devVeteran = "Develop sergeants: $" + Team.FOOD_NEEDED_FOR_VETERANS + ", (K) " + Team.KNOWLEDGE_FOR_SERGEANTS;
		String devBug = "Employ a bug: $" + Team.FOOD_NEEDED_FOR_A_BUG + ", (K) " + Team.KNOWLEDGE_FOR_BUGS;
		specActions.add(FactoryGameFrame.createGameButton(Values.buyShield, Values.buyShieldGray, devShield, devShield + " (unavailable)", tabAction, GameControls.GAM_Shield));
		specActions.add(FactoryGameFrame.createGameButton(Values.buyKamikaze, Values.buyKamikazeGray, devKamikaze, devKamikaze + " (unavailable)", tabAction, GameControls.GAM_Kamikaze));
		specActions.add(FactoryGameFrame.createGameButton(Values.buyVeteran, Values.buyVeteranGray, devVeteran, devVeteran + " (unavailable)", tabAction, GameControls.GAM_Veteran));
		specActions.add(FactoryGameFrame.createGameButton(Values.buyBug, Values.buyBugGray, devBug, devBug + " (unavailable)", tabAction, GameControls.GAM_Bug));
		panel.add(specActions);
		
		panel.add(Box.createVerticalGlue());
		panel.add(Box.createVerticalGlue());
		panel.add(Box.createVerticalGlue());
	}
}
