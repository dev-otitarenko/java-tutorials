package ua.maestro.lib.templates.parsers;

import ua.maestro.lib.commons.Utils;
import ua.maestro.lib.commons.utils.RegexpUtl;
import ua.maestro.lib.xmlcore.domains.PZDocInput;
import ua.maestro.lib.xmlcore.domains.PZDocInput.*;
import ua.maestro.lib.templates.exceptions.ProcessTemplateException;
import ua.maestro.lib.templates.mapping.DMColumn;
import ua.maestro.lib.templates.mapping.DMColumn.*;
import ua.maestro.lib.templates.mapping.DMColumnEditOptions;
import ua.maestro.lib.templates.mapping.DMColumnEditRules;
import ua.maestro.lib.templates.mapping.DMColumnFormatOptions;
import ua.maestro.lib.templates.mapping.DMColumnUserOptions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author maestro
 */
public abstract class ParserFmt implements IParser {
        // The number of dynamic tables in the document
    protected int tabCnt = 0;
        // The Column Model of the document
    private List<DMColumn> columns = new ArrayList<>();
        // ...
    private Map<String, List<String>> parentGroups = new HashMap<>();
        // ...
    private Map<Integer, List<String>> choiceGroups = new HashMap<>();
        // ...
    private List<PZDocInput> excludeInputs = new ArrayList<>();
        // tmplContent
    private String tmplContent;

    public String process(String htmlContent, final List<PZDocInput> fields, boolean isAdjustHtml) throws ProcessTemplateException {
        htmlContent = htmlContent.replace("&nbsp;" ," ");
        Document document = Jsoup.parse(htmlContent);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.outputSettings().charset("UTF-8");
            // The parsing of html
        parseFmt(document);
            // The preparing of document's model
        prepareDocumentModel(document, fields, isAdjustHtml);
            // The checking of the list of fields
        prepareInputs(document, fields, isAdjustHtml);
        
            // Get Count of Group
        int cntGroup = 0;
        for (PZDocInput inp : fields) {
            if (inp.getCntGroup() > cntGroup) cntGroup = inp.getCntGroup();
        }
            // 
        for (int ngroup = 1; ngroup <= cntGroup; ngroup++) {
            List<String> _groups = new ArrayList<>();
            for (PZDocInput inp : fields) {
                if (inp.getCntGroup() == ngroup)
                        _groups.add(inp.getField());
            }
            if (_groups.size() > 1)
                    this.choiceGroups.put(ngroup, _groups);
        }
        
        this.tmplContent = document.toString();
        return this.tmplContent;        
    }
    
    protected abstract void parseFmt(Document document);
    
    protected void checkField (DMColumn field) { }
    
    private void prepareInputs(Document document, List<PZDocInput> fields, boolean isAdjustHtml) throws ProcessTemplateException {
        for (PZDocInput inp : fields) {
            boolean isFnd = false;
            for (PZDocInput excludeInp : this.excludeInputs) {
                if (excludeInp.getField().equalsIgnoreCase(inp.getField())) {
                    isFnd = true;
                    break;
                }
            }
            if (isFnd == false) {
                String _fldNm = inp.getField();
                boolean _rownum  = inp.getRownum() != null ? inp.getRownum() : false,
                        _editable = inp.getEditable() != null ? inp.getEditable() : false;
                int pos = getColIndex(_fldNm);
                DMColumn field;
                if (pos != -1) {
                    field = columns.get(pos);
                    field.setEditable(_editable);
                    if (field.getRownum() != _rownum && _rownum)  field.setRownum(_rownum);
                } else
                    field = new DMColumn(_fldNm,_editable, _rownum, 0, false);
        
                if (pos != -1) {
                    columns.set(pos, field);
                    
                    Elements inputs = document.select("div.fc[p=" + getTabName(field) + "]");
                    for(Element el : inputs) {
                        prepareHtmlInput(el, field, inp, isAdjustHtml);
                    }
                } else {
                    columns.add(field);
                }
            }
        }        
    }
    
    private void prepareDocumentModel (Document document, List<PZDocInput> fields, boolean isAdjustHtml) throws ProcessTemplateException {
        Elements inputs = document.select("div.fc");
        for(Element el : inputs) {
            el.removeAttr("id");
            prepareColumnModel(el, fields, isAdjustHtml);
        }        
    }
    
    private void prepareColumnModel(Element el, List<PZDocInput> fields, boolean isAdjustHtml) throws ProcessTemplateException {
        Attributes attrs = el.attributes();
        String id = attrs.get("p").trim(), fieldNm = id;
        int tabNum = Integer.valueOf(attrs.get("tabno"));

        if (Pattern.matches("TAB\\d+\\(\\?\\)\\.\\w+", id)) {
            tabNum = Integer.valueOf(RegexpUtl.getMatch(id, "TAB(\\d+)\\(\\?\\)\\.\\w+"));
            fieldNm = RegexpUtl.getMatch(id, "TAB\\d+\\(\\?\\)\\.(\\w+)").trim();
        }

        int pos = getColIndex(fieldNm);
        boolean _isRownum = el.hasClass("field-rownum"), _isReadonly = el.hasClass("field-readonly");
        String _numtype = el.attr("numtype"); _numtype = (Utils.isEmpty(_numtype)) ? "digit" : _numtype;
        
        if (el.parent().tagName().equalsIgnoreCase("td")) {
            Element elTd = el.parent();
            if (elTd.children().size() > 1) { 
                el
                  .addClass("field-parent-scretch")
                  .addClass("field-parent-size-" + elTd.children().size());
            }
        }
        
        if (pos == -1) {
            DMColumn fld = new DMColumn(fieldNm, !_isReadonly, _isRownum, tabNum, true);
            if (el.hasClass("field-formatter-letter") || (_isRownum && !_numtype.equalsIgnoreCase("digit"))) {
                DMColumnUserOptions userOpts = new DMColumnUserOptions();
                userOpts.setFormatterOrder("letter");
                fld.setUserOptions(userOpts);
            }
            
            int posInp = getInputIndex(fieldNm, fields);
            if (posInp != -1) {
                PZDocInput inp = fields.get(posInp);
                prepareField(fld, inp);
                prepareHtmlInput(el, fld, inp, isAdjustHtml);
                
                this.excludeInputs.add(inp);
            } else {
                prepareHtmlInput(el, fld, null, 0);
            }
            
            columns.add(fld);
        } else {
            String newId = fieldNm + "_" + (UUID.randomUUID().toString()).replace("-", "");
            DMColumn fld = new DMColumn(newId, !_isReadonly, _isRownum, tabNum, true);
            fld.setParentNm(fieldNm);
            if (el.hasClass("field-formatter-letter")) {
                DMColumnUserOptions userOpts = new DMColumnUserOptions();
                userOpts.setFormatterOrder("letter");
                fld.setUserOptions(userOpts);
            }
            
            int posInp = getInputIndex(fieldNm, fields);
            if (posInp != -1) {
                PZDocInput inp = fields.get(posInp);
                prepareField(fld, inp);
                prepareHtmlInput(el, fld, inp, isAdjustHtml);                
                
                this.excludeInputs.add(inp);
            } else {
                prepareHtmlInput(el, fld, null, 0);
            }            
            
            columns.add(fld);
            
            el.attr("p", getTabName(fld));

            List<String> children = parentGroups.get(fieldNm);
            if (children == null) { children = new ArrayList<>(); }
            children.add(newId);
            parentGroups.put(fieldNm, children);
        }
    }

    private int getColIndex (String nm) {
        int cnt = 0;
        for(DMColumn col : columns) {
            if (col.getName().equals(nm)) {
               return cnt;
            }
            cnt++;
        }
        return -1;
    }
    
    private int getInputIndex(String nm, List<PZDocInput> fields) {
        int cnt = 0;
        for (PZDocInput inp : fields) {
            if (inp.getField().equalsIgnoreCase(nm)) {
                return cnt;
            }
            cnt++;
        }
        return -1;
    }
    
    private String getTabName(final DMColumn col) {
        return (col.getTabn() == 0) ? col.getName() : "TAB" + col.getTabn() + "(?)." + col.getName();
    }
    
    private void prepareHtmlInput(Element el, final DMColumn col, final PZDocInput inp, boolean isAdjustHtml) {
        int _htmlWidth = 0,
            _size = inp.getSize() != null ? inp.getSize() : 0,
            _maxlength = inp.getMaxlength() != null ? inp.getMaxlength() : 0;
        _htmlWidth = (_size != 0) ? (_size*16) : ((_maxlength != 0) ? _maxlength*16 : 0);
        
        prepareHtmlInput(el, col, inp.getAlign() != null && isAdjustHtml ? inp.getAlign().toString().toLowerCase() : null, isAdjustHtml ? _htmlWidth : 0);
    }
    
    private void prepareHtmlInput(Element el, final DMColumn col, String htmlAlign, int htmlWidth) {
        boolean fndAlign = false, fndWidth = false, isset = false;
        Attributes attrs = el.attributes();
        String _style = attrs.get("style");
        String[] _styleArr = _style.split(";");
        if (_styleArr.length != 0) {
            for(String _styleItm : _styleArr) {
                String[] _prmArr = _styleItm.split(":");
                if (_prmArr.length >= 2) {
                    String _styleNm = _prmArr[0];
                    if (_styleNm.equalsIgnoreCase("text-align")) {
                        fndAlign = true;
                    }
                    if (_styleNm.equalsIgnoreCase("width")) {
                        fndWidth = true;
                    }
                }
            }
        }
        if (!fndAlign) {
            if (!Utils.isEmpty(htmlAlign)) {
                isset = true;
                _style += (!Utils.isEmpty(_style) ? ";":"") + String.format("text-align:%s;", htmlAlign);
            }
        }
        if (!fndWidth) {
            isset = true;
            
            String _styleWidth = "", 
                    _editType = !Utils.isEmpty(col.getEditType()) ? col.getEditType() : "text";
            boolean _isMultiSiblings = el.hasClass("field-parent-scretch");
            if (col.getTabn() != 0) {
                int _w = 0;
                if (_editType.equalsIgnoreCase("checkbox")) {
                    _w = 27;
                } else if (col.getRownum()) {
                    if (_isMultiSiblings) _w = 20;
                } else {
                    if (_isMultiSiblings) {
                        _w = 30;
                    }
                }
                _styleWidth = (_w != 0) ? String.format("width:%dpx!important;", _w) : "width:99%!important;";
            } else {
                int _w = 0;
                if (_editType.equalsIgnoreCase("checkbox")) {
                    _w = 27;
                } else {
                    _w = htmlWidth;
                    if (_w == 0 && _isMultiSiblings) {
                        _w = 30;
                    }
                }
                _styleWidth = (_w != 0) ? String.format("width:%dpx!important;", _w) : "width:99%!important;";
            }
            _style += (!Utils.isEmpty(_style) ? ";":"") + _styleWidth;
        }
        if (isset) {
            el.attr("style", _style);
        }
    }
    
    private void prepareField (DMColumn field, final PZDocInput inp) throws ProcessTemplateException {
        int _decimalPlaces = inp.getDecimalPlaces() != null ? inp.getDecimalPlaces() : 0;
        ParamDataType _dataType = inp.getDataType();
        ParamEditType _editType = inp.getHtmlInputType();

        DMColumnEditRules editRules = new DMColumnEditRules();
        DMColumnEditOptions editOpts = new DMColumnEditOptions();
        DMColumnFormatOptions formatOpts = new DMColumnFormatOptions();
        DMColumnUserOptions userOpts = field.getUserOptions();
        if (userOpts == null)
                userOpts = new DMColumnUserOptions(inp.getHtmlInputType(), inp.getBase(), inp.getType(), _dataType);
        else 
                userOpts.setDefaultValues(inp.getHtmlInputType(), inp.getBase(), inp.getType(), _dataType);
        
            // set editrules.required
        if (field.getTabn() == 0)
                if (inp.getRequired()) editRules.setRequired(true);  
            // column.label
        if (!Utils.isEmpty(inp.getLabel())) field.setLabel(inp.getLabel());
        field.setEditable(inp.getEditable() != null ? inp.getEditable() : false);
        
        if (_dataType == ParamDataType.DATE && _editType == ParamEditType.DICT) {
            String[] items = inp.getHtmlInputList().split(":");
            if (items.length >= 2 || items.length <= 4) {
                userOpts.setDctName(items[0]);
                userOpts.setDctField(items[1]);
                if (items.length == 3) {
                    String val = items[2];
                    if (val.contains("&") || val.contains("^") || val.contains("=")) {
                        userOpts.setDctGroup("*");
                        userOpts.setDctFilter(val);
                    } else {
                        userOpts.setDctGroup(val);
                    }
                } else if (items.length == 4) {
                    userOpts.setDctGroup(items[2]);
                    userOpts.setDctFilter(items[3]);
                }
            }
            userOpts.setEditType("date");
            _editType = ParamEditType.DATE;
        }
        
        switch (_editType) {
            case DICT:
                String[] items = inp.getHtmlInputList().split(":");
                if (items.length >= 2 || items.length <=4) {
                    userOpts.setDctName(items[0]);
                    userOpts.setDctField(items[1]);
                    if (items.length == 3) {
                        String val = items[2];
                        if (val.contains("&") || val.contains("^") || val.contains("=")) {
                            userOpts.setDctGroup("*");
                            userOpts.setDctFilter(val);
                        } else {
                            userOpts.setDctGroup(val);
                        }
                    } else if (items.length == 4) {
                        userOpts.setDctGroup(items[2]);
                        userOpts.setDctFilter(items[3]);
                    } else {
                        userOpts.setDctGroup("*");
                    }
                }
                field.setEditType(ColumnEditType.CUSTOM);
                break;
            case SELECT:
                field.setEditType(ColumnEditType.SELECT);
                field.setFormatter(ColumnFormatter.SELECT);
                editOpts.setValue(inp.getHtmlInputList());
                break;
            case CHECKBOX:
                field.setEditType(ColumnEditType.CHECKBOX);
                field.setFormatter(ColumnFormatter.GRP_CHECKBOX);
                editOpts.setValue(inp.getHtmlInputList());
                break;
            case RADIOBOX:
                field.setEditType(ColumnEditType.CHECKBOX);
                field.setFormatter(ColumnFormatter.GRP_RADIOBOX);
                editOpts.setValue(inp.getHtmlInputList());
                break;
            case DATE:
                field.setDatefmt("d.m.Y");
                editRules.setDate(true);
                break;
            default:
                field.setEditType(ColumnEditType.getObjectByValue(_editType.value()));
                break;
        }
        
        if (_editType != ParamEditType.CHECKBOX && _editType != ParamEditType.RADIOBOX && _editType != ParamEditType.DATE) {
            int _maxlength = inp.getMaxlength() != null ? inp.getMaxlength() : 0;
            if (_maxlength != 0) {
                editOpts.setMaxLength(_maxlength);
            }
            
            if (_dataType == ParamDataType.NUMBER) {
                    editRules.setNumber(true);
                    field.setFormatter(ColumnFormatter.NUMBER);
                if (_decimalPlaces != 0) {
                    formatOpts.setDecimalPlaces(_decimalPlaces);
                    formatOpts.setDecimalSeparator(inp.getDecimalSeparator());
                    formatOpts.setDefaultValue("&nbsp;");
                } else {
                    formatOpts.setDecimalSeparator(inp.getDecimalSeparator());
                    formatOpts.setDefaultValue("&nbsp;");
                }
                    // set editrules.minvlue, editrules.maxvalue
                if (inp.getMinValue() != null) editRules.setMinValue(inp.getMinValue());
                if (inp.getMaxValue() != null) editRules.setMaxValue(inp.getMaxValue());
                    // set DIMROWCOL
                int _dimrowcol = inp.getDimrowcol() != null ? inp.getDimrowcol() : 1;
                if (_dimrowcol != 1) { 
                    userOpts.setDimRowCol(_dimrowcol);
                }                
                    // set userOpts: xmlDecimalSeparator, xmlDecimalPlaces
                int _xmlDecimalPlaces = inp.getXmlDecimalPlaces() != null ? inp.getXmlDecimalPlaces() : 0;
                if (_xmlDecimalPlaces != 0) {
                    userOpts.setDecimalPlaces(_xmlDecimalPlaces);
                    userOpts.setDecimalSeparator(inp.getXmlDecimalSeparator());
                }
            } else if (_dataType == ParamDataType.INTEGER) {
                editRules.setInteger(true);
                field.setFormatter(ColumnFormatter.INTEGER);
                formatOpts.setThousandsSeparator("");
                formatOpts.setDefaultValue("&nbsp;");
                
                if (inp.getMinValue() != null) {
                    editRules.setMinValue(inp.getMinValue());
                }
                if (inp.getMaxValue() != null) {
                    editRules.setMaxValue(inp.getMaxValue());
                }
                    // set editrules.minvlue, editrules.maxvalue
                if (inp.getMinValue() != null) editRules.setMinValue(inp.getMinValue());
                if (inp.getMaxValue() != null) editRules.setMaxValue(inp.getMaxValue());
                    // set DIMROWCOL
                int _dimrowcol = inp.getDimrowcol() != null ? inp.getDimrowcol() : 1;
                if (_dimrowcol != 1) { 
                    userOpts.setDimRowCol(_dimrowcol);
                }                
            }
        }
        field.setEditOptions(editOpts);
        field.setEditRules(editRules);
        field.setFormatOptions(formatOpts);
        field.setUserOptions(userOpts);
        
        this.checkField(field);        
    }
    
    @Override
    public List<DMColumn> getColumnModel() {
        return this.columns;
    }
    
    @Override
    public Map<String, List<String>> getParentGroups() {
        return this.parentGroups;
    }
    
    @Override
    public Map<Integer, List<String>> getChoiceGroups() {
        return this.choiceGroups;
    }

    @Override
    public int getTabCnt() {
        return this.tabCnt;
    } 
    
    @Override
    public String getTmplContent() {
        return tmplContent;
    }
    
    @Override
    public Map<Integer, Map<Integer, List<String>>> getXSDChoiceGroups(byte[] xsdData)  throws ParserConfigurationException, SAXException, IOException {
        Map<Integer, Map<Integer, List<String>>> ret = new HashMap<>();
        Integer cnt = 0;
        
        ByteArrayInputStream dct = new ByteArrayInputStream(xsdData);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();            
        org.w3c.dom.Document document = builder.parse(new InputSource(dct));
        document.getDocumentElement().normalize();

        NodeList list = document.getElementsByTagName("xs:complexType");

        for (int i = 0; i < list.getLength(); i++) {
            Node nd = list.item(i);
            org.w3c.dom.Element node = (org.w3c.dom.Element)nd;

            String nm = node.getAttribute("name"); if (nm.isEmpty()) nm = "?";

            if (nm.equals("DBody")) {
                if (nd.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList lstBodyChild = nd.getChildNodes();
                    for (int tempBody = 0; tempBody < lstBodyChild.getLength(); tempBody++) {
                        Node _ndBodyChild = lstBodyChild.item(tempBody);
                        if (_ndBodyChild.getNodeType() == Node.ELEMENT_NODE) {
                            if (((org.w3c.dom.Element)_ndBodyChild).getNodeName().equals("xs:sequence")) {
                                NodeList lstSeqChild = _ndBodyChild.getChildNodes();
                                for (int temp = 0; temp < lstSeqChild.getLength(); temp++) {
                                    Node _ndSeqChild = lstSeqChild.item(temp);
                                    if (_ndSeqChild.getNodeType() == Node.ELEMENT_NODE) {
                                        if (((org.w3c.dom.Element)_ndSeqChild).getNodeName().equals("xs:choice")) {
                                            ret.put(cnt++, getChoiceList(_ndSeqChild.getChildNodes()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ret;        
    }
    
    private Map<Integer, List<String>> getChoiceList (NodeList lstChild) {
        Map<Integer, List<String>> ret = new HashMap<>();
        Integer cnt = 0;
        boolean isIncl = false;
        
        for (int temp=0; temp<lstChild.getLength(); temp++) {
            Node _nd = lstChild.item(temp);
            if (_nd.getNodeType() == Node.ELEMENT_NODE) {
                if (((org.w3c.dom.Element)_nd).getNodeName().equals("xs:sequence")) {
                    isIncl = true;
                    NodeList _lstChild = _nd.getChildNodes();
                    List<String> lstSeqElements = new ArrayList<>();
                    for (int _temp=0; _temp<_lstChild.getLength(); _temp++) {
                        Node _node1 = _lstChild.item(_temp);
                        if (_node1.getNodeType() == Node.ELEMENT_NODE) {
                            org.w3c.dom.Element _el = (org.w3c.dom.Element)_node1;
                            if (_el.getNodeName().equals("xs:element")) { lstSeqElements.add(_el.getAttribute("name")); }
                        }
                    }
                    ret.put(cnt++, lstSeqElements);
                }else if (((org.w3c.dom.Element)_nd).getNodeName().equals("xs:element")) {
                    List<String> lstSeqElements = new ArrayList<>();
                    org.w3c.dom.Element _el = (org.w3c.dom.Element)_nd;
                    lstSeqElements.add(_el.getAttribute("name"));
                    
                    ret.put(cnt++, lstSeqElements);
                }
            } 
        }
        
        return isIncl? ret : new HashMap<Integer, List<String>>();
    }    
}
