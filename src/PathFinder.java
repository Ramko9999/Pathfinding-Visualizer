
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
        PathFinder pathFinder =  new PathFinder(new int[12][12], 7, 0, 9, 10);
        pathFinder.A_Star_Search();
        pathFinder.BFS();

    }

    public int[][] copyArray(int [][] array){
        int [][] newArray = new int[array.length][];
        for(int i = 0; i < array.length; i++){
            newArray[i] = array[i].clone();
        }
        return newArray;
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


        board[9][2] = -1;
        board[8][2] = -1;
        board[6][2] = -1;
        board[7][2] = -1;

        board[5][2] = -1;




    }

    public static void displayArray(int[][] a) {
        for (int[] row : a) {
            for (int x : row) {
                if(x < 10 && x >= 0){
                    System.out.print(" ");
                }
                System.out.print(x + " ");


            }
            System.out.println();
        }
        System.out.println();

    }

    //gives the path using A* search WORKING!
    public void A_Star_Search(){

        int [][] board = copyArray(this.board);
        placeWalls(board);
        board[startRow][startCol] = 10;
        board[endRow][endCol] = 20;



        displayArray(board);
        MinList fCostList = new MinList();
        //we will append the starting node to as our first node
        fCostList.append(new Node(new LocNode(startRow, startCol, 0), endRow, endCol, 0)); //append the first node or starting point to our list



        Node fcostHeader = fCostList.head;
        boolean canBeFound = true;

        //condition checks whether we reached end point
        while(!(fcostHeader.element.row == endRow && fcostHeader.element.col == endCol) && canBeFound){

            //displayArray(board);
            //gets a list of possible places we can move from our current position
            ArrayList<Node> possibleNodes = generateNewNodesForAStar(fcostHeader,  endRow, endCol, board);
            fCostList.head = fCostList.head.next; //pop out the node
            for(Node n : possibleNodes){
                fCostList.append(n); //appends the places in an order maintained way

            }
            fcostHeader = fCostList.head; //keep going
            fCostList._size--;
            if(fCostList._size == 1){
                canBeFound = false;
            }


        }

        if(!canBeFound){
            System.out.println("A* says path cannot be found");
        }
        else{
            int counter = 0;

            System.out.println("A* predicts the best path is as follows: ");
            //if we reached here, we found the end, and we are going to retrace our steps
            while(fcostHeader.element != null){

                System.out.println(fcostHeader.element.row + " , " + fcostHeader.element.col);
                board[fcostHeader.element.row][fcostHeader.element.col] = 4;
                counter++;
                fcostHeader.element = fcostHeader.element.previous;
            }
            displayArray(board);
            System.out.println("# of Moves : "   + counter);
        }

    }

    public ArrayList<Node> generateNewNodesForAStar(Node previousNode, int eRow, int eCol, int [][] board){
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

                if(!(board[moveRow][moveCol] == 10 || board[moveRow][moveCol] <0 || board[moveRow][moveCol] == 1)) {
                    LocNode l = new LocNode(moveRow, moveCol, previousNode.element.size);
                    l.previous = previousNode.element; //attach the previousNode's location in our "move history"
                    board[row][col] = -5; // "pop" out the node we created calculations in
                    board[moveRow][moveCol] = 1; //identify that we are looking at the nodes

                    //find gCost and generate new node
                    double gCost = previousNode.gCost + 10 * Math.sqrt(Math.pow(moveRow - row, 2) + Math.pow(moveCol - col, 2));
                    Node newNode = new Node(l, eRow, eCol, gCost);
                    validNodeGenerator.add(newNode);
                }

            }
        }

        return validNodeGenerator;
    }

    //gives the path using Depth First Search Still Implementing new Ideas
    // maybe better to use recursion
    public void DFS(){

        int [][] board = copyArray(this.board);
        placeWalls(board);
        board[startRow][startCol] = 10;
        board[endRow][endCol] = 20;
        Stack<LocNode> nodeStack = new Stack();
        LocNode startingLocation = new LocNode(startRow, startCol, 0);
        LocNode finalNode = null;
        nodeStack.push(startingLocation);
        boolean isNodeStackEmpty = false;
        boolean foundEndPoint = false;
        //until our locNode stack gives us the location
        while(/*!(((LocNode)nodeStack.peek()).row ==  endRow && ((LocNode)nodeStack.peek()).col ==  endCol) && */  !nodeStack.empty()){

            if(foundEndPoint){
                if(finalNode == null){
                    System.out.println("Changing Node from Null");
                    finalNode = nodeStack.peek();
                }
                else if(finalNode.size > nodeStack.peek().size){
                    System.out.println("Changed Node");
                    System.out.println("Optimal Size : " + nodeStack.peek().size);
                    finalNode = nodeStack.peek();
                }
                foundEndPoint = false;
                nodeStack.pop();

            }
            else{
                LocNode previousLocationNode = nodeStack.peek();
                ArrayList<LocNode> locations = generateNewNodesForTreeSearch(previousLocationNode,  board, "DFS");
                nodeStack.pop();
                LocNode targetNode = null;
                for(LocNode locationNode: locations){
                    if(!nodeStack.empty() && (nodeStack.peek().row ==  endRow && nodeStack.peek().col ==  endCol)){
                        System.out.println("TargetNode found, size :  " + nodeStack.peek().size);

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
            displayArray(board);
        }


    }

    //gives the path using Breadth First Search WORKING!
    public void BFS(){
        int [][] board = copyArray(this.board);
        placeWalls(board);
        board[startRow][startCol] = 10;
        board[endRow][endCol] = 20;
        LinkedList <LocNode> nodeQueue = new LinkedList<>();
        nodeQueue.addFirst(new LocNode(startRow, startCol, 0));

        while(!(nodeQueue.getLast().row == endRow && nodeQueue.getLast().col == endCol)){
            //displayArray(board);
            //System.out.println("--------------------");
            //System.out.println(nodeQueue);
            LocNode endNode = nodeQueue.getLast();
            ArrayList<LocNode> possibleLocNodes = generateNewNodesForTreeSearch(endNode, board, "BFS");
            //dequeue the final node as we have searched possible nodes from it
            nodeQueue.removeLast();

            for(LocNode node: possibleLocNodes){
                nodeQueue.addFirst(node);
            }
            if(nodeQueue.isEmpty()){
                break;
            }
        }

        if(nodeQueue.isEmpty()){
            System.out.println("BFS couldn't find a path");
        }
        else{
            LocNode endNode = nodeQueue.getLast();
            System.out.println("BFS predicts the path is: ");
            int counter = 0;
            while(endNode != null){
                System.out.println(endNode.row + " , " + endNode.col);
                board[endNode.row][endNode.col] = 4;
                endNode = endNode.previous;
                counter++;
            }
            displayArray(board);
            System.out.println("# of Moves: " + counter);
        }

    }

    //gets potential nodes for tree based searching algorithms
    public ArrayList<LocNode> generateNewNodesForTreeSearch(LocNode previousLocation,  int [][] board, String whichSearch){
        boolean dontConsiderOnes = false;
        if(whichSearch.equals("BFS")){
            dontConsiderOnes = true;
        }
        int row = previousLocation.row;
        int col = previousLocation.col;


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

                if(!(board[moveRow][moveCol] == 10 || board[moveRow][moveCol] <0 )) {
                    //dont append things that are already in the queue, as it is a waste of time and space
                    if(!(dontConsiderOnes && board[moveRow][moveCol] == 1)){
                        LocNode l = new LocNode(moveRow, moveCol, previousLocation.size + 1);
                        l.previous = previousLocation; //attach the previousNode's location in our "move history"
                        board[row][col] = -5;
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

