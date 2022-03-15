import java.util.List;

public class Player {
    private Board playerBoard;
    private List<Warship> warshipList;

    public Player(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public List<Warship> getWarshipList() {
        return warshipList;
    }
}
