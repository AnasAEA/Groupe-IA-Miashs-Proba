package agent;

import java.util.ArrayList;

import action.Deplacement;
import lejos.hardware.port.SensorPort;
import lejos.robotics.navigation.MovePilot;
import perception.CapteurTouche;
import perception.CapteurUltrason;

public class Agent {
    // Existing class attributes
    private CapteurUltrason capteurUltrason;
    private CapteurTouche capteurTouche;
    private Deplacement deplacement;
    private ArrayList<float[]> liste = new ArrayList<>();
    private float directionCampAdverse; // D�claration en tant qu'attribut de classe

    // Constructor
    public Agent() {
        capteurUltrason = new CapteurUltrason(SensorPort.S1);
        capteurTouche = new CapteurTouche(SensorPort.S2);
        deplacement = new Deplacement();
    }

    /**
     * D�tecte tout objet � une distance inf�rieure � 50 cm.
     * 
     * @return true si un objet est d�tect�, false sinon.
     */
    public boolean detectObjet() {
        float distanceDobjet = capteurUltrason.getDistance();
        return distanceDobjet < 50;
    }

    // Method to detect objects by rotating 360 degrees
    public ArrayList<float[]> detecterLesObjets() {
        deplacement.getPilot().setAngularSpeed(30); // R�gler la vitesse de rotation
        deplacement.tournerAsync(360); // D�marrer une rotation de 360 degr�s
        MovePilot pilot = deplacement.getPilot();
        this.liste.clear();

        while (pilot.isMoving()) {
            float distance = capteurUltrason.getDistance();
            float directionActuelle = deplacement.getDirection(); // Obtenir l'orientation actuelle

            // Collecter les donn�es si la distance est inf�rieure � un certain seuil
            if (distance < 60.0f) {
                float[] objet = { distance, directionActuelle };
                this.liste.add(objet);
                System.out.println("Objet d�tect� � une distance de : " + distance + " cm, direction : "
                        + directionActuelle + " degr�s.");
            }

            // Pause pour �viter de surcharger le processeur
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pilot.stop(); // S'assurer que le robot arr�te de tourner
        return this.liste;
    }

    // Method to find the closest object
    public float[] bestObjet(ArrayList<float[]> objets) {
        if (objets.isEmpty()) {
            return null;
        }
        float minDistance = Float.MAX_VALUE;
        float[] bestObjet = null;
        for (float[] objet : objets) {
            float distance = objet[0];
            if (distance < minDistance) {
                minDistance = distance;
                bestObjet = objet;
            }
        }
        return bestObjet;
    }

    private void marquerPalet() {
        // Ajuster l'orientation du robot vers le camp adverse
        float currentDirection = deplacement.getDirection();
        float angleToTurn = directionCampAdverse - currentDirection;

        // Ajuster l'angle pour qu'il se situe entre -180 et 180 degrés
        if (angleToTurn > 180) {
            angleToTurn -= 360;
        } else if (angleToTurn < -180) {
            angleToTurn += 360;
        }

        // Déplacer le robot vers la ligne blanche (camp adverse)
        versCouleur(White);

        // Avancer d'environ 5 cm pour s'assurer que le palet se trouve bien dans le
        // camp adverse
        deplacement.avancer(5);
        // Lâcher le palet
        lacherPalet();
        deplacement.avancer(-10);
    }

    public boolean ChercherPalet() {
        ArrayList<float[]> objets = detecterLesObjets();

        // Find the best object (closest)
        float[] bestObj = bestObjet(objets);

        if (bestObj != null) {
            float distanceToObject = bestObj[0];
            float directionToObject = bestObj[1];

            System.out.println("Best object found at distance: " + distanceToObject + " cm, direction: "
                    + directionToObject + " degrees.");
            deplacement.getPilot().setAngularSpeed(30); // R�gler la vitesse de rotation
            // Calculate angle to turn
            float currentDirection = deplacement.getDirection();
            float angleToTurn = directionToObject - currentDirection;

            // Compensate for the 7-degree deviation
            if (angleToTurn > 0) {
                angleToTurn -= 7; // If turning clockwise, reduce angle
            } else if (angleToTurn < 0) {
                angleToTurn += 7; // If turning counter-clockwise, increase angle
            }

            // Adjust angle to be between -180 and 180 degrees
            if (angleToTurn > 180) {
                angleToTurn -= 360;
            } else if (angleToTurn < -180) {
                angleToTurn += 360;
            }

            // Rotate to face the object
            deplacement.getPilot().rotate(angleToTurn);

            // Check if it's a palet
            boolean isPalet = capteurUltrason.detecterPalet();
            return isPalet;
        }
        return false;
    }

    public void run() {
        // Initialiser la direction du camp adverse
        directionCampAdverse = deplacement.getDirection();
        System.out
                .println("Direction du camp adverse initialement réglée à : " + directionCampAdverse + " degrés.");

        // Cleanup resources
        capteurUltrason.close();
        capteurTouche.close();
        deplacement.stop();
        System.out.println("Program terminated.");
    }

    public static void main(String[] args) {
        Agent agent = new Agent();
        agent.run();
    }

}
