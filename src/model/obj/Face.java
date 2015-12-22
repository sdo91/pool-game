package model.obj;

import java.util.ArrayList;
import java.util.List;

public class Face {

	public List<Integer> vertexIndices;
	public List<Integer> normalIndices;
	public List<Integer> textureIndices;
	
	public Face() {
		this.vertexIndices = new ArrayList<>();
		this.normalIndices = new ArrayList<>();
		this.textureIndices = new ArrayList<>();
	}
}
