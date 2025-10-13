package DocumentClasses;

import java.util.*;
import java.lang.Math;

public class OkapiDistance implements DocumentDistance{
    @Override
    public double findDistance(TextVector query, TextVector doc, DocumentCollection docs) {
        final double k1 = 1.2;
        final double b = 0.75;
        final double k2 = 100.0;

        double okapi = 0.0;
        int N = docs.getSize();
        double avdl = docs.getAverageDocumentLength();
        if (avdl == 0.0) {
            return 0.0;
        }
        for (Map.Entry<String, Integer> entry : query.getRawVectorEntrySet()) {
            String word = entry.getKey();
            int dfi = docs.getDocumentFrequency(word);
            int fij = doc.getRawFrequency(word);
            int dl = doc.getDocLength();
            int fiq = query.getRawFrequency(word);
            if (fij == 0 || fiq == 0) {
                continue; // skip if no words match
            }
            double idf = Math.log((N-dfi + 0.5) / (dfi + 0.5));
            double tfDenominator = (k1 * (1 - b +( b * (dl / avdl))) )+ fij;
            double tfDoc = ((k1 + 1) * fij) / tfDenominator;
            double tfQuery = ((k2 + 1) * fiq) / (k2 + fiq);
            okapi += idf * tfDoc * tfQuery;
        }
        return okapi;
    }
}
