package tests;

import lejos.hardware.port.SensorPort;
import lejos.hardware.Button;
import perception.CapteurTouche;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TestCapteurTouche {

    private CapteurTouche capteur;

    // Initialisation du capteur avant chaque test
    @BeforeEach
    public void setUp() {
        capteur = new CapteurTouche(SensorPort.S1);
    }

  

    // Test de la méthode isPressed() avec gestion d'exception
    @Test
    public void testIsPressed() {
        try {
            boolean isPressed = capteur.isPressed();
            System.out.println("Le capteur est pressé : " + isPressed);
            if (isPressed) {
                System.out.println("Le capteur est actuellement pressé.");
            } else {
                System.out.println("Le capteur est actuellement relâché.");
            }
        } catch (Exception e) {
            fail("Erreur lors de l'appel de isPressed : " + e.getMessage());
        }
    }

    // Test de la méthode close() avec gestion d'exception
    @Test
    public void testClose() {
        try {
            capteur.close();
            System.out.println("Capteur fermé avec succès.");
        } catch (Exception e) {
            fail("Erreur lors de la fermeture du capteur : " + e.getMessage());
        }
    }
    

    public static void main(String[] args) {
        System.out.println("Test des capteurs : CapteurTouche et capteurCouleur");
        System.out.println("Appuyez sur le capteur pour tester ou sur ESC pour quitter.");

        // Création d'une instance de CapteurTouche en utilisant le port S1
        CapteurTouche capteurTouche = new CapteurTouche(SensorPort.S1);

        // Création d'une instance de capteurCouleur en utilisant le port S2
        capteurCouleur capteurCouleur = new capteurCouleur(SensorPort.S2);

        try {
            while (true) {
                // Test du capteur de touche
                if (capteurTouche.isPressed()) {
                    System.out.println("Capteur Tactile pressé !");
                } else {
                    System.out.println("Capteur Tactile relâché.");
                }

                // Test du capteur de couleur
                capteurCouleur.updateColorIfChanged();

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
            capteurTouche.close();
            capteurCouleur.close();
        }
    }

}
