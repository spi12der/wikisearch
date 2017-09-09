package com;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
/*import java.util.Collections;
import java.util.Comparator;*/
import java.util.HashMap;
/*import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;*/
import java.util.Map;

import parse.XMLParser;
import utils.StopWordsRemover;

public class Indexing 
{
	public static Map<String,Map<Integer,Integer> > indexTable=new HashMap<String,Map<Integer,Integer> >();
	//public static Map<String,Integer> freqMap=new HashMap<String,Integer>();
	
	public static void main(String[] args) 
	{
		long start=System.currentTimeMillis();
		//String src="/home/rohit/IIIT/Sem3/IRE/wiki-search-small.xml";
		//String des="/home/rohit/IIIT/Sem3/IRE/index.txt";
		String src=args[0];
		String des=args[1];
		XMLParser obj=new XMLParser();
		obj.parse(src);
		indexTable=StopWordsRemover.removeStopWords(indexTable);
		//printIndex();
		saveToFile(des);
		System.out.println("Word Count "+indexTable.size());
		System.out.println("Page Count "+XMLParser.pageId);
		long end=System.currentTimeMillis();
		System.out.println((end-start)/1000 + "sec");
	}
	
	public static void saveToFile(String des)
	{
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(des))) {
            os.writeObject(indexTable);
        }
        catch (Exception e) 
        {
			e.printStackTrace();
		}
    }
	
	public static void printIndex()
	{
		for (Map.Entry<String, Map<Integer,Integer> > entry : indexTable.entrySet())
		{
			System.out.print(entry.getKey()+" : ");
			for (Map.Entry<Integer,Integer> iEntry : entry.getValue().entrySet())
				System.out.print("\t"+iEntry.getKey()+"-->"+iEntry.getValue());
			System.out.println();
		}	
	}
	
	public static void updateIndexTable(String word,int docId)
	{
		Map<Integer,Integer> temp;
		//int c=1;
		if(indexTable.containsKey(word))
		{
			temp=indexTable.get(word);
			//c+=freqMap.get(word);
		}	
		else
			temp=new HashMap<Integer,Integer>();
		if(temp.containsKey(docId))
			temp.put(docId, temp.get(docId)+1);
		else
			temp.put(docId, 1);
		indexTable.put(word, temp);
		//freqMap.put(word, c);
	}
	
	/*public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
	{
	    List<Map.Entry<K, V>> list =
	        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	            return (o1.getValue()).compareTo( o2.getValue() );
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}*/
}
