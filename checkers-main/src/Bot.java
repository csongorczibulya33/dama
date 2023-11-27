import java.util.ArrayList;
import java.util.List;

public class Bot {

    private List<List<Coord>> getMoves(int[][] mat, boolean turn) {
        Board board = new Board(mat, turn);
        List<List<Coord>> moves = new ArrayList<>();
        if (turn) { // player's turn
            for (int i = 0; i < 8 ; i++) {
                for (int j = 0; j < 8; j++) {
                    int piece = board.getPiece(j, i);
                    if (piece == 1 || piece == 2) {
                        List<Coord> possibilities = board.validMoves(new Coord(j, i));
                        for (Coord c : possibilities) {
                            List<Coord> toAdd = new ArrayList<>(2);
                            toAdd.add(new Coord(j, i));
                            toAdd.add(c);
                            moves.add(toAdd);
                        }
                    }
                }
            }
        } else { // bot's turn
            for (int i = 0; i < 8 ; i++) {
                for (int j = 0; j < 8; j++) {
                    int piece = board.getPiece(j, i);
                    if (piece == 3 || piece == 4) {
                        List<Coord> possibilities = board.validMoves(new Coord(j, i));
                        for (Coord c : possibilities) {
                            List<Coord> toAdd = new ArrayList<>(2);
                            toAdd.add(new Coord(j, i));
                            toAdd.add(c);
                            moves.add(toAdd);
                        }
                    }
                }
            }
        }
        return moves;
    }

    private boolean isGameOver(int[][] mat, boolean turn) {
        Board board = new Board(mat, turn);
        return board.isGameOver();
    }

    // A helper function that returns a random move for the bot
    public List<Coord> getRandomMove(int[][] mat, boolean turn) {
       // Get all the possible moves for the bot
       List<List<Coord>> moves = this.getMoves(mat, turn);
       // If there are no moves, return null
       if (moves.isEmpty()) {
        return null;
       }
        // Pick a random index from 0 to the size of the moves list
        int index = (int) (Math.random() * moves.size());
        // Return the move at that index
        return moves.get(index);
}

}
