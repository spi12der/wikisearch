package parse;

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
	
	
	public void parse(String xmlPath)
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
			         if(pageId%80==0)
			        	 WriteDict.writeDictToFile();
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
		      		String[] words = content.split("[^a-zA-Z]");
					for (String oneWord : words) 
						addToDictionary(oneWord, 2);
		      		text=false;
		      		title = "";
		      	}
		      	currentText.setLength(0);
		        
		    }
		
			};

		    saxParser.parse(xmlPath, handler);
	    } 
		catch (Exception e) {
			e.printStackTrace();
	    }
	}
	
	public void addToDictionary(String word,int category)
	{
		String stemmedText = null;
		if (word != "\n" && word != " " && word != "\t" && word != "\r" && word != null
				&& !word.isEmpty() && word.length()>2) 
		{
			try
			{
				stemmedText=porterObj.stem(word.toLowerCase());
				if(!StopWordsRemover.stopWordSet.contains(stemmedText))
				{
					long count=0;
					double size=Indexing.docMap.get(pageId);
					if(Indexing.indexTable.containsKey(stemmedText) && 
							Indexing.indexTable.get(stemmedText).containsKey(pageId))
					{
						count=(long)Indexing.indexTable.get(stemmedText).get(pageId).get(1);
						size-=(count*count);
						
					}
					count++;
					size+=(count*count);
					Indexing.docMap.put(pageId, size);
					Indexing.indexTable.get(stemmedText).get(pageId).put(1, count);
					Indexing.updateIndexTable(stemmedText, pageId,category);
				}	
			}
			catch (StringIndexOutOfBoundsException e) 
			{
				//System.out.println("Exceptional word :"+oneWord);
			}
		}
	}
}
