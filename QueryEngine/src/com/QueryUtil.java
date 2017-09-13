package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import utils.MetaData;

public class QueryUtil 
{
	public String getWordString(char level,String word,String file) throws IOException
	{
		if(level=='A')
		{
			BufferedReader br=null;
			try
			{
				br=new BufferedReader(new FileReader(new File(QueryMain.indexPath+"/"+level+"/"+file)));
				String line;
				while((line = br.readLine()) != null)
				{
					String tokens[]=line.split(":");
					if(word.equalsIgnoreCase(tokens[0]))
					{
						return tokens[1];
					}
				}
				return "";
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			finally
			{
				br.close();
			}
		}
		else
		{
			BufferedReader br=null;
			try
			{
				br=new BufferedReader(new FileReader(new File(QueryMain.indexPath+"/"+level+"/"+file)));
				String line;
				while((line = br.readLine()) != null)
				{
					String tokens[]=line.split(":");
					if(word.compareTo(tokens[0])<=0)
					{
						int val=level;
						level=(char)(val-1);
						return getWordString(level, word, tokens[1]);
					}
				}
				return "";
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			finally
			{
				br.close();
			}
		}
		return null;
	}
	
	public Map<Integer,ResultNode> makeMultiMap(String postingString)
	{
		Map<Integer,ResultNode> postingMap=new TreeMap<Integer,ResultNode>();
		if(!postingString.isEmpty())
		{
			String docs[]=postingString.split(";");
			for(String doc:docs)
			{
				String docT[]=doc.split("#");
				ResultNode r=new ResultNode();
				int docId=Integer.parseInt(docT[0]);
				r.setDocId(docId);
				String cat[]=docT[1].split(",");
				r.setTfScore(Long.parseLong(cat[0].split("-")[1]));
				postingMap.put(docId, r);
			}
		}
		return postingMap;
	}
	
	public Map<Integer,ResultNode> makeTermMap(String postingString,int category)
	{
		Map<Integer,ResultNode> postingMap=new TreeMap<Integer,ResultNode>();
		if(!postingString.isEmpty())
		{
			String docs[]=postingString.split(";");
			for(String doc:docs)
			{
				String docT[]=doc.split("#");
				ResultNode r=new ResultNode();
				int docId=Integer.parseInt(docT[0]);
				r.setDocId(docId);
				String cat[]=docT[1].split(",");
				r.setTfScore(Long.parseLong(cat[0].split("-")[1]));
				boolean flag=false;
				for(int i=1;i<cat.length;i++)
				{
					if(category==Integer.parseInt(cat[i]))
					{
						flag=true;
						break;
					}
				}
				if(flag)
					postingMap.put(docId, r);
			}	
		}	
		return postingMap;
	}
	
	public Map<Integer,ResultNode> mergeMap(Map<Integer,ResultNode> a,Map<Integer,ResultNode> b)
	{
		Map<Integer,ResultNode> temp=new TreeMap<Integer,ResultNode>();
		if(a.size()==0)
			return b;
		else
		{
			for(Integer i:a.keySet())
			{
				if(b.containsKey(i))
				{
					ResultNode at=a.get(i);
					ResultNode bt=b.get(i);
					at.setTfScore(at.getTfScore()+bt.getTfScore());
					temp.put(i, at);
				}
			}
			return temp;
		}
	}
	
	public String getDocTitle(char level,int docId,String file) throws IOException
	{
		if(level=='A')
		{
			BufferedReader br=null;
			try
			{
				br=new BufferedReader(new FileReader(new File(QueryMain.indexPath+"/Doc/"+level+"/"+file)));
				String line;
				while((line = br.readLine()) != null)
				{
					String tokens[]=line.split(":");
					if(docId==Integer.parseInt(tokens[0]))
					{
						return tokens[1];
					}
				}
				return "";
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			finally
			{
				br.close();
			}
		}
		else
		{
			BufferedReader br=null;
			try
			{
				br=new BufferedReader(new FileReader(new File(QueryMain.indexPath+"/Doc/"+level+"/"+file)));
				String line;
				while((line = br.readLine()) != null)
				{
					String tokens[]=line.split(":");
					if(docId<=Integer.parseInt(tokens[0]))
					{
						int val=level;
						level=(char)(val-1);
						return getDocTitle(level, docId, tokens[1]);
					}
				}
				return "";
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			finally
			{
				br.close();
			}
		}
		return null;
	}
	
	public int getCategory(String category)
	{
		if(MetaData.categoryMap.containsKey(category))
			return MetaData.categoryMap.get(category);
		return 0;
	}
	
}
