package DocumentClasses;

import java.util.Map;

public class CosineDistance implements DocumentDistance {

    @Override
    public double findDistance(TextVector query, TextVector doc, DocumentCollection docs) {
        double dotProduct = 0;
        for (Map.Entry<String, Double> entry : query.getNormalizedVectorEntrySet()) {
            Double value = entry.getValue();
            if (value != 0.0) {
                double docValue = doc.getNormalizedFrequency(entry.getKey());
                dotProduct += value * docValue;
            }
        }
        double queryLen = query.getL2Norm();
        double docLen = doc.getL2Norm();
        if (queryLen == 0.0 || docLen == 0.0 || dotProduct == 0.0) {
            return 0.0;
        }
        return dotProduct / (queryLen * docLen);
    }
}
