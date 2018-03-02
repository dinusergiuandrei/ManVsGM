import GameArchitecture.Game;
import GameArchitecture.Player;
import Parser.PgnDatabaseReader;

public class ManVsGM {

    public static void main(String[] args) {
        PgnDatabaseReader parser = new PgnDatabaseReader(args[0]);
        parser.computePgnFilePaths();
        parser.parseDatabase();
    }

    public static void startGame(){
        int playerId = 0;
        Player whitePlayer = new Player(++playerId, "White", "Player", false);
        Player blackPlayer = new Player(++playerId, "Black", "Player", true);

        Game game = new Game(whitePlayer, blackPlayer);
        game.setUp();

        game.start();
    }

}
