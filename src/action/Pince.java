package action;

import lejos.hardware.motor.NXTRegulatedMotor;

public class Pince {
    
    private NXTRegulatedMotor pinceMotor;
    private boolean etat; // true si les pinces sont fermées, false si ouvertes

    public Pince(NXTRegulatedMotor motor) {
        this.pinceMotor = motor;
        this.pinceMotor.setSpeed(motor.getSpeed() * 2); // Ajustement de la vitesse
        this.etat = true; // Initialisé comme fermé
    }
//   public void saisirPalet() {
        if (!etat) { // Si les pinces ne sont pas déjà fermées
            pinceMotor.rotate(-360 * 3); // Ferme les pinces
            while (pinceMotor.isMoving()) {}
            etat = true;
            System.out.println("Palet saisi."); //
    // Active les pinces pour saisir un palet
    public void saisirPalet() {
        if (!etat) { // Si les pinces ne sont pas déjà fermées
            pinceMotor.rotate(-360 * 3); // Ferme les pinces (ajustez la rotation si nécessaire)
            while (pinceMotor.isMoving()) {}
            etat = true;
            System.out.println("Palet saisi.");
        }
    }

    // Relâche le palet
    public void lacherPalet() {
        if (etat) { // Si les pinces ne sont pas déjà ouvertes
            pinceMotor.rotate(360 * 3); // Ouvre les pinces
            while (pinceMotor.isMoving()) {}
            etat = false;
            System.out.println("Palet relâché.");
        }
    }

    // Modifie l'état des pinces
    public void setEtat(boolean etat) {
        if (etat) {
            saisirPalet();
        } else {
            lacherPalet();
        }
    }

    // Vérifie si les pinces sont fermées
    public boolean isClose() {
        return etat;
    }

    // Vérifie si les pinces sont ouvertes
    public boolean isOpen() {
        return !etat;
    }

    // Permet de définir manuellement l'état des pinces
    public void setClosed(boolean etat) {
        this.etat = etat;
    }
