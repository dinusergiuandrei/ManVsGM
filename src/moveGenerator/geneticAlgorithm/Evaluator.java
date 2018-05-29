package moveGenerator.geneticAlgorithm;

import database.Database;
import gameArchitecture.Move;
import gameArchitecture.Table;
import utils.Features;
import utils.Functions;

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
        //return evaluateSimple(individual)
        return evaluateComplex(individual);
    }

    private Double evaluateSimple(Individual individual) {
        Double score;
        AtomicReference<Double> matchingMoves = new AtomicReference<>(0.0);
        AtomicReference<Double> totalMoves = new AtomicReference<>(0.0);
        database.getData().forEach(
                dataSetEntry -> {
                    String positionString = dataSetEntry.getPositionFenString();
                    Move expectedMove = dataSetEntry.getMove();
                    Table table = this.database.getFenToTableMap().get(positionString);
                    Move realMove = individual.getMove(table);
                    if (realMove != null) {
                        if (realMove.equals(expectedMove)) {
                            matchingMoves.getAndSet(matchingMoves.get() + 1);
                            //System.err.println("Somebody guessed a move!");
                        }
                        totalMoves.getAndSet(totalMoves.get() + 1);
                    } else {
                        if (expectedMove == null)
                            matchingMoves.getAndSet(matchingMoves.get() + 1);
                        totalMoves.getAndSet(totalMoves.get() + 1);
                    }
                }
        );
        score = matchingMoves.get() / totalMoves.get();

        return score;
    }

    private Double evaluateComplex(Individual individual){
        Double score;
        AtomicReference<Double> matchingMoves = new AtomicReference<>(0.0);
        AtomicReference<Double> totalMoves = new AtomicReference<>(0.0);

        database.getFenToPlayedMovesMap().forEach(
                (fen, played) -> {
                    Table table = this.database.getFenToTableMap().get(fen);
                    Move realMove = individual.getMove(table);
                    if (realMove != null) {
                        if (played.contains(realMove)) {
                            matchingMoves.getAndSet(matchingMoves.get() + 1);
                        }
                        totalMoves.getAndSet(totalMoves.get() + 1);
                    } else {
                        if (played.size() == 0) {
                            matchingMoves.getAndSet(matchingMoves.get() + 1);
                        }
                        totalMoves.getAndSet(totalMoves.get() + 1);
                    }
                }
        );

        score = matchingMoves.get() / totalMoves.get();

        return score;
    }


    public Double computeIndividualsEvaluationOfPosition(Individual individual, Table table) {
        List<Weight> weights = individual.getChromosome().getWeights();
        //String fen = this.database.getTableToFenMap().get(table);
        String fen = Table.computeFenFromTable(table);

        List<Double> positionFeatures;
        if(this.database.getFenToFeaturesValuesMap().containsKey(fen)) {
            Map<Features, Double> featureScores = this.database.getFenToFeaturesValuesMap().get(fen);
            positionFeatures = new ArrayList<>(featureScores.values());
        }
        else {
            System.out.println(fen + " was not found in the cache");
            Map<Features, Double> featureScores = new LinkedHashMap<>();
            Table position = this.database.getFenToTableMap().get(fen);
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
