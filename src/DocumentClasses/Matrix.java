package DocumentClasses;

import java.util.*;
import java.io.*;

public class Matrix {
    // a variable that is of type a two-dimensional array
    private int[][] matrix;
    private double lambda;

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
        this.lambda = 1.0 / matrix.length;
    }

    // returns the number of rows in which the element at position attribute (a number between 0 and 4) is equal to value.
    private int findFrequency(int attr, int value, ArrayList<Integer> rows) {
        int freq = 0;
        for (Integer row : rows) {
            if (matrix[row][attr] == value) {
                freq++;
            }
        }
        return freq;
    }

    // returns a HashSet of the different values for the specified attribute
    private HashSet<Integer> findDifferentValues(int attr, ArrayList<Integer> rows) {
        HashSet<Integer> differentValues = new HashSet<>();
        for (Integer row : rows) {
            differentValues.add(matrix[row][attr]);
        }
        return differentValues;
    }

    //  Returns an ArrayList of the rows where the value for the attribute is equal to value
    private ArrayList<Integer> findRows(int attr, int value, ArrayList<Integer> rows) {
        ArrayList<Integer> foundRows = new ArrayList<>();
        for (Integer row : rows) {
            if (matrix[row][attr] == value) {
                foundRows.add(row);
            }
        }
        return foundRows;
    }

    // returns log2 of input
    private double log2(double number) {
        return Math.log(number) / Math.log(2);
    }

    // finds the entropy of the dataset that consists of the specified rows
    private double findEntropy(ArrayList<Integer> rows) {
        double entropy = 0.0;
        // index 0 = class 1, index 1 = class 2, index 2 = class 3
        int[] counts = new int[3];
        for (int row : rows) {
            int classification = matrix[row][matrix[0].length - 1];
            if (classification >= 1 && classification <= 3) {
                counts[classification - 1]++;
            }
        }
        if (!rows.isEmpty()) {
            for (int integer : counts) {
                double prob = (double) integer / rows.size();
                if (prob != 0) {
                    entropy -= prob * log2(prob);
                }
            }
        }
        return entropy;
    }

    // weighted average entropy of the specified rows after it is partitioned on the attribute
    private double findEntropy(int attribute, ArrayList<Integer> rows) {
        if (rows.isEmpty()) {
            return 0.0;
        }
        double weightedEntropy = 0.0;
        HashSet<Integer> values = findDifferentValues(attribute, rows);
        for (int value : values) {
            ArrayList<Integer> matchingRows = findRows(attribute, value, rows);
            double entropy = findEntropy(matchingRows);
            weightedEntropy += ((double) matchingRows.size() / rows.size()) * entropy;
        }
        return weightedEntropy;
    }

    // finds the information gain of partitioning on the attribute. Considers only the specified rows.
    private double findGain(int attribute, ArrayList<Integer> rows)  {
        return findEntropy(rows) -  findEntropy(attribute, rows);
    }

    // returns the Information Gain Ratio, where we only look at the data defined by the set of rows and we consider splitting on attribute.
    public double computeIGR(int attribute, ArrayList<Integer> rows) {
        if (rows.isEmpty()) {
            return 0.0;
        }
        double gain = findGain(attribute, rows);
        double denominator = 0.0;
        HashSet<Integer> values = findDifferentValues(attribute, rows);
        for (int value : values) {
            double prob = (double) findFrequency(attribute, value, rows) / rows.size();
            if (prob != 0) {
                denominator -= prob * log2(prob);
            }
        }
        return gain / denominator;
    }

    // returns the most common category for the dataset that is the defined by the specified rows.
    public int findMostCommonValue(ArrayList<Integer> rows) {
        int maxCount = -1;
        int mostCommonClass = -1;
        HashSet<Integer> classes = findDifferentValues(matrix[0].length-1, rows);
        for (int c : classes) {
            int freq = findFrequency(matrix[0].length - 1, c, rows);
            if (freq > maxCount) {
                maxCount = freq;
                mostCommonClass = c;
            }
        }
        return mostCommonClass;
    }

    // Splits the dataset that is defined by rows on the attribute.
    // Each element of the HashMap that is returned contains the value for the attribute and an ArrayList of rows that have this value.
    public HashMap<Integer, ArrayList<Integer>> split(int attribute, ArrayList<Integer> rows) {
        HashMap<Integer, ArrayList<Integer>> result = new HashMap<>();
        HashSet<Integer> values = findDifferentValues(attribute, rows);
        for (int value : values) {
            ArrayList<Integer> matchingRows = findRows(attribute, value, rows);
            result.put(value, matchingRows);
        }
        return result;
    }

    public boolean meetsThreshold(double threshold, ArrayList<Integer> rows) {
        return findEntropy(rows) <= threshold;
    }


    // LAB 8 FUNCTIONS

    // return all indices of all rows
    public ArrayList<Integer> findAllRows() {
        int numRows = matrix.length;
        ArrayList<Integer> rows = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            rows.add(i);
        }
        return rows;
    }

    // returns the index of the category attribute
    public int getCategoryAttribute() {
        return matrix[0].length - 1;
    }

    // takes as input the values for a single row and the category
    // returns the probability that the row belongs to the category using the Naïve Bayesian model
    // Pr (C=category | Features = row
    public double findProb(int[] row, int category) {
        ArrayList<Integer> rows = findAllRows();
        int categoryIndex = getCategoryAttribute();

        int n = rows.size();
        int nj = findFrequency(categoryIndex, category, rows);
        double prob = (double) nj / n; // start with probability of class

        for (int i = 0; i < row.length; i++) {
            int nij = findFrequency(i, row[i], findRows(categoryIndex, category, rows));
            int mi = findDifferentValues(i, rows).size();
            prob *= ((nij + lambda) / (nj + (lambda * mi)));
        }
        return prob;
    }

    // takes as input the values for a single row
    // returns the most probably category using the Naïve Bayesian model
    public int findCategory(int[] row) {
        HashSet<Integer> categories = findDifferentValues(getCategoryAttribute(), findAllRows());
        double maxProb = 0.0;
        int maxCategory = -1;
        for (int category : categories) {
            double prob = findProb(row, category);
            if (prob > maxProb) {
                maxProb = prob;
                maxCategory = category;
            }
        }
        return maxCategory;
    }
}