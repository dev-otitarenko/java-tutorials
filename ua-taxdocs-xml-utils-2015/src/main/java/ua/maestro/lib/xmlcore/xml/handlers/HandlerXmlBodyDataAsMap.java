package ua.maestro.lib.xmlcore.xml.handlers;

import ua.maestro.lib.xmlcore.xml.data.XmlBodyDataRec;
import ua.maestro.lib.xmlcore.xml.data.XmlBodyRec;
import ua.maestro.lib.commons.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maestro on 19.05.17.
 */
public class HandlerXmlBodyDataAsMap extends DefaultHandler {
    private Map<String, XmlBodyDataRec> rows = null;
    private boolean bDeclarBody = false;
    private String attrName = "";
    private String attrValue = "";
    private long attrRownum = 0;
    private boolean attrXsiNil = false;

    public Map<String, XmlBodyDataRec> getRows() {
        return rows;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("DECLARBODY")) {
            this.rows = new HashMap();
            this.bDeclarBody = true;
        }

        if (this.bDeclarBody) {
            this.attrName = qName;
            this.attrValue = null;
            this.attrRownum = 0L;
            this.attrXsiNil = false;
            String val = attributes.getValue("ROWNUM");
            if (!Utils.isEmpty(val)) {
                this.attrRownum = Long.valueOf(val);
            }

            val = attributes.getValue("xsi:nil");
            if (!Utils.isEmpty(val)) {
                this.attrXsiNil = Boolean.parseBoolean(val);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (this.bDeclarBody) {
            if (!Utils.isEmpty(this.attrName) && !Utils.isEmpty(this.attrValue)) {
                if (this.attrRownum == 0L) {
                    this.rows.put(this.attrName, new XmlBodyDataRec(this.attrRownum, this.attrValue));
                } else {
                    XmlBodyDataRec keyValue = (XmlBodyDataRec) this.rows.get(this.attrName);
                    if (keyValue == null) {
                        keyValue = new XmlBodyDataRec(this.attrRownum, (Object) null);
                    }

                    if (keyValue.getRownum() < this.attrRownum) {
                        keyValue.setRownum(this.attrRownum);
                    }

                    List<XmlBodyRec> fldRows = (List) keyValue.getValue();
                    if (fldRows == null) {
                        fldRows = new ArrayList();
                    }

                    ((List) fldRows).add(new XmlBodyRec(this.attrRownum, this.attrValue));
                    keyValue.setValue(fldRows);
                    this.rows.put(this.attrName, keyValue);
                }
            }

            this.attrName = "";
            this.attrValue = null;
            this.attrRownum = 0L;
            this.attrXsiNil = false;

        }

        if (qName.equalsIgnoreCase("DECLARBODY")) {
            this.bDeclarBody = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (this.bDeclarBody) {
            int end = (start + length) - 1;
            while (ch[start] <= '\u0020') {
                if (start == end)
                    return;
                start++;
                length--;
            }
            while (ch[end] <= '\u0020') {
                if (end == start)
                    return;
                length--;
                end--;
            }
            if (attrValue == null) {
                attrValue = new String(ch, start, length);
            } else {
                attrValue += new String(ch, start, length);
            }

        }
    }
}
