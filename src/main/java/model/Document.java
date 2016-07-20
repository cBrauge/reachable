package model;

import java.util.*;

public class Document {
    private String url;
    private Map<String, Double> tfs = new HashMap<>();

    /**
     * Constructor
     * @param url Represents the url of the document
     * @param text Map that represents the document terms and their occurrence.
     * @param nbOfTerm Number of terms in the document
     */
    public Document(String url, Map<String, Double> text, int nbOfTerm) {
        this.url = url;
        this.tfs = text;
        List<Double> list = new ArrayList<>(text.values());
        Collections.sort(list);
        text.replaceAll((s, occurrence) -> occurrence = occurrence / nbOfTerm);
        tfs = text;
    }

    /**
     *
     * @return the url of the document
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return the map of term frequency
     */
    public Map<String, Double> getTfs(){
        return tfs;
    }
}
