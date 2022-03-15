package Players;

import Coordinate.Coordinate;

import java.util.List;

public class Battleship {
    private final int length;
    private List<Coordinate> coordinates;
    private boolean isDestroyed;

    public Battleship(int length) {
        this.length = length;
        this.isDestroyed = false;
    }

    public int getLength() {
        return length;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}
