package dev.lounge-lizard.fourmIR2000.frame;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

final class AboutFrame {

	private AboutFrame() {
		/* nothing */
	}

	/**
	 * Create a new About frame
	 * @param parent	the parent of this frame
	 * @param title		the title of the window
	 */
	static void showAbout(final JFrame parent, final String title) {

		// Creation of the frame
		final JDialog f = new JDialog(parent, title, true);
		
		// SplashScreen
		try {
			final ImageIcon splash = new ImageIcon(ImageIO.read(AboutFrame.class.getResourceAsStream("/resources/pictures/splash.jpg")));
			final JLabel img = new JLabel(splash);
			img.setBounds(0, 0, splash.getIconWidth(), splash.getIconHeight());
			f.add(img);
			f.setSize(img.getSize());
			f.setResizable(false);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
