package dev.lounge-lizard.fourmIR2000.frame;

import java.awt.Cursor;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import dev.lounge-lizard.fourmIR2000.pictures.Values;


final class GameButton {

	/** Picture for 'enabled' state */
	private final ImageIcon enabledPicture;
	
	/** Picture for 'disabled' state */
	private final ImageIcon disabledPicture;
	
	/** Tooltip for 'enabled' state */
	private String enabledTooltip;
	
	/** Tooltip for 'disabled' state */
	private String disabledTooltip;
	
	/** MouseListener associated */
	private MouseListener listener;
	
	/** Current state of the button */
	private boolean enabled;
	
	/** Graphical button */
	private final JLabel label;
	
	
	/**
	 * Create a game button. This is a button that is enabled / disabled with specific pictures.
	 * @param enabledPicture	the picture drawn on the button when enable
	 * @param disabledPicture	the picture drawn on the button when disable
	 * @param enabledTooltip	the tooltip text associated when enable
	 * @param disabledTooltip	the tooltip text associated when disabled
	 */
	public GameButton(Values enabledPicture, Values disabledPicture, String enabledTooltip, String disabledTooltip) {
		this.enabledPicture = new ImageIcon(FactoryGameFrame.giveImage(enabledPicture));
		this.disabledPicture = new ImageIcon(FactoryGameFrame.giveImage(disabledPicture));
		this.enabledTooltip = enabledTooltip;
		this.disabledTooltip = disabledTooltip;
		listener = null;
		label = new JLabel();
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		enabled = true;		// we put the wrong value intentionaly. This is done 
		setEnabled(false);	// to force the change of state
	}
	
	
	/**
	 * Update the MouseListener of the button. If null, we remove the previous one.
	 * @param newListener the new listener to assign
	 */
	void updateMouseListener(final MouseListener newListener) {
		//label.removeMouseListener(listener);
		listener = newListener;
		label.addMouseListener(listener);
	}
	
	
	/**
	 * Give the JLabel representing the button
	 * @return the button
	 */
	JLabel getButton() {
		return label;
	}
	
	
	/**
	 * Change the state of the button. If disabled, it will not respond on listeners
	 * @param state the new state
	 */
	void setEnabled(final boolean state) {
		// If this is the same state than before, we do nothing !
		if (enabled == state)
			return;
		// Else we update the object
		enabled = state;
		if (enabled) {
			label.setIcon(enabledPicture);
			label.setToolTipText(enabledTooltip);
			//if (label.getMouseListeners().length == 0)
			//label.addMouseListener(listener);
		} else {
			label.setIcon(disabledPicture);
			label.setToolTipText(disabledTooltip);
			//label.removeMouseListener(listener);
		}
	}
	

	/**
	 * Change the 'enabled' tooltip text
	 * @param newTooltip the new tooltip
	 */
	void setEnabledTooltip(final String newTooltip) {
		enabledTooltip = newTooltip;
	}
	
	
	/**
	 * Change the 'disabled' tooltip text
	 * @param newTooltip the new tooltip
	 */
	public void setDisabledTooltip(final String newTooltip) {
		disabledTooltip = newTooltip;
	}
}
