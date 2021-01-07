package ua.maestro.lib.templates.mapping;

import com.google.gson.annotations.SerializedName;
import ua.maestro.lib.xmlcore.domains.PZDocInput;
import ua.maestro.lib.xmlcore.domains.PZDocInput.*;

/**
 *
 * @author maestro
 */
public class DMColumnUserOptions extends DMColumnOpts {
    @SerializedName("edittype")
    private String editType;
    @SerializedName("datatype")
    private String dataType;
    @SerializedName("baseType")
    private String baseType;
    @SerializedName("type")
    private String type;
    @SerializedName("dctName")
    private String dctName;
    @SerializedName("dctField")
    private String dctField;
    @SerializedName("dctGroup")
    private String dctGroup;
    @SerializedName("dctFilter")
    private String dctFilter;
    @SerializedName("xmlDecimalPlaces")
    private Integer decimalPlaces;
    @SerializedName("xmlDecimalSeparator")
    private String decimalSeparator;
    @SerializedName("dimrowcol")
    private Integer dimRowCol;
    @SerializedName("formatterorder")
    private String formatterOrder;
    
    public DMColumnUserOptions() {
        
    }
    
    public DMColumnUserOptions(PZDocInput.ParamEditType editTp, String baseTp, String tp, ParamDataType dataTp) {
        this.setDefaultValues(editTp, baseTp, tp, dataTp);
    }
    
    public void setDefaultValues(PZDocInput.ParamEditType editTp, String baseTp, String tp, ParamDataType dataTp) {
        this.editType = editTp != null ? editTp.value() : ParamEditType.TEXT.value();
        this.baseType = baseTp;
        this.type = tp;
        this.dataType = dataTp != null ? dataTp.value() : ParamDataType.STRING.value();
    }

    public String getEditType() {
        return editType;
    }

    public void setEditType(String editType) {
        this.editType = editType;
    }

    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDctName() {
        return dctName;
    }

    public void setDctName(String dctName) {
        this.dctName = dctName;
    }

    public String getDctField() {
        return dctField;
    }

    public void setDctField(String dctField) {
        this.dctField = dctField;
    }

    public String getDctGroup() {
        return dctGroup;
    }

    public void setDctGroup(String dctGroup) {
        this.dctGroup = dctGroup;
    }

    public String getDctFilter() {
        return dctFilter;
    }

    public void setDctFilter(String dctFilter) {
        this.dctFilter = dctFilter;
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

    public Integer getDimRowCol() {
        return dimRowCol;
    }

    public void setDimRowCol(Integer dimRowCol) {
        this.dimRowCol = dimRowCol;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFormatterOrder() {
        return formatterOrder;
    }

    public void setFormatterOrder(String formatterOrder) {
        this.formatterOrder = formatterOrder;
    }
    
    public boolean isEmpty() {
        return isEmptyOrZero(this.baseType) &&
               isEmptyOrZero(this.dataType) &&
               isEmptyOrZero(this.dctName) &&
               isEmptyOrZero(this.dctField) &&
               isEmptyOrZero(this.dctGroup) &&
               isEmptyOrZero(this.decimalPlaces) &&
               isEmptyOrZero(this.decimalSeparator) &&
               isEmptyOrZero(this.dimRowCol) &&
               isEmptyOrZero(this.editType) &&
               isEmptyOrZero(this.formatterOrder); 
    }
}
