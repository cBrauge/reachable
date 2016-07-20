package crawler;

import Utils.Corpus;
import Utils.Lemm;
import controller.Controller;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import model.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ReachableCrawler extends WebCrawler {
    private static Boolean targetIsSet = false;
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz|:[1-9][0-9]{0,5}))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches();
    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            List<String> article_words = Arrays.asList(text.replaceAll("\\p{P}", " ").split("\\s+"));
            addToCorpus(article_words, page.getWebURL().getURL());
        }
    }

    /**
     *
     * @param article_words Page as list of words
     * @param url Url of the page
     */
    private void addToCorpus(List<String> article_words, String url) {
        article_words.stream().map(e -> e = Lemm.getLemm(e.toLowerCase()));

        Map<String, Double> counts =
                article_words.stream().collect(HashMap<String, Double>::new,
                        (map, str) -> {
                            if (!map.containsKey(str)) {
                                map.put(str, Lemm.namedEntity(str));
                            } else {
                                map.put(str, map.get(str) + Lemm.namedEntity(str));
                            }
                        },
                        HashMap<String, Double>::putAll);

        Document doc = new Document(url, counts, article_words.size());
        Corpus corpus = Corpus.getInstance();
        if (!targetIsSet && url.equals(Controller.targetUrl)) {
            targetIsSet = true;
            corpus.setTarget(doc);
        } else {
            corpus.addDocument(doc);
        }
    }
}
