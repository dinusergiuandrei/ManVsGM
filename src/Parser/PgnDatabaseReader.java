package Parser;

import ChessLogic.GameDetails;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class PgnDatabaseReader {
    private String dataBasePath;

    public void setSeparators(String[] separators) {
        this.separators = separators;
    }

    static String[] separators = {" 1-0\\n", " 1/2-1/2\\n", " 0-1\\n", "\\n\\n", " \\*\\n"};
          //  " 1-0\n\n", " 1/2-1/2\n", " 0-1\n\n", "\n\n\n", " *\n\n"};

    public Integer totalMoveCount = 0;

    public static String[] getSeparators() {
        return separators;
    }

    public GameDatabase getDatabase() {
        return database;
    }

    private GameDatabase database;

    private static List<String> pgnFilePaths = new LinkedList<>();

    public PgnDatabaseReader(String dataBasePath) {
        this.dataBasePath = dataBasePath;
        this.database = new GameDatabase();
    }

    public void computePgnFilePaths() {
        File curDir = new File(this.dataBasePath);
        getAllFiles(curDir);
    }

    private void getAllFiles(File currentDirectory) {
        if (currentDirectory.isFile()) {
            pgnFilePaths.add(currentDirectory.getPath());
            return;
        }
        File[] filesList = currentDirectory.listFiles();
        if (filesList != null) {
            for (File file : filesList) {
                if (file.isDirectory())
                    getAllFiles(file);
                if (file.isFile()) {
                    pgnFilePaths.add(file.getPath());
                }
            }
        }
    }

    public void parseDatabase(double percent) {
        pgnFilePaths.forEach(
                path -> {
                    if(Math.random() < percent) {
                        PgnFileParser parser = new PgnFileParser();
                        List<GameDetails> gamesDetails = parser.parseGames(path, separators);
                        gamesDetails.forEach(
                                gameDetails -> {
                                    totalMoveCount += gameDetails.whiteMovesString.size();
                                    totalMoveCount += gameDetails.blackMovesString.size();
                                }
                        );
                        System.out.println(path);
                        this.database.addPlayerToGamesPair(path, gamesDetails);
                    }
                }
        );
    }

    /**
     * Call this method only when new PGN files have been added.
     * It will behave as a "clean-up code" command for PGN files.
     */
    public void cleanAllFiles() {
        pgnFilePaths.forEach(
                path -> {
                    PgnFileParser parser = new PgnFileParser();
                    parser.cleanFile(path);
                }
        );
    }

}











