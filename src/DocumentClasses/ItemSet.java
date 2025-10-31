package DocumentClasses;

import java.util.*;

public class ItemSet {

    private List<Integer> items;

    public ItemSet(String itemSet, HashMap<Integer, Integer> itemFrequency) {
        // item set input as transaction number followed by a list of items that were purchases
        String[] tokens = itemSet.split(", ");
        this.items = new ArrayList<>();
        // start at 1 to account for transaction number
        for (int i = 1; i < tokens.length; i++) {
            int item = Integer.parseInt(tokens[i]);
            this.items.add(item);
            // no duplicate items in transaction so will only add once
            itemFrequency.merge(item, 1, Integer::sum); // number of times it appears in transaction
        }
        // sanity check for duplicate items in a transaction
        if (new HashSet<>(items).size() != items.size()) {
            System.out.println("Error: ItemSet contains duplicate items!");
        }
    }

    // create an itemSet with a single item
    public ItemSet(List<Integer> newItems) {
        Collections.sort(newItems);
        this.items = Collections.unmodifiableList(newItems);
    }

    public List<Integer> getItems() {
        return items;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemSet)) return false;
        ItemSet other = (ItemSet) o;
        return items.equals(other.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}
