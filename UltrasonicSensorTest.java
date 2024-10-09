import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class UltrasonicSensorTest {

    public static void main(String[] args) {

        Port port = LocalEV3.get().getPort("S1");

        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(port);

        SampleProvider distanceProvider = ultrasonicSensor.getDistanceMode();

        float[] sample = new float[distanceProvider.sampleSize()];

        float previousDistance = -1;
        

        while (Button.ESCAPE.isUp()) {
           
            distanceProvider.fetchSample(sample, 0);
            float currentDistance = sample[0] * 100; 
            
            if (!(currentDistance == previousDistance) && currentDistance < 255) {
                System.out.println(+ currentDistance );

                previousDistance = currentDistance;
            }

            Delay.msDelay(500);
        }

        ultrasonicSensor.close();
    }
}
