package ua.maestro.lib.xmlcore.xsd;

import ua.maestro.lib.xmlcore.xml.exceptions.PzXsdException;
import ua.maestro.lib.xmlcore.xsd.data.XsdErrorRec;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 *
 * @author maestro
 */
public class XsdUtils {
    public final static int ERR_WARNING = 1;
    public final static int ERR_ERROR = 2;
    public final static int ERR_FATALERROR = 3;    
    
    public HashMap<String, List<XsdErrorRec>> validateXml(String cdoc, String xml, String xsdTmpl, String ctype, Locale dLocale)
                    throws PzXsdException {
        try {
            ByteArrayInputStream datastr = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            ByteArrayInputStream shstr = new ByteArrayInputStream(xsdTmpl.getBytes(StandardCharsets.UTF_8));
            Source schemaFile = new StreamSource(shstr);
            Source xmlFile = new StreamSource(datastr);

            CustomLSResourceResolver resolver = new CustomLSResourceResolver();
            resolver.setCommonTypes(ctype);
            XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(xmlFile);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schemaFactory.setResourceResolver(resolver);
            schemaFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);

            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            XsdErrorHandler err = new XsdErrorHandler(reader);
            validator.setProperty("http://apache.org/xml/properties/locale", dLocale);
            validator.setErrorHandler(err);
            XMLErrorReporter property = (XMLErrorReporter) validator.getProperty("http://apache.org/xml/properties/internal/error-reporter");
            MessageFormatter messageFormatter = property.getMessageFormatter("http://www.w3.org/TR/xml-schema-1");
            property.putMessageFormatter(XsdMessageFormatter.SCHEMA_DOMAIN, new XsdMessageFormatter());

            validator.validate(new StAXSource(reader));
            return err.errors;
        } catch (IOException | SAXException | XMLStreamException e) {
            throw new PzXsdException(e.getMessage());
        }
    }

    public HashMap<String, List<XsdErrorRec>> validateXml(String cdoc, String xml, String xsdTmpl, String ctype)
            throws PzXsdException, IOException {
        try {
            ByteArrayInputStream datastr = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            ByteArrayInputStream shstr = new ByteArrayInputStream(xsdTmpl.getBytes(StandardCharsets.UTF_8));
            Source schemaFile = new StreamSource(shstr);
            Source xmlFile = new StreamSource(datastr);

            CustomLSResourceResolver resolver = new CustomLSResourceResolver();
            resolver.setCommonTypes(ctype);
            XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(xmlFile);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schemaFactory.setResourceResolver(resolver);
            schemaFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);

            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            XsdErrorHandler err = new XsdErrorHandler(reader);
            validator.setErrorHandler(err);

            validator.validate(new StAXSource(reader));
            return err.errors;
        } catch (SAXException | XMLStreamException e) {
            throw new PzXsdException(e.getMessage());
        }
    }
    
    public HashMap<String, List<XsdErrorRec>> validateXml(String xml, Schema schema, Locale dLocale) throws XMLStreamException, SAXException, IOException {
        ByteArrayInputStream datastr = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Source xmlFile = new StreamSource(datastr);
        XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(xmlFile);
        Validator validator = schema.newValidator();
        XsdErrorHandler err = new XsdErrorHandler(reader);
        validator.setProperty("http://apache.org/xml/properties/locale", dLocale);
        validator.setErrorHandler(err);
        
        XMLErrorReporter property = (XMLErrorReporter) validator.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        MessageFormatter messageFormatter = property.getMessageFormatter("http://www.w3.org/TR/xml-schema-1");
        property.putMessageFormatter(XsdMessageFormatter.SCHEMA_DOMAIN, new XsdMessageFormatter());

        validator.validate(new StAXSource(reader));
        return err.getErrors();
    }

    public HashMap<String, List<XsdErrorRec>> validateXml(String xml, Schema schema) throws XMLStreamException, SAXException, IOException {
        ByteArrayInputStream datastr = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Source xmlFile = new StreamSource(datastr);
        XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(xmlFile);
        Validator validator = schema.newValidator();
        XsdErrorHandler err = new XsdErrorHandler(reader);
        validator.setErrorHandler(err);

        validator.validate(new StAXSource(reader));
        return err.getErrors();
    }
}
