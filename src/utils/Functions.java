package utils;

import moveGenerator.geneticAlgorithm.Weight;

import java.util.List;

public enum Functions {
    LinearCombination (0.0, 1.0);

    Double minValue;

    Double maxValue;

    public Double getMinValue() {
        return minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    Functions(Double minValue, Double maxValue){
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Double evaluate(List<Weight> weights, List<Double> values){
        Double score = 0.0;
        assert weights.size() == values.size();
        switch (this){
            case LinearCombination: {
                for(int i = 0; i<weights.size(); ++i){
                    score += weights.get(i).getWeightValue() * values.get(i);
                }
            }
        }
        return score;
    }
}