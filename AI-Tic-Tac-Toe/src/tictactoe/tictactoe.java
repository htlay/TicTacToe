
package tictactoe;
import java.util.Scanner;

/**
 * @author Samuel Jih
 * @author Hung Lay
 */
public class tictactoe 
{
    private char[][] board;
    private final int SIZE = 8;
    private final char AI_MOVE = 'X';
    private final char PLAYER_MOVE = 'O';
    private final char EMPTY = '-';
    private static int TIME_THINKING;
    private static final int MIN = Integer.MIN_VALUE;
    private static final int MAX = Integer.MAX_VALUE;
    private static final int maxDepth = 15;
    private static final int depingDepth = 5;
    private static final int VAL_NONE = 0;
    private static final int VAL_PLAYER_WIN =  1;
    private static final int VAL_PLAYER_DRAW = 2;
    private static final int VAL_PLAYER_LOSE = 3;
    
    public tictactoe() 
    {
        board = new char[SIZE][SIZE];
        setup();
    }
    
    public void setup() 
    {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        } 
    }
    public void printBoard() 
    {
        String[] label = {"A ", "B ", "C ", "D ", "E ", "F ", "G ", "H "};
        for (int i = 0; i < SIZE; i++) {
            if (i == 0) {
                System.out.println("  1 2 3 4 5 6 7 8");
            }
            for (int j = 0; j < SIZE; j++) {
                if (j == 0) {
                    System.out.print(label[i] + board[i][j] + " ");
                }
                else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
    public boolean cutoff(int start) 
    {
        if (System.currentTimeMillis() - start >= TIME_THINKING) {
        return true;
    }
    return false;
    }
    public void getMove() 
    {
        Scanner in = new Scanner(System.in);
        String move;
        char[] string;
        char rowChar;
        char colChar;
        int rowInt = 0;
        int colInt = 0;
        boolean keepAsking = true;
       
        while (keepAsking) {
            System.out.println("Choose your next move: ");
            move = in.nextLine();
            string = move.toCharArray();
 
            rowChar = string[0];
            colChar = string[1];
 
            if (!Character.isDigit(rowChar) && Character.isDigit(colChar)) {
                colInt = Integer.parseInt(colChar + "") - 1;
               
                if (rowChar >= 65 && rowChar <= 90) {
                    rowInt = rowChar - 65;
                }
                else if (rowChar >= 97 && rowChar <= 122) {
                    rowInt = rowChar - 97;
                }
               
                if (rowInt >= 0 && rowInt <= 7 && colInt >= 0 && colInt <= 7) {
                    if (board[rowInt][colInt] == EMPTY) {
                        keepAsking = false;
                        board[rowInt][colInt] = PLAYER_MOVE; // Make move
                    }
                    else {
                        System.out.println("Move taken. Please enter another move.");
                    }
                }
                else {
                    System.out.println("Out of bounds. Please enter a valid move.");
                }
            }
            else {
                System.out.println("Invalid format. Please enter a valid move.");
            }
        }
    }
 
    public void makeMove()
    {
        int a;
	int best = MIN, depth = maxDepth, score, mi = 0 , mj = 0;

        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j<SIZE; j++)
            {
                if(board[i][j]== EMPTY)
                {
                    board[i][j] = AI_MOVE;
                    score = max(depth-1, MIN, MAX);
                    if(score > best)
                    {
                        mi = i;
                        mj = j;
                        best = score;
                    }
                    board[i][j] = EMPTY;
                }
            }
        }
        a = mi + 97;
        System.out.println("My move is: " + ((char)(a)) + (mj + 1));
	board[mi][mj] = AI_MOVE;
    }
    
    public int min(int depth, int alpha, int beta)
    {
	int best = MAX, score;
	if (check4winner() != 0) return (check4winner());
	if (depth == 0 || cutoff(TIME_THINKING)) 
            return (eval());
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j<SIZE; j++)
            {
                if(board[i][j]== EMPTY)
                {
                    board[i][j] = AI_MOVE;
                    score = Math.min(best,max(depth - 1,alpha,beta));
                    if(score < best)
                    {
                        best = score;
                    }
                    board[i][j] = EMPTY;
                }                
            }
        }
        return best;
    }
    
    public int max(int depth, int alpha, int beta)
    {
	int best = MIN, score;
	if (check4winner() != 0) 
            return (check4winner());
	if (depth == 0 || cutoff(TIME_THINKING)) 
            return (eval());
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j<SIZE; j++)
            {
                if(board[i][j]== EMPTY)
                {
                    board[i][j] = AI_MOVE;
                    score = Math.max(best,min(depth - 1,alpha,beta));
                    if(score > best)
                    {
                        best = score;
                    }
                    board[i][j] = EMPTY;
                }
            }
        }
        return best;
    }
    public boolean checkPlayerOrComputerWinRow(char val){
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j <= 4; j++)
            {
                if ((board[i][j] == val) && 
                    (board[i][j + 1] == val) && 
                    (board[i][j + 2] == val) && 
                    (board[i][j + 3] == val))
                        return true;     
            }
        }
        return false;
    }
    public boolean checkPlayerOrComputerWinCol(char val){
        for (int j = 0; j < SIZE; j++)
        {
            for (int i = 0; i <= 4; i++)
            {
                if ((board[i][j] == val) && 
                    (board[i + 1][j] == val) && 
                    (board[i + 2][j] == val) && 
                    (board[i + 3][j] == val))
                        return true;                
            }
        }
        return false;
    }
    
    public int check4winner()
    {
	if(checkPlayerOrComputerWinRow(PLAYER_MOVE) == true
            || checkPlayerOrComputerWinCol(PLAYER_MOVE) == true)
            return VAL_PLAYER_WIN;
        if(checkPlayerOrComputerWinRow(AI_MOVE) == true
            || checkPlayerOrComputerWinCol(AI_MOVE) == true)
            return VAL_PLAYER_LOSE;
        if(checkPlayerOrComputerWinRow(EMPTY) == false
           && checkPlayerOrComputerWinCol(EMPTY) == false)
            return VAL_PLAYER_DRAW;
	return VAL_NONE; // draw
}

    public int checkGameOver()
    {
        printBoard();
        if (check4winner() == VAL_PLAYER_WIN) 
        { 
            System.out.println("you win"); 
            return VAL_PLAYER_WIN;
        }
        if (check4winner() == VAL_PLAYER_LOSE) 
        { 
            System.out.println("computer win"); 
            return VAL_PLAYER_LOSE;
        }
        if(check4winner()== VAL_PLAYER_DRAW)
        {
            System.out.println("This game is a draw."); 
            return VAL_PLAYER_DRAW;
        }
        return VAL_NONE;
    }

    public int eval() {
	int health = 0;
        	for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 3; j++) {
                if ((board[i][j] == EMPTY) && (board[i][j + 1] == AI_MOVE) 
                        && (board[i][j + 2] == AI_MOVE) && 
                        (board[i][j + 3] == EMPTY)) {
                    health += 750; // AI advantage
                }
                else if ((board[i][j] == EMPTY) && (board[i][j + 1] == PLAYER_MOVE) 
                        && (board[i][j + 2] == PLAYER_MOVE) && 
                        (board[i][j + 3] == EMPTY)) {
                    health -= 750; // Player advantage
                }
            }
        }
        
        // Check for 3 in a row vertically
	for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 3; j++) {
                if ((board[j][i] == EMPTY) && (board[j + 1][i] == AI_MOVE) 
                        && (board[j + 2][i] == AI_MOVE) && 
                        (board[j + 3][i] == EMPTY)) {
                    health += 750;
                }
                else if ((board[j][i] == EMPTY) && (board[j + 1][i] == PLAYER_MOVE) 
                        && (board[j + 2][i] == PLAYER_MOVE) 
                        && (board[j + 3][i] == EMPTY)) {
                    health -= 750;
                }
            }
        }
	// Check for 3 in a row horizontally
	for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 4; j++) {
                if ((board[i][j] == EMPTY) && (board[i][j + 1] == AI_MOVE) 
                        && (board[i][j + 2] == AI_MOVE) && 
                        (board[i][j + 3] == AI_MOVE) && (board[i][j + 4] == EMPTY)) {
                    health += 1000; // AI advantage
                }
                else if ((board[i][j] == EMPTY) && (board[i][j + 1] == PLAYER_MOVE) 
                        && (board[i][j + 2] == PLAYER_MOVE) && 
                        (board[i][j + 3] == PLAYER_MOVE) && (board[i][j + 4] == EMPTY)) {
                    health -= 1000; // Player advantage
                }
            }
        }
        
        // Check for 3 in a row vertically
	for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 4; j++) {
                if ((board[j][i] == EMPTY) && (board[j + 1][i] == AI_MOVE) 
                        && (board[j + 2][i] == AI_MOVE) && 
                        (board[j + 3][i] == AI_MOVE) && (board[j + 4][i] == EMPTY)) {
                    health += 1000;
                }
                else if ((board[j][i] == EMPTY) && (board[j + 1][i] == PLAYER_MOVE) 
                        && (board[j + 2][i] == PLAYER_MOVE) && (board[j + 3][i] == PLAYER_MOVE) 
                        && (board[j + 4][i] == EMPTY)) {
                    health -= 1000;
                }
            }
        }

	// Check for block advantage horizontally
	for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 5; j++) {
                // If AI blocks player
                if ((board[i][j] == AI_MOVE) && (board[i][j + 1] == PLAYER_MOVE) 
                        && (board[i][j + 2] == PLAYER_MOVE) && (board[i][j + 3] == PLAYER_MOVE)) {
                    health += 2000;
                }
                else if ((board[i][j] == PLAYER_MOVE) && (board[i][j + 1] == PLAYER_MOVE) 
                        && (board[i][j + 2] == PLAYER_MOVE) && (board[i][j + 3] == AI_MOVE)) {
                    health += 2000;
                }
                else if ((board[i][j] == PLAYER_MOVE) && (board[i][j + 1] == AI_MOVE) 
                        && (board[i][j + 2] == PLAYER_MOVE) && (board[i][j + 3] == PLAYER_MOVE)) {
                    health += 2000;
                }
                else if ((board[i][j] == PLAYER_MOVE) && (board[i][j + 1] == PLAYER_MOVE) 
                        && (board[i][j + 2] == AI_MOVE) && (board[i][j + 3] == PLAYER_MOVE)) {
                    health += 2000;
                }

                // If player blocks AI
                if ((board[i][j] == PLAYER_MOVE) && (board[i][j + 1] == AI_MOVE) 
                        && (board[i][j + 2] == AI_MOVE) && (board[i][j + 3] == AI_MOVE)) {
                    health -= 2000;
                }
                else if ((board[i][j] == AI_MOVE) && (board[i][j + 1] == AI_MOVE) 
                        && (board[i][j + 2] == AI_MOVE) && (board[i][j + 3] == PLAYER_MOVE)) {
                    health -= 2000;
                }
                else if ((board[i][j] == AI_MOVE) && (board[i][j + 1] == PLAYER_MOVE) 
                        && (board[i][j + 2] == AI_MOVE) && (board[i][j + 3] == AI_MOVE)) {
                    health -= 2000;
                }
                else if ((board[i][j] == AI_MOVE) && (board[i][j + 1] == AI_MOVE) 
                        && (board[i][j + 2] == PLAYER_MOVE) && (board[i][j + 3] == AI_MOVE)) {
                    health -= 2000;
                }
            }
        }

        // Check for block advantage vertically
	for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 5; j++) {
                if ((board[j][i] == AI_MOVE) && (board[j + 1][i] == PLAYER_MOVE) 
                        && (board[j + 2][i] == PLAYER_MOVE) && (board[j + 3][i] == PLAYER_MOVE)) {
                    health += 2000;
                }
                else if ((board[j][i] == PLAYER_MOVE) && (board[j + 1][i] == PLAYER_MOVE) 
                        && (board[j + 2][i] == PLAYER_MOVE) && (board[j + 3][i] == AI_MOVE)) {
                    health += 2000;
                }
                else if ((board[j][i] == PLAYER_MOVE) && (board[j + 1][i] == AI_MOVE) 
                        && (board[j + 2][i] == PLAYER_MOVE) && (board[j + 3][i] == PLAYER_MOVE)) {
                    health += 2000;
                }
                else if ((board[j][i] == PLAYER_MOVE) && (board[j + 1][i] == PLAYER_MOVE) 
                        &&(board[j + 2][i] == AI_MOVE) && (board[j + 3][i] == PLAYER_MOVE)) {
                    health += 2000;
                }

                else if ((board[j][i] == PLAYER_MOVE) && (board[j + 1][i] == AI_MOVE) 
                        && (board[j + 2][i] == AI_MOVE) && (board[j + 3][i] == AI_MOVE)) {
                    health -= 2000;
                }
                else if ((board[j][i] == AI_MOVE) && (board[j + 1][i] == AI_MOVE) 
                        &&(board[j + 2][i] == AI_MOVE) && (board[j + 3][i] == PLAYER_MOVE)) {
                    health -= 2000;
                }
                else if ((board[j][i] == AI_MOVE) && (board[j + 1][i] == PLAYER_MOVE) 
                        &&(board[j + 2][i] == AI_MOVE) && (board[j + 3][i] == AI_MOVE)) {
                    health -= 2000;
                }
                else if ((board[j][i] == AI_MOVE) && (board[j + 1][i] == AI_MOVE) 
                        && (board[j + 2][i] == PLAYER_MOVE) && (board[j + 3][i] == AI_MOVE)) {
                    health -= 2000;
                }
            }
        }
        
	return health;
    }

    public static void main(String args[])
    {
        Scanner kb = new Scanner(System.in);
        tictactoe game = new tictactoe();
        game.setup();
        game.printBoard();
        String goFirst;
        boolean playerTurn;
        System.out.print("Would you like to go first: ");
        goFirst = kb.nextLine();
        int timing;
        
        System.out.print("How long should the computer think about its moves <in second>: ");
        timing= kb.nextInt();
        
        if (goFirst.equals("y") || goFirst.equals("Y")) 
        {
            playerTurn = true;
            for(;;)
            {
                game.getMove();
                // Check User win ?
                if(game.checkGameOver() == VAL_PLAYER_WIN)
                    return;
                try 
                {
                    Thread.sleep(timing * 1000);
                } catch (Exception e) {}
                game.makeMove();
                // Check Computer win ?
                if(game.checkGameOver() == VAL_PLAYER_LOSE)
                 return;
            }
        }
        else
        {
            playerTurn = false;
            for(;;)
            {
                try
                {
                    Thread.sleep(timing * 1000);
                } catch (Exception e) {}
                game.makeMove();
                // Check User win ?
                if(game.checkGameOver() == VAL_PLAYER_LOSE)
                    return;
                game.getMove();
                // Check Computer win ?
                if(game.checkGameOver() == VAL_PLAYER_WIN)
                    return;
            }
        }
    }
}