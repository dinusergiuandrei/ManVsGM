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
                parameters.populationSize,
                parameters.mutationRate,
                parameters.crossOverRate
        );
    }

    public void learnFrom(DataSet dataSet, Double minMoveMatchPercent){
        for(int run = 0; run<this.parameters.runsCount; ++run) {
            this.evaluator = new Evaluator(dataSet, minMoveMatchPercent);
            currentGeneration.initialize();
            for (int i = 0; i < this.parameters.iterationsCount; ++i) {
                currentGeneration.applyMutations();
                currentGeneration.selectNextGeneration();
            }
        }
    }

    @Override
    public Move getMove() {
        return null;
    }

}
