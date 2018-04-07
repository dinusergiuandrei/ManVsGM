import ChessLogic.Game;
import GameArchitecture.Player;
import MoveGenerator.GeneticAlgorithm.GeneticAlgorithm;
import MoveGenerator.GeneticAlgorithm.GeneticAlgorithmParameters;
import MoveGenerator.TerminalMoveGenerator;
import Parser.PgnDatabaseReader;

import java.util.concurrent.atomic.AtomicInteger;

public class ManVsGM {
    private static ApplicationParameters params = new ApplicationParameters();

    public static void main(String[] args) {
        parse(params.dataBasePathAdams, params.dataBaseLoadPercent, true);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
                new GeneticAlgorithmParameters(
                        params.populationSize,
                        params.mutationRate,
                        params.crossOverRate,
                        params.iterations,
                        params.runs
                        )
        );
        geneticAlgorithm.learnFrom(params.dataSet, params.minMoveMatchPercent);
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

        PgnDatabaseReader databaseReader = new PgnDatabaseReader(dataBasePath);
        databaseReader.computePgnFilePaths();
        databaseReader.parseDatabase(databaseLoadPercent);
        databaseReader.getDatabase().computeAllGamesList();

        if (verbose) {
            System.out.println("Parsing " + databaseReader.getDatabase().getAllGames().size() + " games, with " +
                    databaseReader.getTotalMoveCount() + " moves. ( " +
                    databaseReader.getTotalMoveCount() * 1.0 / databaseReader.getDatabase().getAllGames().size() * 1.0 / 2.0
                    + " avarage moves per game. )");
        }

        AtomicInteger index = new AtomicInteger();

        Integer gamesCount = databaseReader.getDatabase().getGamesCount();

        databaseReader.getDatabase().getAllGames().forEach(
                game -> {
                    game.computeMoves();
                    if (verbose) {
                        displayDetails(index.get(), gamesCount);
                        index.getAndIncrement();
                    }
                }

        );

        params.dataSet = databaseReader.getDatabase().computePositionToMoveMap();

        System.out.println("Parsing successful");
    }

    private static void displayDetails(int index, int total) {
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
