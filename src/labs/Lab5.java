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

        int i = 2;
        boolean foundFrequent = true;
        while (foundFrequent) {
            foundFrequent = findFrequentItemSets(i);
            i++;
        }

        for (Map.Entry<Integer, ArrayList<ItemSet>> entry : frequentItemSet.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
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

        boolean found = false;
        // use linked hash set to maintain order
        Set<ItemSet> uniqueSets = new LinkedHashSet<>();

        List<ItemSet> prev = frequentItemSet.get(k-1);
        for (int i = 0; i < prev.size() - 1; i++) {
            for (int j = i + 1; j < prev.size(); j++) {
                List<Integer> combo = combineSets(prev.get(i).getItems(), prev.get(j).getItems(), k);
                // if there is a possible frequent item set
                if (!combo.isEmpty()) {
                    ItemSet candidate = new ItemSet(combo);
                    if (isFrequent(candidate)) {
                        // test if candidate is unique
                        if (uniqueSets.add(candidate)) {
                            found = true;
                        }
                    }
                }
            }
        }
        if (found) {
            frequentItemSet.put(k, new ArrayList<>(uniqueSets));
        }
        return found;
    }

    // returns combined list of items, or empty list if items cannot be combined
    public static ArrayList<Integer> combineSets(List<Integer> items1, List<Integer> items2, int k) {
        ArrayList<Integer> combination = new ArrayList<>(items1);
        combination.addAll(items2);
        Set<Integer> comboSet = new HashSet<>(combination);
        // check if set only shares one item in common
        if (comboSet.size() == k ) {
            return new ArrayList<>(comboSet);
        }
        return new ArrayList<>();
    }

    // returns true if itemSet is frequent
    public static boolean isFrequent(ItemSet itemSet) {
        int hits = 0;
        for (ItemSet item : transactions) {
            boolean itemFound = item.getItems().containsAll(itemSet.getItems());
            if (itemFound) {
                hits++;
            }
        }
        return (double) hits / transactions.size() >= minsup;
    }

    //finds all 1-itemSets
    public static void findFrequentSingleItemSets() {
        ArrayList<ItemSet> singleSets = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : itemFrequency.entrySet()) {
            double ratio = (double) entry.getValue() / transactions.size();
            if (ratio >= minsup) {
                Object ArrayList;
                ItemSet itemSet = new ItemSet(new ArrayList<>(List.of(entry.getKey())));
                singleSets.add(itemSet);
            }
        }
        frequentItemSet.put(1, singleSets);
    }
}
