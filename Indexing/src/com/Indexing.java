package com;

import java.util.Map;
import java.util.TreeMap;

import parse.WriteDict;
import parse.XMLParser;

public class Indexing 
{
	public static Map<String,Map<Integer,Map<Integer,Object> > > indexTable;
	public static Map<Integer,Double> docMap;
	
	static
	{
		indexTable=new TreeMap<String,Map<Integer,Map<Integer,Object> > >();
	}
	
	public static void main(String[] args) 
	{
		long start=System.currentTimeMillis();
		String src="/home/rohit/IIIT/Sem3/IRE/wiki-search-small.xml";
		//String des="/home/rohit/IIIT/Sem3/IRE/index.txt";
		String indexPath="/home/rohit/IIIT/Sem3/IRE/Index";
		/*String src=args[0];
		String des=args[1];*/
		XMLParser obj=new XMLParser();
		obj.parse(src);
		//printIndex();
		//saveToFile(des);
		MergeList ml=new MergeList();
		ml.mergeIndexes(WriteDict.folderPath, indexPath);
		System.out.println("Word Count "+indexTable.size());
		System.out.println("Page Count "+XMLParser.pageId);
		long end=System.currentTimeMillis();
		System.out.println((end-start)/1000 + "sec");
	}
	
	/*public static void saveToFile(String des)
	{
		BufferedWriter bw = null;
		FileWriter fw = null;
        try 
		{
			fw = new FileWriter(des);
			bw = new BufferedWriter(fw);
			for(Map.Entry<String,Map<Integer,Map<Integer,Object> > > entry:indexTable.entrySet())
	        {
				StringBuilder sb=new StringBuilder();
	        	sb.append(entry.getKey()+":");
	        	for(Map.Entry<Integer,Map<Integer,Object> > en:entry.getValue().entrySet())
	        	{
	        		sb.append(en.getKey()+"#");
	        		for(Map.Entry<Integer, Object> e:en.getValue().entrySet())
	        		{
	        			sb.append(e.getKey()+"$"+e.getValue()+",");
	        		}
	        		sb.deleteCharAt(sb.length() - 1);
	        		sb.append(";");
	        	}
	        	sb.deleteCharAt(sb.length() - 1);
	        	sb.append("\n");
	        	bw.write(sb.toString());
	        }
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} 
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
    }*/
	
	public static void updateIndexTable(String word,int docId,int category)
	{
		Map<Integer,Map<Integer,Object> > doc;
		if(indexTable.containsKey(word))
			doc=indexTable.get(word);
		else
			doc=new TreeMap<Integer,Map<Integer,Object> >();
		Map<Integer,Object> cat;
		if(doc.containsKey(docId))
			cat=doc.get(docId);
		else
			cat=new TreeMap<Integer,Object>();
		if(cat.containsKey(category))
			cat.put(category, (int)cat.get(category)+1);
		else
			cat.put(category, 1);
		doc.put(docId, cat);
		indexTable.put(word, doc);
	}
}
