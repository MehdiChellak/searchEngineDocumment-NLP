package MoteurRecherche;

import TLL.Nlp;
import TLL.TFIDF;

import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private Map<String, Float> tfMotsMap = new HashMap<>();
    private Map<String, Map<String, Float>> tfidfMap;
    private  Map<Float, String> similaire = new HashMap<>();
    private Nlp nlp;

    public Main() throws FileNotFoundException
    {
        TFIDF tfidf = new TFIDF();
        this.tfidfMap = tfidf.getTfIdfMap();
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
            result = produit2Vect(tfidfMap.get(doc), tfMotsMap)/vecteurNorme(tfidfMap.get(doc))*vecteurNorme(this.tfMotsMap);
            //System.out.println("result ="+result+" "+doc+" = "+produit2Vect(tfidfMap.get(doc), tfMotsMap)+"*"+vecteurNorme(tfidfMap.get(doc))+"+"+vecteurNorme(this.tfMotsMap));
            similaire.put(result,doc);
        }
        showTop3Max();
    }

    private float produit2Vect(Map<String, Float> stringFloatMap, Map<String, Float> tfMotsMap)
    {
        float sommeProduits = 0;
        for (String name : tfMotsMap.keySet())
        {
            if(stringFloatMap.containsKey(name))
            {
                sommeProduits += stringFloatMap.get(name)*tfMotsMap.get(name);
            }
        }
        return sommeProduits;
    }

    public void createMapForRequestedText(String mots)
    {
        float wcd = 0;
        String stemer = null;
        for (String word : mots.toLowerCase().split(" "))
        {
            wcd += 1;
            stemer = nlp.arabicStemming(word);
            if(!nlp.isStopWord(stemer))
                if (tfMotsMap.containsKey(stemer))
                    tfMotsMap.replace(stemer, tfMotsMap.get(stemer) + 1);
                 else
                    tfMotsMap.put(stemer, (float) 1);

        }
        for (String key : tfMotsMap.keySet())
        {
            tfMotsMap.put(key, (float) tfMotsMap.get(key)/ wcd);
        }
        System.out.println("mots rechrcher"+tfMotsMap.entrySet());
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
            System.out.println("le documment "+(4-i)+" = "+similaire.get(max));
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
        String motsRechreche = "مجموعات من الجزر كالجزر التابعة لدولة";
        Main m = new Main();

        m.lancerRecherche(motsRechreche);

    }
}
