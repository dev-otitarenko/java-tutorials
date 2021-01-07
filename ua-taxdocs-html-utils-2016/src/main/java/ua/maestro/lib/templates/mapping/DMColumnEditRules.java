package ua.maestro.lib.templates.mapping;


/**
 *
 * @author maestro
 */
public class DMColumnEditRules extends DMColumnOpts {
    private Boolean required = null;
    private Boolean number = null;
    private Boolean integer = null;
    private Boolean date = null;
    private Number minValue = null;
    private Number maxValue = null;
    
    public DMColumnEditRules() {
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean v) {
        this.required = v;
    }

    public Boolean getNumber() {
        return number;
    }

    public void setNumber(Boolean v) {
        boolean val = v != null ? v : false;
        if (val) {
            this.date = null;
            this.integer = null;
        }
        this.number = v;
    }

    public Boolean getInteger() {
        return integer;
    }

    public void setInteger(Boolean v) {
        boolean val = v != null ? v : false;
        if (val) {
            this.date = null;
            this.number = null;
        }        
        this.integer = v;
    }

    public Boolean getDate() {
        return date;
    }

    public void setDate(Boolean v) {
        boolean val = v != null ? v : false;
        if (val) {
            this.number = null;
            this.integer = null;
        }        
        this.date = v;
    }

    public Number getMinValue() {
        return minValue;
    }

    public void setMinValue(Number minValue) {
        this.minValue = minValue;
    }

    public Number getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Number maxValue) {
        this.maxValue = maxValue;
    }
    
    public boolean isEmpty() {
        return isEmptyOrZero(this.date) && 
               isEmptyOrZero(this.integer) && 
               isEmptyOrZero(this.number) && 
               isEmptyOrZero(this.required) && 
               isEmptyOrZero(this.minValue) &&
               isEmptyOrZero(this.maxValue);
    }    
}
