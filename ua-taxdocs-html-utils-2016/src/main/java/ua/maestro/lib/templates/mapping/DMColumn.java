package ua.maestro.lib.templates.mapping;

import com.google.gson.annotations.SerializedName;
import ua.maestro.lib.templates.exceptions.ProcessTemplateException;
import ua.maestro.lib.commons.Utils;

/**
 *
 * @author maestro
 */
public class DMColumn {
    private String index;
    private String label;
    private String name;
    private Boolean editable;
    private String formatter;
    private String datefmt;
    @SerializedName("edittype")
    private String editType;
    private int tabn;
    private Boolean rownum;
    
    @SerializedName("editoptions")
    private DMColumnEditOptions editOptions = null;
    @SerializedName("formatoptions")
    private DMColumnFormatOptions formatOptions = null;
    @SerializedName("editrules")
    private DMColumnEditRules editRules = null;
    @SerializedName("useropts")
    private DMColumnUserOptions userOptions = null;
    @SerializedName("__is_editors")
    private boolean hasInputs = false;
    
    private String parentNm;

    public DMColumn(String nm, boolean editable, boolean rn, int tabn, boolean hasInputs) throws ProcessTemplateException {
        if (rn) {
            if (tabn == 0) throw new ProcessTemplateException("Invalid parameters [tabn, rownum]");
            this.index = nm;
            this.name = nm;
            this.editable = false;
            this.rownum = true;
        } else {
            this.index = nm;
            this.name = nm;
            this.editable = editable;
            this.rownum = false;
        }
        this.tabn = tabn;
        this.hasInputs = hasInputs;
    }

    public String getIndex() {
        return index;
    }
    
    public String getName() {
        return name;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public int getTabn() {
        return tabn;
    }

    public Boolean getRownum() {
        return rownum;
    }

    public void setRownum(Boolean rownum) {
        this.rownum = rownum;
    }
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(ColumnFormatter val) {
        this.formatter = val.value();
    }

    public void setFormatter(String val) {
        this.formatter = val;
    }
    
    public String getEditType() {
        return editType;
    }

    public void setEditType(ColumnEditType val) {
        this.editType =  val == ColumnEditType.TEXT ? null : val.value();
    }

    public DMColumnEditOptions getEditOptions() {
        return editOptions;
    }

    public void setEditOptions(DMColumnEditOptions v) {
        this.editOptions = (!v.isEmpty()) ? v : null;
    }

    public DMColumnEditRules getEditRules() {
        return editRules;
    }

    public void setEditRules(DMColumnEditRules v) {
        this.editRules = (!v.isEmpty()) ? v : null;
    }
    
    public DMColumnFormatOptions getFormatOptions() {
        return formatOptions;
    }

    public void setFormatOptions(DMColumnFormatOptions v) {
        this.formatOptions = (!v.isEmpty()) ? v : null;
    }

    public String getDatefmt() {
        return datefmt;
    }

    public void setDatefmt(String datefmt) {
        this.datefmt = datefmt;
    }

    public DMColumnUserOptions getUserOptions() {
        return userOptions;
    }

    public void setUserOptions(DMColumnUserOptions v) {
        this.userOptions = (!v.isEmpty()) ? v : null;
    }

    public boolean isHasInputs() {
        return hasInputs;
    }

    public void setHasInputs(boolean hasInputs) {
        this.hasInputs = hasInputs;
    }

    public String getParentNm() {
        return parentNm;
    }

    public void setParentNm(String parentNm) {
        this.parentNm = parentNm;
    }
    
    @Override
    public String toString() {
        return "[" + this.tabn + ", " + this.editable + ", " + this.rownum + "] " + 
                this.index + 
                ", " + this.name + 
                (!Utils.isEmpty(this.label) ? ", " + this.label : "") +
                (!Utils.isEmpty(this.parentNm) ? "[" + this.parentNm + "]" : "");
    }
    
    public static enum ColumnEditType {
        TEXT("text"),
        DATE("date"),
        DICT("dict"),
        SELECT("select"),
        CHECKBOX("checkbox"),
        FILE("file"),
        CUSTOM("custom");

        private String type;

        ColumnEditType(String v) {
            this.type = v;
        }

        public String value() {
            return this.type;
        }
        
        public static ColumnEditType getObjectByValue(String val){
            for(ColumnEditType e : ColumnEditType.values()){
                if(e.value().equalsIgnoreCase(val)) return e;
            }
            return null;
        }        
    }
    
    public static enum ColumnFormatter {
        INTEGER("integer"),
        NUMBER("number"),
        SELECT("select"),
        GRP_CHECKBOX("chkDMGroup"),
        GRP_RADIOBOX("rdDMGroup");

        private String type;

        ColumnFormatter(String v) {
            this.type = v;
        }

        public String value() {
            return this.type;
        }
        
        public static ColumnFormatter getObjectByValue(String val){
            for(ColumnFormatter e : ColumnFormatter.values()){
                if(e.value().equalsIgnoreCase(val)) return e;
            }
            return null;
        }         
    }    
}
