package tests;

import lejos.hardware.port.SensorPort;
import perception.capteurCouleur;

public class TestCapteurCouleur {

    private capteurCouleur capteur;

    // Constructeur pour initialiser le capteur de couleur
    public TestCapteurCouleur() {
        try {
            capteur = new capteurCouleur(SensorPort.S2); // Connecté sur le port S2
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du capteur de couleur : " + e.getMessage());
        }
    }

    // Test pour vérifier si la couleur bleue est détectée
    public void testIsBlue() {
        try {
            if (capteur.isBlue()) {
                System.out.println("La couleur bleue a été détectée.");
            } else {
                System.out.println("La couleur bleue n'a pas été détectée.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du test de la couleur bleue : " + e.getMessage());
        }
    }

    // Test pour vérifier si la couleur noire est détectée
    public void testIsBlack() {
        try {
            if (capteur.isBlack()) {
                System.out.println("La couleur noire a été détectée.");
            } else {
                System.out.println("La couleur noire n'a pas été détectée.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du test de la couleur noire : " + e.getMessage());
        }
    }

    // Test pour vérifier si la couleur blanche est détectée
    public void testIsWhite() {
        try {
            if (capteur.isWhite()) {
                System.out.println("La couleur blanche a été détectée.");
            } else {
                System.out.println("La couleur blanche n'a pas été détectée.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du test de la couleur blanche : " + e.getMessage());
        }
    }

    // Méthode pour fermer le capteur et libérer les ressources
    public void close() {
        try {
            capteur.close();
            System.out.println("Ressources libérées correctement.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la libération des ressources : " + e.getMessage());
        }
    }
}
