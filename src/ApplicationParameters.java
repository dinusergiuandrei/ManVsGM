import MoveGenerator.Functions;
import MoveGenerator.GeneticAlgorithm.GeneticAlgorithmParameters;

public class ApplicationParameters {
    private String dataBasePath = "database/players";
    private String dataBasePathOpenings = "database/openings";
    private String dataBasePathAdams = "database/players/Adams.pgn";
    private String dataBasePathTest = "database/test/test.pgn";

    /**
     * The percent of games found to be analyzed.
     * This is useful for using only samples of the database,
     * which is too large to be loaded entirely.
     */
    private Double dataBaseLoadPercent = 1.0;

    /**
     * Parameters for the GeneticAlgorithm
     */
    private Integer populationSize = 5;
    private Double mutationRate = 0.01;
    private Double crossOverRate = 0.7;
    private Integer iterationsCount = 10;
    private Integer runsCount = 1;
    private Double chromosomePrecision = 0.00001;
    private Integer chromosomeValueBitCount = 24;

    private Double minMoveMatchPercent = 0.4;

    private Boolean verbose = true;

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
                this.function
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
}
