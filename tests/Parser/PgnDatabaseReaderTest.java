package Parser;

import org.junit.Assert;
import org.junit.Test;

public class PgnDatabaseReaderTest {

    @Test
    public void parseDataBaseTest(){
        String dataBasePath = "database/test/test.pgn";
        double databaseLoadPercent = 1.0;

        PgnDatabaseReader parser = new PgnDatabaseReader(dataBasePath);
        parser.computePgnFilePaths();
        parser.parseDatabase(databaseLoadPercent);
        parser.getDatabase().computeAllGamesList();

        Assert.assertEquals(parser.getDatabase().getAllGames().size(), 10);
    }
}
