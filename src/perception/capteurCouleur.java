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
    public boolean isColor(String colorName) {
        return getColorName().equalsIgnoreCase(colorName);
    }

    public boolean isWhite() {
        return getColorID() == Color.WHITE;
    }

    public boolean isBlack() {
        return getColorID() == Color.BLACK;
    }

    public boolean isBlue() {
        return getColorID() == Color.BLUE;
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
