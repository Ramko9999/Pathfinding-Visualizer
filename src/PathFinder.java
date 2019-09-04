import java.util.ArrayList;

public class PathFinder {

    int [][] board;
    int startRow;
    int startCol;
    int endRow;
    int endCol;


    public PathFinder(int [][] board, int sRow, int sCol, int eRow, int eCol){
        this.board = board;
        startRow = sRow;
        startCol = sCol;
        endRow = eRow;
        endCol = eCol;

    }

    public static void main(String[] args) {
        PathFinder pathFinder =  new PathFinder(new int[12][12], 1, 6, 9, 10);
        pathFinder.A_Star_Search();
    }

    public int[][] copyArray(int [][] array){
        int [][] newArray = new int[array.length][];
        for(int i = 0; i < array.length; i++){
            newArray[i] = array[i].clone();
        }
        return newArray;
    }


    public void initPlaces(){
        board[startRow][startCol] = 10;
        board[endRow][endCol] = 20;
    }

    public void placeWalls(int [][] board){

        board[4][6] = -1;
        board[0][6] = -1;
        board[1][6] = -1;
        board[2][6] = -1;
        board[3][6] = -1;
        board[5][6] = -1;
        board[6][6] = -1;
        board[7][6] = -1;
        board[8][6] = -1;

        board[10][6] = -1;
        board[11][6] = -1;
        board[3][10] = -1;
        board[4][10] = -1;
        board[5][10] = -1;
        board[6][10] = -1;
        board[7][10] = -1;
        board[8][10] = -1;
        board[9][10] = -1;
        board[9][11] = -1;
        board[3][11] = -1;
    }

    public static void displayArray(int[][] a) {
        for (int[] row : a) {
            for (int x : row) {
                if(x == 0){
                    System.out.print(" ");
                }
                System.out.print(x + " ");


            }
            System.out.println();
        }
        System.out.println();

    }

    //gives the path using A* search
    public void A_Star_Search(){

        int [][] board = copyArray(this.board);
        placeWalls(board);
        board[startRow][startCol] = 10;
        board[endRow][endCol] = 20;



        displayArray(board);
        MinList fCostList = new MinList();
        //we will append the starting node to as our first node
        fCostList.append(new Node(new LocNode(startRow, startCol), endRow, endCol, 0)); //append the first node or starting point to our list



        Node fcostHeader = fCostList.head;
        boolean canBeFound = true;

        //condition checks whether we reached end point
        while(!(fcostHeader.element.row == endRow && fcostHeader.element.col == endCol) && canBeFound){

            displayArray(board);

            //gets a list of possible places we can move from our current position
            ArrayList<Node> possibleNodes = generateNewNodes(fcostHeader, startRow, startCol, endRow, endCol, board);


            fCostList.head = fCostList.head.next; //pop out the node

            for(Node n : possibleNodes){
                fCostList.append(n); //appends the places in an order maintained way

            }
            fCostList.print();
            fcostHeader = fCostList.head; //keep going
            fCostList._size--;
            if(fCostList._size == 1){
                fCostList.print();
                System.out.println(fCostList._size);
                canBeFound = false;
            }


        }

        if(!canBeFound){
            System.out.println("Path cannot be found");
        }
        else{
            System.out.println("Shortest Path is as follows: ");
            //if we reached here, we found the end, and we are going to retrace our steps
            while(fcostHeader.element != null){
                System.out.println(fcostHeader.element.row + " , " + fcostHeader.element.col);
                fcostHeader.element = fcostHeader.element.previous;
            }
        }

    }

    public ArrayList<Node> generateNewNodes(Node previousNode, int sRow, int sCol, int eRow, int eCol, int [][] board){
        LocNode location = previousNode.element;
        int row = location.row;
        int col = location.col;
        int [] rowAdditions = {-1,0,1}; //this will gives 8 possible moves around the array
        int [] colAdditions = {-1,0,1};
        ArrayList<Dimension> moveGenerator = new ArrayList<>();
        for(int rowAddition: rowAdditions){
            for(int colAddition: colAdditions){
                if(!(rowAddition == 0 && colAddition == 0)){
                    moveGenerator.add(new Dimension(row + rowAddition, col + colAddition));
                }

            }
        }

        //return an arrayList of Nodes that are possible
        ArrayList<Node> validNodeGenerator = new ArrayList<>();
        for (int i = 0; i < moveGenerator.size(); i++){

            //check whether these moves are all valid
            boolean isNodeValid = true;
            int moveRow = moveGenerator.get(i).row;
            int moveCol = moveGenerator.get(i).col;

            if(moveRow < 0 || moveRow >= board.length){
                isNodeValid = false;
            }
            if(moveCol < 0 || moveCol >= board[0].length){
                isNodeValid = false;
            }


            if(isNodeValid){

                if(!(board[moveRow][moveCol] == 10 || board[moveRow][moveCol] <0 )) {
                    LocNode l = new LocNode(moveRow, moveCol);
                    l.previous = previousNode.element; //attach the previousNode's location in our "move history"
                    board[moveRow][moveCol] = -5;
                    //find gCost and generate new node
                    double gCost = previousNode.gCost + 10 * Math.sqrt(Math.pow(moveRow - row, 2) + Math.pow(moveCol - col, 2));
                    Node newNode = new Node(l, eRow, eCol, gCost);
                    validNodeGenerator.add(newNode);
                }

            }
        }

        return validNodeGenerator;
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

class Dimension{
    int row;
    int col;
    public Dimension(int r, int c){
        row = r;
        col = c;
    }
}
