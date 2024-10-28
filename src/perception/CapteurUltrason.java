package perception;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class CapteurUltrason {
	
	Float distanceActuelle = Float.MAX_VALUE;
	Float distancePrecedente = Float.MAX_VALUE;
	EV3UltrasonicSensor ultrasonicSensor;
    SampleProvider distanceProvider;
    float[] samples;
	
	public CapteurUltrason(Port sensorPort) {
		this.ultrasonicSensor = new EV3UltrasonicSensor(sensorPort);
		this.distanceProvider = ultrasonicSensor.getDistanceMode();
		this.samples = new float[distanceProvider.sampleSize()]; 		
	}
		
	public float getDistance() {
        distanceProvider.fetchSample(samples, 0); 
        float distanceCm = samples[0] * 100; 
        return distanceCm; 
    }
	
	
	public boolean detectPalet(){
		distancePrecedente = distanceActuelle;
		distanceActuelle = this.getDistance();
		System.out.println("Distance Actuelle  = "+distanceActuelle);
		Float minDetection = (float) 32.6;
		return (distancePrecedente <= minDetection && distanceActuelle >= minDetection); 
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		CapteurUltrason ul = new CapteurUltrason();
		
		for(int i = 0; i<15;i++) {
			ul.detectPalet();
			Thread.sleep(1000);
		}
		
		
	}
}
