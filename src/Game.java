import Players.*;
import Util.Coordinate;
import Util.Status;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private final HumanPlayer humanPlayer;
    private final AiPlayer aiPlayer;

    public Game(HumanPlayer humanPlayer, AiPlayer aiPlayer) {
        this.humanPlayer = humanPlayer;
        this.aiPlayer = aiPlayer;
    }

    public void run() {
        AIAlgorithm aiAlgorithm = new AIAlgorithm();
        while (true) {
            printBoards(humanPlayer);
            playerShoot();
            if (isDefeated(aiPlayer)) {
                System.out.println("You won the game");
                break;
            }
            AIShoot(aiAlgorithm);
            if (isDefeated(humanPlayer)) {
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

    private void printBoards(Player player) {
        String format = "%-40s%s%n";
        System.out.printf(format, player.getPlayerBoard(), "");
        System.out.printf(format, player.getHitBoard(), "");
    }

    private void playerShoot() {
        Scanner scanner = new Scanner(System.in);
        boolean retry = true;
        while (retry) {
            int column = getColumn(scanner);
            int row = getRow(scanner);
            Coordinate coordinate = new Coordinate(row, column);
            Status status = isAiHit(coordinate, aiPlayer, humanPlayer.getHitBoard());
            if (status.equals(Status.HIT)) {
                for (Battleship battleship : aiPlayer.getBattleshipList()) {
                    if (battleship.getCoordinates().stream().anyMatch(coordinate1 -> coordinate1.equals(coordinate))) {
                        isAIShipDestroyed(battleship, aiPlayer);
                        retry = false;
                        break;
                    }
                }
            } else if (status.equals(Status.MISSED)) {
                retry = false;
            }
        }
    }

    private void AIShoot(AIAlgorithm aiAlgorithm) {
        Random random = new Random();
        boolean retry = true;
        while (retry) {
            int column = random.nextInt(7);
            int row = random.nextInt(7);
            Coordinate coordinate = new Coordinate(row, column);
            if (aiAlgorithm.getPreviousShots().stream().anyMatch(coordinate1 -> coordinate1.equals(coordinate))) {
                continue;
            }
            coordinate.setStatus(isHitOrMiss(aiAlgorithm, coordinate));
            if (coordinate.getStatus().equals(Status.HIT) || coordinate.getStatus().equals(Status.MISSED)) {
                aiAlgorithm.addShot(coordinate);
                retry = false;
            }
        }
    }

    private Status isHitOrMiss(AIAlgorithm aiAlgorithm, Coordinate coordinate) {
        Status status = isPlayerHit(coordinate, humanPlayer);
        coordinate.setStatus(status);
        aiAlgorithm.addShot(coordinate);
        if (status.equals(Status.HIT)) {
            for (Battleship battleship : humanPlayer.getBattleshipList()) {
                if (battleship.getCoordinates().stream().anyMatch(coordinate1 -> coordinate1.equals(coordinate))) {
                    isPlayerShipDestroyed(battleship, humanPlayer, aiAlgorithm);
                    break;
                }
            }
        }
        return status;
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
     * @param ai             The player object representing the AI
     * @param playerHitBoard The board where the human player can see where they've hit
     * @return The status of the shot: HIT, MISSED or ALREADY_HIT
     */
    private Status isAiHit(Coordinate coordinate, Player ai, Board playerHitBoard) {
        int[][] playerBoard = ai.getPlayerBoard().getBoard();
        if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 1) {
            ai.getPlayerBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] = 2;
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

    private void isAIShipDestroyed(Battleship battleship, AiPlayer ai) {
        if (isShipDestroyed(battleship, ai)) {
            for (Coordinate c: battleship.getCoordinates()) {
                humanPlayer.getHitBoard().getBoard()[c.getRow()][c.getColumn()] = 3;
            }
            System.out.println("You destroyed the " + battleship + "!");
        }
    }

    private void isPlayerShipDestroyed(Battleship battleship, HumanPlayer player, AIAlgorithm aiAlgorithm) {
        if (isShipDestroyed(battleship, player)) {
            aiAlgorithm.reset();
            System.out.println("Your " + battleship + " has been destroyed");
        }
    }

    private boolean isShipDestroyed(Battleship battleship, Player player) {
        for (Coordinate coordinate : battleship.getCoordinates()) {
            if (player.getPlayerBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] == 1) {
                return false;
            }
        }
        battleship.setDestroyed(true);
        for (Coordinate c : battleship.getCoordinates()) {
            c.setStatus(Status.DESTROYED);
            player.getPlayerBoard().getBoard()[c.getRow()][c.getColumn()] = 3;
        }
        return true;
    }

    private boolean isDefeated(Player player) {
        List<Battleship> destroyedBattleships = player.getBattleshipList().stream().filter(Battleship::isDestroyed).toList();
        return destroyedBattleships.size() == 5;
    }
}
