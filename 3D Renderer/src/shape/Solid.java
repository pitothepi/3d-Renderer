package shape;

import java.awt.Color;
import java.util.ArrayList;

import matrixMath.Rotate;
import node.*;

public class Solid {
	public ArrayList<Triangle> triangles;
	public Color defaultColor;
	public static final String[] ROTATION_TYPES = { "XY", "YZ", "XZ" };

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
		this.triangles = new ArrayList<Triangle>(solid.triangles);
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
				validRotation= true;
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
}