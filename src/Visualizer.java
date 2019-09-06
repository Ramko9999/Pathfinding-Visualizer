import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Stack;

public class Visualizer extends JFrame {
    TileButton[][] gridArray = new TileButton[15][15];
    JPanel gridPanel = new JPanel(new GridLayout(15,15));
    int [][] repArray = new int[15][15];
    int sRow = 11;
    int sCol = 10;
    int eRow = 2;
    int eCol = 2;


    public Visualizer(){
        super("Visualizer");
        this.setSize(790,820);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initGrid();
        initUI();
        initPoints();
        this.setVisible(true);

    }

    public void initUI(){
        this.add(gridPanel);
        JButton retry = new JButton("Reset");
        JButton runSearch = new JButton("Run search");
        JPanel actions = new JPanel(new GridLayout(1, 2));
        actions.add(runSearch);
        actions.add(retry);
        this.add(actions, BorderLayout.SOUTH);
        //actions to reset the board
        retry.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for(int i = 0; i < repArray.length;i ++){
                            for(int j = 0 ; j <repArray[i].length; j++){
                                if(repArray[i][j] == -5 || repArray[i][j] == 1){
                                    repArray[i][j] = 0;
                                }
                            }
                        }
                        modifyGrid(repArray);
                    }
                }
        );
        //actions to start the board
        runSearch.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Controller c = new Controller(copyArray(repArray), sRow, sCol, eRow, eCol, "A*");
                        LocNode finalNode = c.getPath();
                        ColorChanger colorChanger = new ColorChanger(c.requestHistory(), finalNode, gridArray, c.requestCalculations());
                        colorChanger.start();

                    }
                }
        );

    }
    public void initPoints(){
        gridArray[sRow][sCol].tile.setBackground(Color.ORANGE);
        repArray[sRow][sCol] = 10;
        repArray[eRow][eCol] = 20;
        gridArray[eRow][eCol].tile.setBackground(Color.ORANGE);

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
                if(repArray[i][j] != -1){
                    if(repArray[i][j] == 10){
                        gridArray[i][j].tile.setBackground(Color.orange);
                    }
                    else if(repArray[i][j] == 20){
                        gridArray[i][j].tile.setBackground(Color.orange);
                    }
                    else{
                        gridArray[i][j].tile.setBackground(Color.white);
                        gridArray[i][j].isClicked = false;
                    }
                    gridArray[i][j].tile.setText("");

            }
    }

}

    public void initGrid(){
        for(int i = 0; i < gridArray.length; i++)
            for(int j = 0; j < gridArray.length; j++){

                 gridArray[i][j] = new TileButton(true, repArray, i, j);
                    gridPanel.add(gridArray[i][j].tile);
            }
        }



    public static void main(String[] args) {
        new Visualizer();
    }


}

class TileButton{
    boolean isClicked = false;
    boolean canRevert = true;
    JButton tile = new JButton();

    public TileButton(boolean r, int [][] gameGrid, int row, int col){

        super();
        canRevert = r;

        tile.setBackground(Color.WHITE);
        tile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(canRevert){
                    isClicked = !isClicked;
                    if(isClicked){
                        gameGrid[row][col] = -1;
                        tile.setBackground(Color.black);
                    }
                    else{
                        gameGrid[row][col] = 0;
                        tile.setBackground(Color.WHITE);
                    }
                }

            }
        });
    }
}

class ColorChanger extends Thread{
    LocNode result;
    TileButton [][] gridArray;
    LinkedList<int [][]> history;
    LinkedList<int [][]> calculations;
    public ColorChanger(LinkedList h, LocNode r, TileButton[][] g, LinkedList c){
        super();
        result = r;
        gridArray = g;
        history = h;
        calculations =c;

    }
    public void run(){
        while(!history.isEmpty()){
            try{
                this.sleep(100);
                int [][] gridMoment = history.getLast();
                processGridMoment(gridMoment);
                if(calculations != null){
                    calculateGridMoment(history.getLast(),calculations.getLast());
                }
            }
            catch(Exception e1){
                e1.printStackTrace();
            }
            history.removeLast();
            if(calculations != null){
                calculations.removeLast();
            }

        }

        while(result != null){
            try{
                this.sleep(100);
                gridArray[result.row][result.col].tile.setBackground(Color.cyan);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            result = result.previous;
        }


    }

    public void processGridMoment(int [][] moment){
        for(int i =0; i < moment.length; i++){
            for(int j = 0; j < moment[i].length; j++){
                if(moment[i][j] == -5){
                    gridArray[i][j].tile.setBackground(Color.RED);
                }
                if(moment[i][j] == 1){
                    gridArray[i][j].tile.setBackground(Color.green);
                }
            }
        }
    }

    public void calculateGridMoment(int [][] moment, int [][] calcMoment){

        for(int i =0; i < moment.length; i++){
            for(int j = 0; j < moment[i].length; j++){
                if(moment[i][j] == -5 || moment[i][j] == 1){
                    gridArray[i][j].tile.setText(calcMoment[i][j] + "");
                    gridArray[i][j].tile.setForeground(Color.white);
                    //gridArray[i][j].tile.setFont(new Font("Helvetica", Font.PLAIN, 8));



                }

            }
        }
    }
}