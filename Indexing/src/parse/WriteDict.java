package parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.Indexing;
import com.MergeList;

public class WriteDict 
{
	public static int fileNo=1;
	public static String folderPath="/home/rohit/IIIT/Sem3/IRE/Part";
	
	public static void writeDictToFile()
	{
		try
		{
			BufferedWriter bw=new BufferedWriter(new FileWriter(new File(folderPath+"/P"+fileNo+".txt")));
			for(Integer i:Indexing.docMap.keySet())
			{
				double size=Indexing.docMap.get(i);
				Indexing.docMap.put(i, Math.pow(size, 0.5));
			}
			for(String word:Indexing.indexTable.keySet())
			{
				for(Integer i:Indexing.indexTable.get(word).keySet())
				{
					double count=(double)Indexing.indexTable.get(word).get(i).get(1);
					Indexing.indexTable.get(word).get(i).put(1, count/Indexing.docMap.get(i));
				}
				MergeList ml=new MergeList();
				ml.writeToFile(word, Indexing.indexTable.get(word), bw);
			}
			fileNo++;
			bw.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		Indexing.indexTable.clear();
		Indexing.docMap.clear();
	}
}
