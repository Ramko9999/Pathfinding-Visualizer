public class LocNode{
    int row;
    int col;
    LocNode previous; //this will used to retrace our steps

    public LocNode(int r, int c){
        row = r;
        col = c;
    }
}