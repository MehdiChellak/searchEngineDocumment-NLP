package Kmeans;

import naifbais.Serialization_naif_baise;
import org.python.antlr.op.In;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class KmeansModel {
	public static final File Dir = new File("DocumentsTs");
	public final int K = 3;
	public Map<String, Map<String, Integer>>getCorpus() throws IOException, ClassNotFoundException {
		Serialization_naif_baise snv = new Serialization_naif_baise();
		Map<String, Map<String, Integer>> corpus = snv.readBigMap();
		return corpus;
	}

	private  Map<String, Integer> clusters = new HashMap<>();
	Serialization_naif_baise snv = new Serialization_naif_baise();
	private Map<String, Map<String, Integer>> corpus;
	private Map<Integer,Map<String, Integer>> centres=new HashMap<>();
	private Map<Integer,Map<String, Float>> centresFloat=new HashMap<>();
	public KmeansModel() throws IOException, ClassNotFoundException {
		corpus = snv.readBigMap();
		Map<String, Integer> toto = snv.readNumberPerClass();
		System.out.println(toto.entrySet());
	}
	static int i;
	public void initClusters( int k) {
		Random rn = new Random();
		for(String soso : corpus.keySet())
		{
			int answer = rn.nextInt(k) + 1;
			clusters.put(soso, answer);
		}
	}
	public Map<String,Integer> somme(Map<String,Integer> centres, Map<String,Integer> document, int numberDoc)
	{
		if(numberDoc == 1)
		{
			return document;
		}
		for(String term : document.keySet())
		{
			if(centres.containsKey(term))
			{
				centres.replace(term,centres.get(term)+document.get(term));
			}else
			{
				centres.put(term,document.get(term));
			}
		}
		return centres;
	}
	public  void centreGravite(int k)
	{
		int numberDoc = 0;
		for(int i = 1; i<=k;i++)
		{
			numberDoc = 0;
			for(String doc : this.clusters.keySet())
			{
				if(clusters.get(doc)==i)
				{
					numberDoc++;

					if(numberDoc==1)
						centres.put(i,this.somme(centres.get(i),corpus.get(doc),numberDoc));
					else
						centres.replace(i,this.somme(centres.get(i),corpus.get(doc),numberDoc));
				}
			}
			devise(numberDoc,i);
		}

	}
	public  int centerTerme(String terme){
		//HashMap<String,Integer> tt= new HashMap<String,Integer>() ;
		int val=0,i=0 ;
		for (String doc : corpus.keySet()) {
			if(corpus.get(doc).containsKey(terme)) {
				i++ ;
			}
		}
		//tt.put(terme, val/i) ;
		return i;
	}

	private void devise(int numberDoc,  int i) {
		Map<String, Float> mapF = new HashMap<>();
		System.out.println(numberDoc);
		for (String mots : centres.get(i).keySet())
		{
			mapF.put(mots, (float) (centres.get(i).get(mots)/(numberDoc+1) ));

			centresFloat.put(i,mapF);
		}
	}
	private float distance(Map<String, Float> centre, Map<String, Integer> doc) {
		float d=0;
		for (String entry : centre.keySet())
		{
			if(centre.containsKey(entry) && doc.containsKey(entry))
			{
				d= (float) (d+Math.pow((centre.get(entry)-doc.get(entry)),2));
			}
		}
		return (float) Math.sqrt(d);
	}

	private void distance(int k) {
		float distance = 0;
		int nvclasse = 0;
		float distanceMin = 0;
		boolean choix = true;
		for(String doc : clusters.keySet())
		{
			for (int i = 1 ; i < k ; i++)
			{
				distance = distance(centresFloat.get(i),this.corpus.get(doc));
				if(distanceMin>distance || choix == true)
				{
					choix=false;
					distanceMin = distance;
					nvclasse = i;
				}
			}
		clusters.replace(doc,nvclasse);

		}
	}




	public static void main(String[] args) throws IOException, ClassNotFoundException {

		int k = 2;
		int fois = 6;
		KmeansModel kmeans = new KmeansModel();
		kmeans.initClusters(k);

			kmeans.print();
			kmeans.centreGravite(k);
			kmeans.distance(k);
			kmeans.print();


		//System.out.println("centres"+kmeans.centres.entrySet());
	}



	public void print()
	{
		//System.out.println(corpus.entrySet());
		System.out.println(clusters.values());

	}
}
//[-1, -1, 0, -1, -1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1]
//