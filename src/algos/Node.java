package algos;

public class Node implements Comparable<Node> {

    private Node ancestor;
    private int row;
    private int col;
    private double cost;


    Node(int row, int col, double cost) {
        this.ancestor = null;
        this.row = row;
        this.col = col;
        this.cost = cost;
    }

    public int compareTo(Node b) {
        if (this.cost > b.cost) {
            return 1;
        } else if (this.cost == b.cost) {
            return 0;
        } else {
            return -1;
        }
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public double getCost() {
        return this.cost;
    }

    public Node getAncestor() {
        return this.ancestor;
    }

    public void setAncestor(Node ancestor) {
        this.ancestor = ancestor;
    }

    public boolean isAtPosition(int r, int c) {
        return this.row == r && this.col == c;
    }

}