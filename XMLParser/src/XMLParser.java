import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLParser 
{
	public static void main(String args[])
	{
		try 
		{
			File XmlFile = new File("simple.xml");
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(XmlFile);
		 
			doc.getDocumentElement().normalize();
		 
			System.out.println("This is the Root element :" + doc.getDocumentElement().getNodeName());
		 
			NodeList nList = doc.getElementsByTagName("apartment");
			System.out.println(nList.getLength());
		 
			System.out.println("----------------------------");
			
			for (int temp = 0; temp < nList.getLength(); temp++)
			{
				 
				Node nNode = nList.item(temp);
		 
				System.out.println("\n");
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
		 			Element eElement = (Element) nNode;
		 			System.out.println("Name: " + eElement.getElementsByTagName("name").item(0).getTextContent());
		 			System.out.println("Address : " + eElement.getElementsByTagName("address").item(0).getTextContent());
				}
		    }
		}
			catch (Exception e) 
			{
				e.printStackTrace();
		    }
		  }
	}
	


