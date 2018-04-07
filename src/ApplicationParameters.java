import ChessLogic.DataSet;

public class ApplicationParameters {
    public String dataBasePath = "database/players";
    public String dataBasePathOpenings = "database/openings";
    public String dataBasePathAdams = "database/players/Adams.pgn";
    public String dataBasePathTest = "database/test/test.pgn";

    /**
     * The percent of games found to be analyzed.
     * This is useful for using only samples of the database,
     * which is too large to be loaded entirely.
     */
    public Double dataBaseLoadPercent = 1.0;

    /**
     * DataSet creates a connection between the positions (memorized in FEN format)
     * and the move which was played in the given position.
     */
    public DataSet dataSet = new DataSet();

    /**
     * Parameters for the GeneticAlgorithm
     */
    public Integer populationSize = 100;
    public Double mutationRate = 0.01;
    public Double crossOverRate = 0.7;
    public Integer iterations = 100;
    public Integer runs = 10;

    public Double minMoveMatchPercent = 0.4;
}
