package crawler;

import Utils.Corpus;
import Utils.Lemm;
import model.Document;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReachableCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz|:[1-9][0-9]{0,5}))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches();
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            if (text.length() > 100) {
                List<String> article_words = Arrays.asList(text.replaceAll("\\p{P}", " ").split("\\s+"));
                article_words.stream().map(e -> e = Lemm.getLemm(e.toLowerCase()));

                Map<String, Double> counts =
                        article_words.stream().collect(HashMap<String,Double>::new,
                                (map, str) -> {
                                    if(!map.containsKey(str)){
                                        map.put(str, 1D);
                                    }else{
                                        map.put(str, map.get(str) + 1D);
                                    }
                                },
                                HashMap<String,Double>::putAll);

                Document doc = new Document(url, counts, article_words.size());
                Corpus corpus = Corpus.getInstance();
                corpus.addDocument(doc);
            }
        }
    }
}
