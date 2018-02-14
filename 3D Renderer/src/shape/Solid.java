package shape;

import java.util.ArrayList;

import node.*;

public class Solid {
	public ArrayList<Vertex> verticies;
	public ArrayList<Triangle> triangles;

	public Solid() {
		verticies = new ArrayList<>();
		triangles = new ArrayList<>();
	}
	
	public Solid(ArrayList<Vertex> verticies, ArrayList<Triangle> triangles) {
		this.verticies = verticies;
		this.triangles = triangles;
	}
}