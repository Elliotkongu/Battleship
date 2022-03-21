package com.example.battleships.Players.Ai;

import com.example.battleships.Util.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * All things needed for the AIs algorithm to function.
 */
public class AiAlgorithm {
    private final List<Coordinate> previousShots;
    private String nextHitDirection;

    public AiAlgorithm() {
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
