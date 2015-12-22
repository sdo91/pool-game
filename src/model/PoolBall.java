package model;

import java.awt.Color;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import controller.WindowController;

public class PoolBall {
	
	public static final double RADIUS = .28;
	
	public Point2d position;
	public Vector2d trajectory;
	public double speed;
	
	public static final double shortHalfLength = 2.91;
	public static final double longHalfLength = 7.21;
	
	public Color color = null;
	
	private final Vector2d baseDirection = new Vector2d(0, -1);
	
	
	public PoolBall(double x, double z, Color color) {
		position = new Point2d(x, z);
		trajectory = new Vector2d(baseDirection);
		speed = 0;
		
		this.color = color;
	}

	public void moveBall() {
		Vector2d delta = new Vector2d(trajectory);
		delta.scale(speed);
		position.add(delta);
		speed -= .002;
		if (speed < 0) {
			speed = 0;
		}
		
		if (Math.abs(position.x) >= longHalfLength) {
			trajectory.x *= -1;
		}
		if (Math.abs(position.y) >= shortHalfLength) {
			trajectory.y *= -1;
		}
		
	}
	
	public void hitBall(double theta, double shotPower) {
		// bound theta
		theta %= 360;
		theta += 360;
		theta %= 360;
		setTrajectory(theta);
		
		speed = shotPower;
		
	}
	
	public void setTrajectory(double theta) {
		//System.out.println("traj");
		//System.out.println(theta);
		trajectory.x =  Math.sin(Math.toRadians(theta));
		trajectory.y = -Math.cos(Math.toRadians(theta));
		trajectory.normalize();
	}
	
	public Vector2d getVelocity() {
		Vector2d result = new Vector2d(trajectory);
		result.scale(speed);
		return result;
	}
	
	public double getAngle() {
		
		double angDeg = Math.toDegrees(baseDirection.angle(trajectory));
		
		
		if (trajectory.x > 0) {
			return angDeg;
		}
		else {
			return 360 - angDeg;
		}
		
		
		
	}

	public void setVelocity(Vector2d velocity) {
		
		speed = velocity.length();
		
		trajectory = new Vector2d(velocity);
		trajectory.normalize();
		
	}
	
	

}
