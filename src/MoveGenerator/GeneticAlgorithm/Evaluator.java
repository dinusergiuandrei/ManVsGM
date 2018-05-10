package MoveGenerator.GeneticAlgorithm;

import ChessLogic.Database;
import ChessLogic.Features;
import GameArchitecture.Move;
import GameArchitecture.Table;
import MoveGenerator.Functions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Evaluator {
    Database database;

    Double minMoveMatchPercent;

    Functions function;

    public Evaluator(Database database, Double minMoveMatchPercent, Functions function) {
        this.database = database;
        this.minMoveMatchPercent = minMoveMatchPercent;
        this.function = function;
    }

    public Double evaluateIndividual(Individual individual) {
        Double score;
        AtomicReference<Double> matchingMoves = new AtomicReference<>(0.0);
        AtomicReference<Double> totalMoves = new AtomicReference<>(0.0);
        database.getData().forEach(
                dataSetEntry -> {
                    String position = dataSetEntry.getPositionFenString();
                    Move expectedMove = dataSetEntry.getMove();
                    Move realMove = individual.getMove(Table.computeTableFromFen(position));
                    if (realMove == expectedMove) {
                        matchingMoves.getAndSet(matchingMoves.get() + 1);
                    }
                    totalMoves.getAndSet(totalMoves.get() + 1);
                }
        );
        score = matchingMoves.get() / totalMoves.get();
        return score;
    }

    public Double computeIndividualsEvaluationOfPosition(Individual individual, Table table) {
        List<Weight> weights = individual.getChromosome().getWeights();
        String fen = Table.computeFenFromTable(table);
        List<Double> positionFeatures;
        if(this.database.getFenToFeaturesValuesMap().containsKey(fen)) {
            Map<Features, Double> featureScores = this.database.getFenToFeaturesValuesMap().get(fen);
            positionFeatures = new ArrayList<>(featureScores.values());

        }
        else {
            //System.out.println(fen + " was not found in the cache");
            Map<Features, Double> featureScores = new LinkedHashMap<>();
            Table position = Table.computeTableFromFen(fen);
            for (Features feature : Features.values()) {
                featureScores.put(feature, feature.evaluate(position));
            }
            positionFeatures = new ArrayList<>(featureScores.values());
        }

        return this.function.evaluate(
                weights,
                positionFeatures
        );
    }




}
