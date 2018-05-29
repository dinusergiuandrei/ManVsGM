package moveGenerator.geneticAlgorithm;

import database.Database;
import gameArchitecture.Move;
import gameArchitecture.Table;
import moveGenerator.MoveGenerator;
import utils.TimeKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GeneticAlgorithm implements MoveGenerator {

    private Generation currentGeneration;

    private Evaluator evaluator;

    private GeneticAlgorithmParameters parameters;

    private Individual bestIndividual;

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

    public void learnFrom(Database database, Double minMoveMatchPercent, Boolean verbose) {
        TimeKeeper gaTimeKeeper = new TimeKeeper();
        TimeKeeper runTimeKeeper = new TimeKeeper();
        TimeKeeper iterationTimeKeeper = new TimeKeeper();

        gaTimeKeeper.tic("Starting genetic algorithm learning...");

        Double bestScore = 0.0;

        List<Long> runningTimes = new ArrayList<>();

        init(database, minMoveMatchPercent);

        for (int run = 0; run < this.parameters.getRunsCount(); ++run) {

            runTimeKeeper.tic("Starting run : " + run);

            initializeFirstGeneration();
            for (int i = 0; i < this.parameters.getIterationsCount(); ++i) {
                iterationTimeKeeper.tic();
                computeScores(verbose);
                selectNextGeneration(verbose);
                applyGeneticOperators(verbose);
                iterationTimeKeeper.toc("Iteration " + i);
            }

            runningTimes.add(runTimeKeeper.toc("Run " + run + " completed "));

            Double bestRunScore = this.currentGeneration.computeBestScore(evaluator);
            Individual bestRunIndividual = this.currentGeneration.getBestIndividual();
            if (bestRunScore > bestScore) {
                bestScore = bestRunScore;
                bestIndividual = bestRunIndividual;
            }
        }

        gaTimeKeeper.toc("Genetic algorithm learning complete");

        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        runningTimes.forEach(
                time -> sum.updateAndGet(v -> v + time)
        );
        System.out.println("Average run time: " + sum.get() * 1.0 / parameters.getRunsCount() / 1000.0 + " seconds.");

        System.out.println("Best score : " + bestScore*100.0);

    }

    private void init(Database database, Double minMoveMatchPercent) {
        this.evaluator = new Evaluator(database, minMoveMatchPercent, parameters.getFunction());
    }

    private void initializeFirstGeneration() {
        System.out.println("Initializing first generation...");
        currentGeneration.initialize();
    }

    private void applyGeneticOperators(Boolean verbose) {
        if (verbose)
            System.out.println("Applying genetic operators...");
        Long startTime = System.currentTimeMillis();
        currentGeneration.applyGeneticOperators(this.evaluator);
        if (verbose)
            System.out.println("Applied genetic operators in: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds ");
    }

    private void selectNextGeneration(Boolean verbose) {
        if (verbose)
            System.out.println("Selecting next generation...");
        Long startTime = System.currentTimeMillis();
        currentGeneration.selectNextGeneration(this.evaluator);
        if (verbose)
            System.out.println("Selected next generation in: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds ");
    }

    private void computeScores(Boolean verbose) {
        if (verbose)
            System.out.println("Computing scores...");
        Long startTime = System.currentTimeMillis();
        this.currentGeneration.computeScore(this.evaluator);
        if (verbose)
            System.out.println("Computed scores in: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds ");
    }

    @Override
    public Move getMove(Table table) {
        return null;
    }

    public Individual getBestIndividual() {
        return bestIndividual;
    }
}
