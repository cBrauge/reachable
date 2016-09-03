package Utils;

import com.google.common.collect.Maps;
import controller.Controller;
import model.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Corpus {
    private int nbDocuments = 0;
    final private Map<String, Double> terms = new HashMap<>();
    final private Map<String, Double> onlyTerms = new HashMap<>();
    private Document target;
    final private static Corpus ourInstance = new Corpus();

    public static Corpus getInstance() {
        return ourInstance;
    }

    private Corpus() {
    }

    /**
     *
     * @param doc Document that will be the target
     */
    public void setTarget(Document doc) {
        target = doc;
        // set universe
        addDocument(target);
    }

    /**
     * Add document to corpus and analyze it
     * @param doc Document that will be added to the corpus
     */
    public void addDocument(Document doc) {
        nbDocuments++;

        for (String term : doc.getTfs().keySet()) {
            Double count = terms.get(term);
            // Add term to corpus if not in
            if (!terms.containsKey(term)) {
                terms.put(term, 1D);
                onlyTerms.put(term, 0D);
            } else {
                terms.put(term, count + 1D);
            }
        }

        analyze(doc);
    }

    /**
     * Calculate idf, add missing keys to the map, recalculate target, calculate cos
     * @param doc Document that will be analyzed
     */
    private void analyze(Document doc) {
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
        List<Double> listCurrent = new ArrayList<>(doc.getTfs().values());
        List<Double> listTarget = new ArrayList<>(targetTmp.values());

        Double similarity = calculateCos(listCurrent, listTarget);

        if (Controller.similarity <= similarity || Controller.displayAll)
            System.out.println(similarity + ": " + doc.getUrl());
    }

    /**
     *
     * @param listCurrent Vector of current's weight
     * @param listTarget Vector of target's weight
     * @return Cosine of similarity between two documents
     */
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
