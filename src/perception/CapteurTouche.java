package perception;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;

public class CapteurTouche {
	    private EV3TouchSensor touchSensor;
	    private SampleProvider touchSampleProvider;
	    private float[] sample;

	    public CapteurTouche(Port sensorPort) {
	        touchSensor = new EV3TouchSensor(sensorPort);
	        touchSampleProvider = touchSensor.getTouchMode();
	        sample = new float[touchSampleProvider.sampleSize()];
	    }

	    public boolean isPressed() {
	        touchSampleProvider.fetchSample(sample, 0);
	        // l'etat de capteurTouche se trouve a l'indice 0
	        return sample[0] > 0;
	    }
	    public void close() {
	        touchSensor.close();
	    }
}
