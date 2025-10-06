package DocumentClasses;

import java.io.Serializable;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class DocumentCollection implements Serializable {

    // maps document ID to TextVector object
    public HashMap<Integer, TextVector> documents;
    public static String[] noiseWordArray = {"a", "about", "above", "all", "along",
            "also", "although", "am", "an", "and", "any", "are", "aren't", "as", "at",
            "be", "because", "been", "but", "by", "can", "cannot", "could", "couldn't",
            "did", "didn't", "do", "does", "doesn't", "e.g.", "either", "etc", "etc.",
            "even", "ever", "enough", "for", "from", "further", "get", "gets", "got", "had", "have",
            "hardly", "has", "hasn't", "having", "he", "hence", "her", "here",
            "hereby", "herein", "hereof", "hereon", "hereto", "herewith", "him",
            "his", "how", "however", "i", "i.e.", "if", "in", "into", "it", "it's", "its",
            "me", "more", "most", "mr", "my", "near", "nor", "now", "no", "not", "or", "on", "of", "onto",
            "other", "our", "out", "over", "really", "said", "same", "she",
            "should", "shouldn't", "since", "so", "some", "such",
            "than", "that", "the", "their", "them", "then", "there", "thereby",
            "therefore", "therefrom", "therein", "thereof", "thereon", "thereto",
            "therewith", "these", "they", "this", "those", "through", "thus", "to",
            "too", "under", "until", "unto", "upon", "us", "very", "was", "wasn't",
            "we", "were", "what", "when", "where", "whereby", "wherein", "whether",
            "which", "while", "who", "whom", "whose", "why", "with", "without",
            "would", "you", "your", "yours", "yes"};

    //  a constructor that uses the data in the file to populate the documents variable
    public DocumentCollection(String inputFile, String type) {
        documents = new HashMap<>();
        File file = new File(inputFile);
        boolean inBody;

        try (Scanner reader = new Scanner(file)) {
            String line = reader.nextLine();
            while (reader.hasNextLine()) {
                // if line starts with ".I" get key value
                if (line.startsWith(".I")) {
                    inBody = false;
                    TextVector textVector;
                    if (type.equals("document")) {
                        textVector = new DocumentVector();
                    } else {
                        textVector = new QueryVector();
                    }
                    int key = Integer.parseInt(line.substring(3));
                    while (reader.hasNextLine()) {
                        line = reader.nextLine();
                        // break loop if it gets to next key
                        if (line.startsWith(".I")) {
                            break;
                        } else if (line.startsWith(".W")) {
                            // enter the body
                            inBody = true;
                        } else if (inBody) {
                            line = line.toLowerCase();
                            String[] words = line.split("[^a-zA-Z]+");

                            for (String word : words) {
                                if (word.length() >= 2 && !isNoiseWord(word)) {
                                    textVector.add(word);
                                }
                            }
                        }
                    }
                    documents.put(key, textVector);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    // returns True if word is a noise word, False otherwise
    private Boolean isNoiseWord(String word) {
        return Arrays.asList(noiseWordArray).contains(word);
    }

    // returns the TextVector for the document with the given id
    public TextVector getDocumentById(int id) {
        return documents.get(id);
    }

    // returns the average length of a document (not counting noise words)
    public int getAverageDocumentLength() {
        int total = 0;
        for (Integer id : documents.keySet()) {
            total += documents.get(id).getTotalWordCount();
        }
        return total / documents.size();
    }

    // returns number of documents
    public int getSize() {
        return documents.size();
    }

    public Collection<TextVector> getDocuments() {
        return documents.values();
    }

    // returns a mapping of document id to TextVector
    public Set<Map.Entry<Integer, TextVector>> getEntrySet() {
        return documents.entrySet();
    }

    // returns the number of documents that contain the input word
    public int getDocumentFrequency(String word) {
        int total = 0;
        for (Integer id : documents.keySet()) {
            if (documents.get(id).contains(word)) {
                total += 1;
            }
        }
        return total;
    }

    // calls normalize(dc) on each document
    public void normalize(DocumentCollection dc) {
        for (Map.Entry<Integer, TextVector> entry : documents.entrySet()) {
            entry.getValue().normalize(dc);
        }
    }
}