package gameArchitecture;

import chessLogic.Game;
import chessLogic.validators.SemanticMoveValidator;

import java.io.Serializable;
import java.util.*;

import static chessLogic.validators.SemanticMoveValidator.isValidMoveSemantically;
import static chessLogic.validators.SyntacticMoveValidator.*;
import static gameArchitecture.Piece.*;
import static gameArchitecture.Square.getSquare;

public class Table implements Serializable {

    private Map<Square, Piece> squareToPieceMap = new LinkedHashMap<>(65);

    private Boolean possibleWhiteShortCastle = true;
    private Boolean possibleWhiteLongCastle = true;
    private Boolean possibleBlackShortCastle = true;
    private Boolean possibleBlackLongCastle = true;
    private Square enPassantTargetSquare = null;

    private Color toMove = Color.White;

    private Integer halfMovesSinceProgress = 0;

    private Integer fullMoveNumber = 0;

    public Game game;

    public Table getCopy() {
        Table newTable = new Table();
        newTable.squareToPieceMap.putAll(this.squareToPieceMap);
        newTable.setPossibleBlackShortCastle(possibleBlackShortCastle);
        newTable.setPossibleBlackLongCastle(possibleBlackLongCastle);
        newTable.setPossibleWhiteShortCastle(possibleWhiteShortCastle);
        newTable.setPossibleWhiteLongCastle(possibleWhiteLongCastle);
        newTable.setEnPassantTargetSquare(enPassantTargetSquare);
        newTable.setToMove(toMove);
        newTable.setHalfMovesSinceProgress(halfMovesSinceProgress);
        newTable.setFullMoveNumber(fullMoveNumber);
        newTable.setGame(game);
        return newTable;
    }

    public void doMove(Move move) {
        this.updateMetadata(move);

        Boolean enPassant = this.handleEnPassant(move);

        Boolean castles = this.handleCastles(move);

        Boolean pawnPromotion = this.handlePawnPromotion(move);

        if (!enPassant && !castles && !pawnPromotion)
            this.doMoveAsSimpleMove(move);

        this.updateToMove();
    }

    public void setUpPieces() {
        squareToPieceMap.put(Square.a1, Piece.whiteRook);
        squareToPieceMap.put(Square.b1, Piece.whiteKnight);
        squareToPieceMap.put(Square.c1, Piece.whiteBishop);
        squareToPieceMap.put(Square.d1, Piece.whiteQueen);
        squareToPieceMap.put(Square.e1, Piece.whiteKing);
        squareToPieceMap.put(Square.f1, Piece.whiteBishop);
        squareToPieceMap.put(Square.g1, Piece.whiteKnight);
        squareToPieceMap.put(Square.h1, Piece.whiteRook);
        squareToPieceMap.put(Square.a2, Piece.whitePawn);
        squareToPieceMap.put(Square.b2, Piece.whitePawn);
        squareToPieceMap.put(Square.c2, Piece.whitePawn);
        squareToPieceMap.put(Square.d2, Piece.whitePawn);
        squareToPieceMap.put(Square.e2, Piece.whitePawn);
        squareToPieceMap.put(Square.f2, Piece.whitePawn);
        squareToPieceMap.put(Square.g2, Piece.whitePawn);
        squareToPieceMap.put(Square.h2, Piece.whitePawn);
        squareToPieceMap.put(Square.a8, Piece.blackRook);
        squareToPieceMap.put(Square.b8, Piece.blackKnight);
        squareToPieceMap.put(Square.c8, Piece.blackBishop);
        squareToPieceMap.put(Square.d8, Piece.blackQueen);
        squareToPieceMap.put(Square.e8, Piece.blackKing);
        squareToPieceMap.put(Square.f8, Piece.blackBishop);
        squareToPieceMap.put(Square.g8, Piece.blackKnight);
        squareToPieceMap.put(Square.h8, Piece.blackRook);
        squareToPieceMap.put(Square.a7, Piece.blackPawn);
        squareToPieceMap.put(Square.b7, Piece.blackPawn);
        squareToPieceMap.put(Square.c7, Piece.blackPawn);
        squareToPieceMap.put(Square.d7, Piece.blackPawn);
        squareToPieceMap.put(Square.e7, Piece.blackPawn);
        squareToPieceMap.put(Square.f7, Piece.blackPawn);
        squareToPieceMap.put(Square.g7, Piece.blackPawn);
        squareToPieceMap.put(Square.h7, Piece.blackPawn);

        for (Square square : Square.values()) {
            if (!squareToPieceMap.containsKey(square)) {
                squareToPieceMap.put(square, Piece.noPiece);
            }
        }
    }

    public static List<Table> computeAllPossibleFollowingPositions(Table table) {
        return null;
    }

    public void displayTable() {
        for (int line = 8; line >= 1; --line) {
            for (int column = 'a'; column <= 'h'; ++column) {
                Square square = getSquare(column, line);
                Piece piece = this.squareToPieceMap.get(square);
                if (piece == Piece.noPiece)
                    System.out.print(" _ ");
                else {
                    System.out.print(((char) piece.getColor().getName().charAt(0)) + piece.getAlias() + " ");
                }
            }
            System.out.println("\n");
        }
    }

    public static Table computeTableFromFen(String fen) {
        Table table = new Table();

        String[] splitResult = fen.split(" ");

        if (splitResult.length != 6) {
            System.err.println("FEN not recognized!");
            return null;
        }

        String piecePlacementString = splitResult[0];
        String activeColourString = splitResult[1];
        String castlingAvailabilityString = splitResult[2];
        String enPassantTargetSquareString = splitResult[3];
        String halfMoveClockString = splitResult[4];
        String fullMoveNumberString = splitResult[5];

        String[] lines = piecePlacementString.split("/");
        for (int line = 8; line >= 1; --line) {
            if (lines[8 - line].length() == 8) {
                for (char column = 'a'; column <= 'h'; ++column) {
                    table.getSquareToPieceMap().put(
                            getSquare(column, line),
                            Piece.getPieceFromFenNotation(lines[8 - line].charAt(column - 'a'))
                    );
                }
            } else {
                int p = 0;
                int column = 0;
                while (p < lines[8 - line].length()) {
                    if (lines[8 - line].charAt(p) >= '1' && lines[8 - line].charAt(p) <= '8') {
                        int l = lines[8 - line].charAt(p) - '0';
                        for (int i = 0; i < l; ++i) {
                            table.getSquareToPieceMap().put(
                                    getSquare(column + 'a', line),
                                    Piece.noPiece
                            );
                            ++column;
                        }
                        ++p;
                    } else {
                        table.getSquareToPieceMap().put(
                                getSquare(column + 'a', line),
                                Piece.getPieceFromFenNotation(lines[8 - line].charAt(p))
                        );
                        ++column;
                        ++p;
                    }
                }
            }

        }

        if (activeColourString.charAt(0) == 'w')
            table.setToMove(Color.White);
        else if (activeColourString.charAt(0) == 'b')
            table.setToMove(Color.Black);
        else {
            System.err.println("Unrecognized color");
            return null;
        }

        table.setPossibleWhiteShortCastle(castlingAvailabilityString.contains("K"));
        table.setPossibleWhiteLongCastle(castlingAvailabilityString.contains("Q"));
        table.setPossibleBlackLongCastle(castlingAvailabilityString.contains("k"));
        table.setPossibleBlackLongCastle(castlingAvailabilityString.contains("q"));

        table.setEnPassantTargetSquare(Square.getSquareByName(enPassantTargetSquareString));

        table.setHalfMovesSinceProgress(Integer.parseInt(halfMoveClockString));

        table.setFullMoveNumber(Integer.parseInt(fullMoveNumberString));

        return table;
    }

    public static String computeFenFromTable(Table table) {
        StringBuilder fen = new StringBuilder();
        for (int line = 8; line >= 1; --line) {
            int length = 0;
            for (char column = 'a'; column <= 'h'; ++column) {
                if (table.getSquareToPieceMap().get(getSquare(column, line)) == Piece.noPiece)
                    ++length;
                else {
                    if (length > 0) {
                        fen.append(String.valueOf(length));
                        length = 0;
                    }
                    try {
                        fen.append(table.getSquareToPieceMap().get(getSquare(column, line)).getFenNotation());
                    } catch (NullPointerException e) {
                        //System.out.println("Null exception 100");
                    }
                }
            }
            if (length > 0) {
                fen.append(String.valueOf(length));
                length = 0;
            }
            if (line > 1)
                fen.append("/");
        }

        if (table.getToMove() == Color.Black) {
            fen.append(" b ");
        } else fen.append(" w ");

        if (table.getPossibleWhiteShortCastle())
            fen.append("K");

        if (table.getPossibleWhiteLongCastle())
            fen.append("Q");


        if (table.getPossibleBlackShortCastle())
            fen.append("k");

        if (table.getPossibleBlackLongCastle())
            fen.append("q");

        if (table.getEnPassantTargetSquare() != null)
            fen.append(" ").append(table.getEnPassantTargetSquare().getName()).append(" ");
        else fen.append(" - ");

        fen.append(String.valueOf(table.getHalfMovesSinceProgress())).append(" ");

        fen.append(String.valueOf(table.getFullMoveNumber()));

        return fen.toString();
    }

    public List<Square> getPositionsOfPiece(Piece piece){
        List<Square> squares = new LinkedList<>();
        this.getSquareToPieceMap().forEach(
                (square, piece1) -> {
                    if(piece == piece1)
                        squares.add(square);
                }
        );
        return squares;
    }

    public Boolean canMove(Move move, Boolean verbose) {
        if (usesNullSquares(move)) {
            if (verbose)
                System.out.println("Using a non valid square.");
            return false;
        }

        if (moveStartsOnEmptySquare(move)) {
            if (verbose)
                System.out.println("Cannot move from a empty square!");
            return false;
        }

        if (moveMadeByWrongColor(move)) {
            if (verbose)
                System.out.println("Wrong color to move!");
            return false;
        }

        if (capturesOwnPieces(move)) {
            if (verbose)
                System.out.println("Can not capture own piece");
            return false;
        }

        return isValidMoveSemantically(this, move);
    }

    public static List<Move> getAllPossibleMoves(Table table, Square square) {
        List<Move> moves = SemanticMoveValidator.getLegalMoves(table, square);
        List<Move> possibleMoves = new ArrayList<>();
        for (Move move : moves) {
            if (move != null)
                if (table.canMove(move, false))
                    possibleMoves.add(move);
        }
        return possibleMoves;
    }

    private Boolean usesNullSquares(Move move) {
        return move.getEndSquare() == null || move.getEndSquare() == null;
    }

    private Boolean capturesOwnPieces(Move move) {
        Color startColor = this.squareToPieceMap.get(move.getStartSquare()).getColor();
        Color endColor = this.squareToPieceMap.get(move.getEndSquare()).getColor();
        return startColor == endColor;
    }

    private Boolean moveStartsOnEmptySquare(Move move) {
        Piece piece = this.squareToPieceMap.get(move.getStartSquare());
        return piece == Piece.noPiece;
    }

    private Boolean moveMadeByWrongColor(Move move) {
        Piece piece = this.squareToPieceMap.get(move.getStartSquare());

        return piece.getColor() != this.getToMove();
    }

    public List<Move> computeAllPossibleMoves() {
        List<Move> possibleMoves = new ArrayList<>();
        for (Square square : Square.values()) {
            try {
                possibleMoves.addAll(Table.getAllPossibleMoves(this, square));
            } catch (Exception e) {

            }
        }
        return possibleMoves;
    }

    public static Table getNewTableAfterMove(Table table, Move move) {
        Table newTable = table.getCopy();
        newTable.doMove(move);
        return newTable;
    }

    public static List<Table> getAllPossibleNextTables(Table table) {
        List<Table> possibleNextTables = new ArrayList<>();
        List<Move> possibleMoves = table.computeAllPossibleMoves();
        for (Move move : possibleMoves) {
            possibleNextTables.add(Table.getNewTableAfterMove(table, move));
        }
        return possibleNextTables;
    }

    public enum Range {
        CLOSE,
        DISTANCE
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int line = 8; line >= 1; --line) {
            for (char column = 'a'; column <= 'h'; ++column) {
                stringBuilder.append(this.squareToPieceMap.get(getSquare(column, line))).append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public Map<Square, Piece> getSquareToPieceMap() {
        return squareToPieceMap;
    }

    public static Square getSquareFromMoveString(String moveString) {
        Square square = null;

        switch (moveString.length()) {
            case 2:
                square = Square.getSquareByName(moveString);
                break;
            case 3:
                square = Square.getSquareByName(moveString.substring(1));
                break;
            case 4:
                square = Square.getSquareByName(moveString.substring(2));
                break;
        }

        if (square != null)
            return square;

        String last = moveString.substring(moveString.length() - 1);
        String types = "QRBN";
        if (types.contains(last)) {
            String squareString = moveString.substring(0, moveString.length() - 1);
            return getSquareFromMoveString(squareString);
        }

        System.out.println("Did not find move");

        return null;
    }

    public Move getMove(String moveString) {

        moveString = cleanMoveString(moveString);

        Move move = getMoveWithoutPawnPromotion(moveString);

        move.setPieceAfterPromotion(getPieceAfterPawnPromotion(moveString, move));

        return move;
    }

    private Piece getPieceAfterPawnPromotion(String moveString, Move move) {
        Piece piece = getPieceFromMoveString(moveString, this.getToMove());
        try {
            if (move.getEndSquare().getLine() == 8 && piece == Piece.whitePawn) {
                char pieceFenNotation = moveString.charAt(moveString.length() - 1);
                return Piece.getPieceFromFenNotation(pieceFenNotation);

            } else if (move.getEndSquare().getLine() == 1 && piece == Piece.blackPawn) {
                char pieceFenNotation = moveString.charAt(moveString.length() - 1);

                return Piece.getPieceFromFenNotation((char) (pieceFenNotation - 'A' + 'a'));
            }
        } catch (NullPointerException e) {
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
            if (this.getToMove() == Color.White) {
                return new Move(getSquare('e', 1), getSquare('g', 1));
            }
            if (this.getToMove() == Color.Black) {
                return new Move(getSquare('e', 8), getSquare('g', 8));
            }
        }

        if (Objects.equals(moveString, "O-O-O")) {
            if (this.getToMove() == Color.White) {
                return new Move(getSquare('e', 1), getSquare('c', 1));
            }
            if (this.getToMove() == Color.Black) {
                return new Move(getSquare('e', 8), getSquare('c', 8));
            }
        }
        return null;
    }

    private Move getMoveFromStringOfLength2(String moveString) {
        // this can only be a pawn move

        Square endSquare = Table.getSquareFromMoveString(moveString);

        List<Move> pawnPushMoves = getAllPawnPushMoves(endSquare, this.getToMove());

        List<Move> legalMoves = new LinkedList<>();

        for (Move move : pawnPushMoves) {
            if (this.squareToPieceMap.get(move.getStartSquare()) == Piece.whitePawn
                    || this.squareToPieceMap.get(move.getStartSquare()) == Piece.blackPawn)
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
            this.displayTable();
        }

        return null;
    }

    private Move getMoveFromStringOfLength3(String moveString) {
        Piece piece = getPieceFromMoveString(moveString, this.getToMove());
        Square endSquare = Table.getSquareFromMoveString(moveString);
        List<Move> baseMoves = null;
        if (piece != null) {
            switch (piece) {
                case whiteKing:
                case blackKing:
                    baseMoves = getAllKingMoves(endSquare, this.getToMove());
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
                    baseMoves = getAllPawnCaptureMoves(endSquare, this.getToMove());
                    List<Move> pawnMoves = getAllPawnPushMoves(endSquare, this.getToMove());
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
                    baseMoves = getAllPawnCaptureMoves(endSquare, this.getToMove());
                    List<Move> pawnSquares1 = getAllPawnPushMoves(endSquare, this.getToMove());
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
                if (this.squareToPieceMap.get(baseMove.getStartSquare()) == piece) {
                    legalMoves.add(baseMove);
                }
            }
        }

        if (legalMoves.size() > 1 && (piece == Piece.whitePawn || piece == Piece.blackPawn)) {
            char detail = moveString.charAt(0);
            if (baseMoves != null) {
                for (Move legalMove : legalMoves) {
                    if (this.squareToPieceMap.get(legalMove.getStartSquare()) == piece
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

    private Move getMoveFromStringOfLength4(String moveString) {
        Piece piece = getPieceFromMoveString(moveString, this.getToMove());
        Square endSquare = Table.getSquareFromMoveString(moveString);
        List<Move> baseMoves = null;
        if (piece != null) {
            switch (piece) {
                case whiteKing:
                case blackKing:
                    baseMoves = getAllKingMoves(endSquare, this.getToMove());
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
                    baseMoves = getAllPawnCaptureMoves(endSquare, this.getToMove());
                    break;
            }
        }

        List<Move> legalMoves = new LinkedList<>();

        if (baseMoves != null) {
            for (Move baseMove : baseMoves) {
                if (this.squareToPieceMap.get(baseMove.getStartSquare()) == piece) {
                    legalMoves.add(baseMove);
                }
            }
        }

        char detail = moveString.charAt(1);
        if (detail >= '1' && detail <= '8') {
            if (baseMoves != null) {
                for (Move legalMove : legalMoves) {
                    if (this.squareToPieceMap.get(legalMove.getStartSquare()) == piece
                            && legalMove.getStartSquare().getLine() == detail - '0') {
                        return legalMove;
                    }
                }
            }
        }

        if (detail >= 'a' && detail <= 'h') {
            if (baseMoves != null) {
                for (Move legalMove : legalMoves) {
                    if (this.squareToPieceMap.get(legalMove.getStartSquare()) == piece
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

    public Player getPlayerToMove() {
        if (this.getToMove() == Color.Black)
            return this.game.getBlackPlayer();
        else return this.game.getWhitePlayer();
    }

    private Boolean isEnPassantMove(Move move) {
        Piece activePiece = this.squareToPieceMap.get(move.getStartSquare());

        if ((activePiece == Piece.whitePawn || activePiece == Piece.blackPawn)
                && this.squareToPieceMap.get(move.getEndSquare()) == Piece.noPiece)
            return true;
        return false;
    }

    public void doMoveAsSimpleMove(Move move) {
        Piece activePiece = this.squareToPieceMap.get(move.getStartSquare());
        this.squareToPieceMap.put(move.getStartSquare(), Piece.noPiece);
        this.squareToPieceMap.put(move.getEndSquare(), activePiece);
    }

    public Boolean handleCastles(Move move) {
        Piece activePiece = this.squareToPieceMap.get(move.getStartSquare());
        if (activePiece == whiteKing && move.getStartSquare().getColumn() == 'e' && move.getEndSquare().getColumn() == 'g') {
            this.squareToPieceMap.put(getSquare('h', 1), Piece.noPiece);
            this.squareToPieceMap.put(getSquare('f', 1), Piece.whiteRook);
            this.setPossibleWhiteShortCastle(false);
            this.setPossibleWhiteLongCastle(false);
            doMoveAsSimpleMove(move);
            return true;
        }
        if (activePiece == whiteKing && move.getStartSquare().getColumn() == 'e' && move.getEndSquare().getColumn() == 'c') {
            this.squareToPieceMap.put(getSquare('a', 1), Piece.noPiece);
            this.squareToPieceMap.put(getSquare('d', 1), Piece.whiteRook);
            this.setPossibleWhiteShortCastle(false);
            this.setPossibleWhiteLongCastle(false);
            doMoveAsSimpleMove(move);
            return true;
        }
        if (activePiece == blackKing && move.getStartSquare().getColumn() == 'e' && move.getEndSquare().getColumn() == 'g') {
            this.squareToPieceMap.put(getSquare('h', 8), Piece.noPiece);
            this.squareToPieceMap.put(getSquare('f', 8), Piece.blackRook);
            this.setPossibleBlackShortCastle(false);
            this.setPossibleBlackLongCastle(false);
            doMoveAsSimpleMove(move);
            return true;
        }
        if (activePiece == blackKing && move.getStartSquare().getColumn() == 'e' && move.getEndSquare().getColumn() == 'c') {
            this.squareToPieceMap.put(getSquare('a', 8), Piece.noPiece);
            this.squareToPieceMap.put(getSquare('d', 8), Piece.blackRook);
            this.setPossibleBlackShortCastle(false);
            this.setPossibleBlackLongCastle(false);
            doMoveAsSimpleMove(move);
            return true;
        }
        return false;
    }

    public Boolean handlePawnPromotion(Move move) {
        Piece activePiece = this.squareToPieceMap.get(move.getStartSquare());

        if (activePiece == Piece.whitePawn && move.getEndSquare().getLine() == 8) {
            this.squareToPieceMap.put(move.getStartSquare(), Piece.noPiece);
            this.squareToPieceMap.put(move.getEndSquare(), move.getPieceAfterPromotion());
            return true;
        }

        if (activePiece == Piece.blackPawn && move.getEndSquare().getLine() == 1) {
            this.squareToPieceMap.put(move.getStartSquare(), Piece.noPiece);
            this.squareToPieceMap.put(move.getEndSquare(), move.getPieceAfterPromotion());
            return true;
        }

        return false;
    }

    public Boolean handleEnPassant(Move move) {
        Piece activePiece = this.squareToPieceMap.get(move.getStartSquare());

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
                && this.squareToPieceMap.get(targetSquare) == Piece.blackPawn
                && move.getEndSquare().getLine() == 6) {
            this.squareToPieceMap.put(targetSquare, Piece.noPiece);
            doMoveAsSimpleMove(move);
            return true;
        }

        if (activePiece == Piece.blackPawn
                && this.squareToPieceMap.get(targetSquare) == Piece.whitePawn
                && move.getEndSquare().getLine() == 3) {
            this.squareToPieceMap.put(targetSquare, Piece.noPiece);
            doMoveAsSimpleMove(move);
            return true;
        }

        return false;
    }

    private Square getEnPassantTargetSquare(Move move) {
        Piece activePiece = this.squareToPieceMap.get(move.getStartSquare());

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
        game.getPositions().add(computeFenFromTable(this));
        game.setCurrentPosition(game.getPositions().size() - 1);
    }

    private Boolean isCaptureMove(Move move) {
        return this.squareToPieceMap.get(move.getEndSquare()) != Piece.noPiece;
    }

    private Boolean isPawnMove(Move move) {
        Piece piece = this.squareToPieceMap.get(move.getStartSquare());
        return piece == Piece.whitePawn || piece == Piece.blackPawn;
    }

    public void updateMetadata(Move move) {
        if (isCaptureMove(move) || isPawnMove(move))
            this.setHalfMovesSinceProgress(0);
        else this.setHalfMovesSinceProgress(this.getHalfMovesSinceProgress() + 1);

        if (this.squareToPieceMap.get(move.getStartSquare()).getColor() == Color.Black) {
            this.setFullMoveNumber(this.getFullMoveNumber() + 1);
        }

        if (this.squareToPieceMap.get(move.getStartSquare()) == whiteKing) {
            this.setPossibleWhiteShortCastle(false);
            this.setPossibleWhiteLongCastle(false);
        }

        if (this.squareToPieceMap.get(move.getStartSquare()) == blackKing) {
            this.setPossibleBlackShortCastle(false);
            this.setPossibleBlackLongCastle(false);
        }

        Piece movedPiece = this.squareToPieceMap.get(move.getStartSquare());

        if (move.getStartSquare() == Square.h1 && movedPiece == whiteRook) {
            this.setPossibleWhiteShortCastle(false);
        }
        if (move.getStartSquare() == Square.a1 && movedPiece == whiteRook) {
            this.setPossibleWhiteLongCastle(false);
        }
        if (move.getStartSquare() == Square.e1 && movedPiece == whiteKing) {
            this.setPossibleWhiteShortCastle(false);
            this.setPossibleWhiteLongCastle(false);
        }
        if (move.getStartSquare() == Square.h8 && movedPiece == blackRook) {
            this.setPossibleBlackShortCastle(false);
        }
        if (move.getStartSquare() == Square.a8 && movedPiece == blackRook) {
            this.setPossibleBlackLongCastle(false);
        }
        if (move.getStartSquare() == Square.e8 && movedPiece == blackKing) {
            this.setPossibleBlackShortCastle(false);
        }

        Square enPassantTargetSquare = getEnPassantTargetSquare(move);
        this.setEnPassantTargetSquare(enPassantTargetSquare);
    }

    // getters and setters
    public Boolean getPossibleWhiteShortCastle() {
        return possibleWhiteShortCastle;
    }

    public void setPossibleWhiteShortCastle(Boolean possibleWhiteShortCastle) {
        this.possibleWhiteShortCastle = possibleWhiteShortCastle;
    }

    public Boolean getPossibleWhiteLongCastle() {
        return possibleWhiteLongCastle;
    }

    public void setPossibleWhiteLongCastle(Boolean possibleWhiteLongCastle) {
        this.possibleWhiteLongCastle = possibleWhiteLongCastle;
    }

    public Boolean getPossibleBlackShortCastle() {
        return possibleBlackShortCastle;
    }

    public void setPossibleBlackShortCastle(Boolean possibleBlackShortCastle) {
        this.possibleBlackShortCastle = possibleBlackShortCastle;
    }

    public Boolean getPossibleBlackLongCastle() {
        return possibleBlackLongCastle;
    }

    public void setPossibleBlackLongCastle(Boolean possibleBlackLongCastle) {
        this.possibleBlackLongCastle = possibleBlackLongCastle;
    }

    public Square getEnPassantTargetSquare() {
        return enPassantTargetSquare;
    }

    public void setEnPassantTargetSquare(Square enPassantTargetSquare) {
        this.enPassantTargetSquare = enPassantTargetSquare;
    }

    public Color getToMove() {
        return toMove;
    }

    public void setToMove(Color toMove) {
        this.toMove = toMove;
    }

    public Integer getHalfMovesSinceProgress() {
        return halfMovesSinceProgress;
    }

    public void setHalfMovesSinceProgress(Integer halfMovesSinceProgress) {
        this.halfMovesSinceProgress = halfMovesSinceProgress;
    }

    public Integer getFullMoveNumber() {
        return fullMoveNumber;
    }

    public void setFullMoveNumber(Integer fullMoveNumber) {
        this.fullMoveNumber = fullMoveNumber;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        String myFen = Table.computeFenFromTable(this);
        String otherFen = Table.computeFenFromTable(table);
        return Objects.equals(myFen, otherFen);
    }

    @Override
    public int hashCode() {
        String myFen = Table.computeFenFromTable(this);
        return Objects.hash(myFen);
    }


}