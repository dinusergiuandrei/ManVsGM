package MoveGenerator.GeneticAlgorithm;

import ChessLogic.Features;
import MoveGenerator.Functions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ChromosomeTest {
    Chromosome chromosome;
    Double precision;
    Integer bitCount;
    Functions function;

    @Before
    public void setUp() {
        precision = 0.1;
        bitCount = 9;
        chromosome = new Chromosome(precision, bitCount);
        function = Functions.LinearCombination;
    }

    @Test
    public void chromosomeManipulationTest() {
        chromosome.getWeights().add(
                new Weight(5.0, precision, bitCount, function.getMinValue(), function.getMaxValue())
        );
        chromosome.getWeights().add(
                new Weight(-9.5, precision, bitCount, function.getMinValue(), function.getMaxValue())
        );

        Chromosome otherChromosome = Chromosome.computeChromosomeFromBits(
                Chromosome.computeBitsFromChromosome(chromosome),
                precision,
                bitCount,
                function.getMinValue(),
                function.getMaxValue()
        );

        for (int i = 0; i < chromosome.getWeights().size(); ++i) {
            Double expected = chromosome.getWeights().get(i).getWeightValue();
            Double real = otherChromosome.getWeights().get(i).getWeightValue();

            //Assert.assertEquals(expected, real);
            Assert.assertEquals(true, Math.abs(expected - real) < precision);
        }

    }

    @Test
    public void weightManipulationTest() {
        Weight expected = new Weight(5.0, precision, bitCount, function.getMinValue(), function.getMaxValue());

        List<Boolean> bits = Weight.computeBitsFromWeight(expected);
        Weight real = Weight.computeWeightFromBits(
                bits,
                precision,
                bitCount,
                function.getMinValue(),
                function.getMaxValue()
        );
        //Assert.assertEquals(expected.getWeightValue(), real.getWeightValue()); // remove this
        Assert.assertEquals(true, Math.abs(real.getWeightValue() - expected.getWeightValue()) <= precision);
    }

    @Test
    public void valueManipulationTest() {
        Double expected = 6.31;
        Double real = Weight.computeValueFromBits(
                Weight.computeBitsFromValue(expected, precision, bitCount),
                precision,
                bitCount
        );
        //Assert.assertEquals(expected, real);
        Assert.assertEquals(true, Math.abs(expected - real) <= precision);
    }

    @Test
    public void mutateTest() {
        Double initialWeightValue = 5.0;
        Double mutationRate = 0.5;
        Integer mutationsCount = 100;

        for(int i=0; i<Features.values().length; ++i) {
            chromosome.getWeights().add(
                    new Weight(initialWeightValue, precision, bitCount, function.getMinValue(), function.getMaxValue())
            );
        }

        for(int i=0; i<mutationsCount; ++i) {
            chromosome.mutate(mutationRate, function.getMinValue(), function.getMaxValue());

            Assert.assertEquals(true, chromosome.getWeights().get(0).getWeightValue() >= function.getMinValue());
            Assert.assertEquals(true, chromosome.getWeights().get(0).getWeightValue() <= function.getMaxValue());
        }
    }
}
