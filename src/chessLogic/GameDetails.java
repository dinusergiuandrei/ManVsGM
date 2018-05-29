package chessLogic;

import gameArchitecture.Move;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameDetails {
    public Map<String, String> tags = new LinkedHashMap<>();

    public List<String> whiteMovesString = new LinkedList<>();

    public List<String> blackMovesString = new LinkedList<>();

    public List<Move> whiteMoves = new LinkedList<>();

    public List<Move> blackMoves = new LinkedList<>();

    public Integer totalMoveCount = 0;

    public Game game;

    public GameDetails() {
    }

    public void computeMoves() {
        this.game = new Game();

        try {
            for (int moveCount = 0; moveCount < this.blackMovesString.size(); ++moveCount) {

                String whiteMoveString = this.whiteMovesString.get(moveCount);

                if (whiteMoveString.contains(" *") || whiteMoveString.length() < 2) {
                    continue;
                }

                Move whiteMove = game.getTable().getMove(whiteMoveString);

                if (whiteMove == null) {
                    System.out.println("Could not recognize white move: " + whiteMoveString);
                    return;
                }

                this.whiteMoves.add(whiteMove);
                totalMoveCount++;
                game.doMove(whiteMove);


                String blackMoveString = this.blackMovesString.get(moveCount);

                if (blackMoveString.contains(" *") || blackMoveString.length() < 2) {
                    continue;
                }

                Move blackMove = game.getTable().getMove(blackMoveString);

                if (blackMove == null) {
                    System.out.println("Could not recognize black move: " + blackMoveString);
                    System.out.println(game.getPositions().get(game.getPositions().size() - 1));
                    return;
                }

                this.blackMoves.add(blackMove);
                totalMoveCount++;

                game.doMove(blackMove);
            }

            if (whiteMovesString.size() > blackMovesString.size()) {

                String whiteMoveString = this.whiteMovesString.get(whiteMovesString.size() - 1);

                if (whiteMoveString.contains(" *") || whiteMoveString.length() < 2) {
                    return;
                }

                Move whiteMove = game.getTable().getMove(whiteMoveString);

                if (whiteMove == null) {
                    System.out.println("Could not recognize white move: " + whiteMoveString);
                    return;
                }

                this.whiteMoves.add(whiteMove);
                totalMoveCount++;

                game.doMove(whiteMove);
            }
        }
        catch (NullPointerException e){
            System.out.println("Could not understand a move in database");
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        tags.forEach(
                (name, value) -> stringBuilder.append("[").append(name).append(" \"").append(value).append("\"]\n")
        );
        for (int i = 0; i < blackMovesString.size(); ++i) {
            stringBuilder.append(i + 1).append(".").append(whiteMovesString.get(i)).append(" ").append(blackMovesString.get(i)).append(" ");
            if ((i + 1) % 10 == 0)
                stringBuilder.append("\n");
        }
        if (whiteMovesString.size() > blackMovesString.size()) {
            stringBuilder.append(whiteMovesString.size()).append(".").append(whiteMovesString.get(whiteMovesString.size() - 1));
        }
        return stringBuilder.toString();
    }
}