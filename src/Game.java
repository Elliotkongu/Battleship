import Players.*;
import Players.Ai.AiAlgorithm;
import Players.Ai.AiPlayer;
import Players.Common.Battleship;
import Players.Common.Player;
import Util.Coordinate;
import Util.Status;

import java.util.*;

public class Game {

    private final HumanPlayer humanPlayer;
    private final AiPlayer aiPlayer;
    private final AiAlgorithm aiAlgorithm;
    private final int boardHeight;
    private final int boardWidth;

    public Game(HumanPlayer humanPlayer, AiPlayer aiPlayer, int boardHeight, int boardWidth) {
        this.humanPlayer = humanPlayer;
        this.aiPlayer = aiPlayer;
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.aiAlgorithm = new AiAlgorithm();
    }

    public void run() {
        while (true) {
            printBoards();
            playerShoot();
            if (isDefeated(aiPlayer)) {
                System.out.println("You won the game");
                break;
            }
            AIShoot();
            if (isDefeated(humanPlayer)) {
                System.out.println("You lost the game");
                break;
            }
        }
    }

    /**
     * Prints out the human players board and hit board.
     * O means empty space (or Ocean), S means Ship, D means Destroyed ship and X means miss.
     */
    private void printBoards() {
        String format = "%-40s%s%n";
        System.out.println("Player Board");
        System.out.printf(format, humanPlayer.getPlayerBoard(), "");
        System.out.println("Opponents Board");
        System.out.printf(format, humanPlayer.getHitBoard(), "");
    }

    /**
     * Reads the console input for where the player wants to shoot
     * Checks if the Coordinate is a hit, miss or already been hit
     * If it's already been hit then the player must choose another Coordinate.
     */
    private void playerShoot() {
        Scanner scanner = new Scanner(System.in);
        boolean retry = true;
        while (retry) {
            int column = humanPlayer.getColumn(scanner, "Write in what column you want to shoot. A letter from A to G");
            int row = humanPlayer.getRow(scanner, "Write in what row you want to shoot. A number from 1 to " + boardHeight);
            Coordinate coordinate = new Coordinate(row, column);
            Status status = isAiHit(coordinate);
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

    /**
     * Calculates where the AI should shoot and checks whether it's a hit or not
     * The first shot is random and each shot after that uses an algorithm
     * Shots go back to being random if there are no previous hits (either because the AI never hit or has destroyed all ships it has hit)
     */
    private void AIShoot() {
        Random random = new Random();
        boolean retry = true;
        while (retry) { //Loop for if a coordinate has already been shot
            int column = random.nextInt(boardWidth);
            int row = random.nextInt(boardHeight);
            Coordinate coordinate = new Coordinate(row, column);
            List<Coordinate> previousShots = aiAlgorithm.getPreviousShots();
            Coordinate previousShot = new Coordinate(-1, -1);
            previousShot.setStatus(Status.MISSED); // The list and Coordinate are set up for convenience’s sake
            if (previousShots.size() > 0) { //This check is only here to stop an error with the first shot
                previousShot = previousShots.get(previousShots.size() - 1);
                if (previousShot.getStatus().equals(Status.HIT)) {
                    coordinate = getCoordinateAfterHit(row, column, coordinate, previousShot, previousShots);
                } else if (previousShot.getStatus().equals(Status.DESTROYED)) {
                    coordinate = getCoordinateAfterDestroyedShip(coordinate, previousShots);
                } else if (previousShots.stream().filter(Coordinate::isHit).toList().size() > 0) { //If the last shot was a miss, check for previous hits and calculate the new Coordinate from the first one found
                    aiAlgorithm.setNextHitDirection(null);
                    List<Coordinate> previousHits = previousShots.stream().filter(Coordinate::isHit).toList(); //Find all previous shots that were hits
                    for (Coordinate hit : previousHits) {
                        List<Coordinate> eligibleCoordinates = getEligibleCoordinates(hit, previousShots); //Get the Coordinates that are either a ship or empty that have not already been shot
                        if (eligibleCoordinates.size() > 0) {
                            coordinate = eligibleCoordinates.get(random.nextInt(eligibleCoordinates.size()));
                            previousShot = hit;
                            break; //Break here because we only want to do this on the first one found with eligible coordinates
                        }
                    }
                }
            }
            boolean alreadyHit = false;
            for (Coordinate c : previousShots) {
                if (c.equals(coordinate)) { //If the coordinate has already been hit then try again
                    alreadyHit = true;
                    aiAlgorithm.reset(); //Reset the shooting direction to stop an infinite loop bug.
                    break;
                }
            }
            if (alreadyHit) {
                continue;
            }
            aiAlgorithm.addShot(coordinate); //Add the shot to the list of previous shots
            coordinate.setStatus(isHitOrMiss(coordinate, previousShot));
            retry = false;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Returns a list of eligible coordinates around a Coordinate
     * @param hit           The Coordinate to check around
     * @param previousShots The list of the AIs previous shots
     * @return A list of Coordinates that are either a ship or empty and haven't been already shot
     */
    private List<Coordinate> getEligibleCoordinates(Coordinate hit, List<Coordinate> previousShots) {
        List<Coordinate> surroundingCoordinates = new ArrayList<>();
        if (hit.getColumn() + 1 <= boardWidth) {
            surroundCoordinate(hit.getRow(), hit.getColumn() + 1, surroundingCoordinates); //Checks the column to the right
        }
        if (hit.getColumn() - 1 >= 0) {
            surroundCoordinate(hit.getRow(), hit.getColumn() - 1, surroundingCoordinates); //Checks the column to the left
        }
        if (hit.getRow() + 1 <= boardHeight) {
            surroundCoordinate(hit.getRow() + 1, hit.getColumn(), surroundingCoordinates); //Checks the row below
        }
        if (hit.getRow() - 1 >= 0) {
            surroundCoordinate(hit.getRow() - 1, hit.getColumn(), surroundingCoordinates); //Checks the row above
        }
        List<Coordinate> eligibleCoordinates = new ArrayList<>();
        for (Coordinate surroundingCoordinate : surroundingCoordinates) {
            if (surroundingCoordinate.getStatus() == null && previousShots.stream().noneMatch(c -> c.equals(surroundingCoordinate))) {
                eligibleCoordinates.add(surroundingCoordinate);
            }
        }
        return eligibleCoordinates;
    }

    /**
     * Checks if the Coordinate is a hit or part of a destroyed ship.
     * Adds the Coordinate to the list of surrounding coordinates
     * @param row                    The row of the Coordinate
     * @param column                 The column of the Coordinate
     * @param surroundingCoordinates The list of surround Coordinates
     */
    private void surroundCoordinate(int row, int column, List<Coordinate> surroundingCoordinates) {
        Coordinate c = new Coordinate(row, column);
        switch (humanPlayer.getPlayerBoard().getBoard()[row][column]) {
            case 2 -> c.setStatus(Status.HIT);
            case 3 -> c.setStatus(Status.DESTROYED);
        }
        surroundingCoordinates.add(c);
    }

    /**
     * Calculates the new Coordinate for the AI to shoot after a hit
     * @param row           The row the AI has selected, only used if a Coordinate has already been shot but still somehow became eligible
     * @param column        The column the AI has selected, only used if a Coordinate has already been shot but still somehow became eligible
     * @param coordinate    The Coordinate that will be shot
     * @param previousShot  The Coordinate shot
     * @param previousShots The list of previous shots.
     * @return The Coordinate to be shot
     */
    private Coordinate getCoordinateAfterHit(int row, int column, Coordinate coordinate, Coordinate previousShot, List<Coordinate> previousShots) {
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

    /**
     * Calculates the new Coordinate for the AI to shoot after it has destroyed a ship
     * @param coordinate    The Coordinate that will be shot
     * @param previousShots The list of previous shots
     * @return The Coordinate to be shot
     */
    private Coordinate getCoordinateAfterDestroyedShip(Coordinate coordinate, List<Coordinate> previousShots) {
        Random random = new Random();
        List<Coordinate> previousHits = previousShots.stream().filter(Coordinate::isHit).toList();
        if (previousHits.size() > 0) {
            for (Coordinate previousHit : previousHits) {
                List<Coordinate> eligibleCoordinates = getEligibleCoordinates(previousHit, previousShots);
                if (eligibleCoordinates.size() > 0) {
                    coordinate = eligibleCoordinates.get(random.nextInt(eligibleCoordinates.size()));
                    break;
                }
            }
        }
        return coordinate;
    }

    /**
     * Checks if the Coordinate chosen by the AI is a hit or a miss.
     * @param coordinate    The Coordinate to check
     * @param previousShot  The Coordinate that was shot
     * @return The status of the shot: HIT, MISSED or ALREADY_HIT
     */
    private Status isHitOrMiss(Coordinate coordinate, Coordinate previousShot) {
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
                        isPlayerShipDestroyed(battleship);
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
     * Checks whether the coordinate is a hit, miss or already hit
     *
     * @param coordinate The coordinate to be checked
     * @return The status of the shot: HIT, MISSED or ALREADY_HIT
     */
    private Status isAiHit(Coordinate coordinate) {
        int[][] playerBoard = aiPlayer.getPlayerBoard().getBoard();
        if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 1) {
            aiPlayer.getPlayerBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] = 2;
            humanPlayer.getHitBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] = 2;
            System.out.println("You hit!");
            return Status.HIT;
        } else if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 2) {
            System.out.println("You've already hit that!");
            return Status.ALREADY_HIT;
        }
        System.out.println("You missed!");
        return Status.MISSED;
    }

    /**
     * Check is the Coordinate is a hit, already hit or missed
     * @param coordinate The Coordinate to be checked
     * @return The status of the shot: HIT, MISSED or ALREADY_HIT
     */
    private Status isPlayerHit(Coordinate coordinate) {
        int[][] playerBoard = humanPlayer.getPlayerBoard().getBoard();
        if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 1) {
            humanPlayer.getPlayerBoard().getBoard()[coordinate.getRow()][coordinate.getColumn()] = 2;
            System.out.println("You've been hit in " + coordinate + "!");
            return Status.HIT;
        } else if (playerBoard[coordinate.getRow()][coordinate.getColumn()] == 2) {
            return Status.ALREADY_HIT;
        }
        System.out.println("Your opponent missed at " + coordinate +"!");
        return Status.MISSED;
    }

    /**
     * Checks if the AIs ship has been destroyed and change the status of Coordinates if it has been
     * @param battleship The battleship to be checked
     */
    private void isAIShipDestroyed(Battleship battleship) {
        if (isShipDestroyed(battleship, aiPlayer)) {
            for (Coordinate c : battleship.getCoordinates()) {
                humanPlayer.getHitBoard().getBoard()[c.getRow()][c.getColumn()] = 3;
            }
            System.out.println("You destroyed the " + battleship + "!");
        }
    }

    /**
     * Check if the players ship has been destroyed and change the status of Coordinates if it has been
     * @param battleship The battleship to be checked
     */
    public void isPlayerShipDestroyed(Battleship battleship) {
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

    /**
     * Check if the ship has been destroyed or not
     * @param battleship The ship to be checked
     * @param player The player who owns the ship
     * @return A boolean of whether the ship has been destroyed or not: true if it has, false if not
     */
    public boolean isShipDestroyed(Battleship battleship, Player player) {
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

    /**
     * Checks if the player has been defeated by looking at their destroyed ships
     * @param player The player to check
     * @return A boolean of whether the player has been defeated or not: true if they have, false if not
     */
    private boolean isDefeated(Player player) {
        List<Battleship> destroyedBattleships = player.getBattleshipList().stream().filter(Battleship::isDestroyed).toList();
        return destroyedBattleships.size() == 5;
    }
}
