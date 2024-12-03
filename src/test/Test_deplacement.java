package test;

import Action.Deplacement;
public class Test_deplacement extends Deplacement{
		
	public Test_deplacement() {
		super();
	}
	
	
	public void test_getDirection_tourner() { //on test les deux methodes getDirection() et tourner(double angleToTurn)
		//la direction initiale
		float directionInitiale = super.getDirection();
		System.out.println("la direction initiale est " + directionInitiale);
		
		//la direction apres une rotation de 90° vers la droite
		super.tourner(90); 
		float directionApresRot90D = super.getDirection();// doit etres egale à directionInitiale + 90
		System.out.println("la direction apres une rotation de 90° vers la droite est " + directionApresRot90D);
		
		//la direction apres une rotation de 90° vers la gauche
		super.tourner(-90);
		float directionApresRot90G = super.getDirection(); // doit etre egale à directionInitiale
		System.out.println("la direction apres une rotation de 90° vers la gauche" + directionApresRot90G);
		
		//la direction apres une rotation de 360° 
		super.tourner(360);
		float directionApresRot360 = super.getDirection(); //doit egale à directionApresRot90G 
		System.out.println("la direction apres une rotation de 360° " + directionApresRot360);
	}
	
	
	
	public void test_avancerSync_avancer_isMoving() { //on test les deux methodes avancerSync(double distance) et public void avancer(double distance)
		//avance 50cm
		super.avancer(50);
		
		//avance 100cm en comptant
		super.avancerSync(100);		
		// Boucle qui s'exécute pendant le déplacement
		int i = 0;
		while (super.isMoving()) {
			System.out.println(i);
		    try {
		    	Thread.sleep(500); // Pause de 500 ms entre chaque itération pour simuler un affichage espacé
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		    i++;
	    }
	}
	
	public void test_tournerAsync_tourner(){ ////on test les deux methodes tournerAsync(double angleToTurn) et tourner(double angleToTurn, boolean heading)
		float directionInitiale = super.getDirection();
		//tourne 160° en comptant
		super.tournerAsync(160);		
		// Boucle qui s'exécute pendant la rotation
		int i = 0;
		while (super.isMoving()) {
			System.out.println(i);
		    try {
		    	Thread.sleep(500); // Pause de 500 ms entre chaque itération pour simuler un affichage espacé
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		    i++;
	    }
				
		//il doit retourner à la direction initiale
		super.tourner(directionInitiale, true);
	}
	
	public void test_modifVitLin() { //on test la methode modifVitLin(double s)
		//on modifie la vitesse lineaire.
		super.modifVitLin(70);
		//apres le robot avance 100cm, la vitesse d'avancement doit augmenter
		super.avancer(100);
	}
	
	public void test_modifVitRot() { //on test la methode modifVitRot(double s)
		//on modifie la vitesse de rotation.
		super.modifVitRot(80);
		//apres le robot tourne 360° avec une vitesse 80
		super.tourner(360);
	}
	
	
	
	public static void main(String[] args) {
		Test_deplacement td = new Test_deplacement();
		td.test_getDirection_tourner();
		
	}

}
