package ua.maestro.lib.templates.parsers;

import java.util.regex.Pattern;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import ua.maestro.lib.commons.utils.RegexpUtl;


/**
 *
 * @author maestro
 */
public class ParserFmtHTML extends ParserFmt {
    protected void parseFmt(Document document) {
        Elements tables = document.select(".table-template");
        for (Element el : tables) {
            tabCnt++;

            String tabNum = "TAB" + tabCnt;
            el.attr("p", "template:" + tabNum);

            checkTableTemplate(el, tabNum);
        }

        Elements fields = document.select("div.field");
        for(Element el : fields) {
            el.removeClass("field");
            prepareInput(el);
        }
    }
    
    private void checkTableTemplate(Element tbl, String tabName) {
        Elements rows = tbl.select("tr.table-template-row");
        for(Element tr : rows) {
            Elements inputs = tr.select("div.field");
            for(Element inp : inputs){
                String p = inp.attributes().get("p");
                inp.attr("p", tabName + "(?)." + p);
            }
            tr.attr("p", "template:" + tabName);
        }
    }
    
    private void prepareInput(Element el) {
        Attributes attrs = el.attributes(), newAttrs = new Attributes();
        String tabNum = "0", id = attrs.get("p");
        boolean isExistStyle = false, isExistCss = false;
        for (Attribute attr : attrs) {
            if (attr.getKey().equalsIgnoreCase("style")) {
                isExistStyle = true;
                //String _style = attr.getValue();
                //if (_style.contains("width:"))
                    newAttrs.put("style", attr.getValue());
                //else
                //    newAttrs.put("style", "min-height:100%;width:100%;" + attr.getValue());
            } else if (attr.getKey().equalsIgnoreCase("len")) {
                newAttrs.put(attr);
            } else if (attr.getKey().equalsIgnoreCase("class")) {
                isExistCss = true;
                newAttrs.put("class", "fc " + attr.getValue());
            } else if (attr.getKey().equalsIgnoreCase("numtype") || attr.getKey().equalsIgnoreCase("dependingon")) {
                newAttrs.put(attr);
            } 
        }
       // if (!isExistStyle) { newAttrs.put("style", "min-height:100%;width:100%;"); }
        if (!isExistCss) { newAttrs.put("class", "fc"); }

        // String fieldNm = id;
        if (Pattern.matches("TAB\\d+\\(\\?\\)\\.\\w+", id)) {
            tabNum = RegexpUtl.getMatch(id, "TAB(\\d+)\\(\\?\\)\\.\\w+");
           // fieldNm = RegexpUtl.getMatch(id, "TAB\\d+\\(\\?\\)\\.(\\w+)");
        }
        newAttrs.put("p", id);
        newAttrs.put("id", id);
        newAttrs.put("tabno", tabNum);

        el.replaceWith(new Element(Tag.valueOf("div"), "", newAttrs));
    }
}
