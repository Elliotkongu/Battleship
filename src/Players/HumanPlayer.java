package Players;

import java.util.Scanner;

public class HumanPlayer extends Player {


    public HumanPlayer() {
        setupPlayerBoard();
    }

    /**
     * Sets up the players board using console input for coordinates
     * The ship sizes are 2,3,3,4,5 and are placed in size order
     */
    public void setupPlayerBoard() {
        boolean twoThrees = false;
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
            int column = getColumn(scanner, battleship.getName());
            int row = getRow(scanner);
            String direction = getDirection(scanner, row, column, i);
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
        setHitBoard(new Board(new int[7][7]));
    }

    /**
     * Gets the column where the human player wants to place their ship
     *
     * @param scanner The scanner for reading the console input
     * @param name    The current name of the ship
     * @return Returns the column as an integer
     */
    private int getColumn(Scanner scanner, String name) {
        boolean inputMatch = false;
        int columnInt = 0;
        while (!inputMatch) {
            System.out.println("Write in what column you want your size " + name + " battleship. A number from 1 to 7");
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
     *
     * @param scanner The scanner for reading the console input
     * @return Returns the row as an integer
     */
    private int getRow(Scanner scanner) {
        int rowInt = 0;
        boolean inputMatch = false;
        while (!inputMatch) {
            System.out.println("Write in what row you want your battleship. A number from 1 to 7");
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
}
