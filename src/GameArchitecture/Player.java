package GameArchitecture;

import java.util.Scanner;

public class Player {

    int id;

    String familyName;

    String givenName;

    boolean isAI;

    Scanner scanner;

    public Player(int id, String familyName, String givenName, boolean isAI){
        this.id = id;
        this.familyName = familyName;
        this.givenName = givenName;
        this.isAI = isAI;
        scanner = new Scanner(System.in);
    }

    public void move(Game game){
        Boolean madeMove = false;
        while(!madeMove) {
            System.out.println("First square: ");
            String startSquareString = scanner.nextLine();

            System.out.println("Second square: ");
            String endSquareString = scanner.nextLine();

            Table.Square startSquare = game.table.getSquare(startSquareString.charAt(0), startSquareString.charAt(1) - '0');
            Table.Square endSquare = game.table.getSquare(endSquareString.charAt(0), endSquareString.charAt(1) - '0');

            if (game.canMove(startSquare, endSquare)) {
                game.doMove(startSquare, endSquare);
                game.table.displayTable();
                madeMove = true;
            } else {
                System.out.println("Could not move " + startSquareString + "-" + endSquareString);
            }
        }
    }

}
