package Parser;

import ChessLogic.GameDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PgnFileParserTest {
    PgnFileParser pgnFileParser;

    String filePath;

    @Before
    public void setUp(){
        filePath = "database/players/Adams.pgn";
        pgnFileParser = new PgnFileParser();
    }

    @Test
    public void parseGamesTest() {

        String[] separators = {" 1-0\n", " 1/2-1/2\n", " 0-1\n", "\n\n"};
        List<GameDetails> games = pgnFileParser.parseGames(filePath, separators);

        Assert.assertNotEquals(games, null);
        Assert.assertNotEquals(games.size(), null);
        games.forEach(
                gameDetails -> {
                    gameDetails.whiteMovesString.forEach(
                            move -> Assert.assertNotEquals(null, move)
                    );
                    gameDetails.blackMovesString.forEach(
                            move -> Assert.assertNotEquals(null, move)
                    );
                }
        );
    }

    @Test
    public void getFileStringTest(){

        String fileString = pgnFileParser.getFileString(filePath);

        Assert.assertNotEquals(null, fileString);

    }
}
