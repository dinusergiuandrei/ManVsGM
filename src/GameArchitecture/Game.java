package GameArchitecture;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static GameArchitecture.Table.*;

public class Game {

    public Table table;

    private Player whitePlayer;

    private Player blackPlayer;

    private Boolean isOver;

    private Color toMove;

    public Game(){
        this.isOver = false;
        toMove = Color.White;
        table = new Table();
        table.setUpPieces();
    }

    public void setWhitePlayer(Player whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }


    public void start() {
        while(!this.isOver){
            if(toMove == Color.White){
                whitePlayer.move(this);
                toMove = Color.Black;
            }
            else {
                blackPlayer.move(this);
                toMove = Color.White;
            }
        }
    }

    private Boolean canMoveKing(Table.Square startSquare, Table.Square endSquare){
        return this.table.getKingMoves(startSquare).contains(endSquare);
    }

    private Boolean canMoveQueen(Table.Square startSquare, Table.Square endSquare){
        return this.table.getQueenMoves(startSquare).contains(endSquare);
    }

    private Boolean canMoveRook(Table.Square startSquare, Table.Square endSquare){
        return this.table.getRookMoves(startSquare).contains(endSquare);
    }

    private Boolean canMoveBishop(Table.Square startSquare, Table.Square endSquare){
        return this.table.getBishopMoves(startSquare).contains(endSquare);

    }

    private Boolean canMoveKnight(Table.Square startSquare, Table.Square endSquare){
        return this.table.getKnightMoves(startSquare).contains(endSquare);
    }

    private Boolean canMovePawn(Table.Square startSquare, Table.Square endSquare){
        return this.table.getPawnMoves(startSquare).contains(endSquare);
    }

    public Boolean canMove(Table.Square startSquare, Table.Square endSquare){
        if(startSquare == null || endSquare == null){
            System.out.println("Using a non valid square.");
            return false;
        }
        Piece piece = this.table.squareToPieceMap.get(startSquare);
        if(piece==Piece.noPiece){
            System.out.println("Cannot move from a empty square!");
            return false;
        }
        if(piece.color!=this.toMove){
            System.out.println("Wrong color to move!");
            return false;
        }
        switch (piece){
            case whiteKing: case blackKing: return canMoveKing(startSquare, endSquare);
            case whiteQueen: case blackQueen: return canMoveQueen(startSquare, endSquare);
            case whiteRook: case blackRook: return canMoveRook(startSquare, endSquare);
            case whiteBishop: case blackBishop: return canMoveBishop(startSquare, endSquare);
            case whiteKnight: case blackKnight: return canMoveKnight(startSquare, endSquare);
            case whitePawn: case blackPawn: return canMovePawn(startSquare, endSquare);
        }
        return false;
    }

    public void doMove(Table.Square startSquare, Table.Square endSquare){
        Piece piece1 = this.table.squareToPieceMap.get(startSquare);

        this.table.squareToPieceMap.put(startSquare, Piece.noPiece);
        this.table.squareToPieceMap.put(endSquare, piece1);

        if(piece1 == Piece.whiteKing && startSquare.column=='e' && endSquare.column=='g'){
            this.table.squareToPieceMap.put(Table.getSquare('h', 1), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('f', 1), Piece.whiteRook);
        }
        if(piece1 == Piece.whiteKing && startSquare.column=='e' && endSquare.column=='c'){
            this.table.squareToPieceMap.put(Table.getSquare('a', 1), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('d', 1), Piece.whiteRook);
        }
        if(piece1 == Piece.blackKing && startSquare.column=='e' && endSquare.column=='g'){
            this.table.squareToPieceMap.put(Table.getSquare('h', 8), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('f', 8), Piece.blackRook);
        }
        if(piece1 == Piece.blackKing && startSquare.column=='e' && endSquare.column=='c'){
            this.table.squareToPieceMap.put(Table.getSquare('a', 8), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('d', 8), Piece.blackRook);
        }

        if(piece1 == Piece.whitePawn && endSquare.line == 8)
            this.table.squareToPieceMap.put(endSquare, Piece.whiteQueen);

        if(piece1 == Piece.blackPawn && endSquare.line == 1)
            this.table.squareToPieceMap.put(endSquare, Piece.blackQueen);

        if(piece1 == Piece.whitePawn && this.table.squareToPieceMap.get(endSquare) == Piece.noPiece) {
            this.table.squareToPieceMap.put(Table.getSquare(endSquare.column, 5), Piece.noPiece);
        }

        if(piece1 == Piece.blackPawn && this.table.squareToPieceMap.get(endSquare) == Piece.noPiece) {
            this.table.squareToPieceMap.put(Table.getSquare(endSquare.column, 4), Piece.noPiece);
        }

        this.table.updatePossibleMoves(startSquare, endSquare);
    }

    public Move getMove(String moveString){
        moveString = cleanMoveString(moveString);

        Piece piece = getPieceFromMoveString(moveString, this.toMove);
        Table.Square endSquare;

        if(Objects.equals(moveString, "O-O")){
            if(toMove==Color.White){
                return new Move(getSquare('e', 1), getSquare('g', 1));
            }
            if(toMove==Color.Black){
                return new Move(getSquare('e', 8), getSquare('g', 8));
            }
        }

        if(Objects.equals(moveString, "O-O-O")){
            if(toMove==Color.White){
                return new Move(getSquare('e', 1), getSquare('c', 1));
            }
            if(toMove==Color.Black){
                return new Move(getSquare('e', 8), getSquare('c', 8));
            }
        }

        if(moveString.length()==2){
            endSquare = getSquare(moveString);
            List<Table.Square> startingSquares = getAllPawnPushMoves(endSquare, toMove);

            List<Table.Square> legalStartingSquares = new LinkedList<>();

            for (Square startingSquare : startingSquares) {
                if(this.table.squareToPieceMap.get(startingSquare) == Piece.whitePawn
                        ||this.table.squareToPieceMap.get(startingSquare) == Piece.blackPawn)
                    legalStartingSquares.add(startingSquare);
            }

            if(legalStartingSquares.size()==1){
                return new Move(legalStartingSquares.get(0), endSquare);
            }

            return null;
        }

        if(moveString.length()==3){
            endSquare = getSquare(moveString.substring(1));
            List<Table.Square> startingSquares = null;
            if (piece != null) {
                switch (piece){
                    case whiteKing:   case blackKing:   startingSquares = getAllKingMoves   (endSquare);   break;
                    case whiteQueen:  case blackQueen:  startingSquares = getAllQueenMoves  (endSquare);   break;
                    case whiteRook:   case blackRook:   startingSquares = getAllRookMoves   (endSquare);   break;
                    case whiteBishop: case blackBishop: startingSquares = getAllBishopMoves (endSquare);   break;
                    case whiteKnight: case blackKnight: startingSquares = getAllKnightMoves (endSquare);   break;
                }
            }

            List<Table.Square> legalStartingSquares = new LinkedList<>();

            if (startingSquares != null) {
                for (Square startingSquare : startingSquares) {
                    if(this.table.squareToPieceMap.get(startingSquare) == piece){
                        legalStartingSquares.add(startingSquare);
                    }
                }
            }

            if(legalStartingSquares.size() == 1){
                return new Move(legalStartingSquares.get(0), endSquare);
            }
            return null;
        }



        return null;
    }

    private Piece getPieceFromMoveString(String moveString, Color toMove){
        Piece piece = null;

        if(this.toMove==Color.White){
            switch (moveString.substring(0, 1)){
                case "N": piece = Piece.whiteKnight; break;
                case "B": piece = Piece.whiteBishop; break;
                case "K": piece = Piece.whiteKing;   break;
                case "Q": piece = Piece.whiteQueen;  break;
                case "R": piece = Piece.whiteRook;   break;
                default: piece = Piece.whitePawn;
            }
        }
        if(this.toMove==Color.Black){
            switch (moveString.substring(0, 1)){
                case "N": piece = Piece.blackKnight; break;
                case "B": piece = Piece.blackBishop; break;
                case "K": piece = Piece.blackKing;   break;
                case "Q": piece = Piece.blackQueen;  break;
                case "R": piece = Piece.blackRook;   break;
                default: piece = Piece.blackPawn;
            }
        }

        return piece;
    }

    private String cleanMoveString(String moveString){
        StringBuilder result = new StringBuilder();
        String extraCharacters = "+:x";
        for(char c : moveString.toCharArray()){
            if(extraCharacters.indexOf(c) == -1){
                result.append(c);
            }
        }
        return result.toString();
    }

    public void updateToMove(){
        if(this.toMove==Color.White)
            this.toMove = Color.Black;
        else this.toMove = Color.White;
    }
}
