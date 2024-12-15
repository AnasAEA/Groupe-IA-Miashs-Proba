package action;

import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.utility.Delay;

/**
 * Classe Pince permettant de contrôler l'ouverture et la fermeture des pinces
 * d'un robot, pour saisir ou relâcher un objet (ex. un palet).
 */
public class Pince {
    
    private NXTRegulatedMotor pince; // Moteur contrôlant les pinces
    private int vitesseMoteur = 1000; // Vitesse du moteur
    private boolean etat; // État des pinces : true si ouvertes, false sinon

    /**
     * Constructeur de la classe Pince.
     * Initialise le moteur associé aux pinces et configure leur état initial (fermé).
     */
    public Pince() {
        pince = Motor.D;
        pince.setSpeed(vitesseMoteur); // Définit la vitesse initiale du moteur
        this.etat = false; // Les pinces sont fermées par défaut
    }

    /**
     * Ferme les pinces pour saisir un objet (par exemple, un palet).
     * Vérifie d'abord que les pinces sont ouvertes avant de les fermer.
     */
    public void fermerPince() {
        if (etat) { // Les pinces ne se ferment que si elles sont ouvertes
            pince.rotate(-850); // Rotation pour fermer les pinces
            while (pince.isMoving()) {
                Delay.msDelay(100); // Pause pour attendre la fin du mouvement
            }
            etat = false; // Met à jour l'état des pinces comme "fermées"
        }
        pince.stop(); // Arrête le moteur
    }

    /**
     * Ouvre les pinces pour relâcher un objet (par exemple, un palet).
     * Vérifie d'abord que les pinces sont fermées avant de les ouvrir.
     */
    public void ouvrirPince() {
        if (!etat) { // Les pinces ne s'ouvrent que si elles sont fermées
            pince.rotateTo(850); // Rotation pour ouvrir les pinces
            while (pince.isMoving()) {
                Delay.msDelay(100); // Pause pour attendre la fin du mouvement
            }
            etat = true; // Met à jour l'état des pinces comme "ouvertes"
        }
        pince.stop(); // Arrête le moteur
    }

    /**
     * Définit directement l'état des pinces (ouvertes ou fermées).
     * Cette méthode est utile pour synchroniser l'état logique avec l'état physique.
     * 
     * @param pincesOuvertes true si les pinces doivent être définies comme ouvertes, false sinon.
     */
    public void setPincesOuvertes(boolean pincesOuvertes) {
        this.etat = pincesOuvertes;
    }
}
