import ChessLogic.Game;
import GameArchitecture.Player;
import MoveGenerator.TerminalMoveGenerator;
import Parser.PgnDatabaseReader;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

//todo: fix parser error (2020 detected games out of 3018) ( Adams.pgn )

public class ManVsGM {
    public static String dataBasePath = "database/players";
    public static String dataBasePathOpenings = "database/openings";
    public static String dataBasePathAdams = "database/players/Adams.pgn";
    public static String dataBasePathIvanchuk = "database/players/Ivanchuk.pgn";
    public static String dataBasePathSeirawan = "database/players/Seirawan.pgn";

    public static Double dataBaseLoadPercent = 0.3;


    public static void main(String[] args) {
        parse(dataBasePath, dataBaseLoadPercent, true);
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

    public static void parse(String dataBasePath, double databaseLoadPercent, Boolean verbose) {
        int total;

        PgnDatabaseReader parser = new PgnDatabaseReader(dataBasePath);
        parser.computePgnFilePaths();
        parser.parseDatabase(databaseLoadPercent);
        parser.getDatabase().computeAllGamesList();

        if(verbose) {
            System.out.println("Parsing " + parser.getDatabase().getAllGames().size() + " games, with " +
                    parser.totalMoveCount + " moves. ( " +
                    parser.totalMoveCount * 1.0 / parser.getDatabase().getAllGames().size() * 1.0
                    + " avarage moves per game. )");
        }

        AtomicInteger index = new AtomicInteger();
        total = parser.getDatabase().getAllGames().size();

        parser.getDatabase().getAllGames().forEach(
                game -> {
                    game.computeMoves();
                    if(verbose) {
                        displayDetails(index.get(), total);
                        index.getAndIncrement();
                    }
                }

        );

        System.out.println("Parsing successful");
    }

    private static void displayDetails(int index, int total){
        double over;
        if (index % (total / 20) == 0) {
            over = 20.0 * (index * 1.0 / total * 1.0);
            System.out.print("[");
            for (int j = 0; j < over; ++j)
                System.out.print("-");
            System.out.print(">");
            for (double j = over; j < 19; ++j)
                System.out.print(".");
            System.out.println("]");
        }
    }
}
