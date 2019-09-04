public class PathFinder {

    int [][] board;
    int startRow;
    int startCol;
    int endRow;
    int endCol;


    public PathFinder(int [][] board, int sRow, int sCol, int eRow, int eCol){
        
    }

}


//used as a way to preserve order in A* search
class MinList {

    Node head; //this should be the maximum node
    int _size;

    public MinList() {
        head = null;
        _size = 0;
    }


    int getSize() {
        return _size;
    }

    void print(){
        Node current = head;
        while(current != null){
            System.out.print(current.cost + " Location: " + current.element.row + " , " + current.element.col);
            System.out.print(" -> ");
            current = current.next;
        }
        System.out.println("----------------------------");
    }

    void append(Node nextNode) {
        //if our MaxList is empty
        if (head == null) {
            head = nextNode;
        }
        //if nextNode is smaller than our head
        else if (head.cost >= nextNode.cost) {
            head.previous = nextNode;
            nextNode.next = head;
            head = head.previous;
        }
        //traverse till we find a place to put our nextNode
        else {
            Node current = head;
            Node lastNode = head.previous;
            boolean isPlaceFound = false;

            while (current != null && !isPlaceFound) {

                //if we reached a point where we can place nextNode
                if (nextNode.cost < current.cost) {
                    //create refrence of nodes that are directly left and to the right
                    Node rightNode = current.previous.next;
                    Node leftNode = current.previous;
                    //start changing pointers
                    current.previous.next = nextNode;
                    nextNode.next = rightNode;
                    nextNode.previous = leftNode;
                    rightNode.previous = nextNode;

                    isPlaceFound = true;
                } else {
                    //iteration
                    lastNode = current;
                    current = current.next;
                }
            }
            //if nextNode was the maximum
            if (!isPlaceFound) {
                lastNode.next = nextNode;
                nextNode.previous = lastNode;
            }
        }


        _size++;
    }

}



//used in MinList
class Node {
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

//used to track back history
class LocNode{
    int row;
    int col;
    LocNode previous; //this will used to retrace our steps

    public LocNode(int r, int c){
        row = r;
        col = c;
    }
}
