package Coordinate;

public record Coordinate(int row, int column) {
    @Override
    public String toString() {
        return row + ", " + column;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Coordinate.class) {
            return ((Coordinate) obj).row() == row && ((Coordinate) obj).column() == column;
        }
        return false;
    }
}
