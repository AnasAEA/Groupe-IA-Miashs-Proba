package perception;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;

public class ColorSensor extends EV3ColorSensor {
    public ColorSensor(Port port) {
        super(port);
    }

  
    public String getCurrentColor() {
        int colorId = getColorID();
        switch (colorId) {
            case Color.RED: return "Rouge";
            case Color.GREEN: return "Vert";
            case Color.BLUE: return "Bleu";
            case Color.YELLOW: return "Jaune";
            case Color.BLACK: return "Noir";
            case Color.WHITE: return "Blanc";
            case Color.GRAY: return "Gris";
            default: return "Inconnu";
        }
    }

   
    public String changeColor() {
        return getCurrentColor();
    }
}
