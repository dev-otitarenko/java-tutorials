package ua.maestro.lib.templates.parsers;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import ua.maestro.lib.commons.Utils;


/**
 *
 * @author maestro
 */
public class ParserFmtXSL extends ParserFmt {
    protected void parseFmt(Document document) {
        Elements tables = document.select("[id=Process]");
        for(Element el : tables) {
            tabCnt++;
            
            String tabNum = "TAB" + tabCnt;
            el.attr("p", "template:" + tabNum);

            checkTableTemplate(el, tabNum);
        }

        Elements fields = document.select("input");
        for(Element el : fields) {
            prepareInput(el);
        }
    }

    private void prepareInput(Element input) {
        Attributes newAttrs = new Attributes();
        String _id = input.attr("id"), _style = input.attr("style"), _class = input.attr("class");
        if (!Utils.isEmpty(_id)) newAttrs.put("p", _id);
        newAttrs.put("style", "min-height:100%;margin:0;width:100%;word-break:break-all;" + (!Utils.isEmpty(_style) ? _style : ""));
        newAttrs.put("class", (!Utils.isEmpty(_class)) ? "fc " +_class : "fc");
        newAttrs.put("tabno", "0");

        Element div = new Element(Tag.valueOf("div"), "", newAttrs);
        input.replaceWith(div);        
    }
    
    private void prepareTblInputs(Element tr, String tabName) {
        Elements spans = tr.select("span[id=spRownum]");
        for(Element input : spans) {
            Attributes newAttrs = new Attributes();
            String _numtype = input.attr("numtype"), _style = input.attr("style"), _class = input.attr("class"), _prefix = "";
            newAttrs.put("p", tabName + "(?).RN_1");
            
            // prefix = prefix.replaceAll("\t+", " ");
            
            newAttrs.put("numtype", !Utils.isEmpty(_numtype) ? _numtype : "digit");
            if (!Utils.isEmpty(_style)) newAttrs.put("style", _style);
            newAttrs.put("class", (!Utils.isEmpty(_class)) ? "fc field-rownum" +_class : "fc field-rownum");
            if (!Utils.isEmpty(_prefix)) newAttrs.put("prefix", _prefix);
            newAttrs.put("tabno", String.valueOf(tabCnt));
            
            Element div = new Element(Tag.valueOf("div"), "", newAttrs);
            input.replaceWith(div);                    
        }
        
        Elements inputs = tr.select("input");
        for(Element input : inputs) {
            Attributes newAttrs = new Attributes();
            String _id = input.attr("id"), _style = input.attr("style"), _class = input.attr("class");
            if (!Utils.isEmpty(_id)) newAttrs.put("p", tabName + "(?)." + _id);
            newAttrs.put("style", "min-height:100%;margin:0;width:100%;word-break:break-all;" + (!Utils.isEmpty(_style) ? _style : ""));
            newAttrs.put("class", (!Utils.isEmpty(_class)) ? "fc " +_class : "fc");
            newAttrs.put("tabno", String.valueOf(tabCnt));
            
            Element div = new Element(Tag.valueOf("div"), "", newAttrs);
            input.replaceWith(div);
        }
    }
    
    private void checkTableTemplate(Element tbl, String tabName) {
        tbl.attr("id", "Template_" + tabName);

        Elements tmplTr = tbl.select("tr[id=StretchTable]");
        for(Element tr : tmplTr) {
            tr
              .removeAttr("id")
              .removeAttr("title")
              .removeAttr("rownum")
              .attr("p", "template:" + tabName);
            tr
              .addClass("dp00-tblrow")
              .addClass("table-template-row");
            
            prepareTblInputs(tr, tabName);
        }
    }
}
