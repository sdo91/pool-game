package controller;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashSet;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import javax.xml.bind.ValidationEvent;

import org.lwjgl.opengl.*;

import model.PoolBall;
import model.obj.ObjLoader;
import model.obj.ObjModel;
import model.obj.ObjRenderer;

import org.lwjgl.*;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;




public class WindowController {
	
	private ObjModel carModel;
	private ObjModel lotModel;
	private ObjModel tireModel;
	private ObjModel playerModel;
	private ObjModel a10Model;
	
	private ObjModel poolTableModel;
	private ObjModel tableSurfaceModel;
	private ObjModel ballModel;
	private ObjModel arrowModel;
	
	public ArrayList<PoolBall> poolBalls;
	public PoolBall cueBall = null;
	
	private ArrayList<Point2d> pockets;
	public boolean showArrow = true;
	
	//public static final int DISPLAY_WIDTH = 640;
	//public static final int DISPLAY_HEIGHT = 480;
	public static final int DISPLAY_WIDTH = 1024;
	public static final int DISPLAY_HEIGHT = 576;
	public static final int FPS = 60;
	
	public double yRotationAngle = 0;
	protected double xRotationAngle = 0;
	protected double xCoord = 0;
	protected double yCoord = -3.0;
	protected double zCoord = -10;
	
	protected double wheelTurnAmount = 0; // Negative for right?
	
	public WindowController() {
		
		try {
			
            Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
            //Display.setFullscreen(true);
            Display.setTitle("CS 455 - Final Project");
            Display.create();
            
            
            Keyboard.create();
            
            Input.initController();
            
            
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
		
		initPerspective();
		
        /*
        carModel = ObjLoader.loadModel("car.obj", "car.png");
        lotModel = ObjLoader.loadModel("ParkingLot.obj", "ParkingLotKody.png");
        tireModel = ObjLoader.loadModel("tire.obj", "tire.png");
        playerModel = ObjLoader.loadModel("playerTris.obj");
        a10Model = ObjLoader.loadModel("A10.obj");
        */
        
        poolTableModel = ObjLoader.loadModel("poolTable.obj", "mahogany.png");
        tableSurfaceModel = ObjLoader.loadModel("tableSurface.obj", "felt.png");
        ballModel = ObjLoader.loadModel("ball.obj");
        arrowModel = ObjLoader.loadModel("arrow.obj");
        
        poolBalls = new ArrayList<>();
        poolBalls.add(new PoolBall(5, 0, Color.WHITE)); // the cue
        
        // colored balls
        double xSide = .6/Math.sqrt(3);
        poolBalls.add(new PoolBall(-4, 0, Color.YELLOW)); // the 1
        
        poolBalls.add(new PoolBall(-4.6, -xSide, Color.BLUE)); // the 2
        poolBalls.add(new PoolBall(-4.6, xSide, Color.RED)); // the 3
        
        poolBalls.add(new PoolBall(-5.2, -2*xSide, Color.MAGENTA)); // the 4
        poolBalls.add(new PoolBall(-5.2, 0, Color.CYAN)); // the 5
        poolBalls.add(new PoolBall(-5.2, 2*xSide, Color.GREEN)); // the 6
        
        cueBall = poolBalls.get(0);
        
        // pockets
        pockets = new ArrayList<>();
        pockets.add(new Point2d(-PoolBall.longHalfLength, -PoolBall.shortHalfLength));
        pockets.add(new Point2d(0, -PoolBall.shortHalfLength));
        pockets.add(new Point2d(PoolBall.longHalfLength, -PoolBall.shortHalfLength));
        pockets.add(new Point2d(PoolBall.longHalfLength, PoolBall.shortHalfLength));
        pockets.add(new Point2d(0, PoolBall.shortHalfLength));
        pockets.add(new Point2d(-PoolBall.longHalfLength, PoolBall.shortHalfLength));
        
        
	}
	
	
	
	private void initPerspective() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		gluPerspective(45.0f, 
				(float)DISPLAY_WIDTH/DISPLAY_HEIGHT, 
				0.1f, 100.0f);
		
		glMatrixMode(GL_MODELVIEW);
	}
	
		
	
	
	
	public void run() {
		
		
		glClearDepth( 1.0f ); /* Depth buffer setup */
		glEnable( GL_DEPTH_TEST ); /* Enables Depth Testing */
		glDepthFunc( GL_LEQUAL ); /* The Type Of Depth Test To Do */
		
		// lighting
		glShadeModel(GL_SMOOTH);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_COLOR_MATERIAL);
		FloatBuffer fb = BufferUtils.createFloatBuffer(4);
		
		
		
		// dif
		fb.clear();
		float[] dif = {1.0f, 1.0f, 1.0f, 1.0f};
		fb.put(dif);
		fb.flip();
		glLight(GL_LIGHT0, GL_DIFFUSE, fb);
		
		// amb
		fb.clear();
		float[] amb = {0.2f, 0.2f, 0.2f, 1.0f};
		fb.put(amb);
		fb.flip();
		glLight(GL_LIGHT0, GL_AMBIENT, fb);
		
		while (!Display.isCloseRequested()) {
			
			Input.processInput(this);
            
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
            //glClear(GL_COLOR_BUFFER_BIT); // 2D
            
			glEnable(GL_TEXTURE_2D);
			Color c = Color.WHITE;
			glColor3d(c.getRed(), c.getGreen(), c.getBlue());
			
			
			/*
			// a10
			glLoadIdentity();
			Transforms.moveCamera(this);
			ObjRenderer.drawObject(a10Model, false);
            */
			
			glLoadIdentity();
			Transforms.moveCamera(this);
			// pos
			fb.clear();
			float[] pos = {0.0f,0.0f,10.0f,1.0f};
			fb.put(pos);
			fb.flip();
			glLight(GL_LIGHT0, GL_POSITION, fb);
			
			
			
			
			// table
			glLoadIdentity();
			Transforms.moveCamera(this);
			ObjRenderer.drawObject(poolTableModel, true);
			
			// surface
			glLoadIdentity();
			Transforms.moveCamera(this);
			//Transforms.adjustSurface();
			ObjRenderer.drawObject(tableSurfaceModel, true);
			
			// update balls
			// detect collisions
			//System.out.println();
			for (int i = 0; i < poolBalls.size(); i++) {
				PoolBall ballA = poolBalls.get(i);
				for (int j = i+1; j < poolBalls.size(); j++) {
					//System.out.println(String.format("collision? %d, %d", i, j));
					PoolBall ballB = poolBalls.get(j);
					
					if (isCollision(ballA, ballB)) {
						
						//System.out.println("collision!");
						// handle it
						handleCollision(ballA, ballB);
						
						while (isCollision(ballA, ballB)) {
							//System.out.println("avoid black hole!");
							ballA.moveBall();
							ballB.moveBall();
						}
					}
				}
			}
			// move balls
			boolean doneMoving = true;
			for (PoolBall ball : poolBalls) {
				if (ball.speed != 0) {
					doneMoving = false;
				}
				ball.moveBall();
			}
			
			
			
			// cue ball
			glDisable(GL_TEXTURE_2D);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			c = Color.WHITE;
			glColor3d(c.getRed(), c.getGreen(), c.getBlue());
			
			glLoadIdentity();
			Transforms.moveCamera(this);
			Transforms.translateBallToTablePosition(cueBall);
			ObjRenderer.drawObject(ballModel, false);
			
			// draw arrow
			if (doneMoving && showArrow) {
				c = Color.RED;
				glColor3d(c.getRed(), c.getGreen(), c.getBlue());
				glLoadIdentity();
				Transforms.moveCamera(this);
				Transforms.translateBallToTablePosition(cueBall);
				Transforms.arrowToBall(this);
				ObjRenderer.drawObject(arrowModel, false);
			}
			
			// draw other balls
			for (int i = 1; i < poolBalls.size(); i++) {
				glLoadIdentity();
				Transforms.moveCamera(this);
				Transforms.translateBallToTablePosition(poolBalls.get(i));
				c = poolBalls.get(i).color;
				glColor3d(c.getRed(), c.getGreen(), c.getBlue());
				ObjRenderer.drawObject(ballModel, false);
			}
			
			
			// check for pockets
			//HashSet<Integer> ballsSunk = new HashSet<>();
			for (Point2d pocket : pockets) {
				for (int i = 1; i < poolBalls.size(); i++) {
					PoolBall ball = poolBalls.get(i);
					
					double dist = pocket.distance(ball.position);
					if (dist < .3) {
						// sunk
						poolBalls.remove(i);
					}
				}
			}
			
			
			
            // update
            Display.update();
            Display.sync(FPS);
            
        }
		
        Display.destroy();
		
		
	}
	
	
	
	private boolean isCollision(PoolBall ballA, PoolBall ballB) {
		double dist = ballA.position.distance(ballB.position);
		return dist < PoolBall.RADIUS * 2; // true if collision
	}



	private void handleCollision(PoolBall ballA, PoolBall ballB) {
		
		// comps?
		double vx_a = ballA.getVelocity().x;
		double vy_a = ballA.getVelocity().y;
		double vx_b = ballB.getVelocity().x;
		double vy_b = ballB.getVelocity().y;
		
		// calc normal (and negNorm)
		Vector2d normal = new Vector2d();
		normal.sub(ballA.position, ballB.position);
		normal.normalize();
		
		Vector2d negNorm = new Vector2d(normal);
		negNorm.scale(-1);
		
		// normal comps
		double dotProd = ballA.getVelocity().dot(negNorm); 
		Vector2d vn_a = new Vector2d(negNorm);
		vn_a.scale(dotProd);
		
		dotProd = ballB.getVelocity().dot(normal);
		Vector2d vn_b = new Vector2d(normal);
		vn_b.scale(dotProd);
		
		// tan. comps
		Vector2d vt_a = new Vector2d();
		vt_a.sub(ballA.getVelocity(), vn_a);
		
		Vector2d vt_b = new Vector2d();
		vt_b.sub(ballB.getVelocity(), vn_b);
		
		// final velocities
		Vector2d vf_a = new Vector2d();
		vf_a.add(vt_a, vn_b);
		ballA.setVelocity(vf_a);
		
		Vector2d vf_b = new Vector2d();
		vf_b.add(vt_b, vn_a);
		ballB.setVelocity(vf_b);
		
		
		//int x = 5;
	}



	private void oldCode() {
		// render lot
		glLoadIdentity();
		Transforms.moveCamera(this);
		Transforms.lotToWorld();
		ObjRenderer.drawObject(lotModel, true);
		
		// render car
		glLoadIdentity();
		Transforms.moveCamera(this);
		Transforms.driveCar();
		Transforms.carToWorldOrigin();
		ObjRenderer.drawObject(carModel, true);
		
		// render tires
		for (int i = 1; i <= 4; i++) {
			glLoadIdentity();
			Transforms.moveCamera(this);
			Transforms.driveCar();
			Transforms.carToWorldOrigin();
			Transforms.tireToCar(i, this);
            ObjRenderer.drawObject(tireModel, true);
		}
		
		// render player
		glDisable(GL_TEXTURE_2D);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		Color c = Color.WHITE;
		glColor3d(c.getRed(), c.getGreen(), c.getBlue());
		
		// player transforms
		glLoadIdentity();
		Transforms.moveCamera(this);
		Transforms.playerToWorld();
		ObjRenderer.drawObject(playerModel, false);
	}


}

