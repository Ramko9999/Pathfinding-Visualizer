import java.util.LinkedList;
import java.util.Stack;

//communicates between the code and ui
public class Controller {
    PathFinder finder;
    String algo;
    LinkedList<int[][]> history;
    LinkedList<int [][]> calculations;


    public Controller(int [][] b, int sR, int sC, int eR, int eC, String a){
        finder = new PathFinder(b, sR, sC, eR, eC);
        algo = a;
    }

    public LocNode getPath(){
        switch (algo){
            case "A*":
                LocNode finalNode =  finder.A_Star_Search();
                history = finder.history;
                calculations = finder.calculationTimeLapse;
                return finalNode;
            default:
                finalNode = finder.BFS();
                history = finder.history;
                calculations = null;
                return finalNode;

        }


    }
    public LinkedList requestHistory(){
        return this.history;
    }

    public LinkedList requestCalculations(){
        System.out.println("Calculations" + calculations);
        return calculations;
    }

}
