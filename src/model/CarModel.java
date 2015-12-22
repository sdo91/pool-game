package model;

public class CarModel {
	
	// put angles, and coords and states here
	// can be public for now
	
	// SINGLETON STUFF
	private static CarModel instance = null;
	private static final double moveDist = 0.1;
	
	public static CarModel getInst() {
		if (instance == null) {
			instance = new CarModel();
		}
		return instance;
	}
	
	private CarModel() {
		carX = 0;
		carZ = 0;
		carTheta = 0;
	}
	
	public void moveX(double amount) {
		carX += moveDist * amount;
	}
	
	public void moveZ(double amount) {
		carZ += moveDist * amount;
	}
	
	public void changeTheta(double tireAngle) {
		carTheta += tireAngle/20.0;
		carTheta %= 360;
	}
	
	public double carX;
	public double carZ;
	public double carTheta;

}
