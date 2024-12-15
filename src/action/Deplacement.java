package action;

import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;
import lejos.robotics.chassis.*;

/**
 * Classe Deplacement permettant de gérer les déplacements d'un robot 
 * à l'aide d'un châssis différentiel. Cette classe propose des méthodes 
 * pour avancer, tourner et ajuster les vitesses linéaires et angulaires.
 */
public class Deplacement {

    // Diamètre des roues du robot en centimètres
    private static final double DIAMETRE = 5.6;
    private MovePilot pilot; // Contrôleur de mouvement
    private PoseProvider poseProvider; // Fournisseur de position basé sur l'odométrie

    /**
     * Constructeur de la classe Deplacement. 
     * Initialise les roues, le châssis, le pilote et le fournisseur de position.
     */
    public Deplacement() {
        Wheel roue1 = WheeledChassis.modelWheel((RegulatedMotor) Motor.B, DIAMETRE).offset(-6.2);
        Wheel roue2 = WheeledChassis.modelWheel((RegulatedMotor) Motor.A, DIAMETRE).offset(6.2);
        Chassis chassis = new WheeledChassis(new Wheel[] { roue1, roue2 }, WheeledChassis.TYPE_DIFFERENTIAL);
        pilot = new MovePilot(chassis);
        poseProvider = new OdometryPoseProvider(pilot); // Initialisation du PoseProvider
    }

    /**
     * Retourne la direction actuelle du robot en degrés.
     * 
     * @return La direction (orientation) du robot en degrés.
     */
    public float getDirection() {
        Pose pose = poseProvider.getPose();
        return pose.getHeading();
    }

    /**
     * Retourne l'instance de MovePilot utilisée pour le contrôle des mouvements.
     * 
     * @return L'instance MovePilot.
     */
    public MovePilot getPilot() {
        return pilot;
    }

    /**
     * Fait avancer le robot sur une distance spécifiée (mode synchrone).
     * 
     * @param distance Distance en centimètres que le robot doit parcourir.
     */
    public void avancerSync(double distance) {
        pilot.travel(distance, true);
    }

    /**
     * Fait avancer le robot sur une distance spécifiée (mode asynchrone).
     * 
     * @param distance Distance en centimètres que le robot doit parcourir.
     */
    public void avancer(double distance) {
        pilot.travel(distance);
    }

    /**
     * Fait tourner le robot d'un angle spécifié (mode synchrone).
     * Ajuste automatiquement les valeurs d'angle pour compenser les erreurs de rotation.
     * 
     * @param angleToTurn Angle en degrés à parcourir.
     */
    public void tournerSync(double angleToTurn) {
        this.modifVitRot(50);
        if (angleToTurn > 0) {
            angleToTurn += 13; // Ajustement pour les rotations dans le sens antihoraire
        } else if (angleToTurn < 0) {
            angleToTurn += 4; // Ajustement pour les rotations dans le sens horaire
        }
        pilot.rotate(angleToTurn, true);
    }

    /**
     * Fait tourner le robot d'un angle spécifié (mode asynchrone).
     * Ajuste automatiquement les valeurs d'angle pour compenser les erreurs de rotation.
     * 
     * @param angleToTurn Angle en degrés à parcourir.
     */
    public void tourner(double angleToTurn) {
        this.modifVitRot(50);
        if (angleToTurn > 0) {
            angleToTurn += 9; // Ajustement pour les rotations dans le sens antihoraire
        } else if (angleToTurn < 0) {
            angleToTurn += 3; // Ajustement pour les rotations dans le sens horaire
        }
        pilot.rotate(angleToTurn);
    }

    /**
     * Méthode spécialisée pour les rotations nécessitant une interaction avec le poseProvider
     * (par exemple en cas d'erreurs de direction). Peut fonctionner en mode ajusté ou simple.
     * 
     * @param angleToTurn Angle en degrés à parcourir.
     * @param heading Si true, ajuste l'angle pour compenser les erreurs d'odométrie.
     */
    public void tourner(double angleToTurn, boolean heading) {
        if (heading) {
            if (angleToTurn > 0) {
                angleToTurn += 13; // Ajustement pour les rotations dans le sens antihoraire
            } else if (angleToTurn < 0) {
                angleToTurn -= 2; // Ajustement pour les rotations dans le sens horaire
            }
            pilot.rotate(angleToTurn);
        } else {
            tourner(angleToTurn);
        }
    }

    /**
     * Modifie la vitesse angulaire du robot (rotation) en degrés par seconde.
     * 
     * @param s Nouvelle vitesse angulaire.
     */
    public void modifVitRot(double s) {
        pilot.setAngularSpeed(s);
    }

    /**
     * Modifie la vitesse linéaire du robot en centimètres par seconde.
     * 
     * @param s Nouvelle vitesse linéaire.
     */
    public void modifVitLin(double s) {
        pilot.setLinearSpeed(s);
    }

    /**
     * Vérifie si le robot est actuellement en mouvement.
     * 
     * @return true si le robot est en mouvement, false sinon.
     */
    public boolean isMoving() {
        return pilot.isMoving();
    }

    /**
     * Arrête immédiatement tous les mouvements du robot.
     */
    public void stop() {
        pilot.stop();
    }
}
