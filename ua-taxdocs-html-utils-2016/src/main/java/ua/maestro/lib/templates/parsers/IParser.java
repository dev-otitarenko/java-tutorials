package ua.maestro.lib.templates.parsers;

import ua.maestro.lib.templates.mapping.DMColumn;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author maestro
 */
public interface IParser {
    List<DMColumn> getColumnModel();
    
    Map<String, List<String>> getParentGroups();
    
    Map<Integer, List<String>> getChoiceGroups();
    
    Map<Integer, Map<Integer, List<String>>> getXSDChoiceGroups(byte[] xsdData)  throws ParserConfigurationException, SAXException, IOException ;

    int getTabCnt();
    
    String getTmplContent();
}
