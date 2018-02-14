package shape;

import java.awt.Color;
import java.util.ArrayList;

import matrixMath.Rotate;
import node.*;

public class Solid {
	public ArrayList<Triangle> triangles;
	public Color defaultColor;
	public static final String[] ROTATION_TYPES = { "XY", "YZ", "XZ" };
	private double minX = Double.POSITIVE_INFINITY;
	private double maxX = Double.NEGATIVE_INFINITY;
	private double minY = Double.POSITIVE_INFINITY;
	private double maxY = Double.NEGATIVE_INFINITY;
	private double minZ = Double.POSITIVE_INFINITY;
	private double maxZ = Double.NEGATIVE_INFINITY;

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

	public Solid(Solid solid) {
		this.triangles = new ArrayList<Triangle>();
		for (Triangle triangle : solid.triangles) {
			this.triangles.add(new Triangle(triangle));
		}
		this.defaultColor = new Color(solid.defaultColor.getRed(), solid.defaultColor.getGreen(),
				solid.defaultColor.getBlue());
	}

	public void addTriangle(double[][] verticies) {
		addTriangle(verticies, defaultColor);
	}

	public void addTriangle(double[][] verticies, Color color) {
		triangles.add(new Triangle(verticies, color));
	}

	public Solid getRotatedSolid(double angle, String rotationType) {
		// check if the type of rotation is valid
		boolean validRotation = false;
		for (String type : ROTATION_TYPES) {
			if (type.equals(rotationType)) {
				validRotation = true;
			}
		}
		if (!validRotation) {
			throw new IllegalArgumentException("Invalid rotation type.");
		}

		// do the rotation
		Solid transformedSolid = new Solid(this);
		switch (rotationType) {
		case "XY":
			for (int i = 0; i < transformedSolid.triangles.size(); i++) {
				transformedSolid.triangles.get(i).v1 = Rotate.XY(this.triangles.get(i).v1, angle);
				transformedSolid.triangles.get(i).v2 = Rotate.XY(this.triangles.get(i).v2, angle);
				transformedSolid.triangles.get(i).v3 = Rotate.XY(this.triangles.get(i).v3, angle);
			}
			break;
		case "YZ":
			for (int i = 0; i < transformedSolid.triangles.size(); i++) {
				transformedSolid.triangles.get(i).v1 = Rotate.YZ(this.triangles.get(i).v1, angle);
				transformedSolid.triangles.get(i).v2 = Rotate.YZ(this.triangles.get(i).v2, angle);
				transformedSolid.triangles.get(i).v3 = Rotate.YZ(this.triangles.get(i).v3, angle);
			}
			break;
		case "XZ":
			for (int i = 0; i < transformedSolid.triangles.size(); i++) {
				transformedSolid.triangles.get(i).v1 = Rotate.XZ(this.triangles.get(i).v1, angle);
				transformedSolid.triangles.get(i).v2 = Rotate.XZ(this.triangles.get(i).v2, angle);
				transformedSolid.triangles.get(i).v3 = Rotate.XZ(this.triangles.get(i).v3, angle);
			}
			break;
		}
		return transformedSolid;
	}

	public double[] getBounds() {
		updateBounds();
		return new double[] { minX, maxX, minY, maxY, minZ, maxZ };
	}
	
	public void expand(double ratio) {
		for (Triangle triangle : triangles)
		{
			triangle.expand(ratio);
		}
	}

	private void updateBounds() {
		for (Triangle triangle : triangles) {
			if (triangle.v1.x < minX)
				minX = triangle.v1.x;
			if (triangle.v2.x < minX)
				minX = triangle.v2.x;
			if (triangle.v3.x < minX)
				minX = triangle.v3.x;
			if (triangle.v1.y < minY)
				minY = triangle.v1.y;
			if (triangle.v2.y < minY)
				minY = triangle.v2.y;
			if (triangle.v3.y < minY)
				minY = triangle.v3.y;
			if (triangle.v1.z < minZ)
				minZ = triangle.v1.z;
			if (triangle.v2.z < minZ)
				minZ = triangle.v2.z;
			if (triangle.v3.z < minZ)
				minZ = triangle.v3.z;
			if (triangle.v1.x > maxX)
				maxX = triangle.v1.x;
			if (triangle.v2.x > maxX)
				maxX = triangle.v2.x;
			if (triangle.v3.x > maxX)
				maxX = triangle.v3.x;
			if (triangle.v1.y > maxY)
				maxY = triangle.v1.y;
			if (triangle.v2.y > maxY)
				maxY = triangle.v2.y;
			if (triangle.v3.y > maxY)
				maxY = triangle.v3.y;
			if (triangle.v1.z > maxZ)
				maxZ = triangle.v1.z;
			if (triangle.v2.z > maxZ)
				maxZ = triangle.v2.z;
			if (triangle.v3.z > maxZ)
				maxZ = triangle.v3.z;
		}
	}
}