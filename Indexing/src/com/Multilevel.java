package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.TreeMap;

public class Multilevel 
{
	public void createMultilevel(char level,String folderPath)
	{
		File base = new File(folderPath+"/"+level);
		File[] listOfFiles = base.listFiles();
		if(listOfFiles.length>1)
		{
			int pageCount=1,wordCount=0;
			int val=level;
			level=(char)(val+1);
			File dir=new File(folderPath+"/"+level);
			dir.mkdirs();
			try
			{
				BufferedWriter bw=new BufferedWriter(new FileWriter(new File(folderPath+"/"+level+"/"+pageCount+".txt")));
				Map<String,String> wordMap=new TreeMap<String,String>();
				for(File f:listOfFiles)
				{
					BufferedReader br=new BufferedReader(new FileReader(f));
					String line,lastLine="";
					while((line = br.readLine()) != null)
					{
						lastLine=line;
					}
					br.close();
					if(wordCount==1000)
					{
						writeMap(bw, wordMap);
						wordMap.clear();
						bw.close();
						pageCount++;
						bw=new BufferedWriter(new FileWriter(new File(folderPath+"/"+level+"/"+pageCount+".txt")));
						wordCount=0;
					}
					wordMap.put(lastLine.split(":")[0], f.getName());
					wordCount++;
				}
				writeMap(bw, wordMap);
				wordMap.clear();
				bw.close();
				createMultilevel(level, folderPath);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}	
	}
	
	public void createMultilevelDoc(char level,String folderPath)
	{
		File base = new File(folderPath+"/"+level);
		File[] listOfFiles = base.listFiles();
		if(listOfFiles.length>1)
		{
			int pageCount=1,wordCount=0;
			int val=level;
			level=(char)(val+1);
			File dir=new File(folderPath+"/"+level);
			dir.mkdirs();
			try
			{
				BufferedWriter bw=new BufferedWriter(new FileWriter(new File(folderPath+"/"+level+"/"+pageCount+".txt")));
				Map<Integer,String> wordMap=new TreeMap<Integer,String>();
				for(File f:listOfFiles)
				{
					BufferedReader br=new BufferedReader(new FileReader(f));
					String line,lastLine="";
					while((line = br.readLine()) != null)
					{
						lastLine=line;
					}
					br.close();
					if(wordCount==1000)
					{
						writeMapDoc(bw, wordMap);
						wordMap.clear();
						bw.close();
						pageCount++;
						bw=new BufferedWriter(new FileWriter(new File(folderPath+"/"+level+"/"+pageCount+".txt")));
						wordCount=0;
					}
					wordMap.put(Integer.parseInt(lastLine.split(":")[0]), f.getName());
					wordCount++;
				}
				writeMapDoc(bw, wordMap);
				wordMap.clear();
				bw.close();
				createMultilevel(level, folderPath);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}	
	}
	
	public void makeDocLevel(String source,String destination)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(new File(source)));
			int pageCount=1;
			File f=new File(destination);
			f.mkdirs();
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File(destination+"/"+pageCount+".txt")));
			String line=null;
			int count=0;
			while((line = br.readLine()) != null)
			{
				if(count==1000)
				{
					bw.close();
					pageCount++;
					count=0;
					bw=new BufferedWriter(new FileWriter(new File(destination+"/"+pageCount+".txt")));
				}
				bw.write(line+"\n");
				count++;
			}
			bw.close();
			br.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void writeMap(BufferedWriter bw,Map<String,String> wordMap)
	{
		try
		{
			for(Map.Entry<String, String> entry:wordMap.entrySet())
			{
				bw.write(entry.getKey()+":"+entry.getValue()+"\n");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void writeMapDoc(BufferedWriter bw,Map<Integer,String> wordMap)
	{
		try
		{
			for(Map.Entry<Integer, String> entry:wordMap.entrySet())
			{
				bw.write(entry.getKey()+":"+entry.getValue()+"\n");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
