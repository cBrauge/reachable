package crawler;

import Utils.Lemm;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.regex.Pattern;

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
            String html = htmlParseData.getHtml();
            Document htmlDocument = Jsoup.parse(html);
            String article = htmlDocument.getElementsByTag("article").get(0).text();
            if (article.length() > 100) {
                String[] article_words = article.replaceAll("\\p{P}", " ").split("\\s+");

                for (String s : article_words) {
                    System.out.print(s);

                    s = Lemm.getLemm(s.toLowerCase());

                    System.out.print(" " + s + " ");
                }
/*
                System.out.println("URL: " + url);
                System.out.println("Article: " + article);
                Set<WebURL> links = htmlParseData.getOutgoingUrls();

                System.out.println("Text length: " + text.length());
                System.out.println("Html length: " + html.length());
                System.out.println("Number of outgoing links: " + links.size());
*/
                //            System.out.println(html);
            }
        }
    }

}
