package ua.maestro.lib.providers.tax;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ua.maestro.lib.commons.Utils;
import ua.maestro.lib.commons.xml.XmlUtils;
import ua.maestro.lib.providers.tax.xml.XMLParam;
import ua.maestro.lib.providers.tax.xml.XMLUtils;
import ua.maestro.lib.xmlcore.provider.interfaces.IProvider;
import ua.maestro.lib.xmlcore.domains.PZDocInput;
import ua.maestro.lib.xmlcore.domains.PZPayerData;
import ua.maestro.lib.xmlcore.domains.PZDocumentData;
import ua.maestro.lib.xmlcore.utils.PzUtils;
import ua.maestro.lib.xmlcore.xml.data.*;
import ua.maestro.lib.xmlcore.xml.exceptions.PzXmlException;
import ua.maestro.lib.xmlcore.xml.records.XMLDocumentBody;
import ua.maestro.lib.xmlcore.xml.records.XMLDocumentData;

/**
 *
 * @author maestro, 2015
 */
public class AppProviderUtl implements IProvider {
    @Override
    public boolean getType(final PZDocumentData doc) {
        boolean ret = false;
        String cdoc = doc.getCDoc().toUpperCase();
        if (cdoc.startsWith("J") || cdoc.startsWith("F") || cdoc.startsWith("A") || cdoc.startsWith("R")) {
            ret = true;
        }
        return ret;
    }
    
    @Override
    public String getFileName (final PZPayerData pay, final PZDocumentData doc) {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.leftPad0(String.valueOf(doc.getCSti()), 4)); // c_Sti
        sb.append(Utils.leftPad0(pay.getTin(), 10)); // tin (14)
        sb.append(doc.getCDoc()); //cdoc (22)
        sb.append(String.valueOf(doc.getCDocStan())); // c_doc_stan (23)
        sb.append(Utils.leftPad0(String.valueOf(doc.getCDocType()),2)); // c_doc_type (25)
        sb.append(Utils.leftPad0(String.valueOf(doc.getCDocCnt()),7)); // c_doc_cnt (32)
        sb.append(doc.getPeriodType()); // perType
        sb.append(Utils.leftPad0(String.valueOf(doc.getPeriodMonth()), 2)); // perMonth (34)
        sb.append(doc.getPeriodYear()); // perYear (38)
        sb.append(Utils.leftPad0(String.valueOf(doc.getCStiOrig()), 4)); // c_sti (42)
        
        return sb.toString();  
    }
    
    @Override
    public HashMap<String, Object> parseFileName(String fname) {
        HashMap<String, Object> ret = new HashMap<>();

        ret.put("C_STI", Integer.parseInt(fname.substring(0, 4)));
        ret.put("TIN", fname.substring(4, 14));
        ret.put("CDOC", fname.substring(14, 14+8));
        ret.put("C_DOC_CNT", Integer.parseInt(fname.substring(25, 32)));
        ret.put("C_DOC_STAN", Integer.parseInt(fname.substring(22, 23)));
        ret.put("C_DOC_TYPE", Integer.parseInt(fname.substring(23, 25)));
        ret.put("PERIOD_YEAR", Integer.parseInt(fname.substring(35, 39)));
        ret.put("PERIOD_MONTH", Integer.parseInt(fname.substring(33, 35)));
        ret.put("PERIOD_TYPE", Integer.parseInt(fname.substring(32, 33)));
     
        return ret;
    }
    
    @Override
    public String checkImpMainrptXml(final PZDocumentData prm, final List<XmlLinkedDocRec> linkDocs) throws SAXException, IOException, TransformerException, ParserConfigurationException {
        return XMLUtils.checkImpMainrptXml(prm, linkDocs);
    } 
    
    @Override
    public String checkImpDodrptXml(final PZDocumentData prm, final PZDocumentData prmMain, final String fileNameRef) throws SAXException, IOException, TransformerException, ParserConfigurationException {
        return XMLUtils.checkImpDodrptXml(prm, prmMain, fileNameRef);
    }
    
    @Override
    public List<XMLDocumentData> getXmlPrm(String srcXmlStr, String prmNms) throws SAXException, ParserConfigurationException, IOException {
        return XMLUtils.getXmlPrm(srcXmlStr, prmNms);
    }
    
    @Override
    public XmlHeaderRec getXMLHeader(final byte[] xml) throws PzXmlException {
        return XMLUtils.getXMLHeader(xml);
    }
    
    @Override
    public List<XmlBodyRecExt> getXMLDataAsList(final byte[] xml) throws PzXmlException {
        return XMLUtils.getXMLDataAsList(xml);
    }
    
    @Override
    public Map<String, XmlBodyDataRec> getXMLDataAsMap(final byte[] xml) throws PzXmlException {
        return XMLUtils.getXMLDataAsMap(xml);
    }
    
    @Override
    public String createXml(final PZDocumentData doc,
                            final String xsdTmpl,
                            final PZPayerData pay,
                            final List<PZDocInput> docVars,
                            final long cnt4Curdate,
                            final String sSoftware)
                throws PzXmlException {
        try {
            Pattern p = Pattern.compile("[^X]+(X)+[\\w]*");

            List<XMLDocumentBody> xmlBodyItems = XMLUtils.getXmlBody(new XMLParam(doc, pay, docVars, cnt4Curdate), xsdTmpl.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            sb.append("<DECLARBODY>");
            for(XMLDocumentBody item : xmlBodyItems) {
                Matcher m = p.matcher(item.getName());
                if (m.matches()) {
                    sb.append(XMLUtils.writeXMLBodyElement(item, 1));
                } else {
                    sb.append(XMLUtils.writeXMLBodyElement(item, 0)); 
                }
            }
            sb.append("</DECLARBODY>");

            doc.setXml(sb.toString());

            String ret = formXml(doc, xsdTmpl, pay, null, sSoftware);
            return ret;
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new PzXmlException(e.getMessage());
        }
    }
    
    @Override
    public String copyXml(PZDocumentData doc,
                          final String xsdTmpl,
                          final PZPayerData pay,
                          List<PZDocInput> docVars,
                          String srcXmlStr,
                          final String sSoftware)
            throws PzXmlException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setValidating(false);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();        
            Document docSrc = docBuilder.parse(new ByteArrayInputStream(srcXmlStr.getBytes("UTF-8")));
            Node declarBodySrc = docSrc.getElementsByTagName("DECLARBODY").item(0);

            Pattern p = Pattern.compile("[^X]+(X)+[\\w]*");

            List<XMLDocumentBody> xmlBodyItems = XMLUtils.getXmlBody(new XMLParam(doc, pay, docVars, 0), xsdTmpl.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append("<DECLARBODY>");
            for(XMLDocumentBody item : xmlBodyItems) {
                Matcher m = p.matcher(item.getName());
                if (m.matches()) {
                    NodeList listSrc = ((Element)declarBodySrc).getElementsByTagName(item.getName());
                    for (int j = 0; j < listSrc.getLength(); j++) {
                        Node nodeSrc = listSrc.item(j);
                        if (nodeSrc.getNodeName().equals(item.getName())) {
                            String rowNum = "0";
                            NamedNodeMap attrs = nodeSrc.getAttributes();
                            Node attNode = attrs.getNamedItem("ROWNUM");
                            if (attNode != null) {
                                rowNum = attNode.getTextContent();
                            }

                            item.setValue(nodeSrc.getTextContent());
                            sb.append(XMLUtils.writeXMLBodyElement(item, Long.parseLong(rowNum)));
                        }
                    }
                } else {
                    if (Utils.isEmpty(item.getValue())) {
                        NodeList listSrc = ((Element)declarBodySrc).getElementsByTagName(item.getName());
                        if (listSrc.getLength() != 0) {
                            for (int j = 0; j < listSrc.getLength(); j++) {
                                Node nodeSrc = listSrc.item(j);
                                item.setValue(nodeSrc.getTextContent());
                                sb.append(XMLUtils.writeXMLBodyElement(item, 0));
                            }
                        } else {
                            sb.append(XMLUtils.writeXMLBodyElement(item, 0));
                        }
                    } else {
                       sb.append(XMLUtils.writeXMLBodyElement(item, 0)); 
                    }
                }
            }
            sb.append("</DECLARBODY>");

            return sb.toString();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new PzXmlException(e.getMessage());
        }
    }
    
    @Override
    public String copyXml(PZDocumentData doc,
                          final String xsdTmpl,
                          PZPayerData pay,
                          List<PZDocInput> docVars,
                          Map<String, BigDecimal> srcData,
                          final String sSoftware) throws PzXmlException {
        try {
            Pattern p = Pattern.compile("[^X]+(X)+[\\w]*");

            List<XMLDocumentBody> xmlBodyItems = XMLUtils.getXmlBody(new XMLParam(doc, pay, docVars, 0), xsdTmpl.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append("<DECLARBODY>");

            for (XMLDocumentBody item : xmlBodyItems) {
                Matcher m = p.matcher(item.getName());
                if (!m.matches()) {
                    if (Utils.isEmpty(item.getValue())) {
                        BigDecimal val = srcData.get(item.getName());
                        if (val != null) item.setValue(val.toString());
                        sb.append(XMLUtils.writeXMLBodyElement(item, 0));
                    } else {
                        sb.append(XMLUtils.writeXMLBodyElement(item, 0));
                    }
                }
            }

            sb.append("</DECLARBODY>");
            doc.setXml(sb.toString());
            return formXml(doc, xsdTmpl, pay, null, sSoftware);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new PzXmlException(e.getMessage());
        }
    }

    @Override
    public String mergeData (final PZDocumentData doc,
                             final String xsdTmpl,
                             final PZPayerData pay,
                             final List<PZDocInput> docVars,
                             final List<Map<String, XmlBodyDataRec>> docsData,
                             final String sSoftware) throws PzXmlException {
        if (docsData.isEmpty()) {
            return createXml(doc, xsdTmpl, pay, docVars, 0, sSoftware);
        }

        try {
            Pattern p = Pattern.compile("[^X]+(X)+[\\w]*");
            List<XMLDocumentBody> xmlBodyItems = XMLUtils.getXmlBody(new XMLParam(doc, pay, docVars, 0), xsdTmpl.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            sb.append("<DECLARBODY>");

            for (XMLDocumentBody item : xmlBodyItems) {
                Matcher m = p.matcher(item.getName());
                PZDocInput var = PzUtils.getColumnRec(docVars, item.getName());

                if (m.matches()) {
                    if (var.isMerge()) {
                        long rownum = 0;
                        for (Map<String, XmlBodyDataRec> docData : docsData) {
                            XmlBodyDataRec data = docData.get(item.getName());
                            if (data != null) {
                                if (data.getRownum() != 0) {
                                    List<XmlBodyRec> fldData = (List<XmlBodyRec>) data.getValue();
                                    for (XmlBodyRec rec : fldData) {
                                        sb.append(XMLUtils.writeXMLBodyElement(item.getName(), item.getNillable(), ++rownum, rec.getValue()));
                                    }
                                }
                            }
                        }
                    } else {
                        sb.append(XMLUtils.writeXMLBodyElement(item, 1));
                    }
                } else {
                    if (var.isMerge()) {
                        if (var.getDataType() == PZDocInput.ParamDataType.INTEGER) {
                            BigInteger val = BigInteger.ZERO;
                            for (Map<String, XmlBodyDataRec> docData : docsData) {
                                XmlBodyDataRec data = docData.get(item.getName());
                                if (data != null) {
                                    if (data.getRownum() == 0) {
                                        val = val.add(PzUtils.getBigInteger(data.getValue()));
                                    }
                                }
                            }
                            sb.append(XMLUtils.writeXMLBodyElement(item.getName(), item.getNillable(), 0, String.valueOf(val)));
                        } else if (var.getDataType() == PZDocInput.ParamDataType.NUMBER) {
                            BigDecimal val = BigDecimal.ZERO;
                            for (Map<String, XmlBodyDataRec> docData : docsData) {
                                XmlBodyDataRec data = docData.get(item.getName());
                                if (data != null) {
                                    if (data.getRownum() == 0) {
                                        val =  val.add(PzUtils.getBigDecimal(data.getValue()));
                                    }
                                }
                            }
                            sb.append(XMLUtils.writeXMLBodyElement(item.getName(), item.getNillable(), 0, String.valueOf(val)));
                        } else {
                            sb.append(XMLUtils.writeXMLBodyElement(item, 0));
                        }
                    } else {
                        sb.append(XMLUtils.writeXMLBodyElement(item, 0));
                    }
                }
            }
            sb.append("</DECLARBODY>");

            doc.setXml(sb.toString());
            return formXml(doc, xsdTmpl, pay, null, sSoftware);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new PzXmlException(e.getMessage());
        }        
    }
    
    @Override
    public String formXml (final PZDocumentData prm,
                           final String xsdTmpl,
                           final PZPayerData pay,
                           final List<PZDocumentData> docs,
                           final int lnkType,
                           final String sSoftware) throws PzXmlException {
        List<XmlLinkedDocRec> lnkDocs = new ArrayList<>();
        int cnt = 0, cntItems = 0;
        if (docs != null) {
            for(PZDocumentData item : docs) {
                cnt++;
                if (lnkType == 1) {
                    if (cnt > 1) {
                        lnkDocs.add(new XmlLinkedDocRec(++cntItems,
                                                    lnkType,
                                                    item.getCDoc()/*cdoc*/,
                                                    item.getCDocType() /* docType */,
                                                    item.getCDocStan() /* docStan */,
                                                    item.getCDocCnt() /* docCnt */,
                                                    getFileName(pay, item) /* fileName */));
                    }
                } else {
                    if (cnt == 1) {
                        lnkDocs.add(new XmlLinkedDocRec(++cntItems,
                                                    lnkType,
                                                    item.getCDoc()/*cdoc*/,
                                                    item.getCDocType() /* docType */,
                                                    item.getCDocStan() /* docStan */,
                                                    item.getCDocCnt() /* docCnt */,
                                                    getFileName(pay, item)/* fileName */));
                        break;                        
                    }
                }
            }
        }
        
        return formXml(prm, xsdTmpl, pay, lnkDocs, sSoftware);
    }
    
    @Override
    public String formXml (final PZDocumentData prm,
                           final String xsdTmpl,
                           final PZPayerData pay,
                           final List<XmlLinkedDocRec> lnkDocs,
                           final String sSoftware) throws PzXmlException {
        try {  
            String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
                                "<DECLAR xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\""+ prm.getCDoc() +  ".xsd\">\n" + 
                                "</DECLAR>";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
            DocumentBuilder builder;             
            
            builder = factory.newDocumentBuilder();  
            org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader( xmlString )));
            NodeList list = doc.getElementsByTagName("DECLAR");
            Node ndHead = list.item(0); 
            XmlUtils.appendXmlFragment(builder, ndHead, XMLUtils.getXmlHeader(prm, xsdTmpl, pay, lnkDocs, prm.getCSti(), prm.getCStiOrig(), sSoftware));

            NodeList list1 = doc.getElementsByTagName("DECLAR");
            Node ndDeclar = list1.item(0);

            XmlUtils.appendXmlFragment(builder, ndDeclar, prm.getXml());
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StringWriter outWriter = new StringWriter();
            StreamResult result = new StreamResult(outWriter);

            transformer.transform(source, result);
            return (outWriter.getBuffer()).toString();
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            throw new PzXmlException(e.getMessage());
        }
    }
}
