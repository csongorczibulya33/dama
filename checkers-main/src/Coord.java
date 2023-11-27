public class Coord {

    private int row;
    private int col;

    public Coord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coord that = (Coord) o;
        return row == that.row && col == that.col;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
            "row=" + row +
            ", col=" + col +
            '}';
    }
}
