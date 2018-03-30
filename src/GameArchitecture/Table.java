package GameArchitecture;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Table {
    public Map<Square, Piece> squareToPieceMap = new LinkedHashMap<>(65);

    public Boolean possibleWhiteShortCastle = true;
    public Boolean possibleWhiteLongCastle = true;
    public Boolean possibleBlackShortCastle = true;
    public Boolean possibleBlackLongCastle = true;

    Square enPassantTargetSquare = null;

    private Color toMove = Color.White;

    private Integer halfMovesSinceProgress = 0;

    private Integer fullMoveNumber = 1;

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
            System.out.println();
        }
    }

    public void updatePossibleMoves(Square startSquare, Square endSquare) {
        Piece piece = this.squareToPieceMap.get(startSquare);

        if (piece == Piece.whiteKing) {
            possibleWhiteShortCastle = false;
            possibleWhiteLongCastle = false;
        }
        if (piece == Piece.blackKing) {
            possibleBlackShortCastle = false;
            possibleBlackLongCastle = false;
        }
        if (piece == Piece.whiteRook && startSquare.getLine() == 1 && startSquare.getColumn() == 'h') {
            possibleWhiteShortCastle = false;
        }
        if (piece == Piece.whiteRook && startSquare.getLine() == 1 && startSquare.getColumn() == 'a') {
            possibleWhiteLongCastle = false;
        }
        if (piece == Piece.blackRook && startSquare.getLine() == 8 && startSquare.getColumn() == 'h') {
            possibleBlackShortCastle = false;
        }
        if (piece == Piece.blackRook && startSquare.getLine() == 8 && startSquare.getColumn() == 'a') {
            possibleBlackLongCastle = false;
        }
    }

    public static Table computeTableFromFEN(String fen){
        Table table = new Table();

        String[] splitResult = fen.split(" ");

        if(splitResult.length!=6){
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
        for(int line = 8; line>=1; --line){
            if(lines[8-line].length() == 8){
                for(char column = 'a'; column<='h'; ++column){
                    table.getSquareToPieceMap().put(
                            getSquare(column, line),
                            Piece.getPieceFromFenNotation(lines[8-line].charAt(column-'a'))
                    );
                }
            }
            else{
                int p = 0;
                int column = 0;
                while(p<lines[8-line].length()){
                    if(lines[8-line].charAt(p) >= '1' && lines[8-line].charAt(p) <= '8'){
                        int l = lines[8-line].charAt(p) - '0';
                        for(int i=0; i<l; ++i){
                            table.getSquareToPieceMap().put(
                                    getSquare(column+'a', line),
                                    Piece.noPiece
                                    );
                            ++column;
                        }
                        ++p;
                    }
                    else {
                        table.getSquareToPieceMap().put(
                                getSquare(column+'a', line),
                                Piece.getPieceFromFenNotation(lines[8-line].charAt(p))
                        );
                        ++column;
                        ++p;
                    }
                }
            }

        }

        if(activeColourString.charAt(0) == 'w')
            table.setToMove(Color.White);
        else if(activeColourString.charAt(0) == 'b')
            table.setToMove(Color.Black);
            else {
            System.err.println("Unrecognized color");
            return null;
        }

        table.setPossibleWhiteShortCastle(castlingAvailabilityString.contains("K"));
        table.setPossibleWhiteLongCastle(castlingAvailabilityString.contains("Q"));
        table.setPossibleBlackLongCastle(castlingAvailabilityString.contains("k"));
        table.setPossibleBlackLongCastle(castlingAvailabilityString.contains("q"));

        table.setEnPassantTargetSquare(getSquareByName(enPassantTargetSquareString));

        table.setHalfMovesSinceProgress(Integer.parseInt(halfMoveClockString));

        table.setFullMoveNumber(Integer.parseInt(fullMoveNumberString));


        return table;
    }

    public static String computeFENFromTable(Table table){
        StringBuilder fen = new StringBuilder();
        for(int line = 8; line>=1; --line){
            int length = 0;
            for(char column = 'a'; column<='h'; ++column){
                if(table.getSquareToPieceMap().get(getSquare(column, line)) == Piece.noPiece)
                    ++length;
                else {
                    if(length>0){
                        fen.append(String.valueOf(length));
                        length = 0;
                    }
                    fen.append(table.getSquareToPieceMap().get(getSquare(column, line)).getFenNotation());
                }
            }
            if(length>0){
                fen.append(String.valueOf(length));
                length = 0;
            }
            if(line > 1)
                fen.append("/");
        }

        if(table.getToMove()==Color.Black){
            fen.append(" b ");
        }
        else fen.append(" w ");

        if(table.getPossibleWhiteShortCastle())
            fen.append("K");

        if(table.getPossibleWhiteLongCastle())
            fen.append("Q");


        if(table.getPossibleBlackShortCastle())
            fen.append("k");

        if(table.getPossibleBlackLongCastle())
            fen.append("q");

        if(table.getEnPassantTargetSquare() != null)
            fen.append(" ").append(table.getEnPassantTargetSquare().getName()).append(" ");
        else fen.append(" - ");

        fen.append(String.valueOf(table.getHalfMovesSinceProgress())).append(" ");

        fen.append(String.valueOf(table.getFullMoveNumber()));

        return fen.toString();
    }


    public Table getCopy(){
        Table newTable = new Table();

        newTable.setPossibleBlackLongCastle(this.possibleBlackLongCastle);
        newTable.setPossibleBlackShortCastle(this.possibleBlackShortCastle);
        newTable.setPossibleWhiteLongCastle(this.possibleWhiteLongCastle);
        newTable.setPossibleWhiteShortCastle(this.possibleWhiteShortCastle);

        newTable.getSquareToPieceMap().putAll(this.squareToPieceMap);

        return newTable;
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

    public static Square getSquare(int column, int line) {
        if (column < 'a' || column > 'h' || line < 1 || line > 8) {
            return null;
        }
        for (Square square : Square.values()) {
            if (square.getLine() == line && square.getColumn() == column)
                return square;
        }
        return null;
    }

    public static Square getSquareFromMoveString(String moveString){
        Square square = null;

        switch (moveString.length()){
            case 2: square = getSquareByName(moveString); break;
            case 3: square = getSquareByName(moveString.substring(1)); break;
            case 4: square = getSquareByName(moveString.substring(2)); break;
        }

        if(square!=null)
            return square;

        String last = moveString.substring(moveString.length()-1);
        String types = "QRBN";
        if(types.contains(last)){
            String squareString = moveString.substring(0, moveString.length()-1);
            return getSquareFromMoveString(squareString);
        }

        System.out.println("Did not find move");

        return null;
    }

    public static Square getSquareByName(String squareName) {
        for (Square square : Square.values()) {
            if (Objects.equals(squareName, square.getName()))
                return square;
        }


        return null;
    }

    public void setSquareToPieceMap(Map<Square, Piece> squareToPieceMap) {
        this.squareToPieceMap = squareToPieceMap;
    }

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

}
