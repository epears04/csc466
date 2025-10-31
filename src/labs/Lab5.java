package labs;

import DocumentClasses.ItemSet;
import java.io.*;
import java.util.*;

public class Lab5 {

    private final double minsup;
    private final ArrayList<ItemSet> transactions; //lists of all itemSets
    private final ArrayList<Integer> items; //lists of all items

    //lists frequent itemSets. E.g., for key=1, store all 1-itemSets, for key=2, all 2-itemSets and so on.
    private final HashMap<Integer, ArrayList<ItemSet>> frequentItemSet;
    // key: transaction number, value: number of transactions it is in
    private final HashMap<Integer, Integer> itemFrequency;
    private final HashMap<ItemSet, Double> itemSupport;

    public Lab5() {
        this.minsup = 0.01;
        this.transactions = new ArrayList<>();
        this.items = new ArrayList<>();
        this.itemFrequency = new HashMap<>();
        this.frequentItemSet = new HashMap<>();
        this.itemSupport = new HashMap<>();

        process("./files/shopping_data.txt");
        findFrequentSingleItemSets();

        int i = 2;
        boolean foundFrequent = true;
        while (foundFrequent) {
            foundFrequent = findFrequentItemSets(i);
            i++;
        }
    }

    // process input file and populate transactions and items
    public void process(String fileName) {
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
    public boolean findFrequentItemSets(int k) {
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
    public ArrayList<Integer> combineSets(List<Integer> items1, List<Integer> items2, int k) {
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
    public boolean isFrequent(ItemSet itemSet) {
        int hits = 0;
        for (ItemSet t : transactions) {
            boolean itemFound = new HashSet<>(t.getItems()).containsAll(itemSet.getItems());
            if (itemFound) hits++;
        }
        double support = (double) hits / transactions.size();
        if (support >= minsup) {
            itemSupport.put(itemSet, support);
            return true;
        }
        return false;
    }

    //finds all 1-itemSets
    public void findFrequentSingleItemSets() {
        ArrayList<ItemSet> singleSets = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : itemFrequency.entrySet()) {
            double ratio = (double) entry.getValue() / transactions.size();
            if (ratio >= minsup) {
                ItemSet itemSet = new ItemSet(new ArrayList<>(List.of(entry.getKey())));
                singleSets.add(itemSet);
                itemSupport.put(itemSet, ratio);
            }
        }
        frequentItemSet.put(1, singleSets);
    }

    public HashMap<Integer, ArrayList<ItemSet>> getFrequentItemSets() {
        return frequentItemSet;
    }

    public ArrayList<ItemSet> getTransactions() {
        return transactions;
    }

    public HashMap<ItemSet, Double> getItemSupport() {
        return itemSupport;
    }

    public static void main(String[] args) {
        Lab5 lab5 = new Lab5();
        for (Map.Entry<Integer, ArrayList<ItemSet>> entry : lab5.getFrequentItemSets().entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }
}
