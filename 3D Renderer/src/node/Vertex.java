package node;

public class Vertex {
	public double x;
	public double y;
	public double z;

	public Vertex(double[] vertex) {
		this(vertex[0], vertex[1], vertex[2]);
	}

	public Vertex(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}