package labs;

import DocumentClasses.ItemSet;
import java.io.*;
import java.util.*;

public class Lab5 {

    private static final double minsup = 0.01;
    private static ArrayList<ItemSet> transactions; //lists of all itemSets
    private static ArrayList<Integer> items; //lists of all items

    //lists frequent itemSets. E.g., for key=1, store all 1-itemSets, for key=2, all 2-itemSets and so on.
    private static HashMap<Integer, ArrayList<ItemSet>> frequentItemSet;
    // key: transaction number, value: number of transactions it is in
    private static HashMap<Integer, Integer> itemFrequency;

    public static void main() {
        transactions = new ArrayList<>();
        items = new ArrayList<>();
        itemFrequency = new HashMap<>();
        frequentItemSet = new HashMap<>();
        process("./files/shopping_data.txt");
        findFrequentSingleItemSets();
//        System.out.println(frequentItemSet);
        for (Map.Entry<Integer, ArrayList<ItemSet>> entry : frequentItemSet.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    // process input file and populate transactions and items
    public static void process(String fileName) {
        File file = new File(fileName);
        try(Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                ItemSet itemSet = new ItemSet(reader.nextLine(), itemFrequency);
                transactions.add(itemSet);
                items.addAll(itemSet.getItems());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    //finds all k-itemSets, Returns false if no itemSets were found (precondition k>=2)
    public static boolean findFrequentItemSets(int k) {
        if (k < 2) {
            return false;
        }
        return k==3;
    }

    // returns true if itemSet is frequent
    public static boolean isFrequent(ItemSet itemSet) {
        int item = itemSet.getItems().getFirst();
        double freq = (double) itemFrequency.getOrDefault(item, 0) / transactions.size();
        return freq >= minsup;
        // TODO: handle k >= 2
    }

    //finds all 1-itemSets
    public static void findFrequentSingleItemSets() {
        ArrayList<ItemSet> singleSets = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : itemFrequency.entrySet()) {
            double ratio = (double) entry.getValue() / transactions.size();
            if (ratio >= minsup) {
                ItemSet itemSet = new ItemSet(entry.getKey());
                singleSets.add(itemSet);
            }
        }
        frequentItemSet.put(1, singleSets);
    }
}
