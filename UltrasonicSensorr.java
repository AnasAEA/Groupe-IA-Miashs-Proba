import lejos.hardware.Button;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;
import java.util.ArrayList;

public class UltrasonicSensorr {
	
	EV3UltrasonicSensor ultrasonicSensor;
    SampleProvider distanceProvider;
    ArrayList<Float> samplesCm;
    float[] samples;
	int indice;
	MovePilot pilot;
	
	public UltrasonicSensorr() {
		this.samplesCm = new ArrayList<Float>();
		this.ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);
		this.distanceProvider = ultrasonicSensor.getDistanceMode();
		this.samples = new float[distanceProvider.sampleSize()]; 
		this.indice = 0;
		
		Wheel wheelLeft = WheeledChassis.modelWheel(Motor.A, 5.6).offset(-6); // Modifier la taille et l'offset selon les roues du robot
		Wheel wheelRight = WheeledChassis.modelWheel(Motor.B, 5.6).offset(6);
		WheeledChassis chassis = new WheeledChassis(new Wheel[]{wheelLeft, wheelRight}, WheeledChassis.TYPE_DIFFERENTIAL);
		this.pilot = new MovePilot(chassis);
	}
		
	public float getDistance() {
        distanceProvider.fetchSample(samples, 0); 
        float distanceCm = samples[0] * 100; 
        
        samplesCm.add(distanceCm); 
        indice++;
        return distanceCm; 
    }
	
	public void avancer(double distance) {
	    pilot.setLinearSpeed(10); // Définit la vitesse linéaire (ajuste selon les besoins)
	    pilot.travel(distance);   // Déplace le robot de la distance en cm
	}
	
	/*public void avancer(double distance) {
	    // Variables pour la conversion de la distance en rotation de la roue
	    double wheelDiameter = 5.6; // diamètre de la roue en cm (par exemple)
	    double rotationPerCm = 360 / (Math.PI * wheelDiameter); // conversion cm en degrés

	    int rotationAngle = (int) (distance * rotationPerCm); // angle de rotation pour atteindre la distance

	    // Initialisation des moteurs
	    RegulatedMotor motorLeft = Motor.A;
	    RegulatedMotor motorRight = Motor.B;

	    // Définir la vitesse des moteurs
	    motorLeft.setSpeed(200); // ajuster la vitesse selon tes besoins
	    motorRight.setSpeed(200);

	    // Faire avancer les moteurs pour atteindre la distance
	    motorLeft.rotate(rotationAngle, true); // true permet de synchroniser les moteurs
	    motorRight.rotate(rotationAngle);

	    // Attendre la fin du mouvement des moteurs
	    motorLeft.waitComplete();
	    motorRight.waitComplete();
	}*/
	
	//public double getAngle(){
		
	//}
	
	public boolean detectPalet(){
		//while (getDistance() < 32.6) {
			if (samplesCm.get(indice) > samplesCm.get(indice - 1) || (samplesCm.get(indice - 1) >= 32.6)) 
				return true;
			//avancer(5);
		//}
		return false; 
	}
	
	//public boolean detectObjet(){
		
	//}
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		UltrasonicSensorr ul = new UltrasonicSensorr();
		
		//ul.avancer(30);
		//ul.detectPalet();
		//boolean b = ul.detectPalet();
		for(int i = 0; i<15;i++) {
			//System.out.println(b);
			System.out.println(ul.ultrasonicSensor.getDistanceMode());
			Thread.sleep(1000);
		}
		
		
	}

}
