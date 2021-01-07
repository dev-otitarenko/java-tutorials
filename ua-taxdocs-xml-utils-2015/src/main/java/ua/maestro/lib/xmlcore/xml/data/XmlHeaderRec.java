/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.xml.data;

import ua.maestro.lib.xmlcore.utils.PzUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maestro
 */
public class XmlHeaderRec implements Serializable{
    private String tin;
    private String c_doc;
    private String c_doc_sub;
    private String c_doc_ver;
    private int c_doc_type;
    private int c_doc_stan;
    private int c_doc_cnt;
    private int c_reg;
    private int c_raj;
    private int period_month;
    private int period_year;
    private int period_type;
    private int c_sti_orig;
    private String d_fill;
    private List<XmlLinkedDocRec> docs = new ArrayList<>();
    
    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getFullCDoc() {
        return PzUtils.getCDoc(c_doc, c_doc_sub, c_doc_ver);
    }
    
    public String getCDoc() {
        return c_doc;
    }

    public void setCDoc(String c_doc) {
        this.c_doc = c_doc;
    }

    public String getCDocSub() {
        return c_doc_sub;
    }

    public void setCDocSub(String c_doc_sub) {
        this.c_doc_sub = c_doc_sub;
    }

    public String getCDocVer() {
        return c_doc_ver;
    }

    public void setCDocVer(String c_doc_ver) {
        this.c_doc_ver = c_doc_ver;
    }

    public int getCDocType() {
        return c_doc_type;
    }

    public void setCDocType(int c_doc_type) {
        this.c_doc_type = c_doc_type;
    }

    public int getCDocStan() {
        return c_doc_stan;
    }

    public void setCDocStan(int c_doc_stan) {
        this.c_doc_stan = c_doc_stan;
    }

    public int getCDocCnt() {
        return c_doc_cnt;
    }

    public void setCDocCnt(int c_doc_cnt) {
        this.c_doc_cnt = c_doc_cnt;
    }

    public int getCReg() {
        return c_reg;
    }

    public void setCReg(int c_reg) {
        this.c_reg = c_reg;
    }

    public int getCRaj() {
        return c_raj;
    }

    public void setCRaj(int c_raj) {
        this.c_raj = c_raj;
    }

    public int getPeriodMonth() {
        return period_month;
    }

    public void setPeriodMonth(int period_month) {
        this.period_month = period_month;
    }

    public int getPeriodYear() {
        return period_year;
    }

    public void setPeriodYear(int period_year) {
        this.period_year = period_year;
    }

    public int getPeriodType() {
        return period_type;
    }

    public void setPeriodType(int period_type) {
        this.period_type = period_type;
    }

    public int getCStiOrig() {
        return c_sti_orig;
    }

    public void setCStiOrig(int c_sti_orig) {
        this.c_sti_orig = c_sti_orig;
    }

    public String getDFill() {
        return d_fill;
    }

    public void setDFill(String d_fill) {
        this.d_fill = d_fill;
    }
    
    public void addLinkedDoc (XmlLinkedDocRec doc) {
        this.docs.add(doc);
    }
    
    public List<XmlLinkedDocRec> getLinkedDocs() { 
        return docs;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n Tin: "); sb.append(this.getTin());
        sb.append("\n CReg: "); sb.append(this.getCReg());
        sb.append("\n CRaj: "); sb.append(this.getCRaj());
        sb.append("\n CStiOrig: "); sb.append(this.getCStiOrig());
        sb.append("\n PeriodYear: "); sb.append(this.getPeriodYear());
        sb.append("\n PeriodMonth: "); sb.append(this.getPeriodMonth());
        sb.append("\n PeriodType: "); sb.append(this.getPeriodType());
        sb.append("\n CDoc: "); sb.append(this.getFullCDoc());
        sb.append("\n CDocStan: "); sb.append(this.getCDocStan());
        sb.append("\n CDocCnt: "); sb.append(this.getCDocCnt());
        sb.append("\n CDocType: "); sb.append(this.getCDocType());
        sb.append("\n DFill: "); sb.append(this.getDFill());
        
        for(XmlLinkedDocRec doc : this.docs) {
            String expr = String.format("\n *[%d, %d, %s] CDoc:%s, CDocStan:%d, CDocType:%d, CDocCnt:%d", doc.getNum(), doc.getType(), doc.getFname(), PzUtils.getCDoc(doc.getCDoc(), doc.getCDocSub(), String.valueOf(doc.getCDocVer())), doc.getCDocStan(), doc.getCDocType(), doc.getCDocCnt());
            sb.append(expr);
        }
        
        return sb.toString();
    }
}
