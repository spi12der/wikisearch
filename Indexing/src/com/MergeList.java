package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.Map;

public class MergeList 
{
	
	public static PriorityQueue<WordNode> pq;
	public static int pageCount;
	
	static
	{
		 pq=new PriorityQueue<WordNode>();
		 pageCount=1;
	}
	
	public List<String> getFileList(String folderPath)
	{
		List<String> fileList=new ArrayList<String>();
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if (listOfFiles[i].isFile()) 
				fileList.add(listOfFiles[i].getName());
		}      
		return fileList;    
	}
	
	public Map<Integer,Map<Integer,Object> > makeMap(String postingList)
	{
		Map<Integer,Map<Integer,Object> > postingMap=new TreeMap<Integer,Map<Integer,Object> >();
		String docs[]=postingList.split(";");
		for(String doc:docs)
		{
			String token[]=doc.split("#");
			String terms[]=token[1].split(",");
			Map<Integer,Object> termMap=new TreeMap<Integer,Object>();
			for(String term:terms)
			{
				String part[]=term.split("-");
				termMap.put(Integer.parseInt(part[0]), part[1]);
			}
			postingMap.put(Integer.parseInt(token[0]), termMap);
		}
		return postingMap;
	}
	
	public Map<Integer,Map<Integer,Object> > mergeMap(Map<Integer,Map<Integer,Object> > a,Map<Integer,Map<Integer,Object> > b)
	{
		for(Map.Entry<Integer,Map<Integer,Object> > entry:a.entrySet())
		{
			/*if(b.containsKey(entry.getKey()))
			{
				Map<Integer,Integer> temp=b.get(entry.getKey());
				for(Map.Entry<Integer,Integer> en:entry.getValue().entrySet())
				{
					int val=en.getValue();
					if(temp.containsKey(en.getKey()))
						val+=temp.get(en.getKey());
					temp.put(en.getKey(),val);
				}
				b.put(entry.getKey(), temp);
			}
			else*/
			b.put(entry.getKey(),entry.getValue());
		}
		return b;
	}
	
	public void addPriorityQueue(BufferedReader bf,int index) throws Exception
	{
		try
		{
			String line;
			if((line = bf.readLine()) != null)
			{
				String tokens[]=line.split(":");
				WordNode wn=new WordNode();
				wn.setWord(tokens[0]);
				wn.setPostingString(tokens[1]);
				wn.setIndex(index);
				pq.add(wn);
			}
		}
		catch (Exception e) 
		{
			throw e;
		}
	}
	
	public void mergeIndexes(String folderPath,String indexPath) throws Exception
	{
		List<String> fileList=getFileList(folderPath);
		BufferedReader bf[]=new BufferedReader[fileList.size()];
		BufferedWriter bw=null;
		try
		{
			int x=0;
			for(String fileName:fileList)
				bf[x++]=new BufferedReader(new FileReader(new File(folderPath+"/"+fileName)));
			for(int i=0;i<bf.length;i++)
				addPriorityQueue(bf[i], i);
			x=0;
			bw=new BufferedWriter(new FileWriter(new File(indexPath+"/A"+pageCount+".txt")));
			while(!pq.isEmpty())
			{
				WordNode temp=pq.poll();
				addPriorityQueue(bf[temp.getIndex()], temp.getIndex());
				Map<Integer,Map<Integer,Object> > postingMap=makeMap(temp.getPostingString());
				while(!pq.isEmpty() && temp.getWord().equalsIgnoreCase(pq.peek().getWord()))
				{
					WordNode t=pq.poll();
					addPriorityQueue(bf[t.getIndex()], t.getIndex());
					postingMap=mergeMap(postingMap, makeMap(t.getPostingString()));
				}
				if(x==1000)
				{
					bw.close();
					pageCount++;
					bw=new BufferedWriter(new FileWriter(new File(indexPath+"/A"+pageCount+".txt")));
					x=0;
				}
				writeToFile(temp.getWord(), postingMap, bw);
				x++;
			}
			bw.close();
		}
		catch (Exception e) 
		{
			throw e;
		}
	}
	
	public void writeToFile(String word,Map<Integer,Map<Integer,Object> > postingMap,BufferedWriter bw) throws Exception
	{
		DecimalFormat df = new DecimalFormat("#.####");
		try
		{
			StringBuilder sb=new StringBuilder();
	    	sb.append(word+":");
	    	for(Map.Entry<Integer,Map<Integer,Object> > en:postingMap.entrySet())
	    	{
	    		sb.append(en.getKey()+"#");
	    		for(Map.Entry<Integer, Object> e:en.getValue().entrySet())
	    		{
	    			if(e.getKey()==1)
	    				sb.append(e.getKey()+"-"+df.format(Double.parseDouble(e.getValue().toString()))+",");
	    			else
	    				sb.append(e.getKey()+"-"+e.getValue()+",");
	    		}
	    		sb.deleteCharAt(sb.length() - 1);
	    		sb.append(";");
	    	}
	    	sb.deleteCharAt(sb.length() - 1);
	    	sb.append("\n");
	    	bw.write(sb.toString());
		}
		catch (Exception e) 
		{
			throw e;
		}
	}
}
