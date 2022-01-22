package CHI2;
import nlp.Nlp;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chi2 {
    Map<String, Map<String, Integer>> bigMap;
    // le fait que j'utilise cette instanciation car hash map ne support pas la supression dans la boucle il genere des problemes
    Map<String, Float> termX2 = new ConcurrentHashMap<>();
    Map<String, Integer> nbrDocsInclass;
    private int NbrDocs;
    Nlp nlp;
    private float maxX2 = 0;

    public Chi2() throws IOException, ClassNotFoundException {

        GetMapFromFiles t = new GetMapFromFiles();
        bigMap = t.getMapfromSerializationBigMap();
        nbrDocsInclass = t.getMapfromSerializationNumberPerClass();

        //System.out.println("yoyo : "+bigMap.entrySet());
        somme(nbrDocsInclass);

        nlp = new Nlp();
    }

    public void boucle()
    {
            float chi2Value=0;
            float maxValueChi2 = 0;
            boolean firstTime;

            for(String doc : bigMap.keySet()) {
                for (String term : bigMap.get(doc).keySet()) {
                    firstTime = true;
                    if(!termX2.containsKey(term)) {
                        for (String className : nbrDocsInclass.keySet()) {
                            chi2Value = X2(term, className);
                            if (chi2Value > maxValueChi2 || firstTime) {
                                maxValueChi2 = chi2Value;
                                firstTime = false;
                            }
                        }

                        this.maxX2 += maxValueChi2;
                        termX2.put(term, maxValueChi2);
                    }
                }
            }
    }

    private float X2(String term, String className)
    {
        int D=0;
        int A=0;
        int B =0;
        for (String doc : bigMap.keySet())
        {
            // cas le term appartient a la class XX
            if(doc.substring(0,2).equals(className))
            {
                if(bigMap.get(doc).containsKey(term))
                {
                    A++;
                }
            }else
            {
                // cas le term n'appartient pas a la class XX mais il appartient mais il appartient a le documment qui appartient a d 'autre class
                if(bigMap.get(doc).containsKey(term))
                {
                    B++;
                }
                // le cas n'appartient ni a la classe ni  a la document current ni a la class de le documment actuelle
                else
                {
                    D++;
                }
            }
        }
        return calculX2(A,B,nbrDocsInclass.get(className)-A,D);
    }

    private float calculX2(int a, int b, int c, int d)
    {
        return (float) ((this.NbrDocs*Math.pow(a*d -c*b,2))/((a+b)*(a+c)*(d+b)*(d+c)));
    }
    private void somme(Map<String, Integer> nbrDocsInclass)
    {
        int somme =0;
        for (int values : nbrDocsInclass.values())
            somme += values;
        this.NbrDocs =  somme;
    }

    public void threshold()
    {
        // calcule de thershold
        float moyen = (float) ((this.maxX2/this.termX2.size())*0.5);
         for (Iterator<String> keys = termX2.keySet().iterator(); keys.hasNext();) {
            String key = keys.next();
            if(termX2.get(key)<moyen)
                termX2.remove(key);
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Chi2 chi2 = new Chi2();
        chi2.boucle();
        chi2.sizeTermX2();
        chi2.threshold();
        chi2.sizeTermX2();
        //chi2.printTermX2();
    }
    private void sizeTermX2()
    {
        System.out.println("la taille est : "+termX2.size());
    }

    public void printTermX2() {
        System.out.println(termX2.entrySet());
    }
}