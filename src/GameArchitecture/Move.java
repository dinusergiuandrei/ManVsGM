package GameArchitecture;

public class Move{
    private Square startSquare;

    private Square endSquare;

    public Square getStartSquare() {
        return startSquare;
    }

    public Square getEndSquare() {
        return endSquare;
    }

    public Move(){}

    public Move(Square startSquare, Square endSquare) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
    }

    @Override
    public String toString() {
        return " ( "+startSquare + " - " + endSquare + " ) ";
    }
}