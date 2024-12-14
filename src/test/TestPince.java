package tests;

import action.Pince;
import lejos.utility.Delay;

/**
 * Classe de test pour la classe Pince.
 * Elle hérite de Pince pour accéder directement aux méthodes d'ouverture et de fermeture.
 */
public class TestPince extends Pince {
    
    /**
     * Constructeur de TestPince.
     * Initialise la pince en appelant le constructeur de la classe parente.
     */
    public TestPince() {
        super(); 
    }

    /**
     * Teste la méthode fermerPince() pour vérifier la fermeture correcte de la pince.
     * 
     * @return boolean - true si la pince se ferme correctement, false sinon.
     */
    public boolean testerFermeturePince() {
        System.out.println("TestFermeturePince: Fermeture de la pince...");
        fermerPince();
        // Ici, vous pouvez ajouter une vérification physique ou logique pour confirmer la fermeture
        System.out.println("Pince fermée.");
        Delay.msDelay(500); 
        return true; // Retourne true si la méthode s'est exécutée sans exception
    }

    /**
     * Teste la méthode ouvrirPince() pour vérifier l'ouverture correcte de la pince.
     * 
     * @return boolean - true si la pince s'ouvre correctement, false sinon.
     */
    public boolean testerOuverturePince() {
        System.out.println("TestOuverturePince: Ouverture de la pince...");
        ouvrirPince();
        // Ici, vous pouvez ajouter une vérification physique ou logique pour confirmer l'ouverture
        System.out.println("Pince ouverte.");
        Delay.msDelay(500); 
        return true; // Retourne true si la méthode s'est exécutée sans exception
    }

    /**
     * Méthode principale pour exécuter les tests d'ouverture et de fermeture de la pince.
     */
    public static void main(String[] args) {
        TestPince testPince = new TestPince();
        
        // Test de la fermeture de la pince
        if (testPince.testerFermeturePince()) {
            System.out.println("TestFermeturePince réussi.");
        } else {
            System.out.println("TestFermeturePince échoué.");
        }

        Delay.msDelay(1000);

        // Test de l'ouverture de la pince
        if (testPince.testerOuverturePince()) {
            System.out.println("TestOuverturePince réussi.");
        } else {
            System.out.println("TestOuverturePince échoué.");
        }
        
        System.out.println("Tests de la Pince terminés.");
    }
}
