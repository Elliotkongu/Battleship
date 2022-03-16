package Util;

public record Coordinate(int row, int column) {
    @Override
    public String toString() {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        return alphabet[column] + ", " + (row+1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Coordinate.class) {
            return ((Coordinate) obj).row() == row && ((Coordinate) obj).column() == column;
        }
        return false;
    }
}
