package labs;

import DocumentClasses.Matrix;
import java.io.*;
import java.util.*;

public class Lab7 {

    // stores [data][attribute]
    private static int[][] data;
    public static void main(String[] args) {
        data = process("./files/data.txt");
        Matrix matrix = new Matrix(data);
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
}
