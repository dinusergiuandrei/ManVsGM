package MoveGenerator.GeneticAlgorithm;

import GameArchitecture.Table;
import MoveGenerator.Functions;

import java.util.*;

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

    public void initialize() {
        population.clear();
        for (int i = 0; i < this.populationSize; ++i) {
            population.add(Individual.computeRandomIndividual(this.chromosomePrecision, this.chromosomeValueBitCount, this.function));
        }
    }

    public void applyGeneticOperators(Evaluator evaluator) {
        applyCrossOver(evaluator, function.getMinValue(), function.getMaxValue());
        applyMutations(function.getMinValue(), function.getMaxValue());
    }

    public void applyMutations(Double minValue, Double maxValue) {
        this.population.forEach(
                individual -> individual.getChromosome().mutate(mutationRate, minValue, maxValue)
        );
    }

    public void applyCrossOver(Evaluator evaluator, Double minValue, Double maxValue) {
        List<Individual> markedForCrossOver = new ArrayList<>();
        for (Individual individual : this.population) {
            if (Math.random() < crossOverRate) {
                markedForCrossOver.add(individual);
            }
        }

        for (int i = 0; i < markedForCrossOver.size() - 1; i += 2) {
            Individual parent1 = markedForCrossOver.get(i);
            Individual parent2 = markedForCrossOver.get(i + 1);

            Double cuttingPointDouble = parent1.getChromosome().getWeights().size() * Math.random();
            Integer cuttingPoint = cuttingPointDouble.intValue();
            if(cuttingPoint == parent1.getChromosome().getWeights().size()){
                --cuttingPoint;
            }

            List<Chromosome> children
                    = Chromosome.getChromosomesAfterCrossOver(
                    parent1.getChromosome(),
                    parent2.getChromosome(),
                    cuttingPoint,
                    minValue,
                    maxValue
            );

            this.population.remove(parent1);
            this.population.remove(parent2);

            Chromosome chromosome1 = children.get(0);
            Chromosome chromosome2 = children.get(1);

            Individual individual1 = new Individual(parent1.getChromosome().getPrecision(), parent1.getChromosome().getValueBitCount());
            individual1.setChromosome(chromosome1);

            Individual individual2 = new Individual(parent1.getChromosome().getPrecision(), parent1.getChromosome().getValueBitCount());
            individual2.setChromosome(chromosome2);

            this.population.add(individual1);
            this.population.add(individual2);

        }

    }

    public void selectNextGeneration(Evaluator evaluator) {
        Map<Individual, Double> individualToValueMap = new LinkedHashMap<>();
        for (Individual individual : this.population) {
            evaluator.database.getData().forEach(
                    dataSetEntry -> {
                        individualToValueMap.put(
                                individual,
                                evaluator.computeIndividualsEvaluationOfPosition(
                                        individual,
                                        evaluator.database.getFenToTableMap().get(dataSetEntry.getPositionFenString())
                                )
                        );
                    }
            );
        }
    }

    public List<Individual> getPopulation() {
        return population;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }
}
