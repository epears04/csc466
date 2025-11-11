package labs;

import DocumentClasses.ItemSet;
import DocumentClasses.Rule;
import java.util.*;

public class Lab6 {

    private static ArrayList<Rule> rules = new ArrayList<>();
    private static HashMap<Integer, ArrayList<ItemSet>> frequentItemSet;
    private static HashMap<ItemSet, Double> itemSupport;
    private static final double MIN_CONF = 0.99;
    // Key: LHS items, Value: { Key: RHS items, Value: confidence of prev rule }
    private static HashMap<ItemSet, HashMap<ItemSet, Double>> rulesByLeft;

    public static void main() {
        Lab5 lab5 = new Lab5();
        frequentItemSet = lab5.getFrequentItemSets();
        itemSupport = lab5.getItemSupport();

        generateRules();
        System.out.println(rules);
    }

    public static void generateRules() {
        rulesByLeft = new HashMap<>();
        // find rules with 1 item on RHS
        for (Map.Entry<Integer, ArrayList<ItemSet>> entry : frequentItemSet.entrySet()) {
            for  (ItemSet itemSet : entry.getValue()) {
                rules.addAll(split(itemSet));
            }
        }
        // combine rules
        for (Map.Entry<ItemSet, HashMap<ItemSet, Double>> entry : rulesByLeft.entrySet()) {
            ItemSet lhs = entry.getKey();
            ArrayList<ItemSet> rhsItems = new ArrayList<>(entry.getValue().keySet());
            int size = 1;

            // iterate through all items with more than 1 rhs candidate
            while(rhsItems.size() > 1) {
                ArrayList<ItemSet> candidates = combine(rhsItems, size);
                ArrayList<ItemSet> nextRhs = new ArrayList<>();

                for (ItemSet candidate : candidates) {
                    ArrayList<Integer> merge = new ArrayList<>(lhs.getItems());
                    merge.addAll(candidate.getItems());
                    ItemSet root = new ItemSet(merge);
                    Double confLhs = itemSupport.get(lhs);
                    Double confRoot = itemSupport.get(root);
                    if (confLhs == null || confRoot == null || confLhs == 0.0) {
                        continue;
                    }

                    double conf = confRoot / confLhs;
                    if (conf >= MIN_CONF) {
                        rules.add(new Rule(lhs, candidate, root));
                        nextRhs.add(candidate);
                    }
                }
                rhsItems = nextRhs;
                size++;
            }
        }
    }

    // takes as input a frequent itemSet and generates all association rules of size 1 that can be extracted from it
    public static ArrayList<Rule> split(ItemSet itemSet) {
        ArrayList<Rule> generatedRules = new ArrayList<>();
        List<Integer> items = itemSet.getItems();
        int index = 0;
        for (Integer item : items) {
            List<Integer> copy = new ArrayList<>(items);
            copy.remove(index);
            ItemSet rhs = new ItemSet(new ArrayList<>(List.of(item)));
            ItemSet lhs = new ItemSet(new ArrayList<>(copy));

            Rule r = new Rule(lhs, rhs, itemSet);
            double conf = getMinConfidence(r);

            if(conf >= MIN_CONF) {
                generatedRules.add(r);
                rulesByLeft.computeIfAbsent(lhs, k -> new HashMap<>()).put(rhs, conf);
            }
            index++;
        }
        return generatedRules;
    }

    public static double getMinConfidence(Rule r) {
        double confLeft = itemSupport.getOrDefault(r.getLeft(), 0.0);
        double confAll = itemSupport.getOrDefault(r.getRoot(), 0.0);
        if (confLeft == 0.0 || confAll == 0.0) {
            return 0.0;
        }
        return (confAll / confLeft);
    }

    // combine two rules
    public static ArrayList<ItemSet> combine(ArrayList<ItemSet> rhsItems, int size) {
        ArrayList<ItemSet> result = new ArrayList<>();

        for(int i = 0; i < rhsItems.size(); i++) {
            for (int j = i + 1; j < rhsItems.size(); j++) {
                List<Integer> a = rhsItems.get(i).getItems();
                List<Integer> b = rhsItems.get(j).getItems();

                Set<Integer> combo = new HashSet<>(a);
                combo.addAll(b);

                if (combo.size() == size + 1) {
                    ItemSet candidate = new ItemSet(new ArrayList<>(combo));
                    if (!result.contains(candidate)) {
                        result.add(candidate);
                    }
                }
            }
        }

        return result;
    }

}
