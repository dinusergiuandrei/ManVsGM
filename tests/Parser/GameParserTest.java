package Parser;

import ChessLogic.GameDetails;
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
        String path = "database/players/Adams.pgn";

        gameParser = new GameParser();
        String[] separators = PgnDatabaseReader.getSeparators();
        gamesStrings = fileParser.getGamesString(fileParser.getFileString(path), separators);

    }

    @Test
    public void parseGameTest(){
        List<GameDetails> games = new LinkedList<>();
        for (String game : gamesStrings) {
            games.add(gameParser.parseGame(game));
        }
        Assert.assertNotEquals(0, games.size());

        games.forEach(
                game -> {
                    Assert.assertNotEquals(0, game.tags.size());
                    Assert.assertEquals(true, game.whiteMovesString.size()>=game.blackMovesString.size());
                    game.whiteMovesString.forEach(
                            move -> {
                                Assert.assertEquals(true, isValidMove(move));
                            }
                    );
                    game.blackMovesString.forEach(
                            move -> {
                                Assert.assertEquals(true, isValidMove(move));
                            }
                    );
                }
        );
    }

    private Boolean isValidMove(String moveString){
        if(moveString.length()<2)
            return false;
        if(moveString.contains(" "))
            return false;
        return true;
    }
}
