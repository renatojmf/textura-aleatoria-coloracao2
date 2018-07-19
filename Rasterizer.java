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
            this.append(activeEdgeTable, edgeTable[i], pixels);
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

    public void append(Vector<Edge> AET, Vector<Edge> ET, Vector<Pixel> pixels) {
        if(ET != null) {

            // Tratando arestas horizontais.
            for (int i = 0; i < ET.size(); i++) 
                if(ET.get(i).getDx() == Double.POSITIVE_INFINITY) {
                    Vector<Edge> horizontal = new Vector<Edge>();
                    horizontal.add(ET.get(i));
                    pixels.addAll(this.retrievePixels(horizontal, ET.get(i).getY()));
                    ET.remove(i);
                    i--;
                }
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
        if(v1.getY() == v0.getY())
            ET[v1.getY() - range].add(v2v1);
        else {
            ET[v1.getY() - range] = new Vector<Edge>();
            ET[v1.getY() - range].add(v2v1);
        }
        return ET;
    }
}