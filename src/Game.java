import Coordinate.Coordinate;
import Players.Battleship;
import Players.Board;
import Players.Player;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    public void run(Player player, Player AI) {
        boolean gameRunning = true;
        Board playerHitBoard = new Board(new int[7][7]);
        while (gameRunning) {
            printBoards(player, playerHitBoard);
            playerShoot(AI, playerHitBoard);
            if (isDefeated(AI)) {
                System.out.println("You won the game");
                gameRunning = false;
            }
            AIShoot(player);
            if (isDefeated(player)) {
                System.out.println("You lost the game");
                gameRunning = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void printBoards(Player player, Board playerHitBoard) {
        String format = "%-40s%s%n";
        System.out.printf(format, player.getPlayerBoard(), "");
        System.out.printf(format, playerHitBoard, "");
//        System.out.println(player.getPlayerBoard() + "\t\t\t\t" + playerHitBoard);
    }

    private void playerShoot(Player AI, Board playerHitBoard) {
        Scanner scanner = new Scanner(System.in);
        int column = getColumn(scanner);
        int row = getRow(scanner);
        Coordinate coordinate = new Coordinate(row, column);
        if (isAIHit(coordinate, AI, playerHitBoard)) {
            for (Battleship battleship:AI.getBattleshipList()) {
                if (battleship.getCoordinates().stream().anyMatch(coordinate1 -> coordinate1.equals(coordinate))) {
                    isShipDestroyed(battleship, AI);
                    break;
                }
            }
        }
    }
    private void AIShoot(Player player) {
        Random random = new Random();
        int column = random.nextInt(7);
        int row = random.nextInt(7);
        Coordinate coordinate = new Coordinate(row, column);
        if (isPlayerHit(coordinate, player)) {
            for (Battleship battleship:player.getBattleshipList()) {
                if (battleship.getCoordinates().stream().anyMatch(coordinate1 -> coordinate1.equals(coordinate))) {
                    isShipDestroyed(battleship, player);
                    break;
                }
            }
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
    private boolean isAIHit(Coordinate coordinate, Player player, Board playerHitBoard) {
        int[][] playerBoard = player.getPlayerBoard().getBoard();
        if (playerBoard[coordinate.row()][coordinate.column()] == 1){
            player.getPlayerBoard().getBoard()[coordinate.row()][coordinate.column()] = 2;
            playerHitBoard.getBoard()[coordinate.row()][coordinate.column()] = 2;
            System.out.println("You hit!");
            return true;
        } else if (playerBoard[coordinate.row()][coordinate.column()] == 2) {
            System.out.println("You've already hit that!");
            return false;
        }
        System.out.println("You missed!");
        return false;
    }

    private boolean isPlayerHit(Coordinate coordinate, Player player) {
        int[][] playerBoard = player.getPlayerBoard().getBoard();
        if (playerBoard[coordinate.row()][coordinate.column()] == 1){
            player.getPlayerBoard().getBoard()[coordinate.row()][coordinate.column()] = 2;
            System.out.println("You've been hit in " + coordinate + "!");
            return true;
        } else if (playerBoard[coordinate.row()][coordinate.column()] == 2) {
            System.out.println("Your opponent already hit that!");
            return false;
        }
        System.out.println("Your opponent missed!");
        return false;
    }

    private void isShipDestroyed(Battleship battleship, Player player) {
        for (Coordinate coordinate:battleship.getCoordinates()) {
            if (player.getPlayerBoard().getBoard()[coordinate.row()][coordinate.column()] == 1) {
                return;
            }
        }
        battleship.setDestroyed(true);
        System.out.println("You destroyed a ship!");
    }

    private boolean isDefeated(Player player) {
        List<Battleship> destroyedBattleships = player.getBattleshipList().stream().filter(Battleship::isDestroyed).toList();
        return destroyedBattleships.size() == 5;
    }
}
