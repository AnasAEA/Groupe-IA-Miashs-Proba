package perception;
import lejos.hardware.Button;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;

public class capteurCouleur {

    private EV3ColorSensor colorSensor;
    private String lastDetectedColor;

        // Constructeur de ColorSensor
    public capteurCouleur(Port port) {
        colorSensor = new EV3ColorSensor(port);
        colorSensor.setFloodlight(Color.WHITE);
        System.out.println("Press enter to calibrate path color...");
        Button.ENTER.waitForPressAndRelease();
        lastDetectedColor = getColorName(); // Initialiser avec la couleur calibrï¿½e
    }


        // Méthode pour obtenir la couleur détectée en utilisant le mode ID de couleur
        public String getColorName() {
            SampleProvider colorIDProvider = colorSensor.getColorIDMode();
            float[] colorSample = new float[colorIDProvider.sampleSize()];
            colorIDProvider.fetchSample(colorSample, 0);
            int colorID = (int) colorSample[0];

            // Return the color name based on the color ID
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
                    return "Unknown Color";
            }
        }

        // Méthode pour mettre à jour la couleur si elle a changé
        public void updateColorIfChanged() {
            String currentColor = getColorName();
            if (!currentColor.equals(lastDetectedColor)) {
                System.out.println("Detected new color: " + currentColor);
                lastDetectedColor = currentColor; // Met à jour la couleur détectée
            }
        }
    }
