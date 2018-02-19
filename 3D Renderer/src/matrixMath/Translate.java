package matrixMath;

import node.Vertex;

public class Translate {
	public static Vertex translate(Vertex in, double XOffest, double YOffset, double ZOffset) {
		return new Vertex(
				in.x + XOffest,
				in.y + YOffset,
				in.z + ZOffset
			);
	}
}
