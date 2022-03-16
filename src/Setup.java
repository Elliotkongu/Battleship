import Players.Battleship;
import Players.Board;
import Players.Player;

import java.util.Random;
import java.util.Scanner;

public class Setup {
    private final int boardHeight = 7;
    private final int boardWidth = 7;

    /**
     * Sets up the players board using console input for coordinates
     * The ship sizes are 2,3,3,4,5 and are placed in size order
     * @param player The player object representing the human player
     */
    public void setupPlayerBoard(Player player) {
        boolean twoThrees = false;
        Scanner scanner = new Scanner(System.in);
        Board player1Board = new Board(new int[boardHeight][boardWidth]);
        String[] shipNames = new String[]{"Patrol Boat", "Submarine", "Destroyer", "Battleship", "Carrier"};
        for (int i = 2; i <= 5; i++) {
            int column = getColumn(scanner, i);
            int row = getRow(scanner);
            String direction = getDirection(scanner, row, column, i);
            Battleship battleship;
            if (!twoThrees) {
                battleship = new Battleship(shipNames[i-2]);
            } else {
                battleship = new Battleship(shipNames[i-1]);
            }
            if (player1Board.addBattleship(column, row, i, direction, battleship)) {
                System.out.println("You can't layer a ship onto another ship!");
                i--;
                continue;
            }
            player.addBattleship(battleship);
            if (i == 3 && !twoThrees) {
                twoThrees = true;
                i--;
            }
            System.out.println(player1Board);
        }
        player.setPlayerBoard(player1Board);
    }

    /**
     * Sets up the AIs board by randomly placing the ships
     * The ship sizes are 2,3,3,4,5
     * @param AI The player object representing the AI
     */
    public void setupAIBoard(Player AI) {
        boolean twoThrees = false;
        Random random = new Random();
        String[] shipNames = new String[]{"Patrol Boat", "Submarine", "Destroyer", "Battleship", "Carrier"};
        Board player2Board = new Board(new int[boardHeight][boardWidth]);
        for (int i = 2; i <= 5; i++) {
            int column = random.nextInt(7);
            int row = random.nextInt(7);
            String direction = getAIDirection(row, column, i);
            if (direction.equalsIgnoreCase("failed")) {
                i--;
                continue;
            }
            Battleship battleship;
            if (!twoThrees) {
                battleship = new Battleship(shipNames[i-2]);
            } else {
                battleship = new Battleship(shipNames[i-1]);
            }
            if (player2Board.addBattleship(column, row, i, direction, battleship)) {
                i--;
                continue;
            }
            AI.addBattleship(battleship);
            if (i == 3 && !twoThrees) {
                twoThrees = true;
                i--;
            }
        }
        AI.setPlayerBoard(player2Board);
    }

    /**
     * Gets the column where the human player wants to place their ship
     * @param scanner The scanner for reading the console input
     * @param size The current size of the ship
     * @return Returns the column as an integer
     */
    private int getColumn(Scanner scanner, int size) {
        boolean inputMatch = false;
        int columnInt = 0;
        while (!inputMatch) {
            System.out.println("Write in what column you want your size " + size + " battleship. A number from 1 to " + boardWidth);
            String column = scanner.nextLine();
            try {
                columnInt = Integer.parseInt(column) - 1;
                if (columnInt <= 6) {
                    inputMatch = true;
                } else {
                    System.out.println("You didn't write a number between 1 to 7");
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
            System.out.println("Write in what row you want your battleship. A number from 1 to " + boardHeight);
            String row = scanner.nextLine();
            try {
                rowInt = Integer.parseInt(row) - 1;
                if (rowInt <= 6) {
                    inputMatch = true;
                } else {
                    System.out.println("You didn't write a number between 1 to 7");
                }
            } catch (Exception e) {
                System.out.println("You didn't write a number!");
            }
        }
        return rowInt;
    }

    /**
     * Gets the direction the player wants to place their ship
     * The direction must be left, right, up or down and there must be enough space
     * @param scanner The scanner for reading the console input
     * @param rowInt Which row the player has chosen to place their ship
     * @param columnInt Which column the player has chosen to place their ship
     * @param length The current length of the ship
     * @return The direction the player has chosen as a String
     */
    private String getDirection(Scanner scanner, int rowInt, int columnInt, int length) {
        boolean inputMatch;
        System.out.println("Write in what direction you want your battleship. Left, Right, Up or Down");
        inputMatch = false;
        String direction = "";
        while (!inputMatch) {
            direction = scanner.nextLine();
            if (!direction.equalsIgnoreCase("left") && !direction.equalsIgnoreCase("right")
                    && !direction.equalsIgnoreCase("up") && !direction.equalsIgnoreCase("down")) {
                System.out.println("You didn't write a proper direction!");
                continue;
            } else if (direction.equalsIgnoreCase("left") && columnInt < length) {
                System.out.println("Not enough space on the left!");
                continue;
            } else if (direction.equalsIgnoreCase("right") && columnInt + length > boardWidth) {
                System.out.println("Not enough space on the right!");
                continue;
            } else if (direction.equalsIgnoreCase("up") && rowInt < length) {
                System.out.println("Not enough space upwards!");
                continue;
            } else if (direction.equalsIgnoreCase("down") && rowInt + length > boardHeight) {
                System.out.println("Not enough space downwards!");
                continue;
            }
            inputMatch = true;
        }
        return direction;
    }

    /**
     * Gets the random direction the AI is going to place its ship
     * The direction will always be left, right, up or down and there must be enough space for the ship
     * @param rowInt The row the AI has chosen
     * @param columnInt The column the AI has chosen
     * @param length The current length of the ship
     * @return The direction that the AI has randomly chosen
     */
    private String getAIDirection(int rowInt, int columnInt, int length) {
        boolean inputMatch;
        inputMatch = false;
        Random random = new Random();
        String[] directionArray = new String[]{"left", "right", "up", "down"};
        String direction = directionArray[random.nextInt(4)];
        int attempts = 0;
        while (!inputMatch) {
            if (direction.equalsIgnoreCase("left") && columnInt < length) {
                direction = directionArray[random.nextInt(4)];
                attempts++;
                continue;
            } else if (direction.equalsIgnoreCase("right") && columnInt + length > boardWidth) {
                direction = directionArray[random.nextInt(4)];
                attempts++;
                continue;
            } else if (direction.equalsIgnoreCase("up") && rowInt < length) {
                direction = directionArray[random.nextInt(4)];
                attempts++;
                continue;
            } else if (direction.equalsIgnoreCase("down") && rowInt + length > boardHeight) {
                direction = directionArray[random.nextInt(4)];
                attempts++;
                continue;
            } else if (attempts >= 15) {
                return "failed";
            }
            inputMatch = true;
        }
        return direction;
    }
}
