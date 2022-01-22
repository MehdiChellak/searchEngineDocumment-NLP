package tfidf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import nlp.Nlp;

class GetMapFromFiles {
    private Nlp nlp;
    private String pathCorpus;

    public GetMapFromFiles() throws FileNotFoundException {

    }
    private Map<String, Map<String, Integer>> bigMap = new HashMap<>();

    public GetMapFromFiles(String pathCorpus) throws FileNotFoundException {
        this.pathCorpus = pathCorpus;
        nlp = new Nlp();
        this.readFiles();
        System.out.println("Mehdi Chellak");
    }

    public void printBigMap() {System.out.println(bigMap);}

    public void readFiles() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(pathCorpus))) {
            for (Path file : stream) {
                Map<String, Integer> hmap = this.makeMapForEachFile(file);
                bigMap.put("" + file.getFileName(), hmap);
            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
    }

    public Map<String, Integer> makeMapForEachFile(Path file) throws FileNotFoundException {
        File fichier = new File(pathCorpus + "\\" + file.getFileName());
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