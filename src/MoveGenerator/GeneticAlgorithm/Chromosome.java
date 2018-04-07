package MoveGenerator.GeneticAlgorithm;

import MoveGenerator.Features;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A chromosome can hold no more than Features.values().length values.
 * (It can, but it makes no sense in the context of a genetic algorithm).
 * valueBitCount is the the number of bits each value is written on.
 * valueBitCount should be larger than ( (maxValue-minValue)/precision+1 ), or data loss occurs.
 */
public class Chromosome {
    private List<Double> values = new ArrayList<>(Features.values().length);

    private Double precision;

    private Integer valueBitCount;

    public Chromosome(Double precision, Integer valueBitCount) {
        this.precision = precision;
        this.valueBitCount = valueBitCount;
    }

    public static Chromosome computeChromosomeFromBits(List<Integer> bits, Double precision, Integer bitCount){
        Chromosome chromosome = new Chromosome(precision, bitCount);
        for(int i=0; i<Features.values().length; ++i){
            List<Integer> bitValue = bits.subList(i*bitCount, (i+1)*bitCount);
            chromosome.getValues().add(computeValueFromBits(bitValue, precision, bitCount));
        }
        return chromosome;
    }

    public static Double computeValueFromBits(List<Integer> bits, Double precision, Integer bitCount){
        Integer intValue = 0;
        Integer b = 1;
        Integer sign = bits.get(0);
        for(int i=1; i<bitCount; ++i){
            intValue += b * bits.get(i);
            b *= 2;
        }
        Double value = intValue * precision;
        if(sign == 0)
            value *= -1;
        return value;
    }

    public static List<Integer> computeBitsFromChromosome(Chromosome chromosome){
        List<Integer> bits
                = new ArrayList<>(Features.values().length * chromosome.getValueBitCount());
        chromosome.getValues().forEach(
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

    public static List<Integer> computeBitsFromDouble(Double x, Double precision, Integer bitCount){
        List<Integer> bits = new ArrayList<>(bitCount);
        Integer sign;
        if(x<0) {
            sign = 0;
            x = Math.abs(x);
        }
        else sign = 1;

        Integer value = (int) (x / precision);

        bits.add(sign);
        while(value>0){
            bits.add(value%2);
            value /= 2;
        }
//        Integer aux;
//        for(int i=0; i<bits.size()/2; ++i){
//            aux = bits.get(i);
//            bits.set(i, bits.get(bits.size()-i-1));
//            bits.set(bits.size()-i-1, aux);
//        }

        while(bits.size()<bitCount){
            bits.add(0);
        }
        return bits;
    }

    public List<Double> getValues() {
        return values;
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
        return Objects.equals(getValues(), that.getValues());
    }

}
