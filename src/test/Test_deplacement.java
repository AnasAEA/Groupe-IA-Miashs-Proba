package tests;

import action.Deplacement;
import lejos.utility.Delay;

/**
 * Classe de test pour la classe Deplacement.
 * Elle hérite de Deplacement pour accéder directement aux méthodes de mouvement.
 */
public class Test_deplacement extends Deplacement {
    
    /**
     * Constructeur de Test_deplacement.
     * Initialise la classe de déplacement en appelant le constructeur de la classe parente.
     */
    public Test_deplacement() {
        super();
    }
    
    /**
     * Teste les méthodes getDirection() et tourner(double angleToTurn).
     * Vérifie que la direction est correctement mise à jour après des rotations.
     * 
     * @return boolean - true si les rotations affectent correctement la direction, false sinon.
     */
    public boolean test_getDirection_tourner() {
        // Obtention de la direction initiale
        float directionInitiale = super.getDirection();
        System.out.println("TestGetDirection_Tourner: Direction initiale = " + directionInitiale + "°");
        
        // Rotation de 90° vers la droite
        super.tourner(90); 
        float directionApresRot90D = super.getDirection(); // Doit être égale à directionInitiale + 90
        System.out.println("Après rotation de 90° vers la droite: " + directionApresRot90D + "°");
        
        // Rotation de 90° vers la gauche
        super.tourner(-90);
        float directionApresRot90G = super.getDirection(); // Doit être égale à directionInitiale
        System.out.println("Après rotation de 90° vers la gauche: " + directionApresRot90G + "°");
        
        // Rotation de 360°
        super.tourner(360);
        float directionApresRot360 = super.getDirection(); // Doit être égale à directionInitiale
        System.out.println("Après rotation de 360°: " + directionApresRot360 + "°");
        
        // Vérification des résultats
        boolean testPassed = (directionApresRot90D == directionInitiale + 90) &&
                             (directionApresRot90G == directionInitiale) &&
                             (directionApresRot360 == directionInitiale);
                             
        System.out.println("TestGetDirection_Tourner: " + (testPassed ? "Réussi." : "Échoué."));
        return testPassed;
    }
    
    /**
     * Teste les méthodes avancerSync(double distance), avancer(double distance), et isMoving().
     * Vérifie que le robot avance correctement et que l'état de mouvement est correct.
     * 
     * @return boolean - true si le robot avance et s'arrête correctement, false sinon.
     */
    public boolean test_avancerSync_avancer_isMoving() {
        System.out.println("TestAvancerSync_Avancer_IsMoving: Avancer de 50 cm.");
        super.avancer(50);
        
        System.out.println("Avancer de 100 cm avec avancerSync.");
        super.avancerSync(100);
        
        // Boucle qui s'exécute pendant le déplacement
        int i = 0;
        while (super.isMoving()) {
            System.out.println("Mouvement en cours... Compteur: " + i);
            try {
                Thread.sleep(500); // Pause de 500 ms entre chaque itération
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
            if (i > 10) { // Timeout pour éviter une boucle infinie
                System.out.println("Timeout atteint, arrêt du déplacement.");
                super.deplacement.stop();
                break;
            }
        }
        
        // Vérification si le robot est toujours en mouvement
        boolean testPassed = !super.isMoving();
        System.out.println("TestAvancerSync_Avancer_IsMoving: " + (testPassed ? "Réussi." : "Échoué."));
        return testPassed;
    }
    
    /**
     * Teste les méthodes tournerAsync(double angleToTurn) et tourner(double angleToTurn, boolean heading).
     * Vérifie que le robot tourne correctement et que la direction est rétablie.
     * 
     * @return boolean - true si les rotations sont correctement effectuées, false sinon.
     */
    public boolean test_tournerAsync_tourner() {
        float directionInitiale = super.getDirection();
        System.out.println("TestTournerAsync_Tourner: Direction initiale = " + directionInitiale + "°");
        
        // Tourner de 160° en mode asynchrone
        super.tournerAsync(160);		
        int i = 0;
        while (super.isMoving()) {
            System.out.println("Rotation en cours... Compteur: " + i);
            try {
                Thread.sleep(500); // Pause de 500 ms entre chaque itération
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
            if (i > 10) { // Timeout pour éviter une boucle infinie
                System.out.println("Timeout atteint, arrêt de la rotation.");
                super.deplacement.stop();
                break;
            }
        }
        
        // Rétablir la direction initiale
        super.tourner(directionInitiale, true);
        float directionFinale = super.getDirection();
        System.out.println("Direction après rétablissement: " + directionFinale + "°");
        
        // Vérification
        boolean testPassed = (directionFinale == directionInitiale);
        System.out.println("TestTournerAsync_Tourner: " + (testPassed ? "Réussi." : "Échoué."));
        return testPassed;
    }
  
    /**
     * Méthode principale pour exécuter les tests de déplacement.
     */
    public static void main(String[] args) {
        Test_deplacement td = new Test_deplacement();
        
        // Test des méthodes getDirection() et tourner()
        if (td.test_getDirection_tourner()) {
            System.out.println("TestGetDirection_Tourner réussi.");
        } else {
            System.out.println("TestGetDirection_Tourner échoué.");
        }
        
        Delay.msDelay(1000);

        // Test des méthodes avancerSync(), avancer() et isMoving()
        if (td.test_avancerSync_avancer_isMoving()) {
            System.out.println("TestAvancerSync_Avancer_IsMoving réussi.");
        } else {
            System.out.println("TestAvancerSync_Avancer_IsMoving échoué.");
        }
        
        Delay.msDelay(1000);

        // Test des méthodes tournerAsync() et tourner()
        if (td.test_tournerAsync_tourner()) {
            System.out.println("TestTournerAsync_Tourner réussi.");
        } else {
            System.out.println("TestTournerAsync_Tourner échoué.");
        }
 }
}
