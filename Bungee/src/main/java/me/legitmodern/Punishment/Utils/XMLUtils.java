package me.legitmodern.Punishment.Utils;

import com.google.common.base.Preconditions;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;

public class XMLUtils {

    /**
     * Get a NodeList at the selected path from a XML File
     *
     * @param path Path to get NodeList from
     * @param XML  XMLFile
     * @return NodeList from selected path / file
     */
    public static NodeList getNodeListAtPath(String path, File XML) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(Preconditions.checkNotNull(XML, "XML file is null"));

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression xPathExpression = xPath.compile(path.replace('.', '/'));

            return Preconditions.checkNotNull((NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET), "NodeList is null");
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get a Node at the selected path from a XML File
     *
     * @param path Path to get Node from
     * @param XML  XMLFile
     * @param item Item in the NodeList from the path
     * @return Node from selected path / file
     */
    public static Node getNodeAtPath(String path, File XML, int item) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(Preconditions.checkNotNull(XML, "XML file is null"));

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression xPathExpression = xPath.compile(path.replace('.', '/'));

            NodeList nodeList = Preconditions.checkNotNull((NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET), "NodeList is null");
            return Preconditions.checkNotNull(nodeList.item(item), "Item is null");

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {

            e.printStackTrace();

        }
        return null;
    }

    /**
     * Copy an input stream to a file
     *
     * @param inputStream Input stream to copy
     * @param destination Destination file
     */
    public static void copyStreamToFile(InputStream inputStream, File destination) {
        Preconditions.checkNotNull(inputStream, "Input Stream is null");
        Preconditions.checkNotNull(destination, "Destination is null");

        try {
            OutputStream out = new FileOutputStream(destination);
            byte[] buf = new byte[1024];
            int len;

            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            out.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}