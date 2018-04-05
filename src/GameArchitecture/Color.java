package GameArchitecture;

public enum Color {
    White("White"),
    Black("Black");

    private String name;

    Color(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public Color getOtherColor() {
        if(this == White)
            return Black;
        else return White;
    }
}
