package GameArchitecture;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TableTest {

    @Test
    public void getTableFromFENTest(){
        List<String> fens = new LinkedList<>();
        fens.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        fens.add("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        fens.add("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2");
        fens.add("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");


        fens.forEach(
                fen -> Assert.assertEquals(fen, Table.computeFENFromTable(Table.computeTableFromFEN(fen)))
        );

    }


}
