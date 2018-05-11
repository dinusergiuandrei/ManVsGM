package ChessLogic;

import GameArchitecture.Move;
import GameArchitecture.Table;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Database creates a connection between the positions (memorized in FEN format)
 * and the move which was played in the given position.
 */
public class Database implements Serializable {
    private Set<DataSetEntry> data = new LinkedHashSet<>();

    private Map<String, Table> fenToTableMap = new LinkedHashMap<>();

    private Map<Table, String> tableToFenMap = new LinkedHashMap<>();

    private Map<Table, List<Move>> tableToListOfPossibleMovesMap = new LinkedHashMap<>();

    private Map<String, Map<Features, Double>> fenToFeaturesValuesMap = new LinkedHashMap<>();

    private Map<String, List<Move>> fenToPlayedMovesMap = new LinkedHashMap<>();

    public void computeCache() {
        Integer total = this.data.size();
        AtomicReference<Integer> index = new AtomicReference<>(0);
        this.data.forEach(
                dataSetEntry -> {
                    displayDetails(index.get(), total);
                    index.set(index.get() + 1);

                    String fen = dataSetEntry.getPositionFenString();
                    Table table = Table.computeTableFromFen(fen);

                    fenToTableMap.put(fen, table);
                    tableToFenMap.put(table, fen);

                    Map<Features, Double> map = new LinkedHashMap<>();
                    for (Features feature : Features.values()) {
                        map.put(feature, feature.evaluate(table));
                    }
                    this.getFenToFeaturesValuesMap().put(fen, map);

                    Move move = dataSetEntry.getMove();
                    if (fenToPlayedMovesMap.containsKey(fen)) {
                        List<Move> playedMoves = fenToPlayedMovesMap.get(fen);
                        if (playedMoves != null) {
                            playedMoves.add(move);
                        }
                    } else {
                        List<Move> playedMoves = new ArrayList<>();
                        playedMoves.add(move);
                        fenToPlayedMovesMap.put(fen, playedMoves);
                    }
                }
        );
    }

    public static void displayDetails(int index, int total) {
        double over;
        Integer tiles = 50;
        if (index % (total / tiles) == 0) {
            Double tilesD = Double.valueOf(tiles);
            over = tilesD * (index * 1.0 / total * 1.0);
            System.out.print("[");
            for (int j = 0; j < over; ++j)
                System.out.print("-");
            System.out.print(">");
            for (double j = over; j < tiles; ++j)
                System.out.print(".");
            System.out.println("]");
        }
    }

    public Set<DataSetEntry> getData() {
        return data;
    }

    public Map<String, Map<Features, Double>> getFenToFeaturesValuesMap() {
        return fenToFeaturesValuesMap;
    }

    public Map<String, Table> getFenToTableMap() {
        return fenToTableMap;
    }

    public Map<Table, String> getTableToFenMap() {
        return tableToFenMap;
    }

    public static void saveStream(Database database, String filePath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(database);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Database loadStream(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fis;
        if (!Files.exists(Paths.get(filePath))) {
            throw new FileNotFoundException("Could not find file at " + filePath);
        }
        fis = new FileInputStream(filePath);
        ObjectInputStream in = new ObjectInputStream(fis);
        Database database = (Database) in.readObject();
        fis.close();
        return database;
    }

    public Map<Table, List<Move>> getTableToListOfPossibleMovesMap() {
        return tableToListOfPossibleMovesMap;
    }

    public Map<String, List<Move>> getFenToPlayedMovesMap() {
        return fenToPlayedMovesMap;
    }
}
