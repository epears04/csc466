package labs;

import DocumentClasses.ItemSet;
import DocumentClasses.Rule;
import java.util.*;

public class Lab6 {

    private static ArrayList<Rule> rules = new ArrayList<>();
    private static ArrayList<ItemSet> transactions;
    private static HashMap<Integer, ArrayList<ItemSet>> frequentItemSet;
    private static HashMap<ItemSet, Double> itemSupport;

    public static void main() {
        Lab5 lab5 = new Lab5();
        transactions = lab5.getTransactions();
        frequentItemSet = lab5.getFrequentItemSets();
        itemSupport = lab5.getItemSupport();
    }


    public static ArrayList<Rule> split(ItemSet itemSet) {
        ArrayList<Rule> generatedRules = new ArrayList<>();
        List<Integer> items = itemSet.getItems();
        for (int i = 1; i < items.size(); i++) {
            ItemSet l = new ItemSet(items.subList(0, i));
            ItemSet r = new ItemSet(items.subList(i, items.size()));
            Rule rule = new Rule(l, r);
            generatedRules.add(rule);
        }
        return generatedRules;
    }

    public static void generateRules() {
        for (int i = 2; i < frequentItemSet.size(); i++) {
            for (ItemSet itemSet : frequentItemSet.get(i)) {
                rules.addAll(split(itemSet));
            }
        }
    }

}
