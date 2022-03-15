import java.util.Random;
import java.util.Scanner;

public class Setup {
    private int boardHeight;
    private int boardWidth;

    public void setupPlayerBoard(int boardHeight, int boardWidth, int maxShipLength) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        boolean twoThrees = false;
        Scanner scanner = new Scanner(System.in);
        Board player1Board = new Board(new int[boardHeight][boardWidth]);
        for (int i = 2; i <= maxShipLength; i++) {
            String direction = "";
            int column = getColumn(scanner, i);
            int row = getRow(scanner);
            direction = getDirection(scanner, row, column, i, direction);
            if (player1Board.addWarshipFail(column, row, i, direction)) {
                System.out.println("You can't layer a ship onto another ship!");
                i--;
                continue;
            }
            if (i == 3 && !twoThrees) {
                twoThrees = true;
                i--;
            }
            System.out.println(player1Board);
        }
    }

    public void setupAIBoard(int boardHeight, int boardWidth, int maxShipLength) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        boolean twoThrees = false;
        Random random = new Random();
        String[] directionArray = new String[]{"left", "right", "up", "down"};
        Board player2Board = new Board(new int[boardHeight][boardWidth]);
        for (int i = 2; i <= maxShipLength; i++) {
            int column = random.nextInt(7);
            int row = random.nextInt(7);
            String direction = getAIDirection(row, column, i, directionArray);
            if (player2Board.addWarshipFail(column, row, i, direction)) {
                i--;
                continue;
            }
            if (i == 3 && !twoThrees) {
                twoThrees = true;
                i--;
            }
        }
        System.out.println(player2Board);
    }

    private int getColumn(Scanner scanner, int size) {
        boolean inputMatch = false;
        int columnInt = 0;
        while (!inputMatch) {
            System.out.println("Write in what column you want your size " + size + " battleship. A number from 1 to " + boardWidth);
            String column = scanner.nextLine();
            try {
                columnInt = Integer.parseInt(column) - 1;
                inputMatch = true;
            } catch (Exception e) {
                System.out.println("You didn't write a number!");
            }
        }
        return columnInt;
    }

    private int getRow(Scanner scanner) {
        int rowInt = 0;
        boolean inputMatch = false;
        while (!inputMatch) {
            System.out.println("Write in what row you want your battleship. A number from 1 to " + boardHeight);
            String row = scanner.nextLine();
            try {
                rowInt = Integer.parseInt(row) - 1;
                inputMatch = true;
            } catch (Exception e) {
                System.out.println("You didn't write a number!");
            }
        }
        return rowInt;
    }

    private String getDirection(Scanner scanner, int rowInt, int columnInt, int length, String direction) {
        boolean inputMatch;
        System.out.println("Write in what direction you want your battleship. Left, Right, Up or Down");
        inputMatch = false;
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

    private String getAIDirection(int rowInt, int columnInt, int length, String[] directionArray) {
        boolean inputMatch;
        inputMatch = false;
        Random random = new Random();
        String direction = directionArray[random.nextInt(4)];
        while (!inputMatch) {
            if (direction.equalsIgnoreCase("left") && columnInt < length) {
                direction = directionArray[random.nextInt(4)];
                continue;
            } else if (direction.equalsIgnoreCase("right") && columnInt + length > boardWidth) {
                direction = directionArray[random.nextInt(4)];
                continue;
            } else if (direction.equalsIgnoreCase("up") && rowInt < length) {
                direction = directionArray[random.nextInt(4)];
                continue;
            } else if (direction.equalsIgnoreCase("down") && rowInt + length > boardHeight) {
                direction = directionArray[random.nextInt(4)];
                continue;
            }
            inputMatch = true;
        }
        return direction;
    }
}
