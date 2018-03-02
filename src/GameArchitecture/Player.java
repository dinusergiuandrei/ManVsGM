package GameArchitecture;

import MoveGenerator.MoveGenerator;

import java.util.Scanner;

public class Player {

    private int id;

    private String familyName;

    private String givenName;

    private MoveGenerator moveGenerator;

    public void setMoveGenerator(MoveGenerator moveGenerator) {
        this.moveGenerator = moveGenerator;
    }

    public int getId() {
        return id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    private Scanner scanner;

    public Player(int id, String familyName, String givenName, MoveGenerator moveGenerator) {
        this.id = id;
        this.familyName = familyName;
        this.givenName = givenName;
        this.moveGenerator = moveGenerator;
        scanner = new Scanner(System.in);
    }

    public void move(Game game) {
        Boolean madeMove = false;
        while (!madeMove) {
            Move move = moveGenerator.getMove();
            if (move != null) {
                if (game.canMove(move.startSquare, move.endSquare)) {
                    game.doMove(move.startSquare, move.endSquare);
                    game.table.displayTable();
                    madeMove = true;
                } else {
                    System.out.println("Could not move " + move.startSquare.toString() + "-" + move.endSquare.toString());
                }
            }
            else{
                System.out.println("I did not understand the move!");
            }
        }
    }

}
