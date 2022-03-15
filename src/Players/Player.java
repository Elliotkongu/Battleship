package Players;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Board playerBoard;
    private List<Battleship> battleshipList;

    public Player() {
        this.battleshipList = new ArrayList<>();
    }

    public Player(Board playerBoard) {
        this.playerBoard = playerBoard;
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
