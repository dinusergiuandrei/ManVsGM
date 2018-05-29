package app;

import moveGenerator.geneticAlgorithm.GeneticAlgorithmParameters;
import utils.Functions;

public class ApplicationParameters {
    private Operation operation = Operation.RunGaComputingDatabase;

    private String dataBasePath = "database/players";
    private String dataBasePathOpenings = "database/openings";
    private String dataBasePathAdams = "database/players/Adams.pgn";
    private String dataBasePathTest = "database/test/test.pgn";

    private String dataBaseStreamSavePath = "database/stream/database.dat";

    private String bestIndividualPath = "database/stream/best_individual.dat";

    public String getDataBaseStreamSavePath() {
        return dataBaseStreamSavePath;
    }

    /**
     * The percent of games found to be analyzed.
     * This is useful for using only samples of the database,
     * which is too large to be loaded entirely.
     */
    private Double dataBaseLoadPercent = 0.0001;

    /**
     * Parameters for the geneticAlgorithm
     */
    private Integer populationSize = 30;
    private Double mutationRate = 0.01;
    private Double crossOverRate = 0.7;
    private Integer iterationsCount = 20;
    private Integer runsCount = 1;
    private Double chromosomePrecision = 0.001;
    private Integer chromosomeValueBitCount = 11;
    private Double minMoveMatchPercent = 0.4;
    private Boolean verbose = false;
    private Functions function = Functions.LinearCombination;

    public GeneticAlgorithmParameters computeGeneticAlgorithmParameters(){
        return new GeneticAlgorithmParameters(
                this.populationSize,
                this.mutationRate,
                this.crossOverRate,
                this.iterationsCount,
                this.runsCount,
                this.chromosomePrecision,
                this.chromosomeValueBitCount,
                this.function,
                this.verbose
        );
    }
    public Boolean getVerbose() {
        return verbose;
    }
    public String getDataBasePathAdams() {
        return dataBasePathAdams;
    }
    public Double getDataBaseLoadPercent() {
        return dataBaseLoadPercent;
    }
    public Double getMinMoveMatchPercent() {
        return minMoveMatchPercent;
    }
    public String getDataBasePath() {
        return dataBasePath;
    }
    public Functions getFunction() {
        return function;
    }
    public Double getChromosomePrecision() {
        return chromosomePrecision;
    }
    public Integer getChromosomeValueBitCount() {
        return chromosomeValueBitCount;
    }
    public Operation getOperation() {
        return operation;
    }

    public String getBestIndividualPath() {
        return bestIndividualPath;
    }

    public enum Operation{
        RunGaLoadingDatabase,
        RunGaComputingDatabase,
        ComputeAndSaveDatabase,
        PlayGamePlayerVsPlayer,
        PlayGamePlayerVsComputer
    }
}
