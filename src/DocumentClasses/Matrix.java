package DocumentClasses;

import java.util.*;
import java.io.*;

public class Matrix {
    // a variable that is of type a two-dimensional array
    private int[][] matrix;

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
    }

    // returns the number of rows in which the element at position attribute (a number between 0 and 4) is equal to value.
    private int findFrequency(int attr, int value, ArrayList<Integer> rows) {
        return 0;
    }

    // returns a HashSet of the different values for the specified attribute
    private HashSet<Integer> findDifferentValues(int attr, ArrayList<Integer> rows) {
        return null;
    }

    //  Returns an ArrayList of the rows where the value for the attribute is equal to value
    private ArrayList<Integer> findRows(int attr, int value, ArrayList<Integer> rows) {
        return null;
    }

    // returns log2 of input
    private double log2(double number) {
        return Math.log(number) / Math.log(2);
    }

    // finds the entropy of the dataset that consists of the specified rows after it is partitioned on the attribute
    private double findEntropy(ArrayList<Integer> rows) {
        return 0.0;
    }

    //finds the entropy of the dataset that consists of the specified rows after it is partitioned on the attribute
    private double findEntropy(int attribute, ArrayList<Integer> rows) {
        return 0.0;
    }

    // finds the information gain of partitioning on the attribute. Considers only the specified rows.
    private double findGain(int attribute, ArrayList<Integer> rows)  {
        return 0.0;
    }

    // returns the Information Gain Ratio, where we only look at the data defined by the set of rows and we consider splitting on attribute.
    public double computeIGR(int attribute, ArrayList<Integer> rows) {
        return 0.0;
    }

    // returns the most common category for the dataset that is the defined by the specified rows.
    public int findMostCommonValue(ArrayList<Integer> rows) {
        return 0;
    }

    // Splits the dataset that is defined by rows on the attribute. Each element of the HashMap that is returned contains the value for the attribute and an ArrayList of rows that have this value.
    public HashMap<Integer, ArrayList<Integer>> split(int attribute, ArrayList<Integer> rows) {
        return null;
    }



}