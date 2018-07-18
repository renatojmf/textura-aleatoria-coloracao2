/* Cada aresta armazena:
- a componente x do vértice MAIS BAIXO;
- a componente y do vértice MAIS ALTO;
- o coeficiente angular já invertido da aresta.
*/

public class Edge {

    private int x;
    private int y;
    private double dx;
    
    public Edge(int x, int y, double dx) {
        this.x = x;
        this.y = y;
        this.dx = dx;
    }

    public double getDx() {
        return this.dx;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void increaseX() {
        if(this.dx > 0)
            this.x += (int) Math.ceil(this.dx);
        else
            this.x += (int) Math.floor(this.dx); 
    }

    public void printEdge() {
        System.out.println("x: " + this.x + " y: " + this.y + " dx: " + this.dx);
    }
    
}