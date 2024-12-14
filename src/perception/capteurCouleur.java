package perception;
import lejos.hardware.Button;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


public class capteurCouleur {

    private EV3ColorSensor colorSensor;

     
        // Constructeur de ColorSensor
    public capteurCouleur(Port port) {
        colorSensor = new EV3ColorSensor(port);
        colorSensor.setFloodlight(true);
    }


         //Methodes de detction des couleurs 
    public boolean isBlue() {
        return isColorMatch(BLUE_CALIBRATION, BLUE_TOLERANCE);
    }

    public boolean isBlack() {
        return isColorMatch(BLACK_CALIBRATION, BLACK_TOLERANCE);
    }

    public boolean isWhite() {
        return isColorMatch(WHITE_CALIBRATION, WHITE_TOLERANCE);
    }

    private boolean isColorMatch(float[] calibrationData, float[] tolerances) {
        float[] sample = getNormalizedSample();

        for (int i = 0; i < sample.length; i++) {
            if (Math.abs(sample[i] - calibrationData[i]) > tolerances[i]) {
                return false;
            }
        }
        return true;
    }

    private float[] getNormalizedSample() {
        SampleProvider rgbMode = colorSensor.getRGBMode();
        float[] sample = new float[rgbMode.sampleSize()];
        rgbMode.fetchSample(sample, 0);

        float sum = sample[0] + sample[1] + sample[2];
        if (sum == 0) sum = 1; // eviter la division par 0

        // Normalisation
        float[] normalizedSample = new float[3];
        normalizedSample[0] = sample[0] / sum;
        normalizedSample[1] = sample[1] / sum;
        normalizedSample[2] = sample[2] / sum;

        return normalizedSample;
    }

    // Method to close the sensor
    public void close() {
        colorSensor.close();
    }
    
    }
