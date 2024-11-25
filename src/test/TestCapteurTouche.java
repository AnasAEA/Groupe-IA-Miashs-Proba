package tests;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.Button;
import perception.CapteurTouche;

public class TestCapteurTouche {

    public static void main(String[] args) {
        System.out.println("Test du CapteurTouche");
        System.out.println("Appuyez sur le capteur pour tester ou sur ESC pour quitter.");

        // Création d'une instance de CapteurTouche en utilisant le port S1
        CapteurTouche capteur = new CapteurTouche(SensorPort.S1);

        try {
            while (true) {
                // Vérifie si le capteur est pressé
                if (capteur.isPressed()) {
                    System.out.println("Capteur pressé !");
                } else {
                    System.out.println("Capteur relâché.");
                }

                // Sortie si le bouton ESC est pressé
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
            // Libération des ressources
            capteur.close();
        }
    }
}
