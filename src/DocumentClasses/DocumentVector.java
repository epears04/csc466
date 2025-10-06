package DocumentClasses;

import java.util.*;

public class DocumentVector extends TextVector{
    private HashMap<String, Double> normalizedVector = new HashMap<>();

    @Override
    public Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet() {
        return normalizedVector.entrySet();
    }

    @Override
    public void normalize(DocumentCollection dc) {
        int maxF = getHighestRawFrequency();
        if (maxF == 0) return;
        int m = dc.getSize();
        for (String word : rawVector.keySet()) {
            int f = getRawFrequency(word);
            int df = dc.getDocumentFrequency(word);
            double normalizedValue;
            if (df == 0) {
                normalizedValue = 0.0;
            }  else {
                normalizedValue = ((double) f / maxF) * log2((double) m / df);
            }
            normalizedVector.put(word, normalizedValue);
        }
    }

    @Override
    public double getNormalizedFrequency(String word) {
        return normalizedVector.getOrDefault(word, 0.0);
    }
}
