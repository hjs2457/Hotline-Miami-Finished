package hmPKG;

import java.awt.Graphics;
import javax.swing.*;

/**
 * MenuDisplay is a modified JPanel
 */
public class MenuDisplay extends JPanel{
	private static final long serialVersionUID = 1L;
	/**
	 * Used to clear the screen smoothly so it doesnt just draw on top of what was previously drawn on the JFrame, given a graphics object. Overidden to call the Menu's repaint method.
	 * @param g the graphics object
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Menu.m.repaint(g);
	}
}