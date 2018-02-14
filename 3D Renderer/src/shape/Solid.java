package shape;

import java.awt.Color;
import java.util.ArrayList;

import node.*;

public class Solid {
	public ArrayList<Triangle> triangles;
	public Color defaultColor;

	public Solid() {
		this(new ArrayList<Triangle>());
	}

	public Solid(ArrayList<Triangle> triangles) {
		this(triangles, Color.WHITE);
	}

	public Solid(ArrayList<Triangle> triangles, Color defaultColor) {
		this.triangles = triangles;
		this.defaultColor = defaultColor;
	}

	public Solid(Color defaultColor) {
		this(new ArrayList<Triangle>(), defaultColor);
	}

	public void addTriangle(double[][] verticies) {
		addTriangle(verticies, defaultColor);
	}

	public void addTriangle(double[][] verticies, Color color) {
		triangles.add(new Triangle(verticies, color));
	}
}