import java.util.Vector;

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
    public void finalSteps(Camera camera, Illumination scene, double width, double height) {

        for (int i = 0; i < this.triangles.length; i++) {
            
            /* Projeção para coordenadas de tela. */
            Pixel[] projectedVertices = this.triangles[i].projectVertices(camera.getD(), camera.getHx(), camera.getHy(), width, height);

            /* Rasterização. */
            Vector<Pixel> internPixels = new Rasterizer(projectedVertices).rasterize();
        }
        

    }
    
}