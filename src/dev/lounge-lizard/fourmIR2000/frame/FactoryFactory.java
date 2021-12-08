package dev.lounge-lizard.fourmIR2000.frame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JTextField;

import dev.lounge-lizard.fourmIR2000.frame.GameFrame.BtnControls;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.LblControls;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.ScrollControls;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.SlideControls;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.TextControls;
import dev.lounge-lizard.fourmIR2000.pictures.Values;

/**
 * This class helps other Factorys to build common components
 */
final class FactoryFactory {

	private FactoryFactory() {
		/* Nothing */
	}


	/**
	 * Add to 'panel' a label describing something, and its associated value.
	 * @param panel		panel where we add controls
	 * @param caption	the description of the value
	 * @param tabLbl	if not null, we assign the component in the map with the key 'control' 
	 * @param control	if not null, used as a key to assign the component in the map
	 */
	static void addValue(final JPanel panel, final String caption, final HashMap<LblControls, JLabel> tabLbl, final LblControls control) {
		final JLabel def = FactoryGameFrame.createLabel(caption, null, false, null, null);
		final JLabel val = FactoryGameFrame.createLabel("", Color.RED, true, tabLbl, control);
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 2, 5);
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(def, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		panel.add(val, gbc);
	}
	
	
	/**
	 * Add to 'panel' a centered image
	 * @param panel		panel where we add the control
	 * @param value 	the image to add
	 */
	static void addImage(final JPanel panel, final Values value) {
		final JLabel btn = FactoryGameFrame.createPicture(value, true, "", null, null);
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 2, 5);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(btn, gbc);
	}
	
	
	/**
	 * Add to 'panel' an image and a progressBar
	 * @param panel			panel where we add controls
	 * @param value			the picture to draw
	 * @param min			minimal value of the bar
	 * @param max			maximal value of the bar
	 * @param begin			starting value of the bar
	 * @param decal			space between ticks of the bar
	 * @param tabScroll		if not null, we assign the component in the map with the key 'control'
	 * @param control		if not null, used as a key to assign the component in the map
	 */
	static void addBar(final JPanel panel, final Values value, final int min, final int max, final int begin, final int decal, final HashMap<ScrollControls, JScrollBar> tabScroll, final ScrollControls control) {
		final JLabel pict = FactoryGameFrame.createPicture(value, true, null, null, null);
		final JScrollBar scroll = FactoryGameFrame.createHorizScrollBar(min, max, begin, decal, tabScroll, control);
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 2, 5);
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(pict, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		panel.add(scroll, gbc);
	}
	

	/**
	 * Add to 'panel' a centered button
	 * @param panel		panel where we add the control
	 * @param value		the picture to draw
	 * @param caption	the tooltip text on the button, if any
	 * @param tabBtn	if not null, we assign the component in the map with the key 'control' 
	 * @param control	if not null, used as a key to assign the component in the map
	 */
	static void addButton(final JPanel panel, final Values value, final String caption, final HashMap<BtnControls, JLabel> tabBtn, final BtnControls control) {
		final JLabel btn = FactoryGameFrame.createPicture(value, true, caption, tabBtn, control);
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 2, 5);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(btn, gbc);
	}
	
	
	/**
	 * Add to 'panel' a slide bar with 2 captions for each extremity
	 * @param panel			panel where we add controls
	 * @param captionLeft	lower left caption
	 * @param captionRight	lower right caption
	 * @param min			minimal value of the bar
	 * @param max			maximal value of the bar
	 * @param decal			space between ticks
	 * @param start			starting value of the bar
	 * @param tabSlide		if not null, we assign the component in the map with the key 'control' 
	 * @param control		if not null, used as a key to assign the component in the map
	 */
	static void addSlideBar(final JPanel panel, final String captionLeft, final String captionRight, final int min, final int max, final int decal, final int start, final HashMap<SlideControls, JSlider> tabSlide, final SlideControls control) {
		final JSlider slide = FactoryGameFrame.createSlider(min, max, decal, start, captionLeft, captionRight, tabSlide, control);
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 2, 5);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		panel.add(slide, gbc);
	}

	
	/**
	 * Add to 'panel' textField with a label describing the field.
	 * @param panel			panel where we add controls
	 * @param caption		caption describing the text
	 * @param defaultValue	default value of the textField
	 * @param tabTxtField	if not null, we assign the component in the map with the key 'control' 
	 * @param control		if not null, used as a key to assign the component in the map
	 */
	static void addValueChooser(final JPanel panel, final String caption, final int defaultValue, final HashMap<TextControls, JTextField> tabTxtField, final TextControls control) {
		JLabel lbl = FactoryGameFrame.createLabel(caption, null, false, null, null);
		final JTextField txt = FactoryGameFrame.createTextField(1, Integer.toString(defaultValue), tabTxtField, control);
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 2, 5);
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(lbl, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		panel.add(txt, gbc);
	}
}
