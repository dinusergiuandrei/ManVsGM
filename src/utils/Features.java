package utils;

import chessLogic.validators.SyntacticMoveValidator;
import gameArchitecture.Color;
import gameArchitecture.Piece;
import gameArchitecture.Square;
import gameArchitecture.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Each feature will describe some aspect of a chess position.
 * All evaluations of features must be between 0 and 1.0
 * To add a new feature Feature:
 * 1. Implement the private double evaluateFeature(Table table) method
 * which returns a value between 0 and 1.
 * The higher the return value, the more relevant the feature is in the given position.
 * 2. Add Feature in the list of features.
 * 3. In the switch in the public evaluate(Table table) method add the
 * case Feature: return evaluateFeature(table);
 */

public enum Features {
    CenterControl,
    KingSafety,
    ConnectedRooks,
    KnightOutpostPossibility,
    KnightActivity,
    BishopActivity,
    ActiveQueen,
    ActiveKing,
    IsolatedPawns,
    PieceCount;

    public double evaluate(Table table) {
        switch (this) {
            case KingSafety:
                return evaluateKingSafety(table);
            case CenterControl:
                return evaluateCenterControl(table);
            case ConnectedRooks:
                return evaluateConnectedRooks(table);
            case KnightOutpostPossibility:
                return evaluateKnightOutpostPossibility(table);
            case KnightActivity:
                return evaluateKnightActivity(table);
            case BishopActivity:
                return evaluateBishopActivity(table);
            case ActiveQueen:
                return evaluateActiveQueen(table);
            case ActiveKing:
                return evaluateActiveKing(table);
            case IsolatedPawns:
                return evaluateIsolatedPawns(table);
            case PieceCount:
                return evaluatePieceCount(table);
            default:
                return 0.0;
        }
    }

    private double evaluateCenterControl(Table table) {
        Color toMove = table.getToMove();
        Color otherColor = toMove.getOtherColor();
        List<Piece> piecesInSmallCenter = new ArrayList<>();
        List<Piece> piecesInLargeCenter = new ArrayList<>();

        Map<Square, Piece> pieceMap = table.getSquareToPieceMap();

        piecesInSmallCenter.add(pieceMap.get(Square.e4));
        piecesInSmallCenter.add(pieceMap.get(Square.d4));
        piecesInSmallCenter.add(pieceMap.get(Square.e5));
        piecesInSmallCenter.add(pieceMap.get(Square.d5));

        piecesInLargeCenter.add(pieceMap.get(Square.c3));
        piecesInLargeCenter.add(pieceMap.get(Square.d3));
        piecesInLargeCenter.add(pieceMap.get(Square.e3));
        piecesInLargeCenter.add(pieceMap.get(Square.f3));

        piecesInLargeCenter.add(pieceMap.get(Square.c6));
        piecesInLargeCenter.add(pieceMap.get(Square.d6));
        piecesInLargeCenter.add(pieceMap.get(Square.e6));
        piecesInLargeCenter.add(pieceMap.get(Square.f6));

        piecesInLargeCenter.add(pieceMap.get(Square.c4));
        piecesInLargeCenter.add(pieceMap.get(Square.c5));

        piecesInLargeCenter.add(pieceMap.get(Square.f4));
        piecesInLargeCenter.add(pieceMap.get(Square.f5));

        Integer ownPiecesInSmallCenter = 0;
        Integer ownPiecesInLargeCenter = 0;
        Integer oppositePiecesInSmallCenter = 0;
        Integer oppositePiecesInLargeCenter = 0;

        for (Piece piece : piecesInSmallCenter)
            if (piece != Piece.noPiece) {
                if (piece.getColor() == toMove)
                    ++ownPiecesInSmallCenter;
                if (piece.getColor() == otherColor)
                    ++oppositePiecesInSmallCenter;
            }


        for (Piece piece : piecesInLargeCenter)
            if (piece != Piece.noPiece) {
                if (piece.getColor() == toMove)
                    ++ownPiecesInLargeCenter;
                if (piece.getColor() == otherColor)
                    ++oppositePiecesInLargeCenter;
            }



        /*
         *  a = 0 4    +
         *  b = 0 4    -
         *  a + b < = 4
         *  c = 0 12   +
         *  d = 0 12   -
         *  c + d <= 12
         *  f(a, b, c, d) = 0 1
         *  df/da>=0
         *  df/db<=0
         *  df/dc>=0
         *  df/dd<=0
         */

        Double score1 = ((ownPiecesInSmallCenter - oppositePiecesInSmallCenter) / 4.0 + 1.0) / 2.0;
        Double score2 = ((ownPiecesInLargeCenter - oppositePiecesInLargeCenter) / 12.0 + 1.0) / 2.0;


        Double score = score1 / 2.0 * 0.7 + score2 / 2.0 * 0.3;

        return score;
    }

    private double evaluateKingSafety(Table table) {
        Color toMove = table.getToMove();
        Square kingSquare = SyntacticMoveValidator.getKingSquare(table, toMove);
        List<Square> neighborSquares = Square.getNeighborSquares(kingSquare);
        Map<Square, Piece> pieceMap = table.getSquareToPieceMap();

        Integer ownPiecesClose = 0;
        Integer opponentPiecesClose = 0;

        for (Square square : neighborSquares) {
            if (square != null && pieceMap.get(square) != null) {
                if (pieceMap.get(square).getColor() == toMove) {
                    ++ownPiecesClose;
                }
                if (pieceMap.get(square).getColor() == toMove.getOtherColor()) {
                    ++opponentPiecesClose;
                }
            }
        }

        Double score = ((ownPiecesClose - opponentPiecesClose) / 8.0 + 1.0) / 2.0;

        return score;
    }

    private double evaluateConnectedRooks(Table table) {
        Color color = table.getToMove();
        List<Square> rookSquares;
        Piece piece = null;
        if (color == Color.White)
            piece = Piece.whiteRook;
        if (color == Color.Black)
            piece = Piece.blackRook;
        rookSquares = table.getPositionsOfPiece(piece);
        if (rookSquares.size() <= 1) {
            return 0.0;
        }

        if (rookSquares.size() == 2) {
            if (rookSquares.get(0).getLine() == rookSquares.get(1).getLine())
                return 0.8;
            if (rookSquares.get(0).getColumn() == rookSquares.get(1).getColumn())
                return 1.0;
        }

        if (rookSquares.size() > 2) {
            int columnMatch = 0;
            int lineMatch = 0;
            int sum = 0;
            for (int i = 0; i < rookSquares.size(); i++) {
                for (int j = i + 1; j < rookSquares.size(); j++) {
                    if (rookSquares.get(i).getLine() == rookSquares.get(j).getLine())
                        ++lineMatch;
                    if (rookSquares.get(i).getColumn() == rookSquares.get(j).getColumn())
                        ++columnMatch;
                    ++sum;
                }
            }
            return 0.8 * (lineMatch / sum) + (columnMatch / sum);
        }

        return 0.0;
    }

    private double evaluateKnightOutpostPossibility(Table table) {
        return 0.03;
    }

    private double evaluateKnightActivity(Table table) {
        Color color = table.getToMove();
        Piece piece = null;
        if(color == Color.White){
            piece = Piece.whiteKnight;
        }
        if(color == Color.Black){
            piece = Piece.blackKnight;
        }

        List<Square> squares = table.getPositionsOfPiece(piece);

        double result = 0.0;
        for (Square square : squares) {
            int advance = 0;
            if(color == Color.White){
                advance = square.getLine() ;
            }
            if(color == Color.Black){
                advance = 9 - square.getLine();
            }
            result += advance/8 + Math.max(Math.abs(square.getColumn() - 'd'), Math.abs(square.getColumn() - 'e'));
        }

        return result/squares.size();
    }

    private double evaluateBishopActivity(Table table) {
        Color color = table.getToMove();
        Piece piece = null;
        if(color == Color.White){
            piece = Piece.whiteBishop;
        }
        if(color == Color.Black){
            piece = Piece.blackBishop;
        }

        List<Square> squares = table.getPositionsOfPiece(piece);

        double result = 0.0;
        for (Square square : squares) {
            int advance = 0;
            if(color == Color.White){
                advance = square.getLine() ;
            }
            if(color == Color.Black){
                advance = 9 - square.getLine();
            }
            result += advance/8 + Math.max(Math.abs(square.getColumn() - 'd'), Math.abs(square.getColumn() - 'e'));
        }

        return result/squares.size();
    }

    private double evaluateActiveQueen(Table table) {
        Color color = table.getToMove();
        Piece piece = null;
        if(color == Color.White){
            piece = Piece.whiteQueen;
        }
        if(color == Color.Black){
            piece = Piece.blackQueen;
        }

        List<Square> queenSquares = table.getPositionsOfPiece(piece);

        double result = 0.0;
        for (Square square : queenSquares) {
            int advance = 0;
            if(color == Color.White){
                advance = square.getLine() ;
            }
            if(color == Color.Black){
                advance = 9 - square.getLine();
            }
            result += advance/8 + Math.max(Math.abs(square.getColumn() - 'd'), Math.abs(square.getColumn() - 'e'));
        }

        return result/queenSquares.size();
    }

    private double evaluateActiveKing(Table table) {
        Color color = table.getToMove();
        Piece piece = null;
        if(color == Color.White){
            piece = Piece.whiteKing;
        }
        if(color == Color.Black){
            piece = Piece.blackKing;
        }

        List<Square> squares = table.getPositionsOfPiece(piece);

        double result = 0.0;
        for (Square square : squares) {
            int advance = 0;
            if(color == Color.White){
                advance = square.getLine() ;
            }
            if(color == Color.Black){
                advance = 9 - square.getLine();
            }
            result += advance/8 + Math.max(Math.abs(square.getColumn() - 'd'), Math.abs(square.getColumn() - 'e'));
        }

        return result/squares.size();
    }

    private double evaluateIsolatedPawns(Table table) {
        Color color = table.getToMove();
        Piece piece = null;
        if (color == Color.White)
            piece = Piece.whitePawn;
        if (color == Color.Black)
            piece = Piece.blackPawn;

        List<Square> pawnSquares = table.getPositionsOfPiece(piece);
        int pawnIslands = 0;
        for (int column = 'a'; column <= 'h'; ++column) {
            int preColumn = column - 1;
            boolean hasPre = false;
            int nextColumn = column + 1;
            boolean hasNext = false;
            for (Square square : pawnSquares) {
                if (square.getColumn() == preColumn){
                    hasPre = true;
                }
                if(square.getColumn() == nextColumn){
                    hasNext = true;
                }
            }
            if(!hasPre && !hasNext)
                ++pawnIslands;
        }
        if(pawnIslands == 0) {
            if (pawnSquares.size() > 0) {
                return 1.0;
            }
            else {
                return 0.0;
            }
        }

        return 1.0/(1+pawnIslands);
    }

    private double evaluatePieceCount(Table table) {
        Double score = 0.0;
        Map<Square, Piece> pieceMap = table.getSquareToPieceMap();
        Color toMove = table.getToMove();
        Color otherColor = toMove.getOtherColor();
        for (Piece piece : pieceMap.values())
            if (piece != null && piece != Piece.noPiece) {
                if (toMove == piece.getColor())
                    score += piece.getValue();
                if (toMove == otherColor)
                    score -= piece.getValue();
            }
        // score -43 43
        Double result = (score + 43.0) / 86.0;

        return result;
    }

}