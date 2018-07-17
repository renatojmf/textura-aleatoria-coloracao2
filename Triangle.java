
public class Triangle {

    private Point[] vertices;
    private double x_normal;
    private double y_normal;
    private double z_normal;

    public Triangle(Point[] vertices) {
        this.vertices = vertices;
        for (int i = 0; i < 3; i++) 
            this.vertices[i].incrementTriangle();
    }
}