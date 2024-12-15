package perception;
import lejos.hardware.Button;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


public class CapteurCouleur {

    private EV3ColorSensor colorSensor;

     
        // Constructeur de ColorSensor
    public CapteurCouleur(Port port) {
        colorSensor = new EV3ColorSensor(port);
        colorSensor.setFloodlight(true);
    }

    public int getColorID() {
        SampleProvider colorIDProvider = colorSensor.getColorIDMode();
        float[] colorSample = new float[colorIDProvider.sampleSize()];
        colorIDProvider.fetchSample(colorSample, 0);
        int colorID = (int) colorSample[0];
        return colorID;
    }

     public String getColorName() {
        int colorID = getColorID();

        switch (colorID) {
            case Color.NONE:
                return "None";
            case Color.BLACK:
                return "Black";
            case Color.BLUE:
                return "Blue";
            case Color.GREEN:
                return "Green";
            case Color.YELLOW:
                return "Yellow";
            case Color.RED:
                return "Red";
            case Color.WHITE:
                return "White";
            case Color.BROWN:
                return "Brown";
            default:
                return "Unknown";
        }
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


   

    // Method to close the sensor
    public void close() {
        colorSensor.close();
    }
    
}
