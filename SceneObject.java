import java.util.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
            if(!isTriangle(projectedVertices)) 
                continue;

            /* Rasterização. */
            Vector<Pixel> internPixels = new Rasterizer(projectedVertices).rasterize();
            for (int j = 0; j < internPixels.size(); j++) {
            
                double[] barycentricCoordinates = this.triangles[i].resolve(internPixels.get(j), projectedVertices);
                Point approximation = this.triangles[i].approximate(barycentricCoordinates);
                Point normalApproximation = this.triangles[i].approximateNormal(barycentricCoordinates);
  
                /* Iluminação. */
                scene.illuminate(internPixels.get(j), approximation, normalApproximation, ctx, camera, zBuffer, width, height);
            }
            
        }
        

    }

    public boolean isTriangle(Pixel[] vertices) {
        // Pixel v1v0 = vertices[1].subtract(vertices[0]);
        // Pixel v2v1 = vertices[1].subtract(vertices[2]);
        // Pixel v2v0 = vertices[2].subtract(vertices[0]);
        // double v1v0Norm = v1v0.norm();
        // double v2v0Norm = v2v0.norm();
        // double v1v2Norm = v2v1.norm();
        // if(v1v0Norm + v2v0Norm < v1v2Norm) 
        //     return false;
        // if(v1v0Norm + v1v2Norm < v2v0Norm)
        //     return false;
        // if(v1v2Norm + v2v0Norm < v1v0Norm)
        //     return false;
        // return true;
        if(vertices[1].getY() == vertices[0].getY() || vertices[2].getY() == vertices[0].getY() || vertices[1].getY() == vertices[2].getY())
            return false;
        if(vertices[1].subtract(vertices[0]).calculateArea(vertices[2].subtract(vertices[0])) == 0)
            return false;
        return true;
    }
    
}