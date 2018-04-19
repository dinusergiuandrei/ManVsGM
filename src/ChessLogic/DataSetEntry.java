package ChessLogic;

import GameArchitecture.Move;

public class DataSetEntry {
    private String position;
    private Move move;

    public DataSetEntry(String position, Move move) {
        this.position = position;
        this.move = move;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }
}
