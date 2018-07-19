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

    public void illuminate(Pixel pixel, Point point, Point normal, GraphicsContext ctx,
        Camera camera, double[][] zBuffer, double width, double height) {
        
        Point N, L, V, R = null;
        int x = pixel.getX();
        int y = pixel.getY();    

        boolean hasDiffuseComponent = true;
        boolean hasSpecularComponent = true;

        if(x >= 0 && x < width && y >= 0 && y < height) {

            if(point.getZ() < zBuffer[x][y]) {
                zBuffer[x][y] = point.getZ();

                N = normal.normalize();
                V = point.multiply(-1).normalize();
                L = this.lightPosition.subtract(point).normalize();

                if(V.scalarProduct(N) < 0)
                    N = N.multiply(-1);
                if(N.scalarProduct(L) < 0) {
                    hasDiffuseComponent = false;
                    hasSpecularComponent = false;
                } else {
                    R = N.multiply(2 * N.scalarProduct(L)).subtract(L).normalize();
                    if(R.scalarProduct(V) < 0)
                        hasSpecularComponent = false;
                }
                this.prepareStage(pixel, ctx, N, L, V, R, hasDiffuseComponent, hasSpecularComponent);
            }

        }
    }

    public void prepareStage(Pixel pixel, GraphicsContext ctx, Point N, Point L, Point V, Point R, 
        boolean diffuse, boolean specular) {

            /* Modelo de Iluminação de Phong. */
            Point ambientalComponent = this.ambientalComponent();
            if(diffuse) 
                ambientalComponent = ambientalComponent.add(this.diffuseComponent(N, L));
            if(specular) 
                ambientalComponent = ambientalComponent.add(this.specularComponent(R, V));
            this.draw(pixel, ctx, ambientalComponent);
    }

    public Point ambientalComponent() {
        return new Point(
            this.ambientalVector.getX() * this.ambientalConstant,
            this.ambientalVector.getY() * this.ambientalConstant,
            this.ambientalVector.getZ() * this.ambientalConstant
        );
    }

    public Point diffuseComponent(Point N, Point L) {
        double scalar = N.scalarProduct(L);
        return new Point(
            this.lightIntensity.getX() * this.diffuseVector.getX() * this.diffuseConstant * scalar,
            this.lightIntensity.getY() * this.diffuseVector.getY() * this.diffuseConstant * scalar,
            this.lightIntensity.getZ() * this.diffuseVector.getZ() * this.diffuseConstant * scalar
        );
    }

    public Point specularComponent(Point R, Point V) {
        double scalar = Math.pow(R.scalarProduct(V), this.rugosityConstant);
        return new Point(
            this.lightIntensity.getX() * this.specularConstant * scalar,
            this.lightIntensity.getY() * this.specularConstant * scalar,
            this.lightIntensity.getZ() * this.specularConstant * scalar
        );
    }

    public void draw(Pixel pixel, GraphicsContext ctx, Point I) {
        int R;
        if(I.getX() > 255)
            R = 255;
        else
            R = (int) Math.round(I.getX());

        int G;
        if(I.getY() > 255)
            G = 255;
        else
            G = (int) Math.round(I.getY());

        int B;
        if(I.getZ() > 255)
            B = 255;
        else
            B = (int) Math.round(I.getZ());

       // I.printPoint();
        ctx.setFill(Color.rgb(R, G, B));
        ctx.fillRect(pixel.getX(), pixel.getY(), 1, 1);
    }
    

    
}