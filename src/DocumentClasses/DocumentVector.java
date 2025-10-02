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

    }

    @Override
    public double getNormalizedFrequency(String word) {
        return normalizedVector.get(word);
    }
}
