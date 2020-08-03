package visual;

import algos.Path;
import algos.PathFinder;
import controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.LinkedList;


public class Visualizer extends JFrame{

    //grid
    private TileButton[][] gridArray = new TileButton[20][20];
    private JPanel gridPanel = new JPanel(new GridLayout(20,20));
    private int [][] repArray = new int[20][20];

    //used for keyListeners and MouseListeners
    private String currentKey = "";
    private String algoKeyResult  = PathFinder.STAR;
    private boolean isEndPointMoving = false;
    private TileButton movingButton;

    //starting conditions
    private int sRow = 18;
    private int sCol = 19;
    private int eRow = 5;
    private int eCol = 2;


    public Visualizer(){

        //initialize a new JFrame
        super("visual.Visualizer");
        this.setSize(800,820);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initGrid();
        initUI();
        this.setVisible(true);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
    }

    //set end points
    public void initPoints(){
        repArray[sRow][sCol] = PathFinder.START;
        repArray[eRow][eCol] = PathFinder.END;
    }

    private void resetGrid(){
        for(int i = 0; i < this.repArray.length; i++){
            for(int j = 0; j < this.repArray[i].length; j++){
                if(this.repArray[i][j] == PathFinder.MARKED || this.repArray[i][j] == PathFinder.CONSIDERED){
                    this.repArray[i][j] = PathFinder.DEFAULT;
                }
            }
        }

        this.modifyGrid(this.repArray);
    }



    //used to create the UI
    public void initUI(){

        initPoints();

        //creates JPanel UI
        JButton retry = new JButton("Reset");
        final JButton runSearch = new JButton("Run " + algoKeyResult);
        JPanel actions = new JPanel(new GridLayout(1, 2));
        actions.add(runSearch);
        actions.add(retry);
        this.add(actions, BorderLayout.SOUTH);

        //actions to reset the board
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyCode = e.getKeyChar();
               switch (keyCode){
                   //set the current key or algo to a certain algo based on character clicked
                   case 'c':
                       currentKey = "C";
                       break;
                   case 'e':
                       currentKey = "E";
                       break;
                   case 'a':
                       algoKeyResult = PathFinder.STAR;
                       runSearch.setText("Run " + algoKeyResult);
                       break;
                   case 'b':
                       algoKeyResult = PathFinder.BFS;
                       runSearch.setText("Run " + algoKeyResult);
                       break;
                   case 'd':
                       algoKeyResult = PathFinder.BIDIRECTIONAL;
                       runSearch.setText("Run " + algoKeyResult);
                   default:
                       currentKey = "";
               }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                currentKey = "";
            }
        });
        //removes focus
        runSearch.setFocusable(false);
        retry.setFocusable(false);
        retry.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        resetGrid();
                    }
                }
        );
        //actions to start the board
        runSearch.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        resetGrid();
                        Controller c = new Controller(copyArray(repArray), sRow, sCol, eRow, eCol, algoKeyResult);
                        ColorChanger colorChanger = new ColorChanger(c.getPath(), gridArray);
                        colorChanger.start();
                    }
                }
        );

    }

    //reset repArray based on tiles
    public void computeGrid(){
        for(int i = 0; i < gridArray.length; i++){
            for(int j = 0; j <gridArray[0].length; j++){
                if(gridArray[i][j].isClicked){
                    repArray[i][j] = PathFinder.WALL;
                }
                else{
                    if(repArray[i][j] != PathFinder.END && repArray[i][j] != PathFinder.START){
                        repArray[i][j] = PathFinder.DEFAULT;
                    }
                }
            }
        }

    }


    public int[][] copyArray(int [][] array){
        int [][] newArray = new int[array.length][];
        for(int i = 0; i < array.length; i++){
            newArray[i] = array[i].clone();
        }
        return newArray;
    }

    public void modifyGrid(int [][] repArray){
        for(int i = 0; i < repArray.length; i++)
            for(int j = 0; j < repArray[i].length; j++){
                if(repArray[i][j] != PathFinder.WALL){

                    //set end points to proper color
                    if(repArray[i][j] == PathFinder.START){

                        gridArray[i][j].setBackground(Settings.START_COLOR);
                    }
                    else if(repArray[i][j] == PathFinder.END){

                        gridArray[i][j].setBackground(Settings.END_COLOR);
                    }
                    else{
                        gridArray[i][j].setBackground(Settings.BLANK_COLOR);
                        gridArray[i][j].isClicked = false;
                    }
                    gridArray[i][j].setText("");

            }
    }
        this.revalidate();
            this.repaint();

}

    public void handleMouseClicks(int row, int col, MouseEvent e){
        TileButton clickedButton = (TileButton) e.getSource();

        //swapping the end points
        if(isEndPointMoving && !(clickedButton.val > 0)){

            //fetch mouseButton and set positions of end point to new positions
            if(movingButton.val == PathFinder.START){

                repArray[sRow][sCol] = PathFinder.DEFAULT;
                repArray[row][col] = PathFinder.START;
                sRow = row;
                sCol = col;

            }
            else{

                repArray[eRow][eCol] = PathFinder.DEFAULT;
                repArray[row][col] = PathFinder.END;
                eRow = row;
                eCol = col;
            }

            //finish and repaint
            isEndPointMoving = false;
            movingButton = null;
            this.remove(gridPanel);
            initGrid();

        }else{

            if(clickedButton.isClicked){
                clickedButton.setBackground(Settings.BLANK_COLOR);
                clickedButton.setOpaque(true);
                clickedButton.setBorderPainted(false);
            }
            else{
                clickedButton.setBackground(Settings.WALL_COLOR);
                clickedButton.setOpaque(true);
                clickedButton.setBorderPainted(false);
            }
            clickedButton.isClicked = !clickedButton.isClicked;
        }
        computeGrid();

    }

    public void handleMouseEntered(MouseEvent e){

        TileButton clickedButton = (TileButton) e.getSource();

        //show the hover effect of the end point when the mouse comes into the tile
        if(isEndPointMoving && !(clickedButton.val > 0)){

            if(movingButton.val == PathFinder.START){
                clickedButton.setBackground(Settings.START_COLOR);
            }
            else{
                clickedButton.setBackground(Settings.END_COLOR);
            }
            clickedButton.setOpaque(true);
            clickedButton.setBorderPainted(false);

        }
    }

    public void handleMouseExited(MouseEvent e){
        TileButton clickedButton = (TileButton) e.getSource();
        //show the hover effect of the end point when the moves leaves the Tile
        if(isEndPointMoving){
            if(clickedButton.isClicked){
                clickedButton.setBackground(Settings.WALL_COLOR);
            }
            else{
                clickedButton.setBackground(Settings.BLANK_COLOR);
            }
            clickedButton.setOpaque(true);
            clickedButton.setBorderPainted(false);
        }
    }

    public void handleMouseMoved(MouseEvent e){

        TileButton clickedButton = (TileButton) e.getSource();

        //if the end point isn't moving create and destroy walls
        if(!(isEndPointMoving && !(clickedButton.val > 0))){

            Color color = null;
            boolean willBeAWall = false;

            if(currentKey.equals("C")){
                color = Settings.WALL_COLOR;
                willBeAWall = true;
            }
            else if(currentKey.equals("E")){
                color = Settings.BLANK_COLOR;

            }

            if(color != null){

                clickedButton.setBackground(color);
                clickedButton.setOpaque(true);
                clickedButton.setBorderPainted(false);
                clickedButton.isClicked = willBeAWall;
                computeGrid();
            }

        }

    }

    public void initGrid(){
        gridPanel = new JPanel(new GridLayout(20,20));
        for(int i = 0; i < gridArray.length; i++)
            for(int j = 0; j < gridArray.length; j++){

                final int row = i;
                final int col = j;

                if(!((row == sRow && col == sCol) || (row == eRow && col == eCol))){
                    gridArray[row][col] = new TileButton();
                    gridArray[row][col].setFocusable(false);
                    gridArray[row][col].setMargin(new Insets(0, 0, 0, 0));
                    //keep walls
                    if(repArray[row][col] == PathFinder.WALL){
                        gridArray[row][col].setBackground(Settings.WALL_COLOR);
                        gridArray[row][col].setOpaque(true);
                        gridArray[row][col].setBorderPainted(false);
                        gridArray[row][col].isClicked = true;
                    }
                     //handles click of the visual.TileButton to turn the button to a wall
                     gridArray[row][col].addMouseListener(new MouseListener() {
                         @Override


                         public void mouseClicked(MouseEvent e) {
                            handleMouseClicks(row, col, e);
                         }

                         @Override
                         public void mousePressed(MouseEvent e) {

                         }

                         @Override
                         public void mouseReleased(MouseEvent e) {

                         }

                         @Override
                         public void mouseEntered(MouseEvent e) {
                             handleMouseEntered(e);
                         }

                         @Override
                         public void mouseExited(MouseEvent e) {
                                handleMouseExited(e);
                         }
                     });

                     //handles the auto-wall creation of the button
                     gridArray[i][j].addMouseMotionListener(new MouseMotionListener() {
                         @Override
                         public void mouseDragged(MouseEvent e) {
                         }

                         @Override
                         public void mouseMoved(MouseEvent e) {
                             handleMouseMoved(e);
                         }


                     });
                 }

                 //this is for end points
                 else{
                     if(sRow == row && sCol == col ){
                         gridArray[i][j] = new TileButton(PathFinder.START);
                         gridArray[i][j].setBackground(Settings.START_COLOR);
                         gridArray[row][col].setOpaque(true);
                         gridArray[row][col].setBorderPainted(false);
                     }
                     else{
                         gridArray[i][j] = new TileButton(PathFinder.END);
                         gridArray[i][j].setBackground(Settings.END_COLOR);
                         gridArray[row][col].setOpaque(true);
                         gridArray[row][col].setBorderPainted(false);
                     }

                     gridArray[i][j].setFocusable(false);
                     gridArray[i][j].setMargin(new Insets(0, 0, 0, 0));

                     gridArray[i][j].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if(!isEndPointMoving){
                                movingButton = (TileButton) e.getSource();
                            }
                            isEndPointMoving = !isEndPointMoving;
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            Visualizer.super.repaint();
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });
                 }
                    gridPanel.add(gridArray[i][j]);
            }

            this.add(gridPanel);
            this.revalidate();
            this.repaint();
        }

    public static void main(String[] args) {
        new Visualizer();
    }


}

class TileButton extends JButton{
    boolean isClicked = false;
    int val = -999;

    public TileButton(){
        super();
        this.setBackground(Settings.BLANK_COLOR);
        this.setOpaque(true);
        this.setBorderPainted(false);
    }

    public TileButton(int v){
        super();
        this.val = v;
        if(v == PathFinder.START){
            this.setBackground(Settings.START_COLOR);
        }
        else{
            this.setBackground(Settings.END_COLOR);
        }
        this.setOpaque(true);
        this.setBorderPainted(false);
    }

}


//used to show the actual algorithm path and show calculations with a delay
class ColorChanger extends Thread{

    private Path path;
    private TileButton [][] gridArray;



    public ColorChanger(Path path, TileButton[][] buttons){
        super("Color Changer");
        this.path = path;
        this.gridArray = buttons;
    }

    public void run(){

        LinkedList<int[][]> history = this.path.getHistory();
        LinkedList<int[][]> calculations = this.path.getCalculations();

        //show the history of the moves with a delay
        while(!history.isEmpty()){
            try{

                //25 second delay
                this.sleep(Settings.HISTORY_DELAY);

                //show calculated and examined nodes as well as costs
                int [][] gridMoment = history.removeLast();

                processGridMoment(gridMoment);
                if(calculations != null){
                    calculateGridMoment(history.getLast(),calculations.getLast());
                }
            }

            catch(Exception e1){
                e1.printStackTrace();
            }
            if(calculations != null){
                calculations.removeLast();
            }

        }

        //display no path alert
        if(!this.path.doesPathExist()){
            JOptionPane.showMessageDialog(null, "There is no path");
        }
        else{

            LinkedList<int[]> path = this.path.getPath();
            //show actual path in different color
            while(path.size() > 0){
                int [] position = path.removeFirst();
                int x = position[0];
                int y = position[1];
                try{
                    this.sleep(Settings.PATH_DELAY);
                    gridArray[x][y].setBackground(Settings.PATH_COLOR);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }



    }

    public void processGridMoment(int [][] moment){
        HashMap<Integer, Color> colorMap = new HashMap<>();
        colorMap.put(PathFinder.MARKED, Settings.MARK_COLOR);
        colorMap.put(PathFinder.CONSIDERED, Settings.CONSIDERATION_COLOR);
        colorMap.put(PathFinder.START_MARKER, Settings.START_MARK_COLOR);
        colorMap.put(PathFinder.END_MARKER, Settings.END_MARK_COLOR);
        colorMap.put(PathFinder.START, Settings.START_COLOR);
        colorMap.put(PathFinder.END, Settings.END_COLOR);


        //sets color based on whether node is examined or calculated
        for(int i =0; i < moment.length; i++){
            for(int j = 0; j < moment[i].length; j++){
                if(moment[i][j] != PathFinder.UNMARKED && moment[i][j] != PathFinder.WALL){
                    gridArray[i][j].setBackground(colorMap.get(moment[i][j]));
                    gridArray[i][j].setOpaque(true);
                    gridArray[i][j].setBorderPainted(false);
                }
            }
        }
    }

    public void calculateGridMoment(int [][] moment, int [][] calcMoment){

        //sets cost of a node
        for(int i =0; i < moment.length; i++){
            for(int j = 0; j < moment[i].length; j++){
                if(moment[i][j] == PathFinder.MARKED || moment[i][j] == PathFinder.CONSIDERED){
                    gridArray[i][j].setText(calcMoment[i][j] + "");
                    gridArray[i][j].setForeground(Settings.BLANK_COLOR);
                    gridArray[i][j].setFont(new Font("Helvetica", Font.BOLD, 7));
                }
            }
        }
    }
}