import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/* The board will be organized in a fashion such that the top left and bottom right corners are
   light-colored and the top right and bottom left corners are dark-colored. Each player starts
   with 12 pieces as is standard in checkers. They will "spawn" in on all the dark-colored squares
   of the bottom and top 3 rows.
   0 = empty square
   1 = regular light piece; 2 = crowned light piece
   3 = regular dark piece; 4 = crowned dark piece
   This class handles the actual game state of the board.

   0 1 2 3 4 5 6 7 <- player 1 side
   1
   2
   3
   4
   5
   6
   7               <- player 2 side

 */
public class Board {

    private int[][] arr;
    private Deque < Move > moves;
    private boolean playerTurn; // true = player 1; false = player 2
    private int whiteNum;
    private int blackNum;
    private int turnsNum;
    private boolean gameOver;

    private static final int EMPTY = 0;
    private static final int WHITE_PIECE = 1;
    private static final int BLACK_PIECE = 3;
    private static final int BOARD_SIZE = 8;


    public Board() {
        this.init();
    }

    public Board(int[][] initB, boolean initT) {
        this.arr = copyBoard(initB);
        this.moves = new ArrayDeque < > ();
        this.playerTurn = initT;
        this.turnsNum = 0;
        calcPieces();
        this.gameOver = isGameOver();
    }

    private int[][] copyBoard(int[][] originalBoard) {
        int[][] copiedBoard = new int[8][8];

        for (int i = 0; i < 8; i++) {
            System.arraycopy(originalBoard[i], 0, copiedBoard[i], 0, 8);
        }

        return copiedBoard;
    }

    private void calcPieces() {
        for (int[] row: this.arr) {
            for (int piece: row) {
                if (piece == 1 || piece == 2) {
                    this.whiteNum++;
                } else if (piece == 3 || piece == 4) {
                    this.blackNum++;
                }
            }
        }
    }

    public void init() {
        this.arr = new int[8][8];
        this.playerTurn = true;
        this.turnsNum = 0;
        this.whiteNum = 12;
        this.blackNum = 12;
        this.moves = new ArrayDeque < > ();
        this.gameOver = false;

        initializeBoardState();
    }

    private void initializeBoardState() {
        for (int i = 1; i < 8; i += 2) {
            this.arr[0][i] = WHITE_PIECE;
            this.arr[2][i] = WHITE_PIECE;
            this.arr[6][i] = BLACK_PIECE;
        }

        for (int i = 0; i < 8; i += 2) {
            this.arr[1][i] = WHITE_PIECE;
            this.arr[5][i] = BLACK_PIECE;
            this.arr[7][i] = BLACK_PIECE;
        }
    }


    // This function just gives you the value of a certain index in the 2-D array.
    public int getPiece(int row, int col) {
        if (row > 7 || col > 7 || row < 0 || col < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return this.arr[row][col];
    }

    public LinkedList < Coord > validMoves(Coord curr) {
        LinkedList < Coord > out = new LinkedList < > ();

        if (!gameOver) {
            int piece = this.arr[curr.getRow()][curr.getCol()];

            if (piece == 0) {
                throw new IllegalCallerException("Üres mező");
            } else if (this.playerTurn && piece >= 3) {
                throw new IllegalCallerException("Illegális lépés");
            } else if (!this.playerTurn && piece <= 2) {
                throw new IllegalCallerException("Illegális lépés");
            }

            switch (piece) {
                case 1:
                case 2:
                    checkMove(out, curr, 1, -1); // down left
                    checkMove(out, curr, 1, 1); // down right

                    if (piece == 2) {
                        checkMove(out, curr, -1, -1); // up left
                        checkMove(out, curr, -1, 1); // up right
                    }

                    break;
                case 3:
                case 4:
                    checkMove(out, curr, -1, -1); // up left
                    checkMove(out, curr, -1, 1); // up right

                    if (piece == 4) {
                        checkMove(out, curr, 1, -1); // down left
                        checkMove(out, curr, 1, 1); // down right
                    }

                    break;
            }
        }

        return out;
    }

    // A helper function that checks if a coordinate is within the board boundaries and returns the value at that coordinate
    private int getValue(Coord coord) {
        int row = coord.getRow();
        int col = coord.getCol();

        if (row < 0 || row >= this.arr.length || col < 0 || col >= this.arr[0].length) {
            return -1; // out of bounds
        }

        return this.arr[row][col];
    }

    // A helper function that checks if there is an empty or opponent square in a given direction and adds it to the possible moves list
    private void checkMove(LinkedList < Coord > out, Coord curr, int dr, int dc) {
        int row = curr.getRow() + dr;
        int col = curr.getCol() + dc;
        int value = getValue(new Coord(row, col));

        if (value == 0) {
            // empty square
            out.add(new Coord(row, col));
        } else if (this.playerTurn && value >= 3 || !this.playerTurn && value <= 2) {
            // opponent square
            row += dr;
            col += dc;
            value = getValue(new Coord(row, col));

            if (value == 0) {
                // empty square after opponent square
                out.add(new Coord(row, col));
            }
        }
    }

    // Undoes the last move. If no moves have been played yet, then it does nothing.
    public void undo() {
        if (this.moves.isEmpty()) {
            return;
        }

        Move move = this.moves.pop();
        Coord start = move.getStart();
        Coord end = move.getEnd();

        if (move.getKinged()) {
            this.arr[start.getRow()][start.getCol()] = this.arr[end.getRow()][end.getCol()] - 1;
        } else {
            this.arr[start.getRow()][start.getCol()] = this.arr[end.getRow()][end.getCol()];
        }

        this.arr[end.getRow()][end.getCol()] = 0;

        if (move.getCaptured() != -1) {
            this.arr[(start.getRow() + end.getRow()) / 2][(start.getCol() + end.getCol()) / 2] = move.getCaptured();
        }

        this.playerTurn = !this.playerTurn;
        this.turnsNum--;

        if (this.gameOver) {
            this.gameOver = false;
        }
    }

    public boolean playMove(Coord start, Coord end) {

        boolean king = false;
        boolean jump = false;
        int captured = -1;

        if (!validMoves(start).contains(end)) {
            System.out.println("Nem jó lépés");
            return false;
        }

        int piece = this.arr[start.getRow()][start.getCol()];

        // Check if the move is a jump by comparing the start and end coordinates
        if (Math.abs(start.getRow() - end.getRow()) == 2 && Math.abs(start.getCol() - end.getCol()) == 2) {
            jump = true;
        }

        // Move the piece from the start to the end coordinate
        this.arr[start.getRow()][start.getCol()] = 0;
        this.arr[end.getRow()][end.getCol()] = piece;

        // If the move was a jump, capture the opponent piece and update the piece counts
        if (jump) {
            captured = this.arr[(start.getRow() + end.getRow()) / 2][(start.getCol() + end.getCol()) / 2];
            this.arr[(start.getRow() + end.getRow()) / 2][(start.getCol() + end.getCol()) / 2] = 0;
            updatePieceCounts(captured);
        }

        // If the piece reached the opposite end of the board, make it a king
        if (piece == 1 && end.getRow() == BOARD_SIZE - 1) {
            this.arr[end.getRow()][end.getCol()] = 2;
            king = true;
        } else if (piece == 3 && end.getRow() == 0) {
            this.arr[end.getRow()][end.getCol()] = 4;
            king = true;
        }

        // Switch the player turn and increment the number of turns
        this.playerTurn = !this.playerTurn;
        this.turnsNum++;
        // Push the move to the stack
        this.moves.push(new Move(start, end, captured, king));

        // Check if the game is over and set the gameOver flag
        if (this.isGameOver()) {
            System.out.println("Game Over");
            this.gameOver = true;
        }

        return true;
    }

    // A helper function that updates the black and white piece counts based on the captured piece
    private void updatePieceCounts(int captured) {
        if (captured == 1 || captured == 2) {
            this.whiteNum--;
        } else if (captured == 3 || captured == 4) {
            this.blackNum--;
        }
    }

    // Checks if the game is over.
    public boolean isGameOver() {
        if (this.whiteNum == 0 || this.blackNum == 0) {
            gameOver = true;
            return true;
        }

        Boolean out = true;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                try {
                    if (!validMoves(new Coord(i, j)).isEmpty()) {
                        gameOver = false;
                        out = false;
                    }
                } catch (Exception ignored) {}
            }
        }

        gameOver = out;
        return out;
    }

    public boolean getPlayerTurn() {
        return this.playerTurn;
    }
    
    // Returns the array of the board. For testing purposes
    public int[][] getBoard() {
        int[][] copy = new int[8][8];

        for (int i = 0; i < 8; i++) {
            copy[i] = this.arr[i];
        }

        return copy;
    }

    // Saves the state of the game to a file.
    public void saveGame(File file) {
        // line 1 = player turn, line 2-9 = array
        BufferedWriter bw = null;
        FileWriter fw;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file, false);
            bw = new BufferedWriter(fw);

            if (this.playerTurn) {
                // saves player turn
                bw.write("1");
            } else {
                bw.write("0");
            }

            bw.newLine();

            for (int i = 0; i < 8; i++) {
                bw.write(Arrays.toString(this.arr[i])); // saves array state
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // Lets players load saved games from files.
    public void loadGame(File file) {
        init();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (IOException e) {
            throw new IllegalArgumentException("File does not exist.");
        }

        try {
            if (Integer.valueOf(reader.readLine()) == 0) {
                this.playerTurn = false;
            }

            for (int i = 0; i < 8; i++) {
                StringBuilder builder = new StringBuilder();
                builder.append(reader.readLine());
                this.arr[i][0] = builder.charAt(1) - 48;
                this.arr[i][1] = builder.charAt(4) - 48;
                this.arr[i][2] = builder.charAt(7) - 48;
                this.arr[i][3] = builder.charAt(10) - 48;
                this.arr[i][4] = builder.charAt(13) - 48;
                this.arr[i][5] = builder.charAt(16) - 48;
                this.arr[i][6] = builder.charAt(19) - 48;
                this.arr[i][7] = builder.charAt(22) - 48;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal file passed");
        }
    }

   
}