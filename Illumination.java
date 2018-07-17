
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
    

    
}