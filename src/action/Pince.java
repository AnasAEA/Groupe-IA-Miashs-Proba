package action;


import lejos.hardware.motor.NXTRegulatedMotor;

public class Pince {
    private NXTRegulatedMotor motor;
    private boolean etatFerme;

    public Pince(NXTRegulatedMotor motor) {
        this.motor = motor;
        this.etatFerme = false; 
    }

 
    public void saisirPalet() {
        motor.rotate(-360 * 3); 
        etatFerme = true;
    }


    public void lacherPalet() {
        motor.rotate(360 * 3);
        etatFerme = false;
    }


    public void setEtat(boolean etat) {
        etatFerme = etat;
    }
}
