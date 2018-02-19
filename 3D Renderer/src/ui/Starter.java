package ui;

import javax.swing.JFileChooser;

public class Starter {
	public static void main(String[] args) {
		Viewer viewer = new Viewer();

		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(viewer);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			viewer.setFileName(fc.getSelectedFile().getPath());
		} else {
			viewer.setFileName("models/fallout 4 helmet.stl");
		}
		
		viewer.start();
	}
}
