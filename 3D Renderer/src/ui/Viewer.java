package ui;

import javax.swing.*;

import io.STL;
import node.Triangle;
import node.Vertex;

import java.awt.*;
import java.util.ArrayList;

import shape.Solid;

public class Viewer extends Component {
	private static final long serialVersionUID = 1L;
	private String fileName;

	public Viewer() {
		// do nothing
	}

	public Viewer(String fileName) {
		this.fileName = fileName;
	}

	public void setFileName(String filename) {
		this.fileName = filename;
	}

	public void start() {
		Solid shape = STL.readBinarySTL(fileName, Color.WHITE);

		JFrame frame = new JFrame();
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());

		// panel to display render results
		RendererPanel renderPanel = new RendererPanel(shape);
		pane.add(renderPanel, BorderLayout.CENTER);

		// show frame
		frame.setSize(400, 400);
		frame.setVisible(true);
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
