package utils;

import java.util.Map;
import java.util.TreeMap;

public class MetaData 
{
	public static Map<Integer,String> docMap=new TreeMap<Integer,String>();
	public static Map<String,Integer> categoryMap=new TreeMap<String,Integer>();
	
	static 
	{
		categoryMap.put("t:",2);
		categoryMap.put("i:",3);
		categoryMap.put("c:",4);
	}
}
