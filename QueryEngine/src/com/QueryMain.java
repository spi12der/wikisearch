package com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class QueryMain 
{
	static char highLevel;
	static String indexPath;
	static String startFile;
	
	static
	{
		highLevel='B';
		indexPath="/home/IRE";
		startFile="1.txt";
	}
	
	public static void main(String[] args) 
	{
		String query="rohit";
		String tokens[]=query.split(" ");
		List<ResultNode> arr;
		QueryMain qm=new QueryMain();
		try
		{
			if(tokens[0].charAt(tokens[0].length()-1)==':')
				arr=qm.termQuery(tokens);
			else
				arr=qm.multiWordQuery(tokens);
			Collections.sort(arr);
			QueryUtil qu=new QueryUtil();
			if(arr.size()==0)
				System.out.println("No Results Found");
			else
			{
				System.out.println("\tDOC_ID\tTITLE");
				System.out.println("----------------------------");
				for(int i=0;i<10 && i<arr.size();i++)
				{
					ResultNode t=arr.get(i);
					System.out.println((i+1)+".\t"+t.getDocId()+"\t"+qu.getDocTitle(t.getDocId()));
				}	
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public List<ResultNode> termQuery(String tokens[]) throws IOException
	{
		List<ResultNode> arr=null;
		QueryUtil qu=new QueryUtil();
		Map<Integer,ResultNode> result=new TreeMap<Integer,ResultNode>();
		Map<Integer,List<String> > termMap=new TreeMap<Integer,List<String> >();
		for(int i=0;i<tokens.length;i++)
		{
			if(tokens[i].charAt(tokens[i].length()-1)==':')
			{
				int term=qu.getCategory(tokens[i]);
				i++;
				List<String> wordList=new ArrayList<String>();
				while(i<tokens.length && !(tokens[i].charAt(tokens[i].length()-1)==':'))
				{
					wordList.add(tokens[i]);
					i++;
				}
				termMap.put(term, wordList);
				i--;
			}
		}
		for(Map.Entry<Integer, List<String> > entry:termMap.entrySet())
		{
			for(String s:entry.getValue())
			{
				Map<Integer,ResultNode> postingMap=qu.makeTermMap(qu.getWordString(highLevel, s,startFile),entry.getKey());
				result=qu.mergeMap(result, postingMap);
			}	
		}
		arr=(List<ResultNode>) result.values();
		return arr;
	}
	
	public List<ResultNode> multiWordQuery(String tokens[]) throws IOException
	{
		List<ResultNode> arr=null;
		QueryUtil qu=new QueryUtil();
		Map<Integer,ResultNode> result=new TreeMap<Integer,ResultNode>();
		for(String token:tokens)
		{
			Map<Integer,ResultNode> postingMap=qu.makeMultiMap(qu.getWordString(highLevel, token,startFile));
			result=qu.mergeMap(result, postingMap);
		}
		arr=(List<ResultNode>) result.values();
		return arr;
	}
	
}