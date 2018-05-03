package MoveGenerator.GeneticAlgorithm;

import ChessLogic.DataSet;
import ChessLogic.DataSetEntry;
import GameArchitecture.Move;
import GameArchitecture.Square;
import MoveGenerator.Functions;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class EvaluatorTest {
    Evaluator evaluator;

    @Before
    public void setUp(){
        evaluator = new Evaluator(createRandomDataSet(), 0.3, Functions.LinearCombination);
    }

    public DataSet createRandomDataSet(){
        List<String> fens = new LinkedList<>();
        fens.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        fens.add("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        fens.add("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2");
        fens.add("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");

        DataSet dataSet = new DataSet();
        for (String fen : fens) {
            dataSet.getData().add(
                    new DataSetEntry(
                            fen,
                            new Move(Square.e2, Square.e4)
                    )
            );
        }

        return dataSet;
    }

    @Test
    public void evaluateIndividualTest(){

    }
}
