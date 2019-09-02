import java.util.ArrayList;

public class Runnner {
    public static void main(String[] args) {
        //20 denotes the end
        //10 denotes the start
        //-1 denotes a wall

        //we will use a linkedlist that preserves the order of elements. The head of the list will be the minimum element

        int [][] board = new int [12][12]; //the board we will walk
        int startRow = 1;
        int startCol = 1;
        int endRow = 2;
        int endCol = 9;
        board[startRow][startCol] = 10;
        board[endRow][endCol] = 20;
        board[0][3] = -1;
        board[1][3] = -1;
        board[2][3] = -1;
        board[3][4] = -1;
        board[3][5] = -1;
        board[0][6] = -1;
        board[1][6] = -1;
        board[2][6] = -1;
        board[3][6] = -1;
        displayArray(board);
        MinList fCostList = new MinList();
        //we wil
        fCostList.append(new Node(new LocNode(startRow, startCol), startRow, startCol, endRow, endCol, 0)); //append the first node or starting point to our list



        Node fcostHeader = fCostList.head;

        //condition checks whether we reached end point
        while(!(fcostHeader.element.row == endRow && fcostHeader.element.col == endCol)){

            displayArray(board);

            //gets a list of possible places we can move from our current position
            ArrayList<Node> possibleNodes = generateNewNodes(fcostHeader, startRow, startCol, endRow, endCol, board);
            fCostList.head = fCostList.head.next;

            for(Node n : possibleNodes){

                fCostList.append(n); //appends the places in an order maintained way

            }
            fCostList.print();
            fcostHeader = fCostList.head; //keep going

        }

        System.out.println("Shortest Path is as follows: ");
        //if we reached here, we found the end, and we are going to retrace our steps
        while(fcostHeader.element != null){
            System.out.println(fcostHeader.element.row + " , " + fcostHeader.element.col);
            fcostHeader.element = fcostHeader.element.previous;
        }

    }



    public static void displayArray(int[][] a) {
        for (int[] row : a) {
            for (int x : row) {
                System.out.print(x + " ");
            }
            System.out.println();
        }
        System.out.println();

    }

    public static ArrayList<Node> generateNewNodes(Node previousNode, int sRow, int sCol, int eRow, int eCol, int [][] board){
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
                    Node newNode = new Node(l, sRow, sCol, eRow, eCol, gCost);
                    validNodeGenerator.add(newNode);
                }

            }
        }

        return validNodeGenerator;
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