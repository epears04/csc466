package DocumentClasses;

import java.util.*;

public class ItemSet {

    private ArrayList<Integer> items;
    private int transactionNum;

    public ItemSet(String itemSet, HashMap<Integer, Integer> itemFrequency) {
        // item set input as transaction number followed by a list of items that were purchases
        String[] tokens = itemSet.split(", ");
        transactionNum = Integer.parseInt(tokens[0]);
        items = new ArrayList<>();
        // start at 1 to account for transaction number
        for (int i = 1; i < tokens.length; i++) {
            int item = Integer.parseInt(tokens[i]);
            items.add(item);
            // no duplicate items in transaction so will only add once
            itemFrequency.merge(item, 1, Integer::sum); // number of times it appears in transaction
        }
        // check for duplicate items in a transaction
        if (new HashSet<>(items).size() != items.size()) {
            System.out.println("Error: ItemSet contains duplicate items!");
        }
    }

    // create an itemSet with a single item
    public ItemSet(int item) {
        items = new ArrayList<>(List.of(item));
        transactionNum = -1; // not associated with transaction
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public int getTransactionNum() {
        return transactionNum;
    }

    public int getSize() {
        return items.size();
    }

    public boolean containsItem(int item) {
        return items.contains(item);
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
