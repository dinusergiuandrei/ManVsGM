package Parser;

import GameArchitecture.GameDetails;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PgnFileParser {

    PgnFileParser() {
    }

    public String getFileString(String filePath){
        Scanner scanner = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fileReader != null) {
            scanner = new Scanner(fileReader);
        }

        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()){
            stringBuilder.append(scanner.nextLine()).append("\n");
        }
        return stringBuilder.toString();
    }

    public List<GameDetails> parseGames(String filePath, String[] separatorsBetweenGames) {
        List<GameDetails> gamesDetails = new LinkedList<>();
        String fileString = this.getFileString(filePath);

        String[] gamesStrings = this.getGamesString(fileString, separatorsBetweenGames);

        GameParser gameParser = new GameParser();

        for (String gameString : gamesStrings) {
            gamesDetails.add(gameParser.parseGame(gameString));
        }
        return gamesDetails;
    }

    /**
     * Use this only when adding new PGNs.
     * "It will behave as a "clean-up code" command for the given PGN file.
     */
    public void cleanFile(String filePath){
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

    public String[] getGamesString(String fileString, String[] separators){
        StringBuilder regExp = new StringBuilder();
        regExp.append("\"");
        for (int i = 0; i < separators.length; i++) {
            regExp.append("(").append(separators[i]).append(")");
            if(i<separators.length-1)
                regExp.append("|");
        }
        regExp.append("\"");
        return fileString.split(regExp.toString());
    }

}
