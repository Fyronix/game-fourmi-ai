package dev.lounge-lizard.fourmIR2000.frame;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import dev.lounge-lizard.fourmIR2000.insect.Insect;

final class FactoryPanelGroup {

	private FactoryPanelGroup() {
		/* Nothing */
	}

	
	/**
	 * Create a game menu showing all selected insects
	 * @param panel		panel where we draw the current group
	 * @param insGroup	group of insects to add in the panel
	 */
	public static void createMenu(final JPanel panel, final List<Insect> insGroup) {
		panel.setLayout(new FlowLayout());
		panel.removeAll();
		panel.setBorder(BorderFactory.createTitledBorder("Current group"));
		for (Insect ins : insGroup)
            panel.add(FactoryGameFrame.createPicture(ins.getPicture(), false, "", null, null));
	}
	
}
