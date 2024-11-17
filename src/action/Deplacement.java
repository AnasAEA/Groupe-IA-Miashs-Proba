package action;


import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;
import lejos.robotics.chassis.*;



public class Deplacement {
	
	private static final double DIAMETRE = 5.6;
  	private MovePilot pilot;
    	private PoseProvider poseProvider;
	
	
	public Deplacement() {
		Wheel roue1 = WheeledChassis.modelWheel((RegulatedMotor)Motor.B,DIAMETRE ).offset(-6.2);
		Wheel roue2 = WheeledChassis.modelWheel((RegulatedMotor)Motor.A, DIAMETRE).offset(6.2);
		Chassis chassis = new WheeledChassis(new Wheel[] {roue1, roue2}, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
        poseProvider = new OdometryPoseProvider(pilot); // Initialisation du PoseProvider

	}
	
    	public float getDirection() { // Renvoie la direction en degrés
        	Pose pose = poseProvider.getPose();
       		return pose.getHeading(); // Récupère l'orientation actuelle du robot
    	}
	
	public MovePilot getPilot() { 
		return pilot;
	}

	public void avancerSync(double distance) { //Ava ncement asynchrone de la distance en paramètres
		 //this.getPilot().setAngularSpeed(30);
   		 pilot.travel(distance, true); 
	}
	
	public void avancer(double distance) { //Avancement asynchrone de la distance en paramètres
  		 pilot.travel(distance); 
	}

	public void tournerAsync(double angleToTurn) { //Rotation asynchrone de l’angle en paramètre
		this.getPilot().setAngularSpeed(30);
		if (angleToTurn > 0) {
              	       angleToTurn -= 7; // If turning clockwise, reduce angle
            	} else if (angleToTurn < 0) {
               	       angleToTurn += 7; // If turning counter-clockwise, increase angle
            	}
		pilot.rotate(angleToTurn, true);
	        // Pas besoin de mettre à jour 'direction' ici ; le PoseProvider s'en charge
    	}

	public void modifVitRot(double s){ //modifie la vitesse de rotation en degrés par seconde
		pilot.setAngularSpeed(s);
	}
	
	public boolean isMoving() { 
		 return pilot.isMoving();
	 }

	public void stop() {
		pilot.stop();
	}
}
