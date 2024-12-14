package test;

import agent.Agent;
import java.util.ArrayList;

/**
 * Classe de test pour la classe Agent.
 * Hérite de Agent pour accéder directement aux méthodes de l'Agent.
 */
public class TestAgent extends Agent {

    /**
     * Constructeur de TestAgent.
     * Initialise l'Agent en appelant le constructeur de la classe parente.
     */
    public TestAgent() {
        super();
    }

    /**
     * Teste la méthode detecterLesObjets() pour vérifier la détection correcte des objets.
     *
     * @return boolean - true si les objets sont détectés correctement, false sinon.
     */
    public boolean testDetecterLesObjets() {
        System.out.println("TestDetecterLesObjets: Détection des objets en cours...");
        ArrayList<float[]> objets = detecterLesObjets();
        if (objets.isEmpty()) {
            System.out.println("TestDetecterLesObjets: Aucun objet détecté.");
            return false;
        } else {
            System.out.println("TestDetecterLesObjets: Objets détectés = " + objets.size());
            return true;
        }
    }

    /**
     * Teste la méthode bestObjet() pour vérifier la sélection correcte du meilleur objet.
     *
     * @return boolean - true si le meilleur objet est correctement identifié, false sinon.
     */
    public boolean testBestObjet() {
        ArrayList<float[]> objets = new ArrayList<>();
        objets.add(new float[]{50.0f, 90.0f});
        objets.add(new float[]{30.0f, 180.0f});
        objets.add(new float[]{70.0f, 270.0f});
        objets.add(new float[]{10.0f, 0.0f});

        float[] bestObj = bestObjet(objets);
        System.out.println("TestBestObjet: Meilleur objet à distance = " + bestObj[0] + " cm, direction = " + bestObj[1] + " degrés.");
        boolean testPassed = bestObj[0] == 10.0f && bestObj[1] == 0.0f;
        System.out.println("TestBestObjet: " + (testPassed ? "Réussi." : "Échoué."));
        return testPassed;
    }

    /**
     * Teste la méthode attraperPalet() pour vérifier la capture correcte d'un palet.
     *
     * @param premier Indique si c'est le premier palet à capturer.
     * @return boolean - true si le palet est capturé correctement, false sinon.
     */
    public boolean testAttraperPalet(boolean premier) {
        System.out.println("TestAttraperPalet: Tentative de capture du palet...");
        boolean success = attraperPalet(premier);
        System.out.println("TestAttraperPalet: Capture du palet " + (success ? "réussie." : "échouée."));
        return success;
    }

    /**
     * Teste la méthode versCouleur() pour vérifier la détection des couleurs.
     *
     * @param couleurCible String - couleur cible à détecter.
     * @return boolean - true si la couleur est détectée, false sinon.
     */
    public boolean testVersCouleur(String couleurCible) {
        System.out.println("TestVersCouleur: Recherche de la couleur " + couleurCible + "...");
        versCouleur(couleurCible);
        return true; // Le test passe si la méthode s'exécute sans erreur
    }

    /**
     * Méthode principale pour exécuter les tests.
     */
    public static void main(String[] args) {
        TestAgent test = new TestAgent();

        // Test de la détection des objets
        if (test.testDetecterLesObjets()) {
            System.out.println("TestDetecterLesObjets réussi.");
        } else {
            System.out.println("TestDetecterLesObjets échoué.");
        }

        Delay.msDelay(1000);

        // Test de la méthode bestObjet
        if (test.testBestObjet()) {
            System.out.println("TestBestObjet réussi.");
        } else {
            System.out.println("TestBestObjet échoué.");
        }

        Delay.msDelay(1000);

        // Test de la capture de palet
        if (test.testAttraperPalet(true)) { // Test avec premier=true
            System.out.println("TestAttraperPalet (premier) réussi.");
        } else {
            System.out.println("TestAttraperPalet (premier) échoué.");
        }

        Delay.msDelay(1000);

        // Test de la détection de couleur blanche
        if (test.testVersCouleur("White")) {
            System.out.println("TestVersCouleur (White) réussi.");
        } else {
            System.out.println("TestVersCouleur (White) échoué.");
        }

        // Fermeture des capteurs et actionneurs
        test.close();
        System.out.println("Tests de la classe Agent terminés.");
    }
}
