package shape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

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
	private String colorMode;
	public static final String DEFAULT_COLOR = "default";
	public static final String FANCY_COLOR = "fancy";
	public static final String SLOPE_COLOR = "slope";

	public Solid() {
		this(new ArrayList<Triangle>());
	}

	public Solid(ArrayList<Triangle> triangles) {
		this(triangles, Color.WHITE);
	}

	public Solid(ArrayList<Triangle> triangles, Color defaultColor) {
		this.triangles = triangles;
		this.defaultColor = defaultColor;
		recenter();
		colorMode = DEFAULT_COLOR;
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
		this.colorMode = solid.getColorMode();
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
		for (Triangle triangle : triangles) {
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

	public void recenter() {
		double[] bounds = getBounds();
		double midX = (bounds[0] + bounds[1]) / 2;
		double midY = (bounds[2] + bounds[3]) / 2;
		double midZ = (bounds[4] + bounds[5]) / 2;
		for (int i = 0; i < triangles.size(); i++) {
			triangles.get(i).v1 = matrixMath.Translate.translate(triangles.get(i).v1, -midX, -midY, -midZ);
			triangles.get(i).v2 = matrixMath.Translate.translate(triangles.get(i).v2, -midX, -midY, -midZ);
			triangles.get(i).v3 = matrixMath.Translate.translate(triangles.get(i).v3, -midX, -midY, -midZ);
		}
	}

	public void reColor(String colorMode) {
		if (this.colorMode.equals(colorMode)) {
			return;
		}
		switch (colorMode) {
		case DEFAULT_COLOR:
			defaultColor();
			break;
		case FANCY_COLOR:
			fancyColor();
			break;
		case SLOPE_COLOR:
			slopeColor();
			break;
		}
	}

	private void defaultColor() {
		for (int i = 0; i < triangles.size(); i++) {
			triangles.get(i).color = defaultColor;
		}
		colorMode = DEFAULT_COLOR;
	}

	private void fancyColor() {
		double[] bounds = getBounds();
		double oldXMin = bounds[0];
		double oldYMin = bounds[2];
		double oldZMin = bounds[4];
		bounds = new double[] { bounds[0] - bounds[0], bounds[1] - bounds[0], bounds[2] - bounds[2],
				bounds[3] - bounds[2], bounds[4] - bounds[4], bounds[5] - bounds[4] };
		for (int i = 0; i < triangles.size(); i++) {
			double avgX = (triangles.get(i).v1.x + triangles.get(i).v2.x + triangles.get(i).v3.x) / 3 - oldXMin;
			int red = (int) (avgX / bounds[1] * 255 + .5);
			double avgY = (triangles.get(i).v1.y + triangles.get(i).v2.y + triangles.get(i).v3.y) / 3 - oldYMin;
			int green = (int) (avgY / bounds[3] * 255 + .5);
			double avgZ = (triangles.get(i).v1.z + triangles.get(i).v2.z + triangles.get(i).v3.z) / 3 - oldZMin;
			int blue = (int) (avgZ / bounds[5] * 255 + .5);
			Color color = new Color(red, green, blue);
			triangles.get(i).color = color;
		}
		colorMode = FANCY_COLOR;
	}

	private void slopeColor() {
		for (int i = 0; i < triangles.size(); i++) {
			// this part is extra lazy
			double[] xs = { triangles.get(i).v1.x, triangles.get(i).v2.x, triangles.get(i).v3.x };
			double[] ys = { triangles.get(i).v1.y, triangles.get(i).v2.y, triangles.get(i).v3.y };
			double[] zs = { triangles.get(i).v1.z, triangles.get(i).v2.z, triangles.get(i).v3.z };
			Arrays.sort(xs);
			Arrays.sort(ys);
			Arrays.sort(zs);
			double dx = Math.abs(xs[2] - xs[0]);
			double dy = Math.abs(ys[2] - ys[0]);
			double dz = Math.abs(zs[2] - zs[0]);
			double yxAngle = Math.abs(Math.atan(dy / dx));
			double yzAngle = Math.abs(Math.atan(dy / dz));
			double angle = yxAngle > yzAngle ? yzAngle : yzAngle;
			double red = angle / (Math.PI / 2) * 255;
			System.out.println(angle + " : " + red);
			triangles.get(i).color = new Color(255, 255 - (int) (red + .5), 255 - (int) (red + .5));
		}
		colorMode = SLOPE_COLOR;
	}

	public String getColorMode() {
		return colorMode;
	}
}