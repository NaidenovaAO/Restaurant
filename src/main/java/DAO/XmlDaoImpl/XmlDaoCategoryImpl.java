package DAO.XmlDaoImpl;

import DAO.Dao;
import model.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class XmlDaoCategoryImpl implements Dao<Category> {


    @Override
    public void create(Category category) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/menu.xml"));
        Node firstChild = document.getFirstChild();
        Element newCategory = document.createElement("nameCategory");
        firstChild.appendChild(newCategory);
        newCategory.setAttribute("name", firstUpperCase(category.getName()));
        reformatXmlFile(document,"src/menu.xml");
    }

    @Override
    public void delete(Category category) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/menu.xml"));
        NodeList nodeList = document.getElementsByTagName("nameCategory");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node namedItem = nodeList.item(i).getAttributes().getNamedItem("name");
            if (((namedItem.getNodeValue()).equals(firstUpperCase(category.getName())))) {
                Element element = (Element) nodeList.item(i);
                Node parent = element.getParentNode();
                parent.removeChild(element);
                parent.normalize();
            }
        }
        reformatXmlFile(document,"src/menu.xml");
    }

    @Override
    public void update(Category category) throws ParserConfigurationException, IOException, SAXException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/menu.xml"));
        NodeList nodeList = document.getElementsByTagName("nameCategory");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node namedItem = nodeList.item(i).getAttributes().getNamedItem("name");
            if (((namedItem.getNodeValue()).equalsIgnoreCase(category.getName()))) {
                Node elementsByTagName = document.getElementsByTagName("nameCategory").item(i);
                org.w3c.dom.NamedNodeMap map = elementsByTagName.getAttributes();
                Node nodeAttr = map.getNamedItem("name");

                //Запрос во View: новое имя категории
               String newNameForCategory =  new BufferedReader(new InputStreamReader(System.in)).readLine();
                Category newCategory = new Category(newNameForCategory);
                nodeAttr.setTextContent(firstUpperCase(newCategory.getName()));
                NodeList list = document.getElementsByTagName(category.getName());
                for (int j = 0; j < list.getLength(); ) {
                    document.renameNode(list.item(j), "", newCategory.getName().toLowerCase());
                }
            }
        }
    }

    @Override
    public List<Category> getAll() throws ParserConfigurationException, IOException, SAXException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("src/menu.xml"));
        NodeList nodeList = document.getElementsByTagName("nameCategory");
        Category category;
        ArrayList<Category> listOfCategory = new ArrayList<Category>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            category = new Category(element.getAttributes().getNamedItem("name").getTextContent());
            listOfCategory.add(category);
        }
        return listOfCategory;
    }

    public static void reformatXmlFile(Document document, String nameOfDocument) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(nameOfDocument));
        transformer.transform(source, result);
    }


    public static String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
