package MoveGenerator.GeneticAlgorithm;
import ChessLogic.Game;
import ChessLogic.GameDetails;
import GameArchitecture.Move;
import MoveGenerator.MoveGenerator;

import java.util.LinkedList;
import java.util.List;

public class GeneticAlgorithm implements MoveGenerator {

    List<Individual> population = new LinkedList<>();

    Integer populationSize;

    Double mutationRate;

    Double crossOverRate;

    public GeneticAlgorithm(Integer populationSize, Double mutationRate, Double crossOverRate) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossOverRate = crossOverRate;
    }

    public void learnFrom(List<Game> games, Double minMoveMatchPercent){

    }

    @Override
    public Move getMove() {
        return null;
    }
}
