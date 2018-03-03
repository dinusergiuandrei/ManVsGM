package Parser;

import GameArchitecture.GameDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class GameParserTest {
    GameParser gameParser;

    String[] gamesStrings;

    @Before
    public void setUp(){
        PgnFileParser fileParser = new PgnFileParser();
        gameParser = new GameParser();
        String[] separators = {" 1-0\n", " 1/2-1/2\n", " 0-1\n", "\n\n"};
        gamesStrings = fileParser.getGamesString(fileParser.getFileString("database/players/Adams.pgn"), separators);
    }

    @Test
    public void parseGameTest(){
        List<GameDetails> games = new LinkedList<>();
        for (String game : gamesStrings) {
            games.add(gameParser.parseGame(game));
        }
        Assert.assertNotEquals(0, games.size());
    }
}
