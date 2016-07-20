package controller;

import crawler.ReachableCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
    public static String targetUrl = "";
    private static String crawlSeed = "";
    public static Double similarity = 0.1D;
    private static String crawlStorageFolder = "/data/crawl/root";
    private static int depthOfCrawl = 1;
    private static int numberOfCrawlers = 4;
    public static void main(String[] args) throws Exception {

        // TODO get it from user
        numberOfCrawlers = 4;
        similarity = 0.5D;
        targetUrl = "http://www.lefigaro.fr/actualite-france/2016/07/18/01016-20160718ARTFIG00196-attentat-de-nice-six-questions-sur-l-enquete-et-l-auteur-de-la-tuerie.php";
        crawlSeed = "http://www.lemonde.fr/police-justice/article/2016/07/18/attentat-de-nice-le-point-sur-les-avancees-de-l-enquete_4971451_1653578.html";
        depthOfCrawl = 4;

        analyze(true);
        analyze(false);
    }

    /**
     *
     * @param isTarget (true) it is the target so we crawl only 0 depth
     * @throws Exception
     */
    private static void analyze(boolean isTarget) throws Exception {
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(isTarget ? 0 : depthOfCrawl);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        if (isTarget) {
            config.setMaxOutgoingLinksToFollow(0);
        }

        controller.addSeed(isTarget ? targetUrl : crawlSeed);

        controller.start(ReachableCrawler.class, numberOfCrawlers);
    }
}
