package DocumentClasses;

import java.util.*;
import java.io.Serializable;
import java.lang.Math;

public abstract class TextVector implements Serializable {

    // stores frequency for each non-noise word
    public HashMap<String, Integer> rawVector;

    public TextVector() {
        rawVector = new HashMap<>();
    }

    // returns the normalized frequency for each word
    public abstract Set<Map.Entry<String,Double>> getNormalizedVectorEntrySet();

    // normalizes the frequency using the TF-IDF formula
    public abstract void normalize(DocumentCollection dc);

    // returns the normalized frequency of a word
    public abstract double getNormalizedFrequency(String word);

    // returns mapping from each word to frequency
    public Set<Map.Entry<String,Integer>> getRawVectorEntrySet() {
        return rawVector.entrySet();
    }

    // adds a word to the rawVector. If the word is not new, frequency is incremented by one
    public void add(String word) {
        // adds word with frequency of 1 or updates word to add 1
        rawVector.merge(word, 1, Integer::sum);
    }

    // returns true if the word is in the rawVector and false otherwise
    public Boolean contains(String word) {
        return rawVector.containsKey(word);
    }

    // returns the frequency of the word
    public int getRawFrequency(String word) {
        return rawVector.getOrDefault(word, 0);
    }

    // returns the total number of non-noise words that are stored for the document
    public int getTotalWordCount() {
        int count = 0;
        for (String word : rawVector.keySet()) {
            count += rawVector.getOrDefault(word, 0);
        }
        return count;
    }

    //  returns the number of distinct words that are stored
    public int getDistinctWordCount() {
        return rawVector.size();
    }

    // returns the highest word frequency
    public int getHighestRawFrequency() {
        int maxFreq = 0;
        for (String word : rawVector.keySet()) {
            if (rawVector.getOrDefault(word, 0) > maxFreq) {
                maxFreq = rawVector.get(word);
            }
        }
        return maxFreq;
    }

    // returns the word with the highest frequency
    public String getMostFrequentWord() {
        String mostFreq = null;
        int maxFreq = 0;
        for (String word : rawVector.keySet()) {
            if (rawVector.get(word) > maxFreq) {
                maxFreq = rawVector.get(word);
                mostFreq = word;
            }
        }
        return mostFreq;
    }

    // returns the square root of the sum of squares in the frequencies
    public double getL2Norm() {
        double sumSquares = 0;
        for (String word : rawVector.keySet()) {
            double normalFreq = getNormalizedFrequency(word);
            sumSquares += normalFreq*normalFreq;
        }
        return Math.sqrt(sumSquares);
    }

    // returns the 20 closes documents
    public ArrayList<Integer> findClosestDocuments(DocumentCollection docs, DocumentDistance distanceAlg) {
        // return if query is empty
        if (this.getTotalWordCount() == 0) {
            return new ArrayList<>();
        }

        // Comparator for smaller to larger (for PQ)
        Comparator<Map.Entry<Integer, Double>> comparator = (a, b) -> {
            int c = Double.compare(a.getValue(), b.getValue());
            return (c != 0) ? c : Integer.compare(a.getKey(), b.getKey()); // break ties with key
        };

        PriorityQueue<Map.Entry<Integer, Double>> pq = new PriorityQueue<>(20, comparator);
        for (Map.Entry<Integer, TextVector> entry : docs.getEntrySet()) {
            int docId = entry.getKey();
            TextVector doc = entry.getValue();

            if (doc.getTotalWordCount() == 0) continue; // skip empty docs
            double similarity = distanceAlg.findDistance(this, doc, docs);

            if (pq.size() < 20) {
                pq.add(new AbstractMap.SimpleEntry<>(docId, similarity));
            } else if (similarity > pq.peek().getValue() ||
                    (similarity == pq.peek().getValue() && docId < pq.peek().getKey())) {
                // replace worst with new worst
                pq.poll();
                pq.add(new AbstractMap.SimpleEntry<>(docId, similarity));
            }
        }

        ArrayList<Map.Entry<Integer, Double>> result = new ArrayList<>(pq);

        // sort descending
        result.sort((a, b) -> {
            int c = Double.compare(b.getValue(), a.getValue());
            return (c != 0) ? c : Integer.compare(a.getKey(), b.getKey());
        });

        ArrayList<Integer> topDocIds = new ArrayList<>(20);
        for (Map.Entry<Integer, Double> entry : result) {
            topDocIds.add(entry.getKey());
        }
        return topDocIds;
    }

    public double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    public int getDocLength() {
        int docLength = 0;
        for (String word : rawVector.keySet()) {
            docLength += rawVector.getOrDefault(word, 0);
        }
        return docLength;
    }
}