package TLL;
import org.python.antlr.ast.Str;
import org.tartarus.snowball.ext.arabicStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import safar.basic.morphology.stemmer.impl.KhojaStemmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Nlp {

    private Map<String, Integer> stopWords = new HashMap<>();
    private String pathStopWords;
    public Nlp(String pathStopWords) throws FileNotFoundException {
        this.pathStopWords = pathStopWords;
        this.stopWords();

    }


    public String englishStemmerPorter(String word)
    {
        englishStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(word);
        if (stemmer.stem()){
            return stemmer.getCurrent();
        }
        return word;
    }
    public String arabicStemmerPorter(String word)
    {
        arabicStemmer stemmer = new arabicStemmer();
        stemmer.setCurrent(word);
        if (stemmer.stem())
        {
            return stemmer.getCurrent();
        }
        return word;
    }

    public String arabicStemming(String s) {
        KhojaStemmer MyStemmer =new  KhojaStemmer();
        s=MyStemmer.stem(s).get(0).getListStemmerAnalysis().get(0).getMorpheme();
        return s;
    }

    public boolean isStopWord(String word) {
        if (this.stopWords.containsKey(word))
            return true;
        return false;
    }

    public String getPathStopWords() {
        return pathStopWords;
    }

    public void setPathStopWords(String pathStopWords) {
        this.pathStopWords = pathStopWords;
    }

    public void stopWords() throws FileNotFoundException {
        String path =this.pathStopWords;
        File fichier = new File(path);
        Scanner myReader = new Scanner(fichier);
        String word=null;
        while (myReader.hasNext()) {
            word = myReader.next();
            this.stopWords.put(word,1);
        }
        myReader.close();
    }


}
