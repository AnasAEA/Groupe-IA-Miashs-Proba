package perception;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
public class ColorSensor extends EV3ColorSensor {
    public ColorSensor(Port port) {
        super(port);
    }


  
    public String getCurrentColor() {
        int colorId = getColorID();
        switch (colorId) {
            case Color.RED: return "Rouge";
            case Color.GREEN: return "Vert";
            case Color.BLUE: return "Bleu";
            case Color.YELLOW: return "Jaune";
            case Color.BLACK: return "Noir";
            case Color.WHITE: return "Blanc";
            case Color.GRAY: return "Gris";
            default: return "Inconnu";
        }
    }

   
    public String changeColor() {
        return getCurrentColor();
    }
}
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
	
	
	public CapteurCouleur() {}
	
	public void GetCurrentColor(Port port){
		colorSensor = new EV3ColorSensor(port); // Initialisation du capteur
        average = new MeanFilter(colorSensor.getRGBMode(), 1); // Filtrage de la couleur en mode RGB
        colorSensor.setFloodlight(Color.WHITE); // faire un eclairage en blanc

        // Calibration de la couleur du chemin
        System.out.println("Appuyez sur ENTER pour calibrer la couleur du chemin...");
        Button.ENTER.waitForPressAndRelease(); // Attente de la calibration par l'utilisateur
        path_color = new float[average.sampleSize()]; // Initialisation du tableau pour stocker la couleur calibrée
        average.fetchSample(path_color, 0);
		
	}
	
	private double calculateScalarProduct(float[] color1, float[] color2) {
	    double sum = 0;
	    
	    // Calcul du produit scalaire entre les deux couleurs
	    for (int i = 0; i < color1.length; i++) {
	        sum += color1[i] * color2[i]; // 
	    }
	    
	    // Affichage du résultat
	    if (sum >= 1 - ERROR && sum <= 1 + ERROR) {
	        System.out.println("Les couleurs sont identiques ou très proches.");
	    } else {
	        System.out.println("Les couleurs sont différentes.");
	    }
	    
	    return sum; 
	}
	
	public boolean onPath()
    {
    	float[] sample = new float[average.sampleSize()];
		average.fetchSample(sample, 0);
		
		double scalaire = calculateScalarProduct(sample, path_color);
		System.out.println(scalaire < ERROR);
		//Button.ENTER.waitForPressAndRelease();
		
		return calculateScalarProduct(sample, path_color) < ERROR;
    	
    }

	

}
//last version
package Robot.Sensor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;

public class ColorSensor extends EV3ColorSensor {
    private static final int RGB_TOLERANCE = 20; // Tolérance pour la comparaison des valeurs RGB
    private static final String COLORS_FILE = "colors.txt"; // Nom du fichier contenant les couleurs calibrées
    private static final Map<Integer, int[]> COLORS = new HashMap<>(); // Map pour stocker les couleurs calibrées
    private static final int[] COLOR_NAMES = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK, Color.GRAY, Color.WHITE}; // Tableau des couleurs de base

    // Chargement des valeurs RGB depuis le fichier de calibration au démarrage du programme
    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(COLORS_FILE))) {
            for (int color : COLOR_NAMES) {
                String[] rgb = reader.readLine().split(";");
                COLORS.put(color, new int[] {
                    Integer.parseInt(rgb[0]), // Valeur rouge calibrée
                    Integer.parseInt(rgb[1]), // Valeur verte calibrée
                    Integer.parseInt(rgb[2])  // Valeur bleue calibrée
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement des couleurs : " + COLORS_FILE, e); // Exception si le fichier est introuvable
        }
    }

    // Constructeur : initialise le capteur de couleur avec le port spécifié
    public ColorSensor(Port port) {
        super(port);
    }

    // Méthode pour obtenir les valeurs RGB actuelles
    private int[] getRGB() {
        SampleProvider rgbMode = getRGBMode(); // Mode RGB du capteur
        float[] sample = new float[rgbMode.sampleSize()];
        rgbMode.fetchSample(sample, 0); // Lit les valeurs RGB
        return new int[] {(int)(sample[0] * 255), (int)(sample[1] * 255), (int)(sample[2] * 255)}; // Convertit les valeurs en entiers de 0 à 255
    }

    // Méthode pour détecter la couleur en fonction des valeurs RGB
    public int getDetectedColor() {
        int[] currentRGB = getRGB(); // Récupère les valeurs RGB actuelles

        // Parcours de chaque couleur calibrée pour voir si elle correspond aux valeurs détectées
        for (Map.Entry<Integer, int[]> entry : COLORS.entrySet()) {
            int[] colorRGB = entry.getValue();
            if (Math.abs(currentRGB[0] - colorRGB[0]) <= RGB_TOLERANCE && // Vérifie la tolérance pour le rouge
                Math.abs(currentRGB[1] - colorRGB[1]) <= RGB_TOLERANCE && // Vérifie la tolérance pour le vert
                Math.abs(currentRGB[2] - colorRGB[2]) <= RGB_TOLERANCE) { // Vérifie la tolérance pour le bleu
                return entry.getKey(); // Retourne la couleur détectée si elle correspond
            }
        }
        return -1; // Retourne -1 si aucune couleur ne correspond
    }

    // Méthode pour vérifier si une couleur spécifique est détectée
    public boolean isColorDetected(int color) {
        return getDetectedColor() == color; // Compare la couleur détectée avec la couleur passée en paramètre
    }

    // Affiche les valeurs RGB actuelles pour la couleur détectée
    public void printCurrentColor() {
        int[] rgb = getRGB(); // Obtient les valeurs RGB actuelles
        System.out.printf("Current RGB - R: %d G: %d B: %d%n", rgb[0], rgb[1], rgb[2]); // Affiche les valeurs RGB
    }
}

