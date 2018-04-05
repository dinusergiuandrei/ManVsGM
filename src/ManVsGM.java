import ChessLogic.Game;
import GameArchitecture.Move;
import GameArchitecture.Player;
import MoveGenerator.TerminalMoveGenerator;
import Parser.PgnDatabaseReader;

import java.util.LinkedList;
import java.util.List;

//todo: fix parser error (2020 detected games out of 3018) ( Adams.pgn )
//todo: fix parser (moves with comments are bad) example : " he can still obtain equality.
//todo: But here is the parting of the ways.} 16. Bxe6+ {White fancies that he holds an advantage
//todo: and attempts to win. The punishment is immediate.} fxe6 17. Qxe6+ Rd7 18. Nxf5 Bd8"
//todo: is seen as a single move

public class ManVsGM {
    public static String dataBasePath = "database/players";
    public static String dataBasePathAdams = "database/players/Adams.pgn";
    public static String dataBasePathIvanchuk = "database/players/Ivanchuk.pgn";

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame() {
        int playerId = 0;
        Game game = new Game();

        Player whitePlayer = new Player(++playerId, "White", "Player", new TerminalMoveGenerator(game));
        Player blackPlayer = new Player(++playerId, "Black", "Player", new TerminalMoveGenerator(game));

        game.setWhitePlayer(whitePlayer);
        game.setBlackPlayer(blackPlayer);

        game.start();
    }

    public static void parse() {
        PgnDatabaseReader parser = new PgnDatabaseReader(dataBasePathAdams);
        parser.computePgnFilePaths();
        parser.parseDatabase();
        parser.getDatabase().computeAllGamesList();

// this is replaced by the follow for. (useful for debug)
//        parser.getDatabase().getAllGames().forEach(
//                GameDetails::computeMoves
//        );

        System.out.println("Parsing " + parser.getDatabase().getAllGames().size() + " games...");
        double procent;
        double over;
        int total;
        total = parser.getDatabase().getAllGames().size();
        for (int i = 0; i < parser.getDatabase().getAllGames().size(); i++) {
            //System.out.println(i);
            parser.getDatabase().getAllGames().get(i).computeMoves(i);

            if(i%10000 == 0) {
                over = 20.0 * (i * 1.0 / total * 1.0);
                System.out.print("[");
                for (int j = 0; j < over; ++j)
                    System.out.print("-");
                System.out.print(">");
                for (double j = over; j < 20; ++j)
                    System.out.print(".");
                System.out.println("]");
            }

        }

        System.out.println("Parsing successful");
    }
}
