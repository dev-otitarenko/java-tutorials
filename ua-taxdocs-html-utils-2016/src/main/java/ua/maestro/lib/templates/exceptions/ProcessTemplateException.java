package ua.maestro.lib.templates.exceptions;

import ua.maestro.lib.commons.Utils;

/**
 *
 * @author maestro
 */
public class ProcessTemplateException extends Exception {
    private String errorMessage;

    public ProcessTemplateException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public ProcessTemplateException() {
        super();
    }

    public String getErrorMessage() {
        return Utils.isEmpty(errorMessage) ? "It's an exception during processing the document's template" : errorMessage;
    }

    @Override
    public String toString() {
        return getErrorMessage();
    }
}
