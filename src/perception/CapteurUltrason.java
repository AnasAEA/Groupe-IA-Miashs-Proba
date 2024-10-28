package perception; //importation des classes necessaires
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class CapteurUltrason { // initialisation des attributs 
	Float distanceActuelle = Float.MAX_VALUE; //distance actuelle
	Float distancePrecedente = Float.MAX_VALUE; //la dernière distance qui a été captée avant celle actuelle
	EV3UltrasonicSensor ultrasonicSensor;  //instance de la classe prédéfinie EV3UltrasonicSensor
    SampleProvider distanceProvider; //echantillon de distance donné par le capteur ultrason
    float[] samples;  //tableau qui stocke les distances.
	
	
	
	public CapteurUltrason(Port sensorPort) {   //initialisation de l'attribut sensor port qui permet l'accés au moteur
		this.ultrasonicSensor = new EV3UltrasonicSensor(sensorPort);
		this.distanceProvider = ultrasonicSensor.getDistanceMode();
		this.samples = new float[distanceProvider.sampleSize()]; 		
	}
		
	public float getDistance() {                  //récupère la distance captée par l'ultrason et la stocke dans le tableau après une conversion en cm
        distanceProvider.fetchSample(samples, 0); 
        float distanceCm = samples[0] * 100; 
        return distanceCm; 
    }
	
	
	public boolean detectPalet(){                  //permet de distinguer si l'objet en face du capteur est un palet ou non,                                    
		distancePrecedente = distanceActuelle; //en comparant les distances captées de getDistance() à la distance minimale à partir de laquelle le palet peut etre detectée
		distanceActuelle = this.getDistance();
		System.out.println("Distance Actuelle  = "+distanceActuelle);
		Float minDetection = (float) 32.6;
		return (distancePrecedente <= minDetection && distanceActuelle >= minDetection); 
	}
	
	
	public static void main(String[] args) throws InterruptedException { //main pour tester 
		// TODO Auto-generated method stub
		CapteurUltrason ul = new CapteurUltrason();
		
		for(int i = 0; i<15;i++) {
			ul.detectPalet();
			Thread.sleep(1000);
		}
		
		
	}
}
