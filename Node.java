public class Node {
    LocNode element; //this is a location node
    Node next;
    Node previous;
    double gCost; //cost calculated from the starting point
    double hCost; //cost calculated from end point
    double cost; //total cost

    void calculateCost(int eRow, int eCol){
        int row = element.row;
        int col= element.col;

        hCost = 10 * Math.sqrt(Math.pow((double)(row - eRow), 2.0) + Math.pow((double)(col - eCol), 2.0)); //find hcost
        cost = hCost + gCost;

    }

    Node(LocNode element, int eRow, int eCol, double gCost) {
        this.element = element;
        this.next = null;
        this.previous = null;
        this.gCost = gCost;
        System.out.println("GCost : " + gCost);
        calculateCost(eRow, eCol);
        //compute the cost
    }
}
