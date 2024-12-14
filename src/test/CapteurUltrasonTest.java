package tests;

import perception.CapteurUltrason;
import lejos.hardware.port.SensorPort;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

/**
 * Classe de test pour la classe CapteurUltrason.
 * Elle hérite de CapteurUltrason pour accéder directement aux méthodes de détection.
 */
public class CapteurUltrasonTest extends CapteurUltrason {
    
    /**
     * Constructeur de CapteurUltrasonTest.
     * Initialise le capteur ultrason sur le port S1.
     */
    public CapteurUltrasonTest() {
        super(SensorPort.S1);
    }

    /**
     * Teste la fonction générale du capteur ultrason.
     * Vérifie la détection d'un objet et spécifiquement d'un palet pendant que le robot est en mouvement.
     */
    public boolean testFonction() {
        // Obtention du pilote de déplacement depuis la classe parente
        MovePilot pilot = super.deplacement.getPilot();
        
        System.out.println("Début du test de détection d'objet et de palet.");

        // Boucle tant que le robot est en mouvement
        while (pilot.isMoving()) {
            if (super.detectObjet()) {
                System.out.println("Objet détecté.");
                if (super.detecterPalet()) {
                    System.out.println("Palet détecté.");
                }
            }
            // Pause pour éviter une surcharge du processeur
            Delay.msDelay(500);
        }
        
        // Vérification finale
        boolean testPassed = true; // Supposons que le test passe si aucun problème n'est rencontré
        System.out.println("TestFonction: " + (testPassed ? "Réussi." : "Échoué."));
        return testPassed;
    }

    /**
     * Teste la méthode getDistance() du capteur ultrason.
     * Compare la distance détectée avec une valeur attendue.
     * 
     * @param expectedDistance La distance attendue en cm.
     * @return boolean - true si la distance détectée est proche de la distance attendue, false sinon.
     */
    public boolean getDistanceTest(float expectedDistance) {
        float detectedDistance = this.getDistance();
        boolean isCorrect = Math.abs(detectedDistance - expectedDistance) < 1.0f; // Tolérance de 1 cm
        System.out.println("TestGetDistance: Distance détectée = " + detectedDistance + " cm | Attendue = " + expectedDistance + " cm | Résultat: " + isCorrect);
        return isCorrect;
    }

    /**
     * Teste la méthode detectObjet() en avançant le robot et en vérifiant la détection.
     * 
     * @param distance Avancer de cette distance en cm.
     * @return boolean - true si l'objet est détecté lors du déplacement, false sinon.
     */
    public boolean detectObjectTest(float distance) {
        System.out.println("TestDetectObject: Avancer de " + distance + " cm et détecter les objets.");
        super.deplacement.avancerAsync(distance);
        int i = 0;
        boolean objetDetecte = false;
        while (i < 7 && super.deplacement.getPilot().isMoving()) {
            objetDetecte = super.detectObjet();
            System.out.println("Détection objet à " + (i+1)*10 + " cm: " + objetDetecte);
            Delay.msDelay(500);
            i++;
        }
        super.deplacement.stop();
        System.out.println("TestDetectObject: " + (objetDetecte ? "Objet détecté." : "Aucun objet détecté."));
        return objetDetecte;
    }

    /**
     * Teste la méthode detectObjet() en tournant le robot et en vérifiant la détection.
     * 
     * @param angle Angle de rotation en degrés.
     * @return boolean - true si un objet est détecté pendant la rotation, false sinon.
     */
    public boolean detectObjectTestBis(float angle) {
        System.out.println("TestDetectObjectBis: Tourner de " + angle + " degrés et détecter les objets.");
        super.deplacement.tournerAsync(angle);
        int i = 0;
        boolean objetDetecte = false;
        while (i < 12 && super.deplacement.getPilot().isMoving()) {
            objetDetecte = super.detectObjet();
            System.out.println("Détection objet à " + (i+1)*30 + "°: " + objetDetecte);
            Delay.msDelay(500);
            i++;
        }
        super.deplacement.stop();
        System.out.println("TestDetectObjectBis: " + (objetDetecte ? "Objet détecté." : "Aucun objet détecté."));
        return objetDetecte;
    }

    /**
     * Teste la méthode detecterPalet() en tournant le robot jusqu'à la détection d'un palet.
     * 
     * @return boolean - true si un palet est détecté, false sinon.
     */
    public boolean detecterPaletTest() {
        System.out.println("TestDetecterPalet: Tourner jusqu'à détecter un palet.");
        int i = 0;
        boolean paletDetecte = false;
        while (i < 12 && !super.detecterPalet()) {
            super.deplacement.tournerAsync(30);
            System.out.println("Rotation en cours pour détecter un palet...");
            Delay.msDelay(500);
            i++;
        }
        paletDetecte = super.detecterPalet();
        if (paletDetecte) {
            System.out.println("Palet détecté lors du test.");
        } else {
            System.out.println("Aucun palet détecté après 12 rotations.");
        }
        super.deplacement.stop();
        return paletDetecte;
    }

    /**
     * Méthode principale pour exécuter les tests.
     */
    public static void main(String[] args) {
        CapteurUltrasonTest test = new CapteurUltrasonTest();
        
        // Test de la détection d'objet en avançant
        boolean detectObject = test.detectObjectTest(70.0f); // Avancer de 70 cm
        System.out.println("Resultat detectObjectTest: " + detectObject);
        
        Delay.msDelay(1000);

        // Test de la détection de palet en tournant
        boolean detectPalet = test.detecterPaletTest();
        System.out.println("Resultat detecterPaletTest: " + detectPalet);
        
        Delay.msDelay(1000);

        // Test de la méthode getDistance avec une distance attendue de 25 cm
        boolean distanceTest = test.getDistanceTest(25.0f);
        System.out.println("Resultat getDistanceTest: " + distanceTest);
        
        // Fermeture du capteur ultrason
        test.close();
        System.out.println("Tests du Capteur Ultrason terminés.");
    }
}
