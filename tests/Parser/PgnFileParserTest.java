package Parser;

import java.util.List;

public class PgnFileParserTest {
    public static void main(String args[]) {
        PgnFileParser pgnFileParser = new PgnFileParser("database/players/Christiansen.pgn");
        List<GameDetails> games = pgnFileParser.parseGames();
        System.out.println(games.get(1).toString());
    }
}
