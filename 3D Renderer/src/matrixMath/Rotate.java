package matrixMath;

import node.Vertex;

public class Rotate {
	public static Vertex XY(Vertex in, double angle) {
		double[] values = new double[] {
				Math.cos(angle), 0, -Math.sin(angle),
				0, 1, 0,
				Math.sin(angle), 0, Math.cos(angle)
			};
		return new Vertex(
				in.x * values[0] + in.y * values[3] + in.z * values[6],
				in.x * values[1] + in.y * values[4] + in.z * values[7],
				in.x * values[2] + in.y * values[5] + in.z * values[8]
			);
	}
	
	public static Vertex YZ(Vertex in, double angle) {
		double[] values = new double[] {
				1, 0, 0,
		        0, Math.cos(angle), Math.sin(angle),
		        0, -Math.sin(angle), Math.cos(angle)
			};
		return new Vertex(
				in.x * values[0] + in.y * values[3] + in.z * values[6],
				in.x * values[1] + in.y * values[4] + in.z * values[7],
				in.x * values[2] + in.y * values[5] + in.z * values[8]
			);
	}
	
	public static Vertex XZ(Vertex in, double angle) {
		double[] values = new double[] {
				Math.cos(angle), 0, -Math.sin(angle),
		        0, 1, 0,
		        Math.sin(angle), 0, Math.cos(angle)
			};
		return new Vertex(
				in.x * values[0] + in.y * values[3] + in.z * values[6],
				in.x * values[1] + in.y * values[4] + in.z * values[7],
				in.x * values[2] + in.y * values[5] + in.z * values[8]
			);
	}
}
