import java.util.Arrays;
import java.util.Comparator;

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
            double x = (d/hx * vertices[i].getX()/vertices[i].getZ());
            double y = (d/hy * vertices[i].getY()/vertices[i].getZ());  
            projectedVertices[i] = new Pixel((int) ((x + 1) * width / 2), (int) ((1 - y) * height / 2));
        }
        return projectedVertices;
    }

    public double[] resolve(Pixel pixel, Pixel[] vertices) {
        sortVertices(vertices);

        /* Triângulo. */
        Pixel v0v1 = vertices[1].subtract(vertices[0]);
        Pixel v0v2 = vertices[2].subtract(vertices[0]);
        double area = v0v1.calculateArea(v0v2);
 
        /* Alfa. */
        Pixel bp = pixel.subtract(vertices[1]);
        Pixel bc = vertices[2].subtract(vertices[1]);
        double pbcArea = bp.calculateArea(bc) / area;

        /* Beta. */
        Pixel ap = pixel.subtract(vertices[0]);
        Pixel ac = vertices[2].subtract(vertices[0]);
        double apcArea = ac.calculateArea(ap) / area;

        /* Gama. */
        double abpArea = 1 - pbcArea - apcArea;
        
        //System.out.println(area);
        return new double[] { pbcArea, apcArea, abpArea };        
    }

    public Point approximate(double[] coordinates) {
        return this.vertices[0].multiply(coordinates[0])
            .add(this.vertices[1].multiply(coordinates[1]))
            .add(this.vertices[2].multiply(coordinates[2]));
    }

    public Point approximateNormal(double[] coordinates) {
        return this.vertices[0].getNormal().multiply(coordinates[0])
            .add(this.vertices[1].getNormal().multiply(coordinates[1]))
            .add(this.vertices[2].getNormal().multiply(coordinates[2]));
    }

    public void sortVertices(Pixel[] vertices) {
        Arrays.sort(vertices, new Comparator<Pixel>() {
            @Override
            public int compare(Pixel a, Pixel b) {
                if(a.getY() < b.getY())
                    return -1;
                else if(a.getY() > b.getY())
                    return 1;
                else
                    return 0;
            }
        });

        Arrays.sort(vertices, new Comparator<Pixel>() {
            @Override
            public int compare(Pixel a, Pixel b) {
                if(b.equals(vertices[0]))
                    return 1;
                else if(a.getX() < b.getX())
                    return -1;
                else if(a.getX() > b.getX())
                    return 1;
                else
                    return 0;
            }
        });
    }
}