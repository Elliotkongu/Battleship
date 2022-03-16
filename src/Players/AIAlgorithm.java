package Players;

import Util.Coordinate;

public class AIAlgorithm {
    private Coordinate firstHit;
    private Coordinate lastHit;
    private String nextHitDirection;

    public AIAlgorithm() {}

    public Coordinate getFirstHit() {
        return firstHit;
    }

    public void setFirstHit(Coordinate firstHit) {
        this.firstHit = firstHit;
    }

    public Coordinate getLastHit() {
        return lastHit;
    }

    public void setLastHit(Coordinate lastHit) {
        this.lastHit = lastHit;
    }

    public String getNextHitDirection() {
        return nextHitDirection;
    }

    public void setNextHitDirection(String nextHitDirection) {
        this.nextHitDirection = nextHitDirection;
    }

    public void reset() {
        firstHit = null;
        lastHit = null;
        nextHitDirection = null;
    }
}
