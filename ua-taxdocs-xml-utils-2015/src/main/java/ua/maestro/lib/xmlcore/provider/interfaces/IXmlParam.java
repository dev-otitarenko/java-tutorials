/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.provider.interfaces;

import ua.maestro.lib.xmlcore.domains.PZDocumentData;

/**
 *
 * @author maestro
 */
public interface IXmlParam {
    PZDocumentData getDoc();
    
    int getPerMonth();
    
    int getPerYear();
    
    int getPerType();
    
    String getDefaultValue(String name);
}
