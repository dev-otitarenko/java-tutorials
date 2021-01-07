package ua.maestro.lib.providers.stat.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ua.maestro.lib.commons.Utils;
import ua.maestro.lib.xmlcore.provider.interfaces.IXmlParam;
import ua.maestro.lib.xmlcore.domains.PZPayerData;
import ua.maestro.lib.xmlcore.domains.PZDocumentData;
import ua.maestro.lib.xmlcore.utils.PzUtils;
import ua.maestro.lib.xmlcore.xml.data.XmlBodyDataRec;
import ua.maestro.lib.xmlcore.xml.data.XmlBodyRecExt;
import ua.maestro.lib.xmlcore.xml.data.XmlHeaderRec;
import ua.maestro.lib.xmlcore.xml.data.XmlLinkedDocRec;
import ua.maestro.lib.xmlcore.xml.exceptions.PzXmlException;
import ua.maestro.lib.xmlcore.xml.handlers.HandlerXmlBodyDataAsList;
import ua.maestro.lib.xmlcore.xml.handlers.HandlerXmlBodyDataAsMap;
import ua.maestro.lib.xmlcore.xml.handlers.HandlerXmlHeader;
import ua.maestro.lib.xmlcore.xml.records.XMLDocumentBody;
import ua.maestro.lib.xmlcore.xml.records.XMLDocumentData;
import ua.maestro.lib.xmlcore.xml.records.XMLDocumentHeader;

/**
 *
 * @author maestro, 2015
 */
public class XMLUtils {
    public static String getXmlHeader(final PZDocumentData prm,
                                      final String xsdTmpl,
                                      final PZPayerData pay,
                                      final List<XmlLinkedDocRec> lnkDocs,
                                      final String sSoftware) throws SAXException, ParserConfigurationException, UnsupportedEncodingException, IOException {
        String cdoc = prm.getCDoc(), cterrit = pay.getCTerrit();
        List<XMLDocumentHeader> xmlHdrItems = getXmlHeader(xsdTmpl.getBytes(StandardCharsets.UTF_8));
        StringBuilder ret = new StringBuilder();
        ret.append("<DECLARHEAD>");

        for (XMLDocumentHeader item : xmlHdrItems) {
            switch (item.getName()) {
                case "TIN": ret.append(writeXMLHeaderElement(item, pay.getTin()));
                    break; 
                case "C_DOC":ret.append(writeXMLHeaderElement(item, cdoc.substring(0, 3)));
                    break;
                case "C_DOC_SUB": ret.append(writeXMLHeaderElement(item, cdoc.substring(3, 6)));
                    break;
                case "C_DOC_VER": ret.append(writeXMLHeaderElement(item, String.valueOf(Integer.parseInt(cdoc.substring(6, 8)))));
                    break;
                case "C_DOC_TYPE": ret.append(writeXMLHeaderElement(item, String.valueOf(prm.getCDocType())));
                    break;
                case "C_DOC_CNT": ret.append(writeXMLHeaderElement(item, String.valueOf(prm.getCDocCnt())));
                    break;
                case "C_REG": {
                        String v = Utils.isEmpty(cterrit) ? "00" : (cterrit.length() > 5 ? cterrit.substring(0, 2) : "00");
                        ret.append(writeXMLHeaderElement(item, v));
                    }
                    break;
                case "C_RAJ": {
                        String v = Utils.isEmpty(cterrit) ? "000" : (cterrit.length() > 5 ? cterrit.substring(2, 5) : "000");
                        ret.append(writeXMLHeaderElement(item, v));
                    }
                    break;
                case "PERIOD_MONTH": ret.append(writeXMLHeaderElement(item, String.valueOf(prm.getPeriodMonth())));
                    break;
                case "PERIOD_TYPE": ret.append(writeXMLHeaderElement(item, String.valueOf(prm.getPeriodType())));
                    break;
                case "PERIOD_YEAR": ret.append(writeXMLHeaderElement(item, String.valueOf(prm.getPeriodYear())));
                    break;
                case "C_STI_ORIG": ret.append(writeXMLHeaderElement(item, String.valueOf(pay.getCSti())));
                    break;
                case "C_DOC_STAN": ret.append(writeXMLHeaderElement(item, String.valueOf(prm.getCDocStan())));
                    break;
                case "LINKED_DOCS": ret.append("<LINKED_DOCS xsi:nil=\"true\"/>");
                    break;
                case "D_FILL": ret.append(writeXMLHeaderElement(item, PzUtils.getCurrDateAsXmlStr()));
                    break;
                case "SOFTWARE": ret.append(writeXMLHeaderElement(item, sSoftware));
                    break;
            }
        }
        
        ret.append("</DECLARHEAD>");
        
        return ret.toString();        
    }
    

    public static String writeXMLHeaderElement(final XMLDocumentHeader hdr, final String val) {
        String tagNm = hdr.getName(), fixed = hdr.getFixed();
        return "<" + tagNm + ">" + (!Utils.isEmpty(fixed) ? fixed : val) + "</" + tagNm + ">";
    }

    public static String writeXMLBodyElement(final String tagNm, final boolean nillable, final long rn, final String val) {
        return "<" + tagNm +
                (rn != 0 ? " ROWNUM=\"" + rn +"\"" : "") +
                (Utils.isEmpty(val) && nillable ? " xsi:nil=\"true\"" : "") + ">" + val + "</" + tagNm + ">";
    }
    
    public static String writeXMLBodyElement(final XMLDocumentBody itm, final long rn) {
        return writeXMLBodyElement(itm.getName(), itm.getNillable(), rn, (Utils.isEmpty(itm.getFixed()) ? itm.getValue() : itm.getFixed()));
    }

    public static List<XMLDocumentHeader> getXmlHeader(final byte[] xsd) throws SAXException, ParserConfigurationException, IOException {
        List<XMLDocumentHeader> ret = new ArrayList<>();
        
        ByteArrayInputStream dct = new ByteArrayInputStream(xsd);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();            
        Document document = builder.parse(new InputSource(dct));
        document.getDocumentElement().normalize();

        NodeList list = document.getElementsByTagName("xs:complexType");

        for (int i = 0; i < list.getLength(); i++) {
            Node nd = list.item(i);
            Element node = (Element)nd;

            String nm = node.getAttribute("name"); if (nm.isEmpty()) nm = "?";

            if (nm.equals("DHead") || nm.equals("DeclareHeadType")) {
                if (nd.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList lstChild = nd.getChildNodes();
                    for (int temp=0; temp<lstChild.getLength(); temp++) {
                        Node _nd = lstChild.item(temp);
                        if (_nd.getNodeType() == Node.ELEMENT_NODE) {
                            if (((Element)_nd).getNodeName().equals("xs:sequence")) {
                                NodeList _lstChild = _nd.getChildNodes();
                                for (int _temp=0; _temp<_lstChild.getLength(); _temp++) {
                                    Node _node1 = _lstChild.item(_temp);
                                    if (_node1.getNodeType() == Node.ELEMENT_NODE) {
                                        Element _el = (Element)_node1;
                                        if (_el.getNodeName().equals("xs:element")) {
                                            String _name = _el.getAttribute("name"),
                                                   _nillable = _el.getAttribute("nillable"),
                                                   _fixed =  _el.getAttribute("fixed");
                                            ret.add(new XMLDocumentHeader(
                                                        _name,
                                                        Boolean.valueOf(Utils.isEmpty(_nillable) ? "false" : _nillable),
                                                        Utils.isEmpty(_fixed) ? "" : _fixed));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }
    
    public static List<XMLDocumentBody> getXmlBody(final IXmlParam xmlPrm, final byte[] xsd) throws SAXException, ParserConfigurationException, IOException {
        List<XMLDocumentBody> xmlBodyItems = new ArrayList<>();

        ByteArrayInputStream dct = new ByteArrayInputStream(xsd);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();            
        Document document = builder.parse(new InputSource(dct));
        document.getDocumentElement().normalize();

        NodeList list = document.getElementsByTagName("xs:complexType");

        for (int i = 0; i < list.getLength(); i++) {
            Node nd = list.item(i);
            Element node = (Element)nd;

            String nm = node.getAttribute("name"); if (nm.isEmpty()) nm = "?";

            if (nm.equals("DBody") || 
                nm.equals("HeaderType") ||
                nm.equals("TableType") ||
                nm.equals("RowType") ||
                nm.equals("FooterType") ||
                nm.equals("DodatokType") ||
                nm.equals("DODATOKType")
               ) {
                if (nd.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList lstChild = nd.getChildNodes();
                    for (int temp=0; temp<lstChild.getLength(); temp++) {
                        Node _nd = lstChild.item(temp);
                        if (_nd.getNodeType() == Node.ELEMENT_NODE) {
                            if (((Element)_nd).getNodeName().equals("xs:sequence")) {
                               xmlBodyItems = getXmlBodySequence(nm, xmlPrm, xmlBodyItems, _nd.getChildNodes());
                            }
                        }
                    }
                }
            }
        }
        
        return xmlBodyItems;
    }
    
    public static List<XMLDocumentBody> getXmlBodySequence(final String groupNm, final IXmlParam xmlPrm, List<XMLDocumentBody> xmlBodyItems, NodeList lstChilds) {
        for (int _temp=0; _temp<lstChilds.getLength(); _temp++) {
            Node _node1 = lstChilds.item(_temp);
            if (_node1.getNodeType() == Node.ELEMENT_NODE) {
                Element _el = (Element)_node1;
                if (_el.getNodeName().equals("xs:element")) {
                    String _name = _el.getAttribute("name"),
                           _nillable = _el.getAttribute("nillable"),
                           _fixed =  _el.getAttribute("fixed"),
                           _minOccurs = _el.getAttribute("minOccurs"),
                           _maxOccurs = _el.getAttribute("maxOccurs"),
                           _type = _el.getAttribute("type");
                    if (_minOccurs.equalsIgnoreCase("unbounded")) _minOccurs = "";
                    if (_maxOccurs.equalsIgnoreCase("unbounded")) _maxOccurs = "";

                    XMLDocumentBody bodyItm = new XMLDocumentBody(
                                                    groupNm,
                                                    _name,
                                                    _type,
                                                    Boolean.valueOf(Utils.isEmpty(_nillable) ? "false" : _nillable),
                                                    Utils.isEmpty(_fixed) ? "" : _fixed,
                                                    (Utils.isEmpty(_minOccurs) ? -1 : Integer.parseInt(_minOccurs)),
                                                    (Utils.isEmpty(_maxOccurs) ? -1 : Integer.parseInt(_maxOccurs)));
                    String _initVal = _fixed;
                    if (Utils.isEmpty(_fixed)) { _initVal = xmlPrm.getDefaultValue(_name); }
                    if (!Utils.isEmpty(_initVal)) { bodyItm.setValue(_initVal); }
                    
                    xmlBodyItems.add(bodyItm);
                } else if (_el.getNodeName().equals("xs:choice")) {
                    xmlBodyItems = getXmlBodyChoice(groupNm, xmlPrm, xmlBodyItems, _el.getChildNodes());
                }
            } 
        }
        return xmlBodyItems;
    }

    private static List<XMLDocumentBody> getXmlBodyChoice(final String groupNm, final IXmlParam xmlPrm, List<XMLDocumentBody> xmlBodyItems, NodeList lstChild) {
        PZDocumentData _doc = xmlPrm.getDoc();
        String cdoc = _doc.getCDoc(),
                _perMonth = (cdoc.startsWith("J02025") || cdoc.startsWith("F02025") || cdoc.startsWith("J02062") || cdoc.startsWith("F02062")) ? 
                                String.format("%2s", xmlPrm.getPerMonth()).replace(" ", "0") : String.valueOf(xmlPrm.getPerMonth());
        for (int temp=0; temp<lstChild.getLength(); temp++) {
            Node _nd = lstChild.item(temp);
            if (_nd.getNodeType() == Node.ELEMENT_NODE) {
                List<XMLDocumentBody> lstSeqElements = new ArrayList<>();
                if (((Element)_nd).getNodeName().equals("xs:sequence")) {
                    NodeList _lstChild = _nd.getChildNodes();
                    for (int _temp=0; _temp<_lstChild.getLength(); _temp++) {
                        Node _node1 = _lstChild.item(_temp);
                        if (_node1.getNodeType() == Node.ELEMENT_NODE) {
                            Element _el = (Element)_node1;
                            if (_el.getNodeName().equals("xs:element")) { 
                                String _name = _el.getAttribute("name"),
                                        _nillable = _el.getAttribute("nillable"),
                                        _fixed =  _el.getAttribute("fixed"),
                                        _minOccurs = _el.getAttribute("minOccurs"),
                                        _maxOccurs = _el.getAttribute("maxOccurs"),
                                        _type = _el.getAttribute("type");
                                lstSeqElements.add(new XMLDocumentBody(groupNm,
                                                    _name,
                                                    _type,
                                                    Boolean.valueOf(Utils.isEmpty(_nillable) ? "false" : _nillable),
                                                    Utils.isEmpty(_fixed) ? "" : _fixed,
                                                    (Utils.isEmpty(_minOccurs) ? -1 : Integer.parseInt(_minOccurs)),
                                                    (Utils.isEmpty(_maxOccurs) ? -1 : Integer.parseInt(_maxOccurs))));
                            }
                        }
                    }
                }else if (((Element)_nd).getNodeName().equals("xs:element")) {
                    Element _el = (Element)_nd;
                    String _name = _el.getAttribute("name"),
                            _nillable = _el.getAttribute("nillable"),
                            _fixed =  _el.getAttribute("fixed"),
                            _minOccurs = _el.getAttribute("minOccurs"),
                            _maxOccurs = _el.getAttribute("maxOccurs"),
                            _type = _el.getAttribute("type");
                    lstSeqElements.add(new XMLDocumentBody(groupNm,
                                        _name,
                                        _type,
                                        Boolean.valueOf(Utils.isEmpty(_nillable) ? "false" : _nillable),
                                        Utils.isEmpty(_fixed) ? "" : _fixed,
                                        (Utils.isEmpty(_minOccurs) ? -1 : Integer.parseInt(_minOccurs)),
                                        (Utils.isEmpty(_maxOccurs) ? -1 : Integer.parseInt(_maxOccurs))));
                }
                
                boolean isIncludeGroup = true;
                for (XMLDocumentBody seqNm : lstSeqElements) {
                    switch (_doc.getCDocStan()) {
                        case 1:
                        case 2:    
                            {
                                if (_doc.getCDocStan() == 1) {
                                    if (seqNm.getName().equals("HZ")) { // Звітня
                                        seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                    } else if (seqNm.getName().equals("HZY")) { // Рік
                                        seqNm.setValue(seqNm.getType().equalsIgnoreCase("dgchk") ? 1 : xmlPrm.getPerYear()); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                    }  
                                } else {
                                    if (seqNm.getName().equals("HZN")) { // Нова звітня
                                        seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                    } else if (seqNm.getName().equals("HZY")) { // Рік
                                        seqNm.setValue(xmlPrm.getPerYear()); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                    }
                                }
                                
                                switch (xmlPrm.getPerType()) {
                                    case 1: 
                                        if (seqNm.getName().equals("HZM")) {// Отчетный месяц
                                            seqNm.setValue(seqNm.getType().equalsIgnoreCase("dgchk") ? "1" : _perMonth); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        } else if (seqNm.getName().equals("HMONTH")) {// Отчетный месяц
                                            seqNm.setValue(seqNm.getType().equalsIgnoreCase("dgchk") ? 1 : xmlPrm.getPerMonth()); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        }                                         
                                        break;
                                    case 2: 
                                        if (seqNm.getName().equals("HZKV")) { // Отчетный квартал
                                            seqNm.setValue(xmlPrm.getPerMonth()/3); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        }
                                        switch(xmlPrm.getPerMonth()/3) {
                                            case 1: if (seqNm.getName().equals("H1KV")) {
                                                        seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                                    }
                                                    break;
                                            case 2: if (seqNm.getName().equals("HHY")){
                                                        seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                                    }
                                                    break;
                                            case 3: if (seqNm.getName().equals("H3KV")) {
                                                        seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                                    }
                                                    break;
                                            case 4: if (seqNm.getName().equals("HY")) {
                                                        seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                                    }
                                                    break;   
                                        }                                        
                                        break;
                                    case 3: 
                                        if (seqNm.getName().equals("HZKV")) {// Полугодие
                                            seqNm.setValue(2); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        } else if (seqNm.getName().equals("HHY")) { // Полугодие
                                            seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        }                                        
                                        break;
                                    case 4: 
                                        if (seqNm.getName().equals("HZKV")) { // 9 месяцев
                                            seqNm.setValue(3); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        } else if (seqNm.getName().equals("H3KV")) { // 9 месяцев
                                            seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        }
                                        break;
                                    case 5: 
                                        if (seqNm.getName().equals("HZKV")) { // Год
                                            seqNm.setValue(4); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        } else if (seqNm.getName().equals("HY")) { // Год
                                            seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        }
                                        break; 
                                }
                            }
                            break;
                        case 3:
                            {
                                if (seqNm.getName().equalsIgnoreCase("HZU")) { // Уточнююча
                                    seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                }
                                switch (xmlPrm.getPerType()) {
                                    case 1: if (seqNm.getName().equals("HZMP")) {
                                                seqNm.setValue(seqNm.getType().equalsIgnoreCase("dgchk") ? 1 : xmlPrm.getPerMonth()); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                            } else if (seqNm.getName().equals("HMONTH")) {
                                                seqNm.setValue(seqNm.getType().equalsIgnoreCase("dgchk") ? 1 : xmlPrm.getPerMonth()); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                            }
                                            break;
                                    case 2: if (seqNm.getName().equals("HZKVP")) {
                                                seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                            } 
                                            switch(xmlPrm.getPerMonth()/3) {
                                                case 1: 
                                                    if (seqNm.getName().equals("H1KV")) {
                                                        seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                                    }                                                        
                                                    break;
                                                case 2:
                                                    if (seqNm.getName().equals("HHY")) {
                                                        seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                                    }                                                        
                                                    break;

                                                case 3:
                                                    if (seqNm.getName().equals("H3KV")) {
                                                        seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                                    }
                                                    break;
                                                case 4:
                                                    if (seqNm.getName().equals("HY")) {
                                                        seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                                    }
                                                    break;
                                            }                                    
                                            break;
                                    case 3: if (seqNm.getName().equals("HZKVP")) {
                                                seqNm.setValue(2); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                            } else if (seqNm.getName().equals("HHY")) { // Полугодие
                                                seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                            }
                                            break; // Полугодие
                                    case 4: if (seqNm.getName().equals("HZKVP")) {
                                                seqNm.setValue(3); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                            } else if (seqNm.getName().equals("H3KV")) { // 9 месяцев
                                                seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                            }
                                            break; 
                                    case 5:
                                        if (seqNm.getName().equals("HZKVP")) {
                                            seqNm.setValue(4); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        } else if (seqNm.getName().equals("HY")) { // Рік
                                            seqNm.setValue(1); xmlBodyItems.add(seqNm); isIncludeGroup = false; break;
                                        }
                                        break;    
                                }
                               
                            }
                            break;
//                        default:
//                            String _initVal = seqNm.getFixed();
//                            if (Utils.isEmpty(_initVal)) { _initVal = xmlPrm.getDefaultValue(seqNm.getName()); }
//                            if (!Utils.isEmpty(_initVal)) { 
//                                seqNm.setValue(_initVal); xmlBodyItems.add(seqNm); isIncludeGroup =false;
//                            }
                    }
                }
                if (lstSeqElements.size() > 1) {
                    if (isIncludeGroup) { xmlBodyItems.add(lstSeqElements.get(0)); }
                }
            } 
        }
        
        return xmlBodyItems;
    } 
    
    public static List<XmlBodyRecExt> getXMLDataAsList(final byte[] xml) throws PzXmlException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            HandlerXmlBodyDataAsList hBody = new HandlerXmlBodyDataAsList();
            saxParser.parse(new ByteArrayInputStream(xml), hBody);
            return hBody.getRows();
        } catch (IOException ex) {
            throw new PzXmlException(ex.getMessage());
        } catch (ParserConfigurationException | SAXException ex) {
            throw new PzXmlException(ex.getLocalizedMessage());
        }
    }

    public static Map<String, XmlBodyDataRec> getXMLDataAsMap(final byte[] xml) throws PzXmlException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            HandlerXmlBodyDataAsMap hBody = new HandlerXmlBodyDataAsMap();
            saxParser.parse(new ByteArrayInputStream(xml), hBody);
            return hBody.getRows();
        } catch (IOException ex) {
            throw new PzXmlException(ex.getMessage());
        } catch (ParserConfigurationException | SAXException ex) {
            throw new PzXmlException(ex.getLocalizedMessage());
        }
    }

    public static XmlHeaderRec getXMLHeader(final byte[] xml) throws PzXmlException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            HandlerXmlHeader hHeader = new HandlerXmlHeader();
            saxParser.parse(new ByteArrayInputStream(xml), hHeader);
            return hHeader.getHeader();
        } catch (IOException ex) {
            throw new PzXmlException(ex.getMessage());
        } catch (ParserConfigurationException | SAXException ex) {
            throw new PzXmlException(ex.getLocalizedMessage());
        }
    }    
    
    @SuppressWarnings("empty-statement")
    public static List<XMLDocumentData> getXmlPrm(String srcXmlStr, String prmNms) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setValidating(false);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();        

        Document docSrc = docBuilder.parse(new ByteArrayInputStream(srcXmlStr.getBytes("UTF-8")));
        Node declarBodySrc = docSrc.getElementsByTagName("DECLARBODY").item(0);
       
        Pattern p = Pattern.compile("[^X]+(X)+[\\w]*");
        
        String[] arrPrms = null;
        if (prmNms.indexOf(";") != 0) arrPrms =  prmNms.split(";") ;
        else {
            arrPrms = new String[1];
            arrPrms[0] = prmNms;
        }
        List<XMLDocumentData> ret = new ArrayList<>();

        for (String prmNm : arrPrms) {
            Matcher m = p.matcher(prmNm);
            if (m.matches()) {
                NodeList listSrc = ((Element)declarBodySrc).getElementsByTagName(prmNm);
                for (int j = 0; j < listSrc.getLength(); j++) {
                    Node nodeSrc = listSrc.item(j);
                    if (nodeSrc.getNodeName().equals(prmNm)) {
                        String rowNum = "";
                        NamedNodeMap attrs = nodeSrc.getAttributes();
                        Node attNode = attrs.getNamedItem("ROWNUM");
                        if (attNode != null) {
                            rowNum = attNode.getTextContent();
                        }
                        ret.add(new XMLDocumentData(prmNm, nodeSrc.getTextContent()));
                    }
                }
            } else {
                NodeList listSrc = ((Element)declarBodySrc).getElementsByTagName(prmNm);
                if (listSrc.getLength() != 0) {
                    for (int j = 0; j < listSrc.getLength(); j++) {
                        Node nodeSrc = listSrc.item(j);
                        ret.add(new XMLDocumentData(prmNm, nodeSrc.getTextContent()));
                    }
                }
            }
        }
        
        return ret;
    }    
}
