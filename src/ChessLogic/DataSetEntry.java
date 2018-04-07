package ChessLogic;

import GameArchitecture.Move;

public class DataSetEntry {
    public String position;
    public Move move;

    public DataSetEntry(String position, Move move) {
        this.position = position;
        this.move = move;
    }
}
