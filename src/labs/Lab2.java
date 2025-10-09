package labs;

import DocumentClasses.CosineDistance;
import DocumentClasses.DocumentCollection;
import DocumentClasses.TextVector;

import java.io.*;
import java.util.*;

public class Lab2 {
    
    public static DocumentCollection documents;
    public static DocumentCollection queries;

    static void main() {

        // load data from binary file
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream("./files/docvector"))) {
            documents = (DocumentCollection) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        if (documents == null) {
            System.err.println("Failed to load documents.");
            return;
        }

        queries = new DocumentCollection("./files/queries.txt", "query");

        documents.normalize(documents);
        queries.normalize(documents);

        CosineDistance cosine = new CosineDistance();

        int queryNum = 1;
        HashMap<Integer, ArrayList<Integer>> results = new HashMap<>();
        for (Map.Entry<Integer, TextVector> entry : queries.getEntrySet()) {
            TextVector query = entry.getValue();
            ArrayList<Integer> closestDocs = query.findClosestDocuments(documents, cosine);
            results.put(queryNum++, closestDocs);
        }

        // serialize the DocumentCollection object to a file
        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("./files/results"))){
            os.writeObject(results);
        } catch(Exception e){
            System.out.println(e);
        }

        System.out.println("documents for query 1:" + results.get(1));
    }

}
