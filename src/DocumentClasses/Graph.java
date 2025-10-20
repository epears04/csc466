package DocumentClasses;

import java.io.*;
import java.util.*;

public class Graph {

    private HashSet<Integer> nodes; // set of nodes from graph.txt
    private HashMap<Integer, ArrayList<Integer>> incomingNodes; // key: node, value: list of incoming nodes
    private HashMap<Integer, Integer> outDegree; // key: node, value: out degree

    public Graph(String fileName) {
        File file = new File(fileName);
        nodes = new HashSet<>();
        incomingNodes = new HashMap<>();
        outDegree = new HashMap<>();

        HashMap<Integer, HashSet<Integer>> outgoingTemp = new HashMap<>();

        // parse graph file
        try(Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] tokens = line.split(",");
                int source = Integer.parseInt(tokens[0]);  // edge starts
                int target = Integer.parseInt(tokens[2]); // edge ends

                // add nodes to set (if not already there)
                nodes.add(source);
                nodes.add(target);

                // build adjacency list
                incomingNodes.computeIfAbsent(target, k -> new ArrayList<>());
                if (!incomingNodes.get(target).contains(source)) {
                    incomingNodes.get(target).add(source);
                }

                // build outgoing
                outgoingTemp.computeIfAbsent(source, k -> new HashSet<>());
                outgoingTemp.get(source).add(target); // add if unique target

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        // update out degree
        for (int node : nodes) {
            if (outgoingTemp.containsKey(node)) {
                outDegree.put(node, outgoingTemp.get(node).size());
            } else {
                outDegree.put(node, 0);
            }
        }

        // ensure every node exists in incoming
        for (int node : nodes) {
            incomingNodes.putIfAbsent(node, new ArrayList<>());
        }

    }

    public HashSet<Integer> getNodes() {
        return nodes;
    }

    public ArrayList<Integer> getIncomingNodes(int sourceNode) {
        return incomingNodes.getOrDefault(sourceNode, new ArrayList<>());
    }

    public int getOutDegree(int sourceNode) {
        return outDegree.get(sourceNode);
    }

    public int getNumNodes() {
        return nodes.size();
    }

}
