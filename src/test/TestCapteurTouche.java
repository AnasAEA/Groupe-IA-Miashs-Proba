package tests;

import lejos.hardware.port.SensorPort;
import lejos.hardware.Button;
import perception.CapteurTouche;
import perception.capteurCouleur;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Classe de test pour la classe CapteurTouche.
 * Utilise JUnit pour les tests unitaires.
 */
public class TestCapteurTouche {

    private CapteurTouche capteur;

    /**
     * Méthode exécutée avant chaque test.
     * Initialise le capteur tactile sur le port S2.
     */
    @BeforeEach
    public void setUp() {
        capteur = new CapteurTouche(SensorPort.S2);
    }

    /**
     * Méthode exécutée après chaque test.
     * Ferme le capteur tactile pour libérer les ressources.
     */
    @AfterEach
    public void tearDown() {
        capteur.close();
    }

    /**
     * Test de la méthode isPressed() pour vérifier la détection correcte des pressions du bouton tactile.
     * Utilise une approche automatisée avec JUnit.
     */
    @Test
    public void testIsPressed() {
        try {
            boolean isPressed = capteur.isPressed();
            System.out.println("TestIsPressed: Le capteur est pressé : " + isPressed);
            if (isPressed) {
                System.out.println("Le capteur est actuellement pressé.");
            } else {
                System.out.println("Le capteur est actuellement relâché.");
            }
        } catch (Exception e) {
            fail("Erreur lors de l'appel de isPressed : " + e.getMessage());
        }
    }

    /**
     * Méthode principale pour exécuter les tests manuels du capteur tactile et capteur de couleur.
     * Permet de tester les capteurs en temps réel via l'interface console.
     * 
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        System.out.println("Test des capteurs : CapteurTouche et capteurCouleur");
        System.out.println("Appuyez sur le capteur tactile pour tester ou sur ESC pour quitter.");

        // Création d'une instance de CapteurTouche en utilisant le port S2
        CapteurTouche capteurTouche = new CapteurTouche(SensorPort.S2);
        try {
            while (true) {
                // Test du capteur de touche
                if (capteurTouche.isPressed()) {
                    System.out.println("Capteur Tactile pressé !");
                } else {
                    System.out.println("Capteur Tactile relâché.");
                }

                // Ajout d'un délai pour éviter une surcharge de la console
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.err.println("Erreur d'interruption : " + e.getMessage());
        } finally {
            // Libération des ressources
            capteurTouche.close();
            System.out.println("Capteurs fermés.");
        }
    }
}
