package ua.maestro.lib.commons.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.StringReader;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlUtils {
	
	public static String formatXML(String xml) {
		return formatXML(xml, 4, -1);
	}
	
	/**
	 * Formats the given xml data.
	 * @param xml The xml data to be formatted.
	 * @param indent Number of whitespaces to indent. 
	 * @param maxCDataLength Max line length for cdata.
	 * @return The formatted xml.
	 */	
	public static String formatXML(String xml, int indent, int maxCDataLength) {
		return formatXML(xml.getBytes(), indent, maxCDataLength);
	}

	/**
	 * Formats the given xml data.
	 * @param xml The xml data to be formatted.
	 * @param indent Number of whitespaces to indent. 
	 * @param maxCDataLength Max line length for cdata.
	 * @return The formatted xml.
	 */
	public static String formatXML(byte[] xml, int indent, int maxCDataLength) {
		SimpleParser parser = new SimpleParser();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XmlFormatter formatter = new XmlFormatter(out);
		formatter.setMaxCDataLength(maxCDataLength);
		formatter.setIndent(0);
		if(indent >= 0) {
			formatter.setIndent(indent);
		}
		
		char[] charArray = new String(xml).toCharArray();
		parser.parse(formatter, charArray, 0, charArray.length);

		return out.toString();
	}
	
	public static String formatDocument(Document document) throws TransformerFactoryConfigurationError, TransformerException, IOException {
        OutputFormat format = new OutputFormat(document, "UTF-8", true);
        format.setLineWidth(160);
//        format.setIndenting(true);
        format.setIndent(2);
//        format.setEncoding("UTF-8");
        Writer out = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(document);

        return out.toString();
	}
	
	public static String formatDocument2(Document document) throws TransformerException {
	    DOMSource domSource = new DOMSource(document);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    StreamResult streamResult = new StreamResult(out);
	    TransformerFactory tf = TransformerFactory.newInstance();

	    Transformer serializer = tf.newTransformer();
	    serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    serializer.setOutputProperty(OutputKeys.INDENT, "yes");
	    serializer.transform(domSource, streamResult);	
	    return new String(out.toByteArray());
	}
	
	/**
	 * Creates a document from the given xml bytes.
	 * @return The desired document. Never returns null but throws some Exception.
	 * @throws ParserConfigurationException, IOException, SAXException  
	 */
	public static Document getDocument(byte[] xml) throws ParserConfigurationException, IOException, SAXException {
		if(xml != null) {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// factory.setNamespaceAware( true );
			// factory.setValidating( true );
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(new ByteArrayInputStream(xml));
			return document;
		}
		throw new IOException("No xml data");
	}
	
    /**
     *  Create new XML document.
     *
     * @param  rootElementName  name of the root element to add, or <code>null</code> if the
     *      document should not have any root just yet
     * @throws ParserConfigurationException 
     */
    public static Document createEmptyDocument(String rootElementName) throws ParserConfigurationException {
    	return createEmptyDocument(rootElementName, null);
    }

    /**
     *  Create new XML document.
     *
     * @param  rootElementName  name of the root element to add, or <code>null</code> if the
     *      document should not have any root just yet
     * @throws ParserConfigurationException 
     */
    public static Document createEmptyDocument(String rootElementName, String namespace) throws ParserConfigurationException {
    	final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	final DocumentBuilder builder = factory.newDocumentBuilder();
        Document result = builder.newDocument();

        if (rootElementName != null) {
        	Element rootElement;
        	if(namespace != null && !namespace.isEmpty()) {
        		rootElement = result.createElementNS(rootElementName, namespace);
        	} else {
        		rootElement = result.createElement(rootElementName);        		
        	}
            result.appendChild(rootElement);
        }
        return result;
    }	
	
	/**
	 * Tests if the given xml data could be parsed into a document.
	 * @param xml The xml data to be parsed
	 * @return <code>true</code> if the given data could be parsed and <code>false</code> otherwise.
	 */
	public static boolean isValidXML(byte[] xml) {
		try {
			getDocument(xml);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
    
    public static void setXmlAttributeVal(Node node, String attName, String val) {
        NamedNodeMap attributes = node.getAttributes();
        Node attNode = node.getOwnerDocument().createAttribute(attName);
        attNode.setNodeValue(val);
        attributes.setNamedItem(attNode);
    }

    public static Node newXmlNode(Document doc, String nm, String val) {
        Node node = doc.createElement(nm);
        node.setTextContent(val); 
        return node;
    }    
    
    public static void appendXmlFragment(DocumentBuilder docBuilder, Node parent, String fragment) throws IOException, SAXException {
        Document doc = parent.getOwnerDocument();
        Node fragmentNode = docBuilder.parse(new InputSource(new StringReader(fragment))).getDocumentElement();
        fragmentNode = doc.importNode(fragmentNode, true);
        parent.appendChild(fragmentNode);
    }

    public static String appendXmlFragment(String xmlString, String nodeName, String append) throws SAXException, IOException, ParserConfigurationException, TransformerException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
        NodeList list = doc.getElementsByTagName(nodeName);
        Element dh = (Element) list.item(0);
        
        appendXmlFragment(builder, dh, append);
        doc.getDocumentElement().normalize();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StringWriter outWriter = new StringWriter();
        StreamResult result = new StreamResult(outWriter);

        transformer.transform(source, result);

        StringBuffer sb = outWriter.getBuffer();
        return sb.toString();
    }
    
    public static String xmlNode2String(Node node) throws TransformerException {
        StringWriter buf = new StringWriter();
        Transformer xform = TransformerFactory.newInstance().newTransformer();
        xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xform.transform(new DOMSource(node), new StreamResult(buf));
        
        return(buf.toString());
    }    
}
