/* Cada aresta armazena:
- a componente x do vértice MAIS BAIXO;
- a componente y do vértice MAIS ALTO;
- o coeficiente angular já invertido da aresta.
*/

public class Edge {

    private double x;
    private double y;
    private double dx;
    
    public Edge(double x, double y, double dx) {
        this.x = x;
        this.y = y;
        this.dx = dx;
    }

    public double getDx() {
        return this.dx;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void increaseX() {
        this.x = x + this.dx;
    }

    public void decreaseY() {
        this.y -= 1;
    }

    public void printEdge() {
        System.out.println("x: " + this.x + " y: " + this.y + " dx: " + this.dx);
    }
    
}