package action;


import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.chassis.*;

public class Deplacement {
  	private MovePilot pilot;
	private float direction =0;
	
	
	public Deplacement() {
		Wheel roue1 = WheeledChassis.modelWheel((RegulatedMotor)Motor.B,DIAMETRE ).offset(-6.2);
		Wheel roue2 = WheeledChassis.modelWheel((RegulatedMotor)Motor.A, DIAMETRE).offset(6.2);
		Chassis chassis = new WheeledChassis(new Wheel[] {roue1, roue2}, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
	}
	
	public float getDirection() { //renvoie la direction en degres
		return direction;
	}
	
	public MovePilot getPilot() { 
		return pilot;
	}

	public void avancerSync(double distance) { //Avancement asynchrone de la distance en paramètres
   		 pilot.travel(distance, true); 
	}

	public void tournerAsync(double angle) { //Rotation asynchrone de l’angle en paramètre
	        pilot.rotate(angle, true);
	        direction += angle;
	        direction = direction % 360;
    	}

	public void modifVitRot(double s){ //modifie la vitesse de rotation en degrés par seconde
		pilot.setAngularSpeed(s);
	}
	
	public void stop() {
		pilot.stop();
	}
}
