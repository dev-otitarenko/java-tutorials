package ua.maestro.lib.commons;

import ua.maestro.lib.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

public class Utils {
    public static boolean isEmpty(String str) {
        return StringUtil.isEmpty(str);
    }

    public static String rightPad0(String str, int size) {
        return StringUtils.rightPad(str, size, "0");
    }
    
    public static String rightPad(String str, int size, char chr) {
        return StringUtils.rightPad(str, size, chr);
    }

    public static String leftPad0(int val, int size) {
        return StringUtils.leftPad(String.valueOf(val), size, "0");
    }
    
	public static String leftPad0(String str, int size){
		return StringUtils.leftPad(str, size, "0");
    }
    
    public static String leftPad(int val, int size, char chr) {
        return StringUtils.leftPad(String.valueOf(val), size, chr);
    }
    
    public static String leftPad(String str, int size, char chr) {
        return StringUtils.leftPad(str, size, chr);
    }
}
