package node;

public class ZPair implements Comparable<ZPair> {
	private Triangle triangle;
	private Double zDepth;

	public ZPair(Triangle triangle, Double zDepth) {
		this.triangle = triangle;
		this.zDepth = zDepth;
	}

	public Double getZDepth() {
		return zDepth;
	}

	public Triangle getTriangle() {
		return triangle;
	}

	@Override
	public int compareTo(ZPair otherPair) {
		return getZDepth().compareTo(otherPair.getZDepth());
	}
}