import Coordinate.Coordinate;
import Players.Battleship;
import Players.Player;

import java.util.List;
import java.util.Scanner;

public class Game {

    public void run(Player player, Player AI) {
        boolean gameRunning = true;
        playerShoot(AI);
//        while (gameRunning) {
//            if (isDefeated(player)) {
//                System.out.println("You lost the game");
//                gameRunning = false;
//            }
//            if (isDefeated(AI)) {
//                System.out.println("You won the game");
//                gameRunning = false;
//            }
//        }
    }

    private void playerShoot(Player AI) {
        Scanner scanner = new Scanner(System.in);
        int column = getColumn(scanner);
        int row = getRow(scanner);
        if (isHit(new Coordinate(row, column), AI)) {
            System.out.println("You hit!");
        } else {
            System.out.println("You missed!");
        }
    }
    /**
     * Gets the column where the human player wants to shoot
     * @param scanner The scanner for reading the console input
     * @return Returns the column as an integer
     */
    private int getColumn(Scanner scanner) {
        boolean inputMatch = false;
        int columnInt = 0;
        while (!inputMatch) {
            System.out.println("Write in what column you want to shoot. A number from 1 to 7");
            String column = scanner.nextLine();
            try {
                columnInt = Integer.parseInt(column) - 1;
                if (columnInt <= 7) {
                    inputMatch = true;
                } else {
                    System.out.println("You didn't write a number between 1 and 7");
                }
            } catch (Exception e) {
                System.out.println("You didn't write a number!");
            }
        }
        return columnInt;
    }

    /**
     * Gets the row where the human player wants to place their ship
     * @param scanner The scanner for reading the console input
     * @return Returns the row as an integer
     */
    private int getRow(Scanner scanner) {
        int rowInt = 0;
        boolean inputMatch = false;
        while (!inputMatch) {
            System.out.println("Write in what row you want to shoot. A number from 1 to 7");
            String row = scanner.nextLine();
            try {
                rowInt = Integer.parseInt(row) - 1;
                if (rowInt <= 7) {
                    inputMatch = true;
                } else {
                    System.out.println("You didn't choose a number between 1 and 7");
                }
            } catch (Exception e) {
                System.out.println("You didn't write a number!");
            }
        }
        return rowInt;
    }
    private boolean isHit(Coordinate coordinate, Player player) {
        int[][] playerBoard = player.getPlayerBoard().getBoard();
        if (playerBoard[coordinate.row()][coordinate.column()] == 1){
            player.getPlayerBoard().getBoard()[coordinate.row()][coordinate.column()] = 2;
            return true;
        }
        return false;
    }

    private boolean isShipDestroyed(Battleship battleship, Player player) {
        List<Coordinate> coordinates = battleship.getCoordinates();
        for (Coordinate coordinate:battleship.getCoordinates()) {
            if (player.getPlayerBoard().getBoard()[coordinate.row()][coordinate.column()] == 1) {
                return false;
            }
        }
        battleship.setDestroyed(true);
        return true;
    }

    private boolean isDefeated(Player player) {
        List<Battleship> destroyedBattleships = player.getBattleshipList().stream().filter(Battleship::isDestroyed).toList();
        return destroyedBattleships.size() == 5;
    }
}
