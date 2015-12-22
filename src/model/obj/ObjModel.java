package model.obj;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class ObjModel {
	
	public List<Vector3f> vertices = new ArrayList<>();
	public List<Vector3f> textureUVs = new ArrayList<>();
	public List<Vector3f> normals = new ArrayList<>();
	public List<Face> faces = new ArrayList<>();
	
	//public Texture texture = null;
	public String textureFilename = null;
	private Texture texture;
	
	public ObjModel() {
		// do nothing
	}
	
	public void setTextureName(String textureFilename) {
		this.textureFilename = textureFilename;
	}
	
	public void loadTexture() {
		
		try {
			
			FileInputStream fis = new FileInputStream(
					new File("resources/textures/"+ textureFilename));
			texture = TextureLoader.getTexture("PNG", fis);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public void bindTexture() {
		texture.bind();
	}

}
