package ChessLogic;

import Parser.GameParser;
import Parser.PgnDatabaseReader;
import Parser.PgnFileParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class GameDetailsTest {
    String path;

    GameDetails gameDetails;

    String[] gamesStrings;

    List<GameDetails> games;

    @Before
    public void setUp(){
        gameDetails = new GameDetails();
        path = "test_data/games/player_one.pgn";
        PgnFileParser fileParser = new PgnFileParser();
        GameParser gameParser = new GameParser();
        String[] separators = PgnDatabaseReader.getSeparators();
        gamesStrings = fileParser.getGamesString(fileParser.getFileString(path), separators);
        games = new LinkedList<>();
        for (String game : gamesStrings) {
            games.add(gameParser.parseGame(game));
        }
    }

    @Test
    public void computeMovesTest(){
        for (GameDetails game : games) {
            game.computeMoves();
        }
        for (GameDetails game : games) {
            Assert.assertEquals(game.whiteMovesString.size(), game.whiteMoves.size());
            Assert.assertEquals(game.blackMovesString.size(), game.blackMoves.size());
        }
    }
}
