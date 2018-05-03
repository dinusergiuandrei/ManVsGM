package ChessLogic;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * DataSet creates a connection between the positions (memorized in FEN format)
 * and the move which was played in the given position.
 */
public class DataSet {
    private Set<DataSetEntry> data = new LinkedHashSet<>();

    private Map<String, Map<Features, Double>> fenToFeaturesValuesMap = new LinkedHashMap<>();

    public Set<DataSetEntry> getData() {
        return data;
    }

    public Map<String, Map<Features, Double>> getFenToFeaturesValuesMap() {
        return fenToFeaturesValuesMap;
    }

}
