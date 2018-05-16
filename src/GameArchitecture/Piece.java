package GameArchitecture;

import ChessLogic.SemanticMoveValidator;

import java.util.List;
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

    public List<Move> getSemanticallyValidMoves(Table table, Square square){
        switch (this){
            case whitePawn:  return SemanticMoveValidator.getLegalPawnMoves(table, square);
            case whiteKnight: return SemanticMoveValidator.getLegalKnightMoves(table, square);
            case whiteBishop: return SemanticMoveValidator.getLegalBishopMoves(table, square);
            case whiteRook: return SemanticMoveValidator.getLegalRookMoves(table, square);
            case whiteQueen: return SemanticMoveValidator.getLegalQueenMoves(table, square);
            case whiteKing: return SemanticMoveValidator.getLegalKingMoves(table, square);

            case blackPawn: return SemanticMoveValidator.getLegalPawnMoves(table, square);
            case blackKnight: return SemanticMoveValidator.getLegalKnightMoves(table, square);
            case blackBishop: return SemanticMoveValidator.getLegalBishopMoves(table, square);
            case blackRook: return SemanticMoveValidator.getLegalRookMoves(table, square);
            case blackQueen: return SemanticMoveValidator.getLegalQueenMoves(table, square);
            case blackKing: return SemanticMoveValidator.getLegalKingMoves(table, square);

            default: return null;
        }
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

    public Double getValue(){
        switch (this){

            case whitePawn: return 1.0;
            case whiteKnight: return 3.0;
            case whiteBishop: return 3.0;
            case whiteRook: return 5.0;
            case whiteQueen: return 9.0;
            case whiteKing: return 4.0;
            case blackPawn: return 1.0;
            case blackKnight: return 3.0;
            case blackBishop: return 3.0;
            case blackRook: return 5.0;
            case blackQueen: return 9.0;
            case blackKing: return 4.0;
            case noPiece: return 0.0;
            default: return 0.0;
        }
    }

    @Override
    public String toString() {
        return this.fenNotation;
    }
}
