package MoveGenerator.GeneticAlgorithm;

import ChessLogic.Database;
import GameArchitecture.Move;
import MoveGenerator.MoveGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

    public void learnFrom(Database database, Double minMoveMatchPercent) {
        Double bestScore = 0.0;

        List<Long> runningTimes = new ArrayList<>();

        Long startTime = System.currentTimeMillis();

        init(database, minMoveMatchPercent);
        computeCache(database);

        System.out.println("Running genetic algorithm...");
        for (int run = 0; run < this.parameters.getRunsCount(); ++run) {
            Long runStartTime = System.currentTimeMillis();
            System.out.println("Starting run : " + run);

            initializeFirstGeneration();
            for (int i = 0; i < this.parameters.getIterationsCount(); ++i) {
                computeScores();
                selectNextGeneration();
                applyGeneticOperators();
            }
            double runTime = (System.currentTimeMillis() - runStartTime) / 1000.0;
            System.out.println("Run " + run + " completed in " + runTime + " seconds.");

            Double bestRunScore = this.currentGeneration.computeBestScore(evaluator);
            if (bestRunScore > bestScore) {
                bestScore = bestRunScore;
            }
        }

        System.out.println("Total genetic algorithm running time: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds.");

        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        runningTimes.forEach(
                time -> sum.updateAndGet(v -> v + time)
        );
        System.out.println("Average run time: " + sum.get() * 1.0 / parameters.getRunsCount());

        System.out.println("Best score : " + bestScore);

    }

    private void init(Database database, Double minMoveMatchPercent) {
        this.evaluator = new Evaluator(database, minMoveMatchPercent, parameters.getFunction());
    }

    private void computeCache(Database database) {
        System.out.println("Computing cache...");
        Long startTime = System.currentTimeMillis();
        database.computeCache();
        System.out.println("Computed cache in: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds ");
    }

    private void initializeFirstGeneration() {
        System.out.println("Initializing first generation...");
        currentGeneration.initialize();
    }

    private void applyGeneticOperators() {
        System.out.println("Applying genetic operators...");
        Long startTime = System.currentTimeMillis();
        currentGeneration.applyGeneticOperators(this.evaluator);
        System.out.println("Applied genetic operators in: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds ");
    }

    private void selectNextGeneration() {
        System.out.println("Selecting next generation...");
        Long startTime = System.currentTimeMillis();
        currentGeneration.selectNextGeneration(this.evaluator);
        System.out.println("Selected next generation in: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds ");
    }

    private void computeScores() {
        System.out.println("Computing scores...");
        Long startTime = System.currentTimeMillis();
        this.currentGeneration.computeScore(this.evaluator);
        System.out.println("Computed scores in: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds ");
    }

    @Override
    public Move getMove() {
        return null;
    }

}
