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
        return rawVector.get(word);
    }

    // returns the total number of non-noise words that are stored for the document
    public int getTotalWordCount() {
        int count = 0;
        for (String word : rawVector.keySet()) {
            count += rawVector.get(word);
        }
        return count;
    }

    //  returns the number of distinct words that are stored
    public int getDistinctWordCount() {
        // TODO: how is distinct word count different than vector size? is it?
        return rawVector.size();
    }

    // returns the highest word frequency
    public int getHighestRawFrequency() {
        int maxFreq = 0;
        for (String word : rawVector.keySet()) {
            if (rawVector.get(word) > maxFreq) {
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
            double normalFreq =getNormalizedFrequency(word);
            sumSquares += normalFreq*normalFreq;
        }
        return Math.sqrt(sumSquares);
    }

    // returns the 20 closes documents
    public ArrayList<Integer> findClosestDocuments(DocumentCollection docs) {
        // TODO: implement
        return null;
    }
}
