
public class Move {
    private final Coord start;
    private final Coord end;
    private final int capture;
    private boolean kinged;

    public Move(int startRow, int startCol, int endRow, int endCol) {
        this.start = new Coord(startRow, startCol);
        this.end = new Coord(endRow, endCol);
        this.capture = -1;
        this.kinged = false;
    }

    public Move(Coord start, Coord end) {
        this.start = start;
        this.end = end;
        this.capture = -1;
        this.kinged = false;
    }

    public Move(Coord start, Coord end, int captured) {
        this.start = start;
        this.end = end;
        this.capture = captured;
        this.kinged = false;
    }

    public Move(int startRow, int startCol, int endRow, int endCol, int captured) {
        this.start = new Coord(startRow, startCol);
        this.end = new Coord(endRow, endCol);
        this.capture = captured;
        this.kinged = false;
    }

    public Move(Coord start, Coord end, int captured, boolean kinged) {
        this.start = start;
        this.end = end;
        this.capture = captured;
        this.kinged = kinged;
    }

    public Move(int startRow, int startCol, int endRow, int endCol, int captured, boolean kinged) {
        this.start = new Coord(startRow, startCol);
        this.end = new Coord(endRow, endCol);
        this.capture = captured;
        this.kinged = kinged;
    }

    public Coord getStart() {
        return this.start;
    }

    public Coord getEnd() {
        return this.end;
    }

    public int getCaptured() {
        return this.capture;
    }

    public boolean getKinged() {
        return this.kinged;
    }

    @Override
    public String toString() {
        return "Move{" +
            "start=" + start +
            ", end=" + end +
            ", capture=" + capture +
            '}';
    }
}
