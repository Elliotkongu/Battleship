import Players.Ai.AiAlgorithm;
import Players.Ai.AiPlayer;
import Players.Common.Battleship;
import Players.HumanPlayer;
import Util.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameTests {

    Game game;
    HumanPlayer humanPlayer;
    AiPlayer aiPlayer;
    AiAlgorithm aiAlgorithm;

    @BeforeEach
    void setup() {
        InputStream in = new ByteArrayInputStream(("A" + System.lineSeparator() + "1" + System.lineSeparator() + "right" + System.lineSeparator() +
                "A" + System.lineSeparator() + "2" + System.lineSeparator() + "right" + System.lineSeparator() +
                "A" + System.lineSeparator() + "3" + System.lineSeparator() + "right" + System.lineSeparator() +
                "A" + System.lineSeparator() + "4" + System.lineSeparator() + "right" + System.lineSeparator() +
                "A" + System.lineSeparator() + "5" + System.lineSeparator() + "right" + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
        System.setIn(in);
        humanPlayer = new HumanPlayer();
        aiPlayer = new AiPlayer();
        aiAlgorithm = new AiAlgorithm();
        game = new Game(humanPlayer, aiPlayer, 7, 7);
    }

    @Test
    void isShipNotDestroyedTest() {
        //Given
        Battleship battleship = new Battleship("Test Boat");
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(0,0));
        coordinates.add(new Coordinate(1,0));
        battleship.setCoordinates(coordinates);
        humanPlayer.getHitBoard().getBoard()[0][0] = 1;
        humanPlayer.getHitBoard().getBoard()[1][0] = 1;
        humanPlayer.addBattleship(battleship);
        //When

        //Then
        assertFalse(game.isShipDestroyed(battleship, humanPlayer));
    }
    @Test
    void isShipDestroyedTest() {
        //Given
        Battleship battleship = new Battleship("Test Boat");
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(0,0));
        coordinates.add(new Coordinate(1,0));
        battleship.setCoordinates(coordinates);
        humanPlayer.getHitBoard().getBoard()[0][0] = 2;
        humanPlayer.getHitBoard().getBoard()[1][0] = 2;
        humanPlayer.addBattleship(battleship);
        //When

        //Then
        assertFalse(game.isShipDestroyed(battleship, humanPlayer));
    }

    @Test
    void isPlayerShipNotDestroyedTest() {
        //Given
        Battleship battleship = new Battleship("Test Boat");
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(0,0));
        coordinates.add(new Coordinate(1,0));
        battleship.setCoordinates(coordinates);
        humanPlayer.getHitBoard().getBoard()[0][0] = 1;
        humanPlayer.getHitBoard().getBoard()[1][0] = 1;
        humanPlayer.addBattleship(battleship);
        //When
        game.isPlayerShipDestroyed(battleship);
        //Then
        assertFalse(battleship.isDestroyed());
    }
}
