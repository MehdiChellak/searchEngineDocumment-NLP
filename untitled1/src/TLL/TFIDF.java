package TLL;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class TFIDF {
    private Map<String, Map<String, Integer>> map;
    private Map<String, Map<String, Float>> tfIdfMap = new HashMap<>();
    private Map<String, Float> idfMap = new HashMap<>();
    private int numberOfdocs = 0;

    public TFIDF() throws FileNotFoundException {
        GetMapFromFiles reader = new GetMapFromFiles();
        map = reader.getBigMap();
        this.numberOfdocs = map.size();
        this.tfidf();
    }

    public void tfidf() throws FileNotFoundException {
        for (String documment : map.keySet())
        {
            calculeTf(documment,map.get(documment));
        }
        printTfidf();
    }

    private void calculeTf(String doc,Map<String, Integer> sousMap) throws FileNotFoundException {
        int wcd =0;
        Map<String, Float> mapFloat = new HashMap<>();
        for (int tt: sousMap.values()) {
            wcd += tt;
        }
        for (String key : sousMap.keySet())
        {
            mapFloat.put(key, ((float)sousMap.get(key)/ wcd)*idfCalculation(key));
        }
        this.tfIdfMap.put(doc,mapFloat);
    }

    public float idfCalculation(String docword) throws FileNotFoundException {
        if(tfIdfMap.containsKey(docword))
            return idfMap.get(docword);
        int numberOfapperance = 0;
        // nombre apperance
        for (String word : map.keySet()) {
            if (map.get(word).containsKey(docword)) {
                numberOfapperance++;
            }
        }
        float idfvalue = (float) Math.log10(numberOfdocs/numberOfapperance);
            idfMap.put(docword,idfvalue);
            return idfvalue;
    }

    public Map<String, Map<String, Float>> getTfIdfMap() {
        return tfIdfMap;
    }

    public  void printTfidf()
    {
        System.out.println("tfdif"+tfIdfMap.entrySet());
        System.out.println("idf"+idfMap.entrySet());
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        TFIDF tt = new TFIDF();
    }
}

