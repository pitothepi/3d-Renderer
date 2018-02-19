package io;

import java.util.List;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import shape.Solid;

// from https://stackoverflow.com/questions/17334031/how-to-read-and-parse-a-big-stl-file

public class STL {
	public static Solid readBinarySTL(String fileName, Color color) {
		InputStream inputStream = null;
		Solid solid = new Solid(color);
		
		try {
			inputStream = new FileInputStream(fileName);
			int n = 0;
			byte[] buffer = new byte[4];
			inputStream.skip(80);
			n = inputStream.read(buffer);
			System.out.println("n==" + n);
			int size = getIntWithLittleEndian(buffer, 0);
			System.out.println("size==" + size);
			List<Float> normalList = new ArrayList<Float>();
			for (int i = 0; i < size; i++) {
				// normal
				for (int k = 0; k < 3; k++) {
					inputStream.read(buffer);
					normalList.add(Float.intBitsToFloat(getIntWithLittleEndian(buffer, 0)));
				}
				// verticies
				double[][] verticies = new double[3][3];
				for (int j = 0; j < 3; j++) {
					inputStream.read(buffer);
					float x = Float.intBitsToFloat(getIntWithLittleEndian(buffer, 0));
					inputStream.read(buffer);
					float y = Float.intBitsToFloat(getIntWithLittleEndian(buffer, 0));
					inputStream.read(buffer);
					float z = Float.intBitsToFloat(getIntWithLittleEndian(buffer, 0));
					// adjustMaxMin(x, y, z);
					verticies[j] = new double[] {x,y,z};
				}
				solid.addTriangle(verticies);
				inputStream.skip(2);
			}
			System.out.println("normalList size== " + normalList.size());
			System.out.println("triagleList size== " + solid.triangles.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		solid.recenter();
		return solid;
	}

	private static int getIntWithLittleEndian(byte[] stlBytes, int offset) {
		return (0xff & stlBytes[offset]) | ((0xff & stlBytes[offset + 1]) << 8) | ((0xff & stlBytes[offset + 2]) << 16)
				| ((0xff & stlBytes[offset + 3]) << 24);
	}
}
