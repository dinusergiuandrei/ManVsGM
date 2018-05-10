package ChessLogic;

import GameArchitecture.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static ChessLogic.SemanticMoveValidator.isValidMoveSemantically;
import static ChessLogic.SyntacticMoveValidator.*;
import static GameArchitecture.Piece.*;
import static GameArchitecture.Table.computeFenFromTable;
import static GameArchitecture.Table.getSquare;

public class Game {
    private List<String> positions = new ArrayList<>();

    private Table table = new Table();

    private Player whitePlayer;

    private Player blackPlayer;

    private Boolean isOver;

    private Integer currentPosition;

    public Game() {
        this.isOver = false;
        this.table.setGame(this);
        table.setUpPieces();

        positions.add(computeFenFromTable(this.table));
        currentPosition = positions.size() - 1;
    }

    public void start() {
        while (!this.isOver) {
            this.table.getPlayerToMove().move(this);
        }
    }

    public void doMove(Move move) {
        table.doMove(move);
        updatePositions();
    }

    public void undo() {
        if (currentPosition == 0) {
            System.out.println("Can not undo! No previous move.");
            return;
        }
        currentPosition--;
        this.table = Table.computeTableFromFen(this.positions.get(currentPosition));
        this.positions.remove(currentPosition + 1);
    }

    private void updatePositions() {
        positions.add(computeFenFromTable(this.table));
        currentPosition = positions.size() - 1;
    }

    public Game getCopy() {
        Game newGame = new Game();

        newGame.setWhitePlayer(this.getWhitePlayer());
        newGame.setBlackPlayer(this.getBlackPlayer());
        newGame.setOver(this.getOver());
        newGame.setToMove(this.getToMove());

        newGame.setTable(table.getCopy());

        return newGame;
    }


    // getters and setters

    public void setWhitePlayer(Player whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public Color getToMove() {
        return this.table.getToMove();
    }

    public List<String> getPositions() {
        return positions;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Boolean getOver() {
        return isOver;
    }

    public void setOver(Boolean over) {
        isOver = over;
    }

    public void setToMove(Color toMove) {
        this.table.setToMove(toMove);
    }

    public Table getTable() {
        return table;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }
}
