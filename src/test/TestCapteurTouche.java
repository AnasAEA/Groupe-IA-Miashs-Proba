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


}
