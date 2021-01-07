package ua.maestro.lib.xmlcore.provider.exceptions;

import ua.maestro.lib.commons.Utils;

/**
 *
 * @author maestro, 2015
 */
public class ProviderException extends Exception {
    private String errorMessage;

    public ProviderException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public ProviderException() {
        super();
    }

    public String getErrorMessage() {
        return Utils.isEmpty(errorMessage) ? "It's an exception during processing with the provider" : errorMessage;
    }
    
    @Override
    public String toString() {
        return getErrorMessage();
    }    
}
