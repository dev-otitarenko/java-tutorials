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
public class XMLDocumentHeader {
    private String name;
    private boolean nillable;
    private String fixed;

    public XMLDocumentHeader(String nm, boolean nillable, String fixed) {
        this.name = nm;
        this.nillable = nillable;
        this.fixed = fixed;
    }

    public String getName() { return name; }

    public boolean getNillable() { return nillable; }

    public String getFixed() { return fixed; }  
     
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" * [%s] Nillable:%b, Fixed:%s", this.name, this.nillable, this.fixed ));
        return sb.toString();
    }
}
