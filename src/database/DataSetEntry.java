package database;

import gameArchitecture.Move;

import java.io.Serializable;

public class DataSetEntry implements Serializable {
    private String positionFenString;
    private Move move;

    public DataSetEntry(String positionFenString, Move move) {
        this.positionFenString = positionFenString;
        this.move = move;
    }

    public String getPositionFenString() {
        return positionFenString;
    }

    public void setPositionFenString(String positionFenString) {
        this.positionFenString = positionFenString;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }
}
