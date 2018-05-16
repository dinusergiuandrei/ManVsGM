package moveGenerator.geneticAlgorithm;


import utils.Functions;

public class GeneticAlgorithmParameters{
    private Integer populationSize;
    private Double mutationRate;
    private Double crossOverRate;
    private Integer iterationsCount;
    private Integer runsCount;
    private Double chromosomePrecision;
    private Integer chromosomeValueBitCount;
    private Functions function;
    private Boolean verbose;

    public Functions getFunction() {
        return function;
    }

    public GeneticAlgorithmParameters(
            Integer populationSize,
            Double mutationRate,
            Double crossOverRate,
            Integer iterationsCount,
            Integer runsCount,
            Double chromosomePrecision,
            Integer chromosomeValueBitCount,
            Functions function,
            Boolean verbose) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossOverRate = crossOverRate;
        this.iterationsCount = iterationsCount;
        this.runsCount = runsCount;
        this.chromosomePrecision = chromosomePrecision;
        this.chromosomeValueBitCount = chromosomeValueBitCount;
        this.function = function;
        this.verbose = verbose;
    }

    public Double getChromosomePrecision() {
        return chromosomePrecision;
    }

    public Integer getChromosomeValueBitCount() {
        return chromosomeValueBitCount;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public Double getMutationRate() {
        return mutationRate;
    }

    public Double getCrossOverRate() {
        return crossOverRate;
    }

    public Integer getIterationsCount() {
        return iterationsCount;
    }

    public Integer getRunsCount() {
        return runsCount;
    }

    public Boolean getVerbose() {
        return verbose;
    }
}