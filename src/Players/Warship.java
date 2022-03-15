package Players;

import Coordinate.Coordinate;

import java.util.List;

public class Warship {
    int length;
    List<Coordinate> Coordinates;
    boolean isDestroyed;

    public Warship(int length, Coordinate startCoordinate) {
        this.length = length;
        this.isDestroyed = false;
    }
}
