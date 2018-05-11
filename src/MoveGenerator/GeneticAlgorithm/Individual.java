package MoveGenerator.GeneticAlgorithm;

import GameArchitecture.Move;
import GameArchitecture.Table;
import MoveGenerator.Functions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Individual {
    Chromosome chromosome;

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

    public Move getMove(Evaluator evaluator, Table table){
        //of all the possible next moves, choose the move that leads to best position
        List<Move> possibleMoves = table.computeAllPossibleMoves();
        Map<Move, Double> moveToScoreMap = new LinkedHashMap<>();
        for (Move move : possibleMoves) {
            if(move != null) {

                Table newTable = Table.getNewTableAfterMove(table, move);

                Double score = evaluator.computeIndividualsEvaluationOfPosition(this, newTable);
                moveToScoreMap.put(move, score);
            }
        }

        AtomicReference<Move> bestMove = new AtomicReference<>();
        AtomicReference<Double> maxScore = new AtomicReference<>(0.0);

        moveToScoreMap.forEach(
                (move, score) -> {
                    if(score> maxScore.get()){
                        bestMove.set(move);
                        maxScore.set(score);
                    }
                }
        );

        return bestMove.get();
    }

    public Chromosome getChromosome() {
        return chromosome;
    }
}
