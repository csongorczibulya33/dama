import java.util.List;

public class Pair {
    private int score;
    private List<Coord> move;

    public Pair(int score, List<Coord> move) {
        this.score = score;
        this.move = move;
    }

    public int getScore() {
        return this.score;
    }

    public List<Coord> getMove() {
        return this.move;
    }
}
