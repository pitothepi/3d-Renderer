package ui;

import javax.swing.*;

import node.Triangle;
import node.Vertex;
import node.ZPair;

import java.awt.*;
import java.awt.geom.Path2D;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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
				double shapeSize = getWidth() < getHeight() ? getWidth() : getHeight();
				shapeSize = shapeSize / 3.5;
				Solid tetrahedron = new Solid();
				tetrahedron
						.addTriangle(
								new double[][] { { shapeSize, shapeSize, shapeSize },
										{ -shapeSize, -shapeSize, shapeSize }, { -shapeSize, shapeSize, -shapeSize } },
								Color.WHITE);
				tetrahedron
						.addTriangle(
								new double[][] { { shapeSize, shapeSize, shapeSize },
										{ -shapeSize, -shapeSize, shapeSize }, { shapeSize, -shapeSize, -shapeSize } },
								Color.RED);
				tetrahedron
						.addTriangle(
								new double[][] { { -shapeSize, shapeSize, -shapeSize },
										{ shapeSize, -shapeSize, -shapeSize }, { shapeSize, shapeSize, shapeSize } },
								Color.GREEN);
				tetrahedron
						.addTriangle(
								new double[][] { { -shapeSize, shapeSize, -shapeSize },
										{ shapeSize, -shapeSize, -shapeSize }, { -shapeSize, -shapeSize, shapeSize } },
								Color.BLUE);

				// transform solid. doing the rotations one at a time is inefficient, but it is
				// simpler
				tetrahedron.triangles = inflate(inflate(inflate(tetrahedron.triangles, shapeSize),shapeSize),shapeSize);
				double heading = Math.toRadians(headingSlider.getValue()) * 2;
				Solid rotatedTetrahedron = tetrahedron.getRotatedSolid(heading, "XY");
				double pitch = Math.toRadians(pitchSlider.getValue()) * 2;
				rotatedTetrahedron = rotatedTetrahedron.getRotatedSolid(pitch, "YZ");

				drawSolid(rotatedTetrahedron, g2);
			}
		};
		pane.add(renderPanel, BorderLayout.CENTER);

		// add listeners
		headingSlider.addChangeListener(e -> renderPanel.repaint());
		pitchSlider.addChangeListener(e -> renderPanel.repaint());

		// show frame
		frame.setSize(400, 400);
		frame.setVisible(true);
	}

	public static void drawSolid(Solid solid, Graphics2D g2) {
		g2.translate(g2.getClipBounds().getWidth() / 2, g2.getClipBounds().getHeight() / 2);
		g2.setColor(Color.WHITE);

		ZPair[] zPairs = new ZPair[solid.triangles.size()];
		int i = 0;
		for (Triangle t : solid.triangles) {
			zPairs[i] = new ZPair(t, (t.v1.z + t.v2.z + t.v3.z) / 3);
			i++;
		}

		Arrays.sort(zPairs);

		for (ZPair zPair : zPairs) {
			Polygon triangle = new Polygon();
			Triangle t = zPair.getTriangle();
			triangle.addPoint((int) (t.v1.x + .5), (int) (t.v1.y + .5));
			triangle.addPoint((int) (t.v2.x + .5), (int) (t.v2.y + .5));
			triangle.addPoint((int) (t.v3.x + .5), (int) (t.v3.y + .5));

			// get normal vector for flat shading
			Vertex norm = t.getNorm();
			// get angle between light and norm
			Vertex lightVector = new Vertex(0, 0, 1);
			double cosTheta = norm.x * lightVector.x + norm.y * lightVector.y + norm.z * lightVector.z;
			double magCosTheta = Math.sqrt(
					lightVector.x * lightVector.x + lightVector.y * lightVector.y + lightVector.z * lightVector.z);
			cosTheta = cosTheta / magCosTheta;
			// get color
			Color color = getShade(t.color, Math.abs(cosTheta));
			g2.setColor(color);
			g2.fillPolygon(triangle);
		}
	}

	/**
	 * Approximates conversion to sRGB.
	 */
	public static Color getShade(Color color, double cosTheta) {
		double redLinear = Math.pow(color.getRed(), 2.4) * cosTheta;
		double greenLinear = Math.pow(color.getGreen(), 2.4) * cosTheta;
		double blueLinear = Math.pow(color.getBlue(), 2.4) * cosTheta;

		int red = (int) Math.pow(redLinear, 1 / 2.4);
		int green = (int) Math.pow(greenLinear, 1 / 2.4);
		int blue = (int) Math.pow(blueLinear, 1 / 2.4);
		return new Color(red, green, blue);
	}

	public static ArrayList<Triangle> inflate(ArrayList<Triangle> tris, double size) {
		ArrayList<Triangle> result = new ArrayList<Triangle>();
		for (Triangle t : tris) {
			Vertex m1 = new Vertex((t.v1.x + t.v2.x) / 2, (t.v1.y + t.v2.y) / 2, (t.v1.z + t.v2.z) / 2);
			Vertex m2 = new Vertex((t.v2.x + t.v3.x) / 2, (t.v2.y + t.v3.y) / 2, (t.v2.z + t.v3.z) / 2);
			Vertex m3 = new Vertex((t.v1.x + t.v3.x) / 2, (t.v1.y + t.v3.y) / 2, (t.v1.z + t.v3.z) / 2);
			result.add(new Triangle(t.v1, m1, m3, t.color));
			result.add(new Triangle(t.v2, m1, m2, t.color));
			result.add(new Triangle(t.v3, m2, m3, t.color));
			result.add(new Triangle(m1, m2, m3, t.color));
		}
		for (Triangle t : result) {
			for (Vertex v : new Vertex[] { t.v1, t.v2, t.v3 }) {
				double l = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) / Math.sqrt(size * size * 3);
				v.x /= l;
				v.y /= l;
				v.z /= l;
			}
		}
		return result;
	}
}
