package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
	private double deltaHeading;
	private double deltaPitch;
	private boolean drawWires;
	private int oldSize;

	public RendererPanel(Solid solid) {
		this.startSolid = solid;
		this.solid = new Solid(solid);
		this.addComponentListener(new ResizeListener());
		DragListener dragListener = new DragListener();
		this.addMouseListener(dragListener);
		this.addMouseMotionListener(dragListener);
		this.addKeyListener(new KeyboardListener());
		deltaHeading = 0;
		deltaPitch = 0;
		drawWires = false;
		oldSize = getSmallerDimension();
	}

	public void incrementHeading(double increment) {
		increment = increment / oldSize * MOVEMENT_MULTIPLIER;
		deltaHeading += increment;
		if (deltaHeading > 360) {
			deltaHeading -= 360;
		} else if (deltaHeading < 0) {
			deltaHeading += 360;
		}
		repaint();
	}

	public void incrementPitch(double increment) {
		increment = increment / oldSize * MOVEMENT_MULTIPLIER;
		deltaPitch += increment;

		if (deltaPitch > 360) {
			deltaPitch -= 360;
		} else if (deltaPitch < 0) {
			deltaPitch += 360;
		}
		repaint();
	}

	public void setHeading(double heading) {
		this.deltaHeading = heading;
		repaint();
	}

	public void setPitch(double pitch) {
		this.deltaPitch = pitch;
		repaint();
	}

	public void paintComponent(Graphics g) {
		System.out.println(deltaHeading + deltaPitch);
		System.out.println(solid.getBounds()[1]);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());

		if (Math.abs(deltaHeading) > 0) {
			solid = solid.getRotatedSolid(Math.toRadians(deltaHeading), "XY");
		}
		if (Math.abs(deltaPitch) > 0) {
			solid = solid.getRotatedSolid(Math.toRadians(deltaPitch), "YZ");
		}
		deltaHeading = 0;
		deltaPitch = 0;

		drawSolid(solid, g2);
	}

	private double getSizeRatio(double size, double[] arrrayToFit) {
		size = size / 2;
		ArrayList<Double> bounds = new ArrayList<Double>(arrrayToFit.length);
		for (Double num : arrrayToFit) {
			bounds.add(Math.abs(num));
		}
		return size / Collections.max(bounds);
	}

	private int getSmallerDimension() {
		return getWidth() < getHeight() ? getWidth() : getHeight();
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
			Color color = getShade(new Color(t.color.getRed(), t.color.getGreen(), t.color.getBlue(), 1),
					Math.abs(cosTheta));
			g2.setColor(color);
			g2.fillPolygon(triangle);
		}
		if (drawWires) {
			for (Triangle t : solid.triangles) {
				Polygon triangle = new Polygon();
				triangle.addPoint((int) (t.v1.x + .5), (int) (t.v1.y + .5));
				triangle.addPoint((int) (t.v2.x + .5), (int) (t.v2.y + .5));
				triangle.addPoint((int) (t.v3.x + .5), (int) (t.v3.y + .5));

				g2.setColor(solid.getIsFancyColor() ? Color.BLACK : Color.WHITE);
				g2.drawPolygon(triangle);
			}
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

	// necessary to goin focus and receive key events
	@Override
	public boolean isFocusTraversable() {
		return true;
	}

	private class ResizeListener extends ComponentAdapter {
		public void componentResized(ComponentEvent e) {
			if (oldSize == 0) {
				double[] bounds = solid.getBounds();
				double sizeRatio = getSizeRatio(getSmallerDimension(), bounds);
				solid.expand(sizeRatio);
				oldSize = getSmallerDimension();
			} else {
				solid.expand((double) (getSmallerDimension()) / oldSize);
				oldSize = getSmallerDimension();
			}
			repaint();
		}
	}

	private class KeyboardListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				drawWires = !drawWires;
				repaint();
			}
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
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				if (solid.getIsFancyColor()) {
					solid = Solid.defaultColor(solid);
				} else {
					solid = Solid.fancyColor(solid);
				}
				if (startSolid.getIsFancyColor()) {
					startSolid = Solid.defaultColor(startSolid);
				} else {
					startSolid = Solid.fancyColor(startSolid);
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
