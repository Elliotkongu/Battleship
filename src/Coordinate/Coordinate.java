package Coordinate;

public record Coordinate(int row, int column, Status status) {
    @Override
    public String toString() {
        return row + ", " + column;
    }
}
