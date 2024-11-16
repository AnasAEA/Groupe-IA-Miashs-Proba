package agent;

import java.util.ArrayList;

import action.Deplacement;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.navigation.MovePilot;
import perception.CapteurTouche;
import perception.CapteurUltrason;
import perception.ColorSensor;

public class Agent {
    // Existing class attributes
    private CapteurUltrason capteurUltrason;
    private CapteurTouche capteurTouche;
    private Deplacement deplacement;
    private ArrayList<float[]> liste = new ArrayList<>();
    private float directionCampAdverse; // Déclaration en tant qu'attribut de classe
    private NXTRegulatedMotor motor; 
    private Pince pince ;
    private ColorSensor couleur;

    // Constructor
    public Agent() {
        capteurUltrason = new CapteurUltrason(SensorPort.S1);
        capteurTouche = new CapteurTouche(SensorPort.S2);
        deplacement = new Deplacement();
	motor = new NXTRegulatedMotor (MotorPort.C);
        pince = new Pince( motor);
        couleur = new ColorSensor(SensorPort.S2);
        
    }

    /**
     * Détecte tout objet a une distance inférieure é 50 cm.
     * 
     * @return true si un objet est détecté, false sinon.
     */

    // Method to detect objects by rotating 360 degrees
    public ArrayList<float[]> detecterLesObjets() {
        deplacement.getPilot().setAngularSpeed(30); // Régler la vitesse de rotation
        deplacement.tournerAsync(360); // Démarrer une rotation de 360 degrés
        MovePilot pilot = deplacement.getPilot();
        this.liste.clear();

        while (pilot.isMoving()) {
            float distance = capteurUltrason.getDistance();
            float directionActuelle = deplacement.getDirection(); // Obtenir l'orientation actuelle

            // Collecter les données si la distance est inférieure à un certain seuil
            if (distance < 60.0f) {
                float[] objet = { distance, directionActuelle };
                this.liste.add(objet);
                System.out.println("Objet détecté é une distance de : " + distance + " cm, direction : "
                        + directionActuelle + " degrés.");
            }

            // Pause pour éviter de surcharger le processeur
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pilot.stop(); // S'assurer que le robot arréte de tourner
        return this.liste;
    }
public void attraperPalet() {
    if (!pince.isEtat()) { 
        System.out.println("La pince est ouverte. Fermeture pour attraper le palet.");
        pince.fermerPince(); 
        pince.setaPalet(true);
    } else {
        System.out.println("La pince est déjà fermée. Impossible d'attraper un autre palet.");
    }
}

public void lacherPalet() {
  
    pince.ouvrirPince(); 
    pince.setaPalet(false);  
    System.out.println("Palet relâché.");

    moteurGauche.rotate(-180);  
    moteurDroit.rotate(-180);   

    moteurGauche.rotate(180);  
    moteurDroit.rotate(-180);  



    // Method to find the closest object
    public float[] bestObjet(ArrayList<float[]> objets) {
        if (objets.isEmpty()) {
            return null;
        }
        float minDistance = Float.MAX_VALUE;
        float[] bestObjet = null;
        for (float[] objet : objets) {
            float distance = objet[0];
            if (distance < minDistance) {
                minDistance = distance;
                bestObjet = objet;
            }
        }
        return bestObjet;
    }

    /**
     * Surveille la présence d'obstacles et effectue une esquive si un obstacle est
     * détecté.
     *
     * @return true si un obstacle était détecté et esquivé, false sinon.
     */
    public boolean Surveiller() {
        if (detectObjet()) {
            System.out.println("Obstacle détecté lors de la surveillance. Initiation de l'évitement...");
            esquive();
            return true;
        }
        return false;
    }

    private void marquerPalet() {
        // Ajuster l'orientation du robot vers le camp adverse
        float currentDirection = deplacement.getDirection();
        float angleToTurn = directionCampAdverse - currentDirection;

        // Ajuster l'angle pour qu'il se situe entre -180 et 180 degrés
        if (angleToTurn > 180) {
            angleToTurn -= 360;
        } else if (angleToTurn < -180) {
            angleToTurn += 360;
        }

        // Déplacer le robot vers la ligne blanche (camp adverse)
        versCouleur(White);

        // Avancer d'environ 5 cm pour s'assurer que le palet se trouve bien dans le
        // camp adverse
        deplacement.avancer(5);
        // Lâcher le palet
        lacherPalet();
        deplacement.avancer(-10);
    }
	 public void versCouleur(String color) {
    	while (!(couleur.getColorName() == color)) {
    		deplacement.avancerAsync(directionCampAdverse);
    	}
    	deplacement.stop();	
    }

    public boolean ChercherPalet() {
        ArrayList<float[]> objets = detecterLesObjets();

        // Find the best object (closest)
        float[] bestObj = bestObjet(objets);

        if (bestObj != null) {
            float distanceToObject = bestObj[0];
            float directionToObject = bestObj[1];

            System.out.println("Best object found at distance: " + distanceToObject + " cm, direction: "
                    + directionToObject + " degrees.");
            deplacement.getPilot().setAngularSpeed(30); // Régler la vitesse de rotation
            // Calculate angle to turn
            float currentDirection = deplacement.getDirection();
            float angleToTurn = directionToObject - currentDirection;

            // Compensate for the 7-degree deviation
            if (angleToTurn > 0) {
                angleToTurn -= 7; // If turning clockwise, reduce angle
            } else if (angleToTurn < 0) {
                angleToTurn += 7; // If turning counter-clockwise, increase angle
            }

            // Adjust angle to be between -180 and 180 degrees
            if (angleToTurn > 180) {
                angleToTurn -= 360;
            } else if (angleToTurn < -180) {
                angleToTurn += 360;
            }

            // Rotate to face the object
            deplacement.getPilot().rotate(angleToTurn);

            // Check if it's a palet
            boolean isPalet = capteurUltrason.detecterPalet();
            return isPalet;
        }
        return false;
    }
    
    public void ligneCentrale() {
	   //apres avoir deposer le palet on tourne 180 deg
	   deplacement.tournerAsync(180); 
	   //on avance jusqu'à que la couleur detectée est Black (ligne centrale)
	   versCouleur("Black");
    }
	 
     public void premierPalet() {
	pince.ouvrirPince();    
    	this.attraperPalet();
	 this.marquerPalet(); 
	this.lacherPalet(); 
	System.out.print( "le palet est déposé");    
    }
     public void second&troisiemePalet() {
    	deplacement.tournerAsync(180);
    	this.versCouleur("Bleu");
     	deplacement.tournerAsync(-90);
     	while (!(capteurUltrason.detecterPalet())){
     		deplacement.avancerAsync(100);
     	}
        	this.attraperPalet();
	        this.marquerPalet(); 
	        this.lacherPalet();
	        System.out.print( "le palet est déposé");
        }
    
    public void run() {
        // Initialiser la direction du camp adverse
        directionCampAdverse = deplacement.getDirection();
        System.out
                .println("Direction du camp adverse initialement réglée à : " + directionCampAdverse + " degrés.");

        // Cleanup resources
        capteurUltrason.close();
        capteurTouche.close();
        deplacement.stop();
        System.out.println("Program terminated.");
    }
	public void versCouleur(String couleurCible) {
	    System.out.println("Recherche de la couleur : " + couleurCible);
	    deplacement.forward();
	    
	    while (deplacement.getPilot().isMoving()) { 
	        // Vérifier s'il y a un obstacle
	    	Surveiller() ;
	    	
	       
	        // Obtenir la couleur courante détectée
	        String couleurCourante = capteurCouleur.getColorName(); // Assurez-vous que capteurCouleur a la méthode getColorName()

	        // Comparer avec la couleur cible
	        if (couleurCourante.equalsIgnoreCase(couleurCible)) {
	            deplacement.stop(); // Arrêter le mouvement si la couleur cible est détectée
	            System.out.println("Couleur cible détectée : " + couleurCourante);
	            break; // Quitter la boucle
	        }

	        // Attente pour éviter un traitement trop rapide
	        Delay.msDelay(100);
	    }

	

}
public void esquive() {
	   
		 float distance = getDistance();
	    // Étape 1 : Tourner de 90 degrés
	    deplacement.tournerAsync(90); // Rotation horaire de 90 degrés
	    Delay.msDelay(500); // Pause pour stabiliser après la rotation

	    // Étape 2 : Avancer de 10 cm
	    deplacement.avancer(10);
	  
	    Delay.msDelay(500);

	    // Étape 3 : Tourner de -90 degrés
	    deplacement.tournerAsync(-90); // Rotation antihoraire de 90 degrés
	   

	    // Étape 4 : Avancer d'une distance calculée
	    float distance = getDistance(); // Appel de la méthode pour obtenir la distance
	    deplacement.avancer(distance + 10); // Avancer de (distance détectée + 10 cm)
	    
	    Delay.msDelay(500);

	    // Étape 5 : Tourner de -90 degrés
	    deplacement.tournerAsync(-90); // Rotation antihoraire de 90 degrés
	    Delay.msDelay(500);

	    // Étape 6 : Avancer de 10 cm
	    deplacement.avancer(10);
	    System.out.println("Avancement final de 10 cm pour compléter l'esquive.");
	    Delay.msDelay(500);
	deplacement.tournerAsync(90);
	
	    System.out.println("Esquive terminée.");
	    
	}
    public static void main(String[] args) {
        Agent agent = new Agent();
        agent.run();
    }

}
