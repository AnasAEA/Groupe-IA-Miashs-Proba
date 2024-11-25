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

	public void avancerSync(double distance) { //Avancement synchrone de la distance en paramètres
   		 pilot.travel(distance, true); 
	}
	
	public void avancer(double distance) { //Avancement asynchrone de la distance en paramètres
  		 pilot.travel(distance); 
	}

	public void tournerAsync(double angleToTurn) { //Rotation synchrone de l’angle en paramètre
		this.modifVitRot(50);
		if (angleToTurn > 0) {
              	       angleToTurn += 13; // If turning counter-clockwise
            	} else if (angleToTurn < 0) {
               	       angleToTurn += 4; // If turning clockwise
            	}
		pilot.rotate(angleToTurn, true);
	        // Pas besoin de mettre à jour 'direction' ici ; le PoseProvider s'en charge
    	}

	public void tourner(double angleToTurn) { //Rotation asynchrone de l’angle en paramètre
		this.modifVitRot(50);
		if (angleToTurn > 0) {
              	       angleToTurn += 9; // If turning counter-clockwise
            	} 
		else if (angleToTurn < 0) {
               	       angleToTurn += 3; // If turning clockwise
            	}
		pilot.rotate(angleToTurn);
	        // Pas besoin de mettre à jour 'direction' ici ; le PoseProvider s'en charge
    	}

	//Methode Speciale pour les cas des rotations qui necessitent interagir avec le pose provider (existence d'une erreur )
	public void tourner(double angleToTurn, boolean heading) {
		if(heading) {
			if (angleToTurn > 0) {
	              	       angleToTurn +=13; //previous value -9
	            	} 
			else if (angleToTurn < 0) {
	               	       angleToTurn -= 2; //previous value 11
	            	}
		        // Pas besoin de mettre à jour 'direction' ici ; le PoseProvider s'en charge
			pilot.rotate(angleToTurn);
	    	}
		else {
			tourner(angleToTurn);
		}
	}

	public void modifVitRot(double s){ //modifie la vitesse de rotation en degrés par seconde
		pilot.setAngularSpeed(s);
	}
	public void modifVitLin(double s){
		pilot.setLinearSpeed(s);
	}
	
	public boolean isMoving() { 
		 return pilot.isMoving();
	 }

	public void stop() {
		pilot.stop();
	}
	
}
