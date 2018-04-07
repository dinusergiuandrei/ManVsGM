package MoveGenerator.GeneticAlgorithm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ChromosomeTest {
    Chromosome chromosome;
    Double precision;
    Integer bitCount;

    @Before
    public void setUp(){
        precision = 0.01;
        bitCount = 60;
        chromosome = new Chromosome(precision, bitCount);
    }

    @Test
    public void chromosomeTest(){
        chromosome.getWeights().add(-5.0);
        chromosome.getWeights().add(-11.58);

        Chromosome otherChromosome = Chromosome.computeChromosomeFromBits(
                Chromosome.computeBitsFromChromosome(chromosome),
                precision,
                bitCount
        );

        Assert.assertEquals(otherChromosome.getWeights(), chromosome.getWeights());
    }

    @Test
    public void valueTest(){
        Double expected = 5.0;
        List<Boolean> bits = Chromosome.computeBitsFromDouble(
                5.0,
                0.1,
                8
        );
        Double real = Chromosome.computeValueFromBits(
                bits,
                0.1,
                8
        );
        Assert.assertEquals(expected, real);
    }
}
