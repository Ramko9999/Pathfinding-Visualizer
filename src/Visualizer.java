import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Stack;

public class Visualizer extends JFrame{
    TileButton[][] gridArray = new TileButton[20][20];
    JPanel gridPanel = new JPanel(new GridLayout(20,20));
    int [][] repArray = new int[20][20];
    String currentKey = "";
    String algoKeyResult  = "A*";
    int sRow = 18;
    int sCol = 19;
    int eRow = 5;
    int eCol = 2;


    public Visualizer(){
        super("Visualizer");
        this.setSize(800,820);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initGrid();
        initUI();
        initPoints();
        this.setVisible(true);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

    }

    public void initUI(){
        this.add(gridPanel);
        JButton retry = new JButton("Reset");
        JButton runSearch = new JButton("Run " + algoKeyResult);
        JPanel actions = new JPanel(new GridLayout(1, 2));
        actions.add(runSearch);
        actions.add(retry);
        this.add(actions, BorderLayout.SOUTH);
        //actions to reset the board
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyCode = e.getKeyChar();
                System.out.println("Changed Key: " + keyCode);
               switch (keyCode){
                   case 'c':
                       currentKey = "C";
                       break;
                   case 'e':
                       currentKey = "E";
                       break;
                   case 'a':
                       algoKeyResult = "A*";
                       runSearch.setText("Run " + algoKeyResult);
                       break;
                   case 'b':
                       algoKeyResult = "BFS";
                       runSearch.setText("Run " + algoKeyResult);
                       break;
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
        runSearch.setFocusable(false);
        retry.setFocusable(false);
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
                        Controller c = new Controller(copyArray(repArray), sRow, sCol, eRow, eCol, algoKeyResult);
                        LocNode finalNode = c.getPath();
                        ColorChanger colorChanger = new ColorChanger(c.requestHistory(), finalNode, gridArray, c.requestCalculations());
                        colorChanger.start();


                    }
                }
        );

    }
    public void initPoints(){
        gridArray[sRow][sCol].setBackground(Color.ORANGE);
        repArray[sRow][sCol] = 10;
        repArray[eRow][eCol] = 20;
        gridArray[eRow][eCol].setBackground(Color.ORANGE);

    }
    public void computeGrid(){
        for(int i = 0; i < gridArray.length; i++){
            for(int j = 0; j <gridArray[0].length; j++){
                if(gridArray[i][j].isClicked){
                    repArray[i][j] = -1;
                }
                else{
                    if(repArray[i][j] != 20 && repArray[i][j] != 10){
                        repArray[i][j] = 0;
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
                if(repArray[i][j] != -1){
                    if(repArray[i][j] == 10){
                        gridArray[i][j].setBackground(Color.orange);
                    }
                    else if(repArray[i][j] == 20){
                        gridArray[i][j].setBackground(Color.orange);
                    }
                    else{
                        gridArray[i][j].setBackground(Color.white);
                        gridArray[i][j].isClicked = false;
                    }
                    gridArray[i][j].setText("");

            }
    }

}

    public void initGrid(){
        for(int i = 0; i < gridArray.length; i++)
            for(int j = 0; j < gridArray.length; j++){

                 gridArray[i][j] = new TileButton();
                 gridArray[i][j].setFocusable(false);
                gridArray[i][j].setMargin(new Insets(0, 0, 0, 0));
                 if(!((i == sRow && j == sCol) || (i == eRow && j == eCol))){


                     gridArray[i][j].addMouseListener(new MouseListener() {
                         @Override
                         public void mouseClicked(MouseEvent e) {
                             TileButton clickedButton = (TileButton) e.getSource();
                             if(clickedButton.isClicked){
                                 clickedButton.setBackground(Color.white);
                             }
                             else{
                                 clickedButton.setBackground(Color.black);
                             }
                             clickedButton.isClicked = !clickedButton.isClicked;
                             computeGrid();
                         }

                         @Override
                         public void mousePressed(MouseEvent e) {

                         }

                         @Override
                         public void mouseReleased(MouseEvent e) {

                         }

                         @Override
                         public void mouseEntered(MouseEvent e) {

                         }

                         @Override
                         public void mouseExited(MouseEvent e) {

                         }
                     });

                     gridArray[i][j].addMouseMotionListener(new MouseMotionListener() {
                         @Override
                         public void mouseDragged(MouseEvent e) {
                         }

                         @Override
                         public void mouseMoved(MouseEvent e) {

                             TileButton clickedButton = (TileButton) e.getSource();
                             Color color = null;
                             boolean willBeAWall = false;
                             if(currentKey.equals("C")){
                                 color = Color.black;
                                 willBeAWall = true;
                             }
                             else if(currentKey.equals("E")){
                                 color = Color.white;

                             }

                             if(color != null){
                                 clickedButton.setBackground(color);
                                 clickedButton.isClicked = willBeAWall;
                                 computeGrid();
                             }


                         }
                     });
                 }


                    gridPanel.add(gridArray[i][j]);
            }
        }



    public static void main(String[] args) {
        new Visualizer();
    }


}

class TileButton extends JButton{
    boolean isClicked = false;

    public TileButton(){
        super();
        this.setBackground(Color.WHITE);
    }
}


class MouseMotionListenerHandler implements MouseMotionListener{
    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Dragged");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println(e.getX());
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
                gridArray[result.row][result.col].setBackground(Color.cyan);
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
                    gridArray[i][j].setBackground(Color.RED);
                }
                if(moment[i][j] == 1){
                    gridArray[i][j].setBackground(Color.green);
                }
            }
        }
    }

    public void calculateGridMoment(int [][] moment, int [][] calcMoment){

        for(int i =0; i < moment.length; i++){
            for(int j = 0; j < moment[i].length; j++){
                if(moment[i][j] == -5 || moment[i][j] == 1){
                    gridArray[i][j].setText(calcMoment[i][j] + "");
                    gridArray[i][j].setForeground(Color.white);
                    gridArray[i][j].setFont(new Font("Helvetica", Font.BOLD, 12));



                }

            }
        }
    }
}