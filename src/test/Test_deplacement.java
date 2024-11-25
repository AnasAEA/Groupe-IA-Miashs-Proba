package test_deplacement;
import Action.Deplacement;
public class Test_deplacement {
	
	Deplacement d;
	
	public Test_deplacement() {
		super();
		d = new Deplacement();
	}
	
	
	public void test_getDirection_tourner() { //on test les deux methodes getDirection() et tourner(double angleToTurn)
		//la direction initiale
		float directionInitiale = d.getDirection();
		System.out.println("la direction initiale est " + directionInitiale);
		
		//la direction apres une rotation de 90° vers la droite
		d.tourner(90); 
		float directionApresRot90D = d.getDirection();// doit etres egale à directionInitiale + 90
		System.out.println("la direction apres une rotation de 90° vers la droite est " + directionApresRot90D);
		
		//la direction apres une rotation de 90° vers la gauche
		d.tourner(-90);
		float directionApresRot90G = d.getDirection(); // doit etre egale à directionInitiale
		System.out.println("la direction apres une rotation de 90° vers la gauche" + directionApresRot90G);
		
		//la direction apres une rotation de 360° 
		d.tourner(360);
		float directionApresRot360 = d.getDirection(); //doit egale à directionApresRot90G 
		System.out.println("la direction apres une rotation de 360° " + directionApresRot360);
	}
	
	
	
	public void test_avancerSync_avancer_isMoving() { //on test les deux methodes avancerSync(double distance) et public void avancer(double distance)
		//avance 50cm
		d.avancer(50);
		
		//avance 100cm en comptant
		d.avancerSync(100);		
		// Boucle qui s'exécute pendant le déplacement
		int i = 0;
		while (d.isMoving()) {
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
		float directionInitiale = d.getDirection();
		//tourne 160° en comptant
		d.tournerAsync(160);		
		// Boucle qui s'exécute pendant la rotation
		int i = 0;
		while (d.isMoving()) {
			System.out.println(i);
		    try {
		    	Thread.sleep(500); // Pause de 500 ms entre chaque itération pour simuler un affichage espacé
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		    i++;
	    }
				
		//il doit retourner à la direction initiale
		d.tourner(directionInitiale, true);
	}
	
	public void test_modifVitLin() { //on test la methode modifVitLin(double s)
		//on modifie la vitesse lineaire.
		d.modifVitLin(70);
		//apres le robot avance 100cm, la vitesse d'avancement doit augmenter
		d.avancer(100);
	}
	
	public void test_modifVitRot() { //on test la methode modifVitRot(double s)
		//on modifie la vitesse de rotation.
		d.modifVitRot(80);
		//apres le robot tourne 360° avec une vitesse 80
		d.tourner(360);
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
