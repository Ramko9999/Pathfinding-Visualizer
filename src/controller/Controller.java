package controller;
import algos.PathFinder;
import algos.Path;


public class Controller {
    private PathFinder finder;
    private String algo;


    public Controller(int [][] b, int sR, int sC, int eR, int eC, String a){
        this.finder = new PathFinder(b, sR, sC, eR, eC);
        this.algo = a;
    }

    public Path getPath(){
        switch (this.algo){
            case PathFinder.STAR:
                return this.finder.getShortestPathWithStar();
            case PathFinder.BIDIRECTIONAL:
                return this.finder.getShortestPathWithBidirectionalBFS();
            default:
                return this.finder.getShortestPathWithBFS();
        }
    }
}
