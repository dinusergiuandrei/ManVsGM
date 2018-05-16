package moveGenerator;

import gameArchitecture.Move;
import gameArchitecture.Table;

public interface MoveGenerator {
    Move getMove(Table table);
}
