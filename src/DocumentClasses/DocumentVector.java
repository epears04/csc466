package DocumentClasses;

import java.util.*;

public class DocumentVector extends TextVector{
    private HashMap<String, Double> normalizedVector = new HashMap<String, Double>();

    @Override
    public Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet() {
        return normalizedVector.entrySet();
    }

    @Override
    public void normalize(DocumentCollection dc) {
        int maxF = getHighestRawFrequency();
        int m = dc.getSize();
        for (String word : rawVector.keySet()) {
            int f = getRawFrequency(word);
            int df = dc.getDocumentFrequency(word);
            double normalizedValue = ((double) f / maxF) * log2((double) m / df);
            normalizedVector.put(word, normalizedValue);
        }
    }

    @Override
    public double getNormalizedFrequency(String word) {
        return normalizedVector.get(word);
    }
}
