import Players.Battleship;
import Players.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Game {

    public void run() {

    }

    private boolean isDefeated(Player player) {
        List<Battleship> destroyedBattleships = player.getWarshipList().stream().filter(Battleship::isDestroyed).toList();
        return destroyedBattleships.size() == 5;
    }
}
