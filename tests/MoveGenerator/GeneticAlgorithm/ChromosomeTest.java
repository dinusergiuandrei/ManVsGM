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
        precision = 0.001;
        bitCount = 11;
        chromosome = new Chromosome(precision, bitCount);
        function = Functions.LinearCombination;
    }

    @Test
    public void chromosomeManipulationTest() {
        for (Features features : Features.values()) {
            chromosome.getWeights().add(
                    new Weight(
                            Math.random(),
                            precision,
                            bitCount,
                            function.getMinValue(),
                            function.getMaxValue()
                    )
            );
        }

        List<Boolean> bits = Chromosome.computeBitsFromChromosome(chromosome);

        Chromosome otherChromosome = Chromosome.computeChromosomeFromBits(
                bits,
                precision,
                bitCount,
                function.getMinValue(),
                function.getMaxValue()
        );

        for (int i = 0; i < chromosome.getWeights().size(); ++i) {
            Double expected = chromosome.getWeights().get(i).getWeightValue();
            Double real = otherChromosome.getWeights().get(i).getWeightValue();

            //Assert.assertEquals(expected, real);
            Assert.assertTrue(Math.abs(expected - real) < precision);
        }

    }

    @Test
    public void weightManipulationTest() {
        Weight expected = new Weight(0.999995, precision, bitCount, function.getMinValue(), function.getMaxValue());

        List<Boolean> bits = Weight.computeBitsFromWeight(expected);
        Weight real = Weight.computeWeightFromBits(
                bits,
                precision,
                bitCount,
                function.getMinValue(),
                function.getMaxValue()
        );
        //Assert.assertEquals(expected.getWeightValue(), real.getWeightValue()); // remove this
        Assert.assertTrue(Math.abs(real.getWeightValue() - expected.getWeightValue()) <= precision);
    }

    @Test
    public void valueManipulationTest() {
        Double expected = 0.634;

        List<Boolean> bits = Weight.computeBitsFromValue(expected, precision, bitCount);

        Double real = Weight.computeValueFromBits(
                bits,
                precision,
                bitCount
        );
        //Assert.assertEquals(expected, real);
        Assert.assertTrue(Math.abs(expected - real) <= precision);
    }

    @Test
    public void mutateTest() {
        Double initialWeightValue = 0.5;
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
