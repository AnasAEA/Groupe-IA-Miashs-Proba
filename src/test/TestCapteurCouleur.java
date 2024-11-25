package tests;

import java.awt.Button;

import lejos.hardware.port.SensorPort;
import perception.capteurCouleur;

public class TestCapteurCouleur {

    public static void main(String[] args) {
        System.out.println("Test du CapteurCouleur");
        System.out.println("Placez le capteur sur une surface colorée.");
        System.out.println("Appuyez sur le bouton ESC pour quitter.");

        // Instanciation du capteur de couleur sur le port S2
        capteurCouleur capteur = new capteurCouleur(SensorPort.S2);

        try {
            while (true) {
                // Mise à jour et affichage de la couleur détectée
                capteur.updateColorIfChanged();

                // Si le bouton ESC est pressé, on sort du programme
                if (Button.ESCAPE.isDown()) {
                    System.out.println("Fin du test.");
                    break;
                }

                // Ajout d'un délai pour éviter une surcharge de la console
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.err.println("Erreur d'interruption : " + e.getMessage());
        } finally {
            // Fermeture du capteur pour libérer les ressources
            capteur.close();
        }
    }
}

