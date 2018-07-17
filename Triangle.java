
public class Triangle {

    private Point[] vertices;
    private double x_normal;
    private double y_normal;
    private double z_normal;

    public Triangle(Point[] vertices) {
        this.vertices = vertices;
        for (int i = 0; i < 3; i++) 
            this.vertices[i].incrementTriangle();
        this.calculateNormal();
    }

    public void calculateNormal() {
        Point v1v0 = this.vertices[1].subtract(this.vertices[0]);
        Point v2v0 = this.vertices[2].subtract(this.vertices[0]);
        Point normal = v1v0.vectorProduct(v2v0).normalize();

        this.x_normal = normal.getX();
        this.y_normal = normal.getY();
        this.z_normal = normal.getZ();
    }

    public void addNormalToVertices() {
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i].addNormal(x_normal, y_normal, z_normal);
        }
    }

    public void normalizeNormal() {
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i].normalizeNormal();
        }
    }

    public Pixel[] projectVertices(double d, double hx, double hy, double width, double height) {
        Pixel[] projectedVertices = new Pixel[3];
        for (int i = 0; i < this.vertices.length; i++) {
            double x = ((d/hx) * vertices[i].getX()/vertices[i].getZ());
            double y = ((d/hy) * vertices[i].getY()/vertices[i].getZ());
            projectedVertices[i] = new Pixel((int) ((x + 1) * width / 2), (int) ((1 - y) * height / 2));
        }
        return projectedVertices;
    }
}