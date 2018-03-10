package GameArchitecture;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Table {

    public Map<Square, Piece> squareToPieceMap = new LinkedHashMap<>(64);

    public Boolean possibleWhiteShortCastle = true;
    public Boolean possibleWhiteLongCastle = true;
    public Boolean possibleBlackShortCastle = true;
    public Boolean possibleBlackLongCastle = true;

    public void setUpPieces() {
        squareToPieceMap.put(Square.a1, Piece.whiteRook);
        squareToPieceMap.put(Square.b1, Piece.whiteKnight);
        squareToPieceMap.put(Square.c1, Piece.whiteBishop);
        squareToPieceMap.put(Square.d1, Piece.whiteQueen);
        squareToPieceMap.put(Square.e1, Piece.whiteKing);
        squareToPieceMap.put(Square.f1, Piece.whiteBishop);
        squareToPieceMap.put(Square.g1, Piece.whiteKnight);
        squareToPieceMap.put(Square.h1, Piece.whiteRook);
        squareToPieceMap.put(Square.a2, Piece.whitePawn);
        squareToPieceMap.put(Square.b2, Piece.whitePawn);
        squareToPieceMap.put(Square.c2, Piece.whitePawn);
        squareToPieceMap.put(Square.d2, Piece.whitePawn);
        squareToPieceMap.put(Square.e2, Piece.whitePawn);
        squareToPieceMap.put(Square.f2, Piece.whitePawn);
        squareToPieceMap.put(Square.g2, Piece.whitePawn);
        squareToPieceMap.put(Square.h2, Piece.whitePawn);
        squareToPieceMap.put(Square.a8, Piece.blackRook);
        squareToPieceMap.put(Square.b8, Piece.blackKnight);
        squareToPieceMap.put(Square.c8, Piece.blackBishop);
        squareToPieceMap.put(Square.d8, Piece.blackQueen);
        squareToPieceMap.put(Square.e8, Piece.blackKing);
        squareToPieceMap.put(Square.f8, Piece.blackBishop);
        squareToPieceMap.put(Square.g8, Piece.blackKnight);
        squareToPieceMap.put(Square.h8, Piece.blackRook);
        squareToPieceMap.put(Square.a7, Piece.blackPawn);
        squareToPieceMap.put(Square.b7, Piece.blackPawn);
        squareToPieceMap.put(Square.c7, Piece.blackPawn);
        squareToPieceMap.put(Square.d7, Piece.blackPawn);
        squareToPieceMap.put(Square.e7, Piece.blackPawn);
        squareToPieceMap.put(Square.f7, Piece.blackPawn);
        squareToPieceMap.put(Square.g7, Piece.blackPawn);
        squareToPieceMap.put(Square.h7, Piece.blackPawn);

        for (Square square : Square.values()) {
            if (!squareToPieceMap.containsKey(square)) {
                squareToPieceMap.put(square, Piece.noPiece);
            }
        }
    }

    public static Square getSquare(int column, int line) {
        if (column < 'a' || column > 'h' || line < 1 || line > 8) {
            return null;
        }
        for (Square square : Square.values()) {
            if (square.getLine() == line && square.getColumn() == column)
                return square;
        }
        return null;
    }

    public static Square getSquare(String squareName) {
        for (Square square : Square.values()) {
            if (Objects.equals(squareName, square.getName()))
                return square;
        }
        return null;
    }

    public void displayTable() {
        for (int line = 8; line >= 1; --line) {
            for (int column = 'a'; column <= 'h'; ++column) {
                Square square = getSquare(column, line);
                Piece piece = this.squareToPieceMap.get(square);
                if (piece == Piece.noPiece)
                    System.out.print(" _ ");
                else {
                    System.out.print(((char) piece.getColor().getName().charAt(0)) + piece.getAlias() + " ");
                }
            }
            System.out.println();
        }
    }

    public void updatePossibleMoves(Square startSquare, Square endSquare) {
        Piece piece = this.squareToPieceMap.get(startSquare);

        if (piece == Piece.whiteKing) {
            possibleWhiteShortCastle = false;
            possibleWhiteLongCastle = false;
        }
        if (piece == Piece.blackKing) {
            possibleBlackShortCastle = false;
            possibleBlackLongCastle = false;
        }
        if (piece == Piece.whiteRook && startSquare.getLine() == 1 && startSquare.getColumn() == 'h') {
            possibleWhiteShortCastle = false;
        }
        if (piece == Piece.whiteRook && startSquare.getLine() == 1 && startSquare.getColumn() == 'a') {
            possibleWhiteLongCastle = false;
        }
        if (piece == Piece.blackRook && startSquare.getLine() == 8 && startSquare.getColumn() == 'h') {
            possibleBlackShortCastle = false;
        }
        if (piece == Piece.blackRook && startSquare.getLine() == 8 && startSquare.getColumn() == 'a') {
            possibleBlackLongCastle = false;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int line = 8; line >= 1; --line) {
            for (char column = 'a'; column <= 'h'; ++column) {
                stringBuilder.append(this.squareToPieceMap.get(getSquare(column, line))).append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public enum Range {
        CLOSE,
        DISTANCE
    }


}
