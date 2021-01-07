/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.domains;

import ua.maestro.lib.commons.Utils;
import java.io.Serializable;

/**
 *
 * @author maestro
 */
public class PZDocInput implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String field;
    protected String label;
    protected String type;
    protected ParamDataType dataType;    
    protected Integer cntGroup;
    protected Boolean rownum;
    protected Boolean required;
    protected Boolean editable;
    protected ParamAlign align;
    protected String base;
    protected Integer size;
    protected Integer maxLength;
    protected Integer decimalPlaces;
    protected String decimalSeparator;
    protected ParamEditType editType;
    protected String htmlList;
    protected String initVal;
    protected Integer dimrowcol;
    protected Integer xmlDecimalPlaces;
    protected String xmlDecimalSeparator;
    protected Long minValue;
    protected Long maxValue;
    protected Integer minLen;
    protected Integer maxLen; 
    protected Boolean enMerge = true;
    
    public PZDocInput() {
    }
    
    public PZDocInput(String field,
                      String label,
                      String type,
                      ParamDataType dataType,
                      Integer cntGroup,
                      Boolean rownum,
                      Boolean required,
                      Boolean editable,
                      ParamAlign align,
                      String base,
                      Integer size,
                      Integer maxLength,
                      Integer decimalPlaces,
                      String decimalSeparator,
                      ParamEditType editType,
                      String htmlList,
                      String initVal,
                      Integer dimrowcol,
                      Integer xmlDecimalPlaces,
                      String xmlDecimalSeparator,
                      Long minValue,
                      Long maxValue,
                      Integer minLen,
                      Integer maxLen,
                      Boolean enMerge) {
        this.field = field;
        this.label = label;
        this.type = type;
        this.dataType = dataType;
        this.cntGroup = cntGroup;
        this.rownum = rownum;
        this.required = required;
        this.editable = editable;
        this.align = align;
        this.base= base;
        this.size = size;
        this.maxLength = maxLength;
        this.decimalPlaces = decimalPlaces;
        this.decimalSeparator = decimalSeparator;
        this.editType = editType;
        this.htmlList = htmlList;
        this.initVal = initVal;
        this.dimrowcol = dimrowcol;
        this.xmlDecimalPlaces = xmlDecimalPlaces;
        this.xmlDecimalSeparator = xmlDecimalSeparator;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minLen = minLen;
        this.maxLen = maxLen;
        this.enMerge = enMerge;
    }    

    public String getField() {
        return field;
    }

    public void setField(String v) {
        this.field = v;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ParamDataType getDataType() {
        return dataType;
    }

    public void setDataType(ParamDataType dataType) {
        this.dataType = dataType;
    }

    public Integer getCntGroup() {
        return cntGroup;
    }

    public void setCntGroup(Integer cntGroup) {
        this.cntGroup = cntGroup;
    }

    public Boolean getRownum() {
        return rownum;
    }

    public void setRownum(Boolean rownum) {
        this.rownum = rownum;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public ParamAlign getAlign() {
        return align;
    }

    public void setAlign(ParamAlign align) {
        this.align = align;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getMaxlength() {
        return maxLength;
    }

    public void setMaxlength(Integer maxlength) {
        this.maxLength = maxlength;
    }

    public ParamEditType getHtmlInputType() {
        return editType;
    }

    public void setHtmlInputType(ParamEditType htmlInputType) {
        this.editType = htmlInputType == null ? ParamEditType.TEXT : htmlInputType;
    }

    public String getHtmlInputList() {
        return htmlList;
    }

    public void setHtmlInputList(String htmlInputList) {
        this.htmlList = htmlInputList;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    public String getInitVal() {
        return initVal;
    }

    public void setInitVal(String initVal) {
        this.initVal = initVal;
    }

    public Integer getDimrowcol() {
        return dimrowcol;
    }

    public void setDimrowcol(Integer dimrowcol) {
        this.dimrowcol = dimrowcol;
    }
    
    public Integer getXmlDecimalPlaces() {
        return xmlDecimalPlaces;
    }

    public void setXmlDecimalPlaces(Integer decimalPlaces) {
        this.xmlDecimalPlaces = decimalPlaces;
    }

    public String getXmlDecimalSeparator() {
        return xmlDecimalSeparator;
    }

    public void setXmlDecimalSeparator(String decimalSeparator) {
        this.xmlDecimalSeparator = decimalSeparator;
    }

    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    public Integer getMinLen() {
        return minLen;
    }

    public void setMinLen(Integer minLen) {
        this.minLen = minLen;
    }

    public Integer getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(Integer maxLen) {
        this.maxLen = maxLen;
    }
    
    public Boolean isMerge() {
        return enMerge;
    }

    public void setEnMerge(Boolean enMerge) {
        this.enMerge = enMerge;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.field + " [" + this.dataType + ", " + this.type + "" + (!Utils.isEmpty(this.label) ? ", \"" +this.label + "\"" : "" )+ "]");
        return sb.toString();
    } 
    
    public static enum ParamAlign {
        CENTER("center"),
        LEFT("left"),
        RIGHT("right");

        private String type;

        ParamAlign(String v) {
            this.type = v;
        }

        public String value() {
            return this.type;
        }
        
        public static ParamAlign getObjectByValue(String val){
            for(ParamAlign e : ParamAlign.values()){
                if(e.value().equalsIgnoreCase(val)) return e;
            }
            return null;
        }
    }
    
    public static enum ParamEditType {
        TEXT("text"),
        DATE("date"),
        DICT("dict"),
        SELECT("select"),
        FILE("file"),
        CHECKBOX("checkbox"),
        RADIOBOX("radiobox");

        private String type;

        ParamEditType(String v) {
            this.type = v;
        }

        public String value() {
            return this.type;
        }
        
        public static ParamEditType getObjectByValue(String val){
            for(ParamEditType e : ParamEditType.values()){
                if(e.value().equalsIgnoreCase(val)) return e;
            }
            return null;
        }
    }

    public static enum ParamDataType {
        STRING("STRING"),
        INTEGER("INTEGER"),
        NUMBER("NUMBER"),
        DATE ("DATE");

        private String type;

        ParamDataType(String v) {
            this.type = v;
        }

        public String value() {
            return this.type;
        }
        
        public static ParamDataType getObjectByValue(String val){
            for(ParamDataType e : ParamDataType.values()){
                if(e.value().equalsIgnoreCase(val)) return e;
            }
            return null;
        }
    }
}
