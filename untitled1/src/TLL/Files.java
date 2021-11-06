package TLL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.tartarus.snowball.ext.englishStemmer;

class GetMapFromFiles {

    public GetMapFromFiles() throws FileNotFoundException {
        this.stopWords();
    }
    private Map<String, Map<String, Integer>> bigMap = new HashMap<>();
    private Map<String, Integer> stopWords = new HashMap<>();
    // put here you corpus (folder of docs path)
    String path = "C:\\Users\\ASUS\\Desktop\\text mining looqman";

    public void printBigMap() {
        System.out.println(bigMap);
    }

    public void readFiles() {
        int i = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
            for (Path file : stream) {

                Map<String, Integer> hmap = this.makeMapForEachFile(file);
                System.out.println(hmap.entrySet());
                bigMap.put("" + file.getFileName(), hmap);
            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
        //System.out.println(bigMap.entrySet());
    }

    public Map<String, Integer> makeMapForEachFile(Path file) throws FileNotFoundException {

        File fichier = new File(path + "\\" + file.getFileName());
        Scanner myReader = new Scanner(fichier);
        Map<String, Integer> hmap = new HashMap<>();
        while (myReader.hasNext()) {
            String word = myReader.next();
            // elimination of stop words and numrical words
            if (!this.isStopWord(word) && !word.matches("[0-9]+")) {
                // get the stemmer of the world
                word = this.englishStemmer(word);
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

    private boolean isStopWord(String word) {
        if (this.stopWords.containsKey(word))
            return true;
        return false;
    }

    public String englishStemmer(String word)
    {
        englishStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(word);
        if (stemmer.stem()){
            return stemmer.getCurrent();
        }
        return word;
    }
    public void stopWords() throws FileNotFoundException {
        File fichier = new File("C:\\Users\\ASUS\\IdeaProjects\\untitled1\\stopWords.txt");
        Scanner myReader = new Scanner(fichier);

        while (myReader.hasNext()) {
            String word = myReader.next();
            this.stopWords.put(word,1);
        }
        myReader.close();
    }

    public static void main(String[] args) throws IOException {
        GetMapFromFiles t = new GetMapFromFiles();
        t.readFiles();
        t.printBigMap();
    }
}