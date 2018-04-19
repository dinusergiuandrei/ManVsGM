package MoveGenerator.GeneticAlgorithm;

import ChessLogic.Features;
import MoveGenerator.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Weight {
    Double weightValue;

    Double minValue;

    Double maxValue;

    Double precision;

    Integer bitCount;

    public Weight(Double weightValue, Double precision, Integer bitCount, Double minValue, Double maxValue) {
        this.weightValue = weightValue;
        this.precision = precision;
        this.bitCount = bitCount;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }


    public static List<Boolean> computeBitsFromWeight(Weight weight){
        Double initialValue = weight.getWeightValue();
        Double multiplicationFactor = 1.0 / weight.getPrecision();
        Double maxValueOnBits = computeMaxValueOnBitsCount(weight.getBitCount() - 1) * weight.getPrecision();
        Double minValue = weight.getMinValue();
        Double intervalPoints = (weight.getMaxValue() - weight.getMinValue()) * multiplicationFactor;
        Double intervalLength = weight.getMaxValue() - weight.getMinValue();

        Double rawValue = ( (initialValue-minValue) / intervalLength * 2.0 - 1.0 ) * maxValueOnBits;

        return computeBitsFromValue(rawValue, weight.getPrecision(), weight.getBitCount());
    }

    public static Weight computeWeightFromBits(List<Boolean> bits, Double precision, Integer bitCount, Double minValue, Double maxValue){
        Double valueFromBits = computeValueFromBits(bits, precision, bitCount);
        Double multiplicationFactor = 1.0 / precision;
        Double intervalPoints = (maxValue - minValue) * multiplicationFactor;
        Double intervalLength = maxValue - minValue;
        Double maxValueOnBits = computeMaxValueOnBitsCount(bitCount-1) * precision;

        Double rawValue = (valueFromBits / maxValueOnBits + 1.0) / 2.0 * intervalLength + minValue;

        return new Weight(rawValue, precision, bitCount, minValue, maxValue);
    }

    public static List<Boolean> computeBitsFromValue(Double rawValue, Double precision, Integer bitCount){
        List<Boolean> bits = new ArrayList<>(bitCount);
        Boolean sign;
        if(rawValue<0) {
            sign = false;
            rawValue *= -1;
        }
        else sign = true;

        Double multiplicationFactor = 1.0 / precision;   // for precision at last decimal
        Integer value = (int) (rawValue * multiplicationFactor);

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

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public void setBitCount(Integer bitCount) {
        this.bitCount = bitCount;
    }

    public static Double computeMaxValueOnBitsCount(Integer bitsCount){
        Double maxValue = 0.0;
        Integer v = 1;
        for(int i=0; i<bitsCount; ++i){
            maxValue += v;
            v *= 2;
        }
        return maxValue;
    }

    public static List<Weight> computeRandomWeights(Functions function, Double precision, Integer bitCount){
        List<Weight> weights = new ArrayList<>(Features.values().length);

        for (int i = 0; i < Features.values().length; i++) {
            Double value = Math.random() * (function.getMaxValue() - function.getMinValue()) + function.getMinValue();
            weights.add(
                    new Weight(value, precision, bitCount, function.getMinValue(), function.getMaxValue())
            );
        }

        return weights;
    }

    public void setWeightValue(Double weightValue) {
        this.weightValue = weightValue;
    }

    public Double getPrecision() {
        return precision;
    }

    public Integer getBitCount() {
        return bitCount;
    }

    public static int getIntegerFromBoolean(boolean b){
        if(b)
            return 1;
        return 0;
    }

    public static boolean getBooleanFromInteger(int i){
        return i != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weight weight = (Weight) o;
        return this.weightValue.equals(weight.getWeightValue());
    }

    @Override
    public int hashCode() {

        return Objects.hash(weightValue, minValue, maxValue, precision, bitCount);
    }

    public Double getWeightValue() {
        return weightValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }
}
