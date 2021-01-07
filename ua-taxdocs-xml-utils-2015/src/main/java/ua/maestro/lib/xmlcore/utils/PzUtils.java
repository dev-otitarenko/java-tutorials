package ua.maestro.lib.xmlcore.utils;

import ua.maestro.lib.xmlcore.domains.PZDocInput;
import ua.maestro.lib.commons.Utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author maestro, 2015
 */
public class PzUtils {
    public static String getFormatDate (Date date, String format) {
        if (Utils.isEmpty(format)) format = "dd.MM.yyyy HH:mm:ss";
        DateFormat formatter = new SimpleDateFormat(format);
        String dd = formatter.format(date);
        return dd;
    }
    
    public static String getCurrentDate (String format) {
        return getFormatDate(new Date(), format);
    }
    
    public static String getCurDaySt (){
        return getFormatDate(new Date(), "dd.MM.yyyy");
    }
    
    public static String toXmlDate() {
        return toXmlDate(getCurDaySt());
    }
    
    public static String toXmlDate(String date){
	   return date.replace(".", "");
    }
    
    public static Date convXmlDate(String val) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        Date date = formatter.parse(val);
        return date;
    }
    
    public static int getCurMm (){
       java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
       calendar.setTime(new java.util.Date());
       return calendar.get(java.util.Calendar.MONTH) ;
    }
    
    public static int getCurYear (){
       java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
       calendar.setTime(new java.util.Date());
       return calendar.get(java.util.Calendar.YEAR);
    }
    
    public static int getCurDay (){
       java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
       calendar.setTime(new java.util.Date());
       return calendar.get(java.util.Calendar.DAY_OF_MONTH);
    }

    public static final String getCDoc(String cdoc, String cdocSub, String cdocVer) {
        if (cdoc.equalsIgnoreCase("E04") || cdoc.equalsIgnoreCase("E05") || cdoc.equalsIgnoreCase("E06") || cdoc.equalsIgnoreCase("E07")) {
            return cdoc + cdocSub + cdocVer;
        }
        return cdoc + cdocSub + String.format("%2s", cdocVer).replace(" ", "0");
    }
    
    public static String getCurrDateAsXmlStr() {
        DateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        return formatter.format(new java.util.Date());
    }
    
    public static String getCurrTimeAsXmlStr() {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new java.util.Date());
    }
    
    public static String getPeriodMonthNm(int perMonth) {
        String ret = "";
        switch(perMonth) { 
            case 1 : ret = "Січень"; break;
            case 2 : ret = "Лютий"; break;
            case 3 : ret = "Березень"; break;
            case 4 : ret = "Квітень"; break;
            case 5 : ret = "Травень"; break;
            case 6 : ret = "Червень"; break;
            case 7 : ret = "Липень"; break;
            case 8 : ret = "Серпень"; break;
            case 9 : ret = "Вересень"; break;
            case 10 : ret = "Жовтень"; break;
            case 11 : ret = "Листопад"; break;                                    
            case 12 : ret = "Грудень"; break;
        }        
        return ret;        
    }
    
    public static String getPeriodMonthNm(int perMonth, int perType) {
        String ret = "";
        switch (perType) {
            case 1: // Месяные отчеты
                switch(perMonth) {
                    case 1 : ret = "Січень"; break;
                    case 2 : ret = "Лютий"; break;
                    case 3 : ret = "Березень"; break;
                    case 4 : ret = "Квітень"; break;
                    case 5 : ret = "Травень"; break;
                    case 6 : ret = "Червень"; break;
                    case 7 : ret = "Липень"; break;
                    case 8 : ret = "Серпень"; break;
                    case 9 : ret = "Вересень"; break;
                    case 10 : ret = "Жовтень"; break;
                    case 11 : ret = "Листопад"; break;
                    case 12 : ret = "Грудень"; break;
                }
                break;
            case 2: // Квартальные отчеты
                switch(perMonth) {
                    case 3 : ret = "I квартал"; break;
                    case 6 : ret = "II квартал"; break;
                    case 9 : ret = "III квартал"; break;
                    case 12 : ret = "IV квартал"; break;
                }
                break;
            case 3: // Полугодие
                switch(perMonth) {
                    case 6 : ret = "I півріччя"; break;
                    case 12 : ret = "II піврічча"; break;
                }
                break;
            case 4: // 9th months
                if(perMonth == 9) { ret = "9 місяців"; }
                break;
            case 5: // Yearly
                if (perMonth == 12) { ret = "Рік"; }
                break;
            default:
                break;
        }
        return ret;
    }
    
    public static String getPeriodTypeNm(int perType) {
        String ret = "";
        switch(perType) {
            case 1: ret = "Місячна"; break;
            case 2: ret = "Квартальна"; break;
            case 3: ret = "Півріччя"; break;    
            case 4: ret = "9 місяців"; break;
            case 5: ret = "Річна"; break;    
        }
        return ret;
    }
    
    public static String getSignNm(int v) {
        String ret = "Не підписано";
        switch(v) {
            case 1: ret = "Підписано гл.бухгалтером"; break;
            case 2: ret = "Підписано гл.директором"; break;
            case 3: ret = "Підписано печаткою"; break;
            case 4: ret = "Зашифровано та відправлено"; break;
        }
        return ret;        
    }
    
    public static String getFlagsNm(int v) {
        String ret = "";
        boolean haveErrors = false;
        if ((v & 2) != 0) { ret += "Помилки камеральної перевірки"; haveErrors = true; }
        if ((v & 4) != 0) { ret += (ret.length() !=0 ? "<br />" : "") + "XSD помилки"; haveErrors = true; }
        if ((v & 8) != 0) { ret += (ret.length() !=0 ? "<br />" : "") + "Критичні помилки"; haveErrors = true; }
        
        return (!haveErrors) ? "Без помилок" : "<font color='red'>" + ret + "</font>";        
    }
    
    public static String getStanNm(int v) {
        String ret = "";
        switch(v) {
            case 1: ret = "Звітна"; break;
            case 2: ret = "Нова звітна"; break;
            case 3: ret = "Уточнююча"; break;    
        }
        return ret;
    }
    
    public static PZDocInput getColumnRec(final List<PZDocInput> docVars, String fieldNm) {
        PZDocInput inp = null;
        for (PZDocInput var : docVars) {
            if (var.getField().equalsIgnoreCase(fieldNm)) {
                inp = var;
                break;
            }
        }
        return inp;
    }

    public static BigDecimal getBigDecimal(Object value ) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
                ret = new BigDecimal( (String) value );
            } else if( value instanceof BigInteger) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = new BigDecimal( ((Number)value).doubleValue() );
            } else {
                throw new ClassCastException("Not possible to convert ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        return ret;
    }

    public static BigInteger getBigInteger(Object value) {
        BigInteger ret = null;
        if ( value != null ) {
            if ( value instanceof BigInteger ) {
                ret = (BigInteger) value;
            } else if ( value instanceof String ) {
                ret = new BigInteger( (String) value );
            } else if ( value instanceof BigDecimal ) {
                ret = ((BigDecimal) value).toBigInteger();
            } else if ( value instanceof Number ) {
                ret = BigInteger.valueOf( ((Number) value).longValue() );
            } else {
                throw new ClassCastException( "Not possible to convert [" + value + "] from class " + value.getClass() + " into a BigInteger." );
            }
        }
        return ret;
    }    
}
