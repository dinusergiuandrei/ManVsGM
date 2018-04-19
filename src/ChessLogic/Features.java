package ChessLogic;

import GameArchitecture.Table;

public enum Features {
    CenterControl,
    KingSafety;

    public double evaluate(Table table){
        switch (this){
            case KingSafety:    return evaluateKingSafety(table);
            case CenterControl: return evaluateCenterControl(table);
            default:            return 0.0;
        }
    }

    private double evaluateCenterControl(Table table){
        return 2.0;
    }

    private double evaluateKingSafety(Table table){
        return 5.0;
    }
}
