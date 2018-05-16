package chessLogic;

import gameArchitecture.Table;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.Features;

import java.util.LinkedList;
import java.util.List;

public class FeaturesTest {
    List<String> fens;

    @Before
    public void setUp(){
        fens = new LinkedList<>();
        fens.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        fens.add("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        fens.add("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2");
        fens.add("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");
    }

    @Test
    public void evaluateTest(){
        for (Features feature : Features.values()) {
            for (String fen : this.fens) {
                Table table = Table.computeTableFromFen(fen);
                Double score = feature.evaluate(table);

                Assert.assertTrue(score >= 0.0);
                Assert.assertTrue(score <= 1.0);
            }
        }
    }
}
