package ua.maestro.lib.templates;

import ua.maestro.lib.templates.mapping.DMColumn;
import ua.maestro.lib.templates.parsers.ParserFmt;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ua.maestro.lib.xmlcore.xml.data.XmlBodyDataRec;
import ua.maestro.lib.xmlcore.xml.data.XmlBodyRec;

/**
 *
 * @author maestro
 */
public class Convert2HTML {
    private ParserFmt parser;
    private Map<String, XmlBodyDataRec> data;
    
    public Convert2HTML(ParserFmt parser, Map<String, XmlBodyDataRec> data) {
        this.parser = parser;
        this.data = data;
    }
    
    public String process() {
        Document document = Jsoup.parse(parser.getTmplContent());
        document.outputSettings().syntax(Document.OutputSettings.Syntax.html);
        document.outputSettings().charset("UTF-8");
        
        int tabCnt = parser.getTabCnt();

        processTab(document);
        for(int ntab = 1; ntab <= tabCnt; ntab++) {
            processTab(document, ntab);
        }
            // Ищем секцию HEAD
        Elements headEls = document.select("head");
        if (headEls != null) {
            Element head = headEls.get(0);
            Elements stylesEls = head.select("link"),
                     scriptsEls = head.select("script");
            for(Element el : stylesEls) {
                el.remove();
            }

            for(Element el : scriptsEls) {
                el.remove();
            }
        }
        return document.toString();
    }
    
    private void processTab(Document document) {
        List<DMColumn> cm = parser.getColumnModel();
        for (DMColumn col : cm) {
            if (col.getTabn() == 0) {
                XmlBodyDataRec rdata = data.get(col.getName());
                String val = rdata != null ? (String)rdata.getValue() : "";

                Elements elements = document.select("div.fc[p=" + col.getIndex() + "]");
                for(Element field : elements) {
                    field
                        .removeAttr("p")
                        .removeAttr("tabno");
                    field.html(val);
                }
            }
        }
    }
    
    private void processTab(Document document, long ntab) {
        List<DMColumn> cm = parser.getColumnModel();
            // Подсчитываем количество строк в таблице
        long rn = 0;
        for (DMColumn col : cm) {
            if (col.getTabn() == ntab) {
                XmlBodyDataRec rdata = data.get(col.getName());
                if (rdata != null) {
                    rn = rdata.getRownum() > rn ? rdata.getRownum() : rn;
                }
            }
        }
            // Очищаем атрибуты
        Elements elements = document.select(".table-template[p=template:TAB" + ntab + "]");
        for (Element table : elements) {
            table.removeAttr("p");
        }
            // Ищем шаблон строки, который будем копировать
        elements = document.select("tr.table-template-row[p=template:TAB" + ntab + "]");
        Element templateRow = elements.get(0);
            // Формируем массив с данными
        Map<String, String> row = new HashMap<>();
        for (long nrow = 1; nrow <= rn; nrow++) {
            for (DMColumn col : cm) {
                if (col.getTabn() == ntab) {
                    row.put(col.getIndex(), "");
                    XmlBodyDataRec rdata = data.get(col.getName());
                    if (rdata != null) {
                        List<XmlBodyRec> fldData = (List<XmlBodyRec>) rdata.getValue();
                        for(XmlBodyRec rec : fldData) {
                            if (rec.getRownum() == nrow) {
                                row.put(col.getIndex(), rec.getValue());
                                break;
                            }
                        }
                    }
                }
            }

            Element newTr = templateRow.clone();
            for (Map.Entry<String, String> entry : row.entrySet()) {
                String fieldNm = entry.getKey(), 
                       fieldVal = entry.getValue(),
                       htmlFieldNm = "TAB" + ntab + "(?)." + fieldNm;
                Elements els = newTr.select("div[p=" + htmlFieldNm + "]");
                if (els != null) {
                    Element field = els.get(0);
                    field
                        .removeAttr("p")
                        .removeAttr("tabno");
                    field.html(fieldVal);
                }
            }
            newTr
                .removeAttr("p")
                .attr("nrow", String.valueOf(nrow));
            templateRow.before(newTr);                    
        }
        templateRow.remove();
    }
}
