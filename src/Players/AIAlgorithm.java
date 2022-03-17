package Players;

import Util.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class AIAlgorithm {
    private final List<Coordinate> previousShots;
    private String nextHitDirection;

    public AIAlgorithm() {
        previousShots = new ArrayList<>();
    }

    public List<Coordinate> getPreviousShots() {
        return previousShots;
    }

    public void addShot(Coordinate coordinate) {
        previousShots.add(coordinate);
    }

    public String getNextHitDirection() {
        return nextHitDirection;
    }

    public void setNextHitDirection(String nextHitDirection) {
        this.nextHitDirection = nextHitDirection;
    }

    public void reset() {
        nextHitDirection = null;
    }
}