package Utils;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if(jarFile.isFile()) {  // Run with JAR file
            final JarFile jar;
            try {
                jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while(entries.hasMoreElements()) {
                    final JarEntry entry = entries.nextElement();
                    final String name = entry.getName();
                    if (name.startsWith("dico" + "/")) { //filter according to the path
                        InputStream dic = jar.getInputStream(entry);
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(dic, "UTF-8"))) {
                            for (String line; (line = br.readLine()) != null; ) {
                                String[] words = line.split("\t");
                                lemms.put(words[0], words[1]);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                jar.close();

            } catch (IOException e) {
                e.printStackTrace();
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
