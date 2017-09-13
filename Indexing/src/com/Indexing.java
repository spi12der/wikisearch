package com;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import parse.WriteDict;
import parse.XMLParser;

public class Indexing 
{
	public static Map<String,Map<Integer,Map<Integer,Object> > > indexTable;
	public static Map<Integer,Double> docMap;
	public static String src;
	public static String rootPath;
	
	static
	{
		indexTable=new TreeMap<String,Map<Integer,Map<Integer,Object> > >();
		docMap=new TreeMap<Integer,Double>();
	}
	
	public static void main(String[] args) 
	{
		long start=System.currentTimeMillis();
		//src=args[0];
		//rootPath=args[1];
		src="/home/rohit/IIIT/Sem3/IRE/wiki-search-small.xml";
		rootPath="/home/rohit/IIIT/Sem3/IRE";
		try
		{
			XMLParser obj=new XMLParser();
			obj.parse(src);
			WriteDict.writeDictToFile();
			System.out.println("File Merging started");
			MergeList ml=new MergeList();
			ml.mergeIndexes(rootPath+"/Part", rootPath+"/Index");
			System.out.println("Multilevel indexing started");
			Multilevel mil=new Multilevel();
			mil.createMultilevel('A', rootPath+"/Index");
			File f=new File(rootPath+"/Index/Doc");
			f.mkdir();
			System.out.println("Multilevel index for document started");
			mil.makeDocLevel(rootPath+"/doc.txt",rootPath+"/Index/Doc/A");
			mil.createMultilevelDoc('A', rootPath+"/Index/Doc");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.println("Page Count "+XMLParser.pageId);
		long end=System.currentTimeMillis();
		System.out.println((end-start)/1000 + "sec");
	}
	
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
			cat.put(category, (long)cat.get(category)+1);
		else
			cat.put(category, 1L);
		doc.put(docId, cat);
		indexTable.put(word, doc);
	}
}
