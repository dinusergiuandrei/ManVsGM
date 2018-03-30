package ChessLogic;

import GameArchitecture.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static ChessLogic.SemanticMoveValidator.isValidMoveSemantically;
import static ChessLogic.SyntacticMoveValidator.*;
import static GameArchitecture.Table.computeFENFromTable;
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
        table.setUpPieces();

        positions.add(computeFENFromTable(this.table));
        currentPosition = positions.size()-1;
    }

    public void setWhitePlayer(Player whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public void start() {
        while (!this.isOver) {
            if (this.table.getToMove() == Color.White) {
                whitePlayer.move(this);
                this.table.setToMove(Color.Black);
            } else {
                blackPlayer.move(this);
                this.table.setToMove(Color.White);
            }
        }
    }

    public Boolean canMove(Move move) {
        Square startSquare = move.getStartSquare();
        Square endSquare = move.getEndSquare();

        if (startSquare == null || endSquare == null) {
            System.out.println("Using a non valid square.");
            return false;
        }
        Piece piece = this.table.squareToPieceMap.get(startSquare);
        if (piece == Piece.noPiece) {
            System.out.println("Cannot move from a empty square!");
            return false;
        }
        if (piece.getColor() != this.table.getToMove()) {
            System.out.println("Wrong color to move!");
            return false;
        }

        return isValidMoveSemantically(this, new Move(startSquare, endSquare));
    }

    public void doMove(Move move){
        Square startSquare = move.getStartSquare();
        Square endSquare = move.getEndSquare();

        Piece piece1 = this.table.squareToPieceMap.get(startSquare);

        this.table.squareToPieceMap.put(startSquare, Piece.noPiece);
        this.table.squareToPieceMap.put(endSquare, piece1);

        if (piece1 == Piece.whiteKing && startSquare.getColumn() == 'e' && endSquare.getColumn() == 'g') {
            this.table.squareToPieceMap.put(Table.getSquare('h', 1), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('f', 1), Piece.whiteRook);
        }
        if (piece1 == Piece.whiteKing && startSquare.getColumn() == 'e' && endSquare.getColumn() == 'c') {
            this.table.squareToPieceMap.put(Table.getSquare('a', 1), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('d', 1), Piece.whiteRook);
        }
        if (piece1 == Piece.blackKing && startSquare.getColumn() == 'e' && endSquare.getColumn() == 'g') {
            this.table.squareToPieceMap.put(Table.getSquare('h', 8), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('f', 8), Piece.blackRook);
        }
        if (piece1 == Piece.blackKing && startSquare.getColumn() == 'e' && endSquare.getColumn() == 'c') {
            this.table.squareToPieceMap.put(Table.getSquare('a', 8), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('d', 8), Piece.blackRook);
        }

        if (piece1 == Piece.whitePawn && endSquare.getLine() == 8)
            this.table.squareToPieceMap.put(endSquare, move.getPieceAfterPromotion());  // todo: piece

        if (piece1 == Piece.blackPawn && endSquare.getLine() == 1)
            this.table.squareToPieceMap.put(endSquare, move.getPieceAfterPromotion());

        if (piece1 == Piece.whitePawn && this.table.squareToPieceMap.get(endSquare) == Piece.noPiece) {
            this.table.squareToPieceMap.put(Table.getSquare(endSquare.getColumn(), 5), Piece.noPiece);
        }

        if (piece1 == Piece.blackPawn && this.table.squareToPieceMap.get(endSquare) == Piece.noPiece) {
            this.table.squareToPieceMap.put(Table.getSquare(endSquare.getColumn(), 4), Piece.noPiece);
        }

        this.table.updatePossibleMoves(startSquare, endSquare);

        this.table.setFullMoveNumber(this.table.getFullMoveNumber()+1);

        updateToMove();
        positions.add(computeFENFromTable(this.table));
        currentPosition = positions.size()-1;
    }

    public void undo(){
        if(this.currentPosition == 0){
            System.out.println("Can not undo! No previous move.");
            return;
        }
        currentPosition--;
        this.table = Table.computeTableFromFEN(this.positions.get(currentPosition));
    }

    public Move getMove(String moveString){
        moveString = cleanMoveString(moveString);

        Move move = getMove1(moveString);
        Piece piece = getPieceFromMoveString(moveString, this.table.getToMove());
        try {
            if (move.getEndSquare().getLine() == 8 && piece.getColor() == Color.White) {
                char l = moveString.charAt(moveString.length() - 1);
                move.setPieceAfterPromotion(Piece.getPieceFromFenNotation(l));

            } else if (move.getEndSquare().getLine() == 1 && piece.getColor() == Color.Black) {
                char l = moveString.charAt(moveString.length() - 1);

                move.setPieceAfterPromotion(Piece.getPieceFromFenNotation((char) (l - 'A' + 'a')));
            }
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        return move;
    }

    public Move getMove1(String moveString) {

        Piece piece = getPieceFromMoveString(moveString, this.table.getToMove());
        Square endSquare;

        if (Objects.equals(moveString, "O-O")) {
            if (this.table.getToMove() == Color.White) {
                return new Move(getSquare('e', 1), getSquare('g', 1));
            }
            if (this.table.getToMove() == Color.Black) {
                return new Move(getSquare('e', 8), getSquare('g', 8));
            }
        }

        if (Objects.equals(moveString, "O-O-O")) {
            if (this.table.getToMove() == Color.White) {
                return new Move(getSquare('e', 1), getSquare('c', 1));
            }
            if (this.table.getToMove() == Color.Black) {
                return new Move(getSquare('e', 8), getSquare('c', 8));
            }
        }

        if (moveString.length() == 2) {
            // this can only be a pawn move

            endSquare = Table.getSquareFromMoveString(moveString);

            List<Move> pawnPushMoves = getAllPawnPushMoves(endSquare, this.table.getToMove());

            List<Move> legalMoves = new LinkedList<>();

            for (Move move : pawnPushMoves) {
                if (this.table.squareToPieceMap.get(move.getStartSquare()) == Piece.whitePawn
                        || this.table.squareToPieceMap.get(move.getStartSquare()) == Piece.blackPawn)
                    legalMoves.add(move);
            }

            if (legalMoves.size() == 1) {
                return legalMoves.get(0);
            }

            if (legalMoves.size() == 2) {
                if (isValidMoveSemantically(this, legalMoves.get(0)))
                    return legalMoves.get(0);
                if (isValidMoveSemantically(this, legalMoves.get(1)))
                    return legalMoves.get(1);

            } else {
                System.out.println("Could not determine move of length 2: " + moveString);
                this.table.displayTable();
            }

            return null;
        }

        if (moveString.length() == 3) {
            endSquare = Table.getSquareFromMoveString(moveString);
            List<Move> baseMoves = null;
            if (piece != null) {
                switch (piece) {
                    case whiteKing:
                    case blackKing:
                        baseMoves = getAllKingMoves(endSquare, this.table.getToMove());
                        break;
                    case whiteQueen:
                    case blackQueen:
                        baseMoves = getAllQueenMoves(endSquare);
                        break;
                    case whiteRook:
                    case blackRook:
                        baseMoves = getAllRookMoves(endSquare);
                        break;
                    case whiteBishop:
                    case blackBishop:
                        baseMoves = getAllBishopMoves(endSquare);
                        break;
                    case whiteKnight:
                    case blackKnight:
                        baseMoves = getAllKnightMoves(endSquare);
                        break;
                    case whitePawn:
                        baseMoves = getAllPawnCaptureMoves(endSquare, this.table.getToMove());
                        List<Move> pawnMoves = getAllPawnPushMoves(endSquare, this.table.getToMove());
                        List<Move> validPawnSquares = new LinkedList<>();
                        pawnMoves.forEach(
                                move -> {
                                    if(move.getStartSquare().getLine() == 7)
                                        validPawnSquares.add(move);
                                }
                        );
                        baseMoves.addAll(validPawnSquares);


                        break;
                    case blackPawn:
                        baseMoves = getAllPawnCaptureMoves(endSquare, this.table.getToMove());
                        List<Move> pawnSquares1 = getAllPawnPushMoves(endSquare, this.table.getToMove());
                        List<Move> validPawnSquares1 = new LinkedList<>();
                        pawnSquares1.forEach(
                                square -> {
                                    if(square.getStartSquare().getLine() == 2)
                                        validPawnSquares1.add(square);
                                }
                        );
                        baseMoves.addAll(validPawnSquares1);

                        break;
                }
            }

            List<Move> legalMoves = new LinkedList<>();

//            if(Objects.equals(moveString, "Nd2") ){
//                System.out.println("Nd2 found");
//            }

            if (baseMoves != null) {
                for (Move baseMove : baseMoves) {
                    if (this.table.squareToPieceMap.get(baseMove.getStartSquare()) == piece) {
                        legalMoves.add(baseMove);
                    }
                }
            }

            if (legalMoves.size() > 1 && (piece == Piece.whitePawn || piece == Piece.blackPawn)) {
                char detail = moveString.charAt(0);
                if (baseMoves != null) {
                    for (Move legalMove : legalMoves) {
                        if (this.table.squareToPieceMap.get(legalMove.getStartSquare()) == piece
                                && legalMove.getStartSquare().getColumn() == detail) {
                            return legalMove;
                        }
                    }
                }

            }


            if (legalMoves.size() == 1) {
                return legalMoves.get(0);
            }

            if (legalMoves.size() == 2) {
                Move move1 = legalMoves.get(0);
                Move move2 = legalMoves.get(1);

                if (isValidMoveSemantically(this, move1))
                    return move1;
                if (isValidMoveSemantically(this, move2))
                    return move2;

                if (move1.getStartSquare().getLine() == move2.getStartSquare().getLine()) {
                    return Math.abs(move1.getStartSquare().getColumn() - endSquare.getColumn()) < Math.abs(move2.getStartSquare().getColumn() - endSquare.getColumn()) ?
                            move1 : move2;
                }
                if (move1.getStartSquare().getColumn() == move2.getStartSquare().getColumn()) {
                    return Math.abs(move1.getStartSquare().getLine() - endSquare.getLine()) < Math.abs(move2.getStartSquare().getLine() - endSquare.getLine()) ?
                            move1 : move2;
                }
            }


        }

        if (moveString.length() == 4) {
            endSquare = Table.getSquareFromMoveString(moveString);
            List<Move> baseMoves = null;
            if (piece != null) {
                switch (piece) {
                    case whiteKing:
                    case blackKing:
                        baseMoves = getAllKingMoves(endSquare, this.table.getToMove());
                        break;
                    case whiteQueen:
                    case blackQueen:
                        baseMoves = getAllQueenMoves(endSquare);
                        break;
                    case whiteRook:
                    case blackRook:
                        baseMoves = getAllRookMoves(endSquare);
                        break;
                    case whiteBishop:
                    case blackBishop:
                        baseMoves = getAllBishopMoves(endSquare);
                        break;
                    case whiteKnight:
                    case blackKnight:
                        baseMoves = getAllKnightMoves(endSquare);
                        break;
                    case whitePawn:
                    case blackPawn:
                        baseMoves = getAllPawnCaptureMoves(endSquare, this.table.getToMove());
                        break;
                }
            }

            List<Move> legalMoves = new LinkedList<>();

            if (baseMoves != null) {
                for (Move baseMove : baseMoves) {
                    if (this.table.squareToPieceMap.get(baseMove.getStartSquare()) == piece) {
                        legalMoves.add(baseMove);
                    }
                }
            }

            char detail = moveString.charAt(1);
            if (detail >= '1' && detail <= '8') {
                if (baseMoves != null) {
                    for (Move legalMove : legalMoves) {
                        if (this.table.squareToPieceMap.get(legalMove.getStartSquare()) == piece
                                && legalMove.getStartSquare().getLine() == detail - '0') {
                            return legalMove;
                        }
                    }
                }
            }

            if (detail >= 'a' && detail <= 'h') {
                if (baseMoves != null) {
                    for (Move legalMove : legalMoves) {
                        if (this.table.squareToPieceMap.get(legalMove.getStartSquare()) == piece
                                && legalMove.getStartSquare().getColumn() == detail) {
                            return legalMove;
                        }
                    }
                }
            }


            if (legalMoves.size() == 1) {
                return legalMoves.get(0);
            } else {
                System.err.println("Could not determine starting square for move " + moveString);
            }


        }

        return null;
    }

    private Piece getPieceFromMoveString(String moveString, Color toMove) {
        Piece piece = null;

        if (toMove == Color.White) {
            switch (moveString.substring(0, 1)) {
                case "N":
                    piece = Piece.whiteKnight;
                    break;
                case "B":
                    piece = Piece.whiteBishop;
                    break;
                case "K":
                    piece = Piece.whiteKing;
                    break;
                case "Q":
                    piece = Piece.whiteQueen;
                    break;
                case "R":
                    piece = Piece.whiteRook;
                    break;
                default:
                    piece = Piece.whitePawn;
            }
        }
        if (toMove == Color.Black) {
            switch (moveString.substring(0, 1)) {
                case "N":
                    piece = Piece.blackKnight;
                    break;
                case "B":
                    piece = Piece.blackBishop;
                    break;
                case "K":
                    piece = Piece.blackKing;
                    break;
                case "Q":
                    piece = Piece.blackQueen;
                    break;
                case "R":
                    piece = Piece.blackRook;
                    break;
                default:
                    piece = Piece.blackPawn;
            }
        }

        return piece;
    }

    private String cleanMoveString(String moveString) {
        StringBuilder result = new StringBuilder();
        String extraCharacters = "+:x=";
        for (char c : moveString.toCharArray()) {
            if (extraCharacters.indexOf(c) == -1) {
                result.append(c);
            }
        }
        return result.toString();
    }

    public void updateToMove() {
        if (this.table.getToMove() == Color.White)
            this.table.setToMove(Color.Black);
        else  this.table.setToMove(Color.White);
    }

    public Color getToMove() {
        return this.table.getToMove();
    }

    public List<String> getPositions() {
        return positions;
    }

    public void setPositions(List<String> positions) {
        this.positions = positions;
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

    public Game getCopy(){
        Game newGame = new Game();

        newGame.setWhitePlayer(this.getWhitePlayer());
        newGame.setBlackPlayer(this.getBlackPlayer());
        newGame.setOver(this.getOver());
        newGame.setToMove(this.getToMove());

        newGame.setTable(table.getCopy());

        return newGame;
    }
}
