package ua.maestro.lib.templates.mapping;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author maestro
 */
public class DMColumnFormatOptions extends DMColumnOpts {
    @SerializedName("defaultValue")
    private String defaultValue;
    
    @SerializedName("decimalPlaces")
    private Integer decimalPlaces;
    
    @SerializedName("decimalSeparator")
    private String decimalSeparator;
    
    @SerializedName("thousandsSeparator")
    private String thousandsSeparator;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    public String getThousandsSeparator() {
        return thousandsSeparator;
    }

    public void setThousandsSeparator(String thousandsSeparator) {
        this.thousandsSeparator = thousandsSeparator;
    }
    
    public boolean isEmpty() {
        return isEmptyOrZero(this.defaultValue) && 
               isEmptyOrZero(this.decimalPlaces) && 
               isEmptyOrZero(this.decimalSeparator) && 
               isEmptyOrZero(this.thousandsSeparator);
    }    
}
