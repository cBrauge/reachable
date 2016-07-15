package model;

import java.util.*;

public class Document {
    private String url;
    private Map<String, Double> tfs = new HashMap<>();
    private Double maxOccurrenceOfTerm;

    public Document(String url, Map<String, Double> text, int nbOfTerm) {
        this.url = url;
        this.tfs = text;
        List<Double> list = new ArrayList(text.values());
        Collections.sort(list);
        maxOccurrenceOfTerm = list.get(0);
        text.replaceAll((s, occurrence) -> occurrence = occurrence / nbOfTerm);
        tfs = text;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Double> getTfs(){
        return tfs;
    }

    public Double getTf(String term) {
            return tfs.get(term);
        }
}
