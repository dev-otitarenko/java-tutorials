/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.xsd;

import ua.maestro.lib.xmlcore.xsd.data.XsdErrorRec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 *
 * @author maestro
 */
public class XsdErrorHandler implements ErrorHandler{
    private XMLStreamReader reader;
    protected HashMap<String, List<XsdErrorRec>> errors = new HashMap<>();

    public XsdErrorHandler(XMLStreamReader reader) {
        this.reader = reader;
    }        

    @Override
    public void error(SAXParseException ex) {
        addError(XsdUtils.ERR_ERROR, ex);
    }

    @Override
    public void fatalError(SAXParseException ex) {
        addError(XsdUtils.ERR_FATALERROR, ex);
    }

    @Override
    public void warning(SAXParseException ex) {
        addError(XsdUtils.ERR_WARNING, ex);
    }

    private void addError(int errType, SAXParseException e) {
        String fld = (reader.isStartElement() || reader.isEndElement()) ? reader.getLocalName() : "?";
        
        List<XsdErrorRec> _list = errors.get(fld);
        if (_list == null) {
            _list = new ArrayList<>();
        }
        _list.add(new XsdErrorRec(errType, e));

        errors.put(fld, _list);
    }
    
    public boolean hasValidationError() {
        return !this.errors.isEmpty();
    }
    
    public HashMap<String, List<XsdErrorRec>> getErrors() {
        return this.errors;
    }
}
