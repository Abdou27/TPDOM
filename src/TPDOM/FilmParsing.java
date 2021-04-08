package TPDOM;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FilmParsing {
	private static final String NEW_LINE = "\n";
	
	Document doc;
	Node root;
	
	public FilmParsing(String filePath) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder =  factory.newDocumentBuilder();
			doc = builder.parse(filePath);
			root = doc.getElementsByTagName("DVDLibrary").item(0);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void afficherDOM() {
		System.out.print("**** Affichage du DOM : ");
		print_node(root, 0);
		System.out.println(NEW_LINE);
	}
	
	private void print_node(Node root, int tab) {
		String t = "";
		for(int i = 0; i < tab; i++) {
			t = t + " ";
		}
		if(root.getNodeValue() == null) {
			System.out.print(NEW_LINE);
			System.out.print(t + root.getNodeName());
			if(root.hasAttributes()) {
				System.out.print("[");
				NamedNodeMap attributes = root.getAttributes();
				for(int i = 0; i < attributes.getLength(); i++) {
					Node attribute = attributes.item(i);
					System.out.print(attribute.getNodeName() + " = \"" + attribute.getNodeValue() + "\"");
					if(attributes.item(i+1) != null){
						System.out.print(", ");
					}
				}
				System.out.print("]");
			}
			System.out.print(" : ");
		} else {
			if(!root.getNodeValue().contains(NEW_LINE)) {
				System.out.print(root.getNodeValue());
			}
		}
		if(root.hasChildNodes()) {
			NodeList children = root.getChildNodes();
			tab += 4;
			for(int i = 0; i < children.getLength(); i++) {
				print_node(children.item(i), tab);
			}
		}
	}
	
	private String elementContent(Element e, String tagName) {
		return e.getElementsByTagName(tagName).item(0).getFirstChild().getNodeValue();
	}
	
	public void listerActeurs() {
		NodeList actors = doc.getElementsByTagName("actor");
		System.out.println("**** Liste des acteurs des DVD :");
		for (int i = 0; i < actors.getLength(); i++) {
			Node actor = actors.item(i);
			Element e = (Element) actor;
			String first_name = elementContent(e, "firstName");
			String last_name = elementContent(e, "lastName");
			System.out.println("- " + first_name + " " + last_name);
		}
		System.out.print(NEW_LINE);
	}
	
	public void listerDatesRetourDVD() {
		NodeList rents = doc.getElementsByTagName("rent");
		System.out.println("**** Liste des dates de retour des DVD :");
		for (int i = 0; i < rents.getLength(); i++) {
			Element e = (Element) rents.item(i);
			String date = e.getAttribute("date");
			System.out.println("- " + date);
		}
		System.out.print(NEW_LINE);
	}
	
	public void requetesXPath() {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		
		System.out.println("**** Liste des titres des films :");
		executeRequest(xpath, "//title");
		System.out.print(NEW_LINE);
		
		System.out.println("**** Liste des titres des films loués :");
		executeRequest(xpath, "//DVD[rent]//title");
		System.out.print(NEW_LINE);
		
		System.out.println("**** Liste des filmes avec le nombre des acteurs :");
		listerFilmesAvecNombreDActeurs(xpath);
		System.out.print(NEW_LINE);
	}
	
	private void executeRequest(XPath xpath, String request) {
		try {
			XPathExpression exp = xpath.compile(request);
			NodeList list = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++) {
				Node item = list.item(i);
				System.out.println("- " + item.getFirstChild().getNodeValue());
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	private void listerFilmesAvecNombreDActeurs(XPath xpath) {
		String request = "//film";
		try {
			XPathExpression exp = xpath.compile(request);
			NodeList list = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++) {
				Element film = (Element) list.item(i);
				request = "count(.//actor)";
				exp = xpath.compile(request);
				int nbr = (int) ((double) exp.evaluate(film, XPathConstants.NUMBER));
				String title = elementContent(film, "title");
				System.out.println("- " + title + " : " + nbr + " acteurs");
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	public void louerFilme(String filmTitle, String lastName, String firstName, String address, String date) {
		NodeList dvds = doc.getElementsByTagName("DVD");
		int p = 2;
		p = p - 0;
		for (int i = 0; i < dvds.getLength(); i++) {
			Element dvd = (Element) dvds.item(i);
			String title = dvd.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
			if(title.equals(filmTitle)) {
				if(dvd.getElementsByTagName("rent").getLength() != 0) {
					Node rent = dvd.getElementsByTagName("rent").item(0);
					dvd.removeChild(rent);
				}
				Node lastNameNode = doc.createElement("lastName");
				lastNameNode.appendChild(doc.createTextNode(lastName));
				Node firstNameNode = doc.createElement("firstName");
				firstNameNode.appendChild(doc.createTextNode(firstName));
				Node addressNode = doc.createElement("address");
				addressNode.appendChild(doc.createTextNode(address));
				Node personNode = doc.createElement("person");
				personNode.appendChild(lastNameNode);
				personNode.appendChild(firstNameNode);
				personNode.appendChild(addressNode);
				Node rentNode = doc.createElement("rent");
				((Element) rentNode).setAttribute("date", date);
				rentNode.appendChild(personNode);
				dvd.appendChild(rentNode);
				System.out.println("**** " + filmTitle + " loué par " + firstName + " " + lastName + " le " + date);
			}
		}
		System.out.print(NEW_LINE);
	}
}
