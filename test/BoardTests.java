import com.example.battleships.Players.Common.Battleship;
import com.example.battleships.Players.Common.Board;
import com.example.battleships.Util.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTests {

    Board board;

    @BeforeEach
    void setup() {
        board = new Board(new int[7][7]);
    }

    @Test
    void addBattleshipFalseTest() {
        //Given
        Battleship battleship = new Battleship("Test Boat");
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(0,0));
        coordinates.add(new Coordinate(1,0));
        battleship.setCoordinates(coordinates);

        assertFalse(board.addBattleship(0,0, 2, "down", battleship));
    }

    @Test
    void addBattleshipTrueTest() {
        //Given
        Battleship battleship = new Battleship("Test Boat");
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(0,0));
        coordinates.add(new Coordinate(1,0));
        battleship.setCoordinates(coordinates);
        board.getBoard()[0][0] = 1;
        assertTrue(board.addBattleship(0,0, 2, "down", battleship));
    }
}
