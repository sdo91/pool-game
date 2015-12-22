package model.obj;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector3f;

public class ObjLoader {
	public static ObjModel loadModel(String filename) {
		
		ObjModel result = null;
		File file = new File("resources/models/" + filename);
		
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			result = new ObjModel();
			
			while (scanner.hasNext()) {
				String lineCode = scanner.next();
				float x,y,z;
				switch (lineCode) {
				
					case("v"):
						//System.out.println("v");
						x = Float.valueOf(scanner.next());
						y = Float.valueOf(scanner.next());
						z = Float.valueOf(scanner.next());
						result.vertices.add(new Vector3f(x, y, z));
						break;
						
					case("vt"):
						//System.out.println("vt");
						x = Float.valueOf(scanner.next());
						y = Float.valueOf(scanner.next());
						z = 0; // 2d coord
						result.textureUVs.add(new Vector3f(x, y, z));
						break;
						
					case("vn"):
						//System.out.println("vn");
						x = Float.valueOf(scanner.next());
						y = Float.valueOf(scanner.next());
						z = Float.valueOf(scanner.next());
						result.normals.add(new Vector3f(x, y, z));
						break;
						
					case("f"):
						//System.out.println("f");
						scanner.skip(" ");
						Face face = new Face();
						
						String[] lineTokens = scanner.nextLine().split(" ");
						
						for (String token : lineTokens) {
							
							String[] indices = token.split("/");
							face.vertexIndices.add(Integer.parseInt(indices[0]));
							if (!indices[1].equals("")) {
								// might be an empty field
								face.textureIndices.add(Integer.parseInt(indices[1]));
							}
							face.normalIndices.add(Integer.parseInt(indices[2]));
						}
						result.faces.add(face);
						break;
						
					default:
						//System.out.println("other");
						// ignore this line
						scanner.nextLine();
						
						
				} // done w/ a line
			} // done w/ all lines
			
			scanner.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	public static ObjModel loadModel(String modelFilename, String textureFilename) {
		ObjModel result = loadModel(modelFilename);
		result.setTextureName(textureFilename);
		result.loadTexture();
		return result;
	}
}
