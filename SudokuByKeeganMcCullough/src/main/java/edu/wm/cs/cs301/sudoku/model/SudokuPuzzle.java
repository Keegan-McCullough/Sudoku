package edu.wm.cs.cs301.sudoku.model;
import java.util.Random;


public class SudokuPuzzle {
    public static final int NUM_ROWS = 9;
    public static final int NUM_COLS = 9;

    public static final int EMPTY = 0;

    public static String[] originalSpaces = new String[40];

    // the current version of the puzzle including valid player moves
    private final int[][] current = new int[NUM_ROWS][NUM_COLS];

    // the solution to the current puzzle
    private final int[][] solution = new int[NUM_ROWS][NUM_COLS];

    public static int counter = 0;

    public static final String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
    public static int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    private SudokuResponse[][] sudokuGrid;

    public SudokuPuzzle() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                solution[i][j] = EMPTY;
                current[i][j] = EMPTY;
            }
        }
        fillGrid(solution);
        fillSpaces(current, solution);
        getSolution();
        getCurrent();
        this.sudokuGrid = initializeSudokuGrid();
    }

    public int[][] getCurrent() {
        return current;
    }

    public int[][] getSolution() {
        return solution;
    }

    public void initialize() {
        this.sudokuGrid = initializeSudokuGrid();
    }


    public static boolean validate(String move, int[][] board) {
        // Flags to see the string inputted is acceptable
        int check1 = 0;
        int check2 = 0;
        int check3 = 0;

        String[] spaces = move.split("");

        // Checking that all characters are numbers
        if (Character.isDigit(move.charAt(0))) {
            check1++;
        }
        if (Character.isDigit(move.charAt(1))) {
            check2++;
        }
        if (Character.isDigit(move.charAt(2))) {
            check3++;
        }

        // If pass the checks and a valid move place the digit
        if (check1 > 0 && check2 > 0 && check3 > 0) {
            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < NUM_COLS; j++) {
                    int row = Integer.parseInt(spaces[0]);
                    int col = Integer.parseInt(spaces[1]);
                    int number = Integer.parseInt(spaces[2]);

                    for (String position : originalSpaces){
                        if (position.equals(spaces[0]+spaces[1])){
                            return false;
                        }
                    }
                    if (i == row && j == col) {
                        // tests for if the number violates sudoku rules
                        if (number == EMPTY && board[i][j] == EMPTY){
                            return false;
                        }else if(number == EMPTY){
                            board[i][j] = EMPTY;
                            return true;
                        } else if(isSafe(board, i, j, number)){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }


    private static boolean isSafe(int[][] board, int row, int col, int number) {

        // row rule
        for (int c = 0; c < NUM_COLS; c++ ) {
            if (number == board[row][c]) {
                return false;
            }
        }

        // column rule
        for (int r = 0; r < NUM_ROWS; r++) {
            if (number == board[r][col]) {
                return false;
            }
        }

        // square rule
        // find position in square
        int rowSpot = row % 3;
        int columnSpot = col % 3;

        // find the positions above, below, and left and right of our current spot
        int upperRow = cap(rowSpot + 1);
        int lowerRow = cap(rowSpot - 1);
        int upperColumn = cap(columnSpot + 1);
        int lowerColumn = cap(columnSpot - 1);

        // this figures out which sub-square we are working with
        int rowOffset = row / 3;
        int columnOffset = col / 3;

        // calculate which neighbors in our sub-square needed to be tested
        upperRow = upperRow + 3 * rowOffset;
        lowerRow = lowerRow + 3 * rowOffset;
        upperColumn = upperColumn + 3 * columnOffset;
        lowerColumn = lowerColumn + 3 * columnOffset;

        // checking if the number is in the square
        if (number == board[upperRow][upperColumn]){return false;}
        if (number == board[upperRow][lowerColumn]){return false;}
        if (number == board[lowerRow][upperColumn]){return false;}
        if (number == board[lowerRow][lowerColumn]){return false;}

        board[row][col] = number;
        return true;
    }

    // cap function helps eliminate code by identifying the actual neighbors in the sub-square
    public static int cap(int val) {
        if (val < 0) {
            val = 2;
        } else if (val > 2) {
            val = 0;
        }
        return val;
    }

    public static boolean checkGrid(int[][] board){
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (board[i][j] == EMPTY){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean fillGrid(int[][] board) {
        Random random = new Random();
        String test;
        // loop to go through every spot in the puzzle
        for (int i = 0; i < 81; i++){
            int row = i / 9;
            int col = i % 9;
            if (board[row][col] == EMPTY){
                // starting at a random number to see if it can fill the current space
                int value = random.nextInt(9) + 1;
                for (int len = 0; len < numbers.length; len++) {
                    if (value > 9){
                        value = 0;
                    }
                    if (isSafe(board, row, col, value)) {
                        board[row][col] = value;
                        if (checkGrid(board)) {
                            return true;
                        } else {
                            if (fillGrid(board)) {
                                return true;
                            }
                            // necessary for backtracking if no number works reset the space to 0
                            board[row][col] = EMPTY;
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }

    // used to fill current board with numbers from solution
    public static int[][] fillSpaces(int[][] current, int[][] solution){
        Random random = new Random();
        // loops until originalSpaces the array holding the numbers filled by solution is filled
        for(int i = 0; i < originalSpaces.length; i++){
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            if (current[row][col] == EMPTY){
                current[row][col] = solution[row][col];
                originalSpaces[i] = letters[row] + letters[col];
            }else{
                i--;
            }
        }
        // checks to see if current can only be solved by the solution
        isUnique(current);
        if(counter == 1) {
            return current;
        }
        // if not unique current is emptied and function recurs until its unique
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                current[i][j] = EMPTY;
                counter = 0;
            }
        }
        fillSpaces(current, solution);
        return current;
    }

    private static boolean isUnique(int[][] current) {
        int[][] board = new int[NUM_ROWS][NUM_COLS];
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                board[i][j] = current[i][j];
            }
        }
        // loop to go through every spot in the puzzle
        for (int i = 0; i < 81; i++){
            int row = i / 9;
            int col = i % 9;
            if (board[row][col] == EMPTY){
                for (int value = 1; value < 10; value++ ) {
                    if (isSafe(board, row, col, value)) {
                        board[row][col] = value;
                        if (checkGrid(board)) {
                            counter++;
                            if (counter > 1){
                                return true;
                            }
                            break;
                        } else {
                            if (isUnique(board)) {
                                return true;
                            }
                            // necessary for backtracking if no number works reset the space to 0
                            board[row][col] = EMPTY;
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }
    
    private SudokuResponse[][] initializeSudokuGrid() {
        SudokuResponse[][] SudokuGrid = new SudokuResponse[NUM_ROWS][NUM_COLS];
        for (int row = 0; row < SudokuGrid.length; row++) {
            for (int column = 0; column < SudokuGrid[row].length; column++) {
                SudokuGrid[row][column] = null;
            }
        }
        return SudokuGrid;
    }

    public SudokuResponse[][] getSudokuGrid() {
        return sudokuGrid;
    }

    public int getNumRows(){return NUM_ROWS;}

    public int getNumCols(){return NUM_COLS;}
}