/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.provider.interfaces;

import ua.maestro.lib.xmlcore.domains.PZDocInput;
import ua.maestro.lib.xmlcore.domains.PZPayerData;
import ua.maestro.lib.xmlcore.domains.PZDocumentData;
import ua.maestro.lib.xmlcore.xml.data.XmlBodyDataRec;
import ua.maestro.lib.xmlcore.xml.data.XmlBodyRecExt;
import ua.maestro.lib.xmlcore.xml.data.XmlHeaderRec;
import ua.maestro.lib.xmlcore.xml.data.XmlLinkedDocRec;
import ua.maestro.lib.xmlcore.xml.exceptions.PzXmlException;
import ua.maestro.lib.xmlcore.xml.records.XMLDocumentData;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 * Describes the interface to process with abstracts XML
 * @author maestro
 */
public interface IProvider {
    boolean getType(final PZDocumentData doc);
    
    String getFileName(final PZPayerData pay, final PZDocumentData doc);
    
    HashMap<String, Object> parseFileName(String fname);
    
    XmlHeaderRec getXMLHeader(final byte[] xml) throws PzXmlException;
    
    List<XmlBodyRecExt> getXMLDataAsList(final byte[] xml) throws PzXmlException;
    
    Map<String, XmlBodyDataRec> getXMLDataAsMap(final byte[] xml) throws PzXmlException;
    
    List<XMLDocumentData> getXmlPrm(String srcXmlStr, String prmNms) throws SAXException, ParserConfigurationException, IOException;
    
    String checkImpMainrptXml(final PZDocumentData prm, final List<XmlLinkedDocRec> linkDocs) throws SAXException, IOException, TransformerException, ParserConfigurationException;
    
    String checkImpDodrptXml(final PZDocumentData prm, final PZDocumentData prmMain, final String fileNameRef) throws SAXException, IOException, TransformerException, ParserConfigurationException;
    
    String createXml(final PZDocumentData doc,
                    final String xsdTmpl,
                    final PZPayerData pay,
                    final List<PZDocInput> docVars,
                    final long cnt4Curdate,
                    final String sSoftware) throws PzXmlException;
    
    String copyXml(final PZDocumentData doc,
                   final String xsdTmpl,
                   final PZPayerData pay,
                   final List<PZDocInput> docVars,
                   final String srcXmlStr,
                   final String sSoftware) throws PzXmlException;

    String copyXml(final PZDocumentData doc,
                   final String xsdTmpl,
                   final PZPayerData pay,
                   final List<PZDocInput> docVars,
                   final Map<String, BigDecimal> srcData,
                   final String sSoftware) throws PzXmlException;
    
    
   String mergeData (final PZDocumentData doc,
                             final String xsdTmpl,
                             final PZPayerData pay,
                             final List<PZDocInput> docVars,
                             final List<Map<String, XmlBodyDataRec>> docsData,
                             final String sSoftware) throws PzXmlException;
    
    String formXml (final PZDocumentData prm,
                    final String xsdTmpl,
                    final PZPayerData pay,
                    final List<PZDocumentData> docs,
                    final int lnkType,
                    final String sSoftware) throws PzXmlException;
    
    String formXml (final PZDocumentData prm,
                    final String xsdTmpl,
                    final PZPayerData pay,
                    final List<XmlLinkedDocRec> lnkDocs,
                    final String sSoftware) throws PzXmlException;    
}
