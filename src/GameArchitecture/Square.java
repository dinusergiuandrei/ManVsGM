package GameArchitecture;

public enum Square {

    a8("a8", 'a', 8), b8("b8", 'b', 8), c8("c8", 'c', 8), d8("d8", 'd', 8), e8("e8", 'e', 8), f8("f8", 'f', 8), g8("g8", 'g', 8), h8("h8", 'h', 8),
    a7("a7", 'a', 7), b7("b7", 'b', 7), c7("c7", 'c', 7), d7("d7", 'd', 7), e7("e7", 'e', 7), f7("f7", 'f', 7), g7("g7", 'g', 7), h7("h7", 'h', 7),
    a6("a6", 'a', 6), b6("b6", 'b', 6), c6("c6", 'c', 6), d6("d6", 'd', 6), e6("e6", 'e', 6), f6("f6", 'f', 6), g6("g6", 'g', 6), h6("h6", 'h', 6),
    a5("a5", 'a', 5), b5("b5", 'b', 5), c5("c5", 'c', 5), d5("d5", 'd', 5), e5("e5", 'e', 5), f5("f5", 'f', 5), g5("g5", 'g', 5), h5("h5", 'h', 5),
    a4("a4", 'a', 4), b4("b4", 'b', 4), c4("c4", 'c', 4), d4("d4", 'd', 4), e4("e4", 'e', 4), f4("f4", 'f', 4), g4("g4", 'g', 4), h4("h4", 'h', 4),
    a3("a3", 'a', 3), b3("b3", 'b', 3), c3("c3", 'c', 3), d3("d3", 'd', 3), e3("e3", 'e', 3), f3("f3", 'f', 3), g3("g3", 'g', 3), h3("h3", 'h', 3),
    a2("a2", 'a', 2), b2("b2", 'b', 2), c2("c2", 'c', 2), d2("d2", 'd', 2), e2("e2", 'e', 2), f2("f2", 'f', 2), g2("g2", 'g', 2), h2("h2", 'h', 2),
    a1("a1", 'a', 1), b1("b1", 'b', 1), c1("c1", 'c', 1), d1("d1", 'd', 1), e1("e1", 'e', 1), f1("f1", 'f', 1), g1("g1", 'g', 1), h1("h1", 'h', 1);

    private String name;

    private int column;

    public String getName() {
        return name;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    private int line;

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