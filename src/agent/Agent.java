package agent;

import java.util.ArrayList;

import action.Deplacement;
import action.Pince;

import lejos.hardware.port.SensorPort;
import lejos.robotics.navigation.MovePilot;
import perception.CapteurTouche;
import perception.CapteurUltrason;
import perception.capteurCouleur;
import lejos.utility.Delay;

public class Agent {
    // Existing class attributes
    private CapteurUltrason capteurUltrason;
    private CapteurTouche capteurTouche;
    private Deplacement deplacement;
    private ArrayList<float[]> liste = new ArrayList<>();
    private float directionCampAdverse; // Déclaration en tant qu'attribut de classe
    private Pince pince ;
    private capteurCouleur couleur;
    private int compteur;	
	

    // Constructor
    public Agent() {
        capteurUltrason = new CapteurUltrason(SensorPort.S1);
        capteurTouche = new CapteurTouche(SensorPort.S2);
        deplacement = new Deplacement();
        pince = new Pince();
        couleur = new capteurCouleur(SensorPort.S4);
        deplacement.modifVitLin(30);
    }


    public void corrigeDirection(float directionPrecedente) {
	float currentDirection = this.getDirection();
        if (currentDirection != directionPrecedente ) {
            System.out.println("Correction de direction : " + currentDirection);
            pilot.rotate(-currentDirection); // Ajuste l'orientation
        }
    }

    /**
     * Détecte tout objet a une distance inférieure é 50 cm.
     * 
     * @return true si un objet est détecté, false sinon.
     */

    // Method to detect objects by rotating 360 degrees
    public ArrayList<float[]> detecterLesObjets() {
        deplacement.tournerAsync(360); // Démarrer une rotation de 360 degrés
        MovePilot pilot = deplacement.getPilot();
        this.liste.clear();

        while (pilot.isMoving()) {
            float distance = capteurUltrason.getDistance();
            float directionActuelle = deplacement.getDirection(); // Obtenir l'orientation actuelle

            // Collecter les données si la distance est inférieure à un certain seuil
            if (distance < 60.0f && distance >= 32.0f) {
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


	 
    public void attraperPalet() {
    	pince.ouvrirPince();
    	deplacement.getPilot().forward();
	while(deplacement.getPilot().isMoving()) {
    	     if(capteurTouche.isPressed()){
		deplacement.stop();
		pince.fermerPince();
		return;
    	     }
    	}
    }
    
    public void lacherPalet() {
    	deplacement.avancer(10);
    	pince.ouvrirPince();
    	deplacement.avancer(-10);
    	deplacement.tournerAsync(180);
    	pince.fermerPince();
    }
	
     public void devierDeLaLigne() {
    	    deplacement.tourner(90);
  	    Delay.msDelay(500);
  	    deplacement.avancer(25);
  	    Delay.msDelay(500);
  	    deplacement.tourner(-90);
    }
    
     public void premierPalet() {
    	this.attraperPalet();
    	this.devierDeLaLigne();
    	this.marquerPalet(); 
    	System.out.print( "le palet est déposé");    
    }
	
     public void secondEttroisiemePalet(String directionTourn) {
    	deplacement.avancer(50);
    	if(directionTourn.equalsIgnoreCase("Gauche"))
    		deplacement.tourner(90);
    	else
    		deplacement.tourner(-90);
    	
     	int i=0;
     	while (i <3 && !(capteurUltrason.detecterPalet()) ){
     		deplacement.avancerSync(30);
     		while(deplacement.getPilot().isMoving()) {
     			Surveiller(); 
         		Delay.msDelay(100);
     		}
     		Delay.msDelay(100);
     		i++;
     	}
     	if(i!=3) {	
     		this.attraperPalet();
     		this.marquerPalet(); 
     		System.out.print( "le palet est déposé");
     	}
     		//TODO : Cas ou aucun Palet sur la ligne Bleu
     		deplacement.stop();
        }
    
    /**
     * Surveille la présence d'obstacles et effectue une esquive si un obstacle est
     * détecté.
     *
     * @return true si un obstacle était détecté et esquivé, false sinon.
     */
    public boolean Surveiller() {
        if (capteurUltrason.detectObjet(25)) {
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

	deplacement.tournerAsync(angleToTurn);
        // Déplacer le robot vers la ligne blanche (camp adverse)
        versCouleur("White");
	
        // Lâcher le palet
        lacherPalet();
	compteur = compteur +1;     
    }

	public void versCouleur(String couleurCible) {
	    System.out.println("Recherche de la couleur : " + couleurCible);
	    deplacement.modifVitLin(30);

	    deplacement.getPilot().forward();
	    
	    while (deplacement.getPilot().isMoving()) { 
	    	
	       
	        // Obtenir la couleur courante détectée
	        String couleurCourante = couleur.getColorName(); // Assurez-vous que capteurCouleur a la méthode getColorName()

	        // Comparer avec la couleur cible
	        if (couleurCourante.equalsIgnoreCase(couleurCible)) {
	            deplacement.stop(); // Arrêter le mouvement si la couleur cible est détectée
	            System.out.println("Couleur cible détectée : " + couleurCourante);
	            break; // Quitter la boucle
	        }

	        // Vérifier s'il y a un obstacle
	    	Surveiller() ;
		    
	        // Attente pour éviter un traitement trop rapide
	        Delay.msDelay(100);
	    }
}
	
	public void esquive() {
	    float distance = capteurUltrason.getDistance();
	    deplacement.tournerAsync(90); Delay.msDelay(500); 
	    deplacement.avancer(25);Delay.msDelay(500);
	    deplacement.tournerAsync(-90);
	    deplacement.avancer(distance + 10);Delay.msDelay(500);                 
	    deplacement.tournerAsync(-90);Delay.msDelay(500);
	    deplacement.avancer(25); Delay.msDelay(500);
	    deplacement.tournerAsync(90);
	    System.out.println("Esquive terminée.");    
	}

	
    public boolean ChercherPalet() {
        ArrayList<float[]> objets = detecterLesObjets();
        float[] bestObj = bestObjet(objets);
        if (bestObj != null) {
            float distanceToObject = bestObj[0];
            float directionToObject = bestObj[1];

            System.out.println("Best object found at distance: " + distanceToObject + " cm, direction: "
                    + directionToObject + " degrees.");
            // Calcul de l'angle de rotation
            float currentDirection = deplacement.getDirection();
            float angleToTurn = directionToObject - currentDirection;

            // Rotate to face the object
            deplacement.tourner(angleToTurn);

            // Verifier si c'est un palet
            boolean isPalet = capteurUltrason.detecterPalet();
            return isPalet;
        }
        return false;
    }
    
    public void ligneCentrale() { 
	deplacement.avancer(100);
    }
		
    public void run() {
        // Initialiser la direction du camp adverse
        directionCampAdverse = deplacement.getDirection();
    }
	
    public static void main(String[] args) {
        Agent agent = new Agent();
        agent.run();
    }

}
