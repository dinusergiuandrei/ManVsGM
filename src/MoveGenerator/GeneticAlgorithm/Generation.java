package MoveGenerator.GeneticAlgorithm;

import GameArchitecture.Table;
import MoveGenerator.Functions;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Generation {
    List<Individual> population = new LinkedList<>();

    Integer populationSize;

    Double mutationRate;

    Double crossOverRate;

    Double chromosomePrecision;

    Integer chromosomeValueBitCount;

    Functions function;

    public Generation(Integer populationSize, Double mutationRate, Double crossOverRate, Double chromosomePrecision, Integer chromosomeValueBitCount, Functions function) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossOverRate = crossOverRate;
        this.chromosomePrecision = chromosomePrecision;
        this.chromosomeValueBitCount = chromosomeValueBitCount;
        this.function = function;
    }

    public void initialize(){
        population.clear();
        for(int i = 0; i<this.populationSize; ++i){
            population.add(Individual.computeRandomIndividual(this.chromosomePrecision, this.chromosomeValueBitCount, this.function));
        }
    }

    public void applyGeneticOperators(Evaluator evaluator){
        applyMutations(function.getMinValue(), function.getMaxValue());
        applyCrossOver(evaluator);
    }

    public void applyMutations(Double minValue, Double maxValue){
        this.population.forEach(
                individual -> individual.getChromosome().mutate(mutationRate, minValue, maxValue)
        );
    }

    public void applyCrossOver(Evaluator evaluator){
        Map<Individual, Double> individualToValueMap = new LinkedHashMap<>();
        for (Individual individual : this.population) {
            evaluator.dataSet.getData().forEach(
                    dataSetEntry -> {
                        individualToValueMap.put(
                                individual,
                                evaluator.computeIndividualsEvaluationOfPosition(
                                        individual,
                                        Table.computeTableFromFen(dataSetEntry.getPosition())
                                )
                        );
                    }
            );
        }

        // mark individuals for crossover. cross over with the function in Individual
    }

    public void selectNextGeneration(Evaluator evaluator){

    }

    public List<Individual> getPopulation() {
        return population;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }
}
