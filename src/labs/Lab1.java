package labs;

import DocumentClasses.DocumentCollection;
import DocumentClasses.TextVector;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

public class Lab1 {
    public static void main(String[] args) {

        DocumentCollection docs = new DocumentCollection("./files/documents.txt");

        String maxWord = "";
        int maxWordFreq = 0;
        int distinctWords = 0;
        int words = 0;

        Collection<TextVector> textVectors = docs.getDocuments();
        for (TextVector textVector : textVectors){
            if (textVector.getHighestRawFrequency() > maxWordFreq) {
                maxWord = textVector.getMostFrequentWord();
                maxWordFreq = textVector.getHighestRawFrequency();
            }
            distinctWords += textVector.getDistinctWordCount();
            words += textVector.getTotalWordCount();
        }
        System.out.println("Word = " + maxWord);
        System.out.println("Frequency = " + maxWordFreq);
        System.out.println("Distinct Number of Words = " + distinctWords);
        System.out.println("Total word count = " + words);

        // serialize the DocumentCollection object to a file
        try(ObjectOutputStream os = new ObjectOutputStream(new
                FileOutputStream(new File("./files/docvector")))){
            os.writeObject(docs);
        } catch(Exception e){
            System.out.println(e);
        }

    }
}
