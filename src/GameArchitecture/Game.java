package GameArchitecture;

public class Game {

    public Table table;

    Player whitePlayer;

    Player blackPlayer;

    public Boolean isOver;

    Color toMove;

    public Game(Player whitePlayer, Player blackPlayer){
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public void setUp(){
        this.isOver = false;
        toMove = Color.White;
        table = new Table();
        table.setUpPieces();
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

    public Piece doMove(Table.Square startSquare, Table.Square endSquare){
        Piece piece1 = this.table.squareToPieceMap.get(startSquare);
        Piece piece2 = this.table.squareToPieceMap.get(endSquare);

        this.table.squareToPieceMap.put(startSquare, Piece.noPiece);
        this.table.squareToPieceMap.put(endSquare, piece1);

        if(piece1 == Piece.whiteKing && startSquare.column=='e' && endSquare.column=='g'){
            this.table.squareToPieceMap.put(this.table.getSquare('h', 1), Piece.noPiece);
            this.table.squareToPieceMap.put(this.table.getSquare('f', 1), Piece.whiteRook);
        }
        if(piece1 == Piece.whiteKing && startSquare.column=='e' && endSquare.column=='c'){
            this.table.squareToPieceMap.put(this.table.getSquare('a', 1), Piece.noPiece);
            this.table.squareToPieceMap.put(this.table.getSquare('d', 1), Piece.whiteRook);
        }
        if(piece1 == Piece.blackKing && startSquare.column=='e' && endSquare.column=='g'){
            this.table.squareToPieceMap.put(this.table.getSquare('h', 8), Piece.noPiece);
            this.table.squareToPieceMap.put(this.table.getSquare('f', 8), Piece.blackRook);
        }
        if(piece1 == Piece.blackKing && startSquare.column=='e' && endSquare.column=='c'){
            this.table.squareToPieceMap.put(this.table.getSquare('a', 8), Piece.noPiece);
            this.table.squareToPieceMap.put(this.table.getSquare('d', 8), Piece.blackRook);
        }

        if(piece1 == Piece.whitePawn && endSquare.line == 8)
            this.table.squareToPieceMap.put(endSquare, Piece.whiteQueen);

        if(piece1 == Piece.blackPawn && endSquare.line == 1)
            this.table.squareToPieceMap.put(endSquare, Piece.blackQueen);

        if(piece1 == Piece.whitePawn && this.table.squareToPieceMap.get(endSquare) == Piece.noPiece) {
            this.table.squareToPieceMap.put(this.table.getSquare(endSquare.column, 5), Piece.noPiece);
        }

        if(piece1 == Piece.blackPawn && this.table.squareToPieceMap.get(endSquare) == Piece.noPiece) {
            this.table.squareToPieceMap.put(this.table.getSquare(endSquare.column, 4), Piece.noPiece);
        }

        this.table.updatePossibleMoves(startSquare, endSquare);

        return piece2;
    }

    public void updateToMove(){
        if(this.toMove==Color.White)
            this.toMove=Color.Black;
        else if(this.toMove==Color.Black)
                this.toMove=Color.White;
    }

}
