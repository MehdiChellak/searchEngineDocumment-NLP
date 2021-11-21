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
    private Nlp nlp;
    private String pathCorpus;


    public GetMapFromFiles() throws FileNotFoundException {
        String pathStopWords ="C:\\Users\\ASUS\\IdeaProjects\\searchEngineDocumment\\untitled1\\asw.txt";
        nlp = new Nlp(pathStopWords);
    }
    private Map<String, Map<String, Integer>> bigMap = new HashMap<>();
    private Map<String, Integer> classNumber = new HashMap<>();

    public GetMapFromFiles(String pathCorpus, String pathStopWords) throws FileNotFoundException {
        this.pathCorpus = pathCorpus;
        nlp = new Nlp(pathStopWords);
    }

    public void printBigMap() {System.out.println(bigMap);}

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

    public Map<String, Integer> getClassNumber() {
        return classNumber;
    }

    public void addCorpus(String path, int i) {
        this.pathCorpus = path;
        int documentCounter = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
            for (Path file : stream) {
                documentCounter++;
                Map<String, Integer> hmap = this.makeMapForEachFile(file);
                bigMap.put("c"+i+" "+file.getFileName(), hmap);

            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
        classNumber.put("c"+i,documentCounter);
    }

    public static void main(String[] args) throws IOException {
        GetMapFromFiles t = new GetMapFromFiles();
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\القضاء العسكري",1);
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\تنفيذ الدستور",2);
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\حماية المستهلك",3);
        t.printBigMap();
        t.printNbrDocsClass();
    }

    private void printNbrDocsClass() {
        System.out.println(classNumber.entrySet());
    }
}