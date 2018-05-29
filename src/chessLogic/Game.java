package chessLogic;

import gameArchitecture.Color;
import gameArchitecture.Move;
import gameArchitecture.Player;
import gameArchitecture.Table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static gameArchitecture.Table.computeFenFromTable;

public class Game {
    private List<String> positions = new ArrayList<>();

    private Table table = new Table();

    private Player whitePlayer;

    private Player blackPlayer;

    private Boolean isOver;

    private Integer currentPosition;

    private GameDetails gameDetails;

    private StringBuilder pgnBuilder = new StringBuilder();

    private String userGamesPath = "database/user/games.pgn";

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
            if (Table.getAllPossibleNextTables(this.table).size() == 0) {
                this.isOver = true;
                this.setOver(true);
                System.out.println(this.getToMove().getName() + " won.");
            }
        }
    }

    public void doMove(Move move) {
        if (this.gameDetails != null) {
            if (this.getToMove() == Color.White) {
                this.gameDetails.whiteMovesString.add(move.toString());
            } else {
                this.gameDetails.blackMovesString.add(move.toString());
            }
        }
        String pieceString = this.table.getSquareToPieceMap().get(move.getStartSquare()).getAlias();
        if (Objects.equals(pieceString, "P"))
            pieceString = "";
        if (this.getToMove() == Color.White) {
            pgnBuilder
                    .append(this.table.getFullMoveNumber() + 1)
                    .append(".")
                    .append(pieceString)
                    .append(move.getEndSquare().getName())
                    .append(" ");
        } else {
            pgnBuilder
                    .append(pieceString)
                    .append(move.getEndSquare().getName())
                    .append(" ");
        }
        if((this.table.getFullMoveNumber()+1) % 10 == 0 && this.table.getToMove() == Color.Black){
            pgnBuilder.append("\n");
        }

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

    public void writeToFile() throws IOException {
        if(this.gameDetails!=null && this.gameDetails.tags!=null) {
            StringBuilder tagsBuilder = new StringBuilder();
            this.gameDetails.tags.forEach(
                    (tag, value) -> tagsBuilder.append("[").append(tag).append(", \"").append(value).append("\"]\n")
            );
            Files.write(Paths.get(this.userGamesPath), tagsBuilder.toString().getBytes(), StandardOpenOption.APPEND);
        }
        Files.write(Paths.get(this.userGamesPath), this.pgnBuilder.toString().getBytes(), StandardOpenOption.APPEND);
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
        if (over) {
            this.pgnBuilder.append(" *\n");
        }
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

    public GameDetails getGameDetails() {
        return gameDetails;
    }

    public void setGameDetails(GameDetails gameDetails) {
        this.gameDetails = gameDetails;
    }
}
