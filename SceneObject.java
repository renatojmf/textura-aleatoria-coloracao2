import java.util.Vector;
import javafx.scene.canvas.GraphicsContext;

public class SceneObject {

    private Triangle[] triangles;

    public SceneObject(Triangle[] triangles) {
        this.triangles = triangles;
    }
    
    /* Cálculo da normal dos triângulos e normalização das normais. */
    public void normalize() {

        for (int i = 0; i < this.triangles.length; i++) 
            this.triangles[i].addNormalToVertices();    
        
        for (int i = 0; i < this.triangles.length; i++) 
            this.triangles[i].normalizeNormal();

    }

    /* Passos finais. */
    public void finalSteps(Camera camera, Illumination scene, double width, double height, GraphicsContext ctx,
        double[][] zBuffer) {

        for (int i = 0; i < this.triangles.length; i++) {
            
            /* Projeção para coordenadas de tela. */
            Pixel[] projectedVertices = this.triangles[i].projectVertices(camera.getD(), camera.getHx(), camera.getHy(), width, height);

            /* Rasterização. */
            Vector<Pixel> internPixels = new Rasterizer(projectedVertices).rasterize();
            for (int j = 0; j < internPixels.size(); j++) {

                double[] barycentricCoordinates = this.resolve(internPixels.get(j), projectedVertices);
                Point approximation = this.triangles[i].approximate(barycentricCoordinates);

                /* Iluminação. */
                scene.illuminate(internPixels.get(j), approximation, ctx, camera, zBuffer, width, height);
            }
            
        }
        

    }

    public double[] resolve(Pixel pixel, Pixel[] vertices) {
        int x = pixel.getX() - vertices[2].getX();
        int y = pixel.getY() - vertices[2].getY(); 
        int x1 = vertices[0].getX() - vertices[2].getX();
        int y1 = vertices[0].getY() - vertices[2].getY();
        int x2 = vertices[1].getX() - vertices[2].getX();
        int y2 = vertices[1].getY() - vertices[2].getY();

        double a2 = (double) (y*x1 - y1*x) / (-y1*x2 + y2*x1);
        double a1 = (x - x2 * a2) / x1;
        double a3 = 1 - a1 - a2;
        return new double[] {a1, a2, a3};
    }
    
}