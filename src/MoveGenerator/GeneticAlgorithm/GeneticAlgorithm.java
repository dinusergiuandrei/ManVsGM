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
        Long startTime = System.currentTimeMillis();

        init(dataSet, minMoveMatchPercent);
        computeCache(dataSet);

        System.out.println("Running genetic algorithm...");
        for(int run = 0; run<this.parameters.getRunsCount(); ++run) {
            Long runStartTime = System.currentTimeMillis();
            System.out.println("Starting run : " + run);

            initializeFirstGeneration();
            for (int i = 0; i < this.parameters.getIterationsCount(); ++i) {
                //applyGeneticOperators();
                //selectNextGeneration();
            }

            System.out.println("Run " + run + " completed in " + (System.currentTimeMillis()/runStartTime) / 1000.0 + " seconds.");
        }

        System.out.println("Total genetic algorithm running time: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds.");
    }

    private void init(DataSet dataSet, Double minMoveMatchPercent){
        this.evaluator = new Evaluator(dataSet, minMoveMatchPercent, parameters.getFunction());
    }

    private void computeCache(DataSet dataSet){
        System.out.println("Computing cache...");
        Long startTime = System.currentTimeMillis();
        dataSet.computeCache();
        System.out.println("Computed cache in: " + (System.currentTimeMillis()-startTime) / 1000.0 + " seconds ");
    }

    private void initializeFirstGeneration(){
        System.out.println("Initializing first generation...");
        currentGeneration.initialize();
    }

    private void applyGeneticOperators(){
        System.out.println("Applying genetic operators...");
        Long startTime = System.currentTimeMillis();
        currentGeneration.applyGeneticOperators(this.evaluator);
        System.out.println("Applied genetic operators in: " + (System.currentTimeMillis()-startTime) / 1000.0 + " seconds ");
    }

    private void selectNextGeneration(){
        System.out.println("Selecting next generation...");
        Long startTime = System.currentTimeMillis();
        currentGeneration.selectNextGeneration(this.evaluator);
        System.out.println("Selected next generation in: " + (System.currentTimeMillis()-startTime) / 1000.0 + " seconds ");
    }

    @Override
    public Move getMove() {
        return null;
    }

}
