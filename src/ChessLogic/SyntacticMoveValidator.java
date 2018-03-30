package ChessLogic;

import GameArchitecture.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static GameArchitecture.Table.getSquare;

public class SyntacticMoveValidator {

    public static List<Move> getAllMoves(Square endSquare, int columnDifference[], int lineDifference[], Table.Range range) {
        List<Move> candidateMoves = new LinkedList<>();
        if (range == Table.Range.CLOSE) {
            for (int i = 0; i < columnDifference.length; ++i) {
                int column = endSquare.getColumn() + columnDifference[i];
                int line = endSquare.getLine() + lineDifference[i];
                Square square = getSquare(column, line);
                if (square == null)
                    continue;
                candidateMoves.add(new Move(square, endSquare));
            }
        }
        if (range == Table.Range.DISTANCE) {
            for (int i = 0; i < columnDifference.length; ++i) {
                for (int distance = 1; ; ++distance) {
                    int column = endSquare.getColumn() + distance * columnDifference[i];
                    int line = endSquare.getLine() + distance * lineDifference[i];
                    Square square = getSquare(column, line);
                    if (square == null)
                        break;
                    candidateMoves.add(new Move(square, endSquare));
                }
            }
        }
        return candidateMoves;
    }

    public static List<Move> getAllKingMoves(Square endingSquare, Color color) {
        List<Move> moves = new LinkedList<>();

        int[] lineDifference = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnDifference = {-1, 0, 1, 1, 1, 0, -1, -1};

        moves.addAll(getAllMoves(endingSquare, columnDifference, lineDifference, Table.Range.CLOSE));

        if (color == Color.White) {
            if (endingSquare == Square.g1 || endingSquare == Square.c1)
                moves.add(new Move(getSquare('e', 1), endingSquare));
        }
        if (color == Color.Black) {
            if (endingSquare == Square.g8 || endingSquare == Square.c8)
                moves.add(new Move(getSquare('e', 8), endingSquare));
        }

        return moves;
    }

    public static List<Move> getAllQueenMoves(Square endingSquare) {
        int[] lineDifference = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnDifference = {-1, 0, 1, 1, 1, 0, -1, -1};

        return getAllMoves(endingSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Move> getAllRookMoves(Square endingSquare) {
        int[] lineDifference = {1, -1, 0, 0};
        int[] columnDifference = {0, 0, 1, -1};

        return getAllMoves(endingSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Move> getAllBishopMoves(Square endingSquare) {
        int[] lineDifference = {1, -1, 1, -1};
        int[] columnDifference = {1, 1, -1, -1};

        return getAllMoves(endingSquare, columnDifference, lineDifference, Table.Range.DISTANCE);
    }

    public static List<Move> getAllKnightMoves(Square endingSquare) {
        int[] lineDifference = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] columnDifference = {1, 2, 2, 1, -1, -2, -2, -1};

        return getAllMoves(endingSquare, columnDifference, lineDifference, Table.Range.CLOSE);
    }

    public static List<Move> getAllPawnPushMoves(Square endingSquare, Color color) {
        List<Move> possibleMoves = new LinkedList<>();

        if (color == Color.White) {
            Square square1 = getSquare(endingSquare.getColumn(), endingSquare.getLine() - 1);
            if (square1 != null)
                possibleMoves.add(new Move(square1, endingSquare));

            if (endingSquare.getLine() == 4)
                possibleMoves.add(new Move(getSquare(endingSquare.getColumn(), 2), endingSquare));

        }
        if (color == Color.Black) {
            Square square1 = getSquare(endingSquare.getColumn(), endingSquare.getLine() + 1);

            if (square1 != null)
                possibleMoves.add(new Move(square1, endingSquare));

            if (endingSquare.getLine() == 5)
                possibleMoves.add(new Move(getSquare(endingSquare.getColumn(), 7), endingSquare));
        }

        return possibleMoves;
    }

    public static List<Move> getAllPawnCaptureMoves(Square endingSquare, Color color) {
        List<Move> possibleMoves = new LinkedList<>();

        try {
            if (color == Color.White) {
                Square square2 = getSquare(endingSquare.getColumn() + 1, endingSquare.getLine() - 1);

                if (square2 != null)
                    possibleMoves.add(new Move(square2, endingSquare));

                Square square3 = getSquare(endingSquare.getColumn() - 1, endingSquare.getLine() - 1);

                if (square3 != null)
                    possibleMoves.add(new Move(square3, endingSquare));

            }
            if (color == Color.Black) {
                Square square2 = getSquare(endingSquare.getColumn() + 1, endingSquare.getLine() + 1);


                if (square2 != null)
                    possibleMoves.add(new Move(square2, endingSquare));

                Square square3 = getSquare(endingSquare.getColumn() - 1, endingSquare.getLine() + 1);


                if (square3 != null)
                    possibleMoves.add(new Move(square3, endingSquare));
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return possibleMoves;
    }

    public static List<Move> getAllPawnMoves(Square endingSquare, Color color) {
        List<Move> moves = new LinkedList<>();
        moves.addAll(getAllPawnPushMoves(endingSquare, color));
        moves.addAll(getAllPawnCaptureMoves(endingSquare, color));
        return moves;
    }

    public static List<Move> getAllMoves(Piece piece, Square endSquare) {
        List<Move> moves = null;
        switch (piece) {
            case whiteKing:
            case blackKing:
                moves = getAllKingMoves(endSquare, piece.getColor());
                break;
            case whiteQueen:
            case blackQueen:
                moves = getAllQueenMoves(endSquare);
                break;
            case whiteRook:
            case blackRook:
                moves = getAllRookMoves(endSquare);
                break;
            case whiteBishop:
            case blackBishop:
                moves = getAllBishopMoves(endSquare);
                break;
            case whiteKnight:
            case blackKnight:
                moves = getAllKnightMoves(endSquare);
                break;
            case whitePawn:
            case blackPawn:
                moves = getAllPawnMoves(endSquare, piece.getColor());
                break;
        }
        return moves;
    }

    public static Boolean isValidMoveSyntactically(Table table, Move move) {
        Piece piece = table.squareToPieceMap.get(move.getStartSquare());
        List<Move> moves = getAllMoves(piece, move.getEndSquare());

        try {
            return moves.contains(move);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean hasKingAttacked(Table table, Color toMove) {
        Square kingSquare = getKingSquare(table, toMove);
        Color otherColor = null;
        if (toMove == Color.White) {
            otherColor = Color.Black;
        } else if (toMove == Color.Black) {
            otherColor = Color.White;
        }

        if (isAttackedByKnightOfColor(table, kingSquare, otherColor)) {
            return true;
        }

        if (isAttackedByBishopOfColor(table, kingSquare, otherColor)) {
            return true;
        }

        if (isAttackedByRookOfColor(table, kingSquare, otherColor)) {
            return true;
        }

        if (isAttackedByQueenOfColor(table, kingSquare, otherColor)) {
            return true;
        }

        if (isAttackedByKingOfColor(table, kingSquare, otherColor)) {
            return true;
        }

        if (isAttackedByPawnOfColor(table, kingSquare, otherColor)) {
            return true;
        }

        return false;
    }

    private static Boolean isAttackedByKnightOfColor(Table table, Square square, Color color) {
        final Boolean[] response = new Boolean[1];
        Piece piece = null;
        if (color == Color.White) {
            piece = Piece.whiteKnight;
        } else if (color == Color.Black) {
            piece = Piece.blackKnight;
        }

        Piece aux = table.squareToPieceMap.get(square);

        table.squareToPieceMap.put(square, piece);

        response[0] = false;
        Piece finalPiece = piece;
        SemanticMoveValidator.getLegalKnightMoves(table, square).forEach(
                square1 -> {
                    if (table.squareToPieceMap.get(square1) == finalPiece) {
                        response[0] = true;
                    }
                }
        );

        table.squareToPieceMap.put(square, aux);

        return response[0];
    }

    private static Boolean isAttackedByBishopOfColor(Table table, Square square, Color color) {
        final Boolean[] response = new Boolean[1];
        Piece piece = null;
        if (color == Color.White) {
            piece = Piece.whiteBishop;
        } else if (color == Color.Black) {
            piece = Piece.blackBishop;
        }

        Piece aux = table.squareToPieceMap.get(square);

        table.squareToPieceMap.put(square, piece);

        response[0] = false;
        Piece finalPiece = piece;
        SemanticMoveValidator.getLegalBishopMoves(table, square).forEach(
                square1 -> {
                    if (table.squareToPieceMap.get(square1) == finalPiece) {
                        response[0] = true;
                    }
                }
        );

        table.squareToPieceMap.put(square, aux);

        return response[0];
    }

    private static Boolean isAttackedByRookOfColor(Table table, Square square, Color color) {
        final Boolean[] response = new Boolean[1];
        Piece piece = null;
        if (color == Color.White) {
            piece = Piece.whiteRook;
        } else if (color == Color.Black) {
            piece = Piece.blackRook;
        }

        Piece aux = table.squareToPieceMap.get(square);

        table.squareToPieceMap.put(square, piece);

        response[0] = false;
        Piece finalPiece = piece;
        SemanticMoveValidator.getLegalRookMoves(table, square).forEach(
                square1 -> {
                    if (table.squareToPieceMap.get(square1) == finalPiece) {
                        response[0] = true;
                    }
                }
        );

        table.squareToPieceMap.put(square, aux);

        return response[0];
    }

    private static Boolean isAttackedByQueenOfColor(Table table, Square square, Color color) {
        final Boolean[] response = new Boolean[1];
        Piece piece = null;
        if (color == Color.White) {
            piece = Piece.whiteQueen;
        } else if (color == Color.Black) {
            piece = Piece.blackQueen;
        }

        Piece aux = table.squareToPieceMap.get(square);

        table.squareToPieceMap.put(square, piece);

        response[0] = false;
        Piece finalPiece = piece;
        SemanticMoveValidator.getLegalQueenMoves(table, square).forEach(
                square1 -> {
                    if (table.squareToPieceMap.get(square1) == finalPiece) {
                        response[0] = true;
                    }
                }
        );

        table.squareToPieceMap.put(square, aux);

        return response[0];

    }

    private static Boolean isAttackedByKingOfColor(Table table, Square square, Color color) {
        final Boolean[] response = new Boolean[1];
        Piece piece = null;
        if (color == Color.White) {
            piece = Piece.whiteKing;
        } else if (color == Color.Black) {
            piece = Piece.blackKing;
        }

        Piece aux = table.squareToPieceMap.get(square);

        table.squareToPieceMap.put(square, piece);

        response[0] = false;
        Piece finalPiece = piece;
        SemanticMoveValidator.getLegalKingMoves(table, square).forEach(
                square1 -> {
                    if (table.squareToPieceMap.get(square1) == finalPiece) {
                        response[0] = true;
                    }
                }
        );

        table.squareToPieceMap.put(square, aux);

        return response[0];
    }

    private static Boolean isAttackedByPawnOfColor(Table table, Square square, Color color) {
        final Boolean[] response = new Boolean[1];
        Piece piece = null;
        if (color == Color.White) {
            piece = Piece.whitePawn;
        } else if (color == Color.Black) {
            piece = Piece.blackPawn;
        }

        Piece aux = table.squareToPieceMap.get(square);

        table.squareToPieceMap.put(square, piece);

        response[0] = false;
        Piece finalPiece = piece;
        SemanticMoveValidator.getLegalPawnMoves(table, square).forEach(
                square1 -> {
                    if (table.squareToPieceMap.get(square1) == finalPiece) {
                        response[0] = true;
                    }
                }
        );

        table.squareToPieceMap.put(square, aux);

        return response[0];
    }


    public static Square getKingSquare(Table table, Color toMove) {
        AtomicReference<Square> kingSquare = new AtomicReference<>();
        if (toMove == Color.White) {
            table.squareToPieceMap.forEach(
                    (Square sq, Piece p) -> {
                        if (p == Piece.whiteKing) {
                            kingSquare.set(sq);
                        }
                    }
            );
        }
        if (toMove == Color.Black) {
            table.squareToPieceMap.forEach(
                    (Square sq, Piece p) -> {
                        if (p == Piece.blackKing) {
                            kingSquare.set(sq);
                        }
                    }
            );
        }

        return kingSquare.get();
    }

}
