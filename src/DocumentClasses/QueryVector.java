package DocumentClasses;

import java.util.*;

public class QueryVector extends TextVector{

    private HashMap<String,Double> normalizedVector = new HashMap<>();

    @Override
    public Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet() {
        return normalizedVector.entrySet();
    }

    // normalizes one query vector
    @Override
    public void normalize(DocumentCollection dc) {
        int maxF = getHighestRawFrequency();
        if (maxF == 0) return;
        int m = dc.getSize(); // total number of docs
        for (String word : rawVector.keySet()) {
            int f = getRawFrequency(word);
            int df = dc.getDocumentFrequency(word);
            double normalizedValue;
            if (df == 0) {
                normalizedValue = 0.0;
            } else {
                normalizedValue = (0.5 + (0.5 * ((double) f / maxF))) * log2((double) m / df);
            }
            normalizedVector.put(word, normalizedValue);
        }
    }

    @Override
    public double getNormalizedFrequency(String word) {
        return normalizedVector.getOrDefault(word, 0.0);
    }
}
