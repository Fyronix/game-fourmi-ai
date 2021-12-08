package dev.lounge-lizard.fourmIR2000.frame;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JTextField;

import dev.lounge-lizard.fourmIR2000.frame.GameFrame.BtnControls;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.ScrollControls;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.SlideControls;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.TextControls;
import fr.umlv.fourmIR2000.pictures.Values;

final class FactoryPanelEditor {

	private FactoryPanelEditor() {
		/* Nothing */
	}

	
	/**
	 * Create the level editor menu
	 * @param panel		  panel where we add controls
	 * @param tabBtn	  container of buttons components accessible on others classes
	 * @param tabScroll	  container of scrollbars components accessible on others classes
	 * @param tabSlide	  container of sliders components accessible on others classes
	 * @param tabTxtField container of textFields components accessible on others classes
	 */
	public static void createMenu(final JPanel panel, 
			final HashMap<BtnControls, JLabel> tabBtn,
			final HashMap<ScrollControls, JScrollBar> tabScroll,
			final HashMap<SlideControls, JSlider> tabSlide,
			final HashMap<TextControls, JTextField> tabTxtField) {

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		final JPanel addElems = new JPanel(new GridLayout(0, 3));
		addElems.setBorder(BorderFactory.createTitledBorder("Add elements"));
		addElems.add(FactoryGameFrame.createPicture(Values.edit_elem_grass, true, "Add grass", tabBtn, BtnControls.BTN_Grass));
		addElems.add(FactoryGameFrame.createPicture(Values.edit_elem_sand, true, "Add sand", tabBtn, BtnControls.BTN_Desert));
		addElems.add(FactoryGameFrame.createPicture(Values.edit_elem_water, true, "Add water", tabBtn, BtnControls.BTN_Water));
		addElems.add(FactoryGameFrame.createPicture(Values.edit_elem_food, true, "Add food", tabBtn, BtnControls.BTN_Food));
		addElems.add(FactoryGameFrame.createPicture(Values.edit_elem_rock, true, "Add rock", tabBtn, BtnControls.BTN_Rock));
		addElems.add(FactoryGameFrame.createPicture(Values.edit_elem_remove, true, "Remove top object", tabBtn, BtnControls.BTN_RemoveTop));
		addElems.add(FactoryGameFrame.createPicture(Values.edit_elem_hill, true, "Place your anthill", tabBtn, BtnControls.BTN_AntHill));
		addElems.add(FactoryGameFrame.createPicture(Values.edit_elem_hill_opp, true, "Place other anthills", tabBtn, BtnControls.BTN_AntHillEn));
		panel.add(addElems);
		
		final JPanel predefWorld = new JPanel(new GridLayout(0, 3));
		predefWorld.setBorder(BorderFactory.createTitledBorder("Quick maps"));
		predefWorld.add(FactoryGameFrame.createPicture(Values.edit_plain, true, "Plain, with lots of grass", tabBtn, BtnControls.BTN_MapPlain));
		predefWorld.add(FactoryGameFrame.createPicture(Values.edit_marsh, true, "Marh, with lots of water and grass", tabBtn, BtnControls.BTN_MapMarsh));
		predefWorld.add(FactoryGameFrame.createPicture(Values.edit_mountain, true, "Mountain, arid and rocky", tabBtn, BtnControls.BTN_MapMountain));
		predefWorld.add(FactoryGameFrame.createPicture(Values.edit_desert, true, "Desert, very arid and rocky", tabBtn, BtnControls.BTN_MapDesert));		
		panel.add(predefWorld);
		
		final JPanel userMap = new JPanel(null);
		userMap.setLayout(new BoxLayout(userMap, BoxLayout.Y_AXIS));
		userMap.setBorder(BorderFactory.createTitledBorder("Random map"));
		final JPanel userContent = new JPanel(new GridLayout());
		final JPanel rndMap = new JPanel(new GridBagLayout());
		FactoryFactory.addSlideBar(rndMap, "Grass", "Sand", 0, 100, 25, 25, tabSlide, SlideControls.SLI_DESERT);
		FactoryFactory.addValueChooser(rndMap, "Max X:", GameFrame.DEFAULT_PLAY_X, tabTxtField, TextControls.TXT_SIZE_X_GAME);
		FactoryFactory.addValueChooser(rndMap, "Max Y:", GameFrame.DEFAULT_PLAY_Y, tabTxtField, TextControls.TXT_SIZE_Y_GAME);
		FactoryFactory.addBar(rndMap, Values.edit_elem_water_mini, 0, 35, 5, 5, tabScroll, ScrollControls.SCR_Water);
		FactoryFactory.addBar(rndMap, Values.edit_elem_rock_mini, 0, 30, 5, 5, tabScroll, ScrollControls.SCR_Rock);
		FactoryFactory.addBar(rndMap, Values.edit_elem_food_mini, 0, 30, 1, 5, tabScroll, ScrollControls.SCR_Food);
		FactoryFactory.addButton(rndMap, Values.edit_user, "Generate your world", tabBtn, BtnControls.BTN_MapUserSelect);
		userContent.add(rndMap);
		userMap.add(userContent);
		panel.add(userMap);
		panel.add(Box.createVerticalGlue());
		panel.add(FactoryGameFrame.createPicture(Values.edit_play, true, "Launch the game !", tabBtn, BtnControls.BTN_Play));
		panel.add(Box.createVerticalGlue());
	}
}
