package labs;

import DocumentClasses.Graph;
import java.io.*;
import java.util.*;

public class Lab4 {

    public static void main(String[] args) throws IOException {
        Graph graph = new Graph("./files/graph.txt");
        int N = graph.getNumNodes();

        // key: node, value: page rank value
        HashMap<Integer, Double> pageRankOld = new HashMap<>();

        // calculate initial page rank values
        for (int node : graph.getNodes()) {
            pageRankOld.put(node, 1.0 / N);
        }

        double diff;
        double d = 0.9;
        int itr = 0;
        do {
            HashMap<Integer, Double> pageRankNew = new HashMap<>();
            double prSum = 0.0;

            //calculate page rank
            for (int node : graph.getNodes()) {
                double pr = (1 - d) / N;

                for (int i : graph.getIncomingNodes(node)) {
                    int outDegree = graph.getOutDegree(i);
                    if (outDegree > 0) {
                        pr += d * (pageRankOld.get(i) / outDegree);
                    }
                }

                pageRankNew.put(node, pr);
                prSum += pr;
            }

            // normalize values
            for (int node : pageRankNew.keySet()) {
                pageRankNew.put(node, pageRankNew.get(node) / prSum);
            }

            diff = findDistance(pageRankOld, pageRankNew);
            System.out.println("distance: " + diff);
            pageRankOld = (HashMap<Integer, Double>) pageRankNew.clone();

            itr++;
        } while (diff >= 0.001);

        List<Integer> top20 = pageRankOld.entrySet().stream()
                .sorted((a,b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(20)
                .map(Map.Entry::getKey)
                .toList();

//        for (Map.Entry<Integer, Double> entry : top20) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }


        System.out.println("page rank new: " + top20);
        System.out.println(pageRankOld.get(1159));
        System.out.println("number of iterations: " + itr);
    }

    public static double findDistance(HashMap<Integer, Double> pageRankOld, HashMap<Integer, Double> pageRankNew) {
        double distance = 0.0;
        for(Map.Entry<Integer, Double> entry : pageRankOld.entrySet()) {
            Double oldVal = entry.getValue();
            Double newVal = pageRankNew.get(entry.getKey());
            distance += Math.abs(newVal-oldVal);
        }
        return distance;
    }
}