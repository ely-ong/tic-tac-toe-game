package ticTacToe;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.util.Random;

public class Controller
{
    public GridPane grid;

    public Button btn11;
    public Button btn21;
    public Button btn31;
    public Button btn12;
    public Button btn22;
    public Button btn32;
    public Button btn13;
    public Button btn23;
    public Button btn33;

    public VBox dashboard;
    public MenuItem newGame;
    public MenuBar bar;
    public Label status;
    public Label selectFirst;
    public Button human;
    public Button cpu;
    public Label selectLvl;
    public Button lvl1;
    public Button lvl2;
    public Button lvl3;

    public static boolean playerTurn;
    public static String playerMark;
    public static String cpuMark;
    public boolean lvl1Mode;
    public boolean lvl2Mode;
    public boolean lvl3Mode;

    public static boolean playerWin;
    public static boolean cpuWin;
    public static boolean draw;

    public Model data;

    private static final int MIN_DEPTH = 0;

    public Controller()
    {
        data = new Model();
    }

    /**quits game if quit option is selected in options menu
     */
    @FXML
    private void quit()
    {
        System.exit(0);
    }

    /**restarts game if new game option is selected in options menu
     *
     * @param e new game option
     * @throws Exception
     */
    @FXML
    public void newGame(ActionEvent e) throws Exception {
        Stage stage = (Stage) bar.getScene().getWindow();
        stage.close();

        Stage window = new Stage();
        start(window);

    }

    /** opens window for game
     *
     * @param window stage used for game
     * @throws Exception
     */
    public void start(Stage window) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("ticTacToe.fxml"));
        window.setTitle("Tic Tac Toe");
        window.setScene(new Scene(root, 1000, 650));
        window.show();

    }

    /** sets up the first player based on option selected
     *
     * @param e option of human or computer player
     */
    @FXML
    private void turnPress(ActionEvent e)
    {
        Button btn = (Button) e.getSource();

        playerTurn = btn.getText().equals("Human");

        firstMessage();
        selectFirst.setDisable(true);
        pickMark();
        changeMessage();

        human.setDisable(true);
        cpu.setDisable(true);

        selectLvl.setDisable(false);
        lvl1.setDisable(false);
        lvl2.setDisable(false);
        lvl3.setDisable(false);

    }

    /** sets up the level based on level option selected
     *
     * @param e option of either level 0, 1, or 2
     */
    @FXML
    public void lvlsPress(ActionEvent e)
    {
        Button btn = (Button) e.getSource();

        grid.setDisable(false);

        switch (btn.getText())
        {
            case "Level 1":
                disableOptions(1);
                lvl1Mode = true;
                break;
            case "Level 2":
                disableOptions(2);
                lvl2Mode = true;
                break;
            case "Level 3":
                disableOptions(3);
                lvl3Mode = true;
        }

        if(!playerTurn)
        {
            getWinner();

            cpuMove();
        }
    }

    /** makes the cpu player perform a move based on lvl selected
     */
    public void cpuMove()
    {
        if(lvl1Mode)
            lvl1Move();

        else if(lvl2Mode)
            lvl2Move();

        else if(lvl3Mode)
            lvl3Move();
    }

    /** disables options after setup
     *
     * @param lvl level number selected
     */
    private void disableOptions(int lvl)
    {
        selectLvl.setText("Level "+lvl+" selected.");

        lvl1.setDisable(true);
        lvl2.setDisable(true);
        lvl3.setDisable(true);
    }

    /** changes welcome message to notify of player colors
     */
    private void changeMessage()
    {
        status.setText("Opponent's color is RED.\nYour color is BLUE.\n\n"+firstMessage());
    }

    /** gets the string corresponding to the mark of the winning player, if existing
     */
    private void getWinner()
    {
        String str = data.getWinMark();

        if(!str.equals("."))
            grid.setDisable(true);

        if(str.equals(cpuMark))
        {
            cpuWin = true;
            status.setText("Opponent wins!");
        }

        else if (str.equals(playerMark))
        {
            playerWin = true;
            status.setText("You win!");
        }

        else if(data.isGridFilled())
        {
            draw = true;
            status.setText("Draw!");
        }

    }

    /** returns line of string notifying which player moves first
     *
     * @return line of string notifying which player moves first
     */
    private String firstMessage()
    {
        if(playerTurn)
            return "You move first.";

        else
            return "Opponent moves first.";
    }

    /** assigns mark to players
     *
     */
    private void pickMark()
    {
        if(playerTurn)
        {
            playerMark = "X";
            cpuMark = "O";
        }

        else
        {
            playerMark = "O";
            cpuMark = "X";
        }
    }


    /** places a mark on a tile chosen by human player
     *
     * @param e tile/button chosen by human player
     */
    @FXML
    public void putMark(ActionEvent e)
    {
        getWinner();

        Button button = (Button) e.getSource();

        if(playerTurn)
        {
            button.setText(playerMark);
            button.setTextFill(Color.BLUE);
            button.setDisable(true);
        }

        data.placeMark(getButtonNum(button), playerMark);

        getWinner();

        if(data.getWinMark().equals(".") && !areAllDisable())
            nextTurn();
    }

    /** makes cpu player perform move for Lvl 1
     */
    @FXML
    public void lvl1Move()
    {
        Random random = new Random();
        int num=-1;
        Button button = getButton(getNearWinBtn(cpuMark));

        do {
            if(!areAllDisable() && !grid.isDisable())
            {
                num = random.nextInt((9 - 1) + 1) + 1;
                button = getButton(num);
            }

            }while (!areAllDisable() && !grid.isDisable() && button.isDisable());

            button.setText(cpuMark);
            button.setTextFill(Color.RED);

            button.setDisable(true);

            data.placeMark(num, cpuMark);

            getWinner();
            nextTurn();
    }

    /** gets a random odd number from available button numbers
     *
     * @param availMoves array of available button numbers
     * @return an odd button number that is an element of availMoves
     */
    public int getOddNum(int[] availMoves)
    {
        Random random = new Random();
        int num =-1;
        num = random.nextInt(((availMoves.length-1) - 0) + 1) + 0;

        while(availMoves[num]%2==0)
            num = random.nextInt(((availMoves.length-1) - 0) + 1) + 0;

        return availMoves[num];

    }

    /** gets a button number adjacent to a number of button that is marked by player
     *
     * @param availMoves array of available button numbers
     * @param btnNum number of button that is marked by player
     * @return button number adjacent to corresponding button of btnNum
     */
    public int getAdjMove(int[] availMoves, int btnNum)
    {
        for(int i=0; i<availMoves.length; i++)
        {
            if(availMoves[i]==btnNum-3|| availMoves[i]==btnNum+3 ||
                    availMoves[i]==btnNum-1 || availMoves[i]==btnNum+1)
                return availMoves[i];
        }

        return 0;
    }

    /** gets number of button that will result in a win
     *
     * @param mark mark of player
     * @return number of button that will result in a win
     */
    public int getNearWinBtn(String mark)
    {
        int num1=1, num2=2, num3=3;
        int base =3, center=5;

        //if a row can nearly be formed
        for (int i=0; i<base*2+1; i+=3)
        {
            if(getButton(num1+i).getText().equals(mark) && getButton(num2+i).getText().equals(mark) &&
            !getButton(num3+i).isDisable())
                return num3+i;

            else if (getButton(num2+i).getText().equals(mark) && getButton(num3+i).getText().equals(mark)&&
                    !getButton(num1+i).isDisable())
                return num1+i;

            else if (getButton(num1+i).getText().equals(mark) && getButton(num3+i).getText().equals(mark)&&
                    !getButton(num2+i).isDisable())
                return num2+i;
        }

        //if a col can nearly be formed
        for(int i=1; i<base+1; i++)
        {
            if(getButton(i).getText().equals(mark) && getButton(i+base).getText().equals(mark) &&
                    !getButton(i+base*2).isDisable())
                return i+base*2;

            else if(getButton(i+base).getText().equals(mark) && getButton(i+base*2).getText().equals(mark)&&
                    !getButton(i).isDisable())
                return i;

            else if(getButton(i).getText().equals(mark) && getButton(i+base*2).getText().equals(mark)&&
                    !getButton(i+base).isDisable())
                return i+base;
        }

        //if a diagonal can nearly be formed
        if(getButton(num1).getText().equals(mark) && getButton(center).getText().equals(mark)&&
                !getButton(center+4).isDisable())
            return center+4;

        else if(getButton(center).getText().equals(mark) && getButton(center+4).getText().equals(mark)&&
                !getButton(num1).isDisable())
            return num1;

        else if(getButton(num1).getText().equals(mark) && getButton(center+4).getText().equals(mark)&&
                !getButton(center).isDisable())
            return center;

        else if(getButton(num3).getText().equals(mark) && getButton(center).getText().equals(mark)&&
                !getButton(center+2).isDisable())
            return center+2;

        else if(getButton(center).getText().equals(mark) && getButton(center+2).getText().equals(mark)&&
                !getButton(num3).isDisable())
            return num3;

        else if(getButton(num3).getText().equals(mark) && getButton(center+2).getText().equals(mark)&&
                !getButton(center).isDisable())
            return center;

        return 0;
    }

    /** makes cpu player perform move for Lvl 2
     */
    public void lvl2Move()
    {
        int[] availMoves = getAvailMoves();
        int num = -1;
        Button button;

        //prioritizes filling up nearly formed line to win
        if(getNearWinBtn(cpuMark)!=0)
        {
            num = getNearWinBtn(cpuMark);
        }

        //next prioritizes blocking player
        else if(getNearWinBtn(playerMark)!=0)
        {
            num = getNearWinBtn(playerMark);
        }

        //if game has only started, cpu picks an odd button (tiles on corners or center)
        else if(availMoves.length==9 || availMoves.length==8)
        {
            num = getOddNum(availMoves);
        }

        //otherwise try to put marks adjacent to another
        else
        {
            for(int i=1; i<10; i++)
            {
               // System.out.println("i="+i);
                if(getButton(i).getText().equals(cpuMark))
                {
                    if(getAdjMove(availMoves, i)!=0)
                        num = getAdjMove(availMoves, i);
                }
            }

        }

        button = getButton(num);

        if(!areAllDisable())
        {
            button.setText(cpuMark);
            button.setTextFill(Color.RED);

            button.setDisable(true);

            data.placeMark(num, cpuMark);
        }

        getWinner();
        nextTurn();

    }

    /** checks if all buttons are disabled/marked
     *
     * @return true if all buttons are disabled/marked, false if not
     */
    private boolean areAllDisable()
    {
        int count=0;

        for(int i=1; i<10; i++)
        {
            if(getButton(i).isDisable())
                count++;
        }

        return count==9;
    }

    /**gets button based on assigned number
     *
     * @param num assigned number of button
     * @return button corresponding to num
     */
    private Button getButton(int num)
    {
        switch (num)
        {
            case 1:
                return btn11;
            case 2:
                return btn21;
            case 3:
                return btn31;
            case 4:
                return btn12;
            case 5:
                return btn22;
            case 6:
                return btn32;
            case 7:
                return btn13;
            case 8:
                return btn23;
            case 9:
                return btn33;
        }

        return null;
    }

    /** gets number based on assigned button
     *
     * @param button assigned button of number
     * @return number corresponding to button
     */
    public int getButtonNum(Button button)
    {
        if (btn11.equals(button)) {
            return 1;
        } else if (btn21.equals(button)) {
            return 2;
        } else if (btn31.equals(button)) {
            return 3;
        } else if (btn12.equals(button)) {
            return 4;
        } else if (btn22.equals(button)) {
            return 5;
        } else if (btn32.equals(button)) {
            return 6;
        } else if (btn13.equals(button)) {
            return 7;
        } else if (btn23.equals(button)) {
            return 8;
        } else if (btn33.equals(button)) {
            return 9;
        }

        return -1;
    }

    /** switches to next player turn
     */
    private void nextTurn()
    {
        if(playerTurn)
        {
            playerTurn = false;
            cpuMove();
        }

        else
        {
            playerTurn=true;
        }
    }

    /** makes cpu player perform move for Lvl 3
     */
    public void lvl3Move() {
        int bestNum = Integer.MAX_VALUE, score, bestBtn = 0;
        int[] availMoves = getAvailMoves(); //array containing tile numbers that are blank
        boolean tempTurn = playerTurn;

        for (int i = 0; i < availMoves.length; i++)
        {
            if (getButton(availMoves[i]).getText().equals(""))
            {
                tryMove(tempTurn, playerMark, cpuMark, availMoves[i]); //tries to place a mark
                score = minimax(MIN_DEPTH, true); //MIN_DEPTH is 0

                undoMove(availMoves[i]);

                if (score < bestNum) {
                    bestNum = score;
                    bestBtn = availMoves[i];
                }
            }
        }

        getButton(bestBtn).setText(cpuMark); //places mark on "best" tile
        getButton(bestBtn).setTextFill(Color.RED);

        getButton(bestBtn).setDisable(true);
        data.placeMark(bestBtn, cpuMark);

        getWinner(); //checks for winner

        nextTurn(); //goes to next turn

    }

    /** performs minimax search for best move
     *
     * @param depth depth of search tree
     * @param playerTempTurn temporary player turn
     * @return heuristic value
     */
    public int minimax(int depth, boolean playerTempTurn)
    {
        String player = playerMark, cpu = cpuMark, temp;
        int[] availMoves = getAvailMoves();
        int score, min = Integer.MAX_VALUE, max = Integer.MIN_VALUE; //player maximizes, cpu minimizes

        temp = data.getWinMark();

        //checks for winner
        if (temp.equals(playerMark))
            return 10 - depth;

        else if (temp.equals(cpuMark))
            return -10 + depth;

        //if draw
        else if (temp.equals(".") && data.isGridFilled())
            return 0;

        if (playerTempTurn) {
            for (int i = 0; i < availMoves.length; i++)
            {
                if (getButton(availMoves[i]).getText().equals("")) {
                    tryMove(playerTempTurn, player, cpu, availMoves[i]); //tries a move

                    score = minimax(depth + 1, false);

                    undoMove(availMoves[i]);

                    max = Math.max(score, max);

                }
            }

            return max;
        }
        else {
            for (int i = 0; i < availMoves.length; i++)
            {
                if (getButton(availMoves[i]).getText().equals("")) {
                    tryMove(playerTempTurn, player, cpu, availMoves[i]);
                    score = minimax(depth + 1, true);

                    undoMove(availMoves[i]);

                    min = Math.min(score, min);

                }
            }

            return min;
        }
    }

    /** makes temporary move for minimax search
     *
     * @param playerTempTurn player turn
     * @param player mark of human player
     * @param cpu mark of cpu player
     * @param btnNum assigned number of button to mark
     */
    public void tryMove(boolean playerTempTurn, String player, String cpu, int btnNum)
    {
        if(playerTempTurn)
        {
            getButton(btnNum).setText(player);
            data.placeMark(btnNum, player);
        }

        else
        {
            getButton(btnNum).setText(cpu);
            data.placeMark(btnNum, cpu);
        }

    }

    /** removes temporary move for minimax search
     *
     * @param btnNum assigned number of button to remove mark
     */
    public void undoMove(int btnNum)
    {
        getButton(btnNum).setText("");
        data.placeMark(btnNum, ".");

    }

    /** gets array of numbers corresponding to blank buttons
     *
     * @return array of numbers corresponding to blank buttons
     */
    public int[] getAvailMoves()
    {
        int num = data.countNumBlank(), ctr=0;
        int[] btns = new int[num];

        for(int i=1; i<4; i++)
        {
            for (int j = 1; j < 4; j++)
            {
                if(data.grid[i][j].equals("."))
                {
                    btns[ctr] = data.getBtnNum(i, j);
                    ctr++;
                }

            }
        }

        return btns;
    }

}
