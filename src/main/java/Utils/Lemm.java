package Utils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class Lemm {
    private HashMap<String, String> lemms = new HashMap<>();

    private static Lemm ourInstance = new Lemm();

    public static Lemm getInstance() {
        return ourInstance;
    }

    private Lemm() {
        // Load the directory as a resource
        URL dir_url = ClassLoader.getSystemResource("dico");
        // Turn the resource into a File object
        File dir = null;
        try {
            dir = new File(dir_url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        for (File dic : dir.listFiles()) {
            try (BufferedReader br = new BufferedReader(new FileReader(dic))) {
                for(String line; (line = br.readLine()) != null; ) {
                    String[] words = line.split("\t");

                    lemms.put(words[0], words[1]);
                }
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getLemm(String str) {
        return getInstance().lemms.get(str);
    }
}
