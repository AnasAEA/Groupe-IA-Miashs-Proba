package action;

import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;


public class Pince {
    
    private NXTRegulatedMotor pince;
    private int vitesseMoteur = 1000;
    private boolean etat; // true si les pinces sont fermées, false si ouvertes

    public Pince() {
        pince = Motor.D;
        pince.setSpeed(vitesseMoteur);
        this.etat = true; // Initialisé comme fermé
    }

    // Active les pinces pour saisir un palet
    public void fermerPince() {
        if(etat) {
	        pince.rotate(-1800);
	        while (pince.isMoving()) {
	            Delay.msDelay(100); // Wait for 100 milliseconds
	        }
	        etat=false;
	        }
	        pince.stop();
    }

    // Relâche le palet
    public void ouvrirPince() {
        if(!etat) {
        pince.rotateTo(1800);
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
