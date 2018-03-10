import ChessLogic.Game;
import ChessLogic.GameDetails;
import GameArchitecture.Player;
import MoveGenerator.TerminalMoveGenerator;
import Parser.PgnDatabaseReader;

public class ManVsGM {
    public static void main(String[] args) {
        startGame();
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
        PgnDatabaseReader parser = new PgnDatabaseReader("database/players/Adams.pgn");
        parser.computePgnFilePaths();
        parser.parseDatabase();
        parser.getDatabase().computeAllGamesList();
        parser.getDatabase().getAllGames().forEach(
                GameDetails::computeMoves
        );
        int x;
        x=3;
        System.out.println(x);
    }

}
