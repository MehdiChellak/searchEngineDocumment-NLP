package Knn;

import naifbais.Serialization_naif_baise;
import nlp.Nlp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Knn knn = new Knn();
        Nlp nlp = new Nlp();
        String rq = "يوجه وكيل الملك فيما يتعلق باختصاصه إلى الوكيل العام للملك لدى المحكمة العسكرية";
        rq ="جرائم الحق العام المرتكبة من قبل العسكريين وشبه العسكريين سواء كانوا فاعلين أصليين أو مساهمين أو مشاركين";
        //rq="وكذا مصدر المنتوج أو السلعة وتاريخ";
        rq="في حالة رفض الشروط الجديدة المتعلقة بالسعر أو التسديد المقترحة عند تجديد العقد";
        Serialization_naif_baise snb = new Serialization_naif_baise();
        HashMap<String,Integer> mapRq = new HashMap<>();
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
        HashMap<String, HashMap<String,Integer>> docs = new HashMap<>();
        System.out.println(snb.readBigMap().entrySet());
        for(String soso : snb.readBigMap().keySet())
        {
            docs.put(soso, (HashMap<String, Integer>) snb.readBigMap().get(soso));
        }

        String classe  = Knn.KNN_Classifieur(docs, mapRq,2) ;
        System.out.println(rq);
        System.out.print("la classe predites est "+classe);
    }
}
