
public class Camera {

    private Point coordinates;
    private Point N;
    private Point V;
    private Point U;
    private double d;
    private double hx;
    private double hy;

    public Camera(Point coordinates, Point N, Point V, double d, double hx, double hy) {
        this.coordinates = coordinates;
        this.N = N;
        this.V = V;
        this.d = d;
        this.hx = hx;
        this.hy = hy;
        this.U = this.UVector();
    }

    public Point UVector() {
        this.V = V.orthogonalize(N).normalize();
        this.N = N.normalize();

        return N.vectorProduct(V);
    }

    public double[][] worldToView() {
        double[][] worldToView = new double[3][3];

        worldToView[0][0] = this.U.getX();
        worldToView[0][1] = this.U.getY();
        worldToView[0][2] = this.U.getZ();

        worldToView[1][0] = this.V.getX();
        worldToView[1][1] = this.V.getY();
        worldToView[1][2] = this.V.getZ();

        worldToView[2][0] = this.N.getX();
        worldToView[2][1] = this.N.getY();
        worldToView[2][2] = this.N.getZ();

        return worldToView;
    }

    public Point getCoordinates() {
        return coordinates;
    }


}