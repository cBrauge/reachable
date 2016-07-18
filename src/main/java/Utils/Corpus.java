package Utils;

import com.google.common.collect.Maps;
import model.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Corpus {
    private int nbDocuments = 0;
    private Map<String, Double> terms = new HashMap<>();
    private Map<String, Double> onlyTerms = new HashMap<>();
    private Document target;
    private static Corpus ourInstance = new Corpus();

    public static Corpus getInstance() {
        return ourInstance;
    }

    private Corpus() {
    }

    // set target and add it to the corpus
    public void setTarget(Document doc) {
        target = doc;
        // set universe
        addDocument(target);
    }

    // Add document to corpus then analyze it
    public void addDocument(Document doc) {
        nbDocuments++;

        for (String term : doc.getTfs().keySet()) {
            Double count = terms.get(term);
            if (!terms.containsKey(term)) {
                terms.put(term, 1D);
                onlyTerms.put(term, 0D);
            } else {
                terms.put(term, count + 1D);
            }
        }

        analyze(doc);
    }

    // analyse document and then calculate tfidf
    private void analyze(Document doc) {
        System.out.println(doc.getUrl());

        // calculate idf
        for (Map.Entry<String, Double> entry : doc.getTfs().entrySet()) {
            Double idf = Math.log(nbDocuments / terms.get(entry.getKey())) + 1;
            entry.setValue(entry.getValue() * idf);
        }

        // add missing keys
        doc.getTfs().putAll(Maps.difference(onlyTerms, doc.getTfs()).entriesOnlyOnLeft());
        target.getTfs().putAll(Maps.difference(onlyTerms, target.getTfs()).entriesOnlyOnLeft());

        // recalculate target
        Map<String, Double> targetTmp = new HashMap<>();

        for (Map.Entry<String, Double> entry : target.getTfs().entrySet()) {
            Double idf = Math.log(nbDocuments / terms.get(entry.getKey())) + 1;
            targetTmp.put(entry.getKey(), entry.getValue() * idf);
        }
        // pass to vector
        List<Double> listCurrent = new ArrayList<Double>(doc.getTfs().values());
        List<Double> listTarget = new ArrayList<Double>(targetTmp.values());

        Double similarity = calculateCos(listCurrent, listTarget);

        System.out.println(similarity);
    }

    private Double calculateCos(List<Double> listCurrent, List<Double> listTarget) {
        Double num = 0d;
        for (int i = 0; i < listCurrent.size(); i++) {
            num += listCurrent.get(i) * listTarget.get(i);
        }
        Double normA = 0D;
        Double normB = 0D;
        for (int i = 0; i < listCurrent.size(); i++) {
            normA += Math.pow(listCurrent.get(i), 2D);
            normB += Math.pow(listTarget.get(i), 2D);
        }
        normA = Math.sqrt(normA);
        normB = Math.sqrt(normB);

        return num / (normA * normB);
    }
}
