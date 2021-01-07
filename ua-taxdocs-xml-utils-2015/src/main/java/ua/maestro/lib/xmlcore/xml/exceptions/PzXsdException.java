/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.xml.exceptions;

import ua.maestro.lib.commons.Utils;

/**
 *
 * @author maestro
 */
public class PzXsdException extends Exception {
    private String errorMessage;

    public PzXsdException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public PzXsdException() {
        super();
    }

    public String getErrorMessage() {
        return Utils.isEmpty(errorMessage) ? "It's an exception during validating with XML" : errorMessage;
    }    
    
    @Override
    public String toString() {
        return getErrorMessage();
    }    
}
