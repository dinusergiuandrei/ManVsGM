package ChessLogic;

import GameArchitecture.Move;
import GameArchitecture.Square;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameDetails{
    public Map<String, String> tags;

    public List<String> whiteMovesString = new LinkedList<>();

    public List<String> blackMovesString = new LinkedList<>();

    public List<Move> whiteMoves = new LinkedList<>();

    public List<Move> blackMoves = new LinkedList<>();

    public GameDetails(){
    }

    public void computeMoves(){
        Game game = new Game();
        //todo: fix :in fen, white has first two moves
        for(int moveCount = 0; moveCount < this.blackMovesString.size(); ++moveCount){
            Move whiteMove = game.getMove(this.whiteMovesString.get(moveCount));


            if(whiteMove == null){
                System.out.println("Could not recognize white move: "+this.whiteMovesString.get(moveCount));
                game.getTable().displayTable();
                return;
            }
            this.whiteMoves.add(whiteMove);
            game.doMove(whiteMove.getStartSquare(), whiteMove.getEndSquare());

            Move blackMove = game.getMove(this.blackMovesString.get(moveCount));
            if(blackMove == null){
                System.out.println("Could not recognize black move: "+this.blackMovesString.get(moveCount));
                return;
            }
            this.blackMoves.add(blackMove);
            game.doMove(blackMove.getStartSquare(), blackMove.getEndSquare());

        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        tags.forEach(
                (name, value) -> stringBuilder.append("[").append(name).append(" \"").append(value).append("\"]\n")
        );
        for(int i = 0; i < blackMovesString.size(); ++i){
            stringBuilder.append(i + 1).append(".").append(whiteMovesString.get(i)).append(" ").append(blackMovesString.get(i)).append(" ");
            if((i+1)%10==0)
                stringBuilder.append("\n");
        }
        if(whiteMovesString.size()> blackMovesString.size()){
            stringBuilder.append(whiteMovesString.size()).append(".").append(whiteMovesString.get(whiteMovesString.size() - 1));
        }
        return stringBuilder.toString();
    }
}