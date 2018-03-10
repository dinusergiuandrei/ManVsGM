package MoveGenerator;

import ChessLogic.Game;
import GameArchitecture.Move;
import GameArchitecture.Square;
import GameArchitecture.Table;

import java.util.Scanner;

public class TerminalMoveGenerator implements MoveGenerator {

    private Scanner scanner;

    private Game game;

    public TerminalMoveGenerator(Game game) {
        this.scanner = new Scanner(System.in);
        this.game = game;
    }

    @Override
    public Move getMove() {
        return getSimpleMoveFromTerminal(game);
    }

    public Move getDetailedMoveFromTerminal(Game game) {
        System.out.println("First square: ");
        String startSquareString = scanner.nextLine();

        System.out.println("Second square: ");
        String endSquareString = scanner.nextLine();

        Square startSquare = Table.getSquare(startSquareString.charAt(0), startSquareString.charAt(1) - '0');
        Square endSquare = Table.getSquare(endSquareString.charAt(0), endSquareString.charAt(1) - '0');

        return new Move(startSquare, endSquare);
    }

    private Move getSimpleMoveFromTerminal(Game game) {
        System.out.print("Simple move : ");
        return game.getMove(scanner.nextLine());
    }

}
