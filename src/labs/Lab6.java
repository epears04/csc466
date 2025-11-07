package labs;

import DocumentClasses.ItemSet;
import DocumentClasses.Rule;
import java.util.*;

public class Lab6 {

    private static ArrayList<Rule> rules = new ArrayList<>();
    private static HashMap<Integer, ArrayList<ItemSet>> frequentItemSet;
    private static HashMap<ItemSet, Double> itemSupport;
    private static final double MIN_CONF = 0.99;

    public static void main() {
        Lab5 lab5 = new Lab5();
        frequentItemSet = lab5.getFrequentItemSets();
        itemSupport = lab5.getItemSupport();

        generateRules();
        System.out.println(rules);
    }

    public static void generateRules() {
        for (Map.Entry<Integer, ArrayList<ItemSet>> entry : frequentItemSet.entrySet()) {
            int k = entry.getKey();
            if (k < 2) continue; // skip itemSets of size 1
            for  (ItemSet itemSet : entry.getValue()) {
                rules.addAll(split(itemSet));
            }
        }
    }

    // takes as input a frequent itemSet and generates all association rules that can be extracted from it
    public static ArrayList<Rule> split(ItemSet itemSet) {
        ArrayList<Rule> generatedRules = new ArrayList<>();
        List<Integer> items = itemSet.getItems();
        int n = items.size();
        int total = 1 << n; // bitwise mask to get all subsets
        for (int mask = 1; mask < total - 1; mask++) {
            ItemSet lhs = new ItemSet(new ArrayList<>());
            ItemSet rhs = new ItemSet(new ArrayList<>());
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    lhs.getItems().add(items.get(i));
                } else {
                    rhs.getItems().add(items.get(i));
                }
            }
            Rule rule = new Rule(lhs, rhs, itemSet);
            if (isMinConfidenceMet(rule)) generatedRules.add(rule);
        }
        return generatedRules;
    }

    public static boolean isMinConfidenceMet(Rule r) {
        double confLeft = itemSupport.get(r.getLeft());
        double confAll = itemSupport.get(r.getRoot());
        return (confAll / confLeft) >= MIN_CONF;
    }

}
