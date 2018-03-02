package Parser;

import GameArchitecture.GameDetails;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Pattern;

public class PgnFileParser {
    private String filePath;

    private Scanner scanner;

    PgnFileParser(String filePath) {
        this.filePath = filePath;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fileReader != null) {
            this.scanner = new Scanner(fileReader);
        }
    }

    public List<GameDetails> parseGames() {
        List<GameDetails> gamesDetails = new LinkedList<>();
        GameDetails gameDetails = parseGame();
        while (gameDetails != null) {
            gamesDetails.add(gameDetails);
            gameDetails = parseGame();
        }
        return gamesDetails;
    }

    private GameDetails parseGame() {
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
                    System.out.println("Hol up");
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


    /**
     * Use this only when adding new PGNs.
     * "It will behave as a "clean-up code" command for the given PGN file.
     */
    public void cleanFile(){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = null;
        if (fileReader != null) {
            scanner = new Scanner(fileReader);
        }
        String fileContent = "";
        String line;
        if (scanner != null) {
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                if (!line.equals("")) {
                    fileContent = fileContent.concat(line);
                    fileContent = fileContent.concat("\n");
                }
            }
        }

        fileContent = fileContent.trim();

        try {
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            writer.print(fileContent);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
