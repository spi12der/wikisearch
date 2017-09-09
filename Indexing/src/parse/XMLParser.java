package parse;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.Indexing;

import utils.PorterStemmer;

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
					{
						String stemmedText = null;
						if (oneWord != "\n" && oneWord != " " && oneWord != "\t" && oneWord != "\r" && oneWord != null
								&& !oneWord.isEmpty() && oneWord.length()>2) {
							try
							{
								stemmedText=porterObj.stem(oneWord.toLowerCase());
								Indexing.updateIndexTable(stemmedText, pageId);
							}
							catch (StringIndexOutOfBoundsException e) 
							{
								//System.out.println("Exceptional word :"+oneWord);
							}
						}
					}
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
}
