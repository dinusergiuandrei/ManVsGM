package GameArchitecture;

import java.util.Scanner;

public class Player {

    private int id;

    private String familyName;

    private String givenName;

    private boolean isAI;

    private Scanner scanner;

    public Player(int id, String familyName, String givenName, boolean isAI) {
        this.id = id;
        this.familyName = familyName;
        this.givenName = givenName;
        this.isAI = isAI;
        scanner = new Scanner(System.in);
    }

    public void move(Game game) {
        Boolean madeMove = false;
        while (!madeMove) {
            Move move = getSimpleMoveFromTerminal(game);
            if (game.canMove(move.startSquare, move.endSquare)) {
                game.doMove(move.startSquare, move.endSquare);
                game.table.displayTable();
                madeMove = true;
            } else {
                System.out.println("Could not move " + move.startSquare.toString() + "-" + move.endSquare.toString());
            }
        }
    }

    public Move getDetailedMoveFromTerminal(Game game) {
        System.out.println("First square: ");
        String startSquareString = scanner.nextLine();

        System.out.println("Second square: ");
        String endSquareString = scanner.nextLine();

        Table.Square startSquare = Table.getSquare(startSquareString.charAt(0), startSquareString.charAt(1) - '0');
        Table.Square endSquare = Table.getSquare(endSquareString.charAt(0), endSquareString.charAt(1) - '0');

        return new Move(startSquare, endSquare);
    }

    private Move getSimpleMoveFromTerminal(Game game){
        System.out.print("Simple move : ");
        return game.getMove(scanner.nextLine());
    }

}
