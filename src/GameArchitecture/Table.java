package GameArchitecture;

import java.util.*;

public class Table {

    public Map<Square, Piece> squareToPieceMap = new LinkedHashMap<>(64);

    public Boolean possibleWhiteShortCastle = true;
    public Boolean possibleWhiteLongCastle  = true;
    public Boolean possibleBlackShortCastle = true;
    public Boolean possibleBlackLongCastle  = true;

    public void setUpPieces(){
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
            if(!squareToPieceMap.containsKey(square)){
                squareToPieceMap.put(square, Piece.noPiece);
            }
        }
    }

    public Square getSquare(int column, int line){
        if(column<'a'||column>'h'||line<1||line>8){
            //System.out.println((char) column + line + " is not a valid square");
            return null;
        }
        for(Square square : Square.values()){
            if(square.line==line && square.column==column)
                return square;
        }
        return null;
    }

    public Boolean isValidPosition(Color toMove){
        Color otherPlayer;
        if(toMove==Color.White) otherPlayer = Color.Black;
        else if(toMove==Color.Black) otherPlayer = Color.White;

        return true;
    }

    public Integer getNumberOfAttacks(){
        return 0;
    }

    public List<Square> getMoves(Square startSquare){
        Piece piece = this.squareToPieceMap.get(startSquare);
        List<Square> moves = null;
        switch (piece){
            case whiteKing:   case blackKing:   moves = getKingMoves   (startSquare);   break;
            case whiteQueen:  case blackQueen:  moves = getQueenMoves  (startSquare);   break;
            case whiteRook:   case blackRook:   moves = getRookMoves   (startSquare);   break;
            case whiteBishop: case blackBishop: moves = getBishopMoves (startSquare);   break;
            case whiteKnight: case blackKnight: moves = getKnightMoves (startSquare);   break;
            case whitePawn:   case blackPawn:   moves = getPawnMoves   (startSquare);   break;
        }
        return moves;
    }

    public List<Square> getKingMoves(Square startSquare){
        Color color = squareToPieceMap.get(startSquare).color;
        List<Square> possibleMoves = new LinkedList<>();
        int[] lineDifference   = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnDifference = {-1, 0, 1, 1, 1, 0, -1, -1};

        possibleMoves.addAll(getMoves(startSquare, columnDifference, lineDifference, Range.CLOSE));

        if(color == Color.White){
            if(possibleWhiteShortCastle)
                possibleMoves.add(getSquare('g', 1));
            if(possibleWhiteLongCastle)
                possibleMoves.add(getSquare('c', 1));
        }
        if(color == Color.Black){
            if(possibleBlackShortCastle)
                possibleMoves.add(getSquare('g', 8));
            if(possibleBlackLongCastle)
                possibleMoves.add(getSquare('c', 8));
        }

        return possibleMoves;
    }

    public List<Square> getQueenMoves(Square startSquare){
        Color color = squareToPieceMap.get(startSquare).color;
        List<Square> possibleSquares = new LinkedList<>();
        int[] lineDifference   = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] columnDifference = {-1, 0, 1, 1, 1, 0, -1, -1};

        return getMoves(startSquare, columnDifference, lineDifference, Range.DISTANCE);
    }

    public List<Square> getRookMoves(Square startSquare){
        Color color = squareToPieceMap.get(startSquare).color;
        List<Square> possibleSquares = new LinkedList<>();
        int[] lineDifference   = {1, -1, 0, 0};
        int[] columnDifference = {0, 0, 1, -1};

        return getMoves(startSquare, columnDifference, lineDifference, Range.DISTANCE);
    }

    public List<Square> getBishopMoves(Square startSquare){
        Color color = squareToPieceMap.get(startSquare).color;
        List<Square> possibleSquares = new LinkedList<>();
        int[] lineDifference   = {1, -1, 1, -1};
        int[] columnDifference = {1, 1, -1, -1};

        return getMoves(startSquare, columnDifference, lineDifference, Range.DISTANCE);
    }

    public List<Square> getKnightMoves(Square startSquare){
        int[] lineDifference   = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] columnDifference = {1, 2, 2, 1, -1, -2, -2, -1};

        return getMoves(startSquare, columnDifference, lineDifference, Range.CLOSE);
    }

    public List<Square> getPawnMoves(Square startSquare) {
        Color color = squareToPieceMap.get(startSquare).color;
        List<Square> possibleSquares = new LinkedList<>();

        int direction = 0;
        if (color == Color.White)
            direction = 1;
        else if (color == Color.Black)
            direction = -1;

        //1 square push
        Square endSquare = getSquare(startSquare.column, startSquare.line + direction);
        if (endSquare != null && this.squareToPieceMap.get(endSquare) == Piece.noPiece)
            possibleSquares.add(endSquare);

        //2 square push
        Square endSquare2 = getSquare(startSquare.column, startSquare.line + 2 * direction);
        if (endSquare2 != null
                && this.squareToPieceMap.get(endSquare) == Piece.noPiece
                && this.squareToPieceMap.get(endSquare2) == Piece.noPiece
                && ( ( startSquare.line == 2 && direction == 1 ) || (startSquare.line == 7 && direction == -1) )
                )
            possibleSquares.add(endSquare2);

        //simple right capture
        Square endSquare3 = getSquare(startSquare.column + 1, startSquare.line + direction);
        if (endSquare3 != null
                && this.squareToPieceMap.get(endSquare3) != Piece.noPiece
                && this.squareToPieceMap.get(endSquare3).color != color
                )
            possibleSquares.add(endSquare3);

        //simple left capture
        Square endSquare4 = getSquare(startSquare.column - 1, startSquare.line + direction);
        if (endSquare4 != null
                && this.squareToPieceMap.get(endSquare4) != Piece.noPiece
                && this.squareToPieceMap.get(endSquare4).color != color
                )
            possibleSquares.add(endSquare4);

        //right en-passant

        int endLine, endColumn;
        int targetLine, targetColumn;

        targetLine = startSquare.line;
        targetColumn = startSquare.column + 1;

        Square endSquare5 = getSquare(startSquare.column + 1, startSquare.line + direction);
        Square targetSquare = getSquare(targetColumn, targetLine);
        if (endSquare5 != null
                && this.squareToPieceMap.get(endSquare5) == Piece.noPiece
                && ( this.squareToPieceMap.get(targetSquare) == Piece.whitePawn || this.squareToPieceMap.get(targetSquare) == Piece.blackPawn )
                && ((targetLine == 4 && this.squareToPieceMap.get(targetSquare).color == Color.White) || (targetLine == 5 && this.squareToPieceMap.get(targetSquare).color == Color.Black) )
                && this.squareToPieceMap.get(targetSquare).color != color
                )
            possibleSquares.add(endSquare5);


        //left en-passant
        targetLine = startSquare.line;
        targetColumn = startSquare.column - 1;
        Square endSquare6 = getSquare(startSquare.column + 1, startSquare.line + direction);
        targetSquare = getSquare(targetColumn, targetLine);
        if (endSquare6 != null
                && this.squareToPieceMap.get(endSquare6) == Piece.noPiece
                && ( this.squareToPieceMap.get(targetSquare) == Piece.whitePawn || this.squareToPieceMap.get(targetSquare) == Piece.blackPawn )
                && ((targetLine == 4 && this.squareToPieceMap.get(targetSquare).color == Color.White) || (targetLine == 5 && this.squareToPieceMap.get(targetSquare).color == Color.Black) )
                && this.squareToPieceMap.get(targetSquare).color != color
                )
            possibleSquares.add(endSquare6);

        return possibleSquares;
    }

    public List<Square> getMoves(Square startSquare, int columnDifference[], int lineDifference[], Range range){
        Color color = squareToPieceMap.get(startSquare).color;
        List<Square> candidateMoves = new LinkedList<>();
        if(range == Range.CLOSE){
            for(int i=0; i<columnDifference.length; ++i)
                for(int j=0; j<lineDifference.length; ++j){
                    int column = startSquare.column + columnDifference[i];
                    int line = startSquare.line + lineDifference[j];
                    Square square = getSquare(column, line);
                    if(square == null)
                        continue;
                    candidateMoves.add(square);
                }
        }
        if(range == Range.DISTANCE){
            for(int i=0; i<columnDifference.length; ++i)
                for(int j=0; j<lineDifference.length; ++j){
                    for(int distance=1; ; ++distance) {
                        int column = startSquare.column + distance*columnDifference[i];
                        int line = startSquare.line + distance*lineDifference[j];
                        Square square = getSquare(column, line);
                        if (square == null)
                            break;
                        Piece piece = this.squareToPieceMap.get(square);
                        if(piece.color == color)
                            break;
                        candidateMoves.add(square);
                    }
                }
        }
        return candidateMoves;
    }

    public void displayTable(){
        for(int line=8; line>=1; --line){
            for(int column='a'; column<='h'; ++column){
                Square square = getSquare(column, line);
                Piece piece = this.squareToPieceMap.get(square);
                if(piece == Piece.noPiece)
                    System.out.print(" _ ");
                else {
                    System.out.print(((char) piece.color.name.charAt(0)) + piece.alias + " " );
                }
            }
            System.out.println();
        }
    }

    public Boolean isEmpty(Square square){
        return this.squareToPieceMap.get(square)==Piece.noPiece;
    }

    public Boolean isSquare(int column, int line){
        return !(getSquare(column, line)==null);
    }

    public void updatePossibleMoves(Table.Square startSquare, Table.Square endSquare){
        Piece piece = this.squareToPieceMap.get(startSquare);

        if(piece == Piece.whiteKing){
            possibleWhiteShortCastle = false;
            possibleWhiteLongCastle = false;
        }
        if(piece == Piece.blackKing){
            possibleBlackShortCastle = false;
            possibleBlackLongCastle = false;
        }
        if(piece == Piece.whiteRook && startSquare.line == 1 && startSquare.column == 'h'){
            possibleWhiteShortCastle = false;
        }
        if(piece == Piece.whiteRook && startSquare.line == 1 && startSquare.column == 'a'){
            possibleWhiteLongCastle = false;
        }
        if(piece == Piece.blackRook && startSquare.line == 8 && startSquare.column == 'h'){
            possibleBlackShortCastle = false;
        }
        if(piece == Piece.blackRook && startSquare.line == 8 && startSquare.column == 'a'){
            possibleBlackLongCastle = false;
        }
    }

    public enum Range{
        CLOSE,
        DISTANCE
    }

    public enum Square{

        a8("a8", 'a', 8), b8("b8", 'b', 8), c8("c8", 'c', 8), d8("d8", 'd', 8), e8("e8", 'e', 8), f8("f8", 'f', 8), g8("g8", 'g', 8), h8("h8", 'h', 8),
        a7("a7", 'a', 7), b7("b7", 'b', 7), c7("c7", 'c', 7), d7("d7", 'd', 7), e7("e7", 'e', 7), f7("f7", 'f', 7), g7("g7", 'g', 7), h7("h7", 'h', 7),
        a6("a6", 'a', 6), b6("b6", 'b', 6), c6("c6", 'c', 6), d6("d6", 'd', 6), e6("e6", 'e', 6), f6("f6", 'f', 6), g6("g6", 'g', 6), h6("h6", 'h', 6),
        a5("a5", 'a', 5), b5("b5", 'b', 5), c5("c5", 'c', 5), d5("d5", 'd', 5), e5("e5", 'e', 5), f5("f5", 'f', 5), g5("g5", 'g', 5), h5("h5", 'h', 5),
        a4("a4", 'a', 4), b4("b4", 'b', 4), c4("c4", 'c', 4), d4("d4", 'd', 4), e4("e4", 'e', 4), f4("f4", 'f', 4), g4("g4", 'g', 4), h4("h4", 'h', 4),
        a3("a3", 'a', 3), b3("b3", 'b', 3), c3("c3", 'c', 3), d3("d3", 'd', 3), e3("e3", 'e', 3), f3("f3", 'f', 3), g3("g3", 'g', 3), h3("h3", 'h', 3),
        a2("a2", 'a', 2), b2("b2", 'b', 2), c2("c2", 'c', 2), d2("d2", 'd', 2), e2("e2", 'e', 2), f2("f2", 'f', 2), g2("g2", 'g', 2), h2("h2", 'h', 2),
        a1("a1", 'a', 1), b1("b1", 'b', 1), c1("c1", 'c', 1), d1("d1", 'd', 1), e1("e1", 'e', 1), f1("f1", 'f', 1), g1("g1", 'g', 1), h1("h1", 'h', 1);

        String name;

        int column;

        int line;

        Square(String name, char column, int line){
            this.name=name;
            this.column=column;
            this.line=line;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
