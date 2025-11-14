package labs;

import DocumentClasses.Matrix;
import java.io.*;
import java.util.*;

public class Lab7 {

    private static final double THRESHOLD = 0.01;
    // stores [data][attribute]
    private static int[][] data;
    private static Matrix matrix;
    public static void main(String[] args) {
        data = process("./files/data.txt");
        matrix = new Matrix(data);

        ArrayList<Integer> attributes = new ArrayList<>(List.of(0, 1, 2, 3));
        ArrayList<Integer> rows = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            rows.add(i);
        }

        printDecisionTree(data, attributes, rows, 0, 100);
    }

    public static int[][] process(String filename) {
        File file = new File(filename);
        // 150 = number of records, 5 = number of features
        int[][] matrix = new int[150][5];

        int row = 0;
        try(Scanner reader = new Scanner(file)) {
            while(reader.hasNextLine()) {
                String  line = reader.nextLine();
                String[] tokens = line.split(",");
                matrix[row][4] = Integer.parseInt(tokens[4]);
                for(int i = 0; i < 4; i++) {
                    int num = (int) Double.parseDouble(tokens[i]);
                    matrix[row][i] = num;
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File " + filename + " not found.");
        }
        return matrix;
    }

    // prints tabs according to number of levels
    private static void printIndents(int level) {
        for(int i = 0; i < level; i++) {
            System.out.print("\t");
        }
    }

    //recursive method that prints the decision tree.
    // It takes as input the data, the set of attributes that have not been used so far in this branch of the tree, the set of rows to examine, the current level, and the information gain ratio from last iteration
    public static void printDecisionTree(int[][] data, ArrayList<Integer> attributes, ArrayList<Integer> rows, int level, double currentIGR) {
        if (rows.isEmpty() || matrix.meetsThreshold(THRESHOLD, rows)) {
            int value = matrix.findMostCommonValue(rows);
            printIndents(level);
            System.out.println("value = " + value);
            return;
        }

        int bestAttr = -1;
        double bestIGR = Double.NEGATIVE_INFINITY;
        for (int attr : attributes) {
            double igr = matrix.computeIGR(attr, rows);
            if (igr > bestIGR) {
                bestIGR = igr;
                bestAttr = attr;
            }
        }

        // no split
        if (bestAttr == -1 || bestIGR <= THRESHOLD) {
            int value = matrix.findMostCommonValue(rows);
            printIndents(level);
            System.out.println("value = " + value);
            return;
        }

        // update possible attributes
        ArrayList<Integer> updatedAttributes = new ArrayList<>(attributes);
        updatedAttributes.remove(Integer.valueOf(bestAttr));

        HashMap<Integer, ArrayList<Integer>> attributeMap = matrix.split(bestAttr, rows);
        ArrayList<Integer> values = new ArrayList<>(attributeMap.keySet());
        for (int v : values) {
            ArrayList<Integer> childRows = attributeMap.get(v);
            printIndents(level);
            System.out.println("When attribute " +(bestAttr + 1)+ " has value " + v);
            printDecisionTree(data, updatedAttributes, childRows, level + 1, bestIGR);
        }
    }
}
