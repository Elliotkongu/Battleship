package Util;

public class Coordinate {

    private final int column;
    private final int row;
    private Status status;

    public Coordinate(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public Coordinate(int column, int row, Status status) {
        this.column = column;
        this.row = row;
        this.status = status;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        return alphabet[column] + ", " + (row+1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Coordinate.class) {
            return ((Coordinate) obj).row == row && ((Coordinate) obj).column == column;
        }
        return false;
    }
}
