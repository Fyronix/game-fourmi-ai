package dev.lounge-lizard.fourmIR2000.frame;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public final class SelectBuilder {

	private SelectBuilder() {
		/* Nothing */
	}

	/**
	 * Print a message on the screen in a new window
	 * @param title    title of the window
	 * @param msg      message to print
	 */
	public static void showMessageInfo(final String title, final String msg) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Print a message on the screen in a new window
	 * @param title    title of the window
	 * @param msg      message to print
	 */
	public static void showMessageAlert(final String title, final String msg) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Show a confirm dialog with 'Yes' and 'No' buttons.
	 * @param title    title of the window
	 * @param msg      The message to print on the window
	 * @return true if the user clicked on the 'Yes' button
	 */
	public static boolean showMessageConfirm(final String title, final String msg) {
		final int resu = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION);
		return resu == JOptionPane.NO_OPTION ? false : true;
	}

	/**
	 * Show a file select window.
	 * @param defaultExtension the extension of the file we want to get
	 * @param isLoadWindow     true when the dialog to open is a load window
	 * @return the name of the file, or null
	 */
	public static String showSelectFile(final String defaultExtension, final boolean isLoadWindow) {
		if (defaultExtension == null)
			throw new IllegalArgumentException();
		// We open the File select window
		final JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		if (isLoadWindow) {
			chooser.setApproveButtonText("Load");
			chooser.setDialogTitle("Load from file");
		} else {
			chooser.setApproveButtonText("Save");
			chooser.setDialogTitle("Save into file");
		}
		chooser.addChoosableFileFilter(new FileFilter() {
			@Override public boolean accept(File file) {
				final String filename = file.getName();
				if (file.isDirectory())
					return true;
				return filename.endsWith("." + defaultExtension);
			}

			@Override public String getDescription() {
				return "*." + defaultExtension;
			}
		});

		// We get the result
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			final StringBuilder path = new StringBuilder(chooser.getSelectedFile().getAbsolutePath());
			if (!path.toString().endsWith("." + defaultExtension))
				path.append(".").append(defaultExtension);
			return path.toString();
		}
		return null;
	}

}
