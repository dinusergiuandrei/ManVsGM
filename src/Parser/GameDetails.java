package Parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameDetails{
    public Map<String, String> tags;

    public List<String> whiteMoves;

    public List<String> blackMoves;

    public GameDetails(){
        whiteMoves = new LinkedList<>();
        blackMoves = new LinkedList<>();
    }

}