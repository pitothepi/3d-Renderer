package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import node.Triangle;
import node.Vertex;
import node.ZPair;
import shape.Solid;

public class RendererPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/** number used to determine rotation speed on drag **/
	private static final double MOVEMENT_MULTIPLIER = 200;
	private Solid solid;
	private Solid startSolid;
	private double heading;
	private double pitch;
	private double shapeSize;

	public RendererPanel(Solid solid) {
		this.startSolid = solid;
		this.solid = new Solid(solid);
		this.addComponentListener(new ResizeListener());
		DragListener dragListener = new DragListener();
		this.addMouseListener(dragListener);
		this.addMouseMotionListener(dragListener);
		heading = 180;
		pitch = 0;
	}

	public void incrementHeading(double increment) {
		increment = increment / shapeSize * MOVEMENT_MULTIPLIER;
		heading += increment;
		repaint();
	}

	public void incrementPitch(double increment) {
		increment = increment / shapeSize * MOVEMENT_MULTIPLIER;
		pitch += increment;
		repaint();
	}

	public void setHeading(double heading) {
		this.heading = heading;
		repaint();
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
		repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());

		Solid rotatedShape = solid.getRotatedSolid(Math.toRadians(heading), "XY");
		rotatedShape = rotatedShape.getRotatedSolid(Math.toRadians(pitch), "YZ");

		drawSolid(rotatedShape, g2);
	}

	private double getSizeRatio(double size, double[] arrrayToFit) {
		size = size / 2;
		ArrayList<Double> bounds = new ArrayList<Double>(arrrayToFit.length);
		for (Double num : arrrayToFit) {
			bounds.add(Math.abs(num));
		}
		return size / Collections.max(bounds);
	}

	private void drawSolid(Solid solid, Graphics2D g2) {
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
	private Color getShade(Color color, double cosTheta) {
		double redLinear = Math.pow(color.getRed(), 2.4) * cosTheta;
		double greenLinear = Math.pow(color.getGreen(), 2.4) * cosTheta;
		double blueLinear = Math.pow(color.getBlue(), 2.4) * cosTheta;

		int red = (int) Math.pow(redLinear, 1 / 2.4);
		int green = (int) Math.pow(greenLinear, 1 / 2.4);
		int blue = (int) Math.pow(blueLinear, 1 / 2.4);
		return new Color(red, green, blue);
	}

	class ResizeListener extends ComponentAdapter {
		public void componentResized(ComponentEvent e) {
			solid = new Solid(startSolid);
			shapeSize = getWidth() < getHeight() ? getWidth() : getHeight();
			double[] bounds = solid.getBounds();
			double sizeRatio = getSizeRatio(shapeSize, bounds);
			solid.expand(sizeRatio);
			System.out.println("render area resized");
			repaint();
		}
	}

	private class DragListener extends MouseInputAdapter {
		private int oldX;
		private int oldY;
		private int currentButtonDown;

		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				oldX = e.getX();
				oldY = e.getY();
				System.out.println("mouse clicked");
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				if (solid.getIsFancyColor()) {
					solid = Solid.defaultColor(solid);
				} else {
					solid = Solid.fancyColor(solid);
				}
				repaint();
			}
			currentButtonDown = e.getButton();
		}

		public void mouseDragged(MouseEvent e) {
			if (currentButtonDown == MouseEvent.BUTTON1) {
				int deltaX = e.getX() - oldX;
				int deltaY = e.getY() - oldY;
				oldX = e.getX();
				oldY = e.getY();
				incrementHeading(deltaX);
				incrementPitch(-deltaY);
			}
		}

		public void mouseReleased(MouseEvent e) {

		}
	}
}
