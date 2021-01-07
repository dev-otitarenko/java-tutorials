/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.xsd.data;

import org.xml.sax.SAXParseException;

/**
 *
 * @author maestro
 */
public class XsdErrorRec {
    private int type;
    private String message;

    public XsdErrorRec(int tp, SAXParseException ex) {
        this.type = tp;
        this.message = ex.getLocalizedMessage();
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return (" * " + this.message);            
    }      
}
