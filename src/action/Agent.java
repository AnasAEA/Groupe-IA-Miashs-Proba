package action;
import java.util.ArrayList;

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

    // Constructor
    public Agent() {
        capteurUltrason = new CapteurUltrason(SensorPort.S1);
        capteurTouche = new CapteurTouche(SensorPort.S2);
        deplacement = new Deplacement();
      pince = new Pince();
    }

    public void setaPalet(boolean aPalet) {
        this.aPalet = aPalet;
    }

  
    public boolean isaPalet() {
        return this.aPalet;
    }
    
    public boolean detectObjet() {
        float distanceDobjet = capteurUltrason.getDistance();
        if (distanceDobjet < 60) {
            return capteurUltrason.detecterPalet();
        }
        return false;
    }
    
    // Method to detect objects by rotating 360 degrees
    public ArrayList<float[]> detecterLesObjets() {
        deplacement.getPilot().setAngularSpeed(30); // Régler la vitesse de rotation
        deplacement.tournerAsync(360); // Démarrer une rotation de 360 degrés
        MovePilot pilot = deplacement.getPilot();
        this.liste.clear();

        while (pilot.isMoving()) {
            float distance = capteurUltrason.getDistance();
            float directionActuelle = deplacement.getDirection(); // Obtenir l'orientation actuelle

            // Collecter les données si la distance est inférieure à un certain seuil
            if (distance < 60.0f) {
                float[] objet = { distance, directionActuelle };
                this.liste.add(objet);
                System.out.println("Objet détecté à une distance de : " + distance + " cm, direction : " + directionActuelle + " degrés.");
            }

            // Pause pour éviter de surcharger le processeur
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pilot.stop(); // S'assurer que le robot arrête de tourner
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
    
    
    public void run() {
        // Detect all objects
        ArrayList<float[]> objets = detecterLesObjets();

        // Find the best object (closest)
        float[] bestObj = bestObjet(objets);

        if (bestObj != null) {
            float distanceToObject = bestObj[0];
            float directionToObject = bestObj[1];

            System.out.println("Best object found at distance: " + distanceToObject + " cm, direction: " + directionToObject + " degrees.");
            deplacement.getPilot().setAngularSpeed(30); // Régler la vitesse de rotation
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
            // Advance towards the object
        /*    deplacement.avancerSync(distanceToObject);

            while (deplacement.getPilot().isMoving()) {
                // Wait for movement to complete
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/

            // Check if it's a palet
            boolean isPalet = capteurUltrason.detecterPalet();
            if (isPalet) {
                System.out.println("Palet detected!");
                // Proceed to collect the palet
            } else {
                System.out.println("Not a palet.");
                // Handle accordingly
            }
        } else {
            System.out.println("No objects detected.");
        }

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
