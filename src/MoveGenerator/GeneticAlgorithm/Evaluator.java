package MoveGenerator.GeneticAlgorithm;

import ChessLogic.DataSet;
import GameArchitecture.Move;
import GameArchitecture.Table;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Evaluator {
    DataSet dataSet;

    Double minMoveMatchPercent;

    public Double evaluateIndividual(Individual individual){
        Double score;
        AtomicReference<Double> matchingMoves = new AtomicReference<>(0.0);
        AtomicReference<Double> totalMoves = new AtomicReference<>(0.0);
        dataSet.data.forEach(
                dataSetEntry -> {
                    String position = dataSetEntry.position;
                    Move move = dataSetEntry.move;
                    if(individual.getMove(Table.computeTableFromFen(position)) == move){
                        matchingMoves.getAndSet(matchingMoves.get() + 1);
                    }
                    totalMoves.getAndSet(totalMoves.get() + 1);
                }
        );
        score = matchingMoves.get() / totalMoves.get();
        return score;
    }

    public Evaluator(DataSet dataSet, Double minMoveMatchPercent){
        this.dataSet = dataSet;
        this.minMoveMatchPercent = minMoveMatchPercent;
    }
}
