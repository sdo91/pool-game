package model.obj;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
//import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class ObjRenderer {
	
	public static Texture texture = null;
	
	public static void loadTexture(String filename) {
		
		try {
			
			FileInputStream fis = new FileInputStream(new File("resources/textures/"+ filename));
			texture = TextureLoader.getTexture("PNG", fis);
			//texture = TextureLoader.getTexture("PNG", fis, true);
			
			//return texture;
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		//return null;
	}
	
	public static void drawObject(ObjModel model, boolean texture) {
    	
    	//boolean normals = true;
		
		if (texture) {
			model.bindTexture();
		}
		
    	glBegin(GL_TRIANGLES);
		// draw all faces
		//for (Face face : model.faces) {
		for (int i = 0; i < model.faces.size(); i++) {
			Face face = model.faces.get(i);
			
			// draw the polygon for this face
			
			{
				// go thru each point in the polygon
				for (int point = 0; point < face.vertexIndices.size(); point++) {
					
					if (true) {
						int normalIndex = face.normalIndices.get(point) - 1;
						Vector3f normalCoords = model.normals.get(normalIndex);
						glNormal3f(normalCoords.x, normalCoords.y, normalCoords.z);
					}
					
					
					if (texture) {
						int textureIndex = face.textureIndices.get(point) - 1;
						Vector3f textureCoords = model.textureUVs.get(textureIndex);
						glTexCoord2f(textureCoords.x, 1-textureCoords.y); // y's are flipped
					}
					
					
					
					// find the vertex of the point in the polygon
					int vertexIndex = face.vertexIndices.get(point) - 1;
					Vector3f vertexCoords = model.vertices.get(vertexIndex);
					glVertex3f(vertexCoords.x, vertexCoords.y, vertexCoords.z);
				}
			}
			
			 
		}
		
		glEnd();
       
	}
}
