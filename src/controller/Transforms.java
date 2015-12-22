package controller;

import static org.lwjgl.opengl.GL11.*;

import java.nio.Buffer;
import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;

import model.CarModel;
import model.PoolBall;

public class Transforms {
	
	private static double ySpin = 0;
	private static DoubleBuffer matrix = BufferUtils.createDoubleBuffer(16); 
	
	
	public static void moveCamera(WindowController wc) {
		
		//translate(.545, 0, 0); // front tires
		//translate(-.475, 0, 0); // rear tires
		//translate(-.38, 0, 0); // driver side
		
		//translate(0, -.5, -10);
		
		
		rotate(wc.xRotationAngle, 1, 0, 0);
		rotate(wc.yRotationAngle, 0, 1, 0);
		
		translate(wc.xCoord, wc.yCoord, wc.zCoord);
		
		//rotate(90, 1, 0, 0); // top view
		//rotate(ySpin, 0, 1, 0); // y spin
		
		ySpin += .05;
		ySpin %= 360;
	}
	
	public static void tireToCar(int tireId, WindowController wc) {
		
		double driverSide = -.38;
		double tireHeight = .13;
		double frontTires = -.545;
		double rearTires = .475;
		
		switch(tireId) {
		
		case 1:
			translate(driverSide, tireHeight, frontTires);
			break;
		case 2:
			translate(-driverSide, tireHeight, frontTires);
			break;
		case 3:
			translate(-driverSide, tireHeight, rearTires);
			break;
		case 4:
			translate(driverSide, tireHeight, rearTires);
			break;
		default:
			new Exception().printStackTrace();
			break;
			
		}
		
		if (tireId == 1 || tireId == 4) {
			rotate(180, 0, 1, 0);
		}
		
		if (tireId <= 2) { // front wheels
			rotate(wc.wheelTurnAmount, 0, 1, 0);
//			if (wc.wheelTurnAmount == -1) {
//				//rotate(30, 0, 1, 0); // left
//				rotate(30, 0, 1, 0); // left
//			}
//			if (wc.wheelTurnAmount == 1) {
//				rotate(-30, 0, 1, 0); // right
//			}
		}
		
		double tireScale = .24;
		scaleUniform(tireScale);
	}
	
	public static void lotToWorld() {
		translate(-5.25, 0, 5.3);
		rotate(-60, 0, 1, 0);
	}
	
	public static void carToWorldOrigin() {
		//rotate(ySpin, 1, 1, 1);
		//translate(0, 0, 0);
		double carScale = 1.5;
		scaleUniform(carScale);
	}
	
	public static void driveCar() {
		//translate(-5, 0, 0);
		//rotate(ySpin, 0, 1, 0);
		//translate(5, 0, 0);
		// flip all 1st signs to rotate other direction
		
		
		CarModel cm = CarModel.getInst();
		
		translate(cm.carX, 0, cm.carZ);
		rotate(cm.carTheta, 0, 1, 0);
		
		
	}
	
	
	
	public static void playerToWorld() {
		translate(0, .5, 0);
		
		rotate(180, 0, 1, 0);
		
		double scaleFactor = 0.5;
		scaleUniform(scaleFactor);
	}
	
	public static void rotate(double theta, double x, double y, double z) {
		double alpha = Math.atan(z/x);
		if (Double.isNaN(alpha)) { alpha = 0; }
		
		double x_new = Math.cos(alpha)*x + Math.sin(alpha)*y;
		double x_new2 = Math.sqrt(x*x+z*z);
		if (x_new != x_new2) {
			System.out.print("x_new might be wrong: ");
			System.out.println(String.format("%s, %s", x_new, x_new2));
		}
		
		double beta = Math.atan(x_new/y);
		if (Double.isNaN(beta)) { beta = 0; }
		
		
		// I think the minus signs are first...
		rotateY(-deg(alpha));
		rotateZ(-deg(beta));
		rotateY(theta);
		rotateZ(deg(beta));
		rotateY(deg(alpha));
	}
	
	public static void rotateY(double theta) {
		matrix.clear();
		double[] values = {
				Math.cos(rad(theta)),0,-Math.sin(rad(theta)),0,
				0,1,0,0,
				Math.sin(rad(theta)),0,Math.cos(rad(theta)),0,
				0,0,0,1};
		
		matrix.put(values);
		matrix.flip();
		
		glMultMatrix(matrix);
	}
	
	public static void rotateZ(double theta) {
		matrix.clear();
		double[] values = {
				Math.cos(rad(theta)),Math.sin(rad(theta)),0,0,
				-Math.sin(rad(theta)),Math.cos(rad(theta)),0,0,
				0,0,1,0,
				0,0,0,1};
		
		matrix.put(values);
		matrix.flip();
		
		glMultMatrix(matrix);
	}
	
	public static void translate2d(double x, double z) {
		translate(x, 0, z);
	}
	
	public static void translate(double x, double y, double z) {
		
		matrix.clear();
		double[] values = {
				1,0,0,0,
				0,1,0,0,
				0,0,1,0,
				x,y,z,1};
		
		matrix.put(values);
		matrix.flip();
		
		glMultMatrix(matrix);
	}
	
	public static void scaleUniform(double scaleFactor) {
		scale(scaleFactor, scaleFactor, scaleFactor);
	}
	
	public static void scale(double x, double y, double z) {
		
		matrix.clear();
		double[] values = {
				x,0,0,0,
				0,y,0,0,
				0,0,z,0,
				0,0,0,1};
		
		matrix.put(values);
		matrix.flip();
		
		glMultMatrix(matrix);
	}
	
	public static double deg(double radians) {
		return radians*180/Math.PI;
	}
	
	public static double rad(double degrees) {
		return degrees*Math.PI/180;
	}
	
	
	
	public static void arrowToBall(WindowController wc) {
		
		
		//rotate about origin
		rotateY(-wc.yRotationAngle);
		//trans from cue
		translate2d(0,-1.5);
		
		// scale 1st
		scale(.25, .05, .25);
		
	}
	
	public static void adjustSurface() {
		translate(0, .5, 0);
		
	}

	public static void translateBallToTablePosition(PoolBall poolBall) {
		translate2d(poolBall.position.x, poolBall.position.y); // y is z
	}

	

	

	

}
