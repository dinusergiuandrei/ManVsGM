package GameArchitecture;

import java.util.Objects;

public enum Piece {
    whitePawn("P", "pawn", Color.White, "P"),
    whiteKnight("N", "knight", Color.White, "N"),
    whiteBishop("B", "bishop", Color.White, "B"),
    whiteRook("R", "rook", Color.White, "R"),
    whiteQueen("Q", "queen", Color.White, "Q"),
    whiteKing("K", "king", Color.White, "K"),
    blackPawn("P", "pawn", Color.Black, "p"),
    blackKnight("N", "knight", Color.Black, "n"),
    blackBishop("B", "bishop", Color.Black, "b"),
    blackRook("R", "rook", Color.Black, "r"),
    blackQueen("Q", "queen", Color.Black, "q"),
    blackKing("K", "king", Color.Black, "k"),
    noPiece("NoP", "NO PIECE", null, "-");

    private String name;
    private String alias;
    private Color color;
    private String fenNotation;

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public Color getColor() {
        return color;
    }

    public String getFenNotation(){ return fenNotation;}

    Piece(String alias, String name, Color color, String fenNotation){
        this.alias = alias;
        this.name = name;
        this.color = color;
        this.fenNotation = fenNotation;
    }

    public static Piece getPiece(String alias){
        for(Piece piece : Piece.values()){
            if(Objects.equals(piece.alias, alias))
                return piece;
        }
        return null;
    }

    public static Piece getPieceFromFenNotation(char c){
        switch (c){
            case 'k': return blackKing;
            case 'q': return blackQueen;
            case 'r': return blackRook;
            case 'b': return blackBishop;
            case 'n': return blackKnight;
            case 'p': return blackPawn;
            case 'K': return whiteKing;
            case 'Q': return whiteQueen;
            case 'R': return whiteRook;
            case 'B': return whiteBishop;
            case 'N': return whiteKnight;
            case 'P': return whitePawn;
            default: return noPiece;
        }
    }

    @Override
    public String toString() {
        return this.fenNotation;
    }
}
