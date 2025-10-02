package labs;

import DocumentClasses.DocumentCollection;
import java.io.*;


public class Lab2 {
    
    public static DocumentCollection documents;
    public static DocumentCollection queries;

    public static void main(String[] args) {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("./files/docvector")))) {
            documents = (DocumentCollection) is.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        queries = new DocumentCollection("./files/queries.txt", "query");

    }

}
