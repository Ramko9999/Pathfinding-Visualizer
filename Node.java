public class Node {
    LocNode element; //this is a location node
    Node next;
    Node previous;
    double gCost;
    double hCost;
    double cost;

    void calculateCost(int sRow, int sCol, int eRow, int eCol){
        int row = element.row;
        int col= element.col;

        hCost = 10 * Math.sqrt(Math.pow((double)(row - eRow), 2.0) + Math.pow((double)(col - eCol), 2.0)); //find hcost
        cost = hCost + gCost;

    }

    Node(LocNode element, int sRow, int sCol, int eRow, int eCol, double gCost) {
        this.element = element;
        this.next = null;
        this.previous = null;
        this.gCost = gCost;
        System.out.println("GCost : " + gCost);
        calculateCost(sRow, sCol, eRow, eCol);
        //compute the cost
    }
}
