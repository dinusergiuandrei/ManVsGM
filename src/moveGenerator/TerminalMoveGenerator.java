package moveGenerator;

import chessLogic.Game;
import gameArchitecture.Move;
import gameArchitecture.Square;
import gameArchitecture.Table;

import java.util.Scanner;

import static gameArchitecture.Square.getSquare;

public class TerminalMoveGenerator implements MoveGenerator {

    private Scanner scanner;

    private Game game;

    public TerminalMoveGenerator(Game game) {
        this.scanner = new Scanner(System.in);
        this.game = game;
    }

    @Override
    public Move getMove(Table table) {
        Move move = null;
        try {
            move = getSimpleMoveFromTerminal(game);
        } catch (Exception e) {
            System.out.println("Could not determine a move! ( " + e.getMessage() + " ) ");
        }
        return move;
    }

    public Move getDetailedMoveFromTerminal(Game game) {
        System.out.println("First square: ");
        String startSquareString = scanner.nextLine();

        System.out.println("Second square: ");
        String endSquareString = scanner.nextLine();

        Square startSquare = getSquare(startSquareString.charAt(0), startSquareString.charAt(1) - '0');
        Square endSquare = getSquare(endSquareString.charAt(0), endSquareString.charAt(1) - '0');

        return new Move(startSquare, endSquare);
    }

    private Move getSimpleMoveFromTerminal(Game game) throws Exception {
        System.out.print("Simple move : ");
        return game.getTable().getMove(scanner.nextLine());
    }

}
