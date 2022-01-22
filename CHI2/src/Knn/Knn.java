package Knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Knn {
	public static double produit_cart(Map<String, Integer> doc1,HashMap<String, Integer> doc2) {
	    double d=0.0;
		for (String entry : doc1.keySet()) 
		{ 
		   if(doc1.containsKey(entry) && doc2.containsKey(entry)) 
		   {
			   d=d+doc1.get(entry)*doc2.get(entry);
		   }
		}
		return d;
	}

	public static String KNN_Classifieur(HashMap<String, HashMap<String,Integer>> docs,HashMap<String,Integer> doc,int n)
	{
		 int d=0,i=0,j=0 ;
				 //lenght;
		 Double m;
		 String class_document1;
	     Map<String,Integer> doc1;
	     Map<String,Double> resultat= new HashMap<String, Double>(); 
	     Map<String,Double> tree= new HashMap<String, Double>(); 
	     double class1 = 0,class2=0;
		 for (String entry : docs.keySet())
		 {
			 doc1=docs.get(entry);
			 m=produit_cart(doc1, doc);
			 resultat.put(entry,m);
		 }

		 tree=sortByValue(resultat);
		 
		 //System.out.println(tree);
		 for(String entry : tree.keySet()) {
		 	if(d<n) {
				 d++;
				 String firstValue = "c1";
				 String secondValue = "c2";
				 if(entry.substring(0,2).equals(firstValue)) {
					class1+=tree.get(entry);
					i++;
		 			}
				 if(entry.substring(0,2).equals(secondValue)){
					 class2+=tree.get(entry);
					 j++;
				 }
		 }
		 }
		 if(i!=0 && j!=0) {
			 class1=class1/i ;
			 class2=class2/j ;
		 }
		 if(class1<class2) {
			 class_document1="c2";
		 }else {
			 class_document1="c1";
		 }
		 return class_document1;
	}
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
	    list.sort(Entry.comparingByValue());
	    Collections.reverse(list);
	    Map<K, V> result = new LinkedHashMap<>();
	    
	    for (Entry<K, V> entry : list) {
	        result.put(entry.getKey(), entry.getValue());
	    }

	    return result;
	}
}

