package MoveGenerator.GeneticAlgorithm;

import ChessLogic.DataSet;
import GameArchitecture.Move;
import GameArchitecture.Table;
import MoveGenerator.Functions;

import java.util.ArrayList;

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


    public Move getMove(Table table){
        //of all the possible next moves, choose the move that leads to best position
        return null;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }
}
