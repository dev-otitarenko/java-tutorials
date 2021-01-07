package ua.maestro.lib.templates.mapping;

import ua.maestro.lib.commons.Utils;

/**
 *
 * @author maestro
 */
public class DMColumnOpts {
    protected boolean isEmptyOrZero(Boolean val) {
        return (val == null ? true : val == false);
    }
    
    protected boolean isEmptyOrZero(Number val) {
        return (val == null ? true : val.floatValue() == 0);
    }

    protected boolean isEmptyOrZero(Integer val) {
        return (val == null ? true : val.intValue() == 0);
    }    
    
    protected boolean isEmptyOrZero(String val) {
        return Utils.isEmpty(val);
    } 
    
    protected boolean isEmptyOrDefaulValue(String val, String defVal) {
        return Utils.isEmpty(val) ? true : val.equalsIgnoreCase(defVal);
    }
}
