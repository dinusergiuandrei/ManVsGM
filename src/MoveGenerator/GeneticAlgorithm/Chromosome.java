package MoveGenerator.GeneticAlgorithm;

import ChessLogic.Features;
import GameArchitecture.Table;
import MoveGenerator.Functions;

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
    private List<Weight> weights = new ArrayList<>(Features.values().length);

    private Double precision;

    private Integer valueBitCount;

    public Chromosome(Double precision, Integer valueBitCount) {
        this.precision = precision;
        this.valueBitCount = valueBitCount;
    }

    public Double evaluate(Table table, Functions function){
        List<Double> values = new ArrayList<>(0);
        for (Features feature : Features.values()) {
            values.add(feature.evaluate(table));
        }
        return function.evaluate(this.weights, values);
    }

    public void mutate(Double mutationRate, Double minValue, Double maxValue){
        Chromosome newChromosome;
        List<Boolean> bits = computeBitsFromChromosome(this);
        for(int i=0; i<bits.size(); ++i){
            if(Math.random() < mutationRate){
                bits.set(i, !bits.get(i));
            }
        }
        newChromosome = computeChromosomeFromBits(bits, this.getPrecision(), this.getValueBitCount(), minValue, maxValue);

        this.weights = newChromosome.getWeights();
    }

    public static List<Chromosome> getChromosomesAfterCrossOver(
            Chromosome parent1, Chromosome parent2,
            Integer cuttingPoint,
            Double minValue,
            Double maxValue
    ){
        Double precision = parent1.getPrecision();
        Integer bitCount = parent1.getValueBitCount();
        List<Chromosome> chromosomes = new ArrayList<>();
        List<Boolean> bitsFromParent1 = computeBitsFromChromosome(parent1);
        List<Boolean> bitsFromParent2 = computeBitsFromChromosome(parent2);

        List<Boolean> bitsForChild1 = bitsFromParent1.subList(0, cuttingPoint);
        List<Boolean> bitsForChild2 = bitsFromParent2.subList(0, cuttingPoint);

        bitsForChild1.addAll(bitsFromParent2.subList(cuttingPoint, bitsFromParent2.size()-1));
        bitsForChild2.addAll(bitsFromParent1.subList(cuttingPoint, bitsFromParent1.size()-1));

        Chromosome child1 = computeChromosomeFromBits(bitsForChild1, precision, bitCount, minValue, maxValue);
        Chromosome child2 = computeChromosomeFromBits(bitsForChild2, precision, bitCount, minValue, maxValue);

        chromosomes.add(child1);
        chromosomes.add(child2);

        return chromosomes;
    }

    public static Chromosome computeChromosomeFromBits(List<Boolean> bits, Double precision, Integer bitCount, Double minValue, Double maxValue){
        Chromosome chromosome = new Chromosome(precision, bitCount);
        for(int i=0; i<Features.values().length; ++i){
            List<Boolean> bitValue = bits.subList(i*bitCount, (i+1)*bitCount);
            chromosome.getWeights().add(Weight.computeWeightFromBits(bitValue, precision, bitCount, minValue, maxValue));
        }
        return chromosome;
    }



    public static List<Boolean> computeBitsFromChromosome(Chromosome chromosome){

        List<Boolean> bits
                = new ArrayList<>(Features.values().length * chromosome.getValueBitCount());
        chromosome.getWeights().forEach(
                value -> bits.addAll(
                        Weight.computeBitsFromWeight(value)
                )
        );
        return bits;
    }



    public List<Weight> getWeights() {
        return weights;
    }

    public Double getPrecision() {
        return precision;
    }

    public Integer getValueBitCount() {
        return valueBitCount;
    }


    public void setWeights(List<Weight> weights) {
        this.weights = weights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chromosome that = (Chromosome) o;
        return Objects.equals(getWeights(), that.getWeights());
    }

}
