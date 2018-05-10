import ChessLogic.Database;
import ChessLogic.Game;
import GameArchitecture.Player;
import MoveGenerator.GeneticAlgorithm.GeneticAlgorithm;
import MoveGenerator.TerminalMoveGenerator;
import Parser.PgnDatabaseReader;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ManVsGM {

    private static ApplicationParameters params = new ApplicationParameters();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Database database;
        //database = computeDatabase();
        database = readDatabase();

        runGeneticAlgorithm(params, database);

        //startGame();
    }

    private static Database computeDatabase(){
        Long startTime = System.currentTimeMillis();
        Database database = parse(params.getDataBasePath(), params.getDataBaseLoadPercent(), params.getVerbose());
        System.out.println("Time to compute: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds...");
        //Database.saveStream(database, params.getDataBaseStreamSavePath());
        return database;
    }

    private static Database readDatabase() throws IOException, ClassNotFoundException {
        Long startTime = System.currentTimeMillis();
        Database loadedDatabase = Database.loadStream(params.getDataBaseStreamSavePath());
        System.out.println("Time to read: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds...");
        return loadedDatabase;
    }

    private static void runGeneticAlgorithm(ApplicationParameters params, Database database){
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm( params.computeGeneticAlgorithmParameters() );
        geneticAlgorithm.learnFrom(database, params.getMinMoveMatchPercent());
    }

    static void startGame() {
        int playerId = 0;
        Game game = new Game();

        Player whitePlayer = new Player(++playerId, "White", "Player", new TerminalMoveGenerator(game));
        Player blackPlayer = new Player(++playerId, "Black", "Player", new TerminalMoveGenerator(game));

        game.setWhitePlayer(whitePlayer);
        game.setBlackPlayer(blackPlayer);

        game.start();
    }

    private static Database parse(String dataBasePath, double databaseLoadPercent, Boolean verbose) {

        Long startTime = System.currentTimeMillis();

        PgnDatabaseReader databaseReader = new PgnDatabaseReader(dataBasePath);
        databaseReader.computePgnFilePaths();
        databaseReader.parseDatabase(databaseLoadPercent);
        databaseReader.getDatabase().computeAllGamesList();

        if (verbose) {
            System.out.println("Parsing " + databaseReader.getDatabase().getAllGames().size() + " games, with " +
                    databaseReader.getTotalMoveCount() + " moves. ( " +
                    databaseReader.getTotalMoveCount() * 1.0 / databaseReader.getDatabase().getAllGames().size() * 1.0 / 2.0
                    + " average moves per game. )");
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

        Database database = databaseReader.getDatabase().computePositionToMoveMap();
        if(verbose) {
            System.out.println("Updated data set with " + database.getData().size() + " positions from "
                    + databaseLoadPercent * 100.0 + " % of the games found at " + dataBasePath);
        }

        System.out.println("Parsing successful in " + (System.currentTimeMillis()-startTime) / 1000.0 + " seconds.\n");

        return database;
    }

    public static void displayDetails(int index, int total) {
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
