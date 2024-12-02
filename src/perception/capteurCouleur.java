package perception;
import lejos.hardware.Button;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


public class capteurCouleur {

    private EV3ColorSensor colorSensor;

     // Pour eviter les erreurs de detections , on normalise les valeurs 
 // Normalized calibration data for colors
    private static final float[] BLUE_CALIBRATION = {0.1035f, 0.4824f, 0.4137f};
    private static final float[] BLACK_CALIBRATION = {0.0f, 1.0f, 0.0f};
    private static final float[] WHITE_CALIBRATION = {0.45f, 0.3735f, 0.1765f};

 // on definit les tolerances pour chaque couleur
    private static final float[] BLUE_TOLERANCE = {0.1f, 0.15f, 0.1f};
    private static final float[] BLACK_TOLERANCE = {0.05f, 0.1f, 0.05f};
    private static final float[] WHITE_TOLERANCE = {0.1f, 0.1f, 0.1f};
        // Constructeur de ColorSensor
    public capteurCouleur(Port port) {
        colorSensor = new EV3ColorSensor(port);
        colorSensor.setFloodlight(Color.WHITE);
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
