package CHI2;

import nlp.Nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class GetMapFromFiles {
    private Nlp nlp;
    private String pathCorpus;
    private Serialization saver;
    private Map<String, Map<String, Integer>> bigMap = new HashMap<>();
    private Map<String, Integer> classNumber = new HashMap<>();


    public GetMapFromFiles() throws FileNotFoundException {
        nlp = new Nlp();
        saver = new Serialization();
    }

    public void printBigMap() {System.out.println(bigMap);}

    public Map<String, Integer> makeMapForEachFile(Path file) throws FileNotFoundException {
        File fichier = new File(pathCorpus + "\\" + file.getFileName());
        Scanner myReader = new Scanner(fichier);
        Map<String, Integer> hmap = new HashMap<>();
        while (myReader.hasNext()) {
            String word = myReader.next();


            // elimination of stop words and numrical words && !word.matches("[0-9]+%")
            if (!this.nlp.isStopWord(word) && word != null) {
                // get the stemmer of the world
                word = this.nlp.arabicStemming(word);
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        GetMapFromFiles t = new GetMapFromFiles();
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\القضاء العسكري",1);
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\تنفيذ الدستور",2);
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\حماية المستهلك",3);
        //t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\afrique",4);
        //t.printBigMap();
        t.printNbrDocsClass();
        t.launcheSerialisation();
        Map<String, Map<String, Integer>> map = t.getMapfromSerializationBigMap();
        System.out.println(map.entrySet());
    }

    private void printNbrDocsClass() {
        System.out.println(classNumber.entrySet());
    }

    /// ---------------------------------------------- serialization methods -----------------------------------------
    public void launcheSerialisation() throws IOException {
        System.out.println("Serialization ... ");
        saver.saveBigMap(bigMap);
        saver.saveNumberPerClass(classNumber);
        System.out.println("Done ..!");
    }
    public Map<String, Map<String, Integer>> getMapfromSerializationBigMap() throws IOException, ClassNotFoundException {
        return saver.readBigMap();
    }
    public Map<String, Integer> getMapfromSerializationNumberPerClass() throws IOException, ClassNotFoundException {
        return saver.readNumberPerClass();
    }
}