/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.xsd;

import com.sun.org.apache.xerces.internal.util.MessageFormatter;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 *
 * @author maestro
 */
public class XsdMessageFormatter implements MessageFormatter {
    public static final String SCHEMA_DOMAIN = "http://www.w3.org/TR/xml-schema-1";
        
       /**
        * The domain of messages concerning the XML 1.0 specification.
        */
    public static final String XML_DOMAIN = "http://www.w3.org/TR/1998/REC-xml-19980210";
    public static final String XMLNS_DOMAIN = "http://www.w3.org/TR/1999/REC-xml-names-19990114";
   
       // private objects to cache the locale and resource bundle
    private Locale fLocale = null;
    private ResourceBundle fResourceBundle = null;        
        //...
    public String formatMessage(Locale locale, String key, Object[] arguments) throws MissingResourceException {
        if (fResourceBundle == null || locale != fLocale) {
           if (locale != null) {
               fResourceBundle = PropertyResourceBundle.getBundle("XMLSchemaMessages", locale);
               // memorize the most-recent locale
               fLocale = locale;
           }
           if (fResourceBundle == null)
               fResourceBundle = PropertyResourceBundle.getBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages");
       }
   
// format message
         String msg;
         try {
             msg = fResourceBundle.getString(key);
             if (arguments != null) {
                 try {
                     msg = java.text.MessageFormat.format(msg, arguments);
                 }
                 catch (Exception e) {
                     msg = fResourceBundle.getString("FormatFailed");
                     msg += " " + fResourceBundle.getString(key);
                 }
             }
         }
 
         // error
        catch (MissingResourceException e) {
            msg = fResourceBundle.getString("BadMessageKey");
            throw new MissingResourceException(key, msg, key);
        }

        // no message
        if (msg == null) {
            msg = key;
            if (arguments.length > 0) {
                StringBuffer str = new StringBuffer(msg);
                str.append('?');
                for (int i = 0; i < arguments.length; i++) {
                    if (i > 0) {
                        str.append('&');
                    }
                    str.append(String.valueOf(arguments[i]));
                }
            }
        }

        return msg;           
    }    
}
