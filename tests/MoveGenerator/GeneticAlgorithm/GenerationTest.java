package MoveGenerator.GeneticAlgorithm;

import MoveGenerator.Functions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class GenerationTest {
    Generation generation;

    @Before
    public void setUp() {
        List<Individual> population = new LinkedList<>();
        Integer populationSize = 10;
        Double mutationRate = 0.25;
        Double crossOverRate = 0.75;
        Double chromosomePrecision = 0.00001;
        Integer chromosomeValueBitCount = 24;
        Functions function = Functions.LinearCombination;
        generation
                = new Generation(
                        populationSize,
                        mutationRate,
                        crossOverRate,
                        chromosomePrecision,
                        chromosomeValueBitCount,
                        function
                );
    }

    @Test
    public void initializeTest(){
        generation.initialize();
        Integer expected = generation.getPopulationSize();
        Integer real = generation.getIndividuals().size();
        Assert.assertEquals(expected, real);
    }
}
