
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
    
}