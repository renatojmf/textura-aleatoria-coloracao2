import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Illumination {

    private Point lightPosition;
    private Point lightIntensity;
    private Point ambientalVector;
    private Point diffuseVector;
    private double ambientalConstant;
    private double diffuseConstant;
    private double specularConstant;
    private double rugosityConstant;

    public Illumination(Point coordinates, double ka, Point Ia, double kd, Point Od, double ks, Point Il, double n) {
        this.lightPosition = coordinates;
        this.ambientalConstant = ka;
        this.ambientalVector = Ia;
        this.diffuseConstant = kd;
        this.diffuseVector = Od;
        this.specularConstant = ks;
        this.lightIntensity = Il;
        this.rugosityConstant = n;
    }

    public void illuminate(Pixel pixel, Point point, GraphicsContext ctx, Camera camera, double[][] zBuffer,
        double width, double height) {
        
        Point N, L, V, R = null;
        int x = pixel.getX();
        int y = pixel.getY();    

        if(x >= 0 && x < width && y >= 0 && y < height) {


        }
    }
    

    
}