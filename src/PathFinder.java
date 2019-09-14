
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

//a class that contains different algorithms to search a grid to find the optimal path

/*

20 represents the end point
10 represents the start point
-5 represents nodes or locations that have been calculated
1 represents nodes or locations that are being examined
-1 represents a wall or a barrier

 */
public class PathFinder {

    //used as maze with starting point and ending point
    private int [][] board;
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;

    //used for visualization purposes
    LinkedList<int[][]> history;
    LinkedList<int [][]> calculationTimeLapse;

    private int [][] calculationMoment;


    public PathFinder(int [][] board, int sRow, int sCol, int eRow, int eCol){
        this.board = board;
        this.startRow = sRow;
        this.startCol = sCol;
        this.endRow = eRow;
        this.endCol = eCol;
        this.calculationMoment = new int[this.board.length][this.board[0].length];

    }

    public static void main(String[] args) {
        PathFinder pathFinder =  new PathFinder(new int[12][12], 7, 0, 9, 10);
        pathFinder.A_Star_Search();
        System.out.println("----------------");
        pathFinder.BFS();

    }

    //used to create a copy of an array
    public int[][] copyArray(int [][] array){
        int [][] newArray = new int[array.length][];
        for(int i = 0; i < array.length; i++){
            newArray[i] = array[i].clone();
        }
        return newArray;
    }

    //gives the path using A* search
    public LocNode A_Star_Search(){

        // creating visualization data structures and maze
        history = new LinkedList<>();
        calculationTimeLapse = new LinkedList<>();
        int [][] board = copyArray(this.board);
        board[startRow][startCol] = 10;
        board[endRow][endCol] = 20;

        //starting the queue
        MinList fCostList = new MinList();

        //append the starting node to as our first node
        fCostList.append(new Node(new LocNode(startRow, startCol, 0), endRow, endCol, 0)); //append the first node or starting point to our list

        //variables determining the state of the a star algo
        Node fcostHeader = fCostList.head;
        boolean canBeFound = true;

        //condition checks whether we reached end point
        while(canBeFound && !(fcostHeader.element.row == endRow && fcostHeader.element.col == endCol)){

            //append to visualization ds
            history.addFirst(copyArray(board));
            calculationTimeLapse.addFirst(copyArray(calculationMoment));

            //gets a list of possible places we can move from our current position
            ArrayList<Node> possibleNodes = generateNewNodesForAStar(fcostHeader,  endRow, endCol, board);

            //pop out the node
            fCostList.head = fCostList.head.next;

            //appends the places in an order maintained way
            for(Node n : possibleNodes){
                fCostList.append(n);

            }

            //pop out node and keep calculating
            fcostHeader = fCostList.head;

            //check whether node is empty, as that signals that algo couldn't find path
            if(fcostHeader == null){
                canBeFound = false;
            }


        }

        if(!canBeFound){
            return null;
        }
        else{

            //if we reached here, we found the end, and we are going to retrace our steps
           return fcostHeader.element;
        }


    }

    //generate new nodes for A* search
    private ArrayList<Node> generateNewNodesForAStar(Node previousNode, int eRow, int eCol, int [][] board){

        //init location and row, col
        LocNode location = previousNode.element;
        int row = location.row;
        int col = location.col;

        //this will gives 8 possible moves around the array
        int [] rowAdditions = {-1,0,1};
        int [] colAdditions = {-1,0,1};

        ArrayList<Dimension> moveGenerator = new ArrayList<>();
        for(int rowAddition: rowAdditions){
            for(int colAddition: colAdditions){

                //if move is not itself and not diagonal, add it
                if(!(rowAddition == colAddition) && !(rowAddition + colAddition == 0)){
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

            //checks whether moves are even on the board
            if(moveRow < 0 || moveRow >= board.length){
                isNodeValid = false;
            }
            if(moveCol < 0 || moveCol >= board[0].length){
                isNodeValid = false;
            }


            if(isNodeValid){

                //checks whether the node is a new node
                if(!(board[moveRow][moveCol] == 10 || board[moveRow][moveCol] <0 || board[moveRow][moveCol] == 1)) {

                    //creates new location node and preserves history through LocNode linkedList
                    LocNode l = new LocNode(moveRow, moveCol, previousNode.element.size);

                    //attach the previousNode's location in our "move history"
                    l.previous = previousNode.element;

                    board[row][col] = -5; // "pop" out the node we created calculations in
                    board[moveRow][moveCol] = 1; //identify that we are looking at the nodes

                    //find gCost and generate new node
                    double gCost = previousNode.gCost + 1 * Math.sqrt(Math.pow(moveRow - row, 2) + Math.pow(moveCol - col, 2));
                    Node newNode = new Node(l, eRow, eCol, gCost);

                    //update calculations
                    calculationMoment[moveRow][moveCol] = (int) newNode.cost;

                    //add node to array list
                    validNodeGenerator.add(newNode);
                }

            }
        }

        return validNodeGenerator;
    }

    //gives the path using Depth First Search: Doesn't find shortest path
    public void DFS(){

        //init maze and end
        int [][] board = copyArray(this.board);
        board[startRow][startCol] = 10;
        board[endRow][endCol] = 20;

        //create a stack for pushing and popping new nodes
        Stack<LocNode> nodeStack = new Stack();

        //init locations
        LocNode startingLocation = new LocNode(startRow, startCol, 0);
        LocNode finalNode = null;
        nodeStack.push(startingLocation);
        boolean isNodeStackEmpty = false;
        boolean foundEndPoint = false;

        //until our locNode stack gives us the location
        while(!nodeStack.empty()){

            //checks whether an end point is found
            if(foundEndPoint){

                //checks whether there is a previous end point
                if(finalNode == null){

                    finalNode = nodeStack.peek();
                }

                //checks whether new end point history is less than old end point history
                else if(finalNode.size > nodeStack.peek().size){

                    finalNode = nodeStack.peek();
                }


                foundEndPoint = false;
                nodeStack.pop();

            }
            else{

                //generates possible LocNodes from latest node in the stack
                LocNode previousLocationNode = nodeStack.peek();
                ArrayList<LocNode> locations = generateNewNodesForTreeSearch(previousLocationNode,  board, "DFS");

                //removes node
                nodeStack.pop();
                LocNode targetNode = null;

                //appends and checks whether a node is a target node
                for(LocNode locationNode: locations){

                    if(!nodeStack.empty() && (nodeStack.peek().row ==  endRow && nodeStack.peek().col ==  endCol)){

                        foundEndPoint = true;
                        targetNode = nodeStack.peek();
                    }

                    else{
                        nodeStack.push(locationNode);
                    }

                }

                //if the final destination is in the loc node list, then it needs to be at the end
                if(targetNode != null){
                    nodeStack.push(targetNode);
                }
            }


        }

        if(isNodeStackEmpty){

            System.out.println("Path can't be found with DFS");
        }
        else{
            //print path

            LocNode current = finalNode; //nodeStack.peek();
            int counter = 0;
            System.out.println("DFS Predicts the best path is as follows: ");

            while( current != null){
                System.out.println(current.row + " , " + current.col);
                board[current.row][current.col] = 4;
                counter++;
                current = current.previous;
            }

            System.out.println("# of Moves : "   + counter);

        }


    }

    //gives the path using Breadth First Search WORKING!

    public LocNode BFS(){
        //init grid and locations
        history = new LinkedList<>();
        int [][] board = copyArray(this.board);
        board[startRow][startCol] = 10;
        board[endRow][endCol] = 20;

        //create LocNode Queue where enqueueing and dequeuing can occur
        LinkedList <LocNode> nodeQueue = new LinkedList<>();
        nodeQueue.addFirst(new LocNode(startRow, startCol, 0));

        //checks whether a destination is reached
        while(!(nodeQueue.getLast().row == endRow && nodeQueue.getLast().col == endCol)){

            //visualization DS
            history.addFirst(copyArray(board));
            LocNode endNode = nodeQueue.getLast();

            //generates new nodes from tree search
            ArrayList<LocNode> possibleLocNodes = generateNewNodesForTreeSearch(endNode, board, "BFS");

            //dequeue the final node as we have searched possible nodes from it
            nodeQueue.removeLast();

            //adds possible nodes
            for(LocNode node: possibleLocNodes){
                nodeQueue.addFirst(node);
            }

            //if Queue is empty, final node cannot be found
            if(nodeQueue.isEmpty()){
                break;
            }
        }

        if(nodeQueue.isEmpty()){
            return null;
        }
        else{
            LocNode endNode = nodeQueue.getLast();
           return endNode;

        }

    }

    //gets potential nodes for tree based searching algorithms
    private ArrayList<LocNode> generateNewNodesForTreeSearch(LocNode previousLocation,  int [][] board, String whichSearch){

        boolean dontConsiderOnes = false; //important for BFS

        if(whichSearch.equals("BFS")){
            dontConsiderOnes = true;
        }

        //get location
        int row = previousLocation.row;
        int col = previousLocation.col;

        //this will gives 8 possible moves around the array
        int [] rowAdditions = {-1,0,1};
        int [] colAdditions = {-1,0,1};

        //creates an ArrayList of moves
        ArrayList<Dimension> moveGenerator = new ArrayList<>();
        for(int rowAddition: rowAdditions){
            for(int colAddition: colAdditions){


                if(!(rowAddition == colAddition) && !(rowAddition + colAddition == 0)){
                    moveGenerator.add(new Dimension(row + rowAddition, col + colAddition));
                }

            }
        }
        ArrayList<LocNode> validLocationGenerator = new ArrayList<>();
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

                if(!(board[moveRow][moveCol] == 10 || board[moveRow][moveCol] <0)) {

                    // do not append things that are already in the queue
                    if(!(dontConsiderOnes && board[moveRow][moveCol] == 1)){

                        //create new LocNode with move history
                        LocNode l = new LocNode(moveRow, moveCol, previousLocation.size + 1);
                        l.previous = previousLocation; //attach the previousNode's location in our "move history"

                        if(row == startRow && col == startCol){ //to indicate the start node
                            board[row][col] = -6;
                        }
                        else{
                            board[row][col] = -5;
                        }

                        board[moveRow][moveCol] = 1; //the node is being examined
                        validLocationGenerator.add(l);
                    }

                }

            }
        }
        return validLocationGenerator;
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

    //appends a new Node in an order maintained manner
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

            //obtain pointers of current node and previous Node
            Node current = head;
            Node lastNode = head.previous;
            boolean isPlaceFound = false;

            //start traversing
            while (current != null && !isPlaceFound) {

                //if we reached a point where we can place nextNode
                if (nextNode.cost < current.cost) {

                    //get pointers of nodes that are directly left and to the right
                    Node rightNode = current.previous.next;
                    Node leftNode = current.previous;

                    //start changing pointers
                    current.previous.next = nextNode;
                    nextNode.next = rightNode;
                    nextNode.previous = leftNode;
                    rightNode.previous = nextNode;

                    //place is found
                    isPlaceFound = true;
                }

                else {

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

        //compute cost
        hCost = 1 * Math.sqrt(Math.pow((double)(row - eRow), 2.0) + Math.pow((double)(col - eCol), 2.0)); //find hcost
        cost = hCost + gCost;

    }

    Node(LocNode element, int eRow, int eCol, double gCost) {
        this.element = element;
        this.next = null;
        this.previous = null;
        this.gCost = gCost;
        calculateCost(eRow, eCol);
        //compute the cost
    }
}

//used to track back history
class LocNode{
    int row;
    int col;
    int size;
    LocNode previous; //this will used to retrace our steps

    public LocNode(int r, int c, int s){
        row = r;
        col = c;
        size = s;
    }

    @Override
    public String toString(){
        return "( " + col + " , " + row + " )";
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

