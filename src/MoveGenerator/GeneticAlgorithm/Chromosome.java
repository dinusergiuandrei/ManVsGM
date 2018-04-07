package MoveGenerator.GeneticAlgorithm;

import MoveGenerator.Features;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A chromosome can hold no more than Features.weights().length weights.
 * (It can, but it makes no sense in the context of a genetic algorithm).
 * valueBitCount is the the number of bits each value is written on.
 * valueBitCount should be larger than ( (maxValue-minValue)/precision+1 ), or data loss occurs.
 */
public class Chromosome {
    private List<Double> weights = new ArrayList<>(Features.values().length);

    private Double precision;

    private Integer valueBitCount;

    public Chromosome(Double precision, Integer valueBitCount) {
        this.precision = precision;
        this.valueBitCount = valueBitCount;
    }

    public void mutate(Double mutationRate){
        Chromosome newChromosome;
        List<Boolean> bits = computeBitsFromChromosome(this);
        for(int i=0; i<bits.size(); ++i){
            if(Math.random() < mutationRate){
                bits.set(i, !bits.get(i));
            }
        }
        newChromosome = computeChromosomeFromBits(bits, this.getPrecision(), this.getValueBitCount());
        this.weights = newChromosome.getWeights();
    }
//
//    public static Chromosome mutate(Chromosome chromosome, Integer position){
//        List<Boolean> bits = computeBitsFromChromosome(chromosome);
//        bits.set(position, !bits.get(position));
//        return computeChromosomeFromBits(bits, chromosome.getPrecision(), chromosome.getValueBitCount());
//    }

    public static List<Chromosome> getChromosomesAfterCrossOver(
            Chromosome parent1,
            Chromosome parent2,
            Integer cuttingPoint){
        Double precision = parent1.getPrecision();
        Integer bitCount = parent1.getValueBitCount();
        List<Chromosome> chromosomes = new ArrayList<>();
        List<Boolean> bitsFromParent1 = computeBitsFromChromosome(parent1);
        List<Boolean> bitsFromParent2 = computeBitsFromChromosome(parent2);

        List<Boolean> bitsForChild1 = bitsFromParent1.subList(0, cuttingPoint);
        List<Boolean> bitsForChild2 = bitsFromParent2.subList(0, cuttingPoint);

        bitsForChild1.addAll(bitsFromParent2.subList(cuttingPoint, bitsFromParent2.size()-1));
        bitsForChild2.addAll(bitsFromParent1.subList(cuttingPoint, bitsFromParent1.size()-1));

        Chromosome child1 = computeChromosomeFromBits(bitsForChild1, precision, bitCount);
        Chromosome child2 = computeChromosomeFromBits(bitsForChild2, precision, bitCount);

        chromosomes.add(child1);
        chromosomes.add(child2);

        return chromosomes;
    }

    public static Chromosome computeChromosomeFromBits(List<Boolean> bits, Double precision, Integer bitCount){
        Chromosome chromosome = new Chromosome(precision, bitCount);
        for(int i=0; i<Features.values().length; ++i){
            List<Boolean> bitValue = bits.subList(i*bitCount, (i+1)*bitCount);
            chromosome.getWeights().add(computeValueFromBits(bitValue, precision, bitCount));
        }
        return chromosome;
    }

    public static Double computeValueFromBits(List<Boolean> bits, Double precision, Integer bitCount){
        Integer intValue = 0;
        Integer b = 1;
        Boolean sign = bits.get(0);
        for(int i=1; i<bitCount; ++i){
            intValue += b * getIntegerFromBoolean(bits.get(i));
            b *= 2;
        }
        Double value = intValue * precision;
        if(!sign)
            value *= -1;
        return value;
    }

    public static List<Boolean> computeBitsFromChromosome(Chromosome chromosome){
        List<Boolean> bits
                = new ArrayList<>(Features.values().length * chromosome.getValueBitCount());
        chromosome.getWeights().forEach(
                value -> bits.addAll(
                        computeBitsFromDouble(
                                value,
                                chromosome.getPrecision(),
                                chromosome.getValueBitCount()
                        )
                )
        );
        return bits;
    }

    public static List<Boolean> computeBitsFromDouble(Double x, Double precision, Integer bitCount){
        List<Boolean> bits = new ArrayList<>(bitCount);
        Boolean sign;
        if(x<0) {
            sign = false;
            x = Math.abs(x);
        }
        else sign = true;

        Integer value = (int) (x / precision);

        bits.add(sign);
        while(value>0){
            bits.add(getBooleanFromInteger( value%2 ));
            value /= 2;
        }

        while(bits.size()<bitCount){
            bits.add(false);
        }
        return bits;
    }

    public static int getIntegerFromBoolean(boolean b){
        if(b)
            return 1;
        return 0;
    }

    public static boolean getBooleanFromInteger(int i){
        return i != 0;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public Double getPrecision() {
        return precision;
    }

    public Integer getValueBitCount() {
        return valueBitCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chromosome that = (Chromosome) o;
        return Objects.equals(getWeights(), that.getWeights());
    }

}
