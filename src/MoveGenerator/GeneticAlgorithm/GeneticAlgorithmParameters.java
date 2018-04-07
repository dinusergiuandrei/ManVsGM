package MoveGenerator.GeneticAlgorithm;


public class GeneticAlgorithmParameters{
    Integer populationSize;
    Double mutationRate;
    Double crossOverRate;
    Integer iterationsCount;
    Integer runsCount;

    public GeneticAlgorithmParameters(
            Integer populationSize,
            Double mutationRate,
            Double crossOverRate,
            Integer iterationsCount,
            Integer runsCount) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossOverRate = crossOverRate;
        this.iterationsCount = iterationsCount;
        this.runsCount = runsCount;
    }
}