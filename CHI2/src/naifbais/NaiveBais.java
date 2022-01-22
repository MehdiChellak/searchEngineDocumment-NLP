package naifbais;

import nlp.Nlp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NaiveBais {
    private Serialization_naif_baise snb;
    private Map<String, Map<String, Integer>> bigmap;
    private Map<String, Integer> numberPerClass;
    private int nomberMotsNonRepeter = 0;
    private Map<String,Integer> mapNonRep = new HashMap<>();
    private Map<String,Integer> mapRq = new  ConcurrentHashMap<>();
    private int NbrDocs = 0;
    private Nlp nlp;

    public NaiveBais() throws IOException, ClassNotFoundException {
        snb = new Serialization_naif_baise();
         bigmap = snb.readBigMap();
         numberPerClass = snb.readNumberPerClass();
         somme(numberPerClass);
         motsNonRepeter(bigmap);
         nlp = new Nlp();
    }

    private void motsNonRepeter(Map<String, Map<String, Integer>> bigmap) {
        for (String classe : bigmap.keySet())
        {
            for (String word : bigmap.get(classe).keySet())
            {
                if(!mapNonRep.containsKey(word))
                {
                    mapNonRep.put(word,1);
                }
            }
        }
        this.nomberMotsNonRepeter = mapNonRep.size();
    }

    private int numberFilesClasse(String classe) {
        int comp = 0;
        for (String c : bigmap.keySet())
        {
            if (c.substring(0,2).equals(classe))
            {
                comp += bigmap.get(c).size();
            }
        }
        return comp;
    }

    private float calule_proba_term(int nk, int m, int n) {

        return (float)(nk + 1)/(float) (m+n);
    }

    private void somme(Map<String, Integer> nbrDocsInclass)
    {
        int somme =0;
        for (int values : nbrDocsInclass.values())
            somme += values;
        this.NbrDocs =  somme;
    }

    private float claculeProbaClass(String classe) {
        return (float)numberPerClass.get(classe)/(float) this.NbrDocs;
    }

    private void print()
    {
        System.out.println(bigmap.entrySet());
    }

    private void predictClasse(String rq) {

        for (String s : rq.split(" ")) {

            if (!nlp.isStopWord(s)) {
                s = nlp.arabicStemming(s);

                if (mapRq.containsKey(s)) {
                    mapRq.replace(s, mapRq.get(s) + 1);
                } else {
                    mapRq.put(s, 1);
                }
            }
        }
        //System.out.println("map requete "+ mapRq.entrySet());
        float proba = 0;
        float probaMax = 0;
        boolean choix;
        String classeMax = "";
        choix = true;

        for(String classe : numberPerClass.keySet())
        {
            proba = claculeProbaClass(classe);
            for (String word_rq : mapRq.keySet())
            {
                proba *= proba_rq(classe,word_rq,mapRq.get(word_rq));
            }

            if(probaMax<proba || choix == true)
            {
                probaMax = proba;
                classeMax = classe;
                choix = false;
            }
            System.out.println(classe+ " avec une proba de "+proba);
        }
         System.out.println("****** "+classeMax+ " avec une probaMax de "+probaMax+" ******");
        //System.out.println("nomber des docs "+NbrDocs);
    }

    private float proba_rq(String classe, String mots, int version)
    {
        // number de mots dans le cas de class qui mentionnez dans le param
        int n = numberFilesClasse(classe);
        int m = this.nomberMotsNonRepeter;
        int nk = 0;
        for (String nom : bigmap.keySet())
        {
            if(nom.substring(0,2).equals(classe) && bigmap.get(nom).containsKey(mots))
            {
                nk = bigmap.get(nom).get(mots);
            }
        }
        return calule_proba_term(nk,m,n);
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        NaiveBais nv = new NaiveBais();
        // nv.toto();
        String rq = "يوجه وكيل الملك فيما يتعلق باختصاصه إلى الوكيل العام للملك لدى المحكمة العسكرية";
        // String rq ="المحكمة العسكرية بالنظر في الأفعال المنسوبة إلى الأحداث";
        nv.predictClasse(rq);
    }
}
