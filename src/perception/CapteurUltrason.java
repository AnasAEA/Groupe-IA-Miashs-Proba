package perception; //importation des classes necessaires
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class CapteurUltrason { // initialisation des attributs 
	Float distanceActuelle = Float.MAX_VALUE; //distance actuelle
	Float distancePrecedente = Float.MAX_VALUE; //la derniÃ¨re distance qui a Ã©tÃ© captÃ©e avant celle actuelle
	EV3UltrasonicSensor ultrasonicSensor;  //instance de la classe prÃ©dÃ©finie EV3UltrasonicSensor
    SampleProvider distanceProvider; //echantillon de distance donnÃ© par le capteur ultrason
    float[] samples;  //tableau qui stocke les distances.
	
	
	
	public CapteurUltrason(Port sensorPort) {   //initialisation de l'attribut sensor port qui permet l'accÃ©s au moteur
		this.ultrasonicSensor = new EV3UltrasonicSensor(sensorPort);
		this.distanceProvider = ultrasonicSensor.getDistanceMode();
		this.samples = new float[distanceProvider.sampleSize()]; 		
	}
		
	public float getDistance() {                  //rÃ©cupÃ¨re la distance captÃ©e par l'ultrason et la stocke dans le tableau aprÃ¨s une conversion en cm
        distanceProvider.fetchSample(samples, 0); 
        float distanceCm = samples[0] * 100; 
        return distanceCm; 
    }
	
	
	public boolean detectPalet(){                  //permet de distinguer si l'objet en face du capteur est un palet ou non,                                    
		distancePrecedente = distanceActuelle; //en comparant les distances captÃ©es de getDistance() Ã  la distance minimale Ã  partir de laquelle le palet peut etre detectÃ©e
		distanceActuelle = this.getDistance();
//		System.out.println("Distance Actuelle  = "+distanceActuelle);
		Float minDetection = (float) 32.6;
		return (distancePrecedente <= minDetection && distanceActuelle >= minDetection); 
	}
	
	
	public static void main(String[] args) throws InterruptedException { //main pour tester 
		// TODO Auto-generated method stub
		CapteurUltrason ul = new CapteurUltrason(SensorPort.S1);
		for(int i = 0; i<15;i++) {
			ul.detectPalet();
			Thread.sleep(1000);
		}
		
		
	}
}






















package perception;

import java.util.ArrayList;

import Action.Deplacement;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class CapteurUltrason {
	Float distanceActuelle = Float.MAX_VALUE; //distance actuelle
	Float distancePrecedente = Float.MAX_VALUE; //la derniÃ¨re distance qui a Ã©tÃ© captÃ©e avant celle actuelle
	EV3UltrasonicSensor ultrasonicSensor;  //instance de la classe prÃ©dÃ©finie EV3UltrasonicSensor
    SampleProvider distanceProvider; //echantillon de distance donnÃ© par le capteur ultrason
    float[] samples;  //tableau qui stocke les distances.
    Deplacement deplacement;
    ArrayList <float[][]> liste;
	
	
	
	public CapteurUltrason(Port sensorPort) {   //initialisation de l'attribut sensor port qui permet l'accÃ©s au moteur
		this.ultrasonicSensor = new EV3UltrasonicSensor(sensorPort);
		this.distanceProvider = ultrasonicSensor.getDistanceMode();
		this.samples = new float[distanceProvider.sampleSize()]; 
		deplacement = new Deplacement();
		liste = new ArrayList <float[][]>();
	}
		
	public float getDistance() {                  //rÃ©cupÃ¨re la distance captÃ©e par l'ultrason et la stocke dans le tableau aprÃ¨s une conversion en cm
        distanceProvider.fetchSample(samples, 0); 
        float distanceCm = samples[0] * 100; 
        return distanceCm; 
    }
	
	
	public boolean detectPalet1(){                  //permet de distinguer si l'objet en face du capteur est un palet ou non,                                    
		distancePrecedente = distanceActuelle; //en comparant les distances captÃ©es de getDistance() Ã  la distance minimale Ã  partir de laquelle le palet peut etre detectÃ©e
		distanceActuelle = this.getDistance();
		System.out.println("Distance Actuelle  = "+distanceActuelle);
		Float minDetection = (float) 32.6;
		return (distancePrecedente <= minDetection && distanceActuelle >= minDetection); 
	}
	
	public boolean detectPalet2() { // detect un pqlet et s'qrrete 32cm du pqlet. puis il fqut qppeler methode qui qvqnce vers le pqlet et qctive les pinces.
		float distancePrecedente = 700;
		while(distancePrecedente > this.getDistance()) { //ou !isPressed()
			if(this.getDistance() < 32){
				return false;
			}
			System.out.println("D1 : " + distancePrecedente);
			float distanceActuelle = this.getDistance();
			System.out.println("D2 :" + distancePrecedente);
			deplacement.avancer(5);
			distancePrecedente = distanceActuelle;
		}
		return  distancePrecedente < this.getDistance();
	}
	
	
	public boolean detectPalet3(float distancePrecedente) {
		deplacement.avancerAsync(distancePrecedente);
		MovePilot pilot = deplacement.getPilot();
		while (pilot.isMoving()) {
			while(distancePrecedente > this.getDistance()) { //ou !isPressed()
				if(this.getDistance() < 32){
					return false;
				}
				System.out.println("D1 : " + distancePrecedente);
				float distanceActuelle = this.getDistance();
				System.out.println("D2 :" + distancePrecedente);
				//deplacement.avancer(5);
				distancePrecedente = distanceActuelle;
			}
			return  distancePrecedente < this.getDistance();
		} return false;
	}
	
	
	public boolean detectObjett() {
		float distanceDobjet = this.getDistance();
		if (distanceDobjet < 60) {
			return detectPalet3(distanceDobjet);
		}
		return false;
	}
	
	
	public ArrayList<float[][]> detectLesObjets() {
		deplacement.tournerAsync(354);
		MovePilot pilot = deplacement.getPilot();
		float angleInitial = deplacement.getDirection(); 
	    this.liste.clear();
		while (pilot.isMoving()) { 
		    do {
		        float distance = this.getDistance(); 
		        float direction = deplacement.getDirection();  
		        float[][] objet = {{distance, direction}};
		        this.liste.add(objet);
		         
		    } while (deplacement.getDirection() < angleInitial + 354); 
		    return this.liste;
		}
		return this.liste;
	}
	
	
	public void detectObjet() { //cette methode sert à comprendre le fonctionnement du robot, elle sera modifiée apres
		float d1 = 700;
		for(int i=0; i<15;i++) {
			System.out.println("Distance precedente  = " + d1);
			deplacement.avancer(3);
			float d2 = this.getDistance();
			System.out.println("Distance actuelle approximee = " + d2);
			if(d1 < this.getDistance()) {
				deplacement.avancer(22); // sera remplacé par (! t.isPressed)
				System.out.println("je suis devant le palet"); 
				deplacement.stop();
				break;
			}else if (this.getDistance() < 31){
				System.out.println("c'est pas un palet");
				deplacement.tourner(84);
				deplacement.avancer(10);
				break;		
			}else {
				deplacement.avancer(5);
			}
			d1 = d2;
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException { //main pour tester 
		// TODO Auto-generated method stub
		CapteurUltrason ul = new CapteurUltrason(SensorPort.S1);
		Deplacement d = new Deplacement();
		CapteurTouche t = new CapteurTouche(SensorPort.S3);
		
		ul.detectPalet2();
		
		//boolean det = ul.detectPalett();
		//System.out.println(det);
		//Thread.sleep(3000);
		
		/*for (int i=0; i < 40; i++) {
			System.out.println(ul.getDistance());
			Thread.sleep(2000);
			//d.avancer(10);
		}
		
		/*float d2 = 100;
		for(int i=0; i<15;i++) {
			System.out.println("d2 : " + d2);
			d.avancer(3);
			float d3 = ul.getDistance();
			//t.add(d3);
			System.out.println("d3 : "+ d3);
			if(d2 < ul.getDistance()) {
				d.avancer(24);
				System.out.println("je suis devant le palet");
				d.stop();
				break;
			}else if (ul.getDistance() < 31){
				System.out.println("c'est pas un palet");
				d.tourner(84);
				d.avancer(10);
				break;
				
			}else {
				d.avancer(5);
			}
			d2 = d3;
				//pilot.rotate(354);
			Thread.sleep(3000);
		}
		
		/*while(!t.isPressed()) {
			d.avancerAsync(5);
		}
		d.stop();*/
		
		
	}

}

 //Detect Palet by Marieme
	public CapteurUltrason() {   //initialisation de l'attribut sensor port qui permet l'accÃ©s au moteur
		this.ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
		this.distanceProvider = ultrasonicSensor.getDistanceMode();
		this.samples = new float[distanceProvider.sampleSize()]; 		
	    this.distances = new float [MAX]; //  MAX longueur du tableau
	   this.d = new Deplacement(); //instance de la classe déplacement
	}
	
	
	public void  enregistrerDistances(long duree) { // enregistre les distances  captées dans un tableau au fur et  mesure qu'on avance
		d.avancerAsync(200);  // on avance de manière asynchroniue
		MovePilot pilot = d.getPilot();
		while (pilot.isMoving()) {
		int i = 0; 
		long startTime = System.currentTimeMillis(); // on definit la duree
		while ((System.currentTimeMillis()- startTime) < duree && i < distances.length) { tant que la duree n'est pas atteinte
			distanceProvider.fetchSample(samples,  0); //on capte les distances
			distances[i] = samples [0]*100; // conversion en cm
			i++;
		}
		d.stop(); // on s'arrete une foit que la duree est finie
		try { // on fait une exception
			Thread.sleep(100);
		}
	catch (InterruptedException e) {
		e.printStackTrace();
	}
	}

	}
	public boolean detectPalet( long duree){  // on detecte pendant une certaine duree
		enregistrerDistances(duree); // on genere le tableau des distances tout en avancant
		int j = 0;
			while ( j <= distances.length) { // on parcoure le tableau des distances 
				if (distances[j] > 32.6 ) { //tant qu'on  une distance superieur  32.6 , on continue  parcourir
					j++;
			}
				}
				if (j < distances.length) { // si on s'est arrete avant la fin du tableau on a eu une distance inferieur a 32.6 donc ce n'est pas un palet
					return false;
				}
				else { //si on finit le tableau on a pas un palet
					return true;
				}
	}

		
