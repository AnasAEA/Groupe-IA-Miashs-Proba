package helloWorld;
import lejos.hardware.Button;
import lejos.utility.Delay;

public class helloWorld {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			System.out.println("Hello World!");
			Button.ENTER.waitForPress();

		} catch (Throwable t) {
			t.printStackTrace();
			Delay.msDelay(10000);
			System.exit(0);
		}
	}

}
