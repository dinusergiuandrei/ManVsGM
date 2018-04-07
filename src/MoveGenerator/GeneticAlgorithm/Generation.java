package MoveGenerator.GeneticAlgorithm;

import java.util.LinkedList;
import java.util.List;

public class Generation {
    List<Individual> population = new LinkedList<>();

    Integer populationSize;

    Double mutationRate;

    Double crossOverRate;

    public Generation(Integer populationSize, Double mutationRate, Double crossOverRate) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossOverRate = crossOverRate;
    }

    public void initialize(){

    }

    public void applyGeneticOperators(){
        applyMutations();
        applyCrossOver();
    }

    public void selectNextGeneration(){

    }

    public void applyMutations(){
        this.population.forEach(
                individual -> individual.getChromosome().mutate(mutationRate)
        );
    }

    public void applyCrossOver(){

    }
}
