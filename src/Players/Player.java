package Players;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private Board playerBoard;
    private Board hitBoard;
    private final List<Battleship> battleshipList;

    public Player() {
        this.battleshipList = new ArrayList<>();
    }

    public Board getHitBoard() {
        return hitBoard;
    }

    public void setHitBoard(Board hitBoard) {
        this.hitBoard = hitBoard;
    }

    public void setPlayerBoard(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public List<Battleship> getBattleshipList() {
        return battleshipList;
    }

    public void addBattleship(Battleship battleship) {
        battleshipList.add(battleship);
    }
}
