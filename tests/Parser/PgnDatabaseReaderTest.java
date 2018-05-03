package Parser;

import org.junit.Assert;
import org.junit.Test;

public class PgnDatabaseReaderTest {

    @Test
    public void parseDataBaseTest(){
        String dataBasePath = "test_data/games/player_two.pgn";
        double databaseLoadPercent = 1.0;

        PgnDatabaseReader parser = new PgnDatabaseReader(dataBasePath);
        parser.computePgnFilePaths();
        parser.parseDatabase(databaseLoadPercent);
        parser.getDatabase().computeAllGamesList();

        Assert.assertTrue(parser.getDatabase().getAllGames().size()>0);
    }
}
