package Parser;

import ChessLogic.GameDetails;

import java.util.*;
import java.util.regex.Pattern;

public class GameParser {

    Scanner scanner;

    public GameParser() {
    }

    GameDetails parseGame(String gameString) {

        scanner = new Scanner(gameString);

        GameDetails gameDetails = new GameDetails();

        if (!scanner.hasNext())
            return null;
        gameDetails.tags = this.getTags();
        this.getMoves(gameDetails.whiteMovesString, gameDetails.blackMovesString);

        return gameDetails;
    }


    private Map<String, String> getTags() {
        Map<String, String> tags = new LinkedHashMap<>();
        String line;
        while (scanner.hasNext(Pattern.compile("[\\[].*"))) {
            line = scanner.nextLine();
            if (line.equals(""))
                break;

            String tagName = getTagName(line);
            String tagValue = getTagValue(line);
            tags.put(tagName, tagValue);
        }
        return tags;
    }

    private void getMoves(List<String> whiteMoves, List<String> blackMoves) {

        String movesString = getMovesString();

        List<String> movePairsString = getMovePairsString(movesString);

        getPlayerMoves(movePairsString, whiteMoves, blackMoves);
    }

    private String getTagName(String line) {
        return line.substring(line.indexOf('[') + 1, line.indexOf(' '));
    }

    private String getTagValue(String line) {
        if (line.indexOf(']') < 0) {
            System.out.println(line);
        }
        return line.substring(line.indexOf('"') + 1, line.indexOf(']') - 1);
    }

    private void getPlayerMoves(List<String> movePairsString, List<String> whiteMoves, List<String> blackMoves) {
        movePairsString.forEach(
                movesPairString -> {
                    String line = movesPairString.substring(movesPairString.indexOf('.') + 1);
                    line = line.trim();
                    String whiteMoveString;
                    String blackMoveString;

                    if (!line.contains(" ")) {
                        whiteMoveString = line;
                        whiteMoves.add(whiteMoveString);
                    } else {
                        whiteMoveString = line.substring(0, line.indexOf(" "));
                        whiteMoveString = whiteMoveString.trim();

                        blackMoveString = line.substring(line.indexOf(" "));
                        blackMoveString = blackMoveString.trim();

                        whiteMoves.add(whiteMoveString);
                        blackMoves.add(blackMoveString);
                    }
                }
        );
    }

    private String getMovesString() {
        String movesString = "";
        String line;
        while (scanner.hasNext(Pattern.compile("[^\\[].*"))) {
            line = scanner.nextLine();
            if (line.equals("")) {
                if (movesString.length() == 0)
                    continue;
                else break;
            }
            movesString = movesString.concat(line);
            movesString = movesString.concat(" ");
        }
        if (movesString.contains("1/2-1/2")) {
            movesString = movesString.substring(0, movesString.indexOf("1/2-1/2"));
        }
        if (movesString.contains("1-0")) {
            movesString = movesString.substring(0, movesString.indexOf("1-0"));
        }
        if (movesString.contains("0-1")) {
            movesString = movesString.substring(0, movesString.indexOf("0-1"));
        }

        return movesString;
    }

    private List<String> getMovePairsString(String movesString) {
        List<String> movePairsStringList = new LinkedList<>();
        int currentMove = 1;
        String startToken = currentMove + ".";
        String endToken = (currentMove + 1) + ".";
        String movePairString;
        while (movesString.contains(startToken)) {
            if (movesString.contains(endToken)) {
                if (!movesString.contains(startToken) ||
                        !movesString.contains(endToken)) {
                    System.err.println("Token error. Moves string must contain at least one of start token and end token");
                }
                movePairString = movesString.substring(
                        movesString.indexOf(startToken),
                        movesString.indexOf(endToken)
                );
            } else movePairString = movesString.substring(movesString.indexOf(startToken));
            ++currentMove;
            startToken = currentMove + ".";
            endToken = (currentMove + 1) + ".";
            movePairString = movePairString.trim();
            movePairsStringList.add(movePairString);
        }
        return movePairsStringList;
    }



}
