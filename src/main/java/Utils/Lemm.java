package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class Lemm {
    final private HashMap<String, String> lemms = new HashMap<>();

    final private static Lemm ourInstance = new Lemm();

    private static Lemm getInstance() {
        return ourInstance;
    }

    /**
     * Constuctor
     */
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
        if (dir != null) {
            // Load dictionary in map
            for (File dic : dir.listFiles()) {
                try (BufferedReader br = new BufferedReader(new FileReader(dic))) {
                    for (String line; (line = br.readLine()) != null; ) {
                        String[] words = line.split("\t");
                        lemms.put(words[0], words[1]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println("No dictionaries loaded!");
        }
    }

    /**
     * @param str Source word of lemme
     * @return lemme version of entry or entry if it is not found
     */
    public static String getLemm(String str) {
        String res = getInstance().lemms.get(str);
        if (res == null)
            return str;
        return res;
    }

    /**
     * @param str Word to detect
     * @return 1 if the word is not a named entity, 6 if it is
     */
    public static Double namedEntity(String str) {
        String res = getInstance().lemms.get(str);
        if (res == null)
            return 6D;
        else
            return 1D;
    }
}
