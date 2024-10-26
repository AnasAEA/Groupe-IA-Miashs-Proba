import lejos.hardware.Button;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import java.util.ArrayList;

public class UltrasonicSensorr {
	
	EV3UltrasonicSensor ultrasonicSensor;
    SampleProvider distanceProvider;
    ArrayList<Float> samplesCm;
    float[] samples;
	int indice;
	
	public UltrasonicSensorr() {
		this.samplesCm = new ArrayList<Float>();
		this.samples = new float[distanceProvider.sampleSize()];
		this.ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
		this.distanceProvider = ultrasonicSensor.getDistanceMode();
		this.indice = 0;
		
	}
		
	public float getDistance() {
        distanceProvider.fetchSample(samples, 0); 
        float distanceCm = samples[0] * 100; 
        samplesCm.add(distanceCm); 
        indice++;
        return distanceCm; 
    }
	
	//public double getAngle(){
		
	//}
	
	public boolean detectPalet(){
		if (samplesCm.get(indice) > samplesCm.get(indice - 1) && (samplesCm.get(indice - 1) >= 32.6)) 
			return true; 
		return false; 
	}
	
	public boolean detectObjet(){
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
