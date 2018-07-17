import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Loader {

    private String objectPath;
    private String scenePath;
    private String cameraPath;

    public Loader(String scenePath, String cameraPath, String objectPath) {
        this.scenePath = scenePath;
        this.cameraPath = cameraPath;
        this.objectPath = objectPath;
    }

    public Illumination loadScene(double[][] worldToView, Point C) {
        try {
            Scanner file = new Scanner(new File(this.scenePath));
            file.useLocale(Locale.US);

            Point coordinates = new Point(file.nextDouble(), file.nextDouble(), file.nextDouble());
            double ka = file.nextDouble();
            Point Ia = new Point(file.nextDouble(), file.nextDouble(), file.nextDouble());
            double kd = file.nextDouble();
            Point Od = new Point(file.nextDouble(), file.nextDouble(), file.nextDouble());
            double ks = file.nextFloat();
            Point Il = new Point(file.nextDouble(), file.nextDouble(), file.nextDouble());
            double n = file.nextDouble();
            file.close();

            System.out.println("Arquivo de cena carregado.");
            return new Illumination(coordinates.subtract(C).multiply(worldToView), ka, Ia, kd, Od, ks, Il, n);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de cena NAO carregado!");
            return null;
        }        
    }

    public Camera loadCamera() {
        try {
            Scanner file = new Scanner(new File(this.cameraPath));
            file.useLocale(Locale.US);

            Point coordinates = new Point(file.nextDouble(), file.nextDouble(), file.nextDouble());
            Point N = new Point(file.nextDouble(), file.nextDouble(), file.nextDouble());
            Point V = new Point(file.nextDouble(), file.nextDouble(), file.nextDouble());
            double d = file.nextDouble();
            double hx = file.nextDouble();
            double hy = file.nextDouble();
            file.close();

            System.out.println("Camera carregada.");
            return new Camera(coordinates, N, V, d, hx, hy);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de camera NAO carregado!");
            return null;
        }        
    }

    public SceneObject loadObject(double[][] worldToView, Point C) {
        try {
            Scanner file = new Scanner(new File(this.objectPath));
            file.useLocale(Locale.US);

            int numPoints = file.nextInt();
            int numTriangles = file.nextInt();

            Point[] points = new Point[numPoints];
            for (int i = 0; i < numPoints; i++) {
                points[i] = new Point(file.nextDouble(), file.nextDouble(), file.nextDouble())
                    .subtract(C).multiply(worldToView);
            }

            Triangle[] triangles = new Triangle[numTriangles];
            for (int i = 0; i < numTriangles; i++) {
                int v0 = file.nextInt() - 1;
                int v1 = file.nextInt() - 1;
                int v2 = file.nextInt() - 1;
                triangles[i] = new Triangle(new Point[] { points[v0], points[v1], points[v2] });
            }       
            file.close();

            System.out.println("Objeto carregado.");
            return new SceneObject(triangles);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de objeto NAO carregado!");
            return null;
        }        
    }
    
}