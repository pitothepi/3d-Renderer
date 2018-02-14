package node;

import java.awt.Color;

public class Triangle {
	public Vertex v1;
	public Vertex v2;
	public Vertex v3;
	public Color color;

	public Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
		this(new double[][] { { v1.x, v1.y, v1.z }, { v2.x, v2.y, v2.z }, { v3.x, v3.y, v3.z } }, color);
	}

	public Triangle(double[][] verticies, Color color) {
		// error checking
		if (verticies.length != 3) {
			throw new IllegalArgumentException();
		}
		for (double[] vertex : verticies) {
			if (vertex.length != 3) {
				throw new IllegalArgumentException();
			}
		}

		this.v1 = new Vertex(verticies[0]);
		this.v2 = new Vertex(verticies[1]);
		this.v3 = new Vertex(verticies[2]);
		this.color = color;
	}

	public Triangle(Triangle triangle) {
		this.v1 = triangle.v1;
		this.v2 = triangle.v2;
		this.v3 = triangle.v3;
		this.color = new Color(triangle.color.getRed(), triangle.color.getGreen(), triangle.color.getBlue());
	}

	public Vertex getNorm() {
		// take cross product of 2 edges of triangle
		Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
        Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
		Vertex norm = new Vertex(
		         ab.y * ac.z - ab.z * ac.y,
		         ab.z * ac.x - ab.x * ac.z,
		         ab.x * ac.y - ab.y * ac.x);
		// make unit vector
		double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
		return new Vertex(norm.x / normalLength, norm.y / normalLength, norm.z / normalLength);
	}

	public void expand(double ratio) {
		v1.x *= ratio;
		v1.y *= ratio;
		v1.z *= ratio;
		v2.x *= ratio;
		v2.y *= ratio;
		v2.z *= ratio;
		v3.x *= ratio;
		v3.y *= ratio;
		v3.z *= ratio;
	}
}