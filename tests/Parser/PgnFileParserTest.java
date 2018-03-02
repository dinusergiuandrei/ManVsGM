package Parser;

import GameArchitecture.GameDetails;
import org.junit.Test;

import java.util.List;

public class PgnFileParserTest {
    @Test
    public void parseGamesTest() {
        PgnFileParser pgnFileParser = new PgnFileParser("database/players/Andreikin.pgn");
        List<GameDetails> games = pgnFileParser.parseGames();
        System.out.println(games.get(0).toString());
    }
}
