package labs;

import DocumentClasses.DocumentCollection;
import DocumentClasses.OkapiDistance;
import DocumentClasses.TextVector;

import java.io.*;
import java.util.*;

public class Lab3 {
    public static DocumentCollection documents;
    public static DocumentCollection queries;
    public static HashMap<Integer, ArrayList<Integer>> cosineResults;
    public static HashMap<Integer, ArrayList<Integer>> okapiResults;

    public static void main(String[] args) {

        // load documents from binary file
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream("./files/docvector"))) {
            documents = (DocumentCollection) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        queries = new DocumentCollection("./files/queries.txt", "query");

        // load cosine results from binary file
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream("./files/results"))) {
            cosineResults = (HashMap<Integer, ArrayList<Integer>>) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("Calculating okapi distances");
        // calculate top 20 documents for each query
        OkapiDistance okapi = new OkapiDistance();
        okapiResults = new HashMap<>();
        for (Map.Entry<Integer, TextVector> entry : queries.getEntrySet()) {
            TextVector query = entry.getValue();
            ArrayList<Integer> closestDocs = query.findClosestDocuments(documents, okapi);
            okapiResults.put(entry.getKey(), closestDocs); // input query number and closest docs
            // TODO: should query number correlate to query number we made in lab 2?
        }

        System.out.println("Parsing human judgement file");
        // key: query number, value: list of relevant document ids
        HashMap<Integer, ArrayList<Integer>> humanJudgement = new HashMap<>();
        try(Scanner reader = new Scanner(new File("./files/human_judgement.txt"))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] tokens = line.split(" ");
                int queryNum = Integer.parseInt(tokens[0]);
                int docId = Integer.parseInt(tokens[1]);
                int relevance = Integer.parseInt(tokens[2]);
                // if document is relevant, add document id to hashmap
                if (relevance >= 1 && relevance <= 3) {
                    if (humanJudgement.containsKey(queryNum)) {
                        ArrayList<Integer> judgements = humanJudgement.get(queryNum);
                        judgements.add(docId);
                        humanJudgement.put(queryNum, judgements); // update list with new doc
                    } else {
                        humanJudgement.put(queryNum, new ArrayList<>(List.of(docId)));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        System.out.println("Calculating MAP scores");
        // print MAP scores
        System.out.println("Cosine Map = " + computeMap(humanJudgement, cosineResults));
        System.out.println("Okapi Map = " + computeMap(humanJudgement, okapiResults));
    }

    // computes the MAP score between 2 hash maps using only the first 20 queries
    // computer results: key -> query number, value -> 20 closest docs
    // human judgement: key -> query number, value -> relevant docs
    public static double computeMap(HashMap<Integer, ArrayList<Integer>> humanJudgement,
                             HashMap<Integer, ArrayList<Integer>> computerResults) {
        double sum = 0.0;
        int queryNum = 0;
        // loop through each query
        for  (Map.Entry<Integer, ArrayList<Integer>> entry : computerResults.entrySet()) {
            int queryId = entry.getKey();
            ArrayList<Integer> computerRanks = entry.getValue();
            ArrayList<Integer> humanRanks = humanJudgement.get(queryId);

            // return early if no human relevant documents
            if (humanRanks == null || humanRanks.isEmpty()) {
                queryNum++;
                continue;
            }

            int numHit = 0;
            double queryPrecision = 0.0;
            for (int i = 1; i <= 20; i++) {
                int docId = computerRanks.get(i - 1);
                if (humanRanks.contains(docId)) {
                    numHit++;
                    queryPrecision += (double) numHit / i;
                }
            }

            double avgPrecision = queryPrecision / (double) humanRanks.size();
            sum += avgPrecision;
            queryNum++;
        }
        if (queryNum == 0) {
            return 0.0;
        }
        return sum / (double) queryNum;
    }
}
