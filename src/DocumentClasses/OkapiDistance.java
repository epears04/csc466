package DocumentClasses;

import java.util.*;
import java.lang.Math;

public class OkapiDistance implements DocumentDistance{
    @Override
    public double findDistance(TextVector query, TextVector doc, DocumentCollection docs) {
        double okapi = 0.0;
        int N = docs.getSize();
        int avdl = docs.getAverageDocumentLength();
        if (avdl == 0) {
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
            double t1 = Math.log((N-dfi + 0.5) / (dfi + 0.5));
            double t2 = ((1.2 + 1) * fij) / (1.2 * (1 - 0.75 + 0.75 * ((double) dl / avdl) + fij));
            double t3 = (double) ((100 + 1) * fiq) / 100 + fiq;
            okapi += t1 * t2 * t3;
        }
        return okapi;
    }
}
