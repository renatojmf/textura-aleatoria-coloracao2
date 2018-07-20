import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rasterizer {

    private Pixel[] vertices;
    private Vector<Pixel> pixels;

    public Rasterizer(Pixel[] vertices) {
        this.vertices = vertices;
        this.pixels = new Vector<Pixel>();
    }

    public Vector<Pixel> rasterize(GraphicsContext ctx) {

        sortVertices();
        // vertices[0].printPoint();
        // vertices[1].printPoint();
        // vertices[2].printPoint();
        // System.out.println();

        // O triângulo possui uma base reta.
        if(vertices[1].getY() == vertices[2].getY())
            if(vertices[1].getX() < vertices[2].getX())
                topDownScanline(vertices[0], vertices[1], vertices[2], ctx);
            else
                topDownScanline(vertices[0], vertices[2], vertices[1], ctx);
        
        // O triângulo possui uma base reta, mas invertida.
        else if(vertices[0].getY() == vertices[1].getY())
            if(vertices[0].getX() < vertices[1].getX())
                bottomUpScanline(vertices[0], vertices[1], vertices[2], ctx);
            else
                bottomUpScanline(vertices[1], vertices[0], vertices[2], ctx);

        // O triângulo possui um vértice intermediário.
        else {

            // Semelhança de triângulos.
            int x = (int) Math.round(vertices[0].getX() + ((double) (vertices[1].getY() - vertices[0].getY()) / (double) (vertices[2].getY() - vertices[0].getY())) * (vertices[2].getX() - vertices[0].getX()));
            int y = vertices[1].getY();

            Pixel verticeIntersection = new Pixel(x, y);
            if(x < vertices[1].getX()) {
                topDownScanline(vertices[0], verticeIntersection, vertices[1],  ctx);
                bottomUpScanline(verticeIntersection, vertices[1], vertices[2], ctx);
            } else {
                topDownScanline(vertices[0], vertices[1], verticeIntersection, ctx);
                bottomUpScanline(vertices[1], verticeIntersection, vertices[2], ctx);    
            }
        }

        return pixels;
    }

    public void topDownScanline(Pixel v1, Pixel v2, Pixel v3, GraphicsContext ctx) {

        double v2v1 = (double) (v2.getX() - v1.getX()) / (double) (v2.getY() - v1.getY());
        double v3v1 = (double) (v3.getX() - v1.getX()) / (double) (v3.getY() - v1.getY());

        double xMin = v1.getX();
        double xMax = v1.getX();

        for (int i = v1.getY(); i <= v2.getY(); i++) {
            for (int j = (int) Math.round(xMin); j < (int) Math.round(xMax); j++) {
                pixels.add(new Pixel(j, i));
            }

            /* Geometric Coherence */
            xMin = xMin + v2v1;
            xMax = xMax + v3v1;
        }
    }

    public void bottomUpScanline(Pixel v1, Pixel v2, Pixel v3, GraphicsContext ctx) {

     //   System.out.println("BOTTOMUP");
        // v1.printPoint();
        // v2.printPoint();
        // v3.printPoint();

        double v2v3 = (double) (v3.getX() - v2.getX()) / (double) (v3.getY() - v2.getY());
        double v3v1 = (double) (v3.getX() - v1.getX()) / (double) (v3.getY() - v1.getY());

        double xMin = v3.getX();
        double xMax = v3.getX();

        for (int i = v3.getY(); i > v1.getY(); i--) {
            for (int j = (int) Math.round(xMin); j < (int) Math.round(xMax); j++) {
                pixels.add(new Pixel(j, i));
        //        pixels.lastElement().printPoint();
            }

            /* Geometric Coherence */
            xMin = xMin - v3v1;
            xMax = xMax - v2v3;
        }
    }

    public void update(Vector<Edge> AET, int scanline) {
 
        for (int i = 0; i < AET.size(); i++) {

            /* Geometric Coherence: */
            AET.get(i).increaseX();
            
            if(AET.get(i).getY() == scanline) {
               AET.remove(i);
               i--;
            }
        }
    }

    public void append(Vector<Edge> AET, Vector<Edge> ET) {
        if(ET != null) {
            AET.addAll(ET);
        }
    }

    public void sortAET(Vector<Edge> AET) {
        AET.sort(new Comparator<Edge>() {
            @Override
            public int compare(Edge a, Edge b) {
                if(a.getX() < b.getX())
                    return -1;
                else if(a.getX() > b.getX())
                    return 1;
                else
                    return 0;
            }
        });
    }

    public Vector<Pixel> retrievePixels(Vector<Edge> AET, int scanline) {

        Vector<Pixel> pixels = new Vector<Pixel>();
        if(!AET.isEmpty()) {
            int xMin = (int) Math.floor(AET.firstElement().getX());
            int xMax = (int) Math.floor(AET.lastElement().getX());
            //System.out.println(xMin + " " + xMax);

            for (int i = xMin; i < xMax; i++) {
                pixels.add(new Pixel(i, scanline));
            }
         
        }
        return pixels;
    } 


    public void sortVertices() {
        Arrays.sort(this.vertices, new Comparator<Pixel>() {
            @Override
            public int compare(Pixel a, Pixel b) {
                if(a.getY() <= b.getY())
                    return -1;
                else if(a.getY() > b.getY())
                    return 1;
                else
                    return 0;
            }
        });
    }

    public Vector<Edge>[] initEdgeTable(Pixel[] vertices) {
        Pixel v0 = vertices[0];
        Pixel v1 = vertices[1];
        Pixel v2 = vertices[2];

        Vector<Edge>[] ET = new Vector[v2.getY() - v0.getY() + 1];
 
        Edge v1v0, v2v1, v2v0;
        if(v1.getY() == v0.getY()) // Se a aresta for horizontal;
            v1v0 = new Edge(v0.getX(), v1.getY(), Double.POSITIVE_INFINITY);
        else
            v1v0 = new Edge(v0.getX(), v1.getY(), (double) (v1.getX() - v0.getX()) / (v1.getY() - v0.getY()));

        if(v2.getY() == v1.getY()) // Se a aresta for horizontal;
            v2v1 = new Edge(v1.getX(), v2.getY(), Double.POSITIVE_INFINITY);
        else
            v2v1 = new Edge(v1.getX(), v2.getY(), (double) (v2.getX() - v1.getX()) / (v2.getY() - v1.getY()));

        if(v2.getY() == v0.getY()) // Se a aresta for horizontal;
            v2v0 = new Edge(v0.getX(), v2.getY(), Double.POSITIVE_INFINITY);
        else
            v2v0 = new Edge(v0.getX(), v2.getY(), (double) (v2.getX() - v0.getX()) / (v2.getY() - v0.getY()));
        
        int range = v0.getY();
        ET[v0.getY() - range] = new Vector<Edge>();
        ET[v0.getY() - range].add(v1v0);
        ET[v0.getY() - range].add(v2v0);
        if(v1.getY() != v0.getY()) {
            ET[v1.getY() - range] = new Vector<Edge>();
            ET[v1.getY() - range].add(v2v1);
            v1v0.decreaseY();
        }
        return ET;
    }
}