package controller;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;

import model.CarModel;

public class Input {
	
	private static Controller gamepad = null;
	private static boolean joysticksReady = false;
	private static double joystickThreshold = .1;
	private static final double rotFactor = 2;
	private static final double mvmtFactor = .15;
	
	private static int oldTime = 0;
	
	private static double shotPower = 0.0;
	
	public static void initController() {
		try {
			
			Controllers.create();
			int controllerCount = Controllers.getControllerCount();
			
			if (controllerCount > 0) {
				System.out.println(String.format("found %d controllers.", controllerCount));
				
				for (int i = 0; i < controllerCount; i++) {
					int numButtons = Controllers.getController(i).getButtonCount();
					System.out.println(String.format("Controller %d has %d buttons", i, numButtons));
					System.out.println(Controllers.getController(i).getName());
					
					if (Controllers.getController(i).getName().equals("Logitech RumblePad 2 USB")) {
					//if (numButtons == 12) { // logitech
						gamepad = Controllers.getController(i);
					}
				}
			}
			
			if (gamepad == null) {
				System.out.println("Gamepad not found");
			}
			
			
			
			
		} catch (LWJGLException e) {
			e.printStackTrace();
			gamepad = null;
		}
	}
	
	public static void processInput(WindowController wc) {
		//System.out.println("processing input");
		// keyboard / controller
		
		if (!joysticksReady && gamepad != null) {
			if(gamepad.getAxisValue(0) != -1 &&
					gamepad.getAxisValue(1) != -1 &&
					gamepad.getAxisValue(2) != -1 &&
					gamepad.getAxisValue(3) != -1) {
				joysticksReady = true;
				
				System.out.println("joysticks ready!");
			}
		}
		
		
		if (joysticksReady) {
			
			// joysticks
			if (Math.abs(gamepad.getAxisValue(0)) > joystickThreshold) {
				//System.out.println("right U/D");
				double value = gamepad.getAxisValue(0);
				//System.out.println(value);
				wc.xRotationAngle += value * rotFactor;
			}
			if (Math.abs(gamepad.getAxisValue(1)) > joystickThreshold) {
				//System.out.println("right L/R");
				double value = gamepad.getAxisValue(1);
				//System.out.println(value);
				wc.yRotationAngle += value * rotFactor;
			}
			if (Math.abs(gamepad.getAxisValue(2)) > joystickThreshold) {
				//System.out.println("left U/D");
				double value = gamepad.getAxisValue(2) * mvmtFactor;
				
				//double slowDownMvtFactor = 1;
				
				// move forward / backward
				wc.zCoord -= Math.cos(Transforms.rad(wc.yRotationAngle)) * value;
				wc.xCoord += Math.sin(Transforms.rad(wc.yRotationAngle)) * value;
				
			}
			if (Math.abs(gamepad.getAxisValue(3)) > joystickThreshold) {
				//System.out.println("left L/R");
				double value = gamepad.getAxisValue(3) * mvmtFactor;
				
				wc.xCoord -= Math.cos(Transforms.rad(wc.yRotationAngle)) * value;
				wc.zCoord -= Math.sin(Transforms.rad(wc.yRotationAngle)) * value;
			}
			
			// move U/D with d pad
			if (gamepad.getPovY() != 0) {
				
				double scaleFact = .05;
				wc.yCoord += scaleFact * gamepad.getPovY();
			}
			
			// shoot with 2
			if (gamepad.isButtonPressed(1)) {
				// charge shot
				shotPower += .01;
				if (shotPower > .5) {
					shotPower = .5;
				}
			}
			else if (shotPower != 0) {
				wc.cueBall.hitBall(wc.yRotationAngle, shotPower);
				shotPower = 0;
			}
			
			
			// turn arrow off/on w/ 2/3
			if (gamepad.isButtonPressed(2)) {
				wc.showArrow = false;
			}
			if (gamepad.isButtonPressed(3)) {
				wc.showArrow = true;
			}
			
			
			// TURN WHEELS
			//doWheelsOld(wc);
			wc.wheelTurnAmount = gamepad.getPovX()*-30.0;
			//System.out.println(String.format("wta: %f", wc.wheelTurnAmount));
			
			if (gamepad.isButtonPressed(7) || gamepad.isButtonPressed(1)) {
				//System.out.println("forward");
				CarModel cm = CarModel.getInst();
				
				if(wc.wheelTurnAmount != 0) {
					cm.changeTheta(wc.wheelTurnAmount);
				}
				
				cm.moveX(-Math.sin(Transforms.rad(cm.carTheta)));
				cm.moveZ(-Math.cos(Transforms.rad(cm.carTheta)));
			}
			if (gamepad.isButtonPressed(6) || gamepad.isButtonPressed(2)) {
				//System.out.println("backward");
				CarModel cm = CarModel.getInst();
				
				if(wc.wheelTurnAmount != 0) {
					cm.changeTheta(-wc.wheelTurnAmount);
				}
				
				cm.moveX(Math.sin(Transforms.rad(cm.carTheta)));
				cm.moveZ(Math.cos(Transforms.rad(cm.carTheta)));
			}
			
			//findButtons();
		}
		else { // gamepad not ready, use keyboard
			
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				wc.yRotationAngle -= 1;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				wc.yRotationAngle += 1;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				wc.xRotationAngle -= 1;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				wc.xRotationAngle += 1;
			}
		}
		
		
	}
	
	private static void doWheelsOld(WindowController wc) {
		if (!gamepad.isButtonPressed(0) && !gamepad.isButtonPressed(2) || 
				gamepad.isButtonPressed(0) && gamepad.isButtonPressed(2)) {
			wc.wheelTurnAmount = 0;
		}
		else if (gamepad.isButtonPressed(0)) {
			wc.wheelTurnAmount = -1;
		}
		else if (gamepad.isButtonPressed(2)) {
			wc.wheelTurnAmount = 1;
		}
	}
	
	/** subtract 1 from printed # on controller to get a button's id
	 */
	private static void findButtons() {
		// to find buttons
		//System.out.println(System.currentTimeMillis());
		int newTime = (int) (System.currentTimeMillis() / (long)1000);
		
		if (newTime != oldTime)	{
			oldTime = newTime;
			
			
			System.out.println(String.format("find buttons on '%s': ", gamepad.getName()));
			for (int i = 0; i < gamepad.getButtonCount(); i++) {
				if (gamepad.isButtonPressed(i)) {
					System.out.println(i);
				}
			}
			
			
			/*System.out.println("dpad:");
			System.out.println(gamepad.getPovX());
			System.out.println(gamepad.getPovY());*/
			
			/*System.out.println("joysticks:");
			System.out.println(gamepad.getXAxisValue());
			System.out.println(gamepad.getYAxisValue());
			System.out.println(gamepad.getZAxisValue());
			System.out.println(gamepad.getRXAxisValue());
			System.out.println(gamepad.getRYAxisValue());
			System.out.println(gamepad.getRZAxisValue());*/
		}
	}

}
