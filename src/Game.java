import Players.AIAlgorithm;
import Util.Coordinate;
import Players.Battleship;
import Players.Board;
import Players.Player;
import Util.Status;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    public void run(Player player, Player AI) {
        Board playerHitBoard = new Board(new int[7][7]);
        AIAlgorithm aiAlgorithm = new AIAlgorithm();
        while (true) {
            printBoards(player, playerHitBoard);
            playerShoot(AI, playerHitBoard);
            if (isDefeated(AI)) {
                System.out.println("You won the game");
                break;
            }
            AIShoot(player, aiAlgorithm);
            if (isDefeated(player)) {
                System.out.println("You lost the game");
                break;
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
    }

    private void playerShoot(Player AI, Board playerHitBoard) {
        Scanner scanner = new Scanner(System.in);
        boolean retry = true;
        while (retry) {
            int column = getColumn(scanner);
            int row = getRow(scanner);
            Coordinate coordinate = new Coordinate(row, column);
            Status status = isAIHit(coordinate, AI, playerHitBoard);
            if (status.equals(Status.HIT)) {
                for (Battleship battleship : AI.getBattleshipList()) {
                    if (battleship.getCoordinates().stream().anyMatch(coordinate1 -> coordinate1.equals(coordinate))) {
                        isShipDestroyed(battleship, AI);
                        retry = false;
                        break;
                    }
                }
            } else if (status.equals(Status.MISSED)) {
                retry = false;
            }
        }
    }

    private void AIShoot(Player player, AIAlgorithm aiAlgorithm) {
        Random random = new Random();
        boolean retry = true;
        while (retry) {
            int column = random.nextInt(7);
            int row = random.nextInt(7);
            Coordinate coordinate = new Coordinate(row, column);
            retry = isHitOrMiss(player, aiAlgorithm, coordinate);
        }
    }

    private boolean isHitOrMiss(Player player, AIAlgorithm aiAlgorithm, Coordinate coordinate) {
        boolean retry = true;
        Status status = isPlayerHit(coordinate, player);
        coordinate.setStatus(status);
        aiAlgorithm.addShot(coordinate);
        if (status.equals(Status.HIT)) {
            for (Battleship battleship : player.getBattleshipList()) {
                if (battleship.getCoordinates().stream().anyMatch(coordinate1 -> coordinate1.equals(coordinate))) {
                    isPlayerShipDestroyed(battleship, player, aiAlgorithm);
                    retry = false;
                    break;
                }
            }
        } else if (status.equals(Status.MISSED)) {
            retry = false;
        }
        return retry;
    }

    /**
     * Gets the column where the human player wants to shoot
     *
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
     *
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

    /**
     * Checks whether the coordinate is a hit, miss or already hit
     *
     * @param coordinate     The coordinate to be checked
     * @param AI             The player object representing the AI
     * @param playerHitBoard The board where the human player can see where they've hit
     * @return The status of the shot: HIT, MISSED or ALREADY_HIT
     */
    private Status isAIHit(Coordinate coordinate, Player AI, Board playerHitBoard) {
        int[][] playerBoard = AI.getPlayerBoard().getBoard();
        if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 1) {
            AI.getPlayerBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] = 2;
            playerHitBoard.getBoard()[coordinate.getRow()][coordinate.getColumn()] = 2;
            System.out.println("You hit!");
            return Status.HIT;
        } else if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 2) {
            System.out.println("You've already hit that!");
            return Status.ALREADY_HIT;
        }
        System.out.println("You missed!");
        return Status.MISSED;
    }

    private Status isPlayerHit(Coordinate coordinate, Player player) {
        int[][] playerBoard = player.getPlayerBoard().getBoard();
        System.out.println("[DEBUG] Coordinate: " + coordinate);
        if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 1) {
            player.getPlayerBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] = 2;
            System.out.println("You've been hit in " + coordinate + "!");
            return Status.HIT;
        } else if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 2) {
            return Status.ALREADY_HIT;
        }
        System.out.println("Your opponent missed!");
        return Status.MISSED;
    }

    private void isShipDestroyed(Battleship battleship, Player player) {
        for (Coordinate coordinate : battleship.getCoordinates()) {
            if (player.getPlayerBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] == 1) {
                return;
            }
        }
        battleship.setDestroyed(true);
        System.out.println("You destroyed the " + battleship + "!");
    }

    private void isPlayerShipDestroyed(Battleship battleship, Player player, AIAlgorithm aiAlgorithm) {
        for (Coordinate coordinate : battleship.getCoordinates()) {
            if (player.getPlayerBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] == 1) {
                return;
            }
        }
        battleship.setDestroyed(true);
        aiAlgorithm.reset();
        System.out.println("Your " +  battleship + " has been destroyed");
    }

    private boolean isDefeated(Player player) {
        List<Battleship> destroyedBattleships = player.getBattleshipList().stream().filter(Battleship::isDestroyed).toList();
        return destroyedBattleships.size() == 5;
    }
}
