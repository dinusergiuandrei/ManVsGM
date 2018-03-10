package ChessLogic;

import GameArchitecture.*;

import java.util.LinkedList;
import java.util.List;

import static GameArchitecture.Table.getSquare;

public class SyntacticMoveValidator {

    public static List<Square> getAllMoves(Square startSquare, int columnDifference[], int lineDifference[], Table.Range range){
        List<Square> candidateMoves = new LinkedList<>();
        if(range == Table.Range.CLOSE) {
            for (int i = 0; i < columnDifference.length; ++i) {
                int column = startSquare.getColumn() + columnDifference[i];
                int line = startSquare.getLine() + lineDifference[i];
                Square square = getSquare(column, line);
                if (square == null)
                    continue;
                candidateMoves.add(square);
            }
        }
        if(range == Table.Range.DISTANCE){
            for(int i=0; i<columnDifference.length; ++i){
                for(int distance=1; ; ++distance) {
                    int column = startSquare.getColumn() + distance*columnDifference[i];
                    int line = startSquare.getLine() + distance*lineDifference[i];
                    Square square = getSquare(column, line);
                    if (square == null)
                        break;
                    candidateMoves.add(square);
                }
            }
        }
        return candidateMoves;
    }

    public static List<Square> getAllKingMoves(Square endingSquare, Color color){
        List<Square> moves = new LinkedList<>();

        int[] lineDifference   = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnDifference = {-1, 0, 1, 1, 1, 0, -1, -1};

        moves.addAll(getAllMoves(endingSquare, columnDifference, lineDifference, Table.Range.CLOSE));

        if(color == Color.White){
            if(endingSquare == Square.g1 || endingSquare == Square.c1)
                moves.add(getSquare('e', 1));
        }
        if(color == Color.Black){
            if(endingSquare == Square.g8 || endingSquare == Square.c8)
                moves.add(getSquare('e', 8));
        }

        return moves;
    }

    public static List<Square> getAllQueenMoves(Square endingSquare){
        int[] lineDifference   = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnDifference = {-1, 0, 1, 1, 1, 0, -1, -1};

        return getAllMoves(endingSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Square> getAllRookMoves(Square endingSquare){
        int[] lineDifference   = {1, -1, 0, 0};
        int[] columnDifference = {0, 0, 1, -1};

        return getAllMoves(endingSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Square> getAllBishopMoves(Square endingSquare){
        int[] lineDifference   = {1, -1, 1, -1};
        int[] columnDifference = {1, 1, -1, -1};

        return getAllMoves(endingSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Square> getAllKnightMoves(Square endingSquare){
        int[] lineDifference   = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] columnDifference = {1, 2, 2, 1, -1, -2, -2, -1};

        return getAllMoves(endingSquare, columnDifference, lineDifference, Table.Range.CLOSE);
    }

    public static List<Square> getAllPawnPushMoves(Square endingSquare, Color color) {
        List<Square> possibleSquares = new LinkedList<>();

        if(color == Color.White){
            Square square1 = getSquare(endingSquare.getColumn(), endingSquare.getLine() - 1);
            if(square1!=null)
                possibleSquares.add(square1);

            if(endingSquare.getLine() == 4)
                possibleSquares.add(getSquare(endingSquare.getColumn(), 2));

        }
        if(color == Color.Black){
            Square square1 = getSquare(endingSquare.getColumn(), endingSquare.getLine()+1);
            if(square1!=null)
                possibleSquares.add(square1);

            if(endingSquare.getLine() == 5)
                possibleSquares.add(getSquare(endingSquare.getColumn(), 7));
        }

        return possibleSquares;
    }

    public static List<Square> getAllPawnCaptureMoves(Square endingSquare, Color color) {
        List<Square> possibleSquares = new LinkedList<>();

        if(color == Color.White){
            Square square2 = getSquare(endingSquare.getColumn()+1, endingSquare.getLine()-1);
            if(square2!=null)
                possibleSquares.add(square2);

            Square square3 = getSquare(endingSquare.getColumn()-1, endingSquare.getLine()-1);
            if(square3!=null)
                possibleSquares.add(square3);

        }
        if(color == Color.Black){
            Square square2 = getSquare(endingSquare.getColumn()+1, endingSquare.getLine()+1);
            if(square2!=null)
                possibleSquares.add(square2);

            Square square3 = getSquare(endingSquare.getColumn()-1, endingSquare.getLine()+1);
            if(square3!=null)
                possibleSquares.add(square3);
        }

        return possibleSquares;
    }

    public static List<Square> getAllPawnMoves(Square endingSquare, Color color){
        List<Square> moves = new LinkedList<>();
        moves.addAll(getAllPawnPushMoves(endingSquare, color));
        moves.addAll(getAllPawnCaptureMoves(endingSquare, color));
        return moves;
    }

    public static List<Square> getAllMoves(Piece piece, Square endSquare){
        List<Square> moves = null;
        switch (piece){
            case whiteKing:   case blackKing:   moves = getAllKingMoves   (endSquare, piece.getColor());   break;
            case whiteQueen:  case blackQueen:  moves = getAllQueenMoves  (endSquare);   break;
            case whiteRook:   case blackRook:   moves = getAllRookMoves   (endSquare);   break;
            case whiteBishop: case blackBishop: moves = getAllBishopMoves (endSquare);   break;
            case whiteKnight: case blackKnight: moves = getAllKnightMoves (endSquare);   break;
            case whitePawn:   case blackPawn:   moves = getAllPawnMoves   (endSquare, piece.getColor()); break;
        }
        return moves;
    }

    public static Boolean isValidMoveSintacticly(Table table, Move move){
        Piece piece = table.squareToPieceMap.get(move.getStartSquare());
        List<Square> moves = getAllMoves(piece, move.getEndSquare());
        return moves.contains(move.getStartSquare());
    }
}
