package labs;

import DocumentClasses.Matrix;
import java.io.*;
import java.util.*;

public class Lab8 {

    private static Matrix matrix;
    private static int[] attrValues;
    private static ArrayList<Integer> categories;

    public static void main(String[] args) {
        matrix = new Matrix(readInput("./files/data.txt"));
        attrValues = new int[4];
        categories = new ArrayList<>(List.of(1,2,3));
        getCustomerInput();
        System.out.println(Arrays.toString(attrValues));
        for (int cat : categories) {
            System.out.println("For value " + cat  + ": Probability is: " + matrix.findProb(attrValues, cat));
        }
        System.out.println("Expected category: " + matrix.findCategory(attrValues));
    }

    // read input from file
    public static int[][] readInput(String fileName) {
        File file = new File(fileName);
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
            System.err.println("File " + fileName + " not found.");
        }
        return matrix;
    }

    // read input from keyboard
    public static void getCustomerInput() {
        Scanner scanner = new Scanner(System.in);
        for(int i = 0; i < 4; i++) {
            System.out.print("Enter value for attribute " + (i) + ": ");
            int val = scanner.nextInt();
            attrValues[i] = val;
        }
    }
}
