/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.xml.records;

/**
 *
 * @author maestro
 */
public class XMLDocumentData {
    private String name;
    private String val;

    public XMLDocumentData(String nm, String val) {
        this.name = nm;
        this.val = val;
    }

    public String getCDocRowc() { return name; }

    public String getCDocRowcVal() { return val; }  
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" * %s = %s", name, val));
        return sb.toString();
    }
}
