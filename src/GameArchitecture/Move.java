package GameArchitecture;

public class Move{
    Table.Square startSquare;
    Table.Square endSquare;

    public Move(){}

    public Move(Table.Square startSquare, Table.Square endSquare) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
    }
}