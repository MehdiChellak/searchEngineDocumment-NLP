package TLL;


import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Chi2 {
    Map<String, Map<String, Integer>> bigMap;
    Map<String,String> termClass = new HashMap<>();
    Map<String, Integer> nbrDocsInclass;
    private int NbrDocs;
    Nlp nlp;

    public Chi2() throws FileNotFoundException {

        GetMapFromFiles t = new GetMapFromFiles();
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\القضاء العسكري",1);
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\تنفيذ الدستور",2);
        t.addCorpus("C:\\Users\\ASUS\\Desktop\\text mining looqman\\Document\\حماية المستهلك",3);
        bigMap = t.getBigMap();
        nbrDocsInclass = t.getClassNumber();
        t.printBigMap();
        somme(nbrDocsInclass);
        String pathStopWords ="C:\\Users\\ASUS\\IdeaProjects\\searchEngineDocumment\\untitled1\\asw.txt";
        nlp = new Nlp(pathStopWords);
    }

    public void boucle(String mots)
    {
        float chi2Value=0;
        float maxValueChi2 = 0;
        String classOfMaxValue = null;
        boolean firstTime;

        for(String term : mots.split(" "))
        {
            firstTime = true;
            term = nlp.arabicStemming(term);
            for (String className : nbrDocsInclass.keySet())
            {
               chi2Value = X2(term,className);
               System.out.println(className+" = "+ chi2Value);
               if(chi2Value>maxValueChi2 || firstTime)
               {
                   maxValueChi2 = chi2Value;
                   classOfMaxValue = className;
                   firstTime = false;
               }
            }
            termClass.put(term,classOfMaxValue);
            System.out.println(term+ " = "+classOfMaxValue);
        }
    }

    private float X2(String term, String className)
    {
        int D=0;
        int A=0;
        int B =0;
        for (String doc : bigMap.keySet())
        {

            if(doc.substring(0,2).equals(className))
            {
                if(bigMap.get(doc).containsKey(term))
                {
                    A++;
                }
            }else
            {
                if(bigMap.get(doc).containsKey(term))
                {
                    A++;
                }else
                {
                    D++;
                }
            }
        }
        return calculX2(A,B,nbrDocsInclass.get(className)-A,D);
    }

    private float calculX2(int a, int b, int c, int d) {
    return (float) ((this.NbrDocs*Math.pow(a*d -c*b,2))/((a+b)*(a+c)*(d+b)*(d+c)));
    }
    private void somme(Map<String, Integer> nbrDocsInclass)
    {
        int somme =0;
        for (int values : nbrDocsInclass.values())
            somme += values;
        this.NbrDocs =  somme;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Chi2 chi2 = new Chi2();
        String motsrq= "لعسكريون الجدد المنتخبة بالاقتراع الحر";
        chi2.boucle(motsrq);
        chi2.printTermValue();
    }

    private void printTermValue() {
        System.out.println(termClass.entrySet());
    }
}