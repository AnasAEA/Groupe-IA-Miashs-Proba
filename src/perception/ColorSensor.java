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

