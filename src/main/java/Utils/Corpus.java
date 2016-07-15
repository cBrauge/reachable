package Utils;

import model.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Corpus {
    private List<Document> documents = new ArrayList<>();
    private Map<String, Double> terms = new HashMap<>();
    private static Corpus ourInstance = new Corpus();

    public static Corpus getInstance() {
        return ourInstance;
    }

    private Corpus() {
    }

    public void addDocument(Document doc) {

        documents.add(doc);
        for (String term : doc.getTfs().keySet()) {
            Double count = terms.get(term);
            if (count == null) {
                terms.put(term, 1D);
            } else {
                terms.put(term, count + 1D);
            }
        }

        // analyse each 100 insertions
        int size = documents.size();

        System.out.println(doc.getUrl());
        if (size % 10 == 0) {
            analyze(size - 10, size);
        }
    }

    private void analyze(Document doc) {
        System.out.println(doc.getUrl());
        // TODO
    }

    private void analyze(int lowBound, int highBound) {
        for (int i = lowBound; i < highBound; i++) {
            analyze(documents.get(i));
        }
    }

}
