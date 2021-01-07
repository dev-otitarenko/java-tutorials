package ua.maestro.lib.xmlcore.xml.handlers;

import ua.maestro.lib.xmlcore.xml.data.XmlBodyRecExt;
import ua.maestro.lib.commons.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maestro on 21.03.17.
 */
public class HandlerXmlBodyDataAsList extends DefaultHandler {
    private List<XmlBodyRecExt> rows = null;
    private boolean bDeclarBody = false;
    private String attrName = "";
    private String attrValue = "";
    private long attrRownum = 0;
    private boolean attrXsiNil = false;

    public List<XmlBodyRecExt> getRows() {
        return rows;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("DECLARBODY")) {
            rows = new ArrayList<>();
            bDeclarBody = true;
        }

        if (bDeclarBody) {
            attrName = qName;
            attrValue = "";
            attrRownum = 0;
            attrXsiNil = false;

            String val = attributes.getValue("ROWNUM");
            if (!Utils.isEmpty(val)) {
                attrRownum = Long.valueOf(val);
            }
            val = attributes.getValue("xsi:nil");
            if (!Utils.isEmpty(val)) {
                attrXsiNil = Boolean.parseBoolean(val);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (bDeclarBody) {
            if (!Utils.isEmpty(attrName) && !Utils.isEmpty(attrValue)) {
                XmlBodyRecExt rec = new XmlBodyRecExt(attrName, attrRownum, attrValue);
                rows.add(rec);
            }
            attrName = "";
            attrValue = "";
            attrRownum = 0;
            attrXsiNil = false;
        }

        if (qName.equalsIgnoreCase("DECLARBODY")) {
            bDeclarBody = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (bDeclarBody) {
            attrValue = new String(new String(ch, start, length));
        }
    }
}
