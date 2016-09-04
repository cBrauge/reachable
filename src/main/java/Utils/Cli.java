package Utils;

import org.apache.commons.cli.*;

import java.util.concurrent.Callable;

public class Cli {
    private String[] args = null;
    private Options options = new Options();
    public int crawlers;
    public double similarity;
    public String target;
    public String base;
    public int depth;
    public Boolean all = false;
    public Cli(String[] args) {
        this.args = args;

        options.addOption("h", "help", false, "Display help.");
        options.addOption("c", "crawlers", true, "Number of crawlers.");
        options.addOption("s", "similarity", true, "Similarity coefficient.");
        options.addOption("t", "target", true, "Target URL.");
        options.addOption("b", "base", true, "Base url from which we start crawl.");
        options.addOption("d", "depth", true, "Depth to crawl.");
        options.addOption("a", "all", false, "Display all crawled sites even if they're below similarity.");
    }

    public void parse() {
        CommandLineParser parser = new BasicParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("h"))
                help();

            // crawlers
            if (cmd.hasOption("c")) {
                crawlers = Integer.parseInt(cmd.getOptionValue("c"));
                if (crawlers < 1) {
                    System.err.print("Must have at least 1 crawler\n");
                    System.exit(1);
                }
            }
            else {
                System.err.print("Missing crawling option\n");
                System.exit(1);
            }

            // similarity
            if (cmd.hasOption("s")) {
                similarity = Double.parseDouble(cmd.getOptionValue("s"));
                if (similarity < 0.0 || similarity > 1.0) {
                    System.err.print("Similarity must be between 0 and 1\n");
                    System.exit(1);
                }
            }
            else {
                System.err.print("Missing similarity option\n");
                System.exit(1);
            }

            // target
            if (cmd.hasOption("t")) {
                target = cmd.getOptionValue("t");
            }
            else {
                System.err.print("Missing target option\n");
                System.exit(1);
            }

            // base
            if (cmd.hasOption("b")) {
                base = cmd.getOptionValue("b");
            }
            else {
                System.err.print("Missing base option\n");
                System.exit(1);
            }

            //depth
            if (cmd.hasOption("d")) {
                depth = Integer.parseInt(cmd.getOptionValue("d"));
                if (depth < 1) {
                    System.err.print("Must have at least 1 depth\n");
                    System.exit(1);
                }
            }
            else {
                System.err.print("Missing depth option\n");
                System.exit(1);
            }
            //all
            if (cmd.hasOption("a")) {
                all = Boolean.parseBoolean(cmd.getOptionValue("a"));
            }

        } catch (ParseException e) {
            System.err.print("Parsing error: " + e);
            help();
        }
        catch (Exception e)
        {
            System.err.print("Error: " + e);
        }
    }

    public void help() {
        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("Main", options);
        System.exit(0);
    }
}