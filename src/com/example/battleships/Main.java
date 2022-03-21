package com.example.battleships;

import com.example.battleships.Players.Ai.AiPlayer;
import com.example.battleships.Players.HumanPlayer;

public class Main {

    public static void main(String[] args) {
        HumanPlayer player = new HumanPlayer();
        AiPlayer ai = new AiPlayer();
        Game game = new Game(player, ai, 7, 7);
        game.run();
    }
}
