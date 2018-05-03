package MoveGenerator.GeneticAlgorithm;
import ChessLogic.DataSet;
import GameArchitecture.Move;
import MoveGenerator.MoveGenerator;

public class GeneticAlgorithm implements MoveGenerator {

    private Generation currentGeneration;

    private Evaluator evaluator;

    private GeneticAlgorithmParameters parameters;

    public GeneticAlgorithm(GeneticAlgorithmParameters parameters) {
        this.parameters = parameters;
        this.currentGeneration = new Generation(
                parameters.getPopulationSize(),
                parameters.getMutationRate(),
                parameters.getCrossOverRate(),
                parameters.getChromosomePrecision(),
                parameters.getChromosomeValueBitCount(),
                parameters.getFunction()
        );
    }

    public void learnFrom(DataSet dataSet, Double minMoveMatchPercent){
        this.evaluator = new Evaluator(dataSet, minMoveMatchPercent, parameters.getFunction());
        System.out.println("Computing cache...");
        this.evaluator.computePositionFeaturesForAllPositions();
        System.out.println("Cache computed.");
        System.out.println("Running GA");
        for(int run = 0; run<this.parameters.getRunsCount(); ++run) {
            System.out.println("Run count: " + run);
            System.out.println("Initializing...");
            currentGeneration.initialize();
            for (int i = 0; i < this.parameters.getIterationsCount(); ++i) {
                System.out.println("Applying genetic operators");
                currentGeneration.applyGeneticOperators(this.evaluator);
                System.out.println("Selecting next generation");
                currentGeneration.selectNextGeneration(this.evaluator);
            }
        }
    }

    public void displayResults(){

    }

    @Override
    public Move getMove() {
        return null;
    }

}
