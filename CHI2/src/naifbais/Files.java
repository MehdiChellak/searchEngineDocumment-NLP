package naifbais;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import nlp.Nlp;

public class Files {
    private String pathCorpus = "";
    private Nlp nlp;
    private Serialization_naif_baise snb;
    private Map<String, Map<String, Integer>> bigMap = new HashMap<>();
    private Map<String, Integer> classNumber = new HashMap<>();
    public Files() throws FileNotFoundException {
        nlp = new Nlp();
        snb = new Serialization_naif_baise();
    }

    public void addCorpus(String path, int i) {
        this.pathCorpus = path;
        int documentCounter = 0;

        try (DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(Paths.get(path))) {
            for (Path file : stream) {
                documentCounter++;
                Map<String, Integer> hmap = this.makeMapForEachFile(file);
                bigMap.put("c"+i+file.getFileName(), hmap);

            }
        } catch (IOException | DirectoryIteratorException ex) {
            System.err.println(ex);
        }
        classNumber.put("c"+i,documentCounter);
    }



    public Map<String, Integer> makeMapForEachFile(Path file) throws FileNotFoundException {
        File fichier = new File(pathCorpus + "\\" + file.getFileName());
        Scanner myReader = new Scanner(fichier);
        Map<String, Integer> hmap = new HashMap<>();

        while (myReader.hasNext()) {
            String word = myReader.next();
            if (!nlp.isStopWord(word) && word != null) {
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
    public  void print()
    {
        System.out.println(bigMap.entrySet());
        System.out.println(classNumber.entrySet());
    }
    public void lanuchSerialization() throws IOException {
        snb.saveBigMap(this.bigMap);
        snb.saveNumberPerClass(this.classNumber);
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Files t = new Files();
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\القضاء العسكري",1);
        //t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\تنفيذ الدستور",2);
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\حماية المستهلك",2);
        //t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\afrique",4);
        t.lanuchSerialization();

        Map<String, Integer> map = t.getMapfromSerializationNumberPerClass();
        System.out.println(map.entrySet());
    }

    public Map<String, Map<String, Integer>> getMapfromSerializationBigMap() throws IOException, ClassNotFoundException {
        return snb.readBigMap();
    }
    public Map<String, Integer> getMapfromSerializationNumberPerClass() throws IOException, ClassNotFoundException {
        return snb.readNumberPerClass();
    }
}
