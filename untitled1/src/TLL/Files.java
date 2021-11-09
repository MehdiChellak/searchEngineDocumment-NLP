package TLL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.tartarus.snowball.ext.arabicStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import safar.basic.morphology.stemmer.impl.KhojaStemmer;

class GetMapFromFiles {
    Nlp nlp;

    public GetMapFromFiles() throws FileNotFoundException {
        nlp = new Nlp();
        this.readFiles();
    }
    private Map<String, Map<String, Integer>> bigMap = new HashMap<>();

    // put here you corpus (folder of docs path)
    String path = "C:\\Users\\ASUS\\Desktop\\corpus\\secondCorpus";

    public void printBigMap() {System.out.println(bigMap);}

    public void readFiles() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
            for (Path file : stream) {
                Map<String, Integer> hmap = this.makeMapForEachFile(file);
                bigMap.put("" + file.getFileName(), hmap);
            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
    }

    public Map<String, Integer> makeMapForEachFile(Path file) throws FileNotFoundException {
        File fichier = new File(path + "\\" + file.getFileName());
        Scanner myReader = new Scanner(fichier);
        Map<String, Integer> hmap = new HashMap<>();
        while (myReader.hasNext()) {
            String word = myReader.next().toLowerCase();
            word = this.nlp.arabicStemming(word);
            // elimination of stop words and numrical words && !word.matches("[0-9]+%")
            if (!this.nlp.isStopWord(word) ) {
                // get the stemmer of the world
                if (hmap.containsKey(word)) {
                    hmap.replace(word, hmap.get(word) + 1);
                } else {
                    hmap.put(word, 1);
                }
            }
        }
        myReader.close();
        return hmap;
    }

    public Map<String, Map<String, Integer>> getBigMap() {
        return bigMap;
    }

    public static void main(String[] args) throws IOException {
        GetMapFromFiles t = new GetMapFromFiles();
        t.printBigMap();
    }
}