import ChessLogic.Game;
import GameArchitecture.Player;
import MoveGenerator.TerminalMoveGenerator;
import Parser.PgnDatabaseReader;

public class ManVsGM {
    public static String dataBasePath = "database/players";
    public static String dataBasePathAdams = "database/players/Adams.pgn";

    public static void main(String[] args) {
        parse();
    }

    public static void startGame(){
        int playerId = 0;
        Game game = new Game();

        Player whitePlayer = new Player(++playerId, "White", "Player", new TerminalMoveGenerator(game));
        Player blackPlayer = new Player(++playerId, "Black", "Player", new TerminalMoveGenerator(game));

        game.setWhitePlayer(whitePlayer);
        game.setBlackPlayer(blackPlayer);

        game.start();
    }

    public static void parse(){
        PgnDatabaseReader parser = new PgnDatabaseReader(dataBasePathAdams);
        parser.computePgnFilePaths();
        parser.parseDatabase();
        parser.getDatabase().computeAllGamesList();

//        parser.getDatabase().getAllGames().forEach(
//                GameDetails::computeMoves
//        );

        System.out.println("Parsing "+parser.getDatabase().getAllGames().size()+ " games..." );
        for (int i = 0; i < parser.getDatabase().getAllGames().size(); i++) {
            System.out.println(i);
            parser.getDatabase().getAllGames().get(i).computeMoves();

        }

        int x;
        x=3333;
        System.out.println(x);
    }
}
