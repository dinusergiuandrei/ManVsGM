package MoveGenerator.GeneticAlgorithm;

import GameArchitecture.Move;
import GameArchitecture.Table;

import java.util.List;

public class Individual {
    Chromosome chromosome;

    public Double evaluate(Table table){
        return 0.0;
    }

    public Move getMove(Table table){
        return null;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }
}
