package tests;

import lejos.hardware.port.SensorPort;
import perception.capteurCouleur;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class TestCapteurCouleur {

    private capteurCouleur capteur;

    // Initialisation du capteur avant chaque test
    @BeforeEach
    public void setUp() {
        capteur = new capteurCouleur(SensorPort.S2);
    }

   

    // Test de la méthode getColorName() avec gestion d'exception
    @Test
    public void testGetColorName() {
        try {
            String detectedColor = capteur.getColorName();
            if (detectedColor == null) {
                throw new Exception("La couleur détectée ne peut pas être nulle.");
            }
            System.out.println("Couleur détectée : " + detectedColor);
        } catch (Exception e) {
            // En cas d'exception, échec du test
            fail("Exception dans testGetColorName : " + e.getMessage());
        }
    }

    // Test de la méthode updateColorIfChanged()
    @Test
    public void testUpdateColorIfChanged() {
        try {
            capteur.updateColorIfChanged();
            System.out.println("Méthode updateColorIfChanged appelée avec succès.");
        } catch (Exception e) {
            fail("Erreur lors de l'appel de updateColorIfChanged : " + e.getMessage());
        }
    }

    
    
}
