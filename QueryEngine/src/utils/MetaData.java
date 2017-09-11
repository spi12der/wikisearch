package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

import com.QueryMain;

public class MetaData 
{
	public static Map<Integer,String> docMap=new TreeMap<Integer,String>();
	public static Map<String,Integer> categoryMap=new TreeMap<String,Integer>();
	
	static 
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(new File(QueryMain.indexPath+"/doc.txt")));
			String line;
			while((line = br.readLine()) != null)
			{
				String docP[]=line.split(":");
				docMap.put(Integer.parseInt(docP[0]), docP[1]);
			}
			br.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		categoryMap.put("t:",2);
		categoryMap.put("c:",3);
		categoryMap.put("i:",4);
	}
}
