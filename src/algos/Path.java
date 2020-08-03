package algos;

import java.util.LinkedList;

public class Path {
    private boolean hasReachedEnd;
    private LinkedList<int[][]> history;
    private LinkedList<int[][]> calculations;
    private LinkedList<int[]> path;
    private String findingMethod;


    public Path(LinkedList<int[][]> history, LinkedList<int[][]> calculations, Node finalNode, String findingMethod) {
        this.history = history;
        this.calculations = calculations;
        this.findingMethod = findingMethod;
        if (finalNode == null) {
            this.hasReachedEnd = false;
        } else {
            this.hasReachedEnd = true;
            this.path = new LinkedList<>();
            Node curr = finalNode;
            while (curr != null) {
                int row = curr.getRow();
                int col = curr.getCol();
                int[] position = {row, col};
                this.path.addFirst(position);
                curr = curr.getAncestor();
            }
        }
    }


    public void linkPaths(Node a, Node b){
        LinkedList<int[]> pathList = new LinkedList<>();
        Node cur = a;
        while(cur != null){
            int row = cur.getRow();
            int col = cur.getCol();
            int [] position = {row, col};
            pathList.addFirst(position);
            cur = cur.getAncestor();
        }
        cur = b;
        while(cur != null){
            int row = cur.getRow();
            int col = cur.getCol();
            int [] position = {row, col};
            pathList.addLast(position);
            cur = cur.getAncestor();
        }
        this.path = pathList;
        this.hasReachedEnd = true;
    }

    public LinkedList<int[][]> getHistory() {
        return this.history;
    }

    public LinkedList<int[][]> getCalculations() {
        return this.calculations;
    }

    public LinkedList<int[]> getPath() {
        return this.path;
    }

    public String getFindingMethod() {
        return this.findingMethod;
    }

    public boolean doesPathExist() {
        return this.hasReachedEnd;
    }
}
