package Players.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains things that are shared between both the AI player and the Human player.
 */
public abstract class Player {
    private Board playerBoard;
    private final List<Battleship> battleshipList;

    public Player() {
        this.battleshipList = new ArrayList<>();
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
