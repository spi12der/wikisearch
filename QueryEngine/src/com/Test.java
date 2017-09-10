package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test 
{
	public static void main(String[] args) 
	{
		double start=System.currentTimeMillis();
		String des="/home/rohit/IIIT/Sem3/IRE/index.txt";
		try 
		{
			File file = new File(des);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			int count=0;
			List<Integer> arr=new ArrayList<Integer>();
			while ((line = bufferedReader.readLine()) != null && count<100) 
			{
				String token[]=line.split(":");
				String docs[]=token[1].split(";");
				for(String doc:docs)
				{
					String docID[]=doc.split("#");
					arr.add(Integer.parseInt(docID[0]));
				}	
				count++;
			}
			for(Integer i:arr)
				System.out.println(i);
			fileReader.close();	
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		double end=System.currentTimeMillis();
		System.out.println((end-start)/1000.0 + "sec");
	}
}
