package controller;

import Utils.Cli;
import crawler.ReachableCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.Console;
import java.io.File;

public class Controller {
    public static String targetUrl = "";
    private static String crawlSeed = "";
    public static Double similarity = 0.1D;
    private static String crawlStorageFolder = "crawl";
    private static int depthOfCrawl = 1;
    private static int numberOfCrawlers = 4;
    public static Boolean displayAll = false;
    public static void main(String[] args) throws Exception {

        Cli cli = new Cli(args);
        cli.parse();

        // TODO get it from user
        numberOfCrawlers = cli.crawlers;
        similarity = cli.similarity;
        targetUrl = cli.target;
        crawlSeed = cli.base;
        depthOfCrawl = cli.depth;
        displayAll = cli.all;
        // Create folder for storage
        new File(crawlStorageFolder).mkdirs();

        System.out.print("Start analyze target ...");
        analyze(true);
        System.out.print("End analyze target ...");
        System.out.print("Start crawling");
        analyze(false);
        System.out.print("End crawling");
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
