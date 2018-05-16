package MoveGenerator.GeneticAlgorithm;

import GameArchitecture.Move;
import GameArchitecture.Table;
import MoveGenerator.Functions;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class IndividualTest {
    Individual individual;

    List<Table> tables;

    @Before
    public void setUp(){
        Functions function = Functions.LinearCombination;
        Double precision = 0.001;
        Integer bitCount = 11;
        individual = new Individual(precision, bitCount);

        tables = new LinkedList<>();
        individual.getChromosome().setWeights(Weight.computeRandomWeights(function, precision, bitCount));

        List<String> fens = new LinkedList<>();
        fens.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        fens.add("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        fens.add("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2");
        fens.add("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");

        fens.forEach(
                fen -> tables.add(Table.computeTableFromFen(fen))
        );

    }

    @Test
    public void evaluateStaticPositionTest(){
        for (Table table : tables) {
            System.out.println(individual.evaluateStaticPosition(table));
        }
    }

    @Test
    public void getMoveTest(){
        for (Table table : tables) {
            Move bestMove = individual.getMove(table);
            if(bestMove == null){
                System.out.println("null move");
            }
            System.out.println(table.canMove(bestMove, true) + " " + bestMove);
        }
    }
}
