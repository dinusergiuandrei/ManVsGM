package ChessLogic;

import GameArchitecture.Table;

/**
 * Each feature will describe some aspect of a chess position.
 * All evaluations of features must be between 0 and 1.0
 * To add a new feature Feature:
 *  1. Implement the private double evaluateFeature(Table table) method
 *      which returns a value between 0 and 1.
 *      The higher the return value, the more relevant the feature is in the given position.
 *  2. Add Feature in the list of features.
 *  3. In the switch in the public evaluate(Table table) method add the
 *      case Feature: return evaluateFeature(table);
 */
public enum Features {
    CenterControl,
    KingSafety,
    PieceActivity,
    ConnectedRooks,
    KnightOutpostPossibility,
    KnightActivity,
    BishopActivity,
    ActiveQueen,
    ActiveKing,
    PawnIslands,
    IsolatedPawns,
    PieceCount
    ;

    public double evaluate(Table table){
        switch (this){
            case KingSafety:               return evaluateKingSafety(table);
            case CenterControl:            return evaluateCenterControl(table);
            case PieceActivity:            return evaluatePieceActivity(table);
            case ConnectedRooks:           return evaluateConnectedRooks(table);
            case KnightOutpostPossibility: return evaluateKnightOutpostPossibility(table);
            case KnightActivity:           return evaluateKnightActivity(table);
            case BishopActivity:           return evaluateBishopActivity(table);
            case ActiveQueen:              return evaluateActiveQueen(table);
            case ActiveKing:               return evaluateActiveKing(table);
            case PawnIslands:              return evaluatePawnIslands(table);
            case IsolatedPawns:            return evaluateIsolatedPawns(table);
            case PieceCount:               return evaluatePieceCount(table);
            default:                       return 0.0;
        }
    }

    private double evaluateCenterControl(Table table){
        return 0.2;
    }

    private double evaluateKingSafety(Table table){
        return 0.5;
    }

    private double evaluatePieceActivity(Table table){
        return 0.1;
    }

    private double evaluateConnectedRooks(Table table){
        return 0.005;
    }

    private double evaluateKnightOutpostPossibility(Table table){
        return 0.03;
    }

    private double evaluateKnightActivity(Table table){
        return 0.23;
    }

    private double evaluateBishopActivity(Table table){
        return 0.24;
    }

    private double evaluateActiveQueen(Table table){
        return 0.30;
    }

    private double evaluateActiveKing(Table table){
        return 0.015;
    }

    private double evaluatePawnIslands(Table table){
        return 0.25;
    }

    private double evaluateIsolatedPawns(Table table){
        return 0.3;
    }

    private double evaluatePieceCount(Table table){
        return 0.8;
    }


}
