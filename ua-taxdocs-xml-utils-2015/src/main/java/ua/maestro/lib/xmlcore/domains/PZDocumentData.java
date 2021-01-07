package ua.maestro.lib.xmlcore.domains;

/**
 *
 * @author maestro
 */
public class PZDocumentData {
    private String iddocref;
    private String iddoc;
    private String tin;
    private int periodYear;
    private int periodMonth;
    private int periodType;
    private long cDocCnt = 1;
    private int cDocType = 0;
    private int cDocStan = 1;
    private int csti = 0;
    private int cstiOrig = 0;
    private String cdoc;
    private String docName;
    private String fileName;
    private long status = 0;
    private String xml;
    private String idorg;
    private String iduser;

    public PZDocumentData() {
        
    }
    
    public PZDocumentData(String iddoc) {
        this.iddoc = iddoc;
    }
    
    public String getIddocref() { return iddocref; }

    public void setIddocref(String v) { this.iddocref = v; }

    public String getIddoc() { return iddoc; }
    
    public void setIddoc(String v) { this.iddoc = v; }

    public String getCDoc() { return cdoc; }

    public void setCDoc(String cdoc) { this.cdoc = cdoc; }

    public long getStatus() { return status; }

    public void setStatus(long v) { this.status = v; }
   
    public String getDocName() { return docName; }

    public void setDocName(String docName) { this.docName = docName; }

    public int getPeriodYear() { return periodYear; }

    public void setPeriodYear(int periodYear) { this.periodYear = periodYear; }

    public int getPeriodMonth() { return periodMonth; }

    public void setPeriodMonth(int periodMonth) { this.periodMonth = periodMonth; }

    public int getPeriodType() { return periodType; }

    public void setPeriodType(int periodType) { this.periodType = periodType; }
    
    public String getTin() { return tin; }
    
    public void setTin(String v) { this.tin = v; }
    
    public int getCSti() { return csti; }
    
    public void setCSti(int v) { this.csti = v; }
    
    public int getCStiOrig() { return cstiOrig; }
    
    public void setCStiOrig(int v) { this.cstiOrig = v; }

    public long getCDocCnt() { return cDocCnt; }

    public void setCDocCnt(long cDocCnt) { this.cDocCnt = cDocCnt; }

    public int getCDocType() { return cDocType; }

    public void setCDocType(int cDocType) { this.cDocType = cDocType; }

    public int getCDocStan() { return cDocStan; }

    public void setCDocStan(int cDocStan) { this.cDocStan = cDocStan; }

    public String getXml() { return xml; }

    public void setXml(String xml) { this.xml = xml; }

    public String getFileName() { return fileName; }

    public void setFileName(String v) { this.fileName = v; }

    public String getIdorg() { return idorg; }

    public void setIdorg(String idorg) { this.idorg = idorg; }

    public String getIduser() { return iduser; }

    public void setIduser(String iduser) { this.iduser = iduser; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("[%s, %s, %d, %d, %d] %s Статус=%d", this.cdoc, this.fileName, this.periodYear, this.periodMonth, this.periodType, this.docName, this.status));
        sb.append(String.format("\n\t № документа=%s, стан документа=%s, тип документа=%s", this.cDocCnt, this.cDocStan, this.cDocType));
        sb.append(String.format("\n\t Код ЄДРПОУ=%s, КодДПІ(копія)=%d, Код ДПІ(оригинал)=%d", this.tin, this.csti, this.cstiOrig));
        sb.append(String.format("\n\t XML:%s", this.xml));
        
        return sb.toString();
    }
}
