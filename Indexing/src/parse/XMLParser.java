package parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.Indexing;

import utils.PorterStemmer;
import utils.StopWordsRemover;

public class XMLParser 
{
	public static int pageId;
	
	PorterStemmer porterObj;
	
	public XMLParser() 
	{
		porterObj=new PorterStemmer();
	}
	
	
	public void parse(String xmlPath) throws Exception
	{
		try
	    {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		
			DefaultHandler handler = new DefaultHandler() {
		
			boolean text = false;
			StringBuilder currentText = new StringBuilder();
			String title = "";
			public void startElement(String uri, String localName,String qName,
		                Attributes attributes) throws SAXException {
				
				if (qName.equalsIgnoreCase("redirect")) 
				{
			         title = attributes.getValue("title");
			         pageId++;
			         WriteDict.writeDocDetails(pageId, title);
			         if(pageId%300==0)
						try {
							System.out.println(pageId+" documents processed");	
							WriteDict.writeDictToFile();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    }
				if (qName.equalsIgnoreCase("text")) {
					text = true;
				}
		
			}

			@Override
			public void characters(char[] ch, int start, int length)
			{
				if (text) 
				{
					currentText.append(title );
					currentText.append(ch, start, length);
				}
			   		
			}
			public void endElement(String uri, String localName, String qName) throws SAXException {
				String content=currentText.toString();
		      	if(content.length() > 0 && text) 
		      	{
		      		String[] w = title.split("[^a-zA-Z]");
					for (String oneWord : w) 
						addToDictionary(oneWord, 2);
					
					
		      		String[] words = content.split("[^a-zA-Z]");
					for (String oneWord : words) 
						addToDictionary(oneWord, 1);
					
					String[] info = infoboxAdder(content).split("[^a-zA-Z]");
					for (String oneWord : info) 
						addToDictionary(oneWord, 3);
					
					String[] cat = catagoryAdder(content).split("[^a-zA-Z]");
					for (String oneWord : cat) 
						addToDictionary(oneWord, 4);
					
		      		text=false;
		      		title = "";
		      	}
		      	currentText.setLength(0);
		        
		    }
		
			};

		    saxParser.parse(xmlPath, handler);
	    } 
		catch (Exception e) {
			throw e;
	    }
	}
	
	public void addToDictionary(String word,int category)
	{
		String stemmedText=getValidWord(word);
		long count=0;
		double size=0;
		if(Indexing.indexTable.containsKey(stemmedText) && 
				Indexing.indexTable.get(stemmedText).containsKey(pageId))
		{
			count=(Long)Indexing.indexTable.get(stemmedText).get(pageId).get(1);
			size=Indexing.docMap.get(pageId);
			size-=(count*count);
			
		}
		count++;
		size+=(count*count);
		Indexing.docMap.put(pageId, size);
		if(category==2)
			Indexing.updateIndexTable(stemmedText,pageId, 1);
		Indexing.updateIndexTable(stemmedText, pageId,category);
	}
	
	public String getValidWord(String word)
	{
		String stemmedText = "";
		if (word != "\n" && word != " " && word != "\t" && word != "\r" && word != null
				&& !word.isEmpty() && word.length()>2 && word.length()<21) 
		{
			try
			{
				stemmedText=porterObj.stem(word.toLowerCase());
				if(StopWordsRemover.stopWordSet.contains(stemmedText))
					stemmedText="";
			}
			catch (StringIndexOutOfBoundsException e) 
			{
				//e.printStackTrace();
			}
		}
		return stemmedText;
	}
	
	public String infoboxAdder(String text) 
	{
		String t = "";
		String regex1 = "\\{\\{infobox";
		String regex2 = "\\}\\}";
		Matcher m1 = Pattern.compile(regex1).matcher(text);
		Matcher m2 = Pattern.compile(regex2).matcher(text);
		while (m1.find())
			if(m2.find(m1.start())){
				t += text.substring(m1.end(), m2.start());
			}
		return t;
	}
	
	public String catagoryAdder(String text) 
	{
		String t = "";
		String regex1 = "\\[\\[category:";
		String regex2 = "\\]\\]";
		Matcher m1 = Pattern.compile(regex1).matcher(text);
		Matcher m2 = Pattern.compile(regex2).matcher(text);
		while (m1.find())
			if(m2.find(m1.start()))
			{
				t += text.substring(m1.end(), m2.start()) + " ";
			}
		return t;
	}
}
