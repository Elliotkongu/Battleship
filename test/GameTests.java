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
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void isAiShipDestroyedTrueTest() {
        //Given
        Battleship battleship = new Battleship("Test Boat");
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(0,0));
        coordinates.add(new Coordinate(1,0));
        battleship.setCoordinates(coordinates);
        aiPlayer.getPlayerBoard().getBoard()[0][0] = 2;
        aiPlayer.getPlayerBoard().getBoard()[1][0] = 2;
        aiPlayer.addBattleship(battleship);
        //When
        game.isAIShipDestroyed(battleship);
        //Then
        assertTrue(battleship.isDestroyed());
    }

    @Test
    void isAiShipDestroyedFalseTest() {
        //Given
        Battleship battleship = new Battleship("Test Boat");
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(0,0));
        coordinates.add(new Coordinate(1,0));
        battleship.setCoordinates(coordinates);
        aiPlayer.getPlayerBoard().getBoard()[0][0] = 1;
        aiPlayer.getPlayerBoard().getBoard()[1][0] = 1;
        aiPlayer.addBattleship(battleship);
        //When
        game.isAIShipDestroyed(battleship);
        //Then
        assertFalse(battleship.isDestroyed());
    }

    @Test
    void isHumanPlayerDefeated() {
        //Given
        Battleship b1 = new Battleship("Test Boat");
        Battleship b2 = new Battleship("Test Destroyer");
        Battleship b3 = new Battleship("Test Submarine");
        Battleship b4 = new Battleship("Test Battleship");
        Battleship b5 = new Battleship("Test Carrier");
        b1.setDestroyed(true);
        b2.setDestroyed(true);
        b3.setDestroyed(true);
        b4.setDestroyed(true);
        b5.setDestroyed(true);
        humanPlayer.addBattleship(b1);
        humanPlayer.addBattleship(b2);
        humanPlayer.addBattleship(b3);
        humanPlayer.addBattleship(b4);
        humanPlayer.addBattleship(b5);
        //Then
        assertTrue(game.isDefeated(humanPlayer));
    }

    @Test
    void isHumanPlayerNotDefeated() {
        //Given
        Battleship b1 = new Battleship("Test Boat");
        Battleship b2 = new Battleship("Test Destroyer");
        Battleship b3 = new Battleship("Test Submarine");
        Battleship b4 = new Battleship("Test Battleship");
        Battleship b5 = new Battleship("Test Carrier");
        humanPlayer.addBattleship(b1);
        humanPlayer.addBattleship(b2);
        humanPlayer.addBattleship(b3);
        humanPlayer.addBattleship(b4);
        humanPlayer.addBattleship(b5);
        //Then
        assertFalse(game.isDefeated(humanPlayer));
    }
}
