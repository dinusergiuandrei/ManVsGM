package MoveGenerator;

import GameArchitecture.Move;
import GameArchitecture.Table;

public interface MoveGenerator {
    Move getMove(Table table);
}
