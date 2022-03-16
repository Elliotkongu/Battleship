package Players;

import Util.Coordinate;

import java.util.List;

public class Battleship {
    private List<Coordinate> coordinates;
    private boolean isDestroyed;
    private final String name;

    public Battleship(String name) {
        this.name = name;
        this.isDestroyed = false;
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return name;
    }
}
