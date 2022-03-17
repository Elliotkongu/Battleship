package Players;

import java.util.Random;

public class AiPlayer extends Player {

    public AiPlayer() {
        setupAIBoard();
    }

    /**
     * Sets up the AIs board by randomly placing the ships
     * The ship sizes are 2,3,3,4,5
     */
    public void setupAIBoard() {
        boolean twoThrees = false;
        Random random = new Random();
        String[] shipNames = new String[]{"Patrol Boat", "Submarine", "Destroyer", "Battleship", "Carrier"};
        Board player2Board = new Board(new int[7][7]);
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
                battleship = new Battleship(shipNames[i - 2]);
            } else {
                battleship = new Battleship(shipNames[i - 1]);
            }
            if (player2Board.addBattleship(column, row, i, direction, battleship)) {
                i--;
                continue;
            }
            addBattleship(battleship);
            if (i == 3 && !twoThrees) {
                twoThrees = true;
                i--;
            }
        }
        setPlayerBoard(player2Board);
    }

    /**
     * Gets the random direction the AI is going to place its ship
     * The direction will always be left, right, up or down and there must be enough space for the ship
     *
     * @param rowInt    The row the AI has chosen
     * @param columnInt The column the AI has chosen
     * @param length    The current length of the ship
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
            } else if (direction.equalsIgnoreCase("right") && columnInt + length > 7) {
                direction = directionArray[random.nextInt(4)];
                attempts++;
                continue;
            } else if (direction.equalsIgnoreCase("up") && rowInt < length) {
                direction = directionArray[random.nextInt(4)];
                attempts++;
                continue;
            } else if (direction.equalsIgnoreCase("down") && rowInt + length > 7) {
                direction = directionArray[random.nextInt(4)];
                attempts++;
                continue;
            } else if (attempts >= 5) {
                return "failed";
            }
            inputMatch = true;
        }
        return direction;
    }
}
