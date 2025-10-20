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

        double d = 0.9;
        int itr = 0;
        while (true){
            itr++;
            double prSum = 0.0;
            HashMap<Integer, Double> pageRankNew = new HashMap<>();
            // put first term of page rank into new page rank
            for (int node : graph.getNodes()) pageRankNew.put(node, (1.0 - d) / N);

            //calculate second term of page rank formula
            for (int i : graph.getNodes()) {
                double sumIn = 0.0;
                for (int jk : graph.getIncomingNodes(i)) {
                    int out = graph.getOutDegree(jk);
                    if (out > 0) sumIn += pageRankOld.get(jk) / out;
                }
                double pr = pageRankNew.get(i) + d * sumIn;
                prSum += pr;
                pageRankNew.put(i, pr);
            }


            // normalize values
            for (int node : pageRankNew.keySet()) {
                pageRankNew.put(node, pageRankNew.get(node) / prSum);
            }

            // calculate L1 distance
            double dist = findDistance(pageRankOld, pageRankNew);
            if (dist < 0.001) {
                break;
            }
            pageRankOld = (HashMap<Integer, Double>) pageRankNew.clone();
        }

        List<Integer> top20 = pageRankOld.entrySet().stream()
                .sorted((a,b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(20)
                .map(Map.Entry::getKey)
                .toList();

        System.out.println("top 20: " + top20);
        System.out.println("iterations:" + itr);
        System.out.println("page rank 1159: " + pageRankOld.get(1159));
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
