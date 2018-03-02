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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        tags.forEach(
                (name, value) -> stringBuilder.append("[").append(name).append(" \"").append(value).append("\"]\n")
        );
        for(int i=0; i < blackMoves.size(); ++i){
            stringBuilder.append(i + 1).append(".").append(whiteMoves.get(i)).append(" ").append(blackMoves.get(i)).append(" ");
        }
        if(whiteMoves.size()>blackMoves.size()){
            stringBuilder.append(whiteMoves.size()).append(".").append(whiteMoves.get(whiteMoves.size() - 1));
        }
        return stringBuilder.toString();
    }
}