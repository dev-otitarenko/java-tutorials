/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.xsd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 *
 * @author maestro
 */
public class CustomLSResourceResolver implements LSResourceResolver {
    private String ctype;
        
    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId,  String baseURI) {
        if (systemId.contains("common_types") || systemId.contains("types") || systemId.contains("bank_type")) {
            InputStream resourceAsStream = null;
            try {
                resourceAsStream = new ByteArrayInputStream(ctype.getBytes("UTF-8"));
                return new CustomLSInput(publicId, systemId, resourceAsStream);
            } catch (UnsupportedEncodingException ex) {
            } finally {
                try { resourceAsStream.close(); } catch (IOException ex) {  }
            }
        }
        return null;
    }

    public void setCommonTypes(String v){
        this.ctype = v;
    }

    public String getCommontTypes() {
        return this.ctype;
    } 
}
