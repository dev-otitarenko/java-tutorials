package ua.maestro.lib.xmlcore.domains;

import ua.maestro.lib.commons.Utils;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author maestro
 */
@XmlRootElement
public class PZPayerData implements Serializable {
    private String id = "";
    private String code = "";
    private String tin = "";
    private String name = "";
    private int c_sti = 0;
    private String stiName = "";
    private String stiTin = "";
    private String innDir = "";
    private String pibDir = "";
    private String telDir = "";
    private String innBuh = "";
    private String pibBuh = "";
    private String telBuh = "";
    private String email = "";
    private String tel = "";
    private String fax = "";
    private String zip = "";
    private String address = "";
    private int adrCRegion = 0;
    private String adrRegionName = "";
    private int adrCity = 0;
    private String adrCityName = "";
    private String vatId = "";
    private String vatNumSvid = "";
    private String kved = "";
    private String kvedName = "";
    private String opf = "";
    private String opfName = "";
    private String coogu = "";
    private String cooguName = "";
    private String kfv = "";
    private String kfvName = "";
    private String okonx = "";
    private String okonxName = "";
    private String c_territ = "";
    private String territName = "";
    private String c_pfu = "";
    private String pfuFil = "";
    private String c_sscu = "";
    private String bankMfo = "";
    private String bankName = "";
    private String bankAccount = "";

    public String getId() { return id; }

    public void setId(String v) { this.id = v; }

    public String getCode() { return code; }

    public void setCode(String v) { this.code = v; }

    public String getTin() { return tin; }
    
    public void setTin(String v) { this.tin = v; }

    public String getName() { return name; }
    
    public void setName(String v) { this.name = v; }

    public int getCSti() { return c_sti; }

    public void setCSti(int v) { this.c_sti = v; }

    public String getStiName() { return stiName; }

    public void setStiName(String v) { this.stiName = v; }

    public String getInnDir() { return innDir; }

    public void setInnDir(String v) { this.innDir = v; }

    public String getPibDir() { return pibDir; }

    public void setPibDir(String v) { this.pibDir = v; }

    public String getInnBuh() { return innBuh; }

    public void setInnBuh(String v) { this.innBuh = v; }

    public String getPibBuh() { return pibBuh; }

    public void setPibBuh(String v) { this.pibBuh = v; }
    
    public String getEmail() { return email; }

    public void setEmail(String v) { this.email = v; }  
    
    public String getTelDir() { return telDir; }

    public void setTelDir(String v) { this.telDir = v; }

    public String getTelBuh() { return telBuh; }

    public void setTelBuh(String v) { this.telBuh = v; }

    public String getTel() { return tel; }

    public void setTel(String v) { this.tel = v; }

    public String getFax() { return fax; }

    public void setFax(String v) { this.fax = v; }
    
    public String getZip() { return zip; }

    public void setZip(String v) { this.zip = v; } 
    
    public String getVatId() { return vatId; }

    public void setVatId(String v) { this.vatId = v; }

    public String getVatNompp() { return vatNumSvid; }

    public void setVatNompp(String v) { this.vatNumSvid = v; }
    
    public String getAddress() { return address; }
    
    public void setAddress(String v) { this.address = v; }
    
    public int getAdrCRegion() { return adrCRegion; }
    
    public void setAdrCRegion(int v) { this.adrCRegion = v; }

    public String getAdrRegionName() { return adrRegionName; }
    
    public void setAdrRegionName(String v) { this.adrRegionName = v; }
    
    public int getAdrCity() { return adrCity; }
    
    public void setAdrCity(int v) { this.adrCity = v; }
  
    public String getAdrCityName() { return adrCityName; }
    
    public void setAdrCityName(String v) { this.adrCityName = v; }
      
    public String getKved() { return kved; }

    public void setKved(String v) { this.kved = v; }

    public String getKvedName() { return kvedName; }

    public void setKvedName(String v) { this.kvedName = v; }     
    
    public String getCPfu() { return c_pfu; }

    public void setCPfu(String v) {this.c_pfu = v; }

    public String getPfuFill() { return pfuFil; }

    public void setPfuFill(String v) { this.pfuFil = v; } 
    
    public int getFacemode() {
        return this.tin.length() == 10 ? 2 : 1;
    }

    public String getStiTin() { return stiTin; }

    public void setStiTin(String v) { this.stiTin = v; }  

    public String getCSSCU() { return c_sscu; }

    public void setCSSCU(String v) { this.c_sscu = v; }

    public String getOpf() { return opf; }

    public void setOpf(String v) { this.opf = v; }

    public String getOpfName() { return opfName; }

    public void setOpfName(String v) { this.opfName = v; }

    public String getCoogu() { return coogu; }

    public void setCoogu(String v) { this.coogu = v; }

    public String getCooguName() { return cooguName; }

    public void setCooguName(String v) { this.cooguName = v; }

    public String getKfv() { return kfv; }

    public void setKfv(String v) { this.kfv = v; }

    public String getKfvName() { return kfvName; }

    public void setKfvName(String v) { this.kfvName = v; }

    public String getOkonx() { return okonx; }

    public void setOkonx(String v) { this.okonx = v; }

    public String getOkonxName() { return okonxName; }

    public void setOkonxName(String v) { this.okonxName = v; }
    
    public String getCTerrit() { return c_territ; }

    public void setCTerrit(String v) { this.c_territ = v; }

    public String getTerritName() { return territName; }

    public void setTerritName(String v) { this.territName = v; }

    public String getPfuFil() { return pfuFil; }

    public void setPfuFil(String v) { this.pfuFil = v; }

    public String getBankMfo() { return bankMfo; }

    public void setBankMfo(String v) { this.bankMfo = v; }

    public String getBankName() { return bankName; }

    public void setBankName(String v) { this.bankName = v; }

    public String getBankAccount() { return bankAccount; }

    public void setBankAccount(String v) { this.bankAccount = v; }
    
    @Override
    public String toString() {
        return " * Предприятие [" + this.id + "] " + this.tin + "\"" + this.name + "\"" +
               "\n ИПН: " + this.vatId + (!Utils.isEmpty(this.vatNumSvid) ? " #" + this.vatNumSvid : "") +
               "\n Директор: " + this.innDir + " " + this.pibDir + (!Utils.isEmpty(this.telDir) ? ", Тел: " + this.telDir : "") +
               "\n Бухгалтер: " + this.innBuh + " " + this.pibBuh + (!Utils.isEmpty(this.telBuh) ? ", Тел: " + this.telBuh : "") +
               "\n Адресные данные: " + this.zip + 
                    (!Utils.isEmpty(this.adrRegionName) ? " " + this.adrRegionName : "") + 
                    (!Utils.isEmpty(this.adrCityName) ? " " + this.adrCityName : "") + 
                    (!Utils.isEmpty(this.address) ? " " + this.address : "") +
               (!Utils.isEmpty(this.tel) ? "\n Телефон: " + this.tel : "") + 
               (!Utils.isEmpty(this.fax) ? "\n Факс: " + this.fax : "") + 
               (!Utils.isEmpty(this.email) ? "\n EMail " + this.email : "") +
               (!Utils.isEmpty(this.kved) || !Utils.isEmpty(this.kvedName) ? "\n КВЕД: " + this.kved + " \"" + this.kvedName + "\"" : "") +
               (!Utils.isEmpty(this.opf) || !Utils.isEmpty(this.opfName) ? "\n ОПФ: " + this.opf + "\"" + this.opfName + " \"" : "") +
               (!Utils.isEmpty(this.kfv) || !Utils.isEmpty(this.kfvName) ? "\n Форма власності за КФВ: " + this.kfv + " \"" + this.kfvName + "\"" : "") +
               (!Utils.isEmpty(this.coogu) || !Utils.isEmpty(this.cooguName) ? "\n Орган державного управління(СПОДУ): " + this.coogu + " \"" + this.cooguName + "\"" : "") +
               (!Utils.isEmpty(this.okonx) || !Utils.isEmpty(this.okonxName) ? "\n Галузь народного господарства України: " + this.okonx + " \"" + this.okonxName + "\"" : "") +
               "\n Код ДПИ: " + this.c_sti + "[" + this.stiTin + "] \"" + this.stiName + "\"" +
               (!Utils.isEmpty(this.c_pfu) || !Utils.isEmpty(this.pfuFil) ? "\n ПФУ: " + this.c_pfu + " [" + this.pfuFil + "]" : "") +
               (!Utils.isEmpty(this.c_territ) || !Utils.isEmpty(this.territName) ? "\n КОАТУУ: " + this.c_territ + " \"" + this.territName + "\"" : "") +
               (!Utils.isEmpty(this.bankMfo) || !Utils.isEmpty(this.bankName) || !Utils.isEmpty(this.bankAccount) ? "\n Банковские реквизиты: " + this.bankMfo + " \"" + this.bankName + "\", #" + this.bankAccount : "");
    } 
}
