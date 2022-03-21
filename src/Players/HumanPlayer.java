package Players;

import java.util.Locale;
import java.util.Scanner;

public class HumanPlayer extends Player {

    private Board hitBoard;

    public HumanPlayer() {
        setupPlayerBoard();
    }

    /**
     * Sets up the players board using console input for coordinates
     * The ship sizes are 2,3,3,4,5 and are placed in size order
     */
    public void setupPlayerBoard() {
        boolean twoThrees = false; //Since there are two boats with the same size we'll need to repeat size 3
        Scanner scanner = new Scanner(System.in);
        Board playerBoard = new Board(new int[7][7]);
        String[] shipNames = new String[]{"Patrol Boat", "Submarine", "Destroyer", "Battleship", "Carrier"};
        for (int i = 2; i <= 5; i++) {
            Battleship battleship;
            if (!twoThrees) {
                battleship = new Battleship(shipNames[i - 2]);
            } else {
                battleship = new Battleship(shipNames[i - 1]);
            }
            int column = getColumn(scanner, "Write in what column you want your " + battleship.getName() + ". A letter from A to G");
            int row = getRow(scanner, "Write in what row you want your " + battleship.getName() + ". A number from 1 to 7");
            String direction = getDirection(scanner, row, column, i, battleship.getName()); //Gets the direction and verifies that there's enough space
            if (playerBoard.addBattleship(column, row, i, direction, battleship)) {
                System.out.println("You can't layer a ship onto another ship!");
                i--;
                continue;
            }
            addBattleship(battleship);
            if (i == 3 && !twoThrees) {
                twoThrees = true;
                i--;
            }
            System.out.println(playerBoard);
        }
        setPlayerBoard(playerBoard);
        this.hitBoard = new Board(new int[7][7]);
    }

    /**
     * Gets the column where the human player wants to place their ship
     *
     * @param scanner   The scanner for reading the console input
     * @param message   The message to be printed out
     * @return Returns the column as an integer
     */
    public int getColumn(Scanner scanner, String message) {
        boolean inputMatch = false;
        int columnInt = -1;
        while (!inputMatch) {
            System.out.println(message);
            String column = scanner.nextLine();
            if (column.toCharArray().length == 1) { //Make sure the player writes one letter
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
            } else {
                System.out.println("You didn't write one letter!");
            }
        }
        return columnInt;
    }

    /**
     * Gets the row where the human player wants to place their ship
     *
     * @param scanner The scanner for reading the console input
     * @param message The message to the printed out
     * @return Returns the row as an integer
     */
    public int getRow(Scanner scanner, String message) {
        int rowInt = 0;
        boolean inputMatch = false;
        while (!inputMatch) {
            System.out.println(message);
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
     *
     * @param scanner   The scanner for reading the console input
     * @param rowInt    Which row the player has chosen to place their ship
     * @param columnInt Which column the player has chosen to place their ship
     * @param length    The current length of the ship
     * @return The direction the player has chosen as a String
     */
    private String getDirection(Scanner scanner, int rowInt, int columnInt, int length, String name) {
        boolean inputMatch;
        System.out.println("Write in what direction you want your " + name +". Left, Right, Up or Down");
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
            } else if (direction.equalsIgnoreCase("right") && columnInt + length > 7) {
                System.out.println("Not enough space on the right!");
                continue;
            } else if (direction.equalsIgnoreCase("up") && rowInt < length) {
                System.out.println("Not enough space upwards!");
                continue;
            } else if (direction.equalsIgnoreCase("down") && rowInt + length > 7) {
                System.out.println("Not enough space downwards!");
                continue;
            }
            inputMatch = true;
        }
        return direction;
    }

    public Board getHitBoard() {
        return hitBoard;
    }
}
