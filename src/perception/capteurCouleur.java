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

    public void collectColorData() {
        SampleProvider rgbMode = colorSensor.getRGBMode();
        float[] sample  = new float[rgbMode.sampleSize()];
        	
        float[] blueCalibration = new float[3];
        float[] blackCalibration = new float[3];
        float[] whiteCalibration = new float[3];
        	
        //blue 
        System.out.println("Poser sur la ligne bleu  et click ENTRER:  ");
        Button.ENTER.waitForPressAndRelease();
        rgbMode.fetchSample(sample, 0);
        System.arraycopy(sample, 0, blueCalibration, 0, sample.length);
        System.out.println("Blue data collected");
        	
        //white 
        System.out.println("Poser sur la ligne blanche  et click ENTRER:  ");
        Button.ENTER.waitForPressAndRelease();
        rgbMode.fetchSample(sample, 0);
        System.arraycopy(sample, 0, whiteCalibration, 0, sample.length);
        System.out.println("White data collected");
        	
        //black
        System.out.println("Poser sur la ligne Noire  et click ENTRER:  ");
        Button.ENTER.waitForPressAndRelease();
        rgbMode.fetchSample(sample, 0);
        System.arraycopy(sample, 0, blackCalibration, 0, sample.length);
        System.out.println("Black data collected");
        	
        	
        //print the data 	
        System.out.println("BLUE: R= "+blueCalibration[0]+" G= "+ blueCalibration[1]+ " B= "+ blueCalibration[2]);
        System.out.println("BLACK: R= "+blackCalibration[0]+" G= "+ blackCalibration[1]+ " B= "+ blackCalibration[2]);
        System.out.println("WHITE: R= "+whiteCalibration[0]+" G= "+ whiteCalibration[1]+ " B= "+ whiteCalibration[2]);
        	
    }   
    
}
