package MoveGenerator.GeneticAlgorithm;

import MoveGenerator.Functions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Generation {
    List<Individual> individuals = new ArrayList<>();

    Map<Individual, Double> individualToValue = new LinkedHashMap<>();

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
        individuals.clear();
        for (int i = 0; i < this.populationSize; ++i) {
            individuals.add(Individual.computeRandomIndividual(this.chromosomePrecision, this.chromosomeValueBitCount, this.function));
        }
    }

    public void applyGeneticOperators(Evaluator evaluator) {
        applyCrossOver(evaluator, function.getMinValue(), function.getMaxValue());
        applyMutations(function.getMinValue(), function.getMaxValue());
    }

    public void applyMutations(Double minValue, Double maxValue) {
        List<Individual> newGeneration = new ArrayList<>();
        this.individuals.forEach(
                individual -> {
                    Individual newIndividual = Individual.computeIndividualFromChromosome(individual.getChromosome().mutate(mutationRate, minValue, maxValue));
                    newGeneration.add(newIndividual);
                }
        );
        this.individuals = newGeneration;
    }

    public void applyCrossOver(Evaluator evaluator, Double minValue, Double maxValue) {
        List<Individual> markedForCrossOver = new ArrayList<>();
        for (Individual individual : this.individuals) {
            if (Math.random() < crossOverRate) {
                markedForCrossOver.add(individual);
            }
        }

        for (int i = 0; i < markedForCrossOver.size() - 1; i += 2) {
            Individual parent1 = markedForCrossOver.get(i);
            Individual parent2 = markedForCrossOver.get(i + 1);

            Double cuttingPointDouble = parent1.getChromosome().getWeights().size() * parent1.getChromosome().getValueBitCount() * Math.random();
            Integer cuttingPoint = cuttingPointDouble.intValue();
            if (cuttingPoint == parent1.getChromosome().getWeights().size()) {
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

            this.individuals.remove(parent1);
            this.individuals.remove(parent2);

            Chromosome chromosome1 = children.get(0);
            Chromosome chromosome2 = children.get(1);

            Individual individual1 = new Individual(parent1.getChromosome().getPrecision(), parent1.getChromosome().getValueBitCount());
            individual1.setChromosome(chromosome1);

            Individual individual2 = new Individual(parent1.getChromosome().getPrecision(), parent1.getChromosome().getValueBitCount());
            individual2.setChromosome(chromosome2);

            this.individuals.add(individual1);
            this.individuals.add(individual2);
        }
    }

    public void computeScore(Evaluator evaluator) {
        final Double[] minScore = {1.0};
        for (Individual individual : this.individuals) {
            Double evaluation = evaluator.evaluateIndividual(individual);
            individualToValue.put(individual, evaluation);
            if (evaluation < minScore[0]) {
                minScore[0] = evaluation;
            }
        }

        individualToValue.keySet().forEach(
                individual -> {
                    Double value = individualToValue.get(individual);
                    individualToValue.put(individual, value - minScore[0]);
                }
        );
    }

    public Double computeBestScore(Evaluator evaluator){
        computeScore(evaluator);
        AtomicReference<Individual> bestIndividual = new AtomicReference<>();
        AtomicReference<Double> bestScore = new AtomicReference<>(0.0);
        this.individualToValue.forEach(
                (individual, score) -> {
                    if(score> bestScore.get()){
                        bestScore.set(score);
                        bestIndividual.set(individual);
                    }
                }
        );
        return bestScore.get();
    }

    public void selectNextGeneration(Evaluator evaluator) {
        AtomicReference<Double> atomicSum = new AtomicReference<>(0.0);
        this.individualToValue.forEach(
                (individual, score) -> {
                    atomicSum.updateAndGet(v -> v + score);
                }
        );

        Map<Individual, Double> individualToProbabilityMap = new LinkedHashMap<>();
        this.individualToValue.forEach(
                (individual, score) -> {
                    individualToProbabilityMap.put(individual, score / atomicSum.get());
                }
        );

        List<Individual> nextGeneration = new ArrayList<>();
        for (Integer i = 0; i < this.populationSize; i++) {
            Double randomValue = Math.random();
            Integer j;
            for (j = 0; j < this.individuals.size() && randomValue > 0; ++j) {
                randomValue -= individualToProbabilityMap.get(this.individuals.get(i));
            }
            --j;
            nextGeneration.add(this.individuals.get(j));
        }

        this.individuals = nextGeneration;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }
}
