import Players.*;
import Util.Coordinate;
import Util.Status;

import java.util.*;

public class Game {

    private final HumanPlayer humanPlayer;
    private final AiPlayer aiPlayer;
    private final int boardHeight;
    private final int boardWidth;

    public Game(HumanPlayer humanPlayer, AiPlayer aiPlayer, int boardHeight, int boardWidth) {
        this.humanPlayer = humanPlayer;
        this.aiPlayer = aiPlayer;
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
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
            Status status = isAiHit(coordinate, humanPlayer.getHitBoard());
            if (status.equals(Status.HIT)) {
                for (Battleship battleship : aiPlayer.getBattleshipList()) {
                    if (battleship.getCoordinates().stream().anyMatch(coordinate1 -> coordinate1.equals(coordinate))) {
                        isAIShipDestroyed(battleship);
                        retry = false;
                        break;
                    }
                }
            } else if (status.equals(Status.MISSED)) {
                humanPlayer.getHitBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] = 4;
                retry = false;
            }
        }
    }

    private void AIShoot(AIAlgorithm aiAlgorithm) {
        Random random = new Random();
        boolean retry = true;
        while (retry) {
            int column = random.nextInt(boardWidth);
            int row = random.nextInt(boardHeight);
            Coordinate coordinate = new Coordinate(row, column);
            List<Coordinate> previousShots = aiAlgorithm.getPreviousShots();
            System.out.println("[DEBUG]: Coordinate: " + coordinate);
            Coordinate previousShot = new Coordinate(-1, -1);
            previousShot.setStatus(Status.MISSED);
            if (previousShots.size() > 0) {
                previousShot = previousShots.get(previousShots.size() - 1);
                if (previousShot.getStatus().equals(Status.HIT)) {
                    coordinate = getCoordinateAfterHit(aiAlgorithm, row, column, coordinate, previousShot, previousShots);
                } else if (previousShot.getStatus().equals(Status.DESTROYED)) {
                    coordinate = getCoordinateAfterDestroyedShip(coordinate, previousShots);
                } else if (previousShots.stream().filter(Coordinate::isHit).toList().size() > 0) {
                    aiAlgorithm.setNextHitDirection(null);
                    List<Coordinate> previousHits = previousShots.stream().filter(Coordinate::isHit).toList();
                    for (Coordinate hit : previousHits) {
                        List<Coordinate> eligibleCoordinates = getEligibleCoordinates(hit, previousShots);
                        if (eligibleCoordinates.size() > 0) {
                            coordinate = eligibleCoordinates.get(random.nextInt(eligibleCoordinates.size()));
                            previousShot = hit;
                            break;
                        }
                    }
                }
            }
            boolean alreadyHit = false;
            for (Coordinate c : previousShots) {
                if (c.equals(coordinate)) {
                    alreadyHit = true;
                    aiAlgorithm.reset();
                    break;
                }
            }
            if (alreadyHit) {
                continue;
            }
            aiAlgorithm.addShot(coordinate);
            coordinate.setStatus(isHitOrMiss(aiAlgorithm, coordinate, previousShot));
            if (!coordinate.getStatus().equals(Status.ALREADY_HIT)) {
                retry = false;
            }
        }
    }

    private List<Coordinate> getEligibleCoordinates(Coordinate hit, List<Coordinate> previousShots) {
        List<Coordinate> surroundingCoordinates = new ArrayList<>();
        if (hit.getColumn() + 1 <= boardWidth) {
            surroundCoordinate(hit.getRow(), hit.getColumn() + 1, surroundingCoordinates);
        }
        if (hit.getColumn() - 1 >= 0) {
            surroundCoordinate(hit.getRow(), hit.getColumn() - 1, surroundingCoordinates);
        }
        if (hit.getRow() + 1 <= boardHeight) {
            surroundCoordinate(hit.getRow() + 1, hit.getColumn(), surroundingCoordinates);
        }
        if (hit.getRow() - 1 >= 0) {
            surroundCoordinate(hit.getRow() - 1, hit.getColumn(), surroundingCoordinates);
        }
        List<Coordinate> eligibleCoordinates = new ArrayList<>();
        for (Coordinate surroundingCoordinate : surroundingCoordinates) {
            if (surroundingCoordinate.getStatus() == null && previousShots.stream().noneMatch(c -> c.equals(surroundingCoordinate))) {
                eligibleCoordinates.add(surroundingCoordinate);
            }
        }
        return eligibleCoordinates;
    }

    private void surroundCoordinate(int hit, int hit1, List<Coordinate> surroundingCoordinates) {
        Coordinate c = new Coordinate(hit, hit1);
        switch (humanPlayer.getPlayerBoard().getBoard()[hit][hit1]) {
            case 2 -> c.setStatus(Status.HIT);
            case 3 -> c.setStatus(Status.DESTROYED);
        }
        surroundingCoordinates.add(c);
    }

    private Coordinate getCoordinateAfterHit(AIAlgorithm aiAlgorithm, int row, int column, Coordinate coordinate, Coordinate previousShot, List<Coordinate> previousShots) {
        Random random = new Random();
        List<Coordinate> eligibleCoordinates = getEligibleCoordinates(previousShot, previousShots);
        if (aiAlgorithm.getNextHitDirection() != null) {
            switch (aiAlgorithm.getNextHitDirection()) {
                case "left" -> coordinate = new Coordinate(previousShot.getRow(), previousShot.getColumn() - 1);
                case "right" -> coordinate = new Coordinate(previousShot.getRow(), previousShot.getColumn() + 1);
                case "up" -> coordinate = new Coordinate(previousShot.getRow() - 1, previousShot.getColumn());
                case "down" -> coordinate = new Coordinate(previousShot.getRow() + 1, previousShot.getColumn());
            }
            if (!eligibleCoordinates.contains(coordinate)) {
                int direction = random.nextInt(eligibleCoordinates.size());
                coordinate = eligibleCoordinates.get(direction);
            }
        } else {
            int direction = random.nextInt(eligibleCoordinates.size());
            coordinate = eligibleCoordinates.get(direction);
        }
        if (previousShots.contains(coordinate)) {
            for (Coordinate c :eligibleCoordinates) {
                if (!previousShots.contains(c)) {
                    coordinate = c;
                    break;
                }
                coordinate = new Coordinate(row, column);
            }
        }
        return coordinate;
    }

    private Coordinate getCoordinateAfterDestroyedShip(Coordinate coordinate, List<Coordinate> previousShots) {
        Random random = new Random();
        List<Coordinate> previousHits = previousShots.stream().filter(Coordinate::isHit).toList();
        if (previousHits.size() > 0) {
            Coordinate previousHit = previousHits.get(0);
            List<Coordinate> eligibleCoordinates = getEligibleCoordinates(previousHit, previousShots);
            coordinate = eligibleCoordinates.get(random.nextInt(eligibleCoordinates.size()) - 1);
            System.out.println("[DEBUG]: new target at " + coordinate);
        }
        return coordinate;
    }

    private Status isHitOrMiss(AIAlgorithm aiAlgorithm, Coordinate coordinate, Coordinate previousShot) {
        Status status = isPlayerHit(coordinate);
        coordinate.setStatus(status);
        if (status.equals(Status.HIT)) {
            if (aiAlgorithm.getNextHitDirection() == null && previousShot.getStatus().equals(Status.HIT)) {
                if (previousShot.getColumn() == coordinate.getColumn() + 1) {
                    aiAlgorithm.setNextHitDirection("left");
                } else if (previousShot.getColumn() == coordinate.getColumn() - 1) {
                    aiAlgorithm.setNextHitDirection("right");
                } else if (previousShot.getRow() == coordinate.getRow() + 1) {
                    aiAlgorithm.setNextHitDirection("up");
                } else if (previousShot.getRow() == coordinate.getRow() - 1) {
                    aiAlgorithm.setNextHitDirection("down");
                }
            }
            for (Battleship battleship : humanPlayer.getBattleshipList()) {
                if (battleship.getCoordinates().stream().anyMatch(coordinate1 -> coordinate1.equals(coordinate))) {
                    if (isShipDestroyed(battleship, humanPlayer)) {
                        isPlayerShipDestroyed(battleship, aiAlgorithm);
                        status = Status.DESTROYED;
                        coordinate.setStatus(status);
                        break;
                    }
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
        int columnInt = -1;
        while (!inputMatch) {
            System.out.println("Write in what column you want to shoot. A letter from A to G");
            String column = scanner.nextLine();
            if (column.toCharArray().length == 1) {
                switch (column.toUpperCase(Locale.ROOT)) {
                    case "A" -> columnInt = 0;
                    case "B" -> columnInt = 1;
                    case "C" -> columnInt = 2;
                    case "D" -> columnInt = 3;
                    case "E" -> columnInt = 4;
                    case "F" -> columnInt = 5;
                    case "G" -> columnInt = 6;
                    default -> System.out.println("You didn't write a correct letter");
                }
                if (columnInt >= 0) {
                    inputMatch = true;
                }
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
            System.out.println("Write in what row you want to shoot. A number from 1 to " + boardHeight);
            String row = scanner.nextLine();
            try {
                rowInt = Integer.parseInt(row) - 1;
                if (rowInt <= boardHeight) {
                    inputMatch = true;
                } else {
                    System.out.println("You didn't choose a number between 1 and " + boardHeight);
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
     * @param playerHitBoard The board where the human player can see where they've hit
     * @return The status of the shot: HIT, MISSED or ALREADY_HIT
     */
    private Status isAiHit(Coordinate coordinate, Board playerHitBoard) {
        int[][] playerBoard = aiPlayer.getPlayerBoard().getBoard();
        if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 1) {
            aiPlayer.getPlayerBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] = 2;
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

    private Status isPlayerHit(Coordinate coordinate) {
        int[][] playerBoard = humanPlayer.getPlayerBoard().getBoard();
        System.out.println("[DEBUG] Coordinate: " + coordinate);
        if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 1) {
            humanPlayer.getPlayerBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] = 2;
            System.out.println("You've been hit in " + coordinate + "!");
            return Status.HIT;
        } else if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 2) {
            return Status.ALREADY_HIT;
        }
        System.out.println("Your opponent missed!");
        return Status.MISSED;
    }

    private void isAIShipDestroyed(Battleship battleship) {
        if (isShipDestroyed(battleship, aiPlayer)) {
            for (Coordinate c : battleship.getCoordinates()) {
                humanPlayer.getHitBoard().getBoard()[c.getRow()][c.getColumn()] = 3;
            }
            System.out.println("You destroyed the " + battleship + "!");
        }
    }

    private void isPlayerShipDestroyed(Battleship battleship, AIAlgorithm aiAlgorithm) {
        List<Coordinate> previousHits = aiAlgorithm.getPreviousShots().stream().filter(Coordinate::isHit).toList();
        if (isShipDestroyed(battleship, humanPlayer)) {
            for (Coordinate c : battleship.getCoordinates()) {
                if (previousHits.stream().anyMatch(coordinate -> coordinate.equals(c))) {
                    previousHits.stream().filter(coordinate -> coordinate.equals(c)).findFirst().get().setStatus(Status.DESTROYED);
                }
            }
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
