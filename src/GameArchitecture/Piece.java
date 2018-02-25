package GameArchitecture;

import java.util.Objects;

public enum Piece {
    whitePawn("P", "pawn", Color.White),
    whiteKnight("N", "knight", Color.White),
    whiteBishop("B", "bishop", Color.White),
    whiteRook("R", "rook", Color.White),
    whiteQueen("Q", "queen", Color.White),
    whiteKing("K", "king", Color.White),
    blackPawn("P", "pawn", Color.Black),
    blackKnight("N", "knight", Color.Black),
    blackBishop("B", "bishop", Color.Black),
    blackRook("R", "rook", Color.Black),
    blackQueen("Q", "queen", Color.Black),
    blackKing("K", "king", Color.Black),
    noPiece("NoP", "NO PIECE", null);

    String name;
    String alias;
    Color color;

    Piece(String alias, String name, Color color){
        this.alias = alias;
        this.name = name;
        this.color = color;
    }

    public static Piece getPiece(String alias){
        for(Piece piece : Piece.values()){
            if(Objects.equals(piece.alias, alias))
                return piece;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
