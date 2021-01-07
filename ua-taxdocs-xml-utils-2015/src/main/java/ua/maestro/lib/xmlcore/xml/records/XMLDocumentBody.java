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
public class XMLDocumentBody {
    private String groupNm;
    private String name;
    private boolean nillable;
    private String fixed;
    private String type;
    private int minOccurs = -1;
    private int maxOccurs = -1;
    private String value = "";

    public XMLDocumentBody(String groupNm, String nm, String type, boolean nillable, String fixed, int minOccurs, int maxOccurs) {
        this.groupNm = groupNm;
        this.name = nm;
        this.type = type;
        this.nillable = nillable;
        this.fixed = fixed;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    public String getGroupNm() { return groupNm; }

    public String getName() { return name; }

    public boolean getNillable() { return nillable; }

    public String getFixed() { return fixed; } 

    public String getType() { return type; }

    public int getMinOccurs() { return minOccurs; }

    public int getMaxOccurs() { return maxOccurs; }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }

    public void setValue(int v) { setValue(String.valueOf(v)); }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" * [%s, %s] Nillable:%b, Fixed:%s, Value:%s", this.name, this.type, this.nillable, this.fixed, this.value));
        return sb.toString();
    }
}
