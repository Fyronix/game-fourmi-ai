package dev.lounge-lizard.fourmIR2000.frame;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JTextField;

import dev.lounge-lizard.fourmIR2000.frame.GameFrame.BtnControls;
import dev.lounge-lizard.fourmIR2000.frame.GameFrame.GameControls;
import dev.lounge-lizard.fourmIR2000.pictures.Values;
import dev.lounge-lizard.lawrence.impl.ImageFile;


/**
 * Class that buid graphic components for the frame, like buttons, pictures, etc.
 */
final class FactoryGameFrame {
	
	private FactoryGameFrame() {
		/* Nothing */
	}

	
	/**
	 * Create a picture component, with a special cursor if needed. For a list
	 * of availables cursor,
	 * @see Cursor
     * 
	 * @param value    the picture to load
	 * @param toolTip  the "tool tip text" to show when we pass on the component with the mouse
	 * @param map	   if not null, we assign the component in the map with the key 'control' 
	 * @param control  if  not null, used as a key to assign the component in the map
	 * @return the new JLabel
	 */
	static JLabel createPicture(final Values value, final boolean isHandCursor, final String toolTip, final HashMap<GameFrame.BtnControls, JLabel> map, final BtnControls control) {
		final JLabel lbl = new JLabel();
		final ImageIcon ico = new ImageIcon(giveImage(value));
		lbl.setIcon(ico);
		if (isHandCursor)
			lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
		if (toolTip != null)
			lbl.setToolTipText(toolTip);
		if (map != null && control != null) 
			map.put(control, lbl);
		return lbl;
	}

	
	/**
	 * Create a game button. This is a button that is enabled / disabled with specific pictures.
	 * @param enabledPicture	the picture drawn on the button when enable
	 * @param disabledPicture	the picture drawn on the button when disable
	 * @param enabledTooltip	the tooltip text associated when enable
	 * @param disabledTooltip	the tooltip text associated when disabled
	 * @return the associated JLabel
	 */
	static JLabel createGameButton(final Values enabledPicture, final Values disabledPicture, final String enabledTooltip, final String disabledTooltip, final HashMap<GameControls, GameButton> map, final GameControls control) {
		final GameButton gb = new GameButton(enabledPicture, disabledPicture, enabledTooltip, disabledTooltip);
		if (map != null && control != null) 
			map.put(control, gb);
		return gb.getButton();
	}
	

	/**
	 * Create an horizontal scrollbar
	 * 
	 * @param min      min value
	 * @param max      max value
	 * @param begin    starting value when the application is launched
	 * @param decal    how much have we to increase/decerase when we click on the bar, not on the arrows
	 * @param map	   if not null, we assign the component in the map with the key 'control' 
	 * @param control  if  not null, used as a key to assign the component in the map
	 * @return then new ScrollBar
	 */
	static JScrollBar createHorizScrollBar(final int min, final int max, final int begin, final int decal, final HashMap<GameFrame.ScrollControls, JScrollBar> map, final GameFrame.ScrollControls control) {
		final JScrollBar scroll = new JScrollBar(Adjustable.HORIZONTAL, begin, decal, min, max);
		scroll.doLayout();
		if (map != null && control != null) 
			map.put(control, scroll);
		return scroll;
	}

    
    /**
	 * Create an horizontal slider
	 * 
	 * @param min      		min value
	 * @param max      		max value
	 * @param tickSpace 	space between the marks under the bar
	 * @param begin    		starting value when the application is launched
	 * @param captionMin	the caption to draw on the minimal value
	 * @param captionMax	the caption to draw on the maximal value
	 * @param map	   		if not null, we assign the component in the map with the key 'control' 
	 * @param control  		if  not null, used as a key to assign the component in the map
	 * @return then new JSlider
	 */
    static JSlider createSlider(final int min, final int max, final int tickSpace, final int begin, final String captionMin, final String captionMax, final HashMap<GameFrame.SlideControls, JSlider> map, final GameFrame.SlideControls control) {
    	final JSlider slide = new JSlider(min, max, begin);
        
        final Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put( new Integer(min), new JLabel(captionMin) );
        labelTable.put( new Integer(max), new JLabel(captionMax) );
       	slide.setMajorTickSpacing(tickSpace);
        slide.setSnapToTicks(true);
        slide.setForeground(Color.gray);
        slide.setPaintTicks(true);
        slide.setPaintLabels(true);
    	slide.setLabelTable( labelTable );
        slide.doLayout();
    	if (map != null && control != null) 
			map.put(control, slide);
        return slide;
    }    
    
    
	/**
	 * Create a new label. This element can not have focus, and can not have a Listener
	 * @param caption  message to print in the label
	 * @param color	   if not 'null', assign the foregrounc color to 'color'
	 * @param isBold   if 'true', the text is set to bold
	 * @param map	   if not null, we assign the component in the map with the key 'control' 
	 * @param control  if  not null, used as a key to assign the component in the map
	 * @return the new Label
	 */
	static JLabel createLabel(final String caption, final Color color, final boolean isBold, final HashMap<GameFrame.LblControls, JLabel> map, final GameFrame.LblControls control) {
		final JLabel label = new JLabel(caption);
		limitTextSize(label);
		label.setForeground(color == null ? Color.GRAY : color);
		if (isBold)
			label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
		if (map != null && control != null) 
			map.put(control, label);
		return label;
	}

	/**
	 * Create a new text field.
	 * @param nbColumns    the number of columns of the field
	 * @param defString    the default text
	 * @param map	   	   if not null, we assign the component in the map with the key 'control' 
	 * @param control  	   if  not null, used as a key to assign the component in the map
	 * @return the new JTextField
	 */
	static JTextField createTextField(final int nbColumns, final String defString, final HashMap<GameFrame.TextControls, JTextField> map, final GameFrame.TextControls control) {
		final JTextField txt = new JTextField(defString, nbColumns);
		txt.setMaximumSize(new Dimension(200, 20));
		if (map != null && control != null) 
			map.put(control, txt);
		return txt;
	}

	/**
	 * Create a new menu. You can add a keyboard shortcut if you want. If you
	 * don't want to, just put '0' instead.
	 * @param caption  the caption of the menu
	 * @param evtKey   the eventual shortcut
	 * @return the new menu
	 */
	static JMenu createMenu(final String caption, final int evtKey) {
		final JMenu mnu = new JMenu(caption);
		if (evtKey != 0)
			mnu.setMnemonic(evtKey);
		return mnu;
	}
	

	

	/**
	 * Return the image associated with an instance of a 'Values' enumaration.
     *  >> This code is adapted from from fr.umlv.lawrence.impl.EnumImageProvider#EnumImageProvider(Class<T>),
	 * written by Julien Cervelle)
	 * @param value    the 'Values' instance to get
	 * @return the image associated
	 */
	static BufferedImage giveImage(final Values value) {
		BufferedImage img = null;
		try {
			final Class<Values> curClass = Values.class;
			final String name = curClass.getField(value.name()).getAnnotation(
					ImageFile.class).value();
			img = ImageIO.read(curClass.getResourceAsStream(name));
		} catch (Exception e) {
			throw new AssertionError(e);
		}
		return img;
	}

    
    /**
     * Fix the size of the text to be the same on all plateforms
     * @param c the component to fix
     */
    private static void limitTextSize(final JComponent c) {
    	c.setFont(new Font(c.getFont().getName(), c.getFont().getStyle(), 11));	
    }
}
