package Players;

import java.util.List;

public class Player {
    private Board playerBoard;
    private List<Warship> warshipList;

    public Player() {}

    public Player(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public List<Warship> getWarshipList() {
        return warshipList;
    }

    public void addWarship(Warship warship) {
        warshipList.add(warship);
    }
}
