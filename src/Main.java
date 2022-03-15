import Players.Player;

public class Main {

    public static void main(String[] args) {
        Player player = new Player();
        Player AI = new Player();
        Setup setup = new Setup();
        setup.setupPlayerBoard(player);
        setup.setupAIBoard(AI);
    }
}
