package ua.maestro.lib.templates.mapping;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author maestro
 */
public class DMColumnEditOptions extends DMColumnOpts {
    private String value;
	
//	@SerializedName("custom_value")
//	private String customValue;
//
//	@SerializedName("custom_element")
//	private String customElement;
    
    @SerializedName("maxlength")
    private Integer maxLength;

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

//	public String getCustomValue() {
//		return customValue;
//	}
//
//	public void setCustomValue(String customValue) {
//		this.customValue = customValue;
//	}
//
//	public String getCustomElement() {
//		return customElement;
//	}
//
//	public void setCustomElement(String customElement) {
//		this.customElement = customElement;
//	}

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
    
    public boolean isEmpty() {
        return isEmptyOrZero(this.value) && 
               //isEmptyOrZero(this.customElement) && 
               //isEmptyOrZero(this.customValue) && 
               isEmptyOrZero(this.maxLength);
    } 
}
