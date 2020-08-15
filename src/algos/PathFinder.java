package algos;
import java.util.LinkedList;
import java.util.PriorityQueue;



public class PathFinder {

    //used as maze with starting point and ending point
    private int[][] board;
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;



    public final static int END = 20;
    public final static int START = 10;
    public final static int MARKED = -5;
    public final static int UNMARKED = 0;
    public final static int CONSIDERED = 1;
    public final static int START_MARKER = 9;
    public final static int END_MARKER = 19;
    public final static int WALL = -1;
    public final static int DEFAULT = 0;

    public final static String STAR = "A*";
    public final static String BFS = "BFS";
    public final static String BIDIRECTIONAL = "Bidirectional BFS";

    public PathFinder(int[][] board, int sRow, int sCol, int eRow, int eCol) {
        this.board = board;
        this.startRow = sRow;
        this.startCol = sCol;
        this.endRow = eRow;
        this.endCol = eCol;
        board[startRow][startCol] = PathFinder.START;
        board[endRow][endCol] = PathFinder.END;
    }

    //used to create a copy of an array
    public int[][] copyArray(int[][] array) {
        int[][] newArray = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i].clone();
        }
        return newArray;
    }


    private boolean isPositionInGrid(int x, int y, int m, int n){
        return (x > -1 && x < m) && (y > -1 && y < n);
    }


    public Path getShortestPathWithStar() {

        PriorityQueue<Node> minHeap = new PriorityQueue<>();

        LinkedList<int[][]> history = new LinkedList<>();
        LinkedList<int[][]> calculations = new LinkedList<>();

        int[][] board = copyArray(this.board);
        int boardRowSize = board.length;
        int boardColSize = board[0].length;

        int [][] calculationMoment = new int[boardRowSize][boardColSize];

        minHeap.add(new Node(startRow, startCol, 0));

        while (minHeap.size() > 0) {

            Node node = minHeap.remove();

            int row = node.getRow();
            int col = node.getCol();

            if(board[row][col] != PathFinder.MARKED) {

                if(!node.isAtPosition(startRow, startCol)){
                    board[row][col] = PathFinder.MARKED;
                }

                calculations.addFirst(copyArray(calculationMoment));

                if (node.isAtPosition(endRow, endCol)) {
                    return new Path(history, calculations, node, PathFinder.STAR);
                }

                history.addFirst(copyArray(board));

                int[][] positions = {{row - 1, col}, {row + 1, col}, {row, col - 1}, {row, col + 1}};
                for (int[] position : positions) {
                    int x = position[0];
                    int y = position[1];
                    if (this.isPositionInGrid(x, y, boardRowSize, boardColSize)) {
                        if (board[x][y] == PathFinder.UNMARKED || board[x][y] == PathFinder.CONSIDERED || board[x][y] == PathFinder.END) {
                            double cost = node.getCost();
                            double moveCost = cost + Math.sqrt(Math.pow(endRow - x, 2) + Math.pow(endCol - y, 2));
                            board[x][y] = PathFinder.CONSIDERED;
                            calculationMoment[x][y] = (int) moveCost;
                            Node child = new Node(x, y, moveCost);
                            child.setAncestor(node);
                            minHeap.add(child);
                        }
                    }
                }
            }
        }
        return new Path(history, calculations, null, PathFinder.STAR);
    }


    public Path getShortestPathWithBFS() {

        //init grid and locations
        LinkedList<int[][]> history = new LinkedList<>();
        int[][] board = copyArray(this.board);
        int boardRowSize = board.length;
        int boardColSize = board[0].length;

        LinkedList<Node> queue = new LinkedList<>();
        queue.addFirst(new Node(startRow, startCol, 0));

        //checks whether a destination is reached
        while (queue.size() > 0) {

            Node node = queue.removeFirst();
            int row = node.getRow();
            int col = node.getCol();

            if (node.isAtPosition(endRow, endCol)) {
                return new Path(history, null, node, PathFinder.BFS);
            }

            if(!node.isAtPosition(startRow, startCol)){
                board[row][col] = PathFinder.MARKED;
            }

            history.addFirst(copyArray(board));

            int[][] positions = {{row - 1, col}, {row + 1, col}, {row, col - 1}, {row, col + 1}};
            for (int[] position : positions) {
                int x = position[0];
                int y = position[1];
                if (this.isPositionInGrid(x, y, boardRowSize, boardColSize)){
                    if (board[x][y] == PathFinder.UNMARKED || board[x][y] == PathFinder.END) {
                        Node child = new Node(x, y, 0);
                        child.setAncestor(node);
                        board[x][y] = PathFinder.CONSIDERED;
                        queue.addLast(child);
                    }
                }

            }
        }
        return new Path(history, null, null, PathFinder.BFS);
    }


    public Path getShortestPathWithBidirectionalBFS(){
        LinkedList<int[][]> history = new LinkedList<>();
        int [][] board = copyArray(this.board);
        int boardRowSize = board.length;
        int boardColSize = board[0].length;

        Node [][] positionToNode = new Node[boardRowSize][boardColSize];

        positionToNode[startRow][startCol] = new Node(startRow, startCol, 0);
        positionToNode[endRow][endCol] = new Node(endRow, endCol, 0);


        LinkedList<Node> startQueue = new LinkedList<>();
        LinkedList<Node> endQueue = new LinkedList<>();

        startQueue.addFirst(positionToNode[startRow][startCol]);
        endQueue.addFirst(positionToNode[endRow][endCol]);

        while(startQueue.size() > 0 && endQueue.size() > 0) {
            Node startNode = startQueue.removeFirst();
            Node endNode = endQueue.removeFirst();

            int startNodeRow = startNode.getRow();
            int startNodeCol = startNode.getCol();

            int endNodeRow = endNode.getRow();
            int endNodeCol = endNode.getCol();

            history.addFirst(copyArray(board));

            int[][] positions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
            for (int i = 0; i < positions.length; i++) {
                int x = startNodeRow + positions[i][0];
                int y = startNodeCol + positions[i][1];
                if (this.isPositionInGrid(x, y, boardRowSize, boardColSize)) {
                    if (board[x][y] == PathFinder.UNMARKED || board[x][y] == PathFinder.END_MARKER) {
                        if (board[x][y] == PathFinder.END_MARKER) {
                            Node endRef = positionToNode[x][y];
                            Path path = new Path(history, null, null, PathFinder.BIDIRECTIONAL);
                            path.linkPaths(startNode, endRef);
                            return path;
                        } else {
                            Node child = new Node(x, y, 0);
                            child.setAncestor(startNode);
                            board[x][y] = PathFinder.START_MARKER;
                            positionToNode[x][y] = child;
                            startQueue.addLast(child);
                        }
                    }
                }

                x = endNodeRow + positions[positions.length - 1 - i][0];
                y = endNodeCol + positions[positions.length - 1 - i][1];
                if (this.isPositionInGrid(x, y, boardRowSize, boardColSize)) {
                    if (board[x][y] == PathFinder.UNMARKED || board[x][y] == PathFinder.START_MARKER) {
                        if (board[x][y] == PathFinder.START_MARKER) {
                            Node startRef = positionToNode[x][y];
                            Path path = new Path(history, null, null, PathFinder.BIDIRECTIONAL);
                            path.linkPaths(endNode, startRef);
                            return path;
                        } else {
                            Node child = new Node(x, y, 0);
                            child.setAncestor(endNode);
                            board[x][y] = PathFinder.END_MARKER;
                            positionToNode[x][y] = child;
                            endQueue.addLast(child);
                        }
                    }
                }

            }
        }

        return new Path(history, null, null, PathFinder.BIDIRECTIONAL);
    }

}