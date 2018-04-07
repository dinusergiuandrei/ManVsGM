package ChessLogic;

import GameArchitecture.*;

import java.util.LinkedList;
import java.util.List;

import static GameArchitecture.Table.getSquare;

public class SemanticMoveValidator {
    public static List<Move> getLegalMoves(Table table, Square startSquare) {
        Piece piece = table.squareToPieceMap.get(startSquare);
        List<Move> moves = null;
        switch (piece) {
            case whiteKing:
            case blackKing:
                moves = getLegalKingMoves(table, startSquare);
                break;
            case whiteQueen:
            case blackQueen:
                moves = getLegalQueenMoves(table, startSquare);
                break;
            case whiteRook:
            case blackRook:
                moves = getLegalRookMoves(table, startSquare);
                break;
            case whiteBishop:
            case blackBishop:
                moves = getLegalBishopMoves(table, startSquare);
                break;
            case whiteKnight:
            case blackKnight:
                moves = getLegalKnightMoves(table, startSquare);
                break;
            case whitePawn:
            case blackPawn:
                moves = getLegalPawnMoves(table, startSquare);
                break;
        }
        return moves;
    }

    public static List<Move> getLegalMoves(Table table, Square startSquare, int columnDifference[], int lineDifference[], Table.Range range) {
        Color color = table.squareToPieceMap.get(startSquare).getColor();
        List<Move> candidateMoves = new LinkedList<>();
        if (range == Table.Range.CLOSE) {
            for (int i = 0; i < columnDifference.length; ++i) {
                int column = startSquare.getColumn() + columnDifference[i];
                int line = startSquare.getLine() + lineDifference[i];
                Square square = getSquare(column, line);
                if (square == null)
                    continue;
                candidateMoves.add(new Move(startSquare, square));
            }
        }
        if (range == Table.Range.DISTANCE) {
            for (int i = 0; i < columnDifference.length; ++i) {
                for (int distance = 1; ; ++distance) {
                    int column = startSquare.getColumn() + distance * columnDifference[i];
                    int line = startSquare.getLine() + distance * lineDifference[i];
                    Square square = getSquare(column, line);
                    if (square == null)
                        break;
                    Piece piece = table.squareToPieceMap.get(square);
                    if (piece.getColor() == color)
                        break;
                    candidateMoves.add(new Move(startSquare, square));
                    if (piece != Piece.noPiece)
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

        possibleSquares.add(getLegalPawnOneSquarePush(table, startSquare, direction));

        possibleSquares.add(getLegalPawnTwoSquarePush(table, startSquare, direction));

        possibleSquares.add(getLegalPawnCapture(table, startSquare, direction, 1));
        possibleSquares.add(getLegalPawnCapture(table, startSquare, direction, -1));


        possibleSquares.add(getLegalPawnEnPassantMove(table, startSquare, direction, 1));
        possibleSquares.add(getLegalPawnEnPassantMove(table, startSquare, direction, -1));


        return possibleSquares;
    }

    private static Move getLegalPawnOneSquarePush(Table table, Square startSquare, Integer direction){
        Square endSquare = getSquare(startSquare.getColumn(), startSquare.getLine() + direction);
        if (endSquare != null && table.squareToPieceMap.get(endSquare) == Piece.noPiece)
            return new Move(startSquare, endSquare);
        return null;
    }

    private static Move getLegalPawnTwoSquarePush(Table table, Square startSquare, Integer direction){
        Square endSquare = getSquare(startSquare.getColumn(), startSquare.getLine() + direction);
        Square endSquare2 = getSquare(startSquare.getColumn(), startSquare.getLine() + 2 * direction);
        if (endSquare2 != null
                && table.squareToPieceMap.get(endSquare) == Piece.noPiece
                && table.squareToPieceMap.get(endSquare2) == Piece.noPiece
                && ((startSquare.getLine() == 2 && direction == 1) || (startSquare.getLine() == 7 && direction == -1))
                )
            return new Move(startSquare, endSquare2);
        return null;
    }

    private static Move getLegalPawnCapture(Table table, Square startSquare, Integer pawnDirection, Integer captureDirection){
        Color color = table.squareToPieceMap.get(startSquare).getColor();
        Square endSquare = getSquare(startSquare.getColumn() + captureDirection, startSquare.getLine() + pawnDirection);
        if (endSquare != null
                && table.squareToPieceMap.get(endSquare) != Piece.noPiece
                && table.squareToPieceMap.get(endSquare).getColor() != color
                )
            return new Move(startSquare, endSquare);
        return null;
    }

    private static Move getLegalPawnEnPassantMove(Table table, Square startSquare, Integer pawnDirection, Integer captureDirection){

        //target square = where the captured pawn is
        //end square = where the pawn lands

        Color color = table.squareToPieceMap.get(startSquare).getColor();

        int endLine, endColumn;
        int targetLine, targetColumn;

        endLine = startSquare.getLine() + pawnDirection;
        endColumn = startSquare.getColumn() + captureDirection;
        targetLine = startSquare.getLine();
        targetColumn = startSquare.getColumn() + captureDirection;
        Square endSquare5 = getSquare(endColumn, endLine);
        Square targetSquare = getSquare(targetColumn, targetLine);
        if (targetSquare != null
                && table.squareToPieceMap.get(endSquare5) == Piece.noPiece
                && (table.squareToPieceMap.get(targetSquare) == Piece.whitePawn || table.squareToPieceMap.get(targetSquare) == Piece.blackPawn)
                && (
                (targetLine == 4 && table.squareToPieceMap.get(targetSquare).getColor() == Color.White)
                        || (targetLine == 5 && table.squareToPieceMap.get(targetSquare).getColor() == Color.Black)
        )
                && table.squareToPieceMap.get(targetSquare).getColor() != color
                )
            return new Move(startSquare, endSquare5);
        return null;
    }

    public static List<Move> getLegalKingMoves(Table table, Square startSquare) {
        Color color = table.squareToPieceMap.get(startSquare).getColor();
        List<Move> possibleMoves = new LinkedList<>();
        int[] lineDifference = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnDifference = {-1, 0, 1, 1, 1, 0, -1, -1};

        possibleMoves.addAll(getLegalMoves(table, startSquare, columnDifference, lineDifference, Table.Range.CLOSE));

        if (color == Color.White) {
            if (table.possibleWhiteShortCastle)
                possibleMoves.add(new Move(startSquare, getSquare('g', 1)));
            if (table.possibleWhiteLongCastle)
                possibleMoves.add(new Move(startSquare, getSquare('c', 1)));
        }
        if (color == Color.Black) {
            if (table.possibleBlackShortCastle)
                possibleMoves.add(new Move(startSquare, getSquare('g', 8)));
            if (table.possibleBlackLongCastle)
                possibleMoves.add(new Move(startSquare, getSquare('c', 8)));
        }

        return possibleMoves;
    }

    public static List<Move> getLegalQueenMoves(Table table, Square startSquare) {
        int[] lineDifference = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnDifference = {-1, 0, 1, 1, 1, 0, -1, -1};

        return getLegalMoves(table, startSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Move> getLegalRookMoves(Table table, Square startSquare) {
        int[] lineDifference = {1, -1, 0, 0};
        int[] columnDifference = {0, 0, 1, -1};

        return getLegalMoves(table, startSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Move> getLegalBishopMoves(Table table, Square startSquare) {
        int[] lineDifference = {1, -1, 1, -1};
        int[] columnDifference = {1, 1, -1, -1};

        return getLegalMoves(table, startSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Move> getLegalKnightMoves(Table table, Square startSquare) {
        int[] lineDifference = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] columnDifference = {1, 2, 2, 1, -1, -2, -2, -1};

        return getLegalMoves(table, startSquare, columnDifference, lineDifference, Table.Range.CLOSE);
    }

    public static Boolean isValidMoveSemantically(Table table, Move move) {


        if (!SyntacticMoveValidator.isValidMoveSyntactically(table, move))
            return false;

        List<Move> legalMoves = getLegalMoves(table, move.getStartSquare());
        if (!legalMoves.contains(move)) {
            return false;
        }

        Table analysisTable = table.getCopy();
        analysisTable.doMove(move);
//        if(move.getStartSquare() == getSquareByName('g', 7)){
//            System.out.println("stop");
//        }

        Color otherColor;
        if (analysisTable.getToMove() == Color.White)
            otherColor = Color.Black;
        else otherColor = Color.White;

//        if(move.getStartSquare() == Square.a3 && move.getEndSquare() == Square.a4){
//            System.out.println("not right");
//        }

        Boolean ok = !SyntacticMoveValidator.hasKingAttacked(analysisTable, otherColor);
        //table.undo();

        return ok;

    }
}
