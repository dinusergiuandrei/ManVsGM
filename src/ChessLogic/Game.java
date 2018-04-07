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
        table.setUpPieces();

        positions.add(computeFenFromTable(this.table));
        currentPosition = positions.size() - 1;
    }

    public void setWhitePlayer(Player whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public void start() {
        while (!this.isOver) {
            this.getPlayerToMove().move(this);
        }
    }

    public Boolean canMove(Move move) {
        if (usesNullSquares(move)) {
            System.out.println("Using a non valid square.");
            return false;
        }

        if (moveStartsOnEmptySquare(move)) {
            System.out.println("Cannot move from a empty square!");
            return false;
        }

        if (moveMadeByWrongColor(move)) {
            System.out.println("Wrong color to move!");
            return false;
        }

        return isValidMoveSemantically(this, move);
    }

    public void doMove(Move move) {
        updateMetadata(move);

        Boolean enPassant = handleEnPassant(move);

        Boolean castles = handleCastles(move);

        Boolean pawnPromotion = handlePawnPromotion(move);

        if (!enPassant && !castles && !pawnPromotion)
            doMoveAsSimpleMove(move);


        updateToMove();

        updatePositions();


    }

    public void undo() {
        if (this.currentPosition == 0) {
            System.out.println("Can not undo! No previous move.");
            return;
        }
        currentPosition--;
        this.table = Table.computeTableFromFen(this.positions.get(currentPosition));
        this.positions.remove(currentPosition + 1);
    }

    public Move getMove(String moveString) {

        moveString = cleanMoveString(moveString);

        Move move = getMoveWithoutPawnPromotion(moveString);

        try {
            move.setPieceAfterPromotion(getPieceAfterPawnPromotion(moveString, move));
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
        return move;
    }

    private Piece getPieceAfterPawnPromotion(String moveString, Move move){
        Piece piece = getPieceFromMoveString(moveString, this.table.getToMove());
        try {
            if (move.getEndSquare().getLine() == 8 && piece == Piece.whitePawn) {
                char pieceFenNotation = moveString.charAt(moveString.length() - 1);
                return Piece.getPieceFromFenNotation(pieceFenNotation);

            } else if (move.getEndSquare().getLine() == 1 && piece == Piece.blackPawn) {
                char pieceFenNotation = moveString.charAt(moveString.length() - 1);

                return Piece.getPieceFromFenNotation((char) (pieceFenNotation - 'A' + 'a'));
            }
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Move getMoveWithoutPawnPromotion(String moveString) {
        Move castlesMove = getCastlesMove(moveString);
        if (castlesMove != null)
            return castlesMove;

        if (moveString.length() == 2) {
            return getMoveFromStringOfLength2(moveString);
        }

        if (moveString.length() == 3) {
            return getMoveFromStringOfLength3(moveString);
        }

        if (moveString.length() == 4) {
            return getMoveFromStringOfLength4(moveString);
        }

        return null;
    }

    private Move getCastlesMove(String moveString) {
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
        return null;
    }

    private Move getMoveFromStringOfLength2(String moveString){
        // this can only be a pawn move

        Square endSquare = Table.getSquareFromMoveString(moveString);

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

    private Move getMoveFromStringOfLength3(String moveString){
        Piece piece = getPieceFromMoveString(moveString, this.table.getToMove());
        Square endSquare = Table.getSquareFromMoveString(moveString);
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
                                if (move.getStartSquare().getLine() == 7)
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
                                if (square.getStartSquare().getLine() == 2)
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

//                if(move1.getStartSquare() == Square.h1 && move1.getEndSquare() == Square.d1 && move2.getStartSquare() == Square.a1){
//                    System.out.println("chosing wrong rook");
//                }

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
        return null;
    }

    private Move getMoveFromStringOfLength4(String moveString){
        Piece piece = getPieceFromMoveString(moveString, this.table.getToMove());
        Square endSquare = Table.getSquareFromMoveString(moveString);
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
                    piece = whiteKing;
                    break;
                case "Q":
                    piece = whiteQueen;
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
                    piece = blackKing;
                    break;
                case "Q":
                    piece = blackQueen;
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
        String extraCharacters = "+:x= *#";

        int quoteStartIndex = moveString.indexOf('{');
        int quoteEndIndex = moveString.indexOf('}');

        if (quoteStartIndex >= 0 && quoteEndIndex >= 0) {
            // ... { ... } ...
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(moveString, 0, quoteStartIndex);
            stringBuilder.append(moveString.substring(quoteEndIndex + 1));
            moveString = stringBuilder.toString();
        }
        if (quoteStartIndex >= 0 && quoteEndIndex < 0) {
            // ... { ...
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(moveString, 0, quoteStartIndex);
            moveString = stringBuilder.toString();
        }
        if (quoteStartIndex < 0 && quoteEndIndex >= 0) {
            // ... } ...
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(moveString.substring(quoteEndIndex + 1));
            moveString = stringBuilder.toString();
        }

        for (char c : moveString.toCharArray()) {
            if (extraCharacters.indexOf(c) == -1) {
                result.append(c);
            }
        }
        return result.toString();
    }

    public void updateToMove() {
        this.setToMove(this.getToMove().getOtherColor());
    }


    // useful but unimportant

    private Player getPlayerToMove() {
        if (this.getToMove() == Color.Black)
            return this.blackPlayer;
        else return this.whitePlayer;
    }

    private Boolean usesNullSquares(Move move) {
        return move.getEndSquare() == null || move.getEndSquare() == null;
    }

    private Boolean moveStartsOnEmptySquare(Move move) {
        Piece piece = this.table.squareToPieceMap.get(move.getStartSquare());
        return piece == Piece.noPiece;
    }

    private Boolean moveMadeByWrongColor(Move move) {
        Piece piece = this.table.squareToPieceMap.get(move.getStartSquare());

        return piece.getColor() != this.table.getToMove();
    }

    private Boolean isEnPassantMove(Move move) {
        Piece activePiece = this.table.squareToPieceMap.get(move.getStartSquare());

        if ((activePiece == Piece.whitePawn || activePiece == Piece.blackPawn)
                && this.table.squareToPieceMap.get(move.getEndSquare()) == Piece.noPiece)
            return true;
        return false;
    }

    private void doMoveAsSimpleMove(Move move) {
        Piece activePiece = this.table.squareToPieceMap.get(move.getStartSquare());
        this.table.squareToPieceMap.put(move.getStartSquare(), Piece.noPiece);
        this.table.squareToPieceMap.put(move.getEndSquare(), activePiece);
    }

    private Boolean handleCastles(Move move) {
        Piece activePiece = this.table.squareToPieceMap.get(move.getStartSquare());
        if (activePiece == whiteKing && move.getStartSquare().getColumn() == 'e' && move.getEndSquare().getColumn() == 'g') {
            this.table.squareToPieceMap.put(Table.getSquare('h', 1), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('f', 1), Piece.whiteRook);
            this.table.setPossibleWhiteShortCastle(false);
            this.table.setPossibleWhiteLongCastle(false);
            doMoveAsSimpleMove(move);
            return true;
        }
        if (activePiece == whiteKing && move.getStartSquare().getColumn() == 'e' && move.getEndSquare().getColumn() == 'c') {
            this.table.squareToPieceMap.put(Table.getSquare('a', 1), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('d', 1), Piece.whiteRook);
            this.table.setPossibleWhiteShortCastle(false);
            this.table.setPossibleWhiteLongCastle(false);
            doMoveAsSimpleMove(move);
            return true;
        }
        if (activePiece == blackKing && move.getStartSquare().getColumn() == 'e' && move.getEndSquare().getColumn() == 'g') {
            this.table.squareToPieceMap.put(Table.getSquare('h', 8), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('f', 8), Piece.blackRook);
            this.table.setPossibleBlackShortCastle(false);
            this.table.setPossibleBlackLongCastle(false);
            doMoveAsSimpleMove(move);
            return true;
        }
        if (activePiece == blackKing && move.getStartSquare().getColumn() == 'e' && move.getEndSquare().getColumn() == 'c') {
            this.table.squareToPieceMap.put(Table.getSquare('a', 8), Piece.noPiece);
            this.table.squareToPieceMap.put(Table.getSquare('d', 8), Piece.blackRook);
            this.table.setPossibleBlackShortCastle(false);
            this.table.setPossibleBlackLongCastle(false);
            doMoveAsSimpleMove(move);
            return true;
        }
        return false;
    }

    private Boolean handlePawnPromotion(Move move) {
        Piece activePiece = this.table.squareToPieceMap.get(move.getStartSquare());

        if (activePiece == Piece.whitePawn && move.getEndSquare().getLine() == 8) {
            this.table.squareToPieceMap.put(move.getStartSquare(), Piece.noPiece);
            this.table.squareToPieceMap.put(move.getEndSquare(), move.getPieceAfterPromotion());
            return true;
        }

        if (activePiece == Piece.blackPawn && move.getEndSquare().getLine() == 1) {
            this.table.squareToPieceMap.put(move.getStartSquare(), Piece.noPiece);
            this.table.squareToPieceMap.put(move.getEndSquare(), move.getPieceAfterPromotion());
            return true;
        }

        return false;
    }

    private Boolean handleEnPassant(Move move) {
        Piece activePiece = this.table.squareToPieceMap.get(move.getStartSquare());

        Square targetSquare = null;
        Boolean enPassant = isEnPassantMove(move);

        if (!enPassant)
            return false;

        if (activePiece == Piece.whitePawn) {
            targetSquare = getSquare(
                    move.getEndSquare().getColumn(),
                    move.getEndSquare().getLine() - 1
            );
        }

        if (activePiece == Piece.blackPawn) {
            targetSquare = getSquare(
                    move.getEndSquare().getColumn(),
                    move.getEndSquare().getLine() + 1
            );
        }

        if (activePiece == Piece.whitePawn
                && this.table.squareToPieceMap.get(targetSquare) == Piece.blackPawn
                && move.getEndSquare().getLine() == 6) {
            this.table.squareToPieceMap.put(targetSquare, Piece.noPiece);
            doMoveAsSimpleMove(move);
            return true;
        }

        if (activePiece == Piece.blackPawn
                && this.table.squareToPieceMap.get(targetSquare) == Piece.whitePawn
                && move.getEndSquare().getLine() == 3) {
            this.table.squareToPieceMap.put(targetSquare, Piece.noPiece);
            doMoveAsSimpleMove(move);
            return true;
        }

        return false;
    }

    private Square getEnPassantTargetSquare(Move move) {
        Piece activePiece = this.table.squareToPieceMap.get(move.getStartSquare());

        if (activePiece == Piece.whitePawn
                && move.getEndSquare().getLine() - move.getStartSquare().getLine() == 2) {
            return getSquare(move.getEndSquare().getColumn(), 3);
        }

        if (activePiece == Piece.blackPawn
                && move.getStartSquare().getLine() - move.getEndSquare().getLine() == 2) {
            return getSquare(move.getEndSquare().getColumn(), 6);
        }

        return null;
    }

    private void updatePositions() {
        positions.add(computeFenFromTable(this.table));
        currentPosition = positions.size() - 1;
    }

    private Boolean isCaptureMove(Move move) {
        return this.table.squareToPieceMap.get(move.getEndSquare()) != Piece.noPiece;
    }

    private Boolean isPawnMove(Move move) {
        Piece piece = this.table.squareToPieceMap.get(move.getStartSquare());
        return piece == Piece.whitePawn || piece == Piece.blackPawn;
    }

    private void updateMetadata(Move move) {
        if (isCaptureMove(move) || isPawnMove(move))
            this.table.setHalfMovesSinceProgress(0);
        else this.table.setHalfMovesSinceProgress(this.table.getHalfMovesSinceProgress() + 1);

        if (this.table.squareToPieceMap.get(move.getStartSquare()).getColor() == Color.Black) {
            this.table.setFullMoveNumber(this.table.getFullMoveNumber() + 1);
        }

        if(this.table.squareToPieceMap.get(move.getStartSquare()) == whiteKing){
            this.table.setPossibleWhiteShortCastle(false);
            this.table.setPossibleWhiteLongCastle(false);
        }

        if(this.table.squareToPieceMap.get(move.getStartSquare()) == blackKing){
            this.table.setPossibleBlackShortCastle(false);
            this.table.setPossibleBlackLongCastle(false);
        }

        Piece movedPiece = this.table.squareToPieceMap.get(move.getStartSquare());

        if(move.getStartSquare() == Square.h1 && movedPiece == whiteRook){
            this.table.setPossibleWhiteShortCastle(false);
        }
        if(move.getStartSquare() == Square.a1 && movedPiece == whiteRook){
            this.table.setPossibleWhiteLongCastle(false);
        }
        if(move.getStartSquare() == Square.e1 && movedPiece == whiteKing){
            this.table.setPossibleWhiteShortCastle(false);
            this.table.setPossibleWhiteLongCastle(false);
        }
        if(move.getStartSquare() == Square.h8 && movedPiece == blackRook){
            this.table.setPossibleBlackShortCastle(false);
        }
        if(move.getStartSquare() == Square.a8 && movedPiece == blackRook){
            this.table.setPossibleBlackLongCastle(false);
        }
        if(move.getStartSquare() == Square.e8 && movedPiece == blackKing){
            this.table.setPossibleBlackShortCastle(false);
        }



        Square enPassantTargetSquare = getEnPassantTargetSquare(move);
        this.table.setEnPassantTargetSquare(enPassantTargetSquare);

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

}
