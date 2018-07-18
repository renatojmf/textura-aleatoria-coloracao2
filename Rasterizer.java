import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

public class Rasterizer {

    private Pixel[] vertices;

    public Rasterizer(Pixel[] vertices) {
        this.vertices = vertices;
    }

    public Vector<Pixel> rasterize() {

        Vector<Pixel> pixels = new Vector<Pixel>();
        sortVertices();
        Vector<Edge>[] edgeTable = initEdgeTable(vertices);

        /* Scanline Conversion */
        Vector<Edge> activeEdgeTable = new Vector<Edge>();
        int range = vertices[2].getY() - vertices[0].getY();
        for (int i = 0; i <= range; i++) {
            
            this.update(activeEdgeTable, i + vertices[0].getY());
            this.append(activeEdgeTable, edgeTable[i], i);
            this.sortAET(activeEdgeTable);
            pixels.addAll(this.retrievePixels(activeEdgeTable, i + vertices[0].getY()));
            
        }

        return pixels;
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

    public void append(Vector<Edge> AET, Vector<Edge> ET, int i) {
        if(ET != null) 
            AET.addAll(ET);
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
            int xMin = AET.firstElement().getX();
            int xMax = AET.lastElement().getX();
            //System.out.println(xMin + " " + xMax);

            for (int i = xMin; i <= xMax; i++) {
                pixels.add(new Pixel(i, scanline));
            }
         
        }
        return pixels;
    } 


    public void sortVertices() {
        Arrays.sort(this.vertices, new Comparator<Pixel>() {
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
    }

    public Vector<Edge>[] initEdgeTable(Pixel[] vertices) {
        Vector<Edge>[] ET = new Vector[vertices[2].getY() - vertices[0].getY() + 1];

        Edge v1v0 = new Edge(vertices[0].getX(), vertices[1].getY(), 
            (double) (vertices[1].getX() - vertices[0].getX()) / (vertices[1].getY() - vertices[0].getY()));
        Edge v2v1 = new Edge(vertices[1].getX(), vertices[2].getY(), 
            (double) (vertices[2].getX() - vertices[1].getX()) / (vertices[2].getY() - vertices[1].getY()));
        Edge v2v0 = new Edge(vertices[0].getX(), vertices[2].getY(), 
            (double) (vertices[2].getX() - vertices[0].getX()) / (vertices[2].getY() - vertices[0].getY()));
        
        int range = vertices[0].getY();
        ET[vertices[0].getY() - range] = new Vector<Edge>();
        ET[vertices[0].getY() - range].add(v1v0);
        ET[vertices[0].getY() - range].add(v2v0);
        ET[vertices[1].getY() - range] = new Vector<Edge>();
        ET[vertices[1].getY() - range].add(v2v1);
        return ET;
    }
}