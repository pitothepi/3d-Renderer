package ui;

import javax.swing.*;

import node.Triangle;

import java.awt.*;
import java.awt.geom.Path2D;

import shape.Solid;

public class Viewer {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());

		// slider to control horizontal rotation
		JSlider headingSlider = new JSlider(0, 360, 180);
		pane.add(headingSlider, BorderLayout.SOUTH);

		// slider to control vertical rotation
		JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
		pane.add(pitchSlider, BorderLayout.EAST);

		// panel to display render results
		JPanel renderPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, getWidth(), getHeight());

				// rendering magic will happen here
				Solid tetrahedron = new Solid();
				tetrahedron.addTriangle(new double[][] { { 100, 100, 100 }, { -100, -100, 100 }, { -100, 100, -100 } },
						Color.WHITE);
				tetrahedron.addTriangle(new double[][] { { 100, 100, 100 }, { -100, -100, 100 }, { 100, -100, -100 } },
						Color.RED);
				tetrahedron.addTriangle(new double[][] { { -100, 100, -100 }, { 100, -100, -100 }, { 100, 100, 100 } },
						Color.GREEN);
				tetrahedron.addTriangle(new double[][] { { -100, 100, -100 }, { 100, -100, -100 }, { -100, -100, 100 } },
						Color.BLUE);
				drawSolid(tetrahedron, g2);
			}
		};
		pane.add(renderPanel, BorderLayout.CENTER);

		frame.setSize(400, 400);
		frame.setVisible(true);
	}
	
	public static void drawSolid(Solid solid, Graphics2D g2) {
		g2.translate(g2.getClipBounds().getWidth() / 2, g2.getClipBounds().getHeight() / 2);
		g2.setColor(Color.WHITE);
		for (Triangle t : solid.triangles) {
		    Path2D path = new Path2D.Double();
		    path.moveTo(t.v1.x, t.v1.y);
		    path.lineTo(t.v2.x, t.v2.y);
		    path.lineTo(t.v3.x, t.v3.y);
		    path.closePath();
		    g2.draw(path);
		}
	}
}
