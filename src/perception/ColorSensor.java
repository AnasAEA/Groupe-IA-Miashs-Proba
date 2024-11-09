package perception;
import lejos.hardware.Button;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;

public class CapteurCouleur {

    private static float[] path_color;
    private static SampleProvider average;
    private static EV3ColorSensor colorSensor;
    private final static double ERROR = 0.01;
    private String lastDetectedColor;

    // Classe interne ColorSensor
    public static class ColorSensor {
        private float[] path_color;
        private SampleProvider average;
        private EV3ColorSensor colorSensor;
        private final double ERROR = 0.01;
        private String lastDetectedColor;

        // Constructeur de ColorSensor
        public ColorSensor(Port port) {
            colorSensor = new EV3ColorSensor(port);
            average = new MeanFilter(colorSensor.getRGBMode(), 1);
            colorSensor.setFloodlight(Color.WHITE);
            System.out.println("Press enter to calibrate path color...");
            Button.ENTER.waitForPressAndRelease();
            path_color = new float[average.sampleSize()];
            average.fetchSample(path_color, 0);
            lastDetectedColor = getColorName(); // Initialiser avec la couleur calibrée
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

        // Méthode pour calculer le produit scalaire entre deux couleurs
      /*  private double calculateScalarProduct(float[] color1, float[] color2) {
            double sum = 0;
            for (int i = 0; i < color1.length; i++) {
                sum += color1[i] * color2[i];
            }
            return sum;
        }

        // Méthode pour vérifier si le capteur est sur le chemin calibré
        public boolean onPath() {
            float[] sample = new float[average.sampleSize()];
            average.fetchSample(sample, 0);

            double scalaire = calculateScalarProduct(sample, path_color);
            return scalaire < ERROR;
        }
*/
        // Méthode pour mettre à jour la couleur si elle a changé
        public void updateColorIfChanged() {
            String currentColor = getColorName();
            if (!currentColor.equals(lastDetectedColor)) {
                System.out.println("Detected new color: " + currentColor);
                lastDetectedColor = currentColor; // Met à jour la couleur détectée
            }
        }
    }
}
public static void main(String[] args) {
            // Initialisation du capteur de couleur sur le port 1
            CapteurCouleur capteur = new CapteurCouleur(SensorPort.S1);

            System.out.println("Appuyez sur un bouton pour commencer le test...");
            Button.waitForAnyPress();

            // Boucle pour tester la détection de couleur
            while (!Button.ESCAPE.isDown()) {
                capteur.updateColorIfChanged(); // Vérifie si la couleur a changé et affiche si c'est le cas
                Delay.msDelay(500); // Délai pour éviter de trop solliciter le capteur
            }

            System.out.println("Test terminé.");
        }
