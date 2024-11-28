package test;

import perception.CapteurUltrason;
import lejos.hardware.port.SensorPort;


//import action.Deplacement;


public class CapteurUltrasonTest extends CapteurUltrason {
  
  	
	public CapteurUltrasonTest () {   //initialisation de l'attribut sensor port qui permet l'accÃ©s au moteur
		super(SensorPort.S1);
	}

public void TestFonction() {
		MovePilot pilot  = super.deplacement.getPilot();  
	    while (pilot.isMoving()) {
	     if (super.detectObjet()) {
		System.out.println("Objet Detecte");
		 if (super.detecterPalet()) {
				System.out.println("Palet Detecte");
			     }
	     }
	   }
		
	
	}
	
	public void getDistanceTest(float x) {
		boolean a =  this.getDistance() == x;
		System.out.println(""+a);
	}
	
	
	
	public void detectObjectTest() {
		int i = 0;
		while (i < 7) {
		super.deplacement.avancerAsync(10);
		System.out.println(super.detectObjet());
		i++;
		}
		super.deplacement.stop();
	}
	
	public void detectObjectTestbis() {
		int i = 0;
		while (i < 12) {
		super.deplacement.tournerAsync(30);
		System.out.println(super.detectObjet());
		i++;
		}
		super.deplacement.stop();
	}
	
	public void detecterPaletTest(){
		int i = 0;
		while (i< 12 && super.detecterPalet() == false ) {
			super.deplacement.tournerAsync(30);;
		}
		
	}
	
	public static void main(String[] args) {
	CapteurUltrasonTest C = new  CapteurUltrasonTest();
	C.detectObjectTest();
	C.detecterPaletTest();
	C.getDistanceTest(25);

	}

}
