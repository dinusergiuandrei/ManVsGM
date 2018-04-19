package MoveGenerator.GeneticAlgorithm;

import ChessLogic.DataSet;
import ChessLogic.Features;
import GameArchitecture.Move;
import GameArchitecture.Table;
import MoveGenerator.Functions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Evaluator {
    DataSet dataSet;

    Double minMoveMatchPercent;

    Functions function;

    public Evaluator(DataSet dataSet, Double minMoveMatchPercent, Functions function){
        this.dataSet = dataSet;
        this.minMoveMatchPercent = minMoveMatchPercent;
        this.function = function;
    }

    public Double evaluateIndividual(Individual individual){
        Double score;
        AtomicReference<Double> matchingMoves = new AtomicReference<>(0.0);
        AtomicReference<Double> totalMoves = new AtomicReference<>(0.0);
        dataSet.getData().forEach(
                dataSetEntry -> {
                    String position = dataSetEntry.getPosition();
                    Move expectedMove = dataSetEntry.getMove();
                    Move realMove = individual.getMove(Table.computeTableFromFen(position));
                    if(realMove == expectedMove){
                        matchingMoves.getAndSet(matchingMoves.get() + 1);
                    }
                    totalMoves.getAndSet(totalMoves.get() + 1);
                }
        );
        score = matchingMoves.get() / totalMoves.get();
        return score;
    }

    public Double computeIndividualsEvaluationOfPosition(Individual individual, Table table){
        return this.function.evaluate(
                individual.getChromosome().getWeights(),
                new ArrayList<>(
                        this.dataSet.getDataSetEntryToFeaturesValuesMap().get(
                                Table.computeFenFromTable(table)
                        ).values()
                )
        );
    }

    public void computePositionFeaturesForAllPositions(){
        this.dataSet.getData().forEach(
                dataSetEntry -> {
                    Map<Features, Double> map = new LinkedHashMap<>();
                    Table position = Table.computeTableFromFen(dataSetEntry.getPosition());
                    for (Features feature : Features.values()) {
                        map.put(feature, feature.evaluate(position));
                    }
                    this.dataSet.getDataSetEntryToFeaturesValuesMap().put(dataSetEntry.getPosition(), map);
                }
        );
    }


}
