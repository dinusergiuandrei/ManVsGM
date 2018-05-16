package moveGenerator.geneticAlgorithm;

import gameArchitecture.Move;
import gameArchitecture.Table;
import moveGenerator.MoveGenerator;
import utils.Features;
import utils.Functions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Individual implements MoveGenerator {
    Chromosome chromosome;

    Evaluator evaluator;

    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Individual(Double precision, Integer valueBitCount){
        this.chromosome = new Chromosome(precision, valueBitCount);
    }

    public void setChromosome(Chromosome chromosome) {
        this.chromosome = chromosome;
    }

    public static Individual computeRandomIndividual(Double precision, Integer valueBitCount, Functions function){
        Individual individual = new Individual(precision, valueBitCount);
        individual.getChromosome().setWeights(Weight.computeRandomWeights(function, precision, valueBitCount));
        return individual;
    }

    public static Individual computeIndividualFromChromosome(Chromosome chromosome){
        Individual individual = new Individual(chromosome.getPrecision(), chromosome.getValueBitCount());
        individual.setChromosome(chromosome);
        return individual;
    }

    @Override
    public Move getMove(Table table){
        //of all the possible next moves, choose the move that leads to best position
        if(table != null) {
            List<Move> possibleMoves = table.computeAllPossibleMoves();
            Map<Move, Double> moveToScoreMap = new LinkedHashMap<>();
            for (Move move : possibleMoves) {
                if (move != null) {

                    Table newTable = Table.getNewTableAfterMove(table, move);

                    Double score;

                    if (evaluator != null)
                        score = evaluator.computeIndividualsEvaluationOfPosition(this, newTable);
                    else score = this.evaluateStaticPosition(newTable);

                    moveToScoreMap.put(move, score);
                }
            }

            AtomicReference<Move> bestMove = new AtomicReference<>();
            AtomicReference<Double> minScore = new AtomicReference<>(Double.MAX_VALUE);

            // we search for the move with the minimum score from the pov of the opponent

            moveToScoreMap.forEach(
                    (move, score) -> {
                        if (score < minScore.get()) {
                            bestMove.set(move);
                            minScore.set(score);
                        }
                    }
            );

            return bestMove.get();
        }
        return null;
    }

    public Double evaluateStaticPosition(Table table){
        Map<Features, Double> featureScores = new LinkedHashMap<>();
        for (Features feature : Features.values()) {
            featureScores.put(feature, feature.evaluate(table));
        }
        List<Double> positionFeatures = new ArrayList<>(featureScores.values());

        return Functions.LinearCombination.evaluate(this.chromosome.getWeights(), positionFeatures);
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    public static Individual computeRandomIndividual(Functions function, Double precision, Integer bitCount){
        Individual individual = new Individual(precision, bitCount);
        individual.getChromosome().setWeights(Weight.computeRandomWeights(function, precision, bitCount));
        return individual;
    }
}
