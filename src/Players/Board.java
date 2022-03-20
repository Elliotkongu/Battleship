package Players;

import Util.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Board {
    int[][] board;

    public Board(int[][] board) {
        this.board = board;
    }

    public boolean addBattleship(int column, int row, int length, String direction, Battleship battleship) {
        List<Coordinate> battleshipCoordinates = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (direction.equalsIgnoreCase("left") && isNotOccupied(column - i, row)) {
                battleshipCoordinates.add(new Coordinate(row, column - i));
            } else if (direction.equalsIgnoreCase("right") && isNotOccupied(column + i, row)) {
                battleshipCoordinates.add(new Coordinate(row, column + i));
            } else if (direction.equalsIgnoreCase("up") && isNotOccupied(column, row - i)) {
                battleshipCoordinates.add(new Coordinate(row - i, column));
            } else if (direction.equalsIgnoreCase("down") && isNotOccupied(column, row + i)) {
                battleshipCoordinates.add(new Coordinate(row + i, column));
            } else {
                return true;
            }
        }
        for (Coordinate coordinate : battleshipCoordinates) {
            board[coordinate.getRow()][coordinate.getColumn()] = 1;
        }
        battleship.setCoordinates(battleshipCoordinates);
        return false;
    }

    public int[][] getBoard() {
        return board;
    }

    private boolean isNotOccupied(int column, int row) {
        return board[row][column] != 1;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        stringBuilder.append("  ");
        for (int i = 0; i < board.length; i++) {
            stringBuilder.append(alphabet[i]).append("|");
        }
        stringBuilder.append("\n");
        for (int i = 0; i < board.length; i++) {
            stringBuilder.append(i + 1).append("|");
            for (int integer : board[i]) {
                switch (integer) {
                    case 0 -> stringBuilder.append("O|");
                    case 1 -> stringBuilder.append("S|");
                    case 2 -> stringBuilder.append("H|");
                    case 3 -> stringBuilder.append("D|");
                    case 4 -> stringBuilder.append("X|");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
