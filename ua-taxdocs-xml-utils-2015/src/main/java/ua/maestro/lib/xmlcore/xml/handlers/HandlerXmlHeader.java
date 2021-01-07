/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.xml.handlers;

import ua.maestro.lib.xmlcore.xml.data.XmlHeaderRec;
import ua.maestro.lib.xmlcore.xml.data.XmlLinkedDocRec;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author maestro
 */
public class HandlerXmlHeader extends DefaultHandler {
    private XmlHeaderRec header = null;
    private XmlLinkedDocRec linkedDoc = null;
    public XmlHeaderRec getHeader() { return header; }
  
    boolean bCDoc = false,
            bCDocSub = false,
            bCDocVer = false,
            bCReg = false,
            bCRaj = false,
            bCStiOrig = false,
            bPeriodYear = false,
            bPeriodMonth = false,
            bPeriodType = false,
            bTin = false,
            bCDocStan = false,
            bCDocType = false,
            bCDocCnt = false,
            bDFill = false,
            bLinkedDocs = false,
            bLnkCDoc = false,
            bLnkCDocSub = false,
            bLnkCDocVer = false,
            bLnkCDocStan = false,
            bLnkCDocType = false,
            bLnkCDocCnt = false,
            bLnkFName = false;
            
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("DECLARHEAD")) { 
            header = new XmlHeaderRec();
        }
        
        if (header != null) {
            if (qName.equalsIgnoreCase("LINKED_DOCS")) {
                bLinkedDocs = true;
            } else if (qName.equalsIgnoreCase("DOC")) {
                if (bLinkedDocs) { 
                    int attrNum = Integer.parseInt(attributes.getValue("NUM")),
                        attrType = Integer.parseInt(attributes.getValue("TYPE"));
                    linkedDoc = new XmlLinkedDocRec(attrNum, attrType);
                }
            } else if (qName.equalsIgnoreCase("TIN")) {
                bTin = true;
            } else if (qName.equalsIgnoreCase("C_REG")) {
                bCReg = true;
            } else if (qName.equalsIgnoreCase("C_RAJ")) {
                bCRaj = true;
            } else if (qName.equalsIgnoreCase("C_STI_ORIG")) {
                bCStiOrig = true;
            } else if (qName.equalsIgnoreCase("D_FILL")) {
                bDFill = true;
            } else if (qName.equalsIgnoreCase("PERIOD_YEAR")) {
                bPeriodYear = true;
            } else if (qName.equalsIgnoreCase("PERIOD_MONTH")) {
                bPeriodMonth = true;
            } else if (qName.equalsIgnoreCase("PERIOD_TYPE")) {
                bPeriodType = true;
            }

            if (bLinkedDocs) {
                if (qName.equalsIgnoreCase("C_DOC")) { bLnkCDoc = true;
                } else if (qName.equalsIgnoreCase("C_DOC_SUB")) { bLnkCDocSub = true;
                } else if (qName.equalsIgnoreCase("C_DOC_VER") || qName.equalsIgnoreCase("VER")) {  bLnkCDocVer = true;
                } else if (qName.equalsIgnoreCase("C_DOC_CNT")) { bLnkCDocCnt = true;
                } else if (qName.equalsIgnoreCase("C_DOC_STAN")) { bLnkCDocStan = true;
                } else if (qName.equalsIgnoreCase("C_DOC_TYPE")) { bLnkCDocType = true;
                } else if (qName.equalsIgnoreCase("FILENAME")) {
                    bLnkFName = true;
                }            
            } else {
                if (qName.equalsIgnoreCase("C_DOC")) { bCDoc = true;
                } else if (qName.equalsIgnoreCase("C_DOC_SUB")) { bCDocSub = true;
                } else if (qName.equalsIgnoreCase("C_DOC_VER") || qName.equalsIgnoreCase("VER")) { bCDocVer = true;
                } else if (qName.equalsIgnoreCase("C_DOC_CNT")) { bCDocCnt = true;
                } else if (qName.equalsIgnoreCase("C_DOC_STAN")) { bCDocStan = true;
                } else if (qName.equalsIgnoreCase("C_DOC_TYPE")) { 
                    bCDocType = true;
                }
            }
        }
    }    
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("LINKED_DOCS")) {
            bLinkedDocs = false;

            bLnkCDoc = false;
            bLnkCDocSub = false;
            bLnkCDocVer = false;
            bLnkCDocStan = false;
            bLnkCDocType = false;
            bLnkCDocCnt = false;
            bLnkFName = false;
        } else if (qName.equalsIgnoreCase("DOC")) {
            bLnkCDoc = false;
            bLnkCDocSub = false;
            bLnkCDocVer = false;
            bLnkCDocStan = false;
            bLnkCDocType = false;
            bLnkCDocCnt = false;
            bLnkFName = false;
            
            header.addLinkedDoc(linkedDoc);
            linkedDoc = null;
        }
    }
 
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (bTin) { header.setTin(new String(ch, start, length)); bTin = false; }
        if (bPeriodYear) { header.setPeriodYear(Integer.parseInt(new String(ch, start, length))); bPeriodYear = false; }
        if (bPeriodMonth) { header.setPeriodMonth(Integer.parseInt(new String(ch, start, length))); bPeriodMonth = false; }
        if (bPeriodType) { header.setPeriodType(Integer.parseInt(new String(ch, start, length))); bPeriodType = false; }
        if (bCReg) { header.setCReg(Integer.parseInt(new String(ch, start, length))); bCReg = false; }
        if (bCRaj) { header.setCRaj(Integer.parseInt(new String(ch, start, length))); bCRaj = false; }
        if (bCStiOrig) { header.setCStiOrig(Integer.parseInt(new String(ch, start, length))); bCStiOrig = false; }
        if (bDFill) { header.setDFill(new String(ch, start, length)); bDFill = false; }
        
        if (bLinkedDocs) {
            if (bLnkCDoc) { linkedDoc.setCDoc(new String(ch, start, length)); bLnkCDoc = false; }
            if (bLnkCDocSub) { linkedDoc.setCDocSub(new String(ch, start, length)); bLnkCDocSub = false; }
            if (bLnkCDocVer) { linkedDoc.setCDocVer(new String(ch, start, length)); bLnkCDocVer = false; }
            if (bLnkCDocType) { linkedDoc.setCDocType(Integer.parseInt(new String(ch, start, length))); bLnkCDocType = false; }
            if (bLnkCDocStan) { linkedDoc.setCDocStan(Integer.parseInt(new String(ch, start, length))); bLnkCDocStan = false; }
            if (bLnkCDocCnt) { linkedDoc.setCDocCnt(Integer.parseInt(new String(ch, start, length))); bLnkCDocCnt = false; }
            if (bLnkFName) { linkedDoc.setFname(new String(ch, start, length)); bLnkFName = false; }
        } else {
            if (bCDoc) { header.setCDoc(new String(ch, start, length)); bCDoc = false; }
            if (bCDocSub) { header.setCDocSub(new String(ch, start, length)); bCDocSub = false; }
            if (bCDocVer) { header.setCDocVer(new String(ch, start, length)); bCDocVer = false; }
            if (bCDocType) { header.setCDocType(Integer.parseInt(new String(ch, start, length))); bCDocType = false; }
            if (bCDocStan) { header.setCDocStan(Integer.parseInt(new String(ch, start, length))); bCDocStan = false; }
            if (bCDocCnt) { header.setCDocCnt(Integer.parseInt(new String(ch, start, length))); bCDocCnt = false; }            
        }
    }    
}
