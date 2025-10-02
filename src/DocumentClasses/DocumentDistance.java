package DocumentClasses;

public interface DocumentDistance {

    // returns the distance between query and document
    double findDistance(TextVector query, TextVector doc, DocumentCollection docs);
}
