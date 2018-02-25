import GameArchitecture.Game;
import GameArchitecture.Player;
import Parser.GameDetails;
import Parser.PgnParser;

import java.io.*;
import java.util.*;

public class Main {

    private static Integer gamesCount = 0;

    private static List<String> pgnFilePaths = new LinkedList<>();

    private static Map<String, List<GameDetails>> playerToGamesMap = new LinkedHashMap<>();

    static String dataBasePath = "database/players";

    public static void main(String[] args) {
        //Long startTime = System.currentTimeMillis();
        startGame();
        //computePgnFilePaths(dataBasePath);
        //parseDatabase();
        //System.out.println(gamesCount + " total games in database");
        //System.out.println("Run time = " + ((System.currentTimeMillis()-startTime)/1000) + " seconds");
    }

    public static void computePgnFilePaths(String path) {
        File curDir = new File(path);
        getAllFiles(curDir);
    }

    private static void getAllFiles(File currentDirectory) {
        if(currentDirectory.isFile()){
            //System.out.println(file.getName());
            pgnFilePaths.add(currentDirectory.getPath());
            return;
        }
        File[] filesList = currentDirectory.listFiles();
        for(File file : filesList){
            if(file.isDirectory())
                getAllFiles(file);
            if(file.isFile()){
                //System.out.println(file.getName());
                pgnFilePaths.add(file.getPath());
            }
        }
    }

    public static void parseDatabase(){
        pgnFilePaths.forEach(
                path -> {
                    try {
                        //cleanFile(path);

                        PgnParser pgnParser = new PgnParser(path);
                        List<GameDetails> gamesDetails = pgnParser.parseGames();
                        playerToGamesMap.put(path, gamesDetails);

                        System.out.println(path + " " + gamesDetails.size()+ " games ");
                        gamesCount += gamesDetails.size();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public static void cleanFile(String path) throws FileNotFoundException {
        FileReader fileReader = new FileReader(path);
        Scanner scanner = new Scanner(fileReader);
        String fileContent = "";
        String line;
        while(scanner.hasNext()){
            line=scanner.nextLine();
            if(!line.equals("")) {
                fileContent = fileContent.concat(line);
                fileContent = fileContent.concat("\n");
            }
        }

        fileContent = fileContent.trim();

        try {
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            writer.print(fileContent);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static void startGame(){
        int playerId = 0;
        Player whitePlayer = new Player(++playerId, "White", "Player", false);
        Player blackPlayer = new Player(++playerId, "Black", "Player", true);

        Game game = new Game(whitePlayer, blackPlayer);
        game.setUp();

        game.start();
    }

}
