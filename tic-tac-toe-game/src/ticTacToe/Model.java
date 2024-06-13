package ticTacToe;

public class Model
{
    public static String[][] grid;

    public Model()
    {
        this.grid = new String[4][4];

        for(int i=1; i<4; i++)
        {
            for(int j=1; j<4; j++)
            {
                grid[i][j] = ".";
            }
        }
    }

    /** gets mark of winning player
     *
     * @return mark of winning player if existing, period if otherwise
     */
    public String getWinMark()
    {
        if(!getWinRow().equals("."))
            return getWinRow();

        else if(!getWinCol().equals("."))
            return getWinCol();

        else if(!getWinDiag().equals("."))
            return getWinDiag();

        return ".";
    }

    /** gets mark of winning player among rows of the board
     *
     * @return mark of winning player if existing, period if otherwise
     */
    public String getWinRow()
    {
        if (!grid[1][1].equals(".") && grid[1][1].equals(grid[1][2]) && grid[1][1].equals(grid[1][3]))
            return grid[1][1];

        else if (!grid[2][1].equals(".") && grid[2][1].equals(grid[2][2]) && grid[2][1].equals(grid[2][3]))
            return grid[2][1];

        else if (!grid[3][1].equals(".") && grid[3][1].equals(grid[3][2]) && grid[3][1].equals(grid[3][3]))
            return grid[3][1];

        return ".";

    }

    /** gets mark of winning player among columns of the board
     *
     * @return mark of winning player if existing, period if otherwise
     */
    public String getWinCol()
    {
        if (!grid[1][1].equals(".") && grid[1][1].equals(grid[2][1]) && grid[1][1].equals(grid[3][1]))
            return grid[1][1];

        else if (!grid[1][2].equals(".") && grid[1][2].equals(grid[2][2]) && grid[1][2].equals(grid[3][2]))
            return grid[1][2];

        else if (!grid[1][3].equals(".") && grid[1][3].equals(grid[2][3]) && grid[1][3].equals(grid[3][3]))
            return grid[1][3];

        return ".";
    }

    /** gets mark of winning player among diagonals of the board
     *
     * @return mark of winning player if existing, period if otherwise
     */
    public String getWinDiag()
    {
        if (!grid[1][1].equals(".") && grid[1][1].equals(grid[2][2]) && grid[1][1].equals(grid[3][3]))
            return grid[1][1];

        else if (!grid[1][3].equals(".") && grid[1][3].equals(grid[2][2]) && grid[1][3].equals(grid[3][1]))
            return grid[1][3];

        return ".";
    }

    /** determines if database board is completely filled
     *
     * @return true if completely filled, false if not
     */
    public boolean isGridFilled()
    {
        int count=0;

        for(int i=1; i<4; i++)
        {
            for(int j=1; j<4; j++)
            {
                if(!grid[i][j].equals("."))
                    count++;
            }

        }

        return count==9;
    }

    /** updates board database with a mark placed by player
     *
     * @param num assigned number of button
     * @param mark mark of player
     */
    public void placeMark(int num, String mark)
    {
        switch (num)
        {
            case 1:
                grid[1][1] = mark;
                break;
            case 2:
                grid[1][2] = mark;
                break;
            case 3:
                grid[1][3] = mark;
                break;
            case 4:
                grid[2][1] = mark;
                break;
            case 5:
                grid[2][2] = mark;
                break;
            case 6:
                grid[2][3] = mark;
                break;
            case 7:
                grid[3][1] = mark;
                break;
            case 8:
                grid[3][2] = mark;
                break;
            case 9:
                grid[3][3] = mark;
        }
    }

    /** gets button number based on row and column position in board database
     *
     * @param i index of row position in board database
     * @param j index of column position in board database
     * @return button number
     */
    public int getBtnNum(int i, int j)
    {
        switch(i)
        {
            case 1:
                return j;
            case 2:
                return j+3;
            case 3:
                return j+3*2;
        }

        return -1;
    }

    /** counts number of blank tiles/buttons
     *
     * @return number of blank tiles/buttons
     */
    public int countNumBlank()
    {
        int num=0;

        for(int i=1; i<4; i++)
        {
            for(int j=1; j<4; j++)
                if(grid[i][j].equals("."))
                    num++;
        }

        return num;
    }

    /** prints configuration of board database for testing
     */
    public void displayGrid()
    {
        for(int i=1; i<4; i++)
        {
            for(int j=1; j<4; j++)
            {
                System.out.print(grid[i][j]);
            }
            System.out.print("\n");
        }
    }
}
