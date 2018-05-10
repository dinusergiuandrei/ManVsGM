package GameArchitecture;

import java.io.Serializable;
import java.util.Objects;

public class Move implements Serializable {
    private Square startSquare;

    private Square endSquare;

    private Piece pieceAfterPromotion;


    public Square getStartSquare() {
        return startSquare;
    }

    public Square getEndSquare() {
        return endSquare;
    }

    public Move(){}

    public Move(Square startSquare, Square endSquare) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
    }

    public Move getReverseMove(){
        Move newMove = new Move(this.endSquare, this.endSquare);
        newMove.setPieceAfterPromotion(this.pieceAfterPromotion);
        return newMove;
    }

    @Override
    public String toString() {
        return " ( "+startSquare + " - " + endSquare + " ) ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return startSquare == move.startSquare &&
                endSquare == move.endSquare;
    }

    @Override
    public int hashCode() {

        return Objects.hash(startSquare, endSquare);
    }

    public Piece getPieceAfterPromotion() {
        return pieceAfterPromotion;
    }

    public void setPieceAfterPromotion(Piece pieceAfterPromotion) {
        this.pieceAfterPromotion = pieceAfterPromotion;
    }
}