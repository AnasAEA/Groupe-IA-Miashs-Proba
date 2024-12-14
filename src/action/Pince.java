package action;

import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;


public class Pince {
    
    private NXTRegulatedMotor pince;
    private int vitesseMoteur = 1000;
    private boolean etat; // true si les pinces sont Ouvertes, false sinon

    public Pince() {
        pince = Motor.D;
        pince.setSpeed(vitesseMoteur);
        this.etat = false; // Initialisé comme fermé
    }

    // Active les pinces pour saisir un palet
    public void fermerPince() {
        if(etat) {
		pince.rotate(-850);
	        while (pince.isMoving()) {
	            Delay.msDelay(100); 
	        }
	        etat=false;
	        }
	        pince.stop();
    }

    // Ouvre les pinces pour relâcher le palet
    public void ouvrirPince() {
        if(!etat) {
        pince.rotateTo(850);
        while (pince.isMoving()) {
            Delay.msDelay(100); // Wait for 100 milliseconds
        }
        etat=true;
        }
        pince.stop();
    }

    public void setPincesOuvertes(boolean pincesOuvertes) { 
		this.etat = pincesOuvertes;
	}

}
