/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.xml.data;

import ua.maestro.lib.xmlcore.utils.PzUtils;
import java.io.Serializable;

/**
 *
 * @author maestro
 */
public class XmlLinkedDocRec implements Serializable {
        private int num;
        private int type;        
        private String c_doc;
        private String c_doc_sub;
        private String c_doc_ver;
        private int c_doc_type;
        private int c_doc_stan;
        private long c_doc_cnt;
        private String fname;

        public XmlLinkedDocRec (int num, int type) {
            this.num = num;
            this.type = type;
        }
        
        public XmlLinkedDocRec(int num, int type, String cdoc, int docType, int docStan, long docCnt, String fname) {
            this.num = num;
            this.type = type;
            this.c_doc = cdoc.substring(0, 3);
            this.c_doc_sub = cdoc.substring(3, 6);
            this.c_doc_ver = (cdoc.startsWith("E")) ?
                                cdoc.substring(cdoc.length(), cdoc.length()) : cdoc.substring(6, 8).replaceAll("^0+(?=\\d+$)", "");
            this.c_doc_type = docType;
            this.c_doc_stan = docStan;
            this.c_doc_cnt = docCnt;
            this.fname = fname;
        }        

        public int getNum() { return num; }

        public int getType() { return type; }

        public String getFullCDoc() {
            return PzUtils.getCDoc(c_doc, c_doc_sub, String.valueOf(c_doc_ver));
        }
        
        public String getCDoc() { return c_doc; }

        public void setCDoc(String v) { this.c_doc = v; }
        
        public String getCDocSub() { return c_doc_sub; }
        
        public void setCDocSub(String v) { this.c_doc_sub = v; }

        public String getCDocVer() { return c_doc_ver; }
        
        public void setCDocVer(String v) { this.c_doc_ver = v; }

        public int getCDocType() { return c_doc_type; }
        
        public void setCDocType(int v) { this.c_doc_type = v; }

        public int getCDocStan() { return c_doc_stan; }
        
        public void setCDocStan(int v) { this.c_doc_stan = v; }        

        public long getCDocCnt() { return c_doc_cnt; }

        public void setCDocCnt(long v) { this.c_doc_cnt = v; }        
        
        public String getFname() { return fname; }
        
        public void setFname(String v) { this.fname = v; }    
}
