package node;

import java.awt.Color;

public class Triangle {
	Vertex v1;
	Vertex v2;
	Vertex v3;
	Color color;

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
}