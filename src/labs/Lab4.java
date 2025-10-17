package labs;

import java.io.*;
import java.util.*;

public class Lab4 {

    public static void main(String[] args) {
        HashSet<Integer> nodes = new HashSet<>(); // set of nodes from graph.txt
        HashMap<Integer, ArrayList<Integer>> adj_list = new HashMap<>(); // key: node, value: list of incoming nodes
        HashMap<Integer, Integer> out_degree = new HashMap<>(); // key: node, value: out degree

        // key: node, value: page rank
        HashMap<Integer, Double> pageRankOld = new HashMap<>();
        HashMap<Integer, Double> pageRankNew = new HashMap<>();

        // parse graph.txt
        try(Scanner reader = new Scanner(new File("./files/graph.txt"))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] tokens = line.split(",");
                int source = Integer.parseInt(tokens[0]);  // edge starts
                int target = Integer.parseInt(tokens[2]); // edge ends

                // add nodes to set (if not already there)
                nodes.add(source);
                nodes.add(target);

                // build adjacency list
                if (adj_list.containsKey(target)) {
                    // add edge if not previously existing
                    if (!adj_list.get(target).contains(source)) {
                        ArrayList<Integer> incoming = adj_list.get(target);
                        incoming.add(source);
                        adj_list.put(target, incoming);
                    }
                } else {
                    adj_list.put(target, new ArrayList<>(List.of(source)));
                }

                // update out_degree
                // TODO: what happens if node has 2 outgoing edges to same node
                if (out_degree.containsKey(source)) {
                    out_degree.put(source, out_degree.get(source) + 1);
                } else {
                    out_degree.put(source, 1);
                }


            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        System.out.println("number of nodes: " + nodes.size());
    }

    // TODO: may change depending on implementation
    public double L1Norm(ArrayList<Double> x, ArrayList<Double> y) {
        return 0.0;
    }

    // parses graph.txt
    // edge form the node at position 1 to the node at position 3. ignore position 2 and 4

}
