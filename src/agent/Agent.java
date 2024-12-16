
package agent;

import java.util.ArrayList;

import action.Deplacement;
import action.Pince;

import lejos.hardware.port.SensorPort;
import lejos.robotics.navigation.MovePilot;
import perception.CapteurTouche;
import perception.CapteurUltrason;
import perception.CapteurCouleur;
import lejos.utility.Delay;

public class Agent {
    // Existing class attributes
    private CapteurUltrason capteurUltrason;
    private CapteurTouche capteurTouche;
    private Deplacement deplacement;
    private ArrayList<float[]> liste = new ArrayList<>();
    private float directionCampAdverse; // Déclaration en tant qu'attribut de classe
    private Pince pince ;
    private CapteurCouleur couleur;
    private int compteur;	
	

    // Constructor
    public Agent() {
        capteurUltrason = new CapteurUltrason(SensorPort.S1);
        capteurTouche = new CapteurTouche(SensorPort.S2);
        deplacement = new Deplacement();
        pince = new Pince();
        couleur = new CapteurCouleur(SensorPort.S4);
        deplacement.modifVitLin(30);
    }



    /**
     * Détecte tout objet a une distance inférieure à 80 cm.
     * 
     * @return true si un objet est détecté, false sinon.
     */

    // Method to detect objects by rotating 360 degrees
    public ArrayList<float[]> detecterLesObjets() {
        deplacement.tournerSync(360); // Démarrer une rotation de 360 degrés
        MovePilot pilot = deplacement.getPilot();
        this.liste.clear();

        while (pilot.isMoving()) {
            float distance = capteurUltrason.getDistance();
            float directionActuelle = deplacement.getDirection(); // Obtenir l'orientation actuelle

            // Collecter les données si la distance est inférieure à un certain seuil
            if (distance < 80.0f && distance >= 34.0f) {
                float[] objet = { distance, directionActuelle };
                this.liste.add(objet);
                System.out.println("Objet détecté à une distance de : " + distance + " cm, direction : "
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


	 
    public boolean attraperPalet(boolean premier) {
    	pince.ouvrirPince();
	deplacement.modifVitLin(30);
	float last_distance  = capteurUltrason.getDistance();
    	if(!premier)
    		deplacement.avancerSync(40);
    	else
    		deplacement.getPilot().forward();

    	while(deplacement.getPilot().isMoving()) {
    	     if(capteurTouche.isPressed()){
    	    	 deplacement.stop();Delay.msDelay(100);
    	    	 pince.fermerPince();
    	    	 return true;
    	     }
    	     if(capteurUltrason.getDistance() == last_distance+40) {
    	    	 deplacement.stop();Delay.msDelay(100);
    	    	 return false ;
    	     }
    	}
    	return false;
    }

    public void retourStrategie() {
    	deplacement.avancer(-40);
    	pince.fermerPince();
    	autrePalets();
    }
    
    public void lacherPalet() {
    	deplacement.avancer(10);
    	pince.ouvrirPince();Delay.msDelay(100);
    	deplacement.avancer(-10);
    	deplacement.tourner(180);Delay.msDelay(500);
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
    	this.attraperPalet(true);
    	this.devierDeLaLigne();
    	this.marquerPalet(); 
    	System.out.print( "le palet est déposé");    
    }
	
     public boolean secondEttroisiemePalet(String directionTourn) {
    	deplacement.avancer(55);
    	if(directionTourn.equalsIgnoreCase("Gauche")) 
    		deplacement.tourner(90);
    	else
    		deplacement.tourner(-90);
    	return attraperPalet(true);
     }
    
    /**
     * Surveille la présence d'obstacles et effectue une esquive si un obstacle est
     * détecté.
     *
     * @return true si un obstacle était détecté et esquivé, false sinon.
     */
    public boolean Surveiller() {
        if (capteurUltrason.detectObjet(10)) {
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

        deplacement.tourner(angleToTurn, true);
        
        float currentDirection2 = deplacement.getDirection();
        float angleToTurn2 = directionCampAdverse - currentDirection2;

        deplacement.tourner(angleToTurn2, true);
        
        // Déplacer le robot vers la ligne blanche (camp adverse)
        versCouleur("White");
        System.out.println("Couleur Blanche detecte");
	
        // Lâcher le palet
        lacherPalet();
        compteur = compteur +1;    
    }

    public void versCouleur(String couleurCible) {
        System.out.println("Recherche de la couleur : " + couleurCible);
        deplacement.modifVitLin(30);
        //float directionIni = deplacement.getDirection();
        deplacement.getPilot().forward();

        while (deplacement.getPilot().isMoving()) { 
            // V�rifier si la couleur cible est d�tect�e
            boolean couleurDetectee = false;

            if (couleurCible.equalsIgnoreCase("White")) {
                couleurDetectee = couleur.isWhite();
            } else if (couleurCible.equalsIgnoreCase("Black")) {
                couleurDetectee = couleur.isBlack();
            } else if (couleurCible.equalsIgnoreCase("Blue")) {
                couleurDetectee = couleur.isBlue();
            } else {
                System.out.println("Couleur cible non reconnue : " + couleurCible);
                deplacement.stop();
                return;
            }
            if (couleurDetectee) {
                deplacement.stop(); // Arr�ter le mouvement si la couleur cible est d�tect�e
                System.out.println("Couleur cible d�tect�e : " + couleurCible);
                break; // Quitter la boucle
            }
            
            // V�rifier s'il y a un obstacle
            Surveiller();

            // Attente pour �viter un traitement trop rapide
            Delay.msDelay(100);
        }
    }
	
    public void esquive() {
	float distance = capteurUltrason.getDistance();
	deplacement.tourner(90); Delay.msDelay(500); 
	deplacement.avancer(25);Delay.msDelay(500);
	deplacement.tourner(-90);
	deplacement.avancer(distance + 10);Delay.msDelay(500);                 
	deplacement.tourner(-90);Delay.msDelay(500);
	deplacement.avancer(25); Delay.msDelay(500);
	deplacement.tourner(90);
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
            deplacement.tourner(angleToTurn, true);

            // Verifier si c'est un palet
            return capteurUltrason.detecterPalet();
            
        }
        return false;
    }
    
    public void ligneCentrale() { 
	deplacement.avancerSync(120);
	while(deplacement.isMoving()) {
		Surveiller();
	}
    }

    public void autrePalets() {
    	ligneCentrale();
    	while (!ChercherPalet()){
    		deplacement.tourner(180);
    		deplacement.avancer(30);
    	}
       	if(attraperPalet(false)) {
       		marquerPalet();
       		return;
       	}
       	retourStrategie();
    }
		
    public void run() {
        // Initialiser la direction du camp adverse
    	directionCampAdverse = (deplacement.getDirection()+360)%360;
    	//Application de la strategie 
      premierPalet();
      if (secondEttroisiemePalet("D")) {
        marquerPalet();
       	if (secondEttroisiemePalet("D")) {
           	marquerPalet();        	
        }
       	else {
        	// Calcul de l'angle de rotation
            float currentDirection = deplacement.getDirection();
            float angleToTurn = directionCampAdverse - currentDirection;

            // Rotate to face the opposite direction of camp adverse
            deplacement.tourner(-angleToTurn,true);
        }
      }else {
        	// Calcul de l'angle de rotation
            float currentDirection = deplacement.getDirection();
            float angleToTurn = directionCampAdverse - currentDirection;

            // Rotate to face the opposite direction of camp adverse
            deplacement.tourner(-angleToTurn,true);
      }
      autrePalets();
      while(compteur <= 6)
	    autrePalets();
        
    }
	
    
    public static void main(String[] args) {
        Agent agent = new Agent();
        agent.run();
    }

}
