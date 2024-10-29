package perception; //importation des classes necessaires
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class CapteurUltrason { // initialisation des attributs 
	Float distanceActuelle = Float.MAX_VALUE; //distance actuelle
	Float distancePrecedente = Float.MAX_VALUE; //la derniÃ¨re distance qui a Ã©tÃ© captÃ©e avant celle actuelle
	EV3UltrasonicSensor ultrasonicSensor;  //instance de la classe prÃ©dÃ©finie EV3UltrasonicSensor
    SampleProvider distanceProvider; //echantillon de distance donnÃ© par le capteur ultrason
    float[] samples;  //tableau qui stocke les distances.
	
	
	
	public CapteurUltrason(Port sensorPort) {   //initialisation de l'attribut sensor port qui permet l'accÃ©s au moteur
		this.ultrasonicSensor = new EV3UltrasonicSensor(sensorPort);
		this.distanceProvider = ultrasonicSensor.getDistanceMode();
		this.samples = new float[distanceProvider.sampleSize()]; 		
	}
		
	public float getDistance() {                  //rÃ©cupÃ¨re la distance captÃ©e par l'ultrason et la stocke dans le tableau aprÃ¨s une conversion en cm
        distanceProvider.fetchSample(samples, 0); 
        float distanceCm = samples[0] * 100; 
        return distanceCm; 
    }
	
	
	public boolean detectPalet(){                  //permet de distinguer si l'objet en face du capteur est un palet ou non,                                    
		distancePrecedente = distanceActuelle; //en comparant les distances captÃ©es de getDistance() Ã  la distance minimale Ã  partir de laquelle le palet peut etre detectÃ©e
		distanceActuelle = this.getDistance();
//		System.out.println("Distance Actuelle  = "+distanceActuelle);
		Float minDetection = (float) 32.6;
		return (distancePrecedente <= minDetection && distanceActuelle >= minDetection); 
	}
	
	
	public static void main(String[] args) throws InterruptedException { //main pour tester 
		// TODO Auto-generated method stub
		CapteurUltrason ul = new CapteurUltrason(SensorPort.S1);
		for(int i = 0; i<15;i++) {
			ul.detectPalet();
			Thread.sleep(1000);
		}
		
		
	}
}
