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
        this.evaluator.computePositionFeaturesForAllPositions();
        for(int run = 0; run<this.parameters.getRunsCount(); ++run) {
            currentGeneration.initialize();
            for (int i = 0; i < this.parameters.getIterationsCount(); ++i) {
                currentGeneration.applyGeneticOperators(this.evaluator);
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
