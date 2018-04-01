package ChessLogic;

import GameArchitecture.*;

import java.util.LinkedList;
import java.util.List;

import static GameArchitecture.Table.getSquare;

public class SemanticMoveValidator {
    public static List<Move> getLegalMoves(Table table, Square startSquare){
        Piece piece = table.squareToPieceMap.get(startSquare);
        List<Move> moves = null;
        switch (piece){
            case whiteKing:   case blackKing:   moves = getLegalKingMoves(table, startSquare);   break;
            case whiteQueen:  case blackQueen:  moves = getLegalQueenMoves(table, startSquare);   break;
            case whiteRook:   case blackRook:   moves = getLegalRookMoves(table, startSquare);   break;
            case whiteBishop: case blackBishop: moves = getLegalBishopMoves(table, startSquare);   break;
            case whiteKnight: case blackKnight: moves = getLegalKnightMoves(table, startSquare);   break;
            case whitePawn:   case blackPawn:   moves = getLegalPawnMoves(table, startSquare);   break;
        }
        return moves;
    }

    public static List<Move> getLegalMoves(Table table, Square startSquare, int columnDifference[], int lineDifference[], Table.Range range){
        Color color = table.squareToPieceMap.get(startSquare).getColor();
        List<Move> candidateMoves = new LinkedList<>();
        if(range == Table.Range.CLOSE){
            for(int i=0; i<columnDifference.length; ++i){
                    int column = startSquare.getColumn() + columnDifference[i];
                    int line = startSquare.getLine() + lineDifference[i];
                    Square square = getSquare(column, line);
                    if(square == null)
                        continue;
                    candidateMoves.add(new Move(startSquare, square));
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
                        Piece piece = table.squareToPieceMap.get(square);
                        if(piece.getColor() == color)
                            break;
                        candidateMoves.add(new Move(startSquare, square));
                        if(piece != Piece.noPiece)
                            break;
                    }
                }
        }
        return candidateMoves;
    }

//    public static List<Move> getLegalPawnCaptureMoves(Table table, Square startSquare){
//
//    }

    public static List<Move> getLegalPawnMoves(Table table, Square startSquare) {
        Color color = table.squareToPieceMap.get(startSquare).getColor();
        List<Move> possibleSquares = new LinkedList<>();

        int direction = 0;
        if (color == Color.White)
            direction = 1;
        else if (color == Color.Black)
            direction = -1;

        //1 square push
        Square endSquare = getSquare(startSquare.getColumn(), startSquare.getLine() + direction);
        if (endSquare != null && table.squareToPieceMap.get(endSquare) == Piece.noPiece)
            possibleSquares.add(new Move(startSquare, endSquare));

        //2 square push
        Square endSquare2 = getSquare(startSquare.getColumn(), startSquare.getLine() + 2 * direction);
        if (endSquare2 != null
                && table.squareToPieceMap.get(endSquare) == Piece.noPiece
                && table.squareToPieceMap.get(endSquare2) == Piece.noPiece
                && ( ( startSquare.getLine() == 2 && direction == 1 ) || (startSquare.getLine() == 7 && direction == -1) )
                )
            possibleSquares.add(new Move(startSquare, endSquare2));

        //simple right capture
        Square endSquare3 = getSquare(startSquare.getColumn() + 1, startSquare.getLine() + direction);
        if (endSquare3 != null
                && table.squareToPieceMap.get(endSquare3) != Piece.noPiece
                && table.squareToPieceMap.get(endSquare3).getColor() != color
                )
            possibleSquares.add(new Move(startSquare, endSquare3));

        //simple left capture
        Square endSquare4 = getSquare(startSquare.getColumn() - 1, startSquare.getLine() + direction);
        if (endSquare4 != null
                && table.squareToPieceMap.get(endSquare4) != Piece.noPiece
                && table.squareToPieceMap.get(endSquare4).getColor() != color
                )
            possibleSquares.add(new Move(startSquare, endSquare4));

        //right en-passant

        int endLine, endColumn;
        int targetLine, targetColumn;

        targetLine = startSquare.getLine();
        targetColumn = startSquare.getColumn() + 1;

        Square endSquare5 = getSquare(startSquare.getColumn() + 1, startSquare.getLine() + direction);
        Square targetSquare = getSquare(targetColumn, targetLine);
        if (endSquare5 != null
                && table.squareToPieceMap.get(endSquare5) == Piece.noPiece
                && ( table.squareToPieceMap.get(targetSquare) == Piece.whitePawn || table.squareToPieceMap.get(targetSquare) == Piece.blackPawn )
                && ((targetLine == 4 && table.squareToPieceMap.get(targetSquare).getColor() == Color.White) || (targetLine == 5 && table.squareToPieceMap.get(targetSquare).getColor() == Color.Black) )
                && table.squareToPieceMap.get(targetSquare).getColor() != color
                )
            possibleSquares.add(new Move(startSquare, endSquare5));


        //left en-passant
        targetLine = startSquare.getLine();
        targetColumn = startSquare.getColumn() - 1;
        Square endSquare6 = getSquare(startSquare.getColumn() + 1, startSquare.getLine() + direction);
        targetSquare = getSquare(targetColumn, targetLine);
        if (endSquare6 != null
                && table.squareToPieceMap.get(endSquare6) == Piece.noPiece
                && ( table.squareToPieceMap.get(targetSquare) == Piece.whitePawn || table.squareToPieceMap.get(targetSquare) == Piece.blackPawn )
                && ((targetLine == 4 && table.squareToPieceMap.get(targetSquare).getColor() == Color.White) || (targetLine == 5 && table.squareToPieceMap.get(targetSquare).getColor() == Color.Black) )
                && table.squareToPieceMap.get(targetSquare).getColor() != color
                )
            possibleSquares.add(new Move(startSquare, endSquare6));

        return possibleSquares;
    }

    public static List<Move> getLegalKingMoves(Table table, Square startSquare){
        Color color = table.squareToPieceMap.get(startSquare).getColor();
        List<Move> possibleMoves = new LinkedList<>();
        int[] lineDifference   = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnDifference = {-1, 0, 1, 1, 1, 0, -1, -1};

        possibleMoves.addAll(getLegalMoves(table, startSquare, columnDifference, lineDifference, Table.Range.CLOSE));

        if(color == Color.White){
            if(table.possibleWhiteShortCastle)
                possibleMoves.add(new Move(startSquare, getSquare('g', 1)));
            if(table.possibleWhiteLongCastle)
                possibleMoves.add(new Move(startSquare, getSquare('c', 1)));
        }
        if(color == Color.Black){
            if(table.possibleBlackShortCastle)
                possibleMoves.add(new Move(startSquare, getSquare('g', 8)));
            if(table.possibleBlackLongCastle)
                possibleMoves.add(new Move(startSquare, getSquare('c', 8)));
        }

        return possibleMoves;
    }

    public static List<Move> getLegalQueenMoves(Table table, Square startSquare){
        int[] lineDifference   = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnDifference = {-1, 0, 1, 1, 1, 0, -1, -1};

        return getLegalMoves(table, startSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Move> getLegalRookMoves(Table table, Square startSquare){
        int[] lineDifference   = {1, -1, 0, 0};
        int[] columnDifference = {0, 0, 1, -1};

        return getLegalMoves(table, startSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Move> getLegalBishopMoves(Table table, Square startSquare){
        int[] lineDifference   = {1, -1, 1, -1};
        int[] columnDifference = {1, 1, -1, -1};

        return getLegalMoves(table, startSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Move> getLegalKnightMoves(Table table, Square startSquare){
        int[] lineDifference   = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] columnDifference = {1, 2, 2, 1, -1, -2, -2, -1};

        return getLegalMoves(table, startSquare, columnDifference, lineDifference, Table.Range.CLOSE);
    }

    public static Boolean isValidMoveSemantically(Game game, Move move){

        if(!SyntacticMoveValidator.isValidMoveSyntactically(game.getTable(), move))
            return false;

        if(!getLegalMoves(game.getTable(), move.getStartSquare()).contains(move))
            return false;

        game.doMove(move);
//        if(move.getStartSquare() == getSquareByName('g', 7)){
//            System.out.println("stop");
//        }

        Color otherColor;
        if(game.getToMove() == Color.White)
            otherColor = Color.Black;
        else otherColor = Color.White;

//        if(move.getStartSquare() == Square.a3 && move.getEndSquare() == Square.a4){
//            System.out.println("not right");
//        }

        Boolean ok = !SyntacticMoveValidator.hasKingAttacked(game.getTable(), otherColor);
        game.undo();

        return ok;

    }
}
