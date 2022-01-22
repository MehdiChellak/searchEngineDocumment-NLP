package moteurRecherche;

import nlp.Nlp;
import tfidf.TFIDF;

import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private Map<String, Float> tfidfMotsMap = new HashMap<>();
    private Map<String, Map<String, Float>> tfidfMap;
    private  Map<Float, String> similaire = new HashMap<>();
    private Map<String, Float> idfMap = new HashMap<>();
    private Nlp nlp;

    public Main() throws FileNotFoundException
    {

    }

    public Main(String pathStopWords, String pathCorpus) throws FileNotFoundException {
        TFIDF tfidf = new TFIDF(pathCorpus, pathStopWords);
        this.tfidfMap = tfidf.getTfIdfMap();
        this.idfMap = tfidf.getIdfMap();
        nlp = new Nlp();
    }

    public float vecteurNorme (Map<String, Float> map)
    {
        float norme=0;
        for (Float number : map.values())
        {
            norme += Math.pow(number, 2);
        }
        return (float) Math.sqrt(norme);
    }

    public void cosinus()
    {
        float result = 0;
        for (String doc : tfidfMap.keySet())
        {
            result = produit2Vect(tfidfMap.get(doc), tfidfMotsMap)/vecteurNorme(tfidfMap.get(doc))*vecteurNorme(this.tfidfMotsMap);
            //System.out.println("result ="+result+" "+doc+" = "+produit2Vect(tfidfMap.get(doc), tfidfMotsMap)+"*"+vecteurNorme(tfidfMap.get(doc))+"+"+vecteurNorme(this.tfidfMotsMap));
            similaire.put(result,doc);
        }
        showTop3Max();
    }

    private float produit2Vect(Map<String, Float> docMap, Map<String, Float> tfidfMotsMap)
    {
        float sommeProduits = 0;
        for (String name : tfidfMotsMap.keySet())
        {
            if(docMap.containsKey(name))
            {
                sommeProduits += docMap.get(name)*tfidfMotsMap.get(name);
            }
        }
        return sommeProduits;
    }

    public void createMapForRequestedText(String mots)
    {
        float wcd = 0;
        String stemer = null;
        for (String word : mots.split(" "))
        {
            wcd += 1;
            stemer = nlp.arabicStemming(word);
            if(!nlp.isStopWord(stemer))
                if (tfidfMotsMap.containsKey(stemer))
                    tfidfMotsMap.replace(stemer, tfidfMotsMap.get(stemer) + 1);
                 else
                    tfidfMotsMap.put(stemer, (float) 1);

        }

        for (String key : tfidfMotsMap.keySet())
        {
            if (idfMap.containsKey(key))
            {
                tfidfMotsMap.put(key, tfidfMotsMap.get(key) / wcd*idfMap.get(key));
            }else
                tfidfMotsMap.put(key, (tfidfMotsMap.get(key) / wcd)*0);
        }

        System.out.println("tfidf mots rechercher"+tfidfMotsMap.entrySet());
    }
    public void showTop3Max()
    {
        float max = 0;
        int i =3;
        while (i>0)
        {
            max = 0;
            for(float value : similaire.keySet())
            {
                if(value>max)
                    max = value;
            }
            System.out.println("le documment "+(4-i)+" = "+similaire.get(max)+" degree de :"+max);
            similaire.remove(max);
            i--;
        }
    }
    public void lancerRecherche(String motsRechreche) {
        createMapForRequestedText(motsRechreche);
        cosinus();
        //System.out.println(similaire.entrySet());
    }

    public static void main(String[] args) throws FileNotFoundException {
        String motsRecherche = "مستوى حياة ببقية القارات";
        // path de stop words arabic stop words
        String pathStopWords = "C:\\Users\\ASUS\\IdeaProjects\\searchEngineDocumment\\untitled1\\asw.txt";
        // path vers le corpus des documents
        String pathCorpus = "C:\\Users\\ASUS\\Desktop\\corpus\\secondCorpus";
        // moteur de recherche
        Main m = new Main(pathStopWords, pathCorpus);
        // lancer la recherche
        m.lancerRecherche(motsRecherche);
    }
}
